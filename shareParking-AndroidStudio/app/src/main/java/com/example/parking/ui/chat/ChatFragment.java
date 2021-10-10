package com.example.parking.ui.chat;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
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

public class ChatFragment extends Fragment {
//    private SettingViewModel settingViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_setting, container, false);

        WebView myWebView = myView.findViewById(R.id.webVw_setting);
//        myWebView.setWebViewClient(new WebViewClient());
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
        myWebView.addJavascriptInterface(new ChatFragment.AndroidBridge(), "androidPersonal");

//        myWebView.addJavascriptInterface(new SettingFragment.AndroidBridge(), "android");
        myWebView.loadUrl("https://shareparking.kr/mypage");
//        return root;
        return myView;
    }

    class AndroidBridge {
        @JavascriptInterface
        public void myParkingLot() {
            Intent intent = new Intent(getActivity(), Test.class);
            startActivity(intent);
            System.out.println("TestActivity: 넘어갔음");
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
//        list2 = new ArrayList<String>();// 채팅방 목록 표현해주기 위한 리스트
//        FirebaseDatabase database = FirebaseDatabase.getInstance(); //데이터베이스 생성 및 선
//        chattingList = database.getReference("chattingList"); //chattingList 값 가져오
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
//                System.out.println("--------------------------------채팅방리스트(in the void method)----------------------------------");
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
//                        System.out.println("윽액악악악: " + list2);
//
//                        for(int j = 0; j < list2.size(); j ++){
//                            String[] check = list2.get(j).split("&");
//                            if(check[0].equals(reqUser) && check[1].equals(displayList.get(i))){
//                                System.out.println("윽액악악악: " + list2.get(j));
//                                editor.putString("roomName", list2.get(j));
//                                editor.commit();
//                            }
//                            else if(check[1].equals(reqUser) && check[0].equals(displayList.get(i))){
//                                System.out.println("윽액악악악: " + list2.get(j));
//                                editor.putString("roomName", list2.get(j));
//                                editor.commit();
//                            }
//                        }
//
//                        String sendName = sharedPreferences.getString("roomName", "user");
//                        System.out.println("넌무엇이냐?: "+ sendName);
//
//                        Intent intent = new Intent(getActivity(), ChatActivity.class);
//                        startActivity(intent);
//                        Toast.makeText(getContext(), displayList.get(i)+"님과의 채팅 입장", Toast.LENGTH_SHORT).show();
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
//        System.out.println("--------------------------------채팅방리스트(out of void method)----------------------------------");
//        for(int i = 0; i < list2.size(); i ++) {
//            System.out.println(list2.get(i));
//        }
//        System.out.println("-----------------------------------------------------------------------------------------------");
//
//        return root;
//    }
