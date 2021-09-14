from django import forms
from map.models import *
from django.contrib.auth.forms import UserCreationForm
from django.contrib.auth.models import User

class ParkingLotForm(forms.ModelForm):
    class Meta:
        model = ParkingLot
        fields = ['name', 'address', 'latitude', 'longitude', 'start_time', 'end_time', 'fee', 'user', 'image']
        labels = {
            'user' : '등록자',
            'name': '이    름',
            'address': '주    소',
            'latitude': '위    도',
            'longitude': '경    도',
            'start_time': '시작시간',
            'end_time': '종료시간',
            'image' : '사    진',
            'fee': '요    금'
        }

class TicketForm(forms.ModelForm):
    class Meta:
        model = Ticket
        fields = ['vaild_start', 'vaild_end']

class UserForm(forms.ModelForm):
    # email = forms.EmailField(label="이메일")
    class Meta:
        model = Personal
        fields = ["nickname", "phone"]