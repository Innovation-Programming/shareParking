package com.example.parking.ui.setting;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.parking.MainActivity;
import com.example.parking.R;
import com.example.parking.Signin;
import com.example.parking.Test;

public class SettingFragment extends Fragment {
    private SettingViewModel settingViewModel;

    private long backBtnTime = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_setting, container, false);

        WebView myWebView = myView.findViewById(R.id.webVw_setting);
        myWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        myWebView.canGoBack();
        myWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK
                        && keyEvent.getAction() == MotionEvent.ACTION_UP
                        && myWebView.canGoBack()) {
                    myWebView.goBack();
                    return true;
                }
                return false;
            }
        });

        myWebView.addJavascriptInterface(new SettingFragment.AndroidBridge(), "android");
        myWebView.loadUrl("https://shareparking.kr/setting");
//        return root;
        return myView;
    }

    class AndroidBridge {
        @JavascriptInterface
        public void returnMain() {
//            ActivityCompat.finishAffinity(getActivity());
            Intent intent = new Intent(getContext(), Signin.class);
            startActivity(intent);

            getActivity().finish();
        }
        @JavascriptInterface
        public void goToMain() {
            Intent intent = new Intent(getContext(), Test.class);
            startActivity(intent);
        }
    }
}