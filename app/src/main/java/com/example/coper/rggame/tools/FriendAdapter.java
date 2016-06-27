package com.example.coper.rggame.tools;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coper.rggame.POJO.Scoring;
import com.example.coper.rggame.R;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by coper on 23/06/16.
 */
public class FriendAdapter extends RecyclerView.Adapter<FriendHolder> {
    private ArrayList<String> friendList;
    private Context context;
    private LayoutInflater inflater;

    public FriendAdapter(Context context, ArrayList<String> friends) {
        this.friendList = friends;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public FriendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.friend_row, parent, false);
        return new FriendHolder(v);
    }

    @Override
    public void onBindViewHolder(FriendHolder holder, int position) {
        String currentFriend = this.friendList.get(position);
        holder.setFriendName(currentFriend);
    }

    @Override
    public int getItemCount() {
        return this.friendList.size();
    }

    public ArrayList<String> getFriends(){
        return this.friendList;
    }
}
