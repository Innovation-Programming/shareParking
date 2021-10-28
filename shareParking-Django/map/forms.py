from django import forms
from map.models import *

class ParkingLotForm(forms.ModelForm):
    class Meta:
        model = ParkingLot
        fields = ['name', 'address', 'addr_detail', 'latitude', 'longitude', 'start_time', 'end_time', 'fee', 'user', 'image', 'space', 'desc']
        labels = {
            'user' : '등록자',
            'name': '이    름',
            'address': '주    소',
            'latitude': '위    도',
            'longitude': '경    도',
            'start_time': '시작시간',
            'end_time': '종료시간',
            'image' : '사    진',
            'fee': '요    금',
            'space': '주차가능',
            'desc': '설    명'
        }