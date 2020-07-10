package com.example.fire;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private EditText name,address,phone;
    private FirebaseFirestore db;
    private ProgressBar progbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progbar = (ProgressBar)findViewById(R.id.progbar);
        name=(EditText)findViewById(R.id.name);
        address=(EditText)findViewById(R.id.address);
        phone=(EditText)findViewById(R.id.phone);
        Button add = (Button) findViewById(R.id.ADD);
        db= FirebaseFirestore.getInstance();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_name=name.getText().toString().trim();
                String user_mobile=phone.getText().toString().trim();
                String user_addr=address.getText().toString().trim();


                progbar.setVisibility(View.VISIBLE);
                CollectionReference profile =db.collection("user-profiles");
                data obj =  new data(user_name,user_addr,user_mobile);

                profile.add(obj)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_LONG).show ();
                                Log.d("err","reached");
                                Intent i = new Intent(MainActivity.this,show.class);
                                i.putExtra("name",user_name);
                                startActivity(i);
                                progbar.setVisibility(View.GONE);

                               // startActivity(new Intent(getApplicationContext(),show.class));


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        progbar.setVisibility(View.VISIBLE);

                    }
                });

            }
        });




    }
}

