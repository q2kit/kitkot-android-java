from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from django.db.models import Q, Exists, OuterRef, Count, F
from django.core.cache import cache

from api.models import (
    User,
    Video,
    Comment,
    Watched,
)

import hashlib
import os
import threading
from google.oauth2 import id_token
from google.auth.transport import requests

from api.func import (
    validate_email,
    validate_phone_number,
    validate_username,
    validate_name,
    generate_access_token,
    send_sms_otp,
    send_mail_otp,
    verify_otp,
    verify_access_token,
    upload_video_to_s3,
    upload_video_to_s3_test,
)

SECRET_KEY = os.environ.get("SECRET_KEY")
DEFAULT_AVATAR = os.environ.get("DEFAULT_AVATAR")


@csrf_exempt
def register(request):
    if request.method == "POST":
        try:
            username = request.POST["username"].lower().strip()
            password = request.POST["password"]
            email = request.POST["email"].lower().strip()
            phone = request.POST["phone"].strip()
            name = request.POST["name"].strip()
        except Exception:
            return JsonResponse({
                "success": False,
                "message": "Fill all fields"
            })

        validator = [
            validate_username(username),
            validate_email(email),
            validate_phone_number(phone),
            validate_name(name)
        ]

        for success, message in validator:
            if not success:
                return JsonResponse({
                    "success": False,
                    "message": message
                })

        check_exists = [
            (User.objects.filter(username=username).exists(),
             "User with this username already exists"),
            (User.objects.filter(email=email).exists(),
             "User with this email already exists"),
            (User.objects.filter(phone=phone).exists(),
             "User with this phone already exists")
        ]

        for exists, message in check_exists:
            if exists:
                return JsonResponse({
                    "success": False,
                    "message": message
                })

        user = User.objects.create(
            username=username,
            password=hashlib.sha512(
                (password + SECRET_KEY).encode("utf-8")).hexdigest(),
            email=email,
            phone=phone,
            name=name,
        )

        return JsonResponse({
            "success": True,
            "message": "User created",
            "token": generate_access_token(user.pk),
        })

    return JsonResponse({
        "success": False,
        "message": "Method not allowed"
    })


@csrf_exempt
def login(request):
    if request.method == "POST":
        try:
            account = request.POST["account"].lower().strip()
            password = request.POST["password"]
        except KeyError:
            return JsonResponse({
                "success": False,
                "message": "Fill all fields"
            })

        try:
            user = User.objects.annotate(
                liked=Count("watched", filter=Q(watched__liked=True))
            ).get(Q(username=account) | Q(
                email=account) | Q(phone=account))
        except User.DoesNotExist:
            return JsonResponse({
                "success": False,
                "message": "User not found"
            })

        userJSON = {
            "uid": user.pk,
            "username": user.username,
            "email": user.email,
            "phone": user.phone,
            "name": user.name,
            "avatar": user.avatar or DEFAULT_AVATAR,
            "followers": user.followers.count(),
            "following": user.following.count(),
            "liked": user.liked,
        }

        if user.password == hashlib.sha512((password + SECRET_KEY).encode("utf-8")).hexdigest():
            return JsonResponse({
                "success": True,
                "message": "User logged in",
                "token": generate_access_token(user.pk),
                "user": userJSON
            })
        else:
            return JsonResponse({
                "success": False,
                "message": "Wrong password"
            })

    return JsonResponse({
        "success": False,
        "message": "Method not allowed"
    })


