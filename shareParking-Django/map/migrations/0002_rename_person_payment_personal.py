# Generated by Django 3.2.7 on 2021-09-27 18:34

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('map', '0001_initial'),
    ]

    operations = [
        migrations.RenameField(
            model_name='payment',
            old_name='person',
            new_name='personal',
        ),
    ]