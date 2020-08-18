package com.example.fire.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fire.R;
import com.example.fire.dashboard;
import com.example.fire.data;
import com.example.fire.drawer.mainDashboard;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class add_profile extends AppCompatActivity {

    private final int REQUEST_LOCATION_PERMISSION = 1;

    private EditText name, address, phone;
    private FirebaseFirestore db;
    private ProgressBar progbar;
    double lat = 0.0;
    double lng = 0.0;

    static final String tag = "profile_add";
    String login_uid = "";
    Location curr_loc2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile);

        check_locationPerm();
        getLocation();

        progbar = (ProgressBar) findViewById(R.id.profile_progbar);
        name =findViewById(R.id.profile_name);
        address = findViewById(R.id.profile_address);
        phone =  findViewById(R.id.profile_phone);
        Button add =  findViewById(R.id.profile_add);

        db = FirebaseFirestore.getInstance();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String user_name = name.getText().toString().trim();
                String user_mobile = phone.getText().toString().trim();
                String user_addr = address.getText().toString().trim();
                String latitude = String.valueOf(lat);
                String longitude = String.valueOf(lng);
                login_uid= FirebaseAuth.getInstance().getUid();
                progbar.setVisibility(View.VISIBLE);
                String online= "1";
                data obj = new data(user_name, user_addr, user_mobile, latitude, longitude,online);
                db.collection("user-profiles").document(login_uid).set(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        Log.d(tag, user_name);
//                        Intent i = new Intent(getApplicationContext(), show.class);
//                        i.putExtra("name", user_name);
//                        startActivity(i);
                        startActivity(new Intent(getApplicationContext(), mainDashboard.class));
                        progbar.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        progbar.setVisibility(View.VISIBLE);
                    }
                });


            }
        });

    }

    public void check_locationPerm() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(getApplicationContext()).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    lat = Double.parseDouble(String.valueOf(location.getLatitude()));
                    lng = Double.parseDouble(String.valueOf(location.getLongitude()));
                    curr_loc2 = location;
                }
            }
        });
    }


    public void gpsSatusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    gpsSatusCheck();
                    getLocation();
                }

            } else {
                Toast.makeText(this, "Location permssion not granted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), dashboard.class));
                finish();
            }
        }
    }
}