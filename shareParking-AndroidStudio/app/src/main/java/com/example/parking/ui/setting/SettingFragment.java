package com.example.parking.ui.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.parking.MainActivity;
import com.example.parking.R;
import com.example.parking.Signin;

public class SettingFragment extends Fragment {
    private SettingViewModel settingViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_setting, container, false);

        WebView myWebView = myView.findViewById(R.id.webVw_setting);
        myWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        myWebView.addJavascriptInterface(new SettingFragment.AndroidBridge(), "android");
        myWebView.loadUrl("https://shareparking.kr/setting");
//        return root;
        return myView;
    }

    class AndroidBridge {
        @JavascriptInterface
        public void returnMain(String username) {
            Intent intent = new Intent(getActivity().getApplicationContext(), Signin.class);
            startActivity(intent);

        }
    }
}