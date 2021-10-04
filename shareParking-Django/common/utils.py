import hashlib
import hmac
import base64

def make_signature(timestamp):
    access_key = 'X6k6LtPpYhrDlFxaY2Gz'
    secret_key = 'rKOD2vTNHhM3T37I9KCltIyRSEo0aI4Gb9BlsFXG' 
    secret_key = bytes(secret_key, 'UTF-8')

    uri = "/sms/v2/services/ncp:sms:kr:270597174975:sms_auth/messages"
    # uri 중간에 Console - Project - 해당 Project 서비스 ID 입력 (예시 = ncp:sms:kr:263092132141:sms)

    
    message = "POST" + " " + uri + "\n" + timestamp + "\n" + access_key
    message = bytes(message, 'UTF-8')
    signingKey = base64.b64encode(hmac.new(secret_key, message, digestmod=hashlib.sha256).digest())
    return signingKey