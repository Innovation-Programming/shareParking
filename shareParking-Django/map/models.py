from django.db import models
from django.contrib.auth.models import User

# Create your models here.
# Create your models here.
class ParkingLot(models.Model):
    name = models.CharField(max_length=50)
    user = models.ForeignKey(User, on_delete=models.CASCADE, null=True)
    # user = models.OneToOneField(User, on_delete=models.CASCADE,null=True,blank=True)
    address = models.CharField(max_length=100)
    image = models.ImageField(upload_to='images/')
    start_time = models.CharField(max_length=10)
    end_time = models.CharField(max_length=10)
    latitude = models.FloatField()
    longitude = models.FloatField()
    fee = models.IntegerField()

    def __str__(self):
        return self.name

class Ticket(models.Model):
    parking_lot = models.ForeignKey(ParkingLot, on_delete=models.CASCADE)
    # user = models.ForeignKey(User, on_delete=models.CASCADE, null=True)
    user = models.OneToOneField(User, on_delete=models.CASCADE,null=True,blank=True)
    vaild_start = models.CharField(max_length=10)
    vaild_end = models.CharField(max_length=10)


#회원정보모델
class Personal(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE,null=True,blank=True)
    # username = models.CharField(max_length=100)
    # password1 = models.CharField(max_length=100)
    nickname = models.CharField(max_length=100)
    point = models.IntegerField()
    email = models.CharField(max_length=200)
    phone = models.IntegerField()
    addr = models.CharField(max_length=200)
    postcode = models.IntegerField()
    

#SMS인증 모델
class Authentication(models.Model):
    phone_number = models.CharField('휴대폰 번호', max_length=30)
    auth_number = models.CharField('인증번호', max_length=30)

    class Meta:
        db_table = 'authentications' # DB 테이블명
        verbose_name_plural = "휴대폰인증 관리 페이지" # Admin 페이지에서 나타나는 설명

class Post(models.Model):
	title = models.CharField(max_length=100)
	photo = models.ImageField(blank=True)