package com.example.coper.rggame.POJO;

import android.media.Image;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by David García Molino on 14/05/16.
 */
public class User {

    private Uri profilePic;
    private String name;
    private Difficulty difficulty;
    private ArrayList<User> friends;

    public User(){
        this.name = "John Doe";
        this.difficulty = Difficulty.Medium;
        this.friends = new ArrayList<User>();
    }

    public User(String name, Difficulty difficulty, ArrayList<User> friends) {
        this.name = name;
        this.difficulty = difficulty;
        this.friends = friends;
    }

    public Uri getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Uri profilePic) {
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }
}
