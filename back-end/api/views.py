from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from django.db.models import Q, Exists, OuterRef, Count, F
from django.core.cache import cache

from api.models import (
    User,
    Video,
    Comment,
    Watched,
    Message,
)

from api.decorator import auth_pass

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
    upload_video_to_s3,
    upload_file_to_cloud,
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
        
        userJSON = {
            "uid": user.pk,
            "username": user.username,
            "email": user.email,
            "phone": user.phone,
            "name": user.name,
            "avatar": user.avatar or DEFAULT_AVATAR,
            "followers": 0,
            "following": 0,
            "liked": 0,
            "videos": 0,
            "is_premium": user.is_premium,
        }

        return JsonResponse({
            "success": True,
            "message": "User created",
            "token": generate_access_token(user.pk),
            "user": userJSON
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
            "is_premium": user.is_premium,
            "videos": user.videos.count(),
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
                    "is_premium": user.is_premium,
                    "videos": user.videos.count(),
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
                    "followers": 0,
                    "following": 0,
                    "liked": 0,
                    "videos": 0,
                    "is_premium": False,

                }
                return JsonResponse({
                    "success": True,
                    "message": "User created",
                    "token": generate_access_token(user.pk),
                    "user": userJSON
                })
        except Exception as e:
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
@auth_pass(["POST", "GET"])
def edit_profile(request):
    if request.method == "POST":
        try:
            password = request.POST["password"]
            name = request.POST.get("name", "").strip()
            username = request.POST.get("username", "").strip()
            email = request.POST.get("email", "").strip()
            phone = request.POST.get("phone", "").strip()
            avatar = request.FILES.get("avatar", None)
            new_password = request.POST.get("new_password", "").strip()
        except KeyError:
            return JsonResponse({
                "success": False,
                "message": "Enter your password"
            })      

        validator = [
            validate_username(username) if username else (True, ""),
            validate_email(email) if email else (True, ""),
            validate_phone_number(phone) if phone else (True, ""),
            validate_name(name) if name else (True, ""),
        ]

        for success, message in validator:
            if not success:
                return JsonResponse({
                    "success": False,
                    "message": message
                })

        check_exists = [
            (User.objects.filter(username=username).exclude(pk=request.user.pk).exists(),
             "User with this username already exists"),
            (User.objects.filter(email=email).exclude(pk=request.user.pk).exists(),
             "User with this email already exists"),
            (User.objects.filter(phone=phone).exclude(pk=request.user.pk).exists(),
             "User with this phone already exists")
        ]

        for exists, message in check_exists:
            if exists:
                return JsonResponse({
                    "success": False,
                    "message": message
                })

        
        if hashlib.sha512((password + SECRET_KEY).encode("utf-8")).hexdigest() != request.user.password:
            return JsonResponse({
                "success": False,
                "message": "Wrong password"
            })
        
        if new_password:
            request.user.password = hashlib.sha512((new_password + SECRET_KEY).encode("utf-8")).hexdigest()

        if name:
            request.user.name = name
        if username:
            request.user.username = username
        if email:
            request.user.email = email
        if phone:
            request.user.phone = phone
        if avatar:
            request.user.avatar = upload_file_to_cloud(avatar)
        request.user.save()
        return JsonResponse({
            "success": True,
            "message": "User info updated"
        })
    elif request.method == "GET":
        return JsonResponse({
            "success": True,
            "user": {
                "name": request.user.name,
                "username": request.user.username,
                "email": request.user.email,
                "phone": request.user.phone,
                "avatar": request.user.avatar
            }
        })


@csrf_exempt
@auth_pass(["GET"])
def get_user_info(request, uid):
    try:
        user = User.objects.get(pk=uid)
    except User.DoesNotExist:
        return JsonResponse({
            "success": False,
            "message": "User not found"
        })
    likes = sum([
        video.likes_count for video in user.videos.all()
    ])
    return JsonResponse({
        "success": True,
        "user": {
            "name": user.name,
            "username": user.username,
            "email": user.email,
            "phone": user.phone,
            "avatar": user.avatar or DEFAULT_AVATAR,
            "is_premium": user.is_premium,
            "videos": user.videos.count(),
            "followers": user.followers.count(),
            "following": user.following.count(),
            "likes": likes
        }
    })


@csrf_exempt
@auth_pass(["POST"])
def post_video(request):
    try:
        description = request.POST["description"]
        is_premium = request.POST.get("is_premium") == "true"
        video = request.FILES["video"]
    except KeyError:
        return JsonResponse({
            "success": False,
            "message": "Fill all fields"
        }, status=400)

    content_type_allow_list = [
        "video/mp4",
    ]

    if video.content_type not in content_type_allow_list:
        return JsonResponse({
            "success": False,
            "message": "File type not allowed, just " + ", ".join(content_type_allow_list)
        }, status=400)


    try:
        # link = upload_video_to_s3(video)
        link = upload_file_to_cloud(video)
    except Exception:
        return JsonResponse({
            "success": False,
            "message": "Error uploading video"
        })

    video = Video.objects.create(
        owner=request.user,
        description=description,
        link=link,
        is_premium=is_premium,
    )

    return JsonResponse({
        "success": True,
        "message": "Video posted",
    })


@csrf_exempt
@auth_pass(["GET"])
def get_videos(request):
    watched = Watched.objects.filter(user=request.user).values_list("video_id", flat=True)
    videos = Video.objects.exclude(pk__in=watched).order_by("-id").annotate(
        owner_name=F("owner__name"),
        owner_avatar=F("owner__avatar"),
        owner_preminum=F("owner__is_premium"),
        is_followed=Exists(request.user.following.filter(pk=OuterRef("owner_id"))),
        is_liked=Exists(Watched.objects.filter(user=request.user, video=OuterRef("pk"), liked=True)),
        liked=Count('watched', filter=Q(watched__liked=True)),
        watched_count=Count('watched'),
        comment=Count('comments'),
    ).values(
        "id",
        "description",
        "link",
        "owner_id",
        "owner_name",
        "owner_avatar",
        "owner_preminum",
        "is_followed",
        "is_liked",
        "is_premium",
        "liked",
        "watched_count",
        "comment"
    )
    if not request.user.is_premium:
        videos = videos.filter(is_premium=False)

    return JsonResponse({
        "success": True,
        "videos": list(videos),
    })


@csrf_exempt
@auth_pass(["GET"])
def get_videos_by_owner(request, owner_id):
    try:
        owner = User.objects.get(pk=owner_id)
    except User.DoesNotExist:
        return JsonResponse({
            "success": False,
            "message": "User not found"
        })
    
    videos = owner.videos.all().order_by("-id").annotate(
        is_liked=Exists(Watched.objects.filter(user=request.user, video=OuterRef("pk"), liked=True)),
        liked=Count('watched', filter=Q(watched__liked=True)),
        watched_count=Count('watched'),
    ).values(
        "id",
        "description",
        "link",
        "is_liked",
        "liked",
        "watched_count",
        "is_premium",
    )
    if not request.user.is_premium:
        videos = videos.filter(is_premium=False)

    return JsonResponse({
        "success": True,
        "videos": list(videos)
    })


@csrf_exempt
@auth_pass(["POST"])
def like_toggle(request):
    try:
        video_id = request.POST["video_id"]
        video = Video.objects.get(pk=video_id)
    except Exception:
        return JsonResponse({
            "success": False,
            "message": "Video not found"
        })
    
    if video.owner == request.user:
        return JsonResponse({
            "success": False,
            "message": "You can't like your own video"
        })
    
    watched = Watched.objects.get_or_create(user=request.user, video=video)[0] # [0] is the object, [1] is a boolean
    watched.liked = not watched.liked
    watched.save()

    return JsonResponse({
        "success": True,
        "message": "OK",
        "liked": watched.liked
    })


@csrf_exempt
@auth_pass(["POST"])
def follow_toggle(request):    
    try:
        other_user_id = request.POST["other_user_id"]
        other_user = User.objects.get(pk=other_user_id)
    except Exception:
        return JsonResponse({
            "success": False,
            "message": "User not found"
        })
    
    if other_user == request.user:
        return JsonResponse({
            "success": False,
            "message": "You can't follow yourself"
        })
    
    if other_user in request.user.following.all():
        request.user.following.remove(other_user)
        followed = False
    else:
        request.user.following.add(other_user)
        followed = True

    return JsonResponse({
        "success": True,
        "message": "OK",
        "followed": followed
    })


@csrf_exempt
@auth_pass(["POST"])
def post_comment(request):        
    try:
        video_id = request.POST["video_id"]
        video = Video.objects.get(pk=video_id)
        content = request.POST["content"]
    except Exception:
        return JsonResponse({
            "success": False,
            "message": "Invalid video or content"
        })
    
    if not Watched.objects.filter(user=request.user, video=video).exists():
        return JsonResponse({
            "success": False,
            "message": "You must watch the video before commenting"
        })

    Comment.objects.create(
        owner=request.user,
        video=video,
        content=content,
    )

    return JsonResponse({
        "success": True,
        "message": "Comment posted",
    })


@csrf_exempt
@auth_pass(["GET"])
def get_comments(request, video_id):
    try:
        video = Video.objects.get(pk=video_id)
    except Video.DoesNotExist:
        return JsonResponse({
            "success": False,
            "message": "Video not found"
        })
    
    comments = video.comments.all().order_by("-id").annotate(
        owner_name=F("owner__name"),
        owner_avatar=F("owner__avatar"),
    ).values(
        "content",
        "owner_name",
        "owner_avatar",
    )

    return JsonResponse({
        "success": True,
        "comments": list(comments)
    })


@csrf_exempt
@auth_pass(["POST"])
def send_message(request):
    try:
        receiver = User.objects.get(pk=request.POST["receiver_id"])
        content = request.POST["content"]
    except Exception:
        return JsonResponse({
            "success": False,
            "message": "Invalid receiver or content"
        })
    
    Message.objects.create(
        sender=request.user,
        receiver=receiver,
        content=content,
    )

    return JsonResponse({
        "success": True,
        "message": "Message sent",
    })


@csrf_exempt
@auth_pass(["GET"])
def get_messages(request, receiver_id):
    try:
        receiver = User.objects.get(pk=receiver_id)
    except Exception:
        return JsonResponse({
            "success": False,
            "message": "Invalid receiver"
        })
    
    messages = Message.objects.filter(
        Q(sender=request.user, receiver=receiver) | Q(sender=receiver, receiver=request.user)
    ).order_by("id").values(
        "content",
        "sender_id",
        "created_at",
    )

    messages = [
        {
            "content": message["content"],
            "created_by_self": message["sender_id"] == request.user.id,
            "created_at": message["created_at"].strftime("%d/%m/%Y %H:%M:%S"),
        }
        for message in messages
    ]

    return JsonResponse({
        "success": True,
        "messages": messages
    })
