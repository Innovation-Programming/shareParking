package com.example.parking.ui.chat;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.example.parking.Camera;
import com.example.parking.ChatActivity;
import com.example.parking.ChatData;
import com.example.parking.MainActivity;
import com.example.parking.R;
import com.example.parking.Signin;
import com.example.parking.Test;
import com.example.parking.ui.personal.PersonalFragment;
import com.example.parking.ui.setting.SettingFragment;
import com.example.parking.ui.setting.SettingViewModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ChatFragment extends Fragment {

    public ValueCallback<Uri> filePathCallbackNormal;
    public ValueCallback<Uri[]> filePathCallbackLollipop;
    public final static int FILECHOOSER_NORMAL_REQ_CODE = 2001;
    public final static int FILECHOOSER_LOLLIPOP_REQ_CODE = 2002;
//    private SettingViewModel settingViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_setting, container, false);

        WebView myWebView = myView.findViewById(R.id.webVw_setting);
//        myWebView.setWebViewClient(new WebViewClient());
//----------------------------------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------------------------------
        myWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http://") || url.startsWith("HTTP://") || url.startsWith("https://") || url.startsWith("HTTPS://") || url.startsWith("javascript:")) {
                    // url load ??? ????????? ????????? ?????? ??????, ??????
                }
                else {
                    // intent ???????????? ?????? ??????
                    Intent intent = null;
                    try {
                        intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME); //IntentURI??????
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
//                    if (packageName != null) {???
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
             * @return ?????? scheme??? ?????? ????????? ?????? ????????? ??????
             * <p>
             * ????????? ?????? 3rd-party ?????? ?????? ?????????????????? ?????? ActivityNotFoundException??? ???????????? ?????? ???????????????.
             * ????????? handler???????????? scheme??? ???????????? intent????????? Package?????? ????????? ??????????????? ???????????? packageName?????? market???????????????.
             */
            protected boolean handleNotFoundPaymentScheme(Context context, String scheme) {
                //PG????????? ???????????? url??? package????????? ?????? ActivityNotFoundException??? ??? ??? market ????????? ????????? ??????
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
                // intent??? ?????? ?????? ???????????? ???????????? ????????? ??????
                // ????????? ??? ???????????? ISP??? BANKPAY
                public final static String ISP = "ispmobile"; // ISP????????? : ispmobile://TID=nictest00m01011606281506341724
                public final static String BANKPAY = "kftc-bankpay";

                public final static String LOTTE_APPCARD = "lotteappcard"; // ??????????????? : intent://lottecard/data?acctid=120160628150229605882165497397&apptid=964241&IOS_RETURN_APP=#Intent;scheme=lotteappcard;package=com.lcacApp;end
                public final static String HYUNDAI_APPCARD = "hdcardappcardansimclick"; // ??????????????? : intent:hdcardappcardansimclick://appcard?acctid=201606281503270019917080296121#Intent;package=com.hyundaicard.appcard;end;
                public final static String SAMSUNG_APPCARD = "mpocket.online.ansimclick"; // ??????????????? : intent://xid=4752902#Intent;scheme=mpocket.online.ansimclick;package=kr.co.samsungcard.mpocket;end;
                public final static String NH_APPCARD = "nhappcardansimclick"; // NH ????????? : intent://appcard?ACCTID=201606281507175365309074630161&P1=1532151#Intent;scheme=nhappcardansimclick;package=nh.smart.mobilecard;end;
                public final static String KB_APPCARD = "kb-acp"; // KB ????????? : intent://pay?srCode=0613325&kb-acp://#Intent;scheme=kb-acp;package=com.kbcard.cxh.appcard;end;
                public final static String MOBIPAY = "cloudpay"; // ??????(????????????) : intent://?tid=2238606309025172#Intent;scheme=cloudpay;package=com.hanaskcard.paycla;end;

                public final static String PACKAGE_ISP = "kvp.jjy.MispAndroid320";
                public final static String PACKAGE_BANKPAY = "com.kftc.bankpay.android";
                public final static String PACKAGE_LOTTE = "com.lcacApp";
                public final static String PACKAGE_HYUNDAI = "com.hyundaicard.appcard";
                public final static String PACKAGE_SAMSUNG = "kr.co.samsungcard.mpocket";
            }
        });

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
        myWebView.loadUrl("https://shareparking.kr/mypage");
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

        @JavascriptInterface
        public void personalChatRoom(String parkingAdmin) {
            //????????? ????????? ???????????? ?????????
            //sharedpreferences ????????? ??????
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
}


