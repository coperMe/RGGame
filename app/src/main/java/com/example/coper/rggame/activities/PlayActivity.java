package com.example.coper.rggame.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.coper.rggame.POJO.Difficulty;
import com.example.coper.rggame.POJO.Riddle;
import com.example.coper.rggame.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

/**
 * @author David García Molino
 */

public class PlayActivity extends AppCompatActivity {

    private final int RIDDLES_PER_GAME = 10;
    private Vector<Riddle> riddleList = null;
    private int [] indexes;
    private int currentRiddle, acumScore, bonusStreak;
    private int num_errors;
    private PlayActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        View layout = findViewById(R.id.background);
        if(layout != null)
            layout.getBackground().setAlpha(180);

        this.setupAnswerField();
        this.fillRiddleVector();
        this.initIndexes();

        context = this;
        if(savedInstanceState == null) {
            /**
             * This case contemplates the situation when the application has been just started.
             */

            final SharedPreferences inGame_prefs = getSharedPreferences("ingame_preferences",MODE_PRIVATE);
            final int currentRid = inGame_prefs.getInt("currentRiddle",-1);
            if(currentRid != -1){

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                // set title
                alertDialogBuilder.setTitle(getResources().getString(R.string.continueGameTitle));

                // set dialog message
                alertDialogBuilder
                        .setMessage(getResources().getString(R.string.continueGameMessage))
                        .setCancelable(false)
                        .setNegativeButton(R.string.notContinueGame,new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                context.currentRiddle = 0;
                                context.acumScore = 0;
                                context.bonusStreak = 0;
                                context.num_errors = 0;
                                context.drawScreen();
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(R.string.continueGame,new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                context.num_errors = inGame_prefs.getInt("errors", 0);
                                context.acumScore = inGame_prefs.getInt("score", 0);
                                context.bonusStreak = inGame_prefs.getInt("bonusStreak", 0);
                                context.currentRiddle = currentRid;

                                Set<String> saved = inGame_prefs.getStringSet("prev_indexes", new HashSet<String>());
                                int position = 0;
                                for(String index : saved) {
                                    context.indexes[position] = Integer.valueOf(index);
                                    position++;
                                }
                                context.drawScreen();
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
                this.num_errors = 0;
            }

        }else{
            /**
             * This case contemplates the situation when the application has been  previously
             * started and the user have changed the focus on the app or the orientation of the
             * screen.
             */


            this.num_errors = savedInstanceState.getInt("errors");
            this.acumScore = savedInstanceState .getInt("score");
            this.bonusStreak = savedInstanceState.getInt("bonus");

            int []recovered = savedInstanceState.getIntArray("prev_indexes");
            this.currentRiddle = savedInstanceState.getInt("currentRiddle");
            System.out.println(currentRiddle);
            if(recovered != null) {
                int position = 0;
                for (int index : recovered) {
                    this.indexes[position] = index;
                    position++;
                }

            }
        }

        this.playGame();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences inGame_prefs = getSharedPreferences("ingame_preferences",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = inGame_prefs.edit();

        Set<String> prev_indexes = new HashSet<String>(RIDDLES_PER_GAME);
        for(int i = 0; i < RIDDLES_PER_GAME; i++)
            prev_indexes.add(Integer.toString(this.indexes[i]));

        prefsEditor.putInt("score", this.acumScore);
        prefsEditor.putInt("errors", this.num_errors);
        prefsEditor.putInt("bonusStreak", this.bonusStreak);
        prefsEditor.putStringSet("prev_indexes", prev_indexes);
        prefsEditor.putInt("currentRiddle", this.currentRiddle);

        prefsEditor.apply();

        setResult(Activity.RESULT_CANCELED);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("score", this.acumScore);
        savedInstanceState.putInt("errors", this.num_errors);
        savedInstanceState.putInt("bonus", this.bonusStreak);
        savedInstanceState.putIntArray("prev_indexes", this.indexes);
        savedInstanceState.putInt("currentRiddle", this.currentRiddle);
        System.out.println(this.currentRiddle);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void playGame() {

        this.drawScreen();

    }

    private void drawScreen() {
        TextView riddle = (TextView) findViewById(R.id.tvRiddle);
        TextView score = (TextView) findViewById(R.id.acumulatedScore);
        EditText answer = (EditText) findViewById(R.id.etAnswer);

        if(riddle != null && answer != null && score != null) {
            score.setText(String.valueOf(this.acumScore));
            riddle.setText(this.riddleList.get(this.indexes[this.currentRiddle]).getRiddle());
            answer.setText("");
        }
    }

    private void initIndexes() {
        this.indexes = new int[RIDDLES_PER_GAME];
        ArrayList<Integer> auxlist = new ArrayList<Integer>();
        int i = 0;
        while (i < RIDDLES_PER_GAME) {
            Random random = new Random();
            int randTaken = random.nextInt(this.riddleList.size());
            if (!auxlist.contains(randTaken)) {
                this.indexes[i] = randTaken;
                i++;
            }
        }
        for (int j = 0; j < auxlist.size(); j++) {
            this.indexes[j] = auxlist.get(j);
        }
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

                        if(PlayActivity.this.checkAnswer()){
                            PlayActivity.this.acumScore += 25 + 50*bonusStreak;
                            PlayActivity.this.bonusStreak++;
                            PlayActivity.this.currentRiddle++;

                            PlayActivity.this.loadNextRiddle();
                        }else {
                            EditText etAnswer = (EditText) findViewById(R.id.etAnswer);

                            PlayActivity.this.bonusStreak = 0;
                            if(etAnswer != null)
                                etAnswer.setError(getResources().getString(R.string.wrongAnswer));
                        }

                        return true;
                    }

                    return false;

                }
            });
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

