package com.example.cse3200;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class QueryActivity extends AppCompatActivity {
    private EditText countryEditText;
    private Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        this.setTitle("Query Data");

        //set actionbar color
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#042C38"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        countryEditText = findViewById(R.id.countryEditTextId);
        searchBtn = findViewById(R.id.searchButton);


        Bundle bundle = new Bundle();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String country = countryEditText.getText().toString().trim();

                bundle.putString("Country", country);

                Intent intent = new Intent(QueryActivity.this , Fetched_Data_Details.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
