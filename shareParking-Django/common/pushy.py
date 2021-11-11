import json
import  urllib.request as urllib2

class PushyAPI:

   @staticmethod
   def sendPushNotification(data, to, options):
      # Insert your Pushy Secret API Key here
      apiKey = 'bc12a8f52f432569b304656c18f62cda1db8d4fd79e36eddc72c82d28db2ced6'
      # apiKey = '09200e9bc022c8f04cb18949f36fc7ab040416bdf400669e1ad02bcce23d074a'
      # Default post data to provided options or empty object
      postData = options or {}

      # Set notification payload and recipients
      postData['to'] = to
      postData['data'] = data
      url = 'https://api.pushy.me/push?api_key={0}'.format(apiKey)
      # Set URL to Send Notifications API endpoint
      req = urllib2.Request(url)

      # Set Content-Type header since we're sending JSON
      req.add_header('Content-Type', 'application/json')
      jsonData = json.dumps(postData) 
      try:
         # Actually send the push
         response = urllib2.urlopen(req, jsonData.encode('utf-8'))
         print(response.read())
      except urllib2.HTTPError as e:
         # Print response errors
         print("Pushy API returned HTTP error " + str(e.code) + ": " + str(e.read()))