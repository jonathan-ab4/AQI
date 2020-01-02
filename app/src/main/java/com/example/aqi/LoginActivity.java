package com.example.aqi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText emailID, password;
    Button btnSignIn;
    TextView tvSignUp;
    FirebaseAuth mFireBaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFireBaseAuth = FirebaseAuth.getInstance();
        emailID = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        btnSignIn = findViewById(R.id.button);
        tvSignUp = findViewById(R.id.textView);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFireBaseUser = mFireBaseAuth.getCurrentUser();
                if(mFireBaseUser != null){
                    Toast.makeText(LoginActivity.this,"You are Logged In",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(LoginActivity.this,"Please Log In",Toast.LENGTH_SHORT).show();
                }
            }
        };

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailID.getText().toString();
                String pwd = password.getText().toString();
                if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Fields are empty!!",Toast.LENGTH_SHORT).show();
                }
                else if(email.isEmpty()){
                    emailID.setError("Please enter email id");
                    emailID.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("Please enter password");
                    password.requestFocus();
                }
                else if(email.isEmpty()==false && pwd.isEmpty()==false){
                    mFireBaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Login Error, please try again",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "Successfully Logged In!",Toast.LENGTH_SHORT).show();
                                Intent intToHome = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intToHome);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(LoginActivity.this, "Error Occurred!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToMain = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intToMain);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFireBaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
