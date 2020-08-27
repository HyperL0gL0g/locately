package com.example.fire.drawer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fire.R;
import com.example.fire.chatUtils.chatListAdapter;
import com.example.fire.chatUtils.chatUserInfoObj;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link something#newInstance} factory method to
 * create an instance of this fragment.
 */
public class something extends Fragment {

    String android_id;
    static List<chatUserInfoObj> mdata;
    RecyclerView recyclerView;
    chatListAdapter adapter;
    TextView emptyView;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public something() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment something.
     */
    // TODO: Rename and change types and number of parameters
    public static something newInstance(String param1, String param2) {
        something fragment = new something();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_something, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mdata = new ArrayList<>();
        recyclerView = getActivity().findViewById(R.id.recyclerViewChatList);

        recyclerView.setHasFixedSize(true);
        emptyView = getActivity().findViewById(R.id.empty_view1231);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        android_id = FirebaseAuth.getInstance().getUid();

        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("chat_userInfo");
        dref.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mdata.clear();
                if(dataSnapshot.exists()){
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    chatUserInfoObj post = dataSnapshot1.getValue(chatUserInfoObj.class);
                    Log.d("adadaddd", post.getDeviceName() + post.getDeviceID());
                    if(!post.getDeviceID().equals(android_id) && post.getAllowed().equals("1")) {
                        Log.d("data???",post.getusername());
                        mdata.add(new chatUserInfoObj(post.getDeviceID(), post.getusername()));
                    }
                    adapter = new chatListAdapter(getContext(), mdata);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                }
                }
                if(mdata.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

    }
}