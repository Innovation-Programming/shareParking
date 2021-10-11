from django.shortcuts import render
from map.models import Ticket
from pay.models import Payment
from common.models import Personal

# Create your views here.
def mypage(request):
    personal = Personal.objects.get(user=request.user)
    payments = Payment.objects.filter(personal=personal, status="paid")
    context = {'payments': payments, 'user': personal}
    return render(request, 'mypage/main.html', context)

def ticket(request):
    personal = Personal.objects.get(user=request.user)
    ticket = Ticket.objects.get(personal=personal)
    park = ticket.parking_lot
    context = {'ticket': ticket, 'park': park}
    return render(request, 'mypage/ticket.html', context)