from os import name
from django.urls.conf import path
from . import views
from django.contrib.auth import views as auth_views

app_name = 'common'

urlpatterns = [
    # path('', views.login_main, name="login"),
    path('', auth_views.LoginView.as_view(template_name = 'common/login.html'), name="login"),
    path('logout/', auth_views.LogoutView.as_view(), name='logout'),
    path('sendSMS/',views.SmsSendView.as_view(), name="sendSMS"),
    path('confirmSMS/', views.SMSVerificationView.as_view(), name="confirmSMS"),
    path('signup/', views.signup, name="signup"),
    # path('logout/', views.logout_view, name="logout"),
    path('setting/', views.setting, name="setting"),
    path('sms/', views.sms, name="sms"),
    path('sendSMS/',views.SmsSendView.as_view(), name="sendSMS"),
    path('confirmSMS/', views.SMSVerificationView.as_view(), name="confirmSMS"),

    # path('send_email/', views.send_email, name='send_email'),
    
    path('forgot_id/', views.ForgotIDView, name="forgot_id"),
    path('forgot_pw/', views.ForgotPwView, name="password_reset"),

    #setting
    path('delete_user/', views.delete_user, name="delete_user"),
    path('personal_info/', views.personal_info ,name="personal_info"),
    path('edit_personal_info/', views.edit_personal_info , name="edit_personal_info"),
]