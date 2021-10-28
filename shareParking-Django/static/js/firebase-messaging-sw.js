importScripts('https://www.gstatic.com/firebasejs/4.8.1/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/4.8.1/firebase-messaging.js');
 
// Initialize Firebase
var config = {
     apiKey: "AIzaSyC5OcdZmLOnckPEbF-hqnjE8UyZ9zZnXr4",
    authDomain: "shareparking-e9e14.firebaseapp.com",
    databaseURL: "https://shareparking-e9e14.firebaseio.com",
    projectId: "shareparking-e9e14",
    storageBucket: "shareparking-e9e14.appspot.com",
    messagingSenderId: "397100250344"
};
firebase.initializeApp(config);
 
const messaging = firebase.messaging();
messaging.setBackgroundMessageHandler(function(payload){
 
    const title = "Hello World";
    const options = {
            body: payload.data.status
    };
 
    return self.registration.showNotification(title,options);
});
