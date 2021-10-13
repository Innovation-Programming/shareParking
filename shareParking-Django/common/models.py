from django.db import models
from django.contrib.auth.models import User


class Post(models.Model):
	title = models.CharField(max_length=100)
	photo = models.ImageField(blank=True)
    
# Create your models here.


#SMS인증 모델
class Authentication(models.Model):
    phone_number = models.CharField('휴대폰 번호', max_length=30)
    auth_number = models.CharField('인증번호', max_length=30)

    class Meta:
        db_table = 'authentication' # DB 테이블명
        verbose_name_plural = "휴대폰인증 관리 페이지" # Admin 페이지에서 나타나는 설명



#회원정보모델
class Personal(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE, primary_key=True)
    # username = models.CharField(max_length=100)
    # password1 = models.CharField(max_length=100)
    nickname = models.CharField(max_length=100)
    point = models.IntegerField(null=True, blank=True)
    deposit = models.IntegerField()
    email = models.CharField(max_length=200)
    phone = models.CharField(max_length=14)
    addr = models.CharField(max_length=200)
    postcode = models.IntegerField()
    
    def __str__(self):
        return self.nickname
    class Meta:
        db_table= 'personal'