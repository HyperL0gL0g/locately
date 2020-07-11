package com.example.fire;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

import java.io.DataOutputStream;
import java.util.List;

public class mapactivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double lat =22.618771;
     double lng = 88.4381169;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapactivity);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

//onready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       db =  FirebaseFirestore.getInstance();
         final String user= getIntent().getExtras().getString("user-name");

        Log.d("current user",user);
        db.collection("user-profiles").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                data p = d.toObject(data.class);

                                if(p!=null && p.getName().equals(user)) {
                                    lat = p.getLat();
                                    lng = p.getLng();
                                  //  showprog.setVisibility(View.GONE);
                                    break;
                                }

                               // Toast.makeText(mapactivity.this,"fetched data from firestore",Toast.LENGTH_LONG).show();

                            }

                        }
                    }


                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mapactivity.this,"error occured",Toast.LENGTH_LONG).show();

            }
        });

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat,lng);
        Toast.makeText(mapactivity.this,"lat= "+String.valueOf(lat)+" "+"lng= "+String.valueOf(lng),Toast.LENGTH_LONG).show();
       // Log.d("latitude", String.valueOf(lat));
        //Log.d("longitude", String.valueOf(lng));

       // Toast.makeText(mapactivity.this, String.valueOf(lat),Toast.LENGTH_LONG).show();
       // Toast.makeText(mapactivity.this, (int) lng[0],Toast.LENGTH_LONG).show();
        mMap.addMarker(new MarkerOptions().position(sydney).title("You are Here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
