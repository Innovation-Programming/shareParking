# Generated by Django 3.2.7 on 2021-10-28 03:05

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('map', '0018_rename_desc_parkinglot_description'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='parkinglot',
            name='description',
        ),
    ]
