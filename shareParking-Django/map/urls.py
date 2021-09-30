from django.urls.conf import path
from django.urls.resolvers import URLPattern
from django.contrib.auth import views as auth_views
from . import views

app_name = 'map'

urlpatterns = [
    path('main/', views.index, name="main"),
    path('form/', views.parking_lot_create, name='parking_lot_create'),
    path('signup/', views.signup, name="signup"),
    path('logout/', views.logout_view, name="logout"),
    path('setting/', views.setting, name="setting"),
    path('sms/', views.sms, name="sms"),
    path('test_chat/', views.test_chat, name="test_chat"),
    path('main/ticket/<int:parking_lot_id>', views.ticket_create, name='ticket_create'),
    path('sendSMS/',views.SmsSendView.as_view(), name="sendSMS"),
    path('confirmSMS/', views.SMSVerificationView.as_view(), name="confirmSMS"),
    # path("login/kakao/", views.kakao_login, name="kakao-login"),
    # path(
    #     "login/kakao/callback/",
    #     views.kakao_login_callback,
    #     name="kakao-callback",
    # ),
    # path('login/', auth_views.LoginView.as_view(template_name = 'login.html'), name='login'),
    # path('logout/', auth_views.LogoutView.as_view(), name="logout"),
]