@csrf_exempt
def google_auth(request):
    if request.method == "POST":
        try:
            g_token = request.POST["g_token"].strip()
        except KeyError:
            return JsonResponse({
                "success": False,
                "message": "Fill all fields"
            })

        try:
            G_CLIENT_ID = os.environ.get("G_CLIENT_ID")
            idinfo = id_token.verify_oauth2_token(g_token, requests.Request(), G_CLIENT_ID)
            email = idinfo["email"]
            try: # login
                user = User.objects.annotate(
                    liked=Count("watched", filter=Q(watched__liked=True))
                ).get(email=email)
                userJSON = {
                    "uid": user.pk,
                    "username": user.username,
                    "email": user.email,
                    "phone": user.phone,
                    "name": user.name,
                    "avatar": user.avatar or DEFAULT_AVATAR,
                    "followers": user.followers.count(),
                    "following": user.following.count(),
                    "liked": user.liked,
                }
                return JsonResponse({
                    "success": True,
                    "message": "User logged in",
                    "token": generate_access_token(user.pk),
                    "user": userJSON
                })
            
            except User.DoesNotExist: # register
                username = email.split("@")[0]
                # name = idinfo["name"]
                name = idinfo.get("family_name", "") + " " + idinfo.get("given_name", "")
                avatar = idinfo["picture"]
                
                user = User.objects.create(
                    username=username,
                    email=email,
                    name=name,
                    avatar=avatar,
                )
                userJSON = {
                    "uid": user.pk,
                    "username": user.username,
                    "email": user.email,
                    "phone": user.phone,
                    "name": user.name,
                    "avatar": user.avatar or DEFAULT_AVATAR,
                }
                return JsonResponse({
                    "success": True,
                    "message": "User created",
                    "token": generate_access_token(user.pk),
                    "user": userJSON
                })
        except Exception as e:
            print("Error:")
            print(e)
            return JsonResponse({
                "success": False,
                "message": "Wrong token"
            })
    return JsonResponse({
        "success": False,
        "message": "Method not allowed"
    })


@csrf_exempt
def reset_password(request):
    if request.method == "POST":
        try:
            email_or_phone = request.POST["email_or_phone"].lower().strip()
        except KeyError:
            return JsonResponse({
                "success": False,
                "message": "Fill all fields"
            })

        if "otp" in request.POST:  # step 2
            otp = request.POST["otp"]
            if verify_otp(email_or_phone, otp):
                try:
                    new_password = request.POST["new_password"]
                    user = User.objects.get(Q(email=email_or_phone) | Q(phone=email_or_phone))
                    user.password = hashlib.sha512((new_password + SECRET_KEY).encode("utf-8")).hexdigest()
                    user.save()
                    return JsonResponse({
                        "success": True,
                        "message": "Password changed"
                    })
                except KeyError:
                    return JsonResponse({
                        "success": False,
                        "message": "Fill all fields"
                    })
            else:
                return JsonResponse({
                    "success": False,
                    "message": "Wrong OTP"
                })

        else:  # step 1
            try:
                user = User.objects.get(Q(email=email_or_phone) | Q(phone=email_or_phone))
            except User.DoesNotExist:
                return JsonResponse({
                    "success": False,
                    "message": "User not found with this email or phone"
                })
            
            if cache.get(email_or_phone):
                return JsonResponse({
                    "success": False,
                    "message": "OTP already sent. If you don't receive it, try again in 5 minutes."
                })
            
            if user.email == email_or_phone:
                t = threading.Thread(target=send_mail_otp, args=(user.email,))
                t.start()
            else:
                t = threading.Thread(target=send_sms_otp, args=(user.phone,))
                t.start()
            
            return JsonResponse({
                "success": True,
                "message": "OTP sent. If you don't receive it, try again in 5 minutes."
            })
        
    return JsonResponse({
        "success": False,
        "message": "Method not allowed"
    })


@csrf_exempt
def post_video(request):
    if request.method == "POST":
        try:
            token = request.headers.get("Authorization").split("Bearer ")[1]
            uid = verify_access_token(token)
            user = User.objects.get(pk=uid)
        except User.DoesNotExist:
            return JsonResponse({
                "success": False,
                "message": "Access token is invalid"
            })
        
        try:
            description = request.POST["description"]
            video = request.FILES["video"]
        except KeyError:
            return JsonResponse({
                "success": False,
                "message": "Fill all fields"
            })

        content_type_allow_list = [
            "video/mp4",
        ]

        if video.content_type not in content_type_allow_list:
            return JsonResponse({
                "success": False,
                "message": "File type not allowed, just " + ", ".join(content_type_allow_list)
            })

        try:
            # link = upload_video_to_s3(video)
            link = upload_video_to_s3_test(video)
        except Exception:
            return JsonResponse({
                "success": False,
                "message": "Error uploading video"
            })

        video = Video.objects.create(
            owner=user,
            description=description,
            link=link,
        )

        return JsonResponse({
            "success": True,
            "message": "Video posted",
        })

    return JsonResponse({
        "success": False,
        "message": "Method not allowed"
    })


