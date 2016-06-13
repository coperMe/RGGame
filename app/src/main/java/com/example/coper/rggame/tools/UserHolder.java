package com.example.coper.rggame.tools;

import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coper.rggame.R;

/**
 * @author David García Molino
 */

public class UserHolder extends RecyclerView.ViewHolder {

    private AppCompatImageView profilePic;
    private TextView name, points;

    public UserHolder(View itemView) {
        super(itemView);

        profilePic = (AppCompatImageView) itemView.findViewById(R.id.profileImage);
        name = (TextView) itemView.findViewById(R.id.tvScoreName);
        points = (TextView) itemView.findViewById(R.id.tvScorePoints);
    }

    public ImageView getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Bitmap profilePic) {
        this.profilePic.setImageBitmap(profilePic);
    }

    public TextView getName() {
        return name;
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public String getPoints() {
        return points.getText().toString();
    }

    public void setPoints(String points) {
        this.points.setText(points);
    }
}
