package com.example.coper.rggame.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DialogTitle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArraySet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.coper.rggame.POJO.Riddle;
import com.example.coper.rggame.POJO.Sex;
import com.example.coper.rggame.POJO.User;
import com.example.coper.rggame.R;
import com.example.coper.rggame.tools.MyOpenHelper;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

/**
 * @author David García Molino
 */

public class PlayActivity extends AppCompatActivity {

    private final int RIDDLES_PER_GAME = 1;
    private Vector<Riddle> riddleList = null;
    private int [] indexes;
    private int currentRiddle, acumScore, bonusStreak;

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

        if(savedInstanceState == null) {
        /**
         * This case contemplates the situation when the application has been just started.
         */

            final SharedPreferences inGame_prefs = getSharedPreferences("ingame_preferences",MODE_PRIVATE);
            final int currentRid = inGame_prefs.getInt("currentRiddle",-1);
            if(currentRid != -1){

                DialogTitle dt = new DialogTitle(this);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                // set title
                alertDialogBuilder.setTitle(getResources().getString(R.string.continueGameTitle));

                // set dialog message
                alertDialogBuilder
                        .setMessage(getResources().getString(R.string.continueGameMessage))
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
                                PlayActivity.this.bonusStreak = inGame_prefs.getInt("bonusStreak",0);
                                PlayActivity.this.currentRiddle = currentRid;

                                Set<String> saved = inGame_prefs.getStringSet("prev_indexes", new HashSet<String>());
                                int position = 0;
                                for(String index : saved) {
                                        PlayActivity.this.indexes[position] = Integer.valueOf(index);
                                        position++;
                                }

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
            this.acumScore = savedInstanceState .getInt("score", 0);
            this.bonusStreak = savedInstanceState.getInt("bonus", 0);
            // Problem detected: NO GOOD RECOVERY OF THE INDEXES ARRAY
            int []recovered = savedInstanceState.getIntArray("prev_indexes");
            this.currentRiddle = savedInstanceState.getInt("currentRiddle", 0);

            if(recovered != null) {
                this.indexes = recovered.clone();
                /*
                int position = 0;
                for (int index : recovered) {
                    this.indexes[position] = index;
                    position++;
                }
                */
            }
        }

        this.playGame();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Bundle tempSave = new Bundle();

        tempSave.putInt("score", this.acumScore);
        tempSave.putInt("bonus", this.bonusStreak);
        tempSave.putIntArray("prev_indexes", this.indexes);
        tempSave.putInt("current", this.currentRiddle);

        SharedPreferences inGame_prefs = getSharedPreferences("ingame_preferences",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = inGame_prefs.edit();

        Set<String> prev_indexes = new HashSet<String>(RIDDLES_PER_GAME);
        for(int i = 0; i < RIDDLES_PER_GAME; i++)
            prev_indexes.add(Integer.toString(this.indexes[i]));

        prefsEditor.putInt("score", this.acumScore);
        prefsEditor.putInt("bonusStreak", this.bonusStreak);
        prefsEditor.putStringSet("prev_indexes", prev_indexes);
        prefsEditor.putInt("currentRiddle", this.currentRiddle);

        prefsEditor.apply();
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

        for(int i = 0; i < this.RIDDLES_PER_GAME; i++){
            Random random = new Random();
            int randTaken = random.nextInt(this.riddleList.size());
            if (!this.indexContained(i, randTaken)) {
                this.indexes[i] = randTaken;
            }
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
        for(int i = 0; i <= currentPos; i++)
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

        return (realAnswer.compareToIgnoreCase(proposedAnswer) == 0);
    }

    private void loadNextRiddle(){

        if (this.currentRiddle < this.RIDDLES_PER_GAME)
            this.drawScreen();
        else
            endGame();

    }

    private void endGame() {
        /**
         * This method implements the saving of the data from the game to the local scores
         * SQLite table and removes current_riddle from the preferences in order to not detect a
         * false started game when the user starts to play again.
         */
        SharedPreferences inGame_prefs = getSharedPreferences("ingame_preferences",MODE_PRIVATE);
        SharedPreferences.Editor edit = inGame_prefs.edit();

        edit.remove("current_riddle");
        edit.apply();

        SharedPreferences game_prefs = getSharedPreferences("user_preferences", MODE_PRIVATE);
        MyOpenHelper database = new MyOpenHelper(this);

        User recording = new User();
        int imageId;
        if (game_prefs.getInt("sex", 0) == Sex.Woman.ordinal())
            imageId = game_prefs.getInt("profileImage", R.drawable.ui_default_batgirl);
        else
            imageId = game_prefs.getInt("profileImage", R.drawable.ui_default_batman);

        recording.setName(game_prefs.getString("userName", game_prefs.getString("name", "")));
        recording.setProfilePic(BitmapFactory.decodeResource( getApplicationContext().getResources(),
                                                              imageId ));

        database.insert( recording, this.acumScore );
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
            this.bonusStreak = 0;
            etAnswer.setError("So... You are not as brilliant as you thought, huh?");
        }
    }
}
