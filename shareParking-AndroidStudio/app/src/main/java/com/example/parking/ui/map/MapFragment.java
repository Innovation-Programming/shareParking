package com.example.parking.ui.map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.parking.ChatActivity;
import com.example.parking.MainActivity;
import com.example.parking.R;
import com.example.parking.Signin;
import com.example.parking.ui.setting.SettingViewModel;

import static android.content.Context.MODE_PRIVATE;

//public class MapFragment extends AppCompatActivity {
public class MapFragment extends Fragment {
    private MapViewModel mapViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_map, container, false);

        WebView myWebView = myView.findViewById(R.id.webVw_map);
        myWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);

        myWebView.addJavascriptInterface(new AndroidBridge(), "androidMain");
        SharedPreferences sf = getContext().getSharedPreferences("sFile",MODE_PRIVATE);

        myWebView.setWebChromeClient(new WebChromeClient() {
           @Override
           public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
               super.onGeolocationPermissionsShowPrompt(origin, callback);
               callback.invoke(origin, true, false);
           }
        });

        myWebView.loadUrl("https://shareparking.kr/map/main");
        return myView;
    }

    class AndroidBridge {
        @JavascriptInterface
        public void chatRoom(String parkingAdmin) {
            //이렇게 받아서 핸드폰에 저장해
            //sharedpreferences 이것을 써서
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            startActivity(intent);
            System.out.println("------------------------------parkingAdmin--------------------------------");
            System.out.println(parkingAdmin);
            System.out.println("------------------------------------------------------------------");
//            PreferenceManager.setString(nickname, "loginId", nickuser);
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("sFile",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String adminUser = parkingAdmin;
            editor.putString("parkingAdmin", adminUser);
            editor.commit();
            System.out.println("---------------------------------adminUser---------------------------------");
            System.out.println(adminUser);
            System.out.println("------------------------------------------------------------------");

//            return adminUser;
        }
    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_map);
//
//        WebView webView = (WebView) findViewById(R.id.webvw_map);
//        webView.setWebViewClient(new WebViewClient());
//
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
////        webView.addJavascriptInterface(new AndroidBridge(), "map_android");
//        webView.loadUrl("http://222.232.60.152:90/main");
//    }
}
