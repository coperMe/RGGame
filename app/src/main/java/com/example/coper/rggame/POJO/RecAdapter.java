package com.example.coper.rggame.POJO;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coper.rggame.R;

import java.util.Vector;

/**
 * @author David García Molino
 */
public class RecAdapter extends RecyclerView.Adapter<UserHolder> {

    private LayoutInflater inflater;
    protected Vector<Scoring> highScores;

    public RecAdapter(Context contexto, Vector<Scoring> highScores) {
        inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.highScores = highScores;
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = inflater.inflate(R.layout.score_row, null);
        return new UserHolder(v);

    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
        Scoring score = this.highScores.elementAt(position);

        holder.setProfilePic(score.getUser().getProfilePic());
        holder.setName(score.getUser().getName());
        holder.setPoints(Integer.toString(score.getScore()));
    }

    @Override
    public int getItemCount() {

        return highScores.size();

    }

}
