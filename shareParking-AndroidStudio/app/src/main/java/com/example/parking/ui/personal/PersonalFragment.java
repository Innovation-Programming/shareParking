package com.example.parking.ui.personal;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.parking.Camera;
import com.example.parking.ChatActivity;
import com.example.parking.Navi;
import com.example.parking.R;
import com.example.parking.Signin;
import com.example.parking.ui.map.MapFragment;
import com.example.parking.ui.setting.SettingViewModel;

import java.io.File;
import java.net.URISyntaxException;
import java.security.AccessController;

import static android.content.Context.MODE_PRIVATE;

public class PersonalFragment extends Fragment {
    private PersonalViewModel personalViewModel;
    private WebView myWebView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_personal, container, false);

        myWebView = myView.findViewById(R.id.webVw_personal);
        myWebView.loadUrl("https://shareparking.kr/map/form");
        myWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http://") || url.startsWith("HTTP://") || url.startsWith("https://") || url.startsWith("HTTPS://") || url.startsWith("javascript:")) {
                    // url load 시 필요한 조건이 있을 경우, 추가
                }
                else {
                    // intent 스키마일 경우 처리
                    Intent intent = null;
                    try {
                        intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME); //IntentURI처리
                        Uri uri = Uri.parse(intent.getDataString());

                        getContext().startActivity(new Intent(Intent.ACTION_VIEW, uri));
                        return true;
                    } catch (URISyntaxException ex) {
                        Log.e("<INIPAYMOBILE>", "URI syntax error : " + url + ":" + ex.getMessage());
                        return false;
                    } catch (ActivityNotFoundException e) {
                        if (intent == null) return false;

                        if (handleNotFoundPaymentScheme(getContext(), intent.getScheme()))
                            return true;

                        String packageName = intent.getPackage();
//                    if (packageName != null) {​
//
//
//                    }
                        if (packageName != null) {
                            getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                            return true;
                        }
                        return false;
                    }
                }
                return false;
            }
            /**
             * @param scheme
             * @return 해당 scheme에 대해 처리를 직접 하는지 여부
             * <p>
             * 결제를 위한 3rd-party 앱이 아직 설치되어있지 않아 ActivityNotFoundException이 발생하는 경우 처리합니다.
             * 여기서 handler되지않은 scheme에 대해서는 intent로부터 Package정보 추출이 가능하다면 다음에서 packageName으로 market이동합니다.
             */
            protected boolean handleNotFoundPaymentScheme(Context context, String scheme) {
                //PG사에서 호출하는 url에 package정보가 없어 ActivityNotFoundException이 난 후 market 실행이 안되는 경우
                if (PaymentScheme.ISP.equalsIgnoreCase(scheme)) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_ISP)));
                    return true;
                } else if (PaymentScheme.BANKPAY.equalsIgnoreCase(scheme)) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_BANKPAY)));
                    return true;
                } else if (PaymentScheme.HYUNDAI_APPCARD.equalsIgnoreCase(scheme)) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_HYUNDAI)));
                    return true;
                } else if (PaymentScheme.LOTTE_APPCARD.equalsIgnoreCase(scheme)) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_LOTTE)));
                    return true;
                } else if (PaymentScheme.SAMSUNG_APPCARD.equalsIgnoreCase(scheme)) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_SAMSUNG)));
                    return true;
                }

                return false;
            }


            class PaymentScheme {
                // intent가 아닌 특정 스키마로 시작하는 케이스 정리
                // 확인해 본 케이스는 ISP와 BANKPAY
                public final static String ISP = "ispmobile"; // ISP모바일 : ispmobile://TID=nictest00m01011606281506341724
                public final static String BANKPAY = "kftc-bankpay";

                public final static String LOTTE_APPCARD = "lotteappcard"; // 롯데앱카드 : intent://lottecard/data?acctid=120160628150229605882165497397&apptid=964241&IOS_RETURN_APP=#Intent;scheme=lotteappcard;package=com.lcacApp;end
                public final static String HYUNDAI_APPCARD = "hdcardappcardansimclick"; // 현대앱카드 : intent:hdcardappcardansimclick://appcard?acctid=201606281503270019917080296121#Intent;package=com.hyundaicard.appcard;end;
                public final static String SAMSUNG_APPCARD = "mpocket.online.ansimclick"; // 삼성앱카드 : intent://xid=4752902#Intent;scheme=mpocket.online.ansimclick;package=kr.co.samsungcard.mpocket;end;
                public final static String NH_APPCARD = "nhappcardansimclick"; // NH 앱카드 : intent://appcard?ACCTID=201606281507175365309074630161&P1=1532151#Intent;scheme=nhappcardansimclick;package=nh.smart.mobilecard;end;
                public final static String KB_APPCARD = "kb-acp"; // KB 앱카드 : intent://pay?srCode=0613325&kb-acp://#Intent;scheme=kb-acp;package=com.kbcard.cxh.appcard;end;
                public final static String MOBIPAY = "cloudpay"; // 하나(모비페이) : intent://?tid=2238606309025172#Intent;scheme=cloudpay;package=com.hanaskcard.paycla;end;

                public final static String PACKAGE_ISP = "kvp.jjy.MispAndroid320";
                public final static String PACKAGE_BANKPAY = "com.kftc.bankpay.android";
                public final static String PACKAGE_LOTTE = "com.lcacApp";
                public final static String PACKAGE_HYUNDAI = "com.hyundaicard.appcard";
                public final static String PACKAGE_SAMSUNG = "kr.co.samsungcard.mpocket";

            }
        });

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.addJavascriptInterface(new AndroidBridge(), "androidPersonal");
        webSettings.setDomStorageEnabled(true);

//        return root;
        return myView;
    }
    class AndroidBridge {
        @JavascriptInterface
        public void camera() {
            Intent intent = new Intent(getActivity(), Camera.class);
            startActivity(intent);
        }
    }

}
