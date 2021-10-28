from django.shortcuts import render
import os
# Create your views here.
from .models import ParkingLot, TicketPayment
from .forms import *
from django.shortcuts import redirect
from django.contrib.auth.decorators import login_required
#for sms import modules
from .models import *
from django.views.decorators.csrf import csrf_exempt
from common.views import *
import json
from django.http import HttpResponse
from django.db.utils import IntegrityError
from PIL import Image
from django.utils import timezone
import datetime as dt
import mypage.views as myview
import math


def index(request):
    parking_lot_list = ParkingLot.objects.order_by('fee')
    context = {'parking_lot_list': parking_lot_list}
    return render(request, 'map/main.html', context)
    # return render(request, 'map/main.html')


@csrf_exempt
@login_required(login_url='login')
def parking_lot_create(request):
    parking_lot_list = ParkingLot.objects.filter(user=request.user)
    parking_lot_list = parking_lot_list.order_by("id")
    if request.method == 'POST':

        name = request.POST['name']
        address = request.POST['address']
        addr_detail = request.POST['addr_detail']
        fee = request.POST['fee']
        start_time = request.POST['start_time']
        end_time = request.POST['end_time']
        latitude = request.POST['latitude']
        longitude = request.POST['longitude']
        image = request.FILES['image']
        user = request.user
        space = request.POST['space']
        desc = request.POST['description']

        ParkingLot.objects.create(
            user = user,
            name = name,
            address = address,
            addr_detail = addr_detail,
            image = image,
            start_time = start_time,
            end_time = end_time,
            latitude = latitude,
            longitude = longitude,
            fee = fee,
            space=space,
            description=desc
        )
        return redirect('map:main')
    context = {'parking_lot_list': parking_lot_list}
    return render(request, 'map/parking_lot_form.html', context)

@csrf_exempt
@login_required(login_url='login')
def parking_lot_modify(request):
    parking_lot_list = ParkingLot.objects.filter(user=request.user)
    
    if request.method == 'POST':
        id = request.POST['id']
        address = request.POST['address']
        addr_detail = request.POST['addr_detail']
        fee = request.POST['fee']
        start_time = request.POST['start_time']
        end_time = request.POST['end_time']
        latitude = request.POST['latitude']
        longitude = request.POST['longitude']
        space = request.POST['space']
        desc = request.POST['desc']

        park = parking_lot_list.get(id=id)
        
        park.address = address
        park.addr_detail = addr_detail
        park.fee = fee
        park.start_time = start_time
        park.end_time = end_time
        park.latitude = latitude
        park.longitude = longitude
        park.space = space
        park.desc = desc
        park.save()
        return HttpResponse(json.dumps({'status': "success"}), content_type="application/json")
    context = {'parking_lot_list': parking_lot_list}
    
    return render(request, 'map/parking_lot_form.html', context)

# def form_test(request):
#     if request.method == "POST":
#         image_dir = Image.open(request.FILES.get("image"))
#         print("*" * 60)
#         print(image_dir)
#         print("*" * 60)


def test_chat(request):
    if request.method == "GET":
        user = User.objects.get(username=str(request.user))
        print(user)
    return render(request, 'test_chat.html')


def buy_ticket(request, parking_lot_id):
    parking_lot = ParkingLot.objects.get(id=parking_lot_id)
    # if request.method == 'POST':
    personal = Personal.objects.get(user=request.user)
    context = {'parking_lot':parking_lot, 'personal': personal}
    return render(request, 'map/ticket_form.html', context)


@csrf_exempt
def create_ticket(request):
    if request.method == 'POST' and request.is_ajax():
        parking_lot_id = request.POST['id']
        user = request.user
        
        parking_lot = ParkingLot.objects.get(id=parking_lot_id)
        personal = Personal.objects.get(user=user)

        max_space = parking_lot.space
        ticket_set = Ticket.objects.filter(parking_lot=parking_lot)

        tickets = len(ticket_set)
        if tickets > 1:
            return HttpResponse(json.dumps({'status': "failed", 'message': "이미 주차권을 보유중입니다."}),
                        content_type="application/json")

        available_space = max_space - tickets

        if available_space < 1:
            return HttpResponse(json.dumps({'status': "failed", 'message': "해당 주차장에 자리가 없습니다."}),
                            content_type="application/json")
        
        Ticket.objects.create(
            parking_lot = parking_lot,
            personal = personal,
            created = timezone.now()
        )

        # 보증금 처리
        personal.deposit = personal.point
        personal.point = 0
        personal.save()

        # 주차장 자리 처리
        parking_lot.space -= 1
        parking_lot.save()
        send_to_firebase_cloud_messaging()
        return HttpResponse(json.dumps({'status': "success"}),
                            content_type="application/json")
    else:
        return HttpResponse(json.dumps({'status': "failed", 'message': "전송방식이 올바르지 않습니다."}),
                            content_type="application/json")


def confirm_ticket(request):
    personal = Personal.objects.get(user=request.user)
    ticket = Ticket.objects.get(personal=personal)
    park = ticket.parking_lot

    now =  timezone.now()
    created = ticket.created
    # 청구금액 계산
    spend_time = now - created
    seconds = spend_time.total_seconds()
    minutes = seconds / 60
    period = math.ceil(minutes / 10)
    biliing_fee = park.fee * period

    personal.deposit -= biliing_fee

    if personal.deposit < 0:
        personal.deposit = 0
    
    personal.point += personal.deposit
    personal.save()
    
    park.space += 1
    park.save()
    TicketPayment.objects.create(
        name = "주차권: " + park.name,
        personal=personal,
        parking_lot=park,
        created = timezone.now(),
        spend_sec = seconds,
        amount = biliing_fee
    )
    ticket.delete()

    return myview.mypage(request)
    
    
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