def get_videos(request):
    try:
        token = request.headers.get("Authorization").split("Bearer ")[1]
        uid = verify_access_token(token)
        user = User.objects.get(pk=uid)
    except Exception:
        return JsonResponse({
            "success": False,
            "message": "Access token is invalid"
        })

    watched = Watched.objects.filter(user=user).values_list("video_id", flat=True)
    videos = Video.objects.exclude(pk__in=watched).order_by("-id").annotate(
        owner_name=F("owner__name"),
        owner_avatar=F("owner__avatar"),
        is_followed=Exists(user.following.filter(pk=OuterRef("owner_id"))),
        is_liked=Exists(Watched.objects.filter(user=user, video=OuterRef("pk"), liked=True)),
        liked=Count('watched', filter=Q(watched__liked=True)),
    ).values(
        "id",
        "description",
        "link",
        "owner_id",
        "owner_name",
        "owner_avatar",
        "is_followed",
        "is_liked",
        "liked",
        "watched",
    )

    return JsonResponse({
        "success": True,
        "videos": list(videos),
    })


def get_videos_by_owner(request, owner_id):
    try:
        token = request.headers.get("Authorization").split("Bearer ")[1]
        uid = verify_access_token(token)
        user = User.objects.get(pk=uid)
    except Exception:
        return JsonResponse({
            "success": False,
            "message": "Access token is invalid"
        })

    try:
        owner = User.objects.get(pk=owner_id)
    except User.DoesNotExist:
        return JsonResponse({
            "success": False,
            "message": "User not found"
        })
    
    videos = owner.videos.all().order_by("-id").annotate(
        is_liked=Exists(Watched.objects.filter(user=user, video=OuterRef("pk"), liked=True)),
        liked=Count('watched', filter=Q(watched__liked=True)),
    ).values(
        "id",
        "description",
        "link",
        "is_liked",
        "liked",
        "watched",
    )
    return JsonResponse({
        "success": True,
        "videos": list(videos)
    })


def like_toggle(request):
    try:
        token = request.headers.get("Authorization").split("Bearer ")[1]
        uid = verify_access_token(token)
        user = User.objects.get(pk=uid)
    except User.DoesNotExist:
        return JsonResponse({
            "success": False,
            "message": "Access token is invalid"
        })
    
    try:
        video_id = request.POST["video_id"]
        video = Video.objects.get(pk=video_id)
    except Exception:
        return JsonResponse({
            "success": False,
            "message": "Video not found"
        })
    
    if video.owner == user:
        return JsonResponse({
            "success": False,
            "message": "You can't like your own video"
        })
    
    watched = Watched.objects.get_or_create(user=user, video=video)[0] # [0] is the object, [1] is a boolean
    watched.liked = not watched.liked
    watched.save()

    return JsonResponse({
        "success": True,
        "message": "OK",
        "liked": watched.liked
    })


def follow_toggle(request):
    try:
        token = request.headers.get("Authorization").split("Bearer ")[1]
        uid = verify_access_token(token)
        user = User.objects.get(pk=uid)
    except User.DoesNotExist:
        return JsonResponse({
            "success": False,
            "message": "Access token is invalid"
        })
    
    try:
        other_user_id = request.POST["other_user_id"]
        other_user = User.objects.get(pk=other_user_id)
    except Exception:
        return JsonResponse({
            "success": False,
            "message": "User not found"
        })
    
    if other_user == user:
        return JsonResponse({
            "success": False,
            "message": "You can't follow yourself"
        })
    
    if other_user in user.following.all():
        user.following.remove(other_user)
        followed = False
    else:
        user.following.add(other_user)
        followed = True

    return JsonResponse({
        "success": True,
        "message": "OK",
        "followed": followed
    })


