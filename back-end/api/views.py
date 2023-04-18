from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from django.db.models import Q
from django.core.cache import cache

from api.models import (
    User
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
)

SECRET_KEY = os.environ.get("SECRET_KEY")


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
            "user": user.to_json()
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
            user = User.objects.get(Q(username=account) | Q(
                email=account) | Q(phone=account))
        except User.DoesNotExist:
            return JsonResponse({
                "success": False,
                "message": "User not found"
            })

        if user.password == hashlib.sha512((password + SECRET_KEY).encode("utf-8")).hexdigest():
            return JsonResponse({
                "success": True,
                "message": "User logged in",
                "token": generate_access_token(user.pk),
                "user": user.to_json()
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
                user = User.objects.get(email=email)
                return JsonResponse({
                    "success": True,
                    "message": "User logged in",
                    "token": generate_access_token(user.pk),
                    "user": user.to_json()
                })
            
            except User.DoesNotExist: # register
                username = email.split("@")[0]
                name = idinfo["name"]
                avatar = idinfo["picture"]
                
                user = User.objects.create(
                    username=username,
                    email=email,
                    name=name,
                    avatar=avatar,
                )
                return JsonResponse({
                    "success": True,
                    "message": "User created",
                    "token": generate_access_token(user.pk),
                    "user": user.to_json()
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
