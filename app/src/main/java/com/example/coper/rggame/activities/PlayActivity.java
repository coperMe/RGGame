package com.example.coper.rggame.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DialogTitle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.coper.rggame.POJO.Riddle;
import com.example.coper.rggame.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

/**
 * @author David García Molino
 */

public class PlayActivity extends AppCompatActivity {

    private final static int RIDDLES_PER_GAME = 10;
    private Vector<Riddle> riddleList = null;
    private Set<String> indexes;
    private int currentRiddle, acumScore, bonusStreak;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        this.setupAnswerField();
        this.fillRiddleVector();
        this.initIndexes();

        if(savedInstanceState == null) {
        /**
         * This case contemplates the situation when the application has been just started.
         */
            final SharedPreferences inGame_prefs = getPreferences(MODE_PRIVATE);
            final int currentRid = inGame_prefs.getInt("currentRiddle",-1);
            if(currentRid != -1){

                DialogTitle dt = new DialogTitle(this);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                // set title
                alertDialogBuilder.setTitle("Continue game");

                // set dialog message
                alertDialogBuilder
                        .setMessage("A previous game has been detected. Dou you want to continue?")
                        .setCancelable(false)
                        .setNegativeButton(R.string.notContinueGame,new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                PlayActivity.this.currentRiddle = 0;
                                PlayActivity.this.acumScore = 0;
                                PlayActivity.this.bonusStreak = 0;

                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(R.string.continueGame,new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                PlayActivity.this.acumScore = currentRid;
                                PlayActivity.this.bonusStreak = inGame_prefs.getInt("bonusStreak",-1);
                                PlayActivity.this.currentRiddle = inGame_prefs.getInt("currentRiddle",-1);

                                PlayActivity.this.indexes = inGame_prefs.getStringSet("prev_indexes", null);

                                dialog.dismiss();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }else {
                this.currentRiddle = 0;
                this.acumScore = 0;
                this.bonusStreak = 0;
            }

        }else{
            /**
             * This case contemplates the situation when the application has been  previously
             * started and the user have changed the focus on the app or the orientation of the
             * screen.
             */
            String []savedIndexes = savedInstanceState.getStringArray("indexes");
            this.currentRiddle = savedInstanceState.getInt("current");
            this.bonusStreak = savedInstanceState.getInt("bonus");
            this.acumScore = savedInstanceState .getInt("score");
            this.indexes = new HashSet<String>();
            if(savedIndexes != null)
                this.indexes.addAll(Arrays.asList(savedIndexes));
        }

        this.playGame();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Bundle tempSave = new Bundle();


        int position = 0;
        String [] tempCasting = new String[10];
        for(String index : this.indexes) {
            tempCasting[position] = index;
            position++;
        }

        tempSave.putStringArray("indexes", tempCasting);
        tempSave.putInt("current", this.currentRiddle);
        tempSave.putInt("bonus", this.bonusStreak);
        tempSave.putInt("score", this.acumScore);

        SharedPreferences inGame_prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = inGame_prefs.edit();

        prefsEditor.putInt("currentRiddle", this.currentRiddle);
        prefsEditor.putInt("bonusStreak", this.bonusStreak);
        prefsEditor.putInt("score", this.acumScore);
        prefsEditor.putStringSet("prev_indexes", this.indexes);

        prefsEditor.apply();
    }

    private void initIndexes() {
        this.indexes = new HashSet<String>();
        int position = 0;

        while(position < this.RIDDLES_PER_GAME){
            Random random = new Random();
            int randTaken = random.nextInt(this.riddleList.size());
            if (!this.indexes.contains(String.valueOf(randTaken))) {
                this.indexes.add(String.valueOf(randTaken));
                position++;
            }
        }
    }

    private void playGame() {

        this.drawScreen();

    }

    private void setupAnswerField(){
        final EditText answer = (EditText) findViewById(R.id.etAnswer);

        if(answer != null) {
            /*
             * This listener is set in order to nullify the error message when the text inside the
             * the  editText changes.
             */
            answer.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    answer.setError(null);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            /**
             * This listener is set in order to detect when the user pushes the enter key in the
             * keyboard and it acts like the solveButton (checking answer and refreshing the screen.
             */
            answer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_NULL) {
                        /*
                        if(checkAnswer()){
                            acumScore += 25 + 50*bonusStreak;
                            bonusStreak++;
                            currentRiddle++;

                            if (currentRiddle < riddleList.size())
                                drawScreen();
                            else
                                endGame();
                        }else {
                            EditText etAnswer = (EditText) findViewById(R.id.etAnswer);

                            bonusStreak = 0;
                            if(etAnswer != null)
                                etAnswer.setError("So... You are not as brilliant as you thought, huh?");
                        }
                        */
                        return true;
                    }

                    return false;

                }
            });
        }
    }

    private void drawScreen() {
        TextView riddle = (TextView) findViewById(R.id.tvRiddle);
        TextView score = (TextView) findViewById(R.id.acumulatedScore);
        EditText answer = (EditText) findViewById(R.id.etAnswer);

        if(riddle != null && answer != null && score != null) {
            score.setText(String.valueOf(this.acumScore));

            String [] tempCasting = new String[10];
            int position = 0;

            for(String currentIndex : this.indexes) {
                tempCasting[position] = currentIndex;
                position++;
            }

            String currRiddle = this.riddleList.get(Integer.parseInt(tempCasting[this.currentRiddle])).getRiddle();
            riddle.setText(currRiddle);
            answer.setText("");
        }
    }

    public void fillRiddleVector() {
        /**
         / The implementation recovers the indexes stored at the String array from
         / a SQLite database, storage of the riddles.
         / A game consists in 10 riddles with a bonus score for each riddle answered in a row
         */
        XmlPullParser riddleLoader = getResources().getXml(R.xml.riddles);
        this.riddleList = new Vector<Riddle>();

        try {
            int riddleId=0;
            while (riddleLoader.getEventType() != XmlPullParser.END_DOCUMENT){
                if(riddleLoader.getEventType() == XmlPullParser.START_TAG &&
                        riddleLoader.getName().equals("riddle")){
                    this.riddleList.add(
                            new Riddle(riddleId,
                                       riddleLoader.getAttributeValue(0),
                                       riddleLoader.getAttributeValue(1)));
                    riddleId++;
                }
                riddleLoader.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickSolveButton(View v) {
        /**
         * This listener checks the answer and when it is right, it refreshes the screen with the
         * following riddle (if possible). When it checks the answer and it is wrong, it sets an
         * error message linked to the answer field.
         */
        EditText etAnswer = (EditText) findViewById(R.id.etAnswer);

        if (this.checkAnswer()) {
            this.acumScore += 25 + 50*this.bonusStreak;
            this.bonusStreak++;
            this.currentRiddle++;

            if (this.currentRiddle < this.riddleList.size())
                this.drawScreen();
            else
                endGame();
        } else if(etAnswer!=null) {
            this.bonusStreak = 0;
            etAnswer.setError("So... You are not as brilliant as you thought, huh?");
        }
    }

    private void endGame() {
        /**
         * This method implements the saving of the data from the game to the local scores
         * SQLite table and removes current_riddle from the preferences in order to not detect a
         * false started game when the user starts again to play.
         */
        SharedPreferences inGame_prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edit = inGame_prefs.edit();

        edit.remove("current_riddle");
        edit.apply();
    }

    public boolean checkAnswer() {
        EditText answerField = (EditText) findViewById(R.id.etAnswer);

        String proposedAnswer = "";
        String [] tempCasting = (String[]) this.indexes.toArray();

        String realAnswer = this.riddleList.get(Integer.valueOf(tempCasting[this.currentRiddle])).getAnswer();

        if(answerField!=null)
            proposedAnswer = answerField.getText().toString();

        return (realAnswer.compareToIgnoreCase(proposedAnswer) == 0);
    }

}
