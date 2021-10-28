from django.contrib.auth.views import PasswordResetConfirmView, PasswordResetView
from django.shortcuts import render, redirect
from django.http import HttpResponse 
from django.contrib.auth import authenticate, login, logout

import json, time, random
import requests
from django.views import View
from django.http import JsonResponse
from .utils import make_signature

from django.contrib.auth import authenticate, login, logout
from .models import *
from .forms import *

# from decorators import unauthenticated_user
from django.conf import settings
from django.contrib import messages
from django.core.mail import EmailMessage, message
from django.template.loader import render_to_string
from django.contrib.auth.hashers import check_password

from firebase_admin import messaging
from pyfcm import FCMNotification

def change_password(request):
    if request.method == "POST":
        current_password = request.POST.get("origin_password")
        user = request.user
        
        if check_password(current_password, user.password):
            new_password = request.POST.get("password1")
            password_confirm = request.POST.get("password2")
            if new_password == password_confirm:
                user.set_password(new_password)
                user.save()
                login(request, user)
                return render(request, 'common/setting.html')

    return render(request, 'common/change_password.html')

def edit_personal_info(request):
    user = request.user
    req_user = Personal.objects.filter(user=user)
    phone = req_user.first().phone
    context = {
        "user_phone" : phone
    }

    print("Personal로 조회한 기존 이메일")
    bfEmail = req_user.first().email
    print(bfEmail)

    print("User로 조회한 기존 이메일")
    req_user2 = User.objects.filter(username=user)
    bfEmail2 = req_user2.first().email
    print(bfEmail2)

    if request.method == "POST":
        newEmail = request.POST.get("email")
        newPhone = request.POST.get("user_phone")
        print(newEmail)
        print(newPhone)

        request.user.personal.email = newEmail
        request.user.personal.phone = newPhone
        request.user.personal.save()

        request.user.email = newEmail
        request.user.save()

        return render(request, 'common/setting.html')
    return render(request, 'common/edit_personal_info.html', context)

def personal_info(request):
    user = request.user
    req_user = Personal.objects.filter(user=user)
    phone = req_user.first().phone
    context = {
        "user_phone" : phone
    }

    return render(request, 'common/personal_info.html', context)

def delete_user(request):
    if request.method == "POST":
        current_password = request.POST.get('pw_del')
        user = request.user
        if check_password(current_password, user.password):
            user.delete()
            return redirect("common:login")
    
    return render(request, 'common/delete_user.html')

#emailTest
def send_email(request):
    subject = "주차나라 비밀번호 초기화 안내"
    to = ["tjtlgus4@naver.com"]
    from_email = "tjtlgus5@gmail.com"
    message = "초기화된 비밀번호 안내입니다. 비밀번호는 1111 입니다. 초기화된 비밀번호로 로그인하여 비밀번호를 재설정 해주세요."
    EmailMessage(subject=subject, body=message, to=to, from_email=from_email).send()

def ForgotPwView(request):
    if request.method == "POST":
        email = request.POST.get('email')
        print(email)
        # current_password = request.POST.get("origin_password")
        try:
            user = User.objects.get(email=email)
            print(user)
            newUser = User.objects.get(username = user)
            newUser.set_password("1111")
            newUser.save()
            print("이메일 전송")
            # send_email(request)
            subject = "주차나라 비밀번호 초기화 안내"
            to = [email]
            from_email = "tjtlgus5@gmail.com"
            message = "초기화된 비밀번호 안내입니다. 비밀번호는 1111 입니다. 초기화된 비밀번호로 로그인하여 비밀번호를 재설정 해주세요."
            EmailMessage(subject=subject, body=message, to=to, from_email=from_email).send()
            print("1")
            messages.info(request, "비밀번호 초기화 이메일을 확인해주세요. 기입한 이메일로 전송하였습니다.")
            print("2")
        except Exception as e:
            print(e)
            messages.info(request, "이메일을 확인해 주세요.")
    return render(request, 'common/findPw.html')
    

# Create your views here.
def login_main(request):
    if request.method == "POST":
        print(request.POST)
        username = request.POST['username']
        password = request.POST['password']
        user = authenticate(request, username=username, password=password)
        login(request, user)
        user_inform = {
            "username" : username,
            "password" : password,
        }
        return JsonResponse({'username':username})
        # return render(request, 'map/main.html', user_inform)
    return render(request, 'common/login.html')

# @unauthenticated_user
def ForgotIDView(request):
    context = {}
    if request.method == "POST":
        email = request.POST.get('email')
        print(email)
        try:
            user = User.objects.get(email=email)
            userId = user
            if user is not None:
                messages.info(request, "가입된 아이디는 " + str(user) + " 입니다.")
        except:
            messages.info(request, "가입된 정보가 없습니다.")
    return render(request, 'common/findID.html', context)


def logout_view(request):
    logout(request)
    return redirect('login')

# def login_view(request):
#     return render(request, 'map/main.html')

def setting(request):
    return render(request, 'common/setting.html')


def signup(request):
    if request.method == "POST":
        print(request.POST)
        form = UserForm(request.POST)
        username = request.POST['username']
        password1 = request.POST['password1']
        password2 = request.POST['password2']
        email = request.POST['email']
        nickname = request.POST['nickname']
        phone = request.POST['phone']
        user=User.objects.create_user(
            username=username,
            password=password1,
            email = email
            )


        Personal.objects.create(
            user = user,
            nickname = nickname,
            email = email,
            phone = phone,
            point = 0,
            deposit = 0,
            addr = "충북 청주시 청원구 대성로 298",
            postcode = 28503
        )

        print("-"*60)
        print(email)
        print("-"*60)

        user = authenticate(request, username=username, password=password1)
        login(request, user)
        return redirect('map:main')
    else:
        form = UserForm()
    return render(request, 'common/signup.html', {'form': form})


