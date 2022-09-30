package com.example.cse3200;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button covidTest,trackPerson,loadData,queryData, logout,CurrentLocationBtn;
    FirebaseAuth auth;
    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Covid Person Tracking");

        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#042C38"));
        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);

        auth = FirebaseAuth.getInstance();

        CurrentLocationBtn = findViewById(R.id.CurrentLocation);
        covidTest = findViewById(R.id.covidTest);
        trackPerson = findViewById(R.id.trackPerson);
        loadData = findViewById(R.id.loadData);
        queryData = findViewById(R.id.queryData);

        logout = findViewById(R.id.logoutBtn);

        reset_alert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();

        SharedPreferences sp = getSharedPreferences("LoginInfo", MODE_PRIVATE);
        email = sp.getString("email", "Email is not passed.");
        Toast.makeText(MainActivity.this, "Main Activity = "+ email , Toast.LENGTH_SHORT).show();

//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            email = bundle.getString("email");
//            Toast.makeText(MainActivity.this, "main activity "+ email, Toast.LENGTH_SHORT).show();
//        }

        CurrentLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.putString("email", email);
//                Toast.makeText(MainActivity.this, "Email is "+email, Toast.LENGTH_LONG).show();
//                Toast.makeText(MainActivity.this, "Current Location Button is clicked", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, GetMapLocationActivity.class);
//                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        covidTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "Covid Test button is clicked", Toast.LENGTH_SHORT).show();
//                Bundle bundle = new Bundle();
//                bundle.putString("email", email);
                Intent intent = new Intent(MainActivity.this, CovidPrediction.class);
//                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        trackPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),TrackPerson.class));
//                Toast.makeText(MainActivity.this, "Track Person button is clicked", Toast.LENGTH_SHORT).show();
            }
        });

        loadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),DetailsActivity.class));
            }
        });

        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),QueryActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.resetUserPassword){
            startActivity(new Intent(getApplicationContext(),ResetPassword.class));
        }

        if (item.getItemId() == R.id.updateEmailMenu){
            View view = inflater.inflate(R.layout.reset_pop,null);
            reset_alert.setTitle("Update Email ?")
                    .setMessage("Enter New Email Address.")
                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // validate the email address
                            EditText email = view.findViewById(R.id.reset_email_pop);
                            if (email.getText().toString().isEmpty()){
                                email.setError("Required Field");
                                return;
                            }
                            //send the reset link
                            FirebaseUser user = auth.getCurrentUser();
                            user.updateEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MainActivity.this, "Email Updated.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).setNegativeButton("Cancel",null)
                    .setView(view)
                    .create().show();
        }

        if (item.getItemId() == R.id.delete_account_menu){
            reset_alert.setTitle("Delete Account Permanently ?")
                    .setMessage("Are You Sure ?")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseUser user = auth.getCurrentUser();
                            user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MainActivity.this, "Account Deleted.", Toast.LENGTH_SHORT).show();
                                    auth.signOut();
                                    startActivity(new Intent(getApplicationContext(),Login.class));
//                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).setNegativeButton("Cancel",null)
                    .create().show();
        }

        return super.onOptionsItemSelected(item);
    }
}