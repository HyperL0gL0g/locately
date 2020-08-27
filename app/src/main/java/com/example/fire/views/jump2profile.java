package com.example.fire.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fire.R;
import com.example.fire.data;
import com.example.fire.drawer.chatActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class jump2profile extends AppCompatActivity {
    private FirebaseFirestore db;
    private TextView who_am_i,name_of_user,online_status,user_mobile_number;
    private Button send,chat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump2profile);
        who_am_i=(TextView)findViewById(R.id.who_am_i);
        name_of_user=(TextView)findViewById(R.id.name_of_user);
        online_status=(TextView)findViewById(R.id.online_status);
        user_mobile_number=(TextView)findViewById(R.id.user_mobile_number);
        send=(Button)findViewById(R.id.send);
        chat=(Button)findViewById(R.id.chat);
        db=FirebaseFirestore.getInstance();

        final String uid = getIntent().getStringExtra("uid_data");

        db.collection("user-profiles").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    data p = documentSnapshot.toObject(data.class);
                    who_am_i.setText(p.getName());
                    name_of_user.setText(p.getName());
                    user_mobile_number.setText(p.getPhone());
                }
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(getApplicationContext(), chatActivity.class);
//                a.putExtra("userName",mdata.get(position).getdeviceName());
                a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                a.putExtra("userID",uid);
                startActivity(a);
            }
        });






    }
}