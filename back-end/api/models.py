from django.db import models

import os

DEFAULT_AVATAR = os.environ.get("DEFAULT_AVATAR")


class User(models.Model):
    username = models.CharField(max_length=255)
    password = models.CharField(max_length=255)
    name = models.CharField(max_length=255)
    email = models.EmailField(max_length=255)
    phone = models.CharField(max_length=255)
    avatar = models.CharField(max_length=255)

    def __str__(self):
        return self.name + "(" + self.username + ")"
    
    def to_json(self):
        return {
            "username": self.username,
            "name": self.name,
            "email": self.email,
            "phone": self.phone,
            "avatar": self.avatar if self.avatar else DEFAULT_AVATAR
        }