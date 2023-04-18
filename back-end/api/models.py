from django.db import models

import os

DEFAULT_AVATAR = os.environ.get("DEFAULT_AVATAR")


class User(models.Model):
    username = models.CharField(max_length=255, null=True, blank=True)
    password = models.CharField(max_length=255)
    name = models.CharField(max_length=255)
    email = models.EmailField(max_length=255)
    phone = models.CharField(max_length=255, null=True, blank=True)
    avatar = models.CharField(max_length=255, null=True, blank=True)
    followers = models.ManyToManyField("self", symmetrical=False, related_name="following", blank=True)

    def __str__(self):
        return self.name
    
    def to_json(self):
        return {
            "uid": self.pk,
            "name": self.name,
            "email": self.email,
            "phone": self.phone,
            "avatar": self.avatar if self.avatar else DEFAULT_AVATAR,
            "followers": list(self.followers.all().values_list("pk", flat=True)),
            "following": list(self.following.all().values_list("pk", flat=True)),
        }