package com.example.cse3200;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private ListView listview;
    private List<PersonInfo> personList;
    private List<ADMIN> adminList;
    private CustomAdapter customAdapter;
    DatabaseReference databaseReference;
    public EditText emailEditText;
    public Button showButton;
    String email, DatabaseEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        this.setTitle("Database User");

        //set actionbar color
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#042C38"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        personList = new ArrayList<>();
        adminList = new ArrayList<>();
        customAdapter = new CustomAdapter(DetailsActivity.this, personList);

        listview = findViewById(R.id.listViewId);
        emailEditText = findViewById(R.id.emailEditTextId);
        showButton = findViewById(R.id.showButtonId);

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailEditText.getText().toString();
                CheckAdmin();


                emailEditText.setVisibility(view.GONE);
                showButton.setVisibility(view.GONE);

            }
        });


    }


    public void CheckAdmin() {
        databaseReference = FirebaseDatabase.getInstance().getReference("ADMIN");
//        String id = databaseReference.push().getKey();
//        ADMIN admin_info = new ADMIN(id, "admin@gmail.com");
//        databaseReference.child(id).setValue(admin_info);

        Query query = FirebaseDatabase.getInstance().getReference("ADMIN")
                .orderByChild("email")
                .equalTo("admin@gmail.com");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adminList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ADMIN admin = ds.getValue(ADMIN.class);
                    adminList.add(admin);
                    DatabaseEmail = admin.getEmail().toString();

//                    Toast.makeText(DetailsActivity.this, "Database "+DatabaseEmail, Toast.LENGTH_LONG).show();
//                    Toast.makeText(DetailsActivity.this, "editText"+email, Toast.LENGTH_LONG).show();

                    if (DatabaseEmail.contentEquals(email))    // valid admin
                    {
                        showDB();
                        //Toast.makeText(DetailsActivity.this, "Equal", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(DetailsActivity.this, "You are not ADMIN", Toast.LENGTH_LONG).show();
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showDB() {
        databaseReference = FirebaseDatabase.getInstance().getReference("User Info");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                personList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    PersonInfo person = ds.getValue(PersonInfo.class);
                    personList.add(person);
                }
                listview.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


