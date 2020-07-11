package com.example.fire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class newmapactivity extends FragmentActivity implements OnMapReadyCallback {
    Location curr_loc;
    private ProgressBar showprog;
    final double[] lat ={0,0};
    final double[] lng = {0.0};
   private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmapactivity);
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        showprog=(ProgressBar)findViewById(R.id.showprogress);
      //  mMap=(GoogleMap)findViewById(R.id.map);
       // getLoc();
       // onMapReady(mMap);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        showprog.setVisibility(View.VISIBLE);
        db.collection("user-profiles").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                data p = d.toObject(data.class);
                                if(p!=null) {
                                    lat[0] = p.getLat();
                                    lng[0] = p.getLng();
                                    onMapReady(mMap);
                                    break;
                                }
                                    showprog.setVisibility(View.GONE);
                                    Toast.makeText(newmapactivity.this,"fetched data from firestore",Toast.LENGTH_LONG).show();

                                }

                            }
                        }


                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(newmapactivity.this,"error occured",Toast.LENGTH_LONG).show();

            }
        });


    }







   /* public void getLoc() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    Log.d("test123",location.getLatitude()+"\t"+location.getLongitude());
                    curr_loc = location;
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert mapFragment != null;
                    mapFragment.getMapAsync(newmapactivity.this);
                }
            }
        });

    }*/


    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng sydney = new LatLng(lat[0],lng[0]);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("you are here"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        Log.d("here", "reached here");

    }



}
