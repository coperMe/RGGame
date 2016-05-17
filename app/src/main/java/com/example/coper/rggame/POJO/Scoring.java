package com.example.coper.rggame.POJO;

/**
 * Created by coper on 16/05/16.
 */
public class Scoring {
    private User user;
    private int score;

    public Scoring(User user, int score) {
        this.user = user;
        this.score = score;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
