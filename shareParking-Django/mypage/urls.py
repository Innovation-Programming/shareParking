from django.urls.conf import path
from . import views

app_name = 'mypage'

urlpatterns = [
    path('', views.mypage, name="main"),
    path('ticket/', views.ticket, name="ticket")
]