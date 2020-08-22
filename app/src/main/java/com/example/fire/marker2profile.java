package com.example.fire;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class marker2profile extends AppCompatActivity {
    private FirebaseFirestore db;
private TextView who_am_i,name_of_user,online_status,user_mobile_number;
private Button send,chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker2profile);
        who_am_i=(TextView)findViewById(R.id.who_am_i);
        name_of_user=(TextView)findViewById(R.id.name_of_user);
        online_status=(TextView)findViewById(R.id.online_status);
        user_mobile_number=(TextView)findViewById(R.id.user_mobile_number);
        send=(Button)findViewById(R.id.send);
        chat=(Button)findViewById(R.id.chat);
        db=FirebaseFirestore.getInstance();

        final String user_here= getIntent().getStringExtra("name of the user");
        Log.i("name received",user_here);




    }
}
