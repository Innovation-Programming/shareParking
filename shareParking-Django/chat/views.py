from django.contrib.auth.models import User
from django.shortcuts import render

def index_chat(request):
    return render(request, 'index_chat.html', {})

# def room(request, room_name):
#     if request.method == "GET":
#         user = User.objects.get(username=str(request.user))
#         print(user)

#         context = {
#             "username" : user,
#             "room_name" : room_name
#         }
#     return render(request, 'room.html', context)
def room(request, room_name):
    if request.method == "GET":
        user = User.objects.get(username=str(request.user))
        print(user)
        room_name = request.GET.get('room_name')
        print(room_name)
        context = {
            "username" : user,
            "room_name" : room_name
        }
    return render(request, 'room.html', context)


    # {
    #     'room_name' : room_name
    # })