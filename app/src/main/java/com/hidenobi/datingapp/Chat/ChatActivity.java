package com.hidenobi.datingapp.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ScrollingView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import com.hidenobi.datingapp.ViewProfileActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mlayoutManager;
    private EditText mSendEditText;
    private ImageView mSendButton;
    private String currentUserId,matchID,chatID;
    private ImageView imageView;
    private TextView textView;
    DatabaseReference mDatabaseUser,mDatabaseChat,mDatabaseMatches;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        matchID = getIntent().getExtras().getString("matchId");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabaseUser = FirebaseDatabase.getInstance("https://datingapp-babdb-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Users").child(currentUserId).child("connections").child("matches").child(matchID).child("ChatId");
        mDatabaseChat = FirebaseDatabase.getInstance("https://datingapp-babdb-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Chat");
        mDatabaseMatches = FirebaseDatabase.getInstance("https://datingapp-babdb-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Users").child(matchID);
        imageView = (ImageView) findViewById(R.id.currentMatchesImage);
        textView = (TextView) findViewById(R.id.currentMatchesName);
        getChatId();
        getInfoId();
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
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("111111111111111111111");
                Intent intent = new Intent(view.getContext(), ViewProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("matchId",matchID);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
                finish();
            }
        });

    }

    private void getInfoId() {
        mDatabaseMatches.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Map<String,Object> map = (Map<String, Object>)  snapshot.getValue();
                    if(map.get("profileImageUrl")!=null){
                        String profileImageUrl = map.get("profileImageUrl").toString();
                        switch (profileImageUrl){
                            case "default":
                                Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(imageView);
                                break;

                            default:
                                Glide.with(getApplication()).load(profileImageUrl).into(imageView);
                                break;
                        }

                    }
                    if(map.get("name")!=null){
                        textView.setText(map.get("name").toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage() {

        String sendMassageText = mSendEditText.getText().toString();
        sendMassageText = sendMassageText.trim();
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