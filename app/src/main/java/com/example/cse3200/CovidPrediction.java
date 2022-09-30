package com.example.cse3200;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CovidPrediction extends AppCompatActivity {
    EditText fever, bodypain, age, runnynose, diffbreath;
    Button predict;
    TextView result;
    ProgressBar progressBar;
    DatabaseReference userReference, suspectedUserReference;
    String url = "https://covid-suspect-app.herokuapp.com/predict";
    String email;
    String NAME, AGE, COUNTRY, EMAIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_prediction);
        this.setTitle("Covid Prediction");

        //set actionbar color
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#042C38"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        userReference = FirebaseDatabase.getInstance().getReference("User Info");
        suspectedUserReference = FirebaseDatabase.getInstance().getReference("Covid Suspected User");

        SharedPreferences sp = getSharedPreferences("LoginInfo", MODE_PRIVATE);
        email = sp.getString("email", "Email is not passed.");
        Toast.makeText(CovidPrediction.this, "CovidPrediction email = "+ email , Toast.LENGTH_SHORT).show();

        getInfo();

//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            email = bundle.getString("email");
//            Toast.makeText(CovidPrediction.this, email, Toast.LENGTH_LONG).show();
//            getInfo();
//        }
//        else
//        {
//            Toast.makeText(CovidPrediction.this, "Email is not passed", Toast.LENGTH_LONG).show();
//        }

        fever = findViewById(R.id.fever);
        bodypain = findViewById(R.id.bodypain);
        age = findViewById(R.id.age);
        runnynose = findViewById(R.id.runnynose);
        diffbreath = findViewById(R.id.diffbreath);
        predict = findViewById(R.id.predict);
        result = findViewById(R.id.result);
        progressBar = findViewById(R.id.progressbar);

        progressBar.setVisibility(View.GONE);

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(view.VISIBLE);

                // hit the API -> Volley
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String data = jsonObject.getString("Suspected");
                            if (data.equals("1")) {
                                result.setText("Covid Positive");
                                progressBar.setVisibility(view.GONE);
                                saveData();
//                                Bundle bundle = new Bundle();
//                                bundle.putString("email", email);
//                                Intent intent = new Intent(CovidPrediction.this, GetMapLocationActivity.class);
//                                intent.putExtras(bundle);
//                                startActivity(intent);
                            } else {
                                result.setText("Covid Negative");
                                progressBar.setVisibility(view.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(CovidPrediction.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("fever", fever.getText().toString());
                        params.put("bodypain", bodypain.getText().toString());
                        params.put("age", age.getText().toString());
                        params.put("runnynose", runnynose.getText().toString());
                        params.put("diffbreath", diffbreath.getText().toString());

                        fever.setText("");
                        bodypain.setText("");
                        age.setText("");
                        runnynose.setText("");
                        diffbreath.setText("");

                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(CovidPrediction.this);
                queue.add(stringRequest);
            }
        });
    }

    public void getInfo() {
//        Toast.makeText(CovidPrediction.this, email, Toast.LENGTH_LONG).show();

        Query query = FirebaseDatabase.getInstance().getReference("User Info")
                .orderByChild("email")
                .equalTo(email);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    PersonInfo person = ds.getValue(PersonInfo.class);

                    NAME = person.getName();
                    AGE = person.getAge();
                    COUNTRY = person.getCountry();
                    EMAIL = person.getEmail();
                    Toast.makeText(CovidPrediction.this, NAME + " " + AGE + " " + COUNTRY, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Toast.makeText(CovidPrediction.this, NAME+" "+AGE+" "+COUNTRY, Toast.LENGTH_LONG).show();
    }


    public void saveData() {
        String lat = "Not Set";
        String lng = "Not Set";
        String covid_id = suspectedUserReference.push().getKey();

        CovidPersonInfo covid_person_info = new CovidPersonInfo(NAME, AGE, EMAIL, COUNTRY, covid_id, lat, lng);
        suspectedUserReference.child(covid_id).setValue(covid_person_info);
        Toast.makeText(CovidPrediction.this, "Covid Person Info is added", Toast.LENGTH_LONG).show();
    }
}
