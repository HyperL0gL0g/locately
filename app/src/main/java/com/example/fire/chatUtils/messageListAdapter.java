package com.example.fire.chatUtils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fire.R;
import com.example.fire.data;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class messageListAdapter extends RecyclerView.Adapter<messageListAdapter.viewHolder>{

    private static final int layout_receive= 0;
    private static final int layout_send= 1;
    private static final int layout_accept_req= 2;
    private static final int layout_send_req= 3;
    private static final int layout_start_chat= 4;
    String android_id;
    Context context;
    List<messageObj> messageObjList;

    public messageListAdapter(Context context, List<messageObj> messageObjList,String android_id) {
        this.context = context;
        this.messageObjList = messageObjList;
        this.android_id = android_id;
    }

    @Override
    public int getItemViewType(int position) {
        String uid = FirebaseAuth.getInstance().getUid();
        if(!messageObjList.get(position).getrequestIDs().equals(uid) && !messageObjList.get(position).getrequestIDs().equals("123") && !messageObjList.get(position).getrequestIDs().equals("456")){
            return layout_accept_req;
        }else if(messageObjList.get(position).getrequestIDs().equals("456")){
            return layout_start_chat;
        }else if(messageObjList.get(position).getrequestIDs().equals(FirebaseAuth.getInstance().getUid())){
            return layout_send_req;
        }else{
            if(messageObjList.get(position).getSenderID().equals(android_id)){
                return layout_send;
            }else{
                return layout_receive;
            }
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        viewHolder viewHolder = null;
        if(viewType == layout_send){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent,parent,false);
            viewHolder = new viewHolder(view);

        }if(viewType == layout_receive){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received,parent,false);
            viewHolder= new viewHolder(view);
        }

        if(viewType == layout_accept_req){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.accept_req,parent,false);
            viewHolder = new viewHolder(view);
        }

        if(viewType == layout_send_req){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_req,parent,false);
            viewHolder = new viewHolder(view);
        }

        if(viewType == layout_start_chat){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.start_chat,parent,false);
            viewHolder = new viewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, final int position) {

        if(holder.getItemViewType() == 2){
            final requestHolderAcept req = new requestHolderAcept(holder.itemView);
            FirebaseFirestore.getInstance().collection("user-profiles").document(messageObjList.get(position).getrequestIDs()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    data a = documentSnapshot.toObject(data.class);
                    req.message.setText(a.getName()+" "+messageObjList.get(position).getMessage());
                }
            });


            req.cross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "cross", Toast.LENGTH_SHORT).show();
                }
            });
            req.tick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String chatid = messageObjList.get(position).getChatID();
                    String[] ids = chatid.split("_");
                    String messageid = messageObjList.get(position).getMessageID();
                    DatabaseReference chat_messagesDref  =  FirebaseDatabase.getInstance().getReference("chat_messages");
                    chat_messagesDref.child(chatid).child(messageid).child("requestIDs").setValue("456");
                    FirebaseDatabase.getInstance().getReference("chat_userInfo").child(ids[1]).child("allowed").setValue("1");
                    FirebaseDatabase.getInstance().getReference("chat_userInfo").child(ids[0]).child("allowed").setValue("1");
                    Toast.makeText(context, "Request Accepted", Toast.LENGTH_SHORT).show();
                }
            });

        }else if(holder.getItemViewType() == 4){
            startChatHolder head = new startChatHolder(holder.itemView);
            head.headerChat.setText("Chat Started");

        }else if(holder.getItemViewType() == 3){
            requestHolder req = new requestHolder(holder.itemView);
            req.message123.setText("You "+messageObjList.get(position).getMessage());
        }else{
            holder.messageBody.setText(messageObjList.get(position).getMessage());
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm a");
            String dateString = formatter.format(new Date(Long.parseLong(messageObjList.get(position).getTimestamp())*1000L));
            holder.timeStamp.setText(dateString);
        }


    }

    @Override
    public int getItemCount() {
        return messageObjList.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{
        TextView messageBody,timeStamp;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            messageBody = itemView.findViewById(R.id.text_message_body);
            timeStamp = itemView.findViewById(R.id.text_message_time);

        }
    }

    class requestHolderAcept extends RecyclerView.ViewHolder{
        ImageButton tick,cross;
        TextView message;

        public requestHolderAcept(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.messagebody);
            tick = itemView.findViewById(R.id.tick);
            cross = itemView.findViewById(R.id.cross);
        }
    }

    class requestHolder extends RecyclerView.ViewHolder{
        TextView message123;

        public requestHolder(@NonNull View itemView) {
            super(itemView);
            message123 = itemView.findViewById(R.id.mess123);
        }
    }

    class startChatHolder extends RecyclerView.ViewHolder{
        TextView headerChat;

        public startChatHolder(@NonNull View itemView) {
            super(itemView);
            headerChat = itemView.findViewById(R.id.header);
        }
    }



}

