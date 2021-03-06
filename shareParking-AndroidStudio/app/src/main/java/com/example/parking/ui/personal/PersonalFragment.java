package com.example.parking.ui.personal;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;

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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.parking.Camera;
import com.example.parking.ChatActivity;
import com.example.parking.MainActivity;
import com.example.parking.Navi;
import com.example.parking.R;
import com.example.parking.Signin;
import com.example.parking.Test;
import com.example.parking.ui.chat.ChatFragment;
import com.example.parking.ui.map.MapFragment;
import com.example.parking.ui.setting.SettingViewModel;

import java.io.File;
import java.net.URISyntaxException;
import java.security.AccessController;

import static android.content.Context.MODE_PRIVATE;

public class PersonalFragment extends Fragment {

    public ValueCallback<Uri> filePathCallbackNormal;
    public ValueCallback<Uri[]> filePathCallbackLollipop;
    public final static int FILECHOOSER_NORMAL_REQ_CODE = 2001;
    public final static int FILECHOOSER_LOLLIPOP_REQ_CODE = 2002;
//    private SettingViewModel settingViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_setting, container, false);

        WebView myWebView = myView.findViewById(R.id.webVw_setting);
        myWebView.setWebViewClient(new WebViewClient());
//----------------------------------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------------------------------
//        myWebView.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                if (url.startsWith("http://") || url.startsWith("HTTP://") || url.startsWith("https://") || url.startsWith("HTTPS://") || url.startsWith("javascript:")) {
//                    // url load ??? ????????? ????????? ?????? ??????, ??????
//                }
//                else {
//                    // intent ???????????? ?????? ??????
//                    Intent intent = null;
//                    try {
//                        intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME); //IntentURI??????
//                        Uri uri = Uri.parse(intent.getDataString());
//
//                        getContext().startActivity(new Intent(Intent.ACTION_VIEW, uri));
//                        return true;
//                    } catch (URISyntaxException ex) {
//                        Log.e("<INIPAYMOBILE>", "URI syntax error : " + url + ":" + ex.getMessage());
//                        return false;
//                    } catch (ActivityNotFoundException e) {
//                        if (intent == null) return false;
//
//                        if (handleNotFoundPaymentScheme(getContext(), intent.getScheme()))
//                            return true;
//
//                        String packageName = intent.getPackage();
////                    if (packageName != null) {???
////
////
////                    }
//                        if (packageName != null) {
//                            getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
//                            return true;
//                        }
//                        return false;
//                    }
//                }
//                return false;
//            }
//            /**
//             * @param scheme
//             * @return ?????? scheme??? ?????? ????????? ?????? ????????? ??????
//             * <p>
//             * ????????? ?????? 3rd-party ?????? ?????? ?????????????????? ?????? ActivityNotFoundException??? ???????????? ?????? ???????????????.
//             * ????????? handler???????????? scheme??? ???????????? intent????????? Package?????? ????????? ??????????????? ???????????? packageName?????? market???????????????.
//             */
//            protected boolean handleNotFoundPaymentScheme(Context context, String scheme) {
//                //PG????????? ???????????? url??? package????????? ?????? ActivityNotFoundException??? ??? ??? market ????????? ????????? ??????
//                if (PaymentScheme.ISP.equalsIgnoreCase(scheme)) {
//                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_ISP)));
//                    return true;
//                } else if (PaymentScheme.BANKPAY.equalsIgnoreCase(scheme)) {
//                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_BANKPAY)));
//                    return true;
//                } else if (PaymentScheme.HYUNDAI_APPCARD.equalsIgnoreCase(scheme)) {
//                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_HYUNDAI)));
//                    return true;
//                } else if (PaymentScheme.LOTTE_APPCARD.equalsIgnoreCase(scheme)) {
//                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_LOTTE)));
//                    return true;
//                } else if (PaymentScheme.SAMSUNG_APPCARD.equalsIgnoreCase(scheme)) {
//                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_SAMSUNG)));
//                    return true;
//                }
//
//                return false;
//            }
//
//
//            class PaymentScheme {
//                // intent??? ?????? ?????? ???????????? ???????????? ????????? ??????
//                // ????????? ??? ???????????? ISP??? BANKPAY
//                public final static String ISP = "ispmobile"; // ISP????????? : ispmobile://TID=nictest00m01011606281506341724
//                public final static String BANKPAY = "kftc-bankpay";
//
//                public final static String LOTTE_APPCARD = "lotteappcard"; // ??????????????? : intent://lottecard/data?acctid=120160628150229605882165497397&apptid=964241&IOS_RETURN_APP=#Intent;scheme=lotteappcard;package=com.lcacApp;end
//                public final static String HYUNDAI_APPCARD = "hdcardappcardansimclick"; // ??????????????? : intent:hdcardappcardansimclick://appcard?acctid=201606281503270019917080296121#Intent;package=com.hyundaicard.appcard;end;
//                public final static String SAMSUNG_APPCARD = "mpocket.online.ansimclick"; // ??????????????? : intent://xid=4752902#Intent;scheme=mpocket.online.ansimclick;package=kr.co.samsungcard.mpocket;end;
//                public final static String NH_APPCARD = "nhappcardansimclick"; // NH ????????? : intent://appcard?ACCTID=201606281507175365309074630161&P1=1532151#Intent;scheme=nhappcardansimclick;package=nh.smart.mobilecard;end;
//                public final static String KB_APPCARD = "kb-acp"; // KB ????????? : intent://pay?srCode=0613325&kb-acp://#Intent;scheme=kb-acp;package=com.kbcard.cxh.appcard;end;
//                public final static String MOBIPAY = "cloudpay"; // ??????(????????????) : intent://?tid=2238606309025172#Intent;scheme=cloudpay;package=com.hanaskcard.paycla;end;
//
//                public final static String PACKAGE_ISP = "kvp.jjy.MispAndroid320";
//                public final static String PACKAGE_BANKPAY = "com.kftc.bankpay.android";
//                public final static String PACKAGE_LOTTE = "com.lcacApp";
//                public final static String PACKAGE_HYUNDAI = "com.hyundaicard.appcard";
//                public final static String PACKAGE_SAMSUNG = "kr.co.samsungcard.mpocket";
//            }
//        });

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

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.addJavascriptInterface(new AndroidBridge(), "Personal");


//        myWebView.addJavascriptInterface(new SettingFragment.AndroidBridge(), "android");
        myWebView.loadUrl("https://shareparking.kr/community");
//        return root;
        return myView;
    }
