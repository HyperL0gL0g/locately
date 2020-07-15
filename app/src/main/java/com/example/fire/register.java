package com.example.fire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.io.PrintWriter;

@SuppressLint("Registered")
public class register extends AppCompatActivity {
    //declare variables
    private FirebaseAuth mAuth;
    private EditText name;
    private EditText email;
    private  EditText password;
    private ProgressBar progress;
    private Button reg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        //initialise variables
        mAuth = FirebaseAuth.getInstance(); //firebase variable

        name=(EditText)findViewById(R.id.name);
        email =(EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        progress =  (ProgressBar)findViewById(R.id.progressBar2);
        reg=(Button) findViewById(R.id.reg);//register button
        //if user presses register button

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String mail = email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                final String username = name.getText().toString().trim();


                progress.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                mAuth.createUserWithEmailAndPassword(mail,pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    User user  =  new User(username,mail);
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                //new tryoutcode
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(username).build();

                                                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d("USER NAME", "User profile updated.");
                                                        }
                                                    }
                                                });
                                                //TRYOUT CODE ENDS
                                                Toast.makeText(register.this,"User registered",Toast.LENGTH_LONG).show();
                                                progress.setVisibility(View.GONE);
                                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                startActivity(new Intent(getApplicationContext(),login.class));
                                                finish();
                                            }
                                        }
                                    });

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progress.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(register.this, "Invalid password", Toast.LENGTH_SHORT).show();
                        } else if (e instanceof FirebaseAuthInvalidUserException) {

                            String errorCode =
                                    ((FirebaseAuthInvalidUserException) e).getErrorCode();

                            Toast.makeText(register.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });








    }
}