    private boolean indexContained(int currentPos, int candidateIndex){
        for(int i = 0; i < currentPos; i++)
            if(this.indexes[i] == candidateIndex)
                return true;

        return false;
    }

    public boolean checkAnswer() {
        EditText answerField = (EditText) findViewById(R.id.etAnswer);

        String proposedAnswer = "";

        String realAnswer = this.riddleList.get(this.indexes[this.currentRiddle]).getAnswer();

        if(answerField!=null)
            proposedAnswer = answerField.getText().toString();

        if(realAnswer.compareToIgnoreCase(proposedAnswer) == 0)
            return true;
        else{


            return false;
        }
    }

    private void loadNextRiddle(){

        if (this.currentRiddle < this.RIDDLES_PER_GAME)
            this.drawScreen();
        else {
            SharedPreferences inGame_prefs = getSharedPreferences("ingame_preferences",MODE_PRIVATE);
            SharedPreferences.Editor edit = inGame_prefs.edit();

            edit.putBoolean("winResult", true);
            edit.apply();

            this.endGame();
        }
    }

    private void endGame() {
        /**
         * This method implements the saving of the data from the game to the local scores
         * SQLite table and removes current_riddle from the preferences in order to not detect a
         * false started game when the user starts to play again.
         */
        SharedPreferences inGame_prefs = getSharedPreferences("ingame_preferences",MODE_PRIVATE);
        SharedPreferences.Editor edit = inGame_prefs.edit();

        edit.putInt("score", this.acumScore);
        edit.apply();
        setResult(Activity.RESULT_OK);

        this.finish();
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

            this.loadNextRiddle();

        } else if(etAnswer!=null) {
            SharedPreferences prefs = getSharedPreferences("user_preferences", MODE_PRIVATE);
            int selectedDifficultyOrdinal = prefs.getInt("difficulty", Difficulty.Medium.ordinal());
            Difficulty selectedDifficulty = Difficulty.values()[selectedDifficultyOrdinal];

            this.num_errors++;

            if (this.num_errors >= selectedDifficulty.getNumCalls()){
                SharedPreferences inGame_prefs = getSharedPreferences("ingame_preferences",MODE_PRIVATE);
                SharedPreferences.Editor edit = inGame_prefs.edit();

                edit.putBoolean("winResult", false);
                edit.apply();

                this.endGame();
            }else {
                this.bonusStreak = 0;
                etAnswer.setError(getResources().getString(R.string.wrongAnswer));
            }
        }
    }
}