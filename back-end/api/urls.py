"""project URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/4.1/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.urls import path

from api.views import (
    register,
    login,
    google_auth,
    reset_password,
    edit_profile,
    post_video,
    get_videos,
    get_videos_by_owner,
    post_comment,
    get_comments,
)

urlpatterns = [
    path('register/', register),
    path('login/', login),
    path('google-auth/', google_auth),
    path('reset-password/', reset_password),
    path('edit-profile/', edit_profile),

    path('post-video/', post_video),
    path('videos/', get_videos),
    path('<int:owner_id>/videos/', get_videos_by_owner),
    path('post-comment/', post_comment),
    path('<int:video_id>/comments/', get_comments),
]
