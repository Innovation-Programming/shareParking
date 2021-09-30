from django.shortcuts import render
# Create your views here.
from django.http import HttpResponse
from django.urls import reverse
from django.utils import timezone
#for sms import modules
import json
import requests
from django.views.decorators.csrf import csrf_exempt
from common.models import *
from .models import *
# Create your views here.


def pay(request):
    print(request.user)
    personal = Personal.objects.get(user=request.user)
    context = {'personal' : personal}
    return render(request, 'pay/pay.html', context)


# 결제정보를 DB에 저장
@csrf_exempt
def pay_request(request):
    
    if request.method == 'POST' and request.is_ajax():
        personal = Personal.objects.get(user=request.user)
        Payment.objects.create(
            mid=request.POST.get('mid'),
            personal=personal,
            created=timezone.now(),
            name=request.POST.get('name'),
            method=request.POST.get('method'),
            amount=request.POST.get('amount'),
            status="unpaid"
        )

        return HttpResponse(json.dumps({'status': "success", 'message': "성공"}),
                                    content_type="application/json")
    else:
        return HttpResponse(json.dumps({'status': "failed", 'message': "전송방식이 올바르지 않습니다."}),
                                    content_type="application/json")


# 아임포트 서버와 결제금액 대조 후 결제처리
@csrf_exempt
def pay_process(request):
    if request.method == 'POST' and request.is_ajax():
        uid = request.POST.get('imp_uid')
        mid = request.POST.get('mid')
    elif request.method == "GET":
        uid = request.GET['imp_uid']
        mid = request.GET['merchant_uid']
    else:
        return HttpResponse(json.dumps({'status': "failed", 'message': "전송방식이 올바르지 않습니다."}),
                                    content_type="application/json")


    # 장고 DB에서 결제금액 조회
    payment = Payment.objects.get(mid=mid)
    amount = payment.amount
    int(amount)

    # 액세스 토큰(access token) 발급받기
    data = {
        "imp_key": "6055957363343863",
        "imp_secret": "8tqms0bq3Mlt5mVkUNNiSxryP4oFoORxiiTLi5blSxPXTLiNy66qExZtCn90c1xujqgCwsXoZNmSyEG7"
    }

    response = requests.post('https://api.iamport.kr/users/getToken', data=data)
    data = response.json()
    my_token = data['response']['access_token']

    #  imp_uid로 아임포트 서버에서 결제 정보 조회
    headers = {"Authorization": my_token}
    response = requests.get('https://api.iamport.kr/payments/'+uid, data=data, headers = headers)
    data = response.json()

    # B에서 결제되어야 하는 금액 조회 const
    amountToBePaid = data['response']['amount']  # 아임포트에서 결제후 실제 결제라고 인지 된 금액
    amountToBePaid = int(amountToBePaid)
    status = data['response']['status']  # 아임포트에서의 상태

    if amount==amountToBePaid:
        # DB에 결제 정보 저장
        payment.status = status
        payment.save()
        
        context = {
            'payment': payment
        }

        if status=='paid':
            personal = Personal.objects.get(user=request.user)
            personal.point += amountToBePaid
            personal.save()

        return render(request, 'pay/pay_complete.html', context)
    else:
        return HttpResponse(json.dumps({'status': "forgery", 'message': "위조된 결제시도"}), content_type="application/json")


#결제완료 페이지
def pay_complete(request):
    return render(request, 'pay/pay_complete.html')