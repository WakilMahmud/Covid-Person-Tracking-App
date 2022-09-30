package com.example.cse3200;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
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

public class FetchCovidUser extends AppCompatActivity {
    private ListView listview;
    private List<CovidPersonInfo> personList;
    private LatLngAdapter latLngAdapter;
    DatabaseReference suspectedUserReference;
    String email;
    String lat, lng, name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_covid_user);
        this.setTitle("Covid user");

        //set actionbar color
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#042C38"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        personList = new ArrayList<>();
        latLngAdapter = new LatLngAdapter(FetchCovidUser.this, personList);

        listview = findViewById(R.id.listViewId);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            email = bundle.getString("EMAIL");
//            Toast.makeText(FetchCovidUser.this, "Fetch Covid User Activity" , Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onStart() {
        suspectedUserReference = FirebaseDatabase.getInstance().getReference("Covid Suspected User");

        Query covidQuery = FirebaseDatabase.getInstance().getReference("Covid Suspected User")
                .orderByChild("email")
                .equalTo(email);

        covidQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                personList.clear();

                long count = snapshot.getChildrenCount();
//                Toast.makeText(getApplicationContext(), "# Covid Person = "+count, Toast.LENGTH_LONG).show();
                if (count == 0)
                    Toast.makeText(getApplicationContext(), "Person Not Found", Toast.LENGTH_LONG).show();
                else {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        CovidPersonInfo person = ds.getValue(CovidPersonInfo.class);
                        personList.add(person);

                        name = person.getName();
                        lat = person.getLat();
                        lng = person.getLng();
                        Toast.makeText(getApplicationContext(), lat + " " + lng, Toast.LENGTH_LONG).show();
                    }
                }

                listview.setAdapter(latLngAdapter);


                Bundle bundle = new Bundle();
                bundle.putString("NAME", name);
                bundle.putString("LATITUDE", lat);
                bundle.putString("LONGITUDE", lng);


                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        Intent intent = new Intent(FetchCovidUser.this, ShowOnMap.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                }, 3000);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        super.onStart();
    }
}
