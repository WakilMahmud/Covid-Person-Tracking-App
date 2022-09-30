package com.example.cse3200;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class GetMapLocationActivity extends AppCompatActivity {

    SupportMapFragment smf;
    FusedLocationProviderClient client;
    String email;
    DatabaseReference suspectedUserReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_map_location);
        this.setTitle("Get Map Location");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set actionbar color
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#042C38"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        smf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        client = LocationServices.getFusedLocationProviderClient(this);

        SharedPreferences sp = getSharedPreferences("LoginInfo", MODE_PRIVATE);
        email = sp.getString("email", "Email is not passed.");
        Toast.makeText(GetMapLocationActivity.this, "GetMapLocation Activity = "+ email , Toast.LENGTH_SHORT).show();

//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            email = bundle.getString("email");
//            Toast.makeText(GetMapLocationActivity.this, email, Toast.LENGTH_LONG).show();
//        }

        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getmylocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }


    public void getmylocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                smf.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap)
                    {
                        double lat = location.getLatitude();
                        double lng = location.getLongitude();

                        Toast.makeText(getApplicationContext(), "Lat="+lat+" Lng="+lng ,Toast.LENGTH_LONG).show();
                        setLatLng(lat,lng);


                        LatLng currentLocation=new LatLng(lat,lng);
//                        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
//                        LatLng latLng=new LatLng(24.364098210393536, 88.62994715365436);

                        MarkerOptions markerOptions=new MarkerOptions().position(currentLocation).title("Lat = "+lat+" Lng = "+lng);
                        googleMap.addMarker(markerOptions);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,17));

                    }
                });
            }
        });
    }

    public void setLatLng(double lat, double lng)
    {
        suspectedUserReference = FirebaseDatabase.getInstance().getReference("Covid Suspected User");

        Query query = FirebaseDatabase.getInstance().getReference("Covid Suspected User")
                .orderByChild("email")
                .equalTo(email);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long  count =  snapshot.getChildrenCount();
                if(count==0)
                    Toast.makeText(getApplicationContext(), "Person Not Found" ,Toast.LENGTH_LONG).show();
                else
                {
                    for(DataSnapshot ds : snapshot.getChildren())
                    {
                        CovidPersonInfo person = ds.getValue(CovidPersonInfo.class);

                        //*** Find the Key
                        String Key = ds.getKey();
                        suspectedUserReference.child(Key).child("lat").setValue(String.valueOf(lat));
                        suspectedUserReference.child(Key).child("lng").setValue(String.valueOf(lng));

//                      Toast.makeText(getApplicationContext(), name+age ,Toast.LENGTH_LONG).show();
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}