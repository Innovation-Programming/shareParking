from django import forms
from .models import Personal

class UserForm(forms.ModelForm):
    # email = forms.EmailField(label="이메일")
    class Meta:
        model = Personal
        fields = ["nickname", "phone", "email"]