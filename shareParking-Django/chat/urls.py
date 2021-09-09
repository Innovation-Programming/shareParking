from django.urls.conf import path
from . import views

app_name = 'chat'

urlpatterns = [
    path('', views.index_chat, name='index'),
    path('<str:room_name>/', views.room, name='room'),
]