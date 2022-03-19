package com.hidenobi.datingapp.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hidenobi.datingapp.Matches.MatchesActivity;
import com.hidenobi.datingapp.Matches.MatchesAdapter;
import com.hidenobi.datingapp.Matches.MatchesObject;
import com.hidenobi.datingapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mlayoutManager;
    private EditText mSendEditText;
    private Button mSendButton;
    private String currentUserId,matchID,chatID;
    DatabaseReference mDatabaseUser,mDatabaseChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        matchID = getIntent().getExtras().getString("matchId");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabaseUser = FirebaseDatabase.getInstance("https://datingapp-babdb-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Users").child(currentUserId).child("connections").child("matches").child(matchID).child("ChatId");
        mDatabaseChat = FirebaseDatabase.getInstance("https://datingapp-babdb-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Chat");

        getChatId();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
        mlayoutManager = new LinearLayoutManager(ChatActivity.this);
        mRecyclerView.setLayoutManager(mlayoutManager);
        mChatAdapter = new ChatAdapter(getDataSetChat(),ChatActivity.this);
        mRecyclerView.setAdapter(mChatAdapter);
        mSendEditText = findViewById(R.id.message);
        mSendButton = findViewById(R.id.send);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

    }

    private void sendMessage() {

        String sendMassageText = mSendEditText.getText().toString();
        if(!sendMassageText.isEmpty()){
            DatabaseReference newMassageDb = mDatabaseChat.push();
            Map newMassage = new HashMap();
            newMassage.put("createdByUser", currentUserId);
            newMassage.put("text",sendMassageText);
            newMassageDb.setValue(newMassage);
        }
        mSendEditText.setText(null);
    }
    private void getChatId(){
        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    chatID=snapshot.getValue().toString();
                    mDatabaseChat = mDatabaseChat.child(chatID);
                    getChatMessage();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getChatMessage() {
        mDatabaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    String message = null;
                    String createdByUser = null;
                    if(snapshot.child("text").getValue()!=null){
                        message=snapshot.child("text").getValue().toString();
                        System.out.println(message);
                    }
                    if(snapshot.child("createdByUser").getValue()!=null){
                        createdByUser = snapshot.child("createdByUser").getValue().toString();
                        System.out.println(createdByUser);
                    }
                    if(message!=null&&createdByUser!=null){
                        Boolean currentUserBoolean=false;
                        if(createdByUser.equals(currentUserId)){
                            currentUserBoolean=true;
                        }

                        ChatObject newMessage = new ChatObject(message,currentUserBoolean);
                        resultsChat.add(newMessage);
                        mChatAdapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private ArrayList<ChatObject> resultsChat = new ArrayList<ChatObject>();
    private List<ChatObject> getDataSetChat() {
        return resultsChat;
    }
}