# 네이버 SMS 인증
class SmsSendView(View):
    def send_sms(self, phone_number, auth_number):
        timestamp = str(int(time.time() * 1000))  
        headers = {
            'Content-Type': "application/json; charset=UTF-8", # 네이버 참고서 차용
            'x-ncp-apigw-timestamp': timestamp, # 네이버 API 서버와 5분이상 시간차이 발생시 오류
            'x-ncp-iam-access-key': 'X6k6LtPpYhrDlFxaY2Gz', #access_key
            'x-ncp-apigw-signature-v2': make_signature(timestamp) # utils.py 이용
        }
        body = {
            "type": "SMS", 
            "contentType": "COMM",
            "from": "01056260610", # 사전에 등록해놓은 발신용 번호 입력, 타 번호 입력시 오류
            "content": f"[인증번호:{auth_number}]", # 메세지를 이쁘게 꾸며보자
            "messages": [{"to": f"{phone_number}"}] # 네이버 양식에 따른 messages.to 입력
        }
        body = json.dumps(body)
        uri = "https://sens.apigw.ntruss.com/sms/v2/services/ncp:sms:kr:270597174975:sms_auth/messages"
        response = requests.post(uri, headers=headers, data=body)

        print(response.text)
        # 발송 URI 부분에는 아래 URL을 넣어주면 된다.
        # https://sens.apigw.ntruss.com/sms/v2/services/ncp:sms:kr:270597174975:sms_auth/messages 
        # 다만, 너무 길고 동시에 보안이슈가 있기에 별도로 분기해놓은 settings 파일에 넣어서 불러오는 것을 추천한다.


    def post(self, request):
        #data = json.loads(request.body)
        try:
            # input_mobile_num = data['phone_number']
            input_mobile_num = request.POST.get('phone')
            print(input_mobile_num)
            auth_num = random.randint(10000, 100000) # 랜덤숫자 생성, 5자리로 계획하였다.
            auth_mobile = Authentication.objects.get(phone_number=input_mobile_num)
            auth_mobile.auth_number = auth_num
            auth_mobile.save()
            self.send_sms(phone_number=input_mobile_num, auth_number=auth_num)
            return JsonResponse({'message': '인증번호 발송완료'}, status=200)
        except Authentication.DoesNotExist: # 인증요청번호 미 존재 시 DB 입력 로직 작성
            Authentication.objects.create(
                phone_number=input_mobile_num,
                auth_number=auth_num,
            ).save()
            self.send_sms(phone_number=input_mobile_num, auth_number=auth_num)
            return JsonResponse({'message': '인증번호 발송 및 DB 입력완료'}, status=200)


# 네이버 SMS 인증번호 검증
class SMSVerificationView(View):
    def post(self, request):
        #data = json.loads(request.body)

        try:
            verification = Authentication.objects.get(phone_number=request.POST.get('phone'))

            if verification.auth_number == request.POST.get('auth_number'):
                return JsonResponse({'message': '인증 완료되었습니다.'}, status=200)

            else:
                return JsonResponse({'message': '인증 실패입니다.'}, status=400)

        except Authentication.DoesNotExist:
            return JsonResponse({'message': '해당 휴대폰 번호가 존재하지 않습니다.'}, status=400)


def sms(request):
    SmsSendView
    return HttpResponse('전송완료')


def kakao_login(request):
    client_id = os.environ.get("4a59f5ef88dfe45ae62d45d932d9f7c2")
    redirect_uri = "http://127.0.0.1:8000/users/login/kakao/callback/"

    return redirect(
        f"https://kauth.kakao.com/oauth/authorize?client_id={client_id}&redirect_uri={redirect_uri}&response_type=code"
    )

def send_to_firebase_cloud_messaging():
    # This registration token comes from the client FCM SDKs.
    # registration_token = 'erCpWP1eT3So0zYMWBcj6p:APA91bGAZ5T2Wau0HqOqU_zvaJsZMvhghD15ExMbUZsGcqHlEcfbf-PfRR6W-19UGg2CRpzeKDLMlHy9zTmE69b9t7S5FxPsKbBtPzZn0iRBDJ0gShPBS35PtK7EfSPqPguzGzPx-Ca-'
    registration_token = 'dBsL0YSLQ9u9WQdOjoOO3D:APA91bFKxrMATraM2r2MdN3zqERDrd1PegH65C3xPoAKC_SnBFQCgeQbDUENDBE7-KdEyrmv2Yu0IErjHC-g2dkXlkdq89ncOWUS25D0pfgJhTjMr4VrOdf4RZHoE3ch0oNuuqjr3G6E'
    # See documentation on defining a message payload.
    message = messaging.Message(
    notification=messaging.Notification(
        title='안녕하세요 타이틀 입니다',
        body='입차했습니다',
    ),
    token=registration_token,
    )

    response = messaging.send(message)
    # Response is a message ID string.
    print('Successfully sent message:', response)

# def send_message():
#     APIKEY = "Your Server Key"
#     TOKEN = "Your Token"
#     # 파이어베이스 콘솔에서 얻어 온 서버 키를 넣어 줌
#     push_service = FCMNotification(APIKEY)

#     # 메시지 (data 타입)
#     data_message = {
#         "body": "body",
#         "title": "title
#     }
 
#     # 토큰값을 이용해 1명에게 푸시알림을 전송함
#     result = push_service.single_device_data_message(registration_id=TOKEN, data_message=data_message)
 
#     # 전송 결과 출력
#     print(result)