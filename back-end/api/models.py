from django.db import models


class User(models.Model):
    username = models.CharField(max_length=255, null=True, blank=True)
    password = models.CharField(max_length=255)
    name = models.CharField(max_length=255)
    email = models.EmailField(max_length=255)
    phone = models.CharField(max_length=255, null=True, blank=True)
    avatar = models.CharField(max_length=255, null=True, blank=True)
    following = models.ManyToManyField("self", symmetrical=False, related_name="followers", blank=True)
    is_premium = models.BooleanField(default=False)

    def __str__(self):
        return self.name
    

class Video(models.Model):
    owner = models.ForeignKey(User, on_delete=models.CASCADE, related_name="videos")
    description = models.CharField(max_length=1000)
    link = models.CharField(max_length=255)
    created_at = models.DateTimeField(auto_now_add=True)
    thumbnail = models.CharField(max_length=255, null=True, blank=True)
    is_premium = models.BooleanField(default=False)
    
    def __str__(self):
        return str(self.id) + " - " + self.owner.name
    

class Comment(models.Model):
    owner = models.ForeignKey(User, on_delete=models.CASCADE)
    video = models.ForeignKey(Video, on_delete=models.CASCADE, related_name="comments")
    content = models.CharField(max_length=1000)

    def __str__(self):
        return str(self.id) + " - " + self.owner.name
    

class Watched(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    video = models.ForeignKey(Video, on_delete=models.CASCADE)
    liked = models.BooleanField(default=False)

    def __str__(self):
        return self.user.name + " - " + str(self.video.id)


class Message(models.Model):
    sender = models.ForeignKey(User, on_delete=models.CASCADE, related_name="sent_messages")
    receiver = models.ForeignKey(User, on_delete=models.CASCADE, related_name="received_messages")
    content = models.CharField(max_length=1000)
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return self.sender.name + " - " + self.receiver.name