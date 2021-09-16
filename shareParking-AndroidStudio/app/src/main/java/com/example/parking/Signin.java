package com.example.parking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.SslError;
import android.os.Bundle;
import android.preference.Preference;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Console;

public class Signin extends AppCompatActivity {

//    private Context nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        WebView webView = (WebView) findViewById(R.id.webvw);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.addJavascriptInterface(new AndroidBridge(), "android");
        SharedPreferences sf = getSharedPreferences("sFile",MODE_PRIVATE);


//        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl("https://shareparking.kr");
//        webView.loadUrl("https://6a72-203-252-240-68.ngrok.io");
    }

    class AndroidBridge {
        @JavascriptInterface
        public void goMain(String username) {
            //이렇게 받아서 핸드폰에 저장해
            //sharedpreferences 이것을 써서
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

//            PreferenceManager.setString(nickname, "loginId", nickuser);
            SharedPreferences sharedPreferences = getSharedPreferences("sFile",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String nickname = username;
            editor.putString("loginId", nickname);
            editor.commit();
            System.out.println("------------------------------------------------------------------");
            System.out.println(nickname);
            System.out.println("------------------------------------------------------------------");

//            return nickname;
        }


    }
}
