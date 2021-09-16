package com.example.parking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintsChangedListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.android.AndroidEventTarget;

import java.util.ArrayList;
import java.util.List;

import javax.sql.ConnectionPoolDataSource;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChatData> chatList;
//    private String nick = "nick2"; //1:1채팅 or 1:다 채팅 ----> nick2 이걸 username으로 쓰고싶으면 인자를 다시 받아와야되는부분?!
    private String rpUser, parkingAdmin;
    private String chatRoomName;
    private EditText EditText_chat;
    private Button Button_send;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Button_send = findViewById(R.id.Button_send);
        EditText_chat = findViewById(R.id.EditText_chat);

        SharedPreferences sharedPreferences = getSharedPreferences("sFile",MODE_PRIVATE);
        rpUser = sharedPreferences.getString("loginId", "user");//로그인할때 받아온 접속한 유저의 이름 셋
        parkingAdmin = sharedPreferences.getString("parkingAdmin", "user");
        chatRoomName = rpUser + "&" +parkingAdmin;
        System.out.println("------------------------------------------------------------------");
        System.out.println(rpUser);
        System.out.println(parkingAdmin);
        System.out.println(chatRoomName);
        System.out.println("------------------------------------------------------------------");
        Button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = EditText_chat.getText().toString(); //msg

                if(msg != null) {
                    ChatData chat = new ChatData();
                    chat.setNickname(rpUser);
                    chat.setMsg(msg);
                    myRef.push().setValue(chat);
                    EditText_chat.setText("");

                }
            }
        });

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

        chatList = new ArrayList<>();
        mAdapter = new ChatAdapter(chatList, ChatActivity.this, rpUser);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance(); // 데이터베이스 생성 및 선언
        myRef = database.getReference(chatRoomName); // message받아오기
        myRef.child("roomName");
        
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("chat", dataSnapshot.getValue().toString());
                ChatData chat = dataSnapshot.getValue(ChatData.class);
                ((ChatAdapter) mAdapter).addChat(chat);
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



        //1. recyclerView - 반복
        //2. database 내용 input
        //3. 상대방 기기에 채팅 내용이 보임

        //1-1. recyclerview - chat data
        //1. message, nickname, isMine - Data Transfer Object

    }
}