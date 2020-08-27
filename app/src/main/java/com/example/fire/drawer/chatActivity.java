package com.example.fire.drawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.fire.R;
import com.example.fire.chatUtils.messageListAdapter;
import com.example.fire.chatUtils.messageObj;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class chatActivity extends AppCompatActivity {

    ImageButton send;
    EditText edit;
    String android_id;
    static String chatID;
    ArrayList<String> temp;
    ArrayList<messageObj> messageObjArrayList;

    RecyclerView recyclerView;
    messageListAdapter adapter;
    DatabaseReference chat_messagesDref;


    boolean checker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        final String receiverID = getIntent().getStringExtra("userID");
//        String receiverName = getIntent().getStringExtra("userName");

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        messageObjArrayList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyvlerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        send = findViewById(R.id.button);
        edit = findViewById(R.id.editText);
        temp = new ArrayList<>();

        Log.d("receiverID",receiverID);

        android_id = FirebaseAuth.getInstance().getUid();

        chatID = android_id+"_"+receiverID;

        chat_messagesDref  =  FirebaseDatabase.getInstance().getReference("chat_messages");
        chat_messagesDref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    temp.add(dataSnapshot1.getKey());
                }
                Log.d("test",temp.toString());
                for (String item:temp) {
                    if(item.contains(android_id) && item.contains(receiverID)){
                        chatID= item;
                    }
                }

                chat_messagesDref.child(chatID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("runnng",chatID+String.valueOf(dataSnapshot.getValue()));
                        messageObjArrayList.clear();
                        for(DataSnapshot ds: dataSnapshot.getChildren()) {
                            messageObj messageObj = ds.getValue(messageObj.class);
                            if(messageObj.getrequestIDs().length() == 28){
                                checker=true;
                            }else{
                                checker = false;
                            }
                            messageObjArrayList.add(new messageObj(messageObj.getMessage(),messageObj.getSenderID(),messageObj.getMessageID(),messageObj.getChatID(),messageObj.getTimestamp(),messageObj.getrequestIDs()));
                            adapter = new messageListAdapter(getApplicationContext(), messageObjArrayList, android_id);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.d("updatedChaitid",chatID);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ts = String.valueOf(System.currentTimeMillis()/1000);
                String text = String.valueOf(edit.getText());
                if(text.equals("")){
                    Toast.makeText(chatActivity.this, "Cant send blank", Toast.LENGTH_SHORT).show();
                }else{
                    if(checker){
                        Toast.makeText(getApplicationContext(), "Not Authorized", Toast.LENGTH_SHORT).show();
                        edit.setText("");
                    }else{
                        String messageID = chat_messagesDref.push().getKey();
                        messageObj messageObj = new messageObj(text,android_id,messageID,chatID,ts,"123");
                        chat_messagesDref.child(chatID).child(messageID).setValue(messageObj);
                        edit.setText("");
                    }

                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


}