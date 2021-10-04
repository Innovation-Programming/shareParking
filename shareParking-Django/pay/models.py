from django.db import models
# Create your models here.


#결제정보모델
class Payment(models.Model):
    mid = models.CharField(max_length=50)
    personal = models.ForeignKey('common.Personal', on_delete=models.CASCADE)
    created = models.DateTimeField(auto_now_add=True)
    name = models.CharField(max_length=50)
    method = models.CharField(max_length=10)
    amount = models.IntegerField()
    # status >> paid(완료), ready(미결제), cancellmed(취소). failed(실패) 
    status = models.CharField(max_length=10)

    def __str__(self):
        return self.mid