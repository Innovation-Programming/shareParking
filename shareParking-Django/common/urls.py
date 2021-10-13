from os import name
from django.urls.conf import path
from . import views
from django.contrib.auth import views as auth_views

app_name = 'common'

urlpatterns = [
    path('', views.login_main, name="login"),
    path('sendSMS/',views.SmsSendView.as_view(), name="sendSMS"),
    path('confirmSMS/', views.SMSVerificationView.as_view(), name="confirmSMS"),
    path('signup/', views.signup, name="signup"),
    path('logout/', views.logout_view, name="logout"),
    path('setting/', views.setting, name="setting"),
    path('sms/', views.sms, name="sms"),
    path('sendSMS/',views.SmsSendView.as_view(), name="sendSMS"),
    path('confirmSMS/', views.SMSVerificationView.as_view(), name="confirmSMS"),

    # path('send_email/', views.send_email, name='send_email'),
    
    path('forgot_id/', views.ForgotIDView, name="forgot_id"),
    path('forgot_pw/', views.send_email, name="password_reset"),
]