//    private DatabaseReference chattingList;
//    private ChatViewModel chatViewModel;
//    private ArrayList<String> list2, displayList;
//    public String reqUser;
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        chatViewModel =
//                ViewModelProviders.of(this).get(ChatViewModel.class);
//        final View root = inflater.inflate(R.layout.fragment_chat, container, false);
//        final ListView list_view = (ListView) root.findViewById(R.id.list2);
//
//        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                getContext(),
//                android.R.layout.simple_list_item_1,
//                list2
//        );
//
//        final SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("sFile", Context.MODE_PRIVATE);
//        final SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        reqUser = sharedPreferences.getString("loginId","user");
//        System.out.println("*********************************************************************");
//        System.out.println(reqUser);
//        System.out.println("*********************************************************************");
//
//        displayList = new ArrayList<String>();
//        list2 = new ArrayList<String>();// ????????? ?????? ??????????????? ?????? ?????????
//        FirebaseDatabase database = FirebaseDatabase.getInstance(); //?????????????????? ?????? ??? ???
//        chattingList = database.getReference("chattingList"); //chattingList ??? ?????????
//        System.out.println("toString: "+chattingList.toString());
//
//        chattingList.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Log.d("ChattingList", "list : " + dataSnapshot.getValue());
//
//                displayList.clear();
//
//                list2.add(dataSnapshot.getValue().toString());
//                System.out.println("size:"+ list2.size());
//                System.out.println("list2:"+ list2);
//
//                for(int i = 0; i < list2.size(); i ++) {
//                    String[] sep = list2.get(i).split("&");
//                    if(sep[0].equals(reqUser) || sep[1].equals(reqUser)){
//                        if(!sep[0].equals(reqUser)){
//                            displayList.add(sep[0]);
//                        }
//                        else if(!sep[1].equals(reqUser)){
//                            displayList.add(sep[1]);
//                        }
//                    }
//                }
//
//                System.out.println("--------------------------------??????????????????(in the void method)----------------------------------");
//
//
//                list_view.setAdapter(new ArrayAdapter<String>(
//                        getContext(),
//                        android.R.layout.simple_list_item_1,
//                        displayList
//                ));
//
//                list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        System.out.println(list2.size());
//                        System.out.println("???????????????: " + list2);
//
//                        for(int j = 0; j < list2.size(); j ++){
//                            String[] check = list2.get(j).split("&");
//                            if(check[0].equals(reqUser) && check[1].equals(displayList.get(i))){
//                                System.out.println("???????????????: " + list2.get(j));
//                                editor.putString("roomName", list2.get(j));
//                                editor.commit();
//                            }
//                            else if(check[1].equals(reqUser) && check[0].equals(displayList.get(i))){
//                                System.out.println("???????????????: " + list2.get(j));
//                                editor.putString("roomName", list2.get(j));
//                                editor.commit();
//                            }
//                        }
//
//                        String sendName = sharedPreferences.getString("roomName", "user");
//                        System.out.println("????????????????: "+ sendName);
//
//                        Intent intent = new Intent(getActivity(), ChatActivity.class);
//                        startActivity(intent);
//                        Toast.makeText(getContext(), displayList.get(i)+"????????? ?????? ??????", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                System.out.println("-----------------------------------------------------------------------------------------------");
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//        });
//
//        System.out.println("--------------------------------??????????????????(out of void method)----------------------------------");
//        for(int i = 0; i < list2.size(); i ++) {
//            System.out.println(list2.get(i));
//        }
//        System.out.println("-----------------------------------------------------------------------------------------------");
//
//        return root;
//    }
