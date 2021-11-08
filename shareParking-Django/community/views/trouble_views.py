from django.shortcuts import render


def index(request):
    return render(request, 'trouble/trouble_list.html')