package com.example.parking.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.example.parking.ChatActivity;
import com.example.parking.ChatData;
import com.example.parking.MainActivity;
import com.example.parking.R;
import com.example.parking.ui.setting.SettingViewModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    private DatabaseReference chattingList;
    private ChatViewModel chatViewModel;
    private ArrayList<String> list2, displayList;
    public String reqUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        chatViewModel =
                ViewModelProviders.of(this).get(ChatViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_chat, container, false);
        final ListView list_view = (ListView) root.findViewById(R.id.list2);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1,
                list2
        );

        final SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("sFile", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        reqUser = sharedPreferences.getString("loginId","user");
        System.out.println("*********************************************************************");
        System.out.println(reqUser);
        System.out.println("*********************************************************************");

        displayList = new ArrayList<String>();
        list2 = new ArrayList<String>();// 채팅방 목록 표현해주기 위한 리스트
        FirebaseDatabase database = FirebaseDatabase.getInstance(); //데이터베이스 생성 및 선
        chattingList = database.getReference("chattingList"); //chattingList 값 가져오
        System.out.println("toString: "+chattingList.toString());

        chattingList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("ChattingList", "list : " + dataSnapshot.getValue());

                displayList.clear();

                list2.add(dataSnapshot.getValue().toString());
                System.out.println("size:"+ list2.size());
                System.out.println("list2:"+ list2);

                for(int i = 0; i < list2.size(); i ++) {
                    String[] sep = list2.get(i).split("&");
                    if(sep[0].equals(reqUser) || sep[1].equals(reqUser)){
                        if(!sep[0].equals(reqUser)){
                            displayList.add(sep[0]);
                        }
                        else if(!sep[1].equals(reqUser)){
                            displayList.add(sep[1]);
                        }
                    }
                }

                System.out.println("--------------------------------채팅방리스트(in the void method)----------------------------------");


                list_view.setAdapter(new ArrayAdapter<String>(
                        getContext(),
                        android.R.layout.simple_list_item_1,
                        displayList
                ));

                list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        System.out.println(list2.size());
                        System.out.println("윽액악악악: " + list2);

                        for(int j = 0; j < list2.size(); j ++){
                            String[] check = list2.get(j).split("&");
                            if(check[0].equals(reqUser) && check[1].equals(displayList.get(i))){
                                System.out.println("윽액악악악: " + list2.get(j));
                                editor.putString("roomName", list2.get(j));
                                editor.commit();
                            }
                            else if(check[1].equals(reqUser) && check[0].equals(displayList.get(i))){
                                System.out.println("윽액악악악: " + list2.get(j));
                                editor.putString("roomName", list2.get(j));
                                editor.commit();
                            }
                        }

                        String sendName = sharedPreferences.getString("roomName", "user");
                        System.out.println("넌무엇이냐?: "+ sendName);

//                        Intent intent = new Intent(getActivity(), ChatActivity.class);
//                        startActivity(intent);
                        Toast.makeText(getContext(), displayList.get(i), Toast.LENGTH_SHORT).show();
                    }
                });

                System.out.println("-----------------------------------------------------------------------------------------------");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        System.out.println("--------------------------------채팅방리스트(out of void method)----------------------------------");
        for(int i = 0; i < list2.size(); i ++) {
            System.out.println(list2.get(i));
        }
        System.out.println("-----------------------------------------------------------------------------------------------");

        return root;
    }


}
