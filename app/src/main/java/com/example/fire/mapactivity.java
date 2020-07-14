package com.example.fire;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.DataOutputStream;
import java.util.List;

public class mapactivity extends FragmentActivity implements OnMapReadyCallback , GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    double lat ;
    double lng ;
    private Marker myMarker;
    FirebaseFirestore db;
    private   String sendtoprof;
    static final String tag="map";
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
         final String user= getIntent().getStringExtra("user-name");


        Log.d(tag,user);
        db.collection("user-profiles").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                data p = d.toObject(data.class);
                                String curr=p.getName();
                                Log.d("curr",p.getName());
                                if(p!=null && curr.equals(user)) {
                                    Log.d(tag,"true");
                                    lat = Double.parseDouble(p.getLat());
                                    lng = Double.parseDouble(p.getLng());
                                    sendtoprof=curr;
                                    Log.d(tag,sendtoprof);
                                    Log.d("newlat", String.valueOf(lat));
                                    Log.d("newlng", String.valueOf(lng));
                                    LatLng user_location = new LatLng(lat,lng);
                                    Toast.makeText(mapactivity.this,"lat= "+String.valueOf(lat)+" "+"lng= "+String.valueOf(lng),Toast.LENGTH_SHORT).show();
                                    mMap.setOnMarkerClickListener(mapactivity.this);
                                  myMarker =  mMap.addMarker(new MarkerOptions()
                                          .position(user_location));



                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(user_location));

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
       // LatLng sydney = new LatLng(lat,lng);
        //Toast.makeText(mapactivity.this,"lat= "+String.valueOf(lat)+" "+"lng= "+String.valueOf(lng),Toast.LENGTH_LONG).show();

      //  mMap.addMarker(new MarkerOptions().position(sydney).title("You are Here"));
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.equals(myMarker))
        {
            Log.d(tag,sendtoprof);
        Toast.makeText(mapactivity.this,"clicked",Toast.LENGTH_LONG).show();
        Intent i =  new Intent(mapactivity.this,profile.class);

           i.putExtra("name of user",sendtoprof);
        startActivity(i);
        }
        return false;
    }
}
