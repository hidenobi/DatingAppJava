package com.hidenobi.datingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private Button mRegister,mNewLogin;
    private EditText mEmail,mPassword,m_Name;

    private RadioGroup mRadioGroup;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth=FirebaseAuth.getInstance();
        firebaseAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent = new Intent(RegistrationActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };
        mNewLogin=(Button)findViewById(R.id.newLogin);
        mRegister=(Button) findViewById(R.id.Register);
        mEmail=(EditText) findViewById(R.id.email);
        mPassword=(EditText) findViewById(R.id.password);
        m_Name=(EditText) findViewById(R.id.name);
        mRadioGroup = (RadioGroup)  findViewById(R.id.radioGroup);
        mRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                int selectId =mRadioGroup.getCheckedRadioButtonId();
                final RadioButton mRadioButton = (RadioButton) findViewById(selectId);
                if(selectId<0){
                    Toast.makeText(RegistrationActivity.this,"Vui lòng lựa chọn giới tính",Toast.LENGTH_SHORT).show();
                    return;
                }
                final String name= m_Name.getText().toString();
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String sex = mRadioButton.getText().toString();
                final String mSex;
                if(sex=="Nam") mSex = "Male";
                else mSex = "Female";

                if(name.isEmpty()||email.isEmpty()||password.isEmpty()){
                    Toast.makeText(RegistrationActivity.this,"Vui lòng điền đầy đủ thông tin",Toast.LENGTH_SHORT).show();
                    return;
                }

                
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(RegistrationActivity.this,"sign up error",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(RegistrationActivity.this,"sign up complete",Toast.LENGTH_SHORT).show();
                            String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance("https://datingapp-babdb-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Users").child(userId);

                            Map userInfo = new HashMap<>();
                            userInfo.put("name",name);
                            userInfo.put("sex",mSex);
                            userInfo.put("profileImageUrl","default");


                            currentUserDb.updateChildren(userInfo);
                        }
                    }
                });
            }
        });
        mNewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(intent);
                return;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}