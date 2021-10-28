from django.urls.conf import path
from django.urls.resolvers import URLPattern
from django.contrib.auth import views as auth_views
from . import views

app_name = 'map'

urlpatterns = [
    path('main/', views.index, name="main"),
    path('form/', views.parking_lot_create, name='parking_lot_create'),
    path('form/modify', views.parking_lot_modify, name='parking_lot_modify'),
    path('test_chat/', views.test_chat, name="test_chat"),
    path('ticket/buy/<int:parking_lot_id>', views.buy_ticket, name='buy_ticket'),
    path('ticket/create/', views.create_ticket, name='create_ticket'),
    path('ticket/confirm/', views.confirm_ticket, name='confirm_ticket'),
    # path("login/kakao/", views.kakao_login, name="kakao-login"),
    # path(
    #     "login/kakao/callback/",
    #     views.kakao_login_callback,
    #     name="kakao-callback",
    # ),
    # path('login/', auth_views.LoginView.as_view(template_name = 'login.html'), name='login'),
    # path('logout/', auth_views.LogoutView.as_view(), name="logout"),
]