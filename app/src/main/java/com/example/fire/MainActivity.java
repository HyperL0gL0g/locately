package com.example.fire;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private EditText name,address,phone;
    private FirebaseFirestore db;
    private ProgressBar progbar;
    static final String tag="main";
    double lat =0.0;
    private double lng = 0.0;
    //adding
    private FusedLocationProviderClient fusedLocationClient2;
    Location curr_loc2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fusedLocationClient2 = LocationServices.getFusedLocationProviderClient(this);

        progbar = (ProgressBar)findViewById(R.id.progbar);
        name=(EditText)findViewById(R.id.name);
        address=(EditText)findViewById(R.id.address);
        phone=(EditText)findViewById(R.id.phone);
        Button add = (Button) findViewById(R.id.ADD);


        getLocation();

        db= FirebaseFirestore.getInstance();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_name=name.getText().toString().trim();
                String user_mobile=phone.getText().toString().trim();
                String user_addr=address.getText().toString().trim();
                double latitude=lat;
                double longitude=lng;



                progbar.setVisibility(View.VISIBLE);
                CollectionReference profile =db.collection("user-profiles");
                data obj =  new data(user_name,user_addr,user_mobile,latitude,longitude);

                profile.add(obj)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_SHORT).show();
                              //  Log.d("err","reached");
                                        Log.d(tag,user_name);
                                Intent i = new Intent(MainActivity.this,show.class);
                                i.putExtra("name",user_name);
                                startActivity(i);



                                progbar.setVisibility(View.GONE);

                               // startActivity(new Intent(getApplicationContext(),show.class));


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        progbar.setVisibility(View.VISIBLE);

                    }
                });

            }
        });
//adding




    }

    public void getLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient2.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    lat =Double.parseDouble(String.valueOf(location.getLatitude()));
                    lng =Double.parseDouble(String.valueOf(location.getLongitude ()));
                   // Log.d("test123",location.getLatitude()+"\t"+location.getLongitude());
                    curr_loc2 = location;

                }
            }
        });

    }





}

