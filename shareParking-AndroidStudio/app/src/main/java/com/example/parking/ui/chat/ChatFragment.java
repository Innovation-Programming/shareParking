package com.example.parking.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.example.parking.ChatActivity;
import com.example.parking.ChatData;
import com.example.parking.R;
import com.example.parking.ui.setting.SettingViewModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    private DatabaseReference chattingList;
    private ChatViewModel chatViewModel;
    private List<String> chatList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        chatViewModel =
                ViewModelProviders.of(this).get(ChatViewModel.class);
        View root = inflater.inflate(R.layout.fragment_chat, container, false);

        ListView list_view = root.findViewById(R.id.list2);

        chatList = new ArrayList<>();// 채팅방 목록 표현해주기 위한 리스

        FirebaseDatabase database = FirebaseDatabase.getInstance(); //데이터베이스 생성 및 선
        chattingList = database.getReference("chattingList"); //chattingList 값 가져오기


//        System.out.println(chattingList.getRef());
        chattingList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("ChattingList", "list : " + dataSnapshot.getValue());
                chatList.add(dataSnapshot.getValue().toString());
                System.out.println("--------------------------------채팅방리스트----------------------------------");
                for(int i = 0; i < chatList.size(); i ++) {
                    System.out.println(chatList.get(i));
                }
                System.out.println("--------------------------------채팅방리스트----------------------------------");
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
//        final TextView textView = root.findViewById(R.id.text_chat);
//        Button button = root.findViewById(R.id.chatting_test);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), ChatActivity.class);
//                startActivity(intent);
//            }
//        });
//        chatViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });



        return root;
    }


}
