package com.example.coper.rggame.POJO;

/**
 * Created by David García Molino on 14/05/16.
 */
public enum Difficulty {
    Easy(3),
    Medium(2),
    Hard(1),
    Riddle_Master(0);

    private int numCalls;

    Difficulty(int i) {
        this.numCalls = i;
    }

    public int getNumCalls(){
        return this.numCalls;
    }
}
