from django.urls.conf import path
from . import views

app_name = 'mypage'

urlpatterns = [
    path('', views.mypage, name="point"),
    path('ticket/', views.ticket, name="ticket")
]