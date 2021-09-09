package com.example.parking.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
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

import com.example.parking.R;
import com.example.parking.ui.setting.SettingViewModel;
//public class MapFragment extends AppCompatActivity {
public class MapFragment extends Fragment {
    private MapViewModel mapViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        mapViewModel =
//                ViewModelProviders.of(this).get(MapViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_map, container, false);
//        final TextView textView = root.findViewById(R.id.text_map);
//        mapViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        View myView = inflater.inflate(R.layout.activity_map, container, false);

        WebView myWebView = myView.findViewById(R.id.webVw_map);
//        myWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);

        myWebView.setWebChromeClient(new WebChromeClient() {
           @Override
           public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
               super.onGeolocationPermissionsShowPrompt(origin, callback);
               callback.invoke(origin, true, false);
           }
        });


        myWebView.loadUrl("http://222.232.60.152:90/main");
        return myView;
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
