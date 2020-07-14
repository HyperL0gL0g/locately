package com.example.fire;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_LOCATION_PERMISSION = 1;

    private EditText name,address,phone;
    private FirebaseFirestore db;
    private ProgressBar progbar;
    static final String tag="main";
    double lat =0.0;
    double lng =0.0;

    String login_uid = "";
    Location curr_loc2;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        progbar = (ProgressBar)findViewById(R.id.progbar);
        name=(EditText)findViewById(R.id.name);
        address=(EditText)findViewById(R.id.address);
        phone=(EditText)findViewById(R.id.phone);
        Button add = (Button) findViewById(R.id.ADD);

        login_uid = FirebaseAuth.getInstance().getUid();

        requestLocationPermission();
        getLocation();

        // jobscheduler code
        ComponentName jobServiceComponent = new ComponentName(getApplicationContext(), background_locationScheduler.class);
        JobInfo.Builder jobInfoBuilder = new JobInfo.Builder(0, jobServiceComponent);
        jobInfoBuilder.setPeriodic(900000);
        jobInfoBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        jobInfoBuilder.setRequiresCharging(false);
        jobInfoBuilder.setRequiresDeviceIdle(false);
        jobInfoBuilder.setPersisted(true);
        JobScheduler scheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(jobInfoBuilder.build());



        // adding data to a specific doc id (login id)
        db= FirebaseFirestore.getInstance();
        add.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       final String user_name = name.getText().toString().trim();
                                       String user_mobile = phone.getText().toString().trim();
                                       String user_addr = address.getText().toString().trim();
                                       String latitude = String.valueOf(lat);
                                       String longitude = String.valueOf(lng);

                                       progbar.setVisibility(View.VISIBLE);
                                       data obj = new data(user_name, user_addr, user_mobile, latitude, longitude);
                                       db.collection("user-profiles").document(login_uid).set(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void aVoid) {
                                               Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                               Log.d(tag, user_name);
                                               Intent i = new Intent(MainActivity.this, show.class);
                                               i.putExtra("name", user_name);
                                               startActivity(i);
                                               progbar.setVisibility(View.GONE);
                                           }
                                       }).addOnFailureListener(new OnFailureListener() {
                                           @Override
                                           public void onFailure(@NonNull Exception e) {
                                               Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                               progbar.setVisibility(View.VISIBLE);
                                           }
                                       });
                                   }
            });
        }


        public void getLocation(){
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(EasyPermissions.hasPermissions(this, perms)) {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
        else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }
}
