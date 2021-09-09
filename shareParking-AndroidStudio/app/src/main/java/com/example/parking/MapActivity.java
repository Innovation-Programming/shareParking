//package com.example.parking;
//
//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.webkit.GeolocationPermissions;
//import android.webkit.WebChromeClient;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//public class MapActivity extends AppCompatActivity {
//
//    private static final String URL_DAUM_MAP = "http://m.map.daum.net/";
//    private static final String URL_NAVER_MAP = "http://m.map.naver.com/";
////    private static final String TAG = MainActivityTest.class.getSimpleName();
//    private static final int MY_PERMISSION_REQUEST_LOCATION = 0;
//    private WebView webView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_map);
//        webView = (WebView) findViewById(R.id.webVw_map);
//    }
//    private void initWebView() {
//        webView.getSettings().setJavaScriptEnabled(true); // 자바스크립트 사용을 허용한다.
//        webView.setWebViewClient(new WebViewClient()); // 새로운 창을 띄우지 않고 내부에서 웹뷰를 실행시킨다.
//        webView.setWebChromeClient(new WebChromeClient(){
//            @Override public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
//                super.onGeolocationPermissionsShowPrompt(origin, callback);
//                callback.invoke(origin, true, false);
//            }
//        });
//
//        webView.loadUrl(URL_NAVER_MAP);
//
//    }
//    private void permissionCheck(){
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//            //Manifest.permission.ACCESS_FINE_LOCATION 접근 승낙 상태 일때
//            initWebView(); }
//        else{
//            //Manifest.permission.ACCESS_FINE_LOCATION 접근 거절 상태 일때
//            // 사용자에게 접근권한 설정을 요구하는 다이얼로그를 띄운다.
//            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSION_REQUEST_LOCATION);
//        }
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults); if(requestCode == MY_PERMISSION_REQUEST_LOCATION){
//            initWebView();
//        }
//    }
//}