package com.example.coper.rggame.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.coper.rggame.POJO.Riddle;
import com.example.coper.rggame.R;

import java.util.Vector;

public class PlayActivity extends AppCompatActivity {

    private Vector<Riddle> riddleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
    }

    public Vector<Riddle> generateRiddleList(){

        riddleList = new Vector<Riddle>();
        Riddle rid = null;

        rid = new Riddle( 1,
                        "We're five little items of an everyday sort. " +
                        "You'll find us all in a tennis court" ,
                        "Vowels");
        riddleList.add(rid);

        rid = new Riddle( 2,
                        "What is the beginning of eternity, the end of time and space, " +
                        "the beginning of every end and the end of every race?",
                        "Letter E");
        riddleList.add(rid);
        rid = new Riddle( 3,
                        "The more you take away, the larger it grows. What is it?",
                        "A hole");
        riddleList.add(rid);
        rid = new Riddle( 4,
                        "Tear one off and scratch my head, what was once red is black instead.",
                        "A match");
        riddleList.add(rid);
        rid = new Riddle( 5,
                        "What is it that no man wants to have but no man want to loose?",
                        "A lawsuit");
        riddleList.add(rid);
        rid = new Riddle( 6,
                        "What is it that travels on all fours in the morning, " +
                                "on two legs at noon, and three at twilight?",
                        "A man");
        riddleList.add(rid);
        rid = new Riddle( 7,
                        "If you do not know where I am, then I am something, " +
                                "but when you know where I am, I am nothing. What am I?",
                        "A riddle");
        riddleList.add(rid);
        rid = new Riddle( 8,
                        "Everything in me is ancient. What am I?",
                        "A museum");
        riddleList.add(rid);
        rid = new Riddle( 9,
                        "What starts at the top and then goes down, " +
                                "has the ability to purify and has all the colours of the rainbow?",
                        "Light");
        riddleList.add(rid);
        rid = new Riddle( 10,
                        "What kind of art does Aquaman prefer?",
                        "Watercolor");
        riddleList.add(rid);
        rid = new Riddle( 11,
                        "It is nowhere, but it's everywhere except if something is. What is it?",
                        "Nothing");
        riddleList.add(rid);
        rid = new Riddle( 12,
                        "What is strong enough to smash ships but still fears the sun?",
                        "Ice");
        riddleList.add(rid);
        rid = new Riddle( 13,
                        "The poor have, the rich need it and if you eat it you'll die",
                        "Nothing");
        riddleList.add(rid);
        rid = new Riddle( 14,
                        "If you know me, you'll want to share me but if you share me, " +
                                "I'll be gone. What am I?",
                        "A secret");
        riddleList.add(rid);

        return riddleList;
    }
}
