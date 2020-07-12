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

public class show extends AppCompatActivity {
private FirebaseFirestore db;
private ProgressBar showprog;
private String sendtomap;
    static final String tag="show";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        Log.d("start","dsdasds");
        showprog=(ProgressBar)findViewById(R.id.showprogress);

         final TextView show_name = (TextView) findViewById(R.id.show_name);
        final TextView show_addr = (TextView) findViewById(R.id.show_addr);
         final TextView show_phone = (TextView) findViewById(R.id.show_phone);
        final TextView show_lat = (TextView) findViewById(R.id.latitude);

        final TextView show_lng = (TextView) findViewById(R.id.longitude);
         final Button show,gotomap,gotomap2;
        show = (Button)findViewById(R.id.s);
        gotomap=(Button)findViewById(R.id.gotomap);
        gotomap2=(Button)findViewById(R.id.gotomap2);


        final String user= getIntent().getStringExtra("name");
        if(user==null)
            Log.d(tag,"null");

        Log.d(tag,user);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showprog.setVisibility(View.VISIBLE);
                Log.d(tag,"reached");
db=FirebaseFirestore.getInstance();
                db.collection("user-profiles").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot d : list) {
                                        data p = d.toObject(data.class);

                                        if(p!=null && p.getName().equals(user)) {
                                            String addr = p.getAddress();
                                            double latitude = p.getLat();
                                            double longitude = p.getLng();
                                            String phone = p.getPhone();
                                            String name = p.getName();
                                            sendtomap=name;
                                            show_name.setText("name= " + name);
                                            show_addr.setText("address= " + addr);
                                            show_phone.setText("number= " + phone);
                                            show_lat.setText("lat=" + latitude);
                                            show_lng.setText(("lng=" + longitude));
                                            showprog.setVisibility(View.GONE);
                                            Log.d(tag,"data showed");
                                            Toast.makeText(show.this, "fetched data from firestore", Toast.LENGTH_SHORT).show();
                                            break;
                                        }

                                    }
                                }
                            }

        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(show.this,"error occured",Toast.LENGTH_SHORT).show();

                    }
                });


        }


});

   gotomap.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
      Log.d(tag,sendtomap);
           Intent j= new Intent(show.this,mapactivity.class);
           j.putExtra("user-name",sendtomap);
           startActivity(j);



       }
   });

   gotomap2.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           startActivity(new Intent(show.this,newmapactivity.class));

       }
   });





}
}