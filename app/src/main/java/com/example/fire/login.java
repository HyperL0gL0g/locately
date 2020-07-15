package com.example.fire;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class login extends AppCompatActivity {
    private EditText email,password;
    private Button login,map_button,register;
    private ProgressBar login_progress;
    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page);
        email=(EditText) findViewById(R.id.email);
        login=(Button)findViewById(R.id.login);
        login_progress=(ProgressBar)findViewById(R.id.login_progress);

        password=(EditText)findViewById(R.id.password);
        register=(Button)findViewById(R.id.register);
        mauth=FirebaseAuth.getInstance();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),register.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_id=email.getText().toString().trim();
                String password_id=password.getText().toString().trim();
                login_progress.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                mauth.signInWithEmailAndPassword(email_id,password_id).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            login_progress.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(getApplicationContext(),"logged in ",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),dashboard.class));
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        login_progress.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(login.this, "Invalid password", Toast.LENGTH_SHORT).show();
                        } else if (e instanceof FirebaseAuthInvalidUserException) {
                            Toast.makeText(login.this, "Incorrect email address", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(login.this,"Dont know", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });





    }
}



