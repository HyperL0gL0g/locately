package com.example.fire;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class mapactivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double lat;
    private Button refresh;
    double lng;
    private Marker myMarker;
    FirebaseFirestore db;
    private String sendtoprof;
    static final String tag = "map";
    String uid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapactivity);
       // refresh = (Button) findViewById(R.id.refresh);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
    }
    private void setUpMapIfNeeded() {

        if (mMap == null) {

            Log.d("MyMap", "setUpMapIfNeeded");
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMapAsync(this);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            uid = user.getUid();
        }
    }

    //onready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        db = FirebaseFirestore.getInstance();

        final String user = getIntent().getStringExtra("user-name");
        //added refresh button

        refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  finish();
        onResume();
              //  startActivity(getIntent());
            }
        });

        Log.d(tag, user);
        db.collection("user-profiles").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                data p = d.toObject(data.class);
                                String curr = p.getName();
                                Log.d("curr", p.getName());
                                if (p != null) {
                                    Log.d(tag, "true");
                                    lat = Double.parseDouble(p.getLat());
                                    lng = Double.parseDouble(p.getLng());
                                    sendtoprof = curr;
                                    Log.d(tag, sendtoprof);
                                    Log.d("newlat", String.valueOf(lat));
                                    Log.d("newlng", String.valueOf(lng));
                                    LatLng user_location;
                                    if (curr.equals(user)) {
                                        user_location = new LatLng(lat, lng);
                                        myMarker = mMap.addMarker(new MarkerOptions()
                                                .position(user_location)
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                                    } else

                                    {

                                        user_location = new LatLng(lat, lng);
                                        myMarker = mMap.addMarker(new MarkerOptions()
                                                .position(user_location)
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));


                                    }
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(user_location));


                                }



                            }

                        }
                    }


                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mapactivity.this, "error occured", Toast.LENGTH_LONG).show();

            }
        });
    }

//onresume
        @Override
        public void onResume() {
            super.onResume();
            Log.i(tag, "resume");
            db = FirebaseFirestore.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            uid = user.getUid();
            DocumentReference doc = db.collection("user-profiles").document(uid);
            doc.update("online","1");


            db.collection("user-profiles").get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot d : list) {
                                    data p = d.toObject(data.class);
                                    if (p != null) {

                                        lat = Double.parseDouble(p.getLat());
                                        lng = Double.parseDouble(p.getLng());
                                        if (p.getOnline().equals("1"))
                                            {
                                                LatLng user_location=   new LatLng(lat, lng);
                                                myMarker = mMap.addMarker(new MarkerOptions()
                                                        .position(user_location)
                                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                            }
                                        else if(p.getOnline().equals("0"))

                                        {
                                            LatLng user_location=   new LatLng(lat, lng);
                                            myMarker = mMap.addMarker(new MarkerOptions()
                                                    .position(user_location)
                                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                                        }
                                    }


                                }
                                //online.setText("online=" + count + "");

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mapactivity.this, "error occured", Toast.LENGTH_LONG).show();

                }

                ;
            });
        }


        //onpause
        @Override
        public void onPause() {
            super.onPause();
            Log.i(tag, "onpause");
            db = FirebaseFirestore.getInstance();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            uid = user.getUid();
            DocumentReference doc = db.collection("user-profiles").document(uid);
            doc.update("online","0");
        }
        //onstop
        @Override
        public void onStop() {
            super.onStop();
            db = FirebaseFirestore.getInstance();
            Log.i(tag, "onstop");

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            uid = user.getUid();
            DocumentReference doc = db.collection("user-profiles").document(uid);
            doc.update("online","0");
        }

        //ondestroy
        @Override
        public void onDestroy() {
            super.onDestroy();
            db = FirebaseFirestore.getInstance();
            setUpMapIfNeeded();
            Log.i(tag, "ondestroy");
            DocumentReference doc = db.collection("user-profiles").document(uid);
            doc.update("online","0");

        }
    }

