from django.shortcuts import render
from pay.models import Payment
from common.models import Personal
# Create your views here.
def mypage(request):
    user = Personal.objects.get(user=request.user)
    payments = Payment.objects.filter(personal=user, status="paid")
    context = {'payments': payments, 'user': user}
    return render(request, 'mypage/main.html', context)