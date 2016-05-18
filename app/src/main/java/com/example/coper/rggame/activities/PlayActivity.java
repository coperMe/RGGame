package com.example.coper.rggame.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.coper.rggame.POJO.Riddle;
import com.example.coper.rggame.R;

import java.util.Arrays;
import java.util.Vector;

/**
 * @author David García Molino
 */

public class PlayActivity extends AppCompatActivity {

    private Vector<Riddle> riddleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
    }


    public Vector<Riddle> generateRiddleVector(){
        /**
        / The first implementation of this method stored hardcoded all the riddles in a Vector of
        / riddles. Final implementation will recover the indexes stored at the String array from
        / a sqlite database, storage of the riddles.
        / A game consists in 10 riddles with a bonus for each riddle in a row
        */
        String []indexes;
        int position = 0;
        indexes = new String[10];

        while (position < indexes.length){
            Double random = Math.floor(Math.random())%14;
            if(!Arrays.asList(indexes).contains(random)) {
                indexes[position] = Integer.toString(random.intValue()+1);
                position++;
            }
        }

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
        rid = new Riddle( 15,
                        "I'll be right under your feet in the midday sun; you cannot leave me," +
                                "no matter how fast you run",
                        "Shadow");
        riddleList.add(rid);
        rid = new Riddle( 16,
                        "The more there is, the less you see. What could I be?",
                        "Darkness");
        riddleList.add(rid);
        rid = new Riddle( 17,
                        "Hit me hard and I will crack but you'll never stop me from staring back." +
                                "What am I?",
                        "Mirror");
        riddleList.add(rid);

        while (position < indexes.length){
            Double random = Math.floor(Math.random())%riddleList.size();
            if(!Arrays.asList(indexes).contains(random)) {
                indexes[position] = Integer.toString(random.intValue()+1);
                position++;
            }
        }

        return riddleList;
    }
}
