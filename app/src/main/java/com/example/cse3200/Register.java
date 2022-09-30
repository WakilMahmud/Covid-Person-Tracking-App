package com.example.cse3200;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    EditText Name,Email,Password,ConfPass,Country,Age;
    Button registerUserBtn;

    FirebaseAuth fAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.setTitle("Register");

        //set actionbar color
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#042C38"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        Name = findViewById(R.id.registerName);
        Age = findViewById(R.id.registerAge);
        Country = findViewById(R.id.country);
        Email = findViewById(R.id.registerEmail);
        Password = findViewById(R.id.registerPassword);
        ConfPass = findViewById(R.id.confPassword);
        registerUserBtn = findViewById(R.id.registerBtn);


        fAuth = FirebaseAuth.getInstance();


        registerUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //extract the data from the form

                String fullName = Name.getText().toString();
                String email = Email.getText().toString();
                String password = Password.getText().toString();
                String confpass = ConfPass.getText().toString();

                if (fullName.isEmpty()){
                    Name.setError("Full Name is Required");
                    return;
                }

                if (email.isEmpty()){
                    Email.setError("Email is Required");
                    return;
                }

                if (password.isEmpty()){
                    Password.setError("Password is Required");
                    return;
                }

                if (confpass.isEmpty()){
                    ConfPass.setError("Confirmation password is Required");
                    return;
                }

                if(!password.equals(confpass)){
                    ConfPass.setError("Password Do not Match.");
                    return;
                }


                //data is validated
                //register the user using firebase


                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            //authentication successful
                            fAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText( getApplicationContext(), "Registered successfully. Please check your email for verification.",Toast.LENGTH_SHORT).show();
                                        getData();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    public void getData()
    {
        String name = Name.getText().toString().trim();
        String age = Age.getText().toString().trim();
        String country = Country.getText().toString().trim();
        String email = Email.getText().toString().trim();

//        Toast.makeText( getApplicationContext(), name+" "+age ,Toast.LENGTH_SHORT).show();
        Bundle bundle = new Bundle();

        bundle.putString("name", name);
        bundle.putString("age", age);
        bundle.putString("country",country);
        bundle.putString("email",email);

        Intent intent = new Intent(Register.this,Login.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}