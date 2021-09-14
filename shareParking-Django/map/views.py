from django.shortcuts import render
import os
# Create your views here.
from django.http import HttpResponse
from .models import ParkingLot
from .forms import *
from django.shortcuts import redirect
from map.forms import UserForm
from django.contrib.auth import authenticate, login, logout
from django.contrib import messages
from django.contrib.auth.decorators import login_required
from django.urls import reverse

#for sms import modules
import json, time, random
import requests
from django.views import View
from django.http import JsonResponse
from .utils import make_signature
from .models import *


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
        # return r/nder(request, 'map/main.html', user_inform)
    return render(request, 'login.html')

def logout_view(request):
    logout(request)
    return redirect('login')

# def login_view(request):
    
#     return render(request, 'map/main.html')

def setting(request):
    return render(request, 'setting.html')

def signup(request):
    if request.method == "POST":
        print(request.POST)
        form = UserForm(request.POST)
        username = request.POST['username']
        password1 = request.POST['password1']
        password2 = request.POST['password2']
        nickname = request.POST['nickname']
        phone = request.POST['phone']
        user=User.objects.create_user(username=username,password=password1)


        Personal.objects.create(
            user = user,
            nickname = nickname,
            phone = phone,
        )

        user = authenticate(request, username=username, password=password1)
        login(request, user)
        return redirect('map:main')
    else:
        form = UserForm()
    return render(request, 'signup.html', {'form': form})

def index(request):
    print("hi")
    parking_lot_list = ParkingLot.objects.all()
    print(parking_lot_list)
    print("="*50)
    print(ParkingLot.objects.filter(id=1))
    context = {'parking_lot_list': parking_lot_list}
    return render(request, 'map/main.html', context)

@login_required(login_url='login')
def parking_lot_create(request):
    if request.method == 'POST':
        # print(request.POST)
        # print(request.user)

        
        form = ParkingLotForm(request.POST or None)
        name = request.POST['name']
        address = request.POST['address']
        fee = request.POST['fee']
        start_time = request.POST['start_time']
        end_time = request.POST['end_time']
        latitude = request.POST['latitude']
        longitude = request.POST['longitude']
        image = request.FILES['image']
        user = request.user

        # park = ParkingLot()
        # park.name = request.POST['name']
        # park.address = request.POST['address']
        # park.fee = request.POST['fee']
        # park.start_time = request.POST['start_time']
        # park.end_time = request.POST['end_time']
        # park.latitude = request.POST['latitude']
        # park.longitude = request.POST['longitude']
        # park.image = request.FILES['image']
        # park.user = request.user

        ParkingLot.objects.create(
            user = user,
            name = name,
            address = address,
            image = image,
            start_time = start_time,
            end_time = end_time,
            latitude = latitude,
            longitude = longitude,
            fee = fee
        )
        return redirect('map:main')
        # if form.is_valid():
        #     parking_lot = form.save(commit=False)
        #     parking_lot.user = request.user
        #     parking_lot.save()

        #     return redirect('map:main')
            # return render(request, 'map/main.html', {'owner' : request.user})
            # redirect(reverse('map:main', kwargs={'owner' : request.user}))
    else:
        print("ㅇ")
        form = ParkingLotForm()
    context = {'form': form}
    return render(request, 'map/parking_lot_form.html', context)

def test_chat(request):
    if request.method == "GET":
        user = User.objects.get(username=str(request.user))
        print(user)
    return render(request, 'test_chat.html')

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

#ticket_create
def ticket_create(request, parking_lot_id):
    # pybo 질문 등록
    parking_lot = ParkingLot.objects.get(id=parking_lot_id)
    if request.method == 'POST':
        # parking_lot = get_object_or_404(ParkingLot, pk=parking_lot_id)
        form = TicketForm(request.POST or None)
        if form.is_valid():
            ticket = form.save(commit=False)
            ticket.parking_lot = parking_lot
            ticket.save()
            return redirect('map:main')
    else:
        form = ParkingLotForm()
    context = {'form': form, 'parking_lot':parking_lot}
    return render(request, 'map/ticket_form.html', context)


def sms(request):
    SmsSendView
    return HttpResponse('전송완료')

