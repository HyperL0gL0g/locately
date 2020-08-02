package com.example.fire.chatUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fire.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class messageListAdapter extends RecyclerView.Adapter<messageListAdapter.viewHolder>{

    private static final int layout_receive= 0;
    private static final int layout_send= 1;
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
        if(messageObjList.get(position).getSenderID().equals(android_id)){
            return layout_send;
        }else{
            return layout_receive;
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
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received,parent,false);
            viewHolder= new viewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.messageBody.setText(messageObjList.get(position).getMessage());
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm a");
        String dateString = formatter.format(new Date(Long.parseLong(messageObjList.get(position).getTimestamp())*1000L));
        holder.timeStamp.setText(dateString);
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



}

