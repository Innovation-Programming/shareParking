# Generated by Django 3.2.7 on 2021-10-12 18:23

import datetime
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('map', '0012_auto_20211012_1755'),
    ]

    operations = [
        migrations.AddField(
            model_name='ticketpayment',
            name='amount',
            field=models.IntegerField(default=0),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='ticketpayment',
            name='created',
            field=models.DateTimeField(default=datetime.datetime(2021, 10, 12, 18, 23, 5, 284934)),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='ticketpayment',
            name='parking_lot',
            field=models.ForeignKey(default=0, on_delete=django.db.models.deletion.CASCADE, to='map.parkinglot'),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='ticketpayment',
            name='spend_mins',
            field=models.IntegerField(default=0),
            preserve_default=False,
        ),
    ]