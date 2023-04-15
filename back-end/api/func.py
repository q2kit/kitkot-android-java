from django.core.cache import cache

from mailer import Mailer

import requests
import os
import random
import re
import jwt
import datetime

from api.models import User


def validate_phone_number(phone_number):
    if re.match(r"^0\d{9}$", phone_number):
        return True, ""
    else:
        return False, "Invalid phone number, should be 10 digits and start with 0"


def validate_email(email):
    if re.match(r"^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$", email):
        return True, ""
    else:
        return False, "Invalid email"


def validate_username(username):
    if re.match(r"^[a-zA-Z0-9_]{3,}$", username):
        return True, ""
    else:
        return False, "Invalid username, should be at least 3 characters and only contain letters, numbers and _"


def validate_name(name):
    if re.match(r"^[a-zA-Z0-9_ ]+$", name):
        return True, ""
    else:
        return False, "Invalid name, should only contain letters, numbers and _"


def send_sms_otp(phone_number):
    success, message = validate_phone_number(phone_number)
    if not success:
        return (False, message)
    
    if cache.get(phone_number):
        return (False, "OTP already sent, you can resend in 5 minutes")

    otp = str(random.randint(100000, 999999))
    cache.set(phone_number, otp, 600)

    SMS_OTP_TOKEN = os.environ.get("SMS_OTP_TOKEN")

    url = f"https://api.speedsms.vn/index.php/sms/send?access-token={SMS_OTP_TOKEN}&to={phone_number}&content={otp}&sms-type=4"
    response = requests.get(url).json()

    if response["status"] == "success":
        return (True, "OTP sent")
    else:
        return (False, response["message"])


def send_mail_otp(email):
    success, message = validate_email(email)
    if not success:
        return (False, message)

    if cache.get(email):
        return (False, "OTP already sent, you can resend in 5 minutes")

    otp = str(random.randint(100000, 999999))
    cache.set(email, otp)

    MAIL_SENDER = os.environ.get("MAIL_SENDER")
    MAIL_SENDER_PASS = os.environ.get("MAIL_SENDER_PASS")
    SENDER_NAME = "KitKot Team"
    SUBJECT = "KitKot - OTP"
    MESSAGE = f"""
        <html>
            <head></head>
            <body style="font-size: 16px">
                <p>Your OTP code is: <b>{otp}</b>.</p>
                <p>Please use this code to reset your password.</p>
                <br>
                <p style="font-size: 13px"><i>If you did not request this, please ignore this email. Thank you :)</i></p>
            </body>
        </html>
    """

    try:
        mail = Mailer(email=MAIL_SENDER, password=MAIL_SENDER_PASS)
        mail.settings(provider=mail.GMAIL)
        mail.send(
            sender_name=SENDER_NAME,
            receiver=email,
            subject=SUBJECT,
            message=MESSAGE,
        )
        return (True, "OTP sent")
    except Exception as e:
        return (False, str(e))


def verify_otp(email_or_phone, otp):
    if cache.get(email_or_phone) == otp:
        cache.delete(email_or_phone)
        return True
    else:
        return False


def generate_access_token(uid):
    SECRET_KEY = os.environ.get("SECRET_KEY")
    return jwt.encode({
        "uid": uid,
        "exp": datetime.datetime.utcnow() + datetime.timedelta(days=30),
        "iat": datetime.datetime.utcnow(),
    }, SECRET_KEY, algorithm="HS256")