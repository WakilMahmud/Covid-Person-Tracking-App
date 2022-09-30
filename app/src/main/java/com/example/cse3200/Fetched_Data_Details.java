package com.example.cse3200;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Fetched_Data_Details extends AppCompatActivity {

    private ListView listview;
    private List<PersonInfo> personList;
    private CustomAdapter customAdapter;
    DatabaseReference databaseReference;
    String country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetched_data_details);
        this.setTitle("Fetch Query Data");

        //set actionbar color
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#042C38"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        personList = new ArrayList<>();
        customAdapter = new CustomAdapter(Fetched_Data_Details.this, personList);

        listview =findViewById(R.id.listViewId);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            country = bundle.getString("Country");
        }
    }

    @Override
    protected void onStart() {
        databaseReference = FirebaseDatabase.getInstance().getReference("User Info");

        Query query = FirebaseDatabase.getInstance().getReference("User Info")
                .orderByChild("country")
                .equalTo(country);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                personList.clear();

                long  count =  snapshot.getChildrenCount();
                Toast.makeText(getApplicationContext(), "# User = "+count, Toast.LENGTH_LONG).show();
                if(count==0)
                    Toast.makeText(getApplicationContext(), "Person Not Found" ,Toast.LENGTH_LONG).show();
                else
                {
                    for(DataSnapshot ds : snapshot.getChildren())
                    {
                        PersonInfo person = ds.getValue(PersonInfo.class);
                        personList.add(person);

//                        int lat = Integer.parseInt(person.getLatitude());
//                        int lng = Integer.parseInt(person.getLongitude());
                    }
                }


                listview.setAdapter(customAdapter);

//                Bundle bundle = new Bundle();
//                bundle.putString("LATITUDE", lat);
//                bundle.putString("LONGITUDE", lng);
//
//                Intent intent = new Intent(Fetched_Data_Details.this , GetMapLocationActivity.class);
//                intent.putExtras(bundle);
//                startActivity(intent);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        super.onStart();
    }
}