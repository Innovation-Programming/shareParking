# Generated by Django 3.2.7 on 2021-10-25 16:04

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('common', '0002_personal_deposit'),
    ]

    operations = [
        migrations.AlterField(
            model_name='personal',
            name='point',
            field=models.IntegerField(blank=True, null=True),
        ),
    ]