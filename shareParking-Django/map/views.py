from django.shortcuts import render
import os
# Create your views here.
from .models import ParkingLot
from .forms import *
from django.shortcuts import redirect
from django.contrib.auth.decorators import login_required
#for sms import modules
from .models import *





def index(request):
    parking_lot_list = ParkingLot.objects.all()
    context = {'parking_lot_list': parking_lot_list}
    return render(request, 'map/main.html', context)


@login_required(login_url='login')
def parking_lot_create(request):
    if request.method == 'POST':
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
    else:
        form = ParkingLotForm()
    context = {'form': form}
    return render(request, 'map/parking_lot_form.html', context)

def test_chat(request):
    if request.method == "GET":
        user = User.objects.get(username=str(request.user))
        print(user)
    return render(request, 'test_chat.html')

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