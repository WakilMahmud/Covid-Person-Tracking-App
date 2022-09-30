package com.example.cse3200;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {
    Button createAccountBtn, loginBtn, forget_password_btn;
    EditText userEmail, password;
    String name, age, country, email;
    FirebaseAuth fAuth;
    DatabaseReference databaseReference;

    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setTitle("Login");

        //set actionbar color
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#042C38"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        fAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User Info");
        reset_alert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            name = bundle.getString("name");
            age = bundle.getString("age");
            country = bundle.getString("country");
            email = bundle.getString("email");
        }


        createAccountBtn = findViewById(R.id.createAccountBtn);
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        userEmail = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginBtn);
        forget_password_btn = findViewById(R.id.forget_password_btn);
        forget_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start alertdialog
                View view = inflater.inflate(R.layout.reset_pop, null);


                reset_alert.setTitle("Reset Forgot Password ?")
                        .setMessage("Enter Your Email to get Password Reset link.")
                        .setPositiveButton("Reset Link", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // validate the email address
                                EditText email = view.findViewById(R.id.reset_email_pop);
                                if (email.getText().toString().isEmpty()) {
                                    email.setError("Required Field");
                                    return;
                                }
                                //send the reset link
                                fAuth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Login.this, "Reset_Email_Sent", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }).setNegativeButton("Cancel", null)
                        .setView(view)
                        .create().show();


            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // extract / validate

                if (userEmail.getText().toString().isEmpty()) {
                    userEmail.setError("Email is Missing.");
                    return;
                }

                if (password.getText().toString().isEmpty()) {
                    password.setError("Password is Missing.");
                    return;
                }
                // data is valid
                // login user



                fAuth.signInWithEmailAndPassword(userEmail.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // user authentication is successful
                        if (!fAuth.getCurrentUser().isEmailVerified())
                        {
                            // Not verified
                            Toast.makeText( getApplicationContext(), "You are not Verified User.",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            //verified
                            saveData();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(Login.this,"Please Create an account.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void saveData()
    {
        String id = databaseReference.push().getKey();
        String loginEmail = userEmail.getText().toString();

        if(name!=null && age!=null && country!=null && email!=null){
            PersonInfo person_info = new PersonInfo(name,age,country,email,id);
            databaseReference.child(id).setValue(person_info);
            Toast.makeText(Login.this,"Person info is added",Toast.LENGTH_LONG).show();
        }

        SharedPreferences sp = getSharedPreferences("LoginInfo", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("email", loginEmail);
        ed.apply();


        //Bundle bundle = new Bundle();
        //bundle.putString("email", loginEmail);
        Intent intent = new Intent(Login.this, MainActivity.class);
        //intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null && fAuth.getCurrentUser().isEmailVerified()) {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }
    }
}