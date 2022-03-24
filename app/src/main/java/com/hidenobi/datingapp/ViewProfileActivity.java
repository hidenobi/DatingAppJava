package com.hidenobi.datingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ViewProfileActivity extends AppCompatActivity {

    private String matchID;
    private TextView mNameField,mPhoneField,mAgeField,mStatus,mRelationship,mHome,mJobs,mHobby;
    private ImageView mProfileImage;
    private DatabaseReference mUserDatabase;

    private  String name, phone, profileImageUrl,userSex,age,status,relationship,home,job,hobby;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        matchID = getIntent().getExtras().getString("matchId");
        mAgeField = (TextView) findViewById(R.id.age);
        mStatus =(TextView) findViewById(R.id.status);
        mNameField = (TextView) findViewById(R.id.name);
        mPhoneField =(TextView) findViewById(R.id.phone);
        mHobby=(TextView) findViewById(R.id.hobby);
        mRelationship=(TextView) findViewById(R.id.relationship);
        mHome = (TextView) findViewById(R.id.home);
        mJobs = (TextView) findViewById(R.id.jobs);
        mProfileImage =(ImageView) findViewById(R.id.profileImage);
        mUserDatabase = FirebaseDatabase.getInstance("https://datingapp-babdb-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Users").child(matchID);
        getUserInfo();
    }

    private void getUserInfo() {
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()&&snapshot.getChildrenCount()>0){
                    Map<String,Object> map = (Map<String, Object>)  snapshot.getValue();
                    if(map.get("name")!=null){
                        name = map.get("name").toString();
                        mNameField.setText(name);
                    }
                    if(map.get("age")!=null){
                        age=map.get("age").toString();
                        mAgeField.setText(age);
                    }
                    if(map.get("status")!=null){
                        status=map.get("status").toString();
                        mStatus.setText(status);
                    }
                    if(map.get("phone")!=null){
                        phone = map.get("phone").toString();
                        mPhoneField.setText(phone);
                    }
                    if(map.get("home")!=null){
                        home = map.get("home").toString();
                        mHome.setText(home);
                    }
                    if(map.get("jobs")!=null){
                        job = map.get("jobs").toString();
                        mJobs.setText(job);
                    }
                    if(map.get("relationship")!=null){
                        relationship = map.get("relationship").toString();
                        mRelationship.setText(relationship);
                    }
                    if(map.get("hobby")!=null){
                        hobby = map.get("hobby").toString();
                        mHobby.setText(hobby);
                    }
                    if(map.get("sex")!=null){
                        userSex= map.get("sex").toString();
                    }
                    if(map.get("profileImageUrl")!=null){
                        profileImageUrl = map.get("profileImageUrl").toString();
                        switch (profileImageUrl){
                            case "default":
                                Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(mProfileImage);
                                break;

                            default:
                                Glide.with(getApplication()).load(profileImageUrl).into(mProfileImage);
                                break;
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}