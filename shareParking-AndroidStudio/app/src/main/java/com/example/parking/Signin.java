package com.example.parking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.http.SslError;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Base64;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import java.io.Console;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Signin extends AppCompatActivity {

//    private Context nickname;
//    private ISessionCallback mSessionCallback;


    private SessionCallback sessionCallback = new SessionCallback();
    Session session;

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

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.addJavascriptInterface(new AndroidBridge(), "android");
        SharedPreferences sf = getSharedPreferences("sFile",MODE_PRIVATE);


//        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl("https://shareparking.kr");
        getHashKey();

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

        public void kakaoLogin(){
            session = Session.getCurrentSession();
            session.addCallback(sessionCallback);

            session.open(AuthType.KAKAO_LOGIN_ALL, Signin.this);
        }
        
//            mSessionCallback = new ISessionCallback() {
//                @Override
//                public void onSessionOpened() {
//                    //로그인 요청
//                    UserManagement.getInstance().me(new MeV2ResponseCallback() {
//                        @Override
//                        public void onFailure(ErrorResult errorResult) {
//                            //로그인 실패
//                            Toast.makeText(Signin.this, "로그인 도중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onSessionClosed(ErrorResult errorResult) {
//                            //세션이 닫힘
//                            Toast.makeText(Signin.this, "세션이 닫혔습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onSuccess(MeV2Response result) {
//                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                            intent.putExtra("name", result.getKakaoAccount().getProfile().getNickname());
//                            intent.putExtra("email", result.getKakaoAccount().getEmail());
//                            startActivity(intent);
//                            Toast.makeText(Signin.this, "환영합니다.", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//
//                @Override
//                public void onSessionOpenFailed(KakaoException exception) {
//                    Toast.makeText(Signin.this, "onSessionOpenFailed", Toast.LENGTH_SHORT).show();
//                }
//            };
//            Session.getCurrentSession().addCallback(mSessionCallback);
//            Session.getCurrentSession().checkAndImplicitOpen();
//        }
    }
    private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }
}
