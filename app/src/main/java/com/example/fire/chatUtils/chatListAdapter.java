package com.example.fire.chatUtils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fire.R;
import com.example.fire.drawer.chatActivity;

import java.util.List;

public class chatListAdapter extends RecyclerView.Adapter<chatListAdapter.viewHolder> {

    Context context;
    List<chatListObj> mdata;

    public chatListAdapter(Context context, List<chatListObj> mdata) {
        this.context = context;
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.listview,null);
        chatListAdapter.viewHolder holder = new chatListAdapter.viewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        chatListObj chatListObj  = mdata.get(position);
        holder.tv.setText(chatListObj.getdeviceName());
        //
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, final int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(context, chatActivity.class);
                a.putExtra("userName",mdata.get(position).getdeviceName());
                a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                a.putExtra("userID",mdata.get(position).getdeviceID());
                context.startActivity(a);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        View view;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            this.view=itemView;
            tv = itemView.findViewById(R.id.UserName);
        }
    }

}
