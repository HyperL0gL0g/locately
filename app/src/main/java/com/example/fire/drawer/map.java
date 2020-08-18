package com.example.fire.drawer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.fire.R;
import com.example.fire.profile;

import com.example.fire.data;

import com.example.fire.views.add_profile;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link map#newInstance} factory method to
 * create an instance of this fragment.
 */
public class map extends Fragment implements OnMapReadyCallback {


    private GoogleMap mMap;
    double lat;
    private Button refresh;
    double lng;
    private Marker myMarker;
    FirebaseFirestore db;
    private String sendtoprof;
    static final String tag = "map";
    String uid="";
    String login_uid;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public map() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment map.
     */
    // TODO: Rename and change types and number of parameters
    public static map newInstance(String param1, String param2) {
        map fragment = new map();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

//        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
//                .findFragmentById(R.id.map123);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map123);
        mapFragment.getMapAsync(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        db = FirebaseFirestore.getInstance();
        login_uid = FirebaseAuth.getInstance().getUid();
        db.collection("user-profiles").document(login_uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    //Log.d("map","exits");
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        Log.d("map","exits");
                    }else{
                        new AlertDialog.Builder(getContext())
                                .setTitle("New User")
                                .setMessage("Please Set up your profile")
                                .setPositiveButton("Take me there", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        getActivity().startActivity(new Intent(getActivity(), add_profile.class));
                                    }
                                })
                                .setCancelable(false)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
            }
        });

    }

    private void setUpMapIfNeeded() {

        if (mMap == null) {

            Log.d("MyMap", "setUpMapIfNeeded");
            ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map123))
                    .getMapAsync(this);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            uid = user.getUid();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        final String user = getActivity().getIntent().getStringExtra("user-name");
        //added refresh button

        refresh = (Button) getView().findViewById(R.id.refresh123);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  finish();
                onResume();
                //  startActivity(getIntent());
            }
        });

//        Log.d(tag, user);
        Log.d("scam",FirebaseAuth.getInstance().getUid());
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
                new AlertDialog.Builder(getContext())
                        .setTitle("Setup Profile")
                        .setMessage("To continue please setup profile")
                        .setPositiveButton("Take me there", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().startActivity(new Intent(getActivity(),profile.class));
                            }
                        })
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

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
                Toast.makeText(getActivity(), "error occured", Toast.LENGTH_LONG).show();

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