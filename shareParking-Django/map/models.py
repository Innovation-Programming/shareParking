from django.db import models
from django.contrib.auth.models import User
from pay.models import *
from common.models import Personal

# Create your models here.
class ParkingLot(models.Model):
    name = models.CharField(max_length=50)
    user = models.ForeignKey(User, on_delete=models.CASCADE, null=True)
    address = models.CharField(max_length=100)
    addr_detail = models.CharField(max_length=100, blank=True, null=True)
    image = models.ImageField(upload_to='images/')
    start_time = models.CharField(max_length=10)
    end_time = models.CharField(max_length=10)
    space = models.IntegerField()
    latitude = models.FloatField()
    longitude = models.FloatField()
    fee = models.IntegerField()
    desc = models.TextField(blank=True, null=True)
    def __str__(self):
        return self.name


class Ticket(models.Model):
    parking_lot = models.ForeignKey(ParkingLot, on_delete=models.CASCADE)
    # user = models.ForeignKey(User, on_delete=models.CASCADE, null=True)
    personal = models.OneToOneField(Personal, on_delete=models.CASCADE)
    created = models.DateTimeField()


class TicketPayment(models.Model):
    name = models.CharField(max_length=50)
    parking_lot = models.ForeignKey(ParkingLot, on_delete=models.CASCADE)
    personal = models.ForeignKey(Personal, on_delete=models.CASCADE)
    created = models.DateTimeField()
    spend_sec = models.IntegerField()
    amount = models.IntegerField()