//----------------------------------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------------------------------

    class AndroidBridge {
        @JavascriptInterface
        public void myParkingLot() {
            Intent intent = new Intent(getActivity(), Test.class);
            startActivity(intent);
            System.out.println("TestActivity: ????????????");
        }

//        @JavascriptInterface
//        public void goToMain() {
//            Intent intent = new Intent(getActivity(), MainActivity.class);
//            startActivity(intent);
//        }
    }

}

//    public ValueCallback<Uri> filePathCallbackNormal;
//    public ValueCallback<Uri[]> filePathCallbackLollipop;
//    public final static int FILECHOOSER_NORMAL_REQ_CODE = 2001;
//    public final static int FILECHOOSER_LOLLIPOP_REQ_CODE = 2002;
////    private SettingViewModel settingViewModel;
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        View myView = inflater.inflate(R.layout.activity_setting, container, false);
//
//        WebView myWebView = myView.findViewById(R.id.webVw_setting);
////        myWebView.setWebViewClient(new WebViewClient());
////----------------------------------------------------------------------------------------------------------------
//
////----------------------------------------------------------------------------------------------------------------
//        myWebView.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                if (url.startsWith("http://") || url.startsWith("HTTP://") || url.startsWith("https://") || url.startsWith("HTTPS://") || url.startsWith("javascript:")) {
//                    // url load ??? ????????? ????????? ?????? ??????, ??????
//                }
//                else {
//                    // intent ???????????? ?????? ??????
//                    Intent intent = null;
//                    try {
//                        intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME); //IntentURI??????
//                        Uri uri = Uri.parse(intent.getDataString());
//
//                        getContext().startActivity(new Intent(Intent.ACTION_VIEW, uri));
//                        return true;
//                    } catch (URISyntaxException ex) {
//                        Log.e("<INIPAYMOBILE>", "URI syntax error : " + url + ":" + ex.getMessage());
//                        return false;
//                    } catch (ActivityNotFoundException e) {
//                        if (intent == null) return false;
//
//                        if (handleNotFoundPaymentScheme(getContext(), intent.getScheme()))
//                            return true;
//
//                        String packageName = intent.getPackage();
////                    if (packageName != null) {???
////
////
////                    }
//                        if (packageName != null) {
//                            getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
//                            return true;
//                        }
//                        return false;
//                    }
//                }
//                return false;
//            }
//            /**
//             * @param scheme
//             * @return ?????? scheme??? ?????? ????????? ?????? ????????? ??????
//             * <p>
//             * ????????? ?????? 3rd-party ?????? ?????? ?????????????????? ?????? ActivityNotFoundException??? ???????????? ?????? ???????????????.
//             * ????????? handler???????????? scheme??? ???????????? intent????????? Package?????? ????????? ??????????????? ???????????? packageName?????? market???????????????.
//             */
//            protected boolean handleNotFoundPaymentScheme(Context context, String scheme) {
//                //PG????????? ???????????? url??? package????????? ?????? ActivityNotFoundException??? ??? ??? market ????????? ????????? ??????
//                if (PaymentScheme.ISP.equalsIgnoreCase(scheme)) {
//                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_ISP)));
//                    return true;
//                } else if (PaymentScheme.BANKPAY.equalsIgnoreCase(scheme)) {
//                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_BANKPAY)));
//                    return true;
//                } else if (PaymentScheme.HYUNDAI_APPCARD.equalsIgnoreCase(scheme)) {
//                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_HYUNDAI)));
//                    return true;
//                } else if (PaymentScheme.LOTTE_APPCARD.equalsIgnoreCase(scheme)) {
//                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_LOTTE)));
//                    return true;
//                } else if (PaymentScheme.SAMSUNG_APPCARD.equalsIgnoreCase(scheme)) {
//                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_SAMSUNG)));
//                    return true;
//                }
//
//                return false;
//            }
//
//
//            class PaymentScheme {
//                // intent??? ?????? ?????? ???????????? ???????????? ????????? ??????
//                // ????????? ??? ???????????? ISP??? BANKPAY
//                public final static String ISP = "ispmobile"; // ISP????????? : ispmobile://TID=nictest00m01011606281506341724
//                public final static String BANKPAY = "kftc-bankpay";
//
//                public final static String LOTTE_APPCARD = "lotteappcard"; // ??????????????? : intent://lottecard/data?acctid=120160628150229605882165497397&apptid=964241&IOS_RETURN_APP=#Intent;scheme=lotteappcard;package=com.lcacApp;end
//                public final static String HYUNDAI_APPCARD = "hdcardappcardansimclick"; // ??????????????? : intent:hdcardappcardansimclick://appcard?acctid=201606281503270019917080296121#Intent;package=com.hyundaicard.appcard;end;
//                public final static String SAMSUNG_APPCARD = "mpocket.online.ansimclick"; // ??????????????? : intent://xid=4752902#Intent;scheme=mpocket.online.ansimclick;package=kr.co.samsungcard.mpocket;end;
//                public final static String NH_APPCARD = "nhappcardansimclick"; // NH ????????? : intent://appcard?ACCTID=201606281507175365309074630161&P1=1532151#Intent;scheme=nhappcardansimclick;package=nh.smart.mobilecard;end;
//                public final static String KB_APPCARD = "kb-acp"; // KB ????????? : intent://pay?srCode=0613325&kb-acp://#Intent;scheme=kb-acp;package=com.kbcard.cxh.appcard;end;
//                public final static String MOBIPAY = "cloudpay"; // ??????(????????????) : intent://?tid=2238606309025172#Intent;scheme=cloudpay;package=com.hanaskcard.paycla;end;
//
//                public final static String PACKAGE_ISP = "kvp.jjy.MispAndroid320";
//                public final static String PACKAGE_BANKPAY = "com.kftc.bankpay.android";
//                public final static String PACKAGE_LOTTE = "com.lcacApp";
//                public final static String PACKAGE_HYUNDAI = "com.hyundaicard.appcard";
//                public final static String PACKAGE_SAMSUNG = "kr.co.samsungcard.mpocket";
//            }
//        });
//        WebSettings webSettings = myWebView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        myWebView.addJavascriptInterface(new PersonalFragment.AndroidBridge(), "androidPersonal");
//
////        myWebView.addJavascriptInterface(new SettingFragment.AndroidBridge(), "android");
//        myWebView.loadUrl("https://shareparking.kr/mypage");
////        return root;
//        return myView;
//    }
////----------------------------------------------------------------------------------------------------------------
//
////----------------------------------------------------------------------------------------------------------------
//
//    class AndroidBridge {
//        @JavascriptInterface
//        public void myParkingLot() {
//            Intent intent = new Intent(getActivity(), Test.class);
//            startActivity(intent);
//            System.out.println("TestActivity: ????????????");
//        }
//    }
//}

