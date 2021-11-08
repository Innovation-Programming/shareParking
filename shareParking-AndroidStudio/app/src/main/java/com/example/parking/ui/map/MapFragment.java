package com.example.parking.ui.map;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.parking.ChatActivity;
import com.example.parking.MainActivity;
import com.example.parking.Navi;
import com.example.parking.R;
import com.example.parking.Signin;
import com.example.parking.ui.setting.SettingViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.skt.Tmap.TMapTapi;

import java.net.URISyntaxException;

import static android.content.Context.MODE_PRIVATE;

//public class MapFragment extends AppCompatActivity {
public class MapFragment extends Fragment {
    private MapViewModel mapViewModel;
    String token = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

//        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//            @Override
//            public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                if (!task.isSuccessful()) {
//                    return;
//                }
//                //SharedPreference 사용하여 토큰 값 저장
//                SharedPreferences sharedPreferences = getContext().getSharedPreferences("sFile", MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                token = task.getResult().getToken();
//                editor.putString("Token1", token);
//                editor.commit();
//
//                System.out.println("!!!!tokenChecktokenChecktokenChecktokenChecktokenChecktokenCheck!!!!");
//                System.out.println(token);
//                System.out.println("!!!!tokenChecktokenChecktokenChecktokenChecktokenChecktokenCheck!!!!");
//            }
//        });

        View myView = inflater.inflate(R.layout.activity_map, container, false);

        WebView myWebView = myView.findViewById(R.id.webVw_map);
        myWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);

        myWebView.addJavascriptInterface(new AndroidBridge(), "androidMain");
        SharedPreferences sf = getContext().getSharedPreferences("sFile",MODE_PRIVATE);
        token = sf.getString("tokenFromPushy", null);

        System.out.println("이 기기의 토큰값은 : " + token);


        myWebView.setWebChromeClient(new WebChromeClient() {
           @Override
           public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
               super.onGeolocationPermissionsShowPrompt(origin, callback);
               callback.invoke(origin, true, false);
           }
        });

//        myWebView.canGoBack();
        myWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (myWebView.canGoBack()) {
                    ActivityCompat.finishAffinity(getActivity());
                    android.os.Process.killProcess(android.os.Process.myPid());	// 앱 프로세스 종료
                    return true;
                }
                else {

                }
                return false;
            }
        });

        myWebView.loadUrl("https://shareparking.kr/map/main?userToken="+token);

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

        @JavascriptInterface
        public void navi(String location) {
            //이렇게 받아서 핸드폰에 저장해
            //sharedpreferences 이것을 써서
            Intent intent = new Intent(getActivity(), Navi.class);
            startActivity(intent);
            System.out.println("------------------------------NaviInform--------------------------------");
            System.out.println(location);
            System.out.println("------------------------------------------------------------------");
//            PreferenceManager.setString(nickname, "loginId", nickuser);
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("sFile",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String locations = location;

            String latitude;
            String longitude;
            String parkingName;

            String[] lalo = locations.split("//");
            latitude = lalo[0];
            longitude = lalo[1];
            parkingName = lalo[2];


            editor.putString("latitude", latitude);
            editor.putString("longitude", longitude);
            editor.putString("parkingName", parkingName);
            editor.commit();
            System.out.println("---------------------------------values---------------------------------");
            System.out.println(latitude);
            System.out.println(longitude);
            System.out.println(parkingName);
            System.out.println("------------------------------------------------------------------");
        }
    }
}
