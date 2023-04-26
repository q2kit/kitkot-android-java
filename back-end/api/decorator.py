from django.http import JsonResponse

from api.func import verify_access_token
from api.models import User


def auth_pass(methods):
    def decorator(func):
        def wrapper(request, *args, **kwargs):
            if request.method in methods or methods == ["*"]:
                try:
                    token = request.headers.get("Authorization").split("Bearer ")[1]
                    uid = verify_access_token(token)
                    user = User.objects.get(id=uid)
                    request.user = user
                    return func(request, *args, **kwargs)
                except:
                    return JsonResponse({
                        "success": False,
                        "message": "Invalid access token"
                    }, status=401)
            else:
                return JsonResponse({
                    "success": False,
                    "message": "Method not allowed"
                }, status=405)
            
        return wrapper

    return decorator
