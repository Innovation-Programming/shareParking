from django.db import models
from django.contrib.auth.models import User
from pay.models import *
from common.models import Personal

# Create your models here.
class ParkingLot(models.Model):
    name = models.CharField(max_length=50)
    user = models.ForeignKey(User, on_delete=models.CASCADE, null=True)
    # user = models.OneToOneField(User, on_delete=models.CASCADE,null=True,blank=True)
    address = models.CharField(max_length=100)
    image = models.ImageField(upload_to='images/')
    start_time = models.CharField(max_length=10)
    space = models.IntegerField()
    end_time = models.CharField(max_length=10)
    latitude = models.FloatField()
    longitude = models.FloatField()
    fee = models.IntegerField()

    def __str__(self):
        return self.name



class Ticket(models.Model):
    parking_lot = models.ForeignKey(ParkingLot, on_delete=models.CASCADE)
    # user = models.ForeignKey(User, on_delete=models.CASCADE, null=True)
    personal = models.OneToOneField(Personal, on_delete=models.CASCADE)
    created = models.DateTimeField()
    is_available = models.BooleanField()