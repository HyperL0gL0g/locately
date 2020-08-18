package com.example.fire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class profile extends AppCompatActivity {
    private TextView name,address,phone;

    static final String tag="profile";
    private ProgressBar prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name=(TextView)findViewById(R.id.name);
        address= (TextView)findViewById(R.id.address);
        Button gobacktomap = (Button) findViewById(R.id.backtomap);
        phone= (TextView) findViewById(R.id.phone);
        prog=(ProgressBar) findViewById(R.id.prog);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

      final String user_prof= (getIntent().getStringExtra("name of user"));
      if(user_prof==null)
          Log.d(tag,"null");
      else
        Log.d(tag,user_prof);
      prog.setVisibility(View.VISIBLE);
        db.collection("user-profiles").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                data p = d.toObject(data.class);

                                if((p != null) && p.getName().equals(user_prof)) {
                                   // String addr = p.getAddress();
                                  //  double latitude = p.getLat();
                                   // double longitude = p.getLng();
                                    String prof_phone = p.getPhone();
                                    String prof_name = p.getName();
                                    String user_address=p.getAddress();
                                    //sendtomap=name;
                                    Log.d("profile",user_address);
                                    Log.d("profile",prof_name);
                                    Log.d("profile",prof_phone);

                                    name.setText("name= " + prof_name);
                                    address.setText("address= " + user_address);
                                    phone.setText("number= " + prof_phone);
                                   prog.setVisibility(View.GONE);
                                   // show_lat.setText("lat=" + latitude);
                                    //show_lng.setText(("lng=" + longitude));
                                    //showprog.setVisibility(View.GONE);
                                    Toast.makeText(profile.this, "fetched user data ", Toast.LENGTH_SHORT).show();
                                    break;
                                }

                            }
                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profile.this,"error occured",Toast.LENGTH_SHORT).show();

            }
        });

      gobacktomap.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              startActivity(new Intent(profile.this,mapactivity.class));

          }
      });
    }





    }

