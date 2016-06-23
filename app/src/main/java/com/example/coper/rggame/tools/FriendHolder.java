package com.example.coper.rggame.tools;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by coper on 23/06/16.
 */
public class FriendHolder extends RecyclerView.ViewHolder {

    private String friendName;

    public FriendHolder(View itemView) {
        super(itemView);
    }

    public void setFriendName(String name){
        this.friendName = name;
    }

    public String getFriendName(){
        return this.friendName;
    }
}
