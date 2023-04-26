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
    

class Video(models.Model):
    owner = models.ForeignKey(User, on_delete=models.CASCADE)
    description = models.CharField(max_length=1000)
    link = models.CharField(max_length=255)

    def __str__(self):
        return self.id + " - " + self.owner.name
    
    def to_json(self):
        return {
            "id": self.id,
            "owner": {
                "username": self.owner.username,
                "name": self.owner.name,
                "avatar": self.owner.avatar if self.owner.avatar else DEFAULT_AVATAR,
            },
            "description": self.description,
            "link": self.link
        }
    