def kakao_login(request):
    client_id = os.environ.get("4a59f5ef88dfe45ae62d45d932d9f7c2")
    redirect_uri = "http://127.0.0.1:8000/users/login/kakao/callback/"

    return redirect(
        f"https://kauth.kakao.com/oauth/authorize?client_id={client_id}&redirect_uri={redirect_uri}&response_type=code"
    )
    
# def kakao_login_callback(request):
#         code = request.GET.get("code", None)
#         client_id = os.environ.get("4a59f5ef88dfe45ae62d45d932d9f7c2")
#         redirect_uri = "http://127.0.0.1:8000/users/login/kakao/callback/"
#         client_secret = os.environ.get("KAKAO_SECRET")
#         request_access_token = requests.post(
#             f"https://kauth.kakao.com/oauth/token?grant_type=authorization_code&client_id={client_id}&redirect_uri={redirect_uri}&code={code}&client_secret={client_secret}",
#             headers={"Accept": "application/json"},
#         )
#         access_token_json = request_access_token.json()
#         error = access_token_json.get("error", None)
#         if error is not None:
#             print(error)
#             KakaoException("Can't get access token")
#         access_token = access_token_json.get("access_token")
#         headers = {"Authorization": f"Bearer {access_token}"}
#         profile_request = requests.post(
#             "https://kapi.kakao.com/v2/user/me",
#             headers=headers,
#         )
#         profile_json = profile_request.json()
#         kakao_account = profile_json.get("kakao_account")
#         profile = kakao_account.get("profile")

#         nickname = profile.get("nickname", None)
#         avatar_url = profile.get("profile_image_url", None)
#         email = kakao_account.get("email", None)
#         gender = kakao_account.get("gender", None)

#         user = models.User.objects.get_or_none(email=email)
#         if user is not None:
#             if user.login_method != models.User.LOGIN_KAKAO:
#                 raise GithubException(f"Please login with {user.login_method}")
#         else:
#             user = models.User.objects.create_user(
#                 email=email,
#                 username=email,
#                 first_name=nickname,
#                 gender=gender,
#                 login_method=models.User.LOGIN_KAKAO,
#             )

#             if avatar_url is not None:
#                 avatar_request = requests.get(avatar_url)
#                 user.avatar.save(
#                     f"{nickname}-avatar", ContentFile(avatar_request.content)
#                 )
#             user.set_unusable_password()
#             user.save()
#         messages.success(request, f"{user.email} signed up and logged in with Kakao")
#         login(request, user)
#         return redirect(reverse("core:home"))
#     except KakaoException as error:
#         messages.error(request, error)
#         return redirect(reverse("core:home"))
#     except SocialLoginException as error:
#         messages.error(request, error)
#         return redirect(reverse("core:home"))
# def kakao_login(request):
#     _context = {'check':False}
#     if request.session.get('access_token'):
#         _context['check'] = True
#     return render(request, 'login.html', _context)

# def kakaoLoginLogic(request):
#     _restApiKey = '4a59f5ef88dfe45ae62d45d932d9f7c2'
#     _redirectUrl = 'http://127.0.0.1:8000/kakaoLoginLogicRedirect'
#     _url = f'https://kauth.kakao.com/oauth/authorize?client_id{_restApiKey}&redirect_uri={_redirectUrl}&response_type=code'
#     return redirect(_url)

# def kakaoLoginLogicRedirect(request):
#     _qs = request.GET['code']
#     _restApiKey = '4a59f5ef88dfe45ae62d45d932d9f7c2'
#     _redirect_uri = 'http://127.0.0.1:8000'
#     _url = f'https://kauth.kakao.com/oauth/token?grant_type=authorization_code&client_id{_restApiKey}&redirect_uri={_redirect_uri}&code={_qs}'
#     _res = requests.post(_url)
#     _result = _res.json()
#     request.session['access_token'] = _result['access_token']
#     request.session.modified = True
#     return render(request, 'map/main.html')

# def kakaoLogout(request):
#     _token = request.session['access_token']
#     _url = 'https://kapi.kakao.com/v1/user/logout'
#     _header = {
#         'Authorization' : f'bearer {_token}'
#     }

#     _res = requests.post(_url, headers=_header)
#     _result = _res.json()
#     if _result.get('id'):
#         del request.session['access_token']
#         return render(request, 'login.html')