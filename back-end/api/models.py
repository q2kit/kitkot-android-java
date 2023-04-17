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
    followers = models.ManyToManyField("self", symmetrical=False, related_name="following")

    def __str__(self):
        return self.name + "(" + self.pk + ")"
    
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
    

class Video(models.Model):
    owner = models.ForeignKey(User, on_delete=models.CASCADE, related_name="videos")
    description = models.CharField(max_length=1000)
    link = models.CharField(max_length=255)
    
    def __str__(self):
        return self.id + " - " + self.owner.name
    
    def to_json(self):
        return {
            "id": self.id,
            "owner": {
                "uid": self.owner.pk,
                "name": self.owner.name,
                "avatar": self.owner.avatar if self.owner.avatar else DEFAULT_AVATAR,
            },
            "description": self.description,
            "link": self.link
        }
    

class Comment(models.Model):
    owner = models.ForeignKey(User, on_delete=models.CASCADE)
    video = models.ForeignKey(Video, on_delete=models.CASCADE)
    content = models.CharField(max_length=1000)

    def __str__(self):
        return self.id + " - " + self.owner.name
    
    def to_json(self):
        return {
            "id": self.id,
            "owner": {
                "uid": self.owner.pk,
                "name": self.owner.name,
                "avatar": self.owner.avatar if self.owner.avatar else DEFAULT_AVATAR,
            },
            "video": self.video.id,
            "content": self.content
        }
    

class Watched(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    video = models.ForeignKey(Video, on_delete=models.CASCADE)
    liked = models.BooleanField(default=False)

    def __str__(self):
        return self.user.name + " - " + self.video.id
    
    def to_json(self):
        return {
            "id": self.id,
            "user": self.user.pk,
            "video": self.video.id,
            "liked": self.liked
        }