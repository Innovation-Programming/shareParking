from django.shortcuts import render
from map.models import Ticket, TicketPayment
from pay.models import Payment
from common.models import Personal
import datetime as dt
from django.utils import timezone
# Create your views here.
def mypage(request):
    personal = Personal.objects.get(user=request.user)
    point_pays = Payment.objects.filter(personal=personal, status="paid")
    ticket_pays = TicketPayment.objects.filter(personal=personal)

    payments = []
    for row in point_pays:
        payments.append({'name':row.name, 'created':row.created, 'amount':row.amount})
    
    for row in ticket_pays:
        payments.append({'name':row.name, 'created':row.created, 'amount':row.amount})
    
    payments.sort(key = lambda x:x['created'])
    
    for i in payments:
        print(i)
    context = {'payments': payments, 'user': personal}
    return render(request, 'mypage/main.html', context)

def ticket(request):
    personal = Personal.objects.get(user=request.user)
    try:
        ticket = Ticket.objects.get(personal=personal)
    except Ticket.DoesNotExist:
        return render(request, 'mypage/no_ticket.html')

    
    park = ticket.parking_lot
    now =  timezone.now()
    created = ticket.created
    # 청구금액 계산
    spend_time = now - created
    sec_diff = int(spend_time.total_seconds())
    context = {'ticket': ticket, 'park': park, 'user': personal, 'sec_diff': sec_diff}
    return render(request, 'mypage/ticket.html', context)