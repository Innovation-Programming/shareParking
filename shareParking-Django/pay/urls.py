from django.urls.conf import path
from django.urls.resolvers import URLPattern
from django.contrib.auth import views as auth_views
from . import views

app_name = 'pay'

urlpatterns = [
    path('', views.pay, name="main"),
    path('request/', views.pay_request, name="pay_request"),
    path('process/', views.pay_process, name="pay_process"),
    path('complete/', views.pay_complete, name="pay_complete"),
]