package com.example.fire;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class dashboard extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        TextView user_mail = (TextView) findViewById(R.id.mail);
        TextView dash = (TextView) findViewById(R.id.dash);
        Button gotoadd = (Button) findViewById(R.id.gotoadd);
        Button signout = (Button) findViewById(R.id.signout);
        FirebaseUser u  = FirebaseAuth.getInstance().getCurrentUser();
        String uid = u.getUid();
        String name= u.getEmail();
        String username=u.getDisplayName();
        Log.d("username",username);

        user_mail.setText(name);
        dash.setText(username+"'s"+" DashBoard");

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(),"Signing-Out",Toast.LENGTH_LONG).show();
                Intent i   = new Intent(getApplicationContext(),MainActivity.class);
                finish();
                startActivity(i);
            }
        });
        gotoadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

    }
}

