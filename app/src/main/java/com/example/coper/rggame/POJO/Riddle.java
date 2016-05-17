package com.example.coper.rggame.POJO;

/**
 * Created by coper on 16/05/16.
 */
public class Riddle {
    private int number;
    private String riddle;
    private String answer;

    public Riddle(int number, String riddle, String answer) {
        this.number = number;
        this.riddle = riddle;
        this.answer = answer;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getRiddle() {
        return riddle;
    }

    public void setRiddle(String riddle) {
        this.riddle = riddle;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
