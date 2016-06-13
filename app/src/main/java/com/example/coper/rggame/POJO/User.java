package com.example.coper.rggame.POJO;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * @author David García Molino
 */

public class User {

    private Bitmap profilePic;
    private String name;
    private Difficulty difficulty;
    private ArrayList<User> friends;

    public User(){
        this.name = "John Doe";
        this.difficulty = Difficulty.Medium;
        this.friends = new ArrayList<User>();
    }

    public User(Bitmap profileImage, String name, Difficulty difficulty, ArrayList<User> friends) {
        this.profilePic = profileImage;
        this.name = name;
        this.difficulty = difficulty;
        this.friends = friends;
    }



    public Bitmap getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Bitmap profilePic) {
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
