package com.example.cse3200;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TrackPerson extends AppCompatActivity {
    private EditText emailEditTextId;
    private Button locationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_person);
        this.setTitle("Track Person Activity");

        //set actionbar color
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#042C38"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        emailEditTextId = findViewById(R.id.emailEditTextId);
        locationBtn = findViewById(R.id.locationButton);


        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditTextId.getText().toString().trim();

                Toast.makeText(TrackPerson.this, "Hello "+email , Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("EMAIL", email);

                Intent intent = new Intent(TrackPerson.this , FetchCovidUser.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}