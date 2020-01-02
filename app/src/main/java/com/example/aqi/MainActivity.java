package com.example.aqi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText emailID, password;
    Button btnSignUp;
    TextView tvSignIn;
    private FirebaseAuth mFireBaseAuth;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFireBaseAuth = FirebaseAuth.getInstance();
        emailID = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        btnSignUp = findViewById(R.id.button);
        tvSignIn = findViewById(R.id.textView);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailID.getText().toString();
                String pwd = password.getText().toString();
                if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(MainActivity.this, "Fields are empty!!",Toast.LENGTH_SHORT).show();
                }
                else if(email.isEmpty()){
                    emailID.setError("Please enter email id");
                    emailID.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("Please enter password");
                    password.requestFocus();
                }
                else if(!(email.isEmpty() && pwd.isEmpty())){
                    mFireBaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Sign Up Unsuccessful, Please try again",Toast.LENGTH_SHORT).show();
                            }
                            else{

                                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("number");
                                mDatabaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String a = dataSnapshot.getValue().toString();
                                            try {
                                                int num = Integer.parseInt(a);
                                                mDatabaseReference.setValue(++num);
                                                mDatabaseReference = FirebaseDatabase.getInstance().getReference();

                                                for(int i=1;i<13;i++)
                                                {
                                                    mDatabaseReference.child("Daily_Report").child("User_"+num).child("record_"+ i).child("latitude").setValue(0);
                                                    mDatabaseReference.child("Daily_Report").child("User_"+num).child("record_"+ i).child("longitude").setValue(0);
                                                    mDatabaseReference.child("Daily_Report").child("User_"+num).child("record_"+ i).child("aqi").setValue(0);
                                                }

                                                for(int i = 1;i<13;i++)
                                                {
                                                    for(int j=1;j<5;j++)
                                                    {
                                                        mDatabaseReference.child("Monthly_Report").child("User_"+num).child("Month_"+i).child("week_"+j).setValue(0);
                                                    }
                                                }

                                                Intent toHome = new Intent(MainActivity.this, HomeActivity.class);
                                                startActivity(toHome);
                                            }
                                            catch (Exception e)
                                            {
                                                Toast.makeText(MainActivity.this, "Error Occurred!",Toast.LENGTH_SHORT).show();
                                            }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(MainActivity.this, "Error Occurred!",Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                    });
                }
                else{
                    Toast.makeText(MainActivity.this, "Error Occurred!",Toast.LENGTH_SHORT).show();
                }

            }
        });
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