//-------------------------------------------------------------------------------------------------

//public class PersonalFragment extends Fragment {
//    private static final int RESULT_OK = -1;
//    private long time = 0;
//
//    public ValueCallback<Uri> filePathCallbackNormal;
//    public ValueCallback<Uri[]> filePathCallbackLollipop;
//    public final static int FILECHOOSER_NORMAL_REQ_CODE = 2001;
//    public final static int FILECHOOSER_LOLLIPOP_REQ_CODE = 2002;
//
//    private Uri cameraImageUri = null;
//    private PersonalViewModel personalViewModel;
//    private WebView myWebView;
//    ValueCallback mFilePathCallback;
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        View myView = inflater.inflate(R.layout.activity_personal, container, false);
//
//        myWebView = myView.findViewById(R.id.webVw_personal);
//
//        checkVerify();
//
//        WebSettings webSettings = myWebView.getSettings();
//        webSettings.setJavaScriptEnabled(true); //?????????????????? ??????
//        webSettings.setSupportMultipleWindows(true); //?????? ????????? ??????
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //?????????????????? ??????????????? ??????
//        webSettings.setLoadWithOverviewMode(true); //???????????? ??????
//        webSettings.setUseWideViewPort(true); //?????? ????????? ????????? ??????
//        webSettings.setSupportZoom(false); //?????? ??? ?????? ??????
//        webSettings.setBuiltInZoomControls(false); //?????? ?????? ?????? ?????? ??????
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webSettings.setDomStorageEnabled(true);
//
//
//        myWebView.setWebChromeClient(new WebChromeClient(){
//            // ????????????????????? alert???
//            @Override
//            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
//                new android.app.AlertDialog.Builder(view.getContext())
//                        .setTitle("Alert")
//                        .setMessage(message)
//                        .setPositiveButton(android.R.string.ok,
//                                new android.app.AlertDialog.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        result.confirm();
//                                    }
//                                })
//                        .setCancelable(false)
//                        .create()
//                        .show();
//                return true;
//            }
//
//            @Override
//            public boolean onJsConfirm(WebView view, String url, String message,
//                                       final JsResult result) {
//                new android.app.AlertDialog.Builder(view.getContext())
//                        .setTitle("Confirm")
//                        .setMessage(message)
//                        .setPositiveButton("Yes",
//                                new android.app.AlertDialog.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        result.confirm();
//                                    }
//                                })
//                        .setNegativeButton("No",
//                                new android.app.AlertDialog.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        result.cancel();
//                                    }
//                                })
//                        .setCancelable(false)
//                        .create()
//                        .show();
//                return true;
//            }
//
//            // For Android 5.0+ ????????? - input type="file" ????????? ???????????? ??? ??????
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            public boolean onShowFileChooser(
//                    WebView webView, ValueCallback<Uri[]> filePathCallback,
//                    FileChooserParams fileChooserParams) {
//                Log.d("MainActivity", "5.0+");
//
//                // Callback ????????? (??????!)
//                if (filePathCallbackLollipop != null) {
//                    filePathCallbackLollipop.onReceiveValue(null);
//                    filePathCallbackLollipop = null;
//                }
//                filePathCallbackLollipop = filePathCallback;
//
//                boolean isCapture = fileChooserParams.isCaptureEnabled();
//                runCamera(isCapture);
//                return true;
//            }
//        });
//
//        myWebView.loadUrl("https://shareparking.kr/map/form");
////        myWebView.setWebChromeClient(new WebChromeClient() {
////            @Override
////            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
////                return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
//////                mFilePathCallback = filePathCallback;
////
//////                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
////                intent.addCategory(Intent.CATEGORY_OPENABLE);
////                intent.setType("image/*");
////
////                startActivityForResult(intent, 0);
////                return true;
////
////            }
////
////
////        });
//        myWebView.setWebViewClient(new WebViewClientClass());
////        myWebView.setWebViewClient(new WebViewClient(){
////            @Override
////            public boolean shouldOverrideUrlLoading(WebView view, String url) {
////                if (url.startsWith("http://") || url.startsWith("HTTP://") || url.startsWith("https://") || url.startsWith("HTTPS://") || url.startsWith("javascript:")) {
////                    // url load ??? ????????? ????????? ?????? ??????, ??????
////                }
////                else {
////                    // intent ???????????? ?????? ??????
////                    Intent intent = null;
////                    try {
////                        intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME); //IntentURI??????
////                        Uri uri = Uri.parse(intent.getDataString());
////
////                        getContext().startActivity(new Intent(Intent.ACTION_VIEW, uri));
////                        return true;
////                    } catch (URISyntaxException ex) {
////                        Log.e("<INIPAYMOBILE>", "URI syntax error : " + url + ":" + ex.getMessage());
////                        return false;
////                    } catch (ActivityNotFoundException e) {
////                        if (intent == null) return false;
////
////                        if (handleNotFoundPaymentScheme(getContext(), intent.getScheme()))
////                            return true;
////
////                        String packageName = intent.getPackage();
//////                    if (packageName != null) {???
//////
//////
//////                    }
////                        if (packageName != null) {
////                            getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
////                            return true;
////                        }
////                        return false;
////                    }
////                }
////                return false;
////            }
////            /**
////             * @param scheme
////             * @return ?????? scheme??? ?????? ????????? ?????? ????????? ??????
////             * <p>
////             * ????????? ?????? 3rd-party ?????? ?????? ?????????????????? ?????? ActivityNotFoundException??? ???????????? ?????? ???????????????.
////             * ????????? handler???????????? scheme??? ???????????? intent????????? Package?????? ????????? ??????????????? ???????????? packageName?????? market???????????????.
////             */
////            protected boolean handleNotFoundPaymentScheme(Context context, String scheme) {
////                //PG????????? ???????????? url??? package????????? ?????? ActivityNotFoundException??? ??? ??? market ????????? ????????? ??????
////                if (PaymentScheme.ISP.equalsIgnoreCase(scheme)) {
////                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_ISP)));
////                    return true;
////                } else if (PaymentScheme.BANKPAY.equalsIgnoreCase(scheme)) {
////                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_BANKPAY)));
////                    return true;
////                } else if (PaymentScheme.HYUNDAI_APPCARD.equalsIgnoreCase(scheme)) {
////                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_HYUNDAI)));
////                    return true;
////                } else if (PaymentScheme.LOTTE_APPCARD.equalsIgnoreCase(scheme)) {
////                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_LOTTE)));
////                    return true;
////                } else if (PaymentScheme.SAMSUNG_APPCARD.equalsIgnoreCase(scheme)) {
////                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_SAMSUNG)));
////                    return true;
////                }
////
////                return false;
////            }
////
////
////            class PaymentScheme {
////                // intent??? ?????? ?????? ???????????? ???????????? ????????? ??????
////                // ????????? ??? ???????????? ISP??? BANKPAY
////                public final static String ISP = "ispmobile"; // ISP????????? : ispmobile://TID=nictest00m01011606281506341724
////                public final static String BANKPAY = "kftc-bankpay";
////
////                public final static String LOTTE_APPCARD = "lotteappcard"; // ??????????????? : intent://lottecard/data?acctid=120160628150229605882165497397&apptid=964241&IOS_RETURN_APP=#Intent;scheme=lotteappcard;package=com.lcacApp;end
////                public final static String HYUNDAI_APPCARD = "hdcardappcardansimclick"; // ??????????????? : intent:hdcardappcardansimclick://appcard?acctid=201606281503270019917080296121#Intent;package=com.hyundaicard.appcard;end;
////                public final static String SAMSUNG_APPCARD = "mpocket.online.ansimclick"; // ??????????????? : intent://xid=4752902#Intent;scheme=mpocket.online.ansimclick;package=kr.co.samsungcard.mpocket;end;
////                public final static String NH_APPCARD = "nhappcardansimclick"; // NH ????????? : intent://appcard?ACCTID=201606281507175365309074630161&P1=1532151#Intent;scheme=nhappcardansimclick;package=nh.smart.mobilecard;end;
////                public final static String KB_APPCARD = "kb-acp"; // KB ????????? : intent://pay?srCode=0613325&kb-acp://#Intent;scheme=kb-acp;package=com.kbcard.cxh.appcard;end;
////                public final static String MOBIPAY = "cloudpay"; // ??????(????????????) : intent://?tid=2238606309025172#Intent;scheme=cloudpay;package=com.hanaskcard.paycla;end;
////
////                public final static String PACKAGE_ISP = "kvp.jjy.MispAndroid320";
////                public final static String PACKAGE_BANKPAY = "com.kftc.bankpay.android";
////                public final static String PACKAGE_LOTTE = "com.lcacApp";
////                public final static String PACKAGE_HYUNDAI = "com.hyundaicard.appcard";
////                public final static String PACKAGE_SAMSUNG = "kr.co.samsungcard.mpocket";
////            }
////        });
//
//
////        webSettings.setJavaScriptEnabled(true);
////        webSettings.setMediaPlaybackRequiresUserGesture(false);
////        webSettings.setPluginState(WebSettings.PluginState.ON);
//        myWebView.addJavascriptInterface(new PersonalFragment.AndroidBridge(), "personal");
////        webSettings.setDomStorageEnabled(true);
//
////        return root;
//        return myView;
//    }
//
//    //?????? ?????? ?????? ??????
//    @TargetApi(Build.VERSION_CODES.M)
//    public void checkVerify() {
//
//        if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(),Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
////            Log.d("checkVerify() : ","if??? ?????????");
//
//            //????????? ?????? ???????????? ?????? ?????? ?????? ??????
//            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.CAMERA)) {
//
//                Toast.makeText(getContext(),"?????? ?????? ????????? ????????? ????????? ????????? ??????????????? ???????????? ???????????? ?????????????????????.",Toast.LENGTH_SHORT).show();
//
//            } else {
////                Log.d("checkVerify() : ","????????? ??? ???????????? ?????? ??????");
//                // ????????? ??? ???????????? ?????? ??????
//                ActivityCompat.requestPermissions(this.getActivity(),new String[]{Manifest.permission.INTERNET, Manifest.permission.CAMERA,
//                        Manifest.permission.ACCESS_NETWORK_STATE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//            }
//        }
//    }
//
//    //?????? ?????? ????????? ?????? ?????? ??????
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
//    {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        //Log.d("onRequestPermissionsResult() : ","?????????");
//
//        if (requestCode == 1)
//        {
//            if (grantResults.length > 0)
//            {
//                for (int i=0; i<grantResults.length; ++i)
//                {
//                    if (grantResults[i] == PackageManager.PERMISSION_DENIED)
//                    {
//                        // ?????????, ????????? ??? ???????????? ??????????????? ????????? ?????? ????????? ??????
//                        new AlertDialog.Builder(this.getContext()).setTitle("??????").setMessage("????????? ?????????????????? ?????? ????????? ??? ????????????.")
//                                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                        getActivity().getFragmentManager().popBackStack();
//                                    }
//                                }).setNegativeButton("?????? ??????", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                                        .setData(Uri.parse("package:" + getContext().getPackageName()));
//                                getContext().startActivity(intent);
//                            }
//                        }).setCancelable(false).show();
//
//                        return;
//                    }
//                }
//
//            }
//        }
//    }
//
//    //??????????????? ????????? ??? ????????? ?????? ????????? ????????? ??? ??????
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.d("onActivityResult() ","resultCode = " + Integer.toString(requestCode));
//
//        switch (requestCode)
//        {
//            case FILECHOOSER_NORMAL_REQ_CODE:
//                if (resultCode == RESULT_OK)
//                {
//                    if (filePathCallbackNormal == null) return;
//                    Uri result = (data == null || resultCode != RESULT_OK) ? null : data.getData();
//                    //  onReceiveValue ??? ????????? ????????????.
//                    filePathCallbackNormal.onReceiveValue(result);
//                    filePathCallbackNormal = null;
//                }
//                break;
//            case FILECHOOSER_LOLLIPOP_REQ_CODE:
//                Log.d("onActivityResult() ","FILECHOOSER_LOLLIPOP_REQ_CODE = " + Integer.toString(FILECHOOSER_LOLLIPOP_REQ_CODE));
//
//                if (resultCode == RESULT_OK)
//                {
//                    Log.d("onActivityResult() ","FILECHOOSER_LOLLIPOP_REQ_CODE ??? if???  RESULT_OK ?????? ?????????");
//
//                    if (filePathCallbackLollipop == null) return;
//                    if (data == null)
//                        data = new Intent();
//                    if (data.getData() == null)
//                        data.setData(cameraImageUri);
//
//                    filePathCallbackLollipop.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
//                    filePathCallbackLollipop = null;
//                }
//                else
//                {
//                    Log.d("onActivityResult() ","FILECHOOSER_LOLLIPOP_REQ_CODE ??? if?????? else??? ?????????~");
//                    if (filePathCallbackLollipop != null)
//                    {   //  resultCode??? RESULT_OK??? ???????????? ????????? null ???????????? ??????.(????????? ?????? ????????? ???????????? input ????????? ???????????? ???????????? ??????)
//
//                        Log.d("onActivityResult() ","FILECHOOSER_LOLLIPOP_REQ_CODE ??? if?????? filePathCallbackLollipop??? null??? ?????????");
//                        filePathCallbackLollipop.onReceiveValue(null);
//                        filePathCallbackLollipop = null;
//                    }
//
//                    if (filePathCallbackNormal != null)
//                    {
//                        filePathCallbackNormal.onReceiveValue(null);
//                        filePathCallbackNormal = null;
//                    }
//                }
//                break;
//            default:
//
//                break;
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    // ????????? ?????? ??????
//    private void runCamera(boolean _isCapture)
//    {
//        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        //intentCamera.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        //File path = getFilesDir();
//        File path = Environment.getExternalStorageDirectory();
//        File file = new File(path, "sample.png"); // sample.png ??? ???????????? ????????? ??? ????????? ?????????????????? ????????? ????????????
//        // File ????????? URI ??? ?????????.
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
//        {
//            String strpa = getContext().getPackageName();
//            cameraImageUri = FileProvider.getUriForFile(getContext(), strpa + ".fileprovider", file);
//        }
//        else
//        {
//            cameraImageUri = Uri.fromFile(file);
//        }
//        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
//
//        if (!_isCapture)
//        { // ???????????? ?????????, ????????? ?????? ????????? ?????? ???
//
//            Intent pickIntent = new Intent(Intent.ACTION_PICK);
//            pickIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//            pickIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//            String pickTitle = "?????? ????????? ????????? ???????????????.";
//            Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
//
//            // ????????? intent ???????????????..
//            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{intentCamera});
//            startActivityForResult(chooserIntent, FILECHOOSER_LOLLIPOP_REQ_CODE);
//        }
//        else
//        {// ?????? ????????? ??????..
//            startActivityForResult(intentCamera, FILECHOOSER_LOLLIPOP_REQ_CODE);
//        }
//    }
//
////    @Override
////    public boolean onKeyDown(int keyCode, KeyEvent event) {
////
////        if ((keyCode==KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
////            myWebView.goBack();
////            return true;
////        }
////        return super.onKeyDown(keyCode, event);
////    }
//
//    private class WebChromeClientClass extends WebChromeClient {
//        // ????????????????????? alert???
//        @Override
//        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
//            new AlertDialog.Builder(view.getContext())
//                    .setTitle("Alert")
//                    .setMessage(message)
//                    .setPositiveButton(android.R.string.ok,
//                            new AlertDialog.OnClickListener(){
//                                public void onClick(DialogInterface dialog, int which) {
//                                    result.confirm();
//                                }
//                            })
//                    .setCancelable(false)
//                    .create()
//                    .show();
//            return true;
//        }
//
//        // ????????????????????? confirm???
//        @Override
//        public boolean onJsConfirm(WebView view, String url, String message,
//                                   final JsResult result) {
//            new AlertDialog.Builder(view.getContext())
//                    .setTitle("Confirm")
//                    .setMessage(message)
//                    .setPositiveButton("Yes",
//                            new AlertDialog.OnClickListener(){
//                                public void onClick(DialogInterface dialog, int which) {
//                                    result.confirm();
//                                }
//                            })
//                    .setNegativeButton("No",
//                            new AlertDialog.OnClickListener(){
//                                public void onClick(DialogInterface dialog, int which) {
//                                    result.cancel();
//                                }
//                            })
//                    .setCancelable(false)
//                    .create()
//                    .show();
//            return true;
//        }
//
//        // For Android 5.0+ ????????? - input type="file" ????????? ???????????? ??? ??????
//        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//        public boolean onShowFileChooser(
//                WebView webView, ValueCallback<Uri[]> filePathCallback,
//                FileChooserParams fileChooserParams) {
////            Log.d("MainActivity - onShowFileChooser", "5.0+");
//
//            // Callback ????????? (??????!)
//            if (filePathCallbackLollipop != null) {
//                filePathCallbackLollipop.onReceiveValue(null);
//                filePathCallbackLollipop = null;
//            }
//            filePathCallbackLollipop = filePathCallback;
//
//            boolean isCapture = fileChooserParams.isCaptureEnabled();
//
//            Log.d("onShowFileChooser : " , String.valueOf(isCapture));
//            runCamera(isCapture);
//            return true;
//        }
//    }
//
//    private class WebViewClientClass extends WebViewClient {
//        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//            //Log.d("WebViewClient URL : " , request.getUrl().toString());
//            view.loadUrl(request.getUrl().toString());
//            return true;
//            //return super.shouldOverrideUrlLoading(view, request);
//        }
//    }
//
//    class AndroidBridge {
//        @JavascriptInterface
//        public void camera() {
//            Intent intent = new Intent(getActivity(), Camera.class);
//            startActivity(intent);
//            System.out.println("cameraAction: ????????????");
//        }
//
//        @JavascriptInterface
//        public void goToMain() {
//            Intent intent = new Intent(getContext(), MainActivity.class);
//            startActivity(intent);
//        }
//    }

//}
//-------------------------------------------------------------------------------------------------


