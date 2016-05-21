package com.example.coper.rggame.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Vector;

/**
 * @author David García Molino
 */

public class PlayActivity extends AppCompatActivity {

    private Vector<Riddle> riddleList = null;
    private int[] indexes;
    private int currentRiddle, acumScore, bonusStreak;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        this.setupAnswerField();
        this.fillRiddleVector();

        if(savedInstanceState == null) {

            this.initIndexesArray();

            this.currentRiddle = 0;
            this.acumScore = 0;
            this.bonusStreak = 0;

        }else{

            this.currentRiddle = savedInstanceState.getInt("current");
            this.bonusStreak = savedInstanceState.getInt("bonus");
            this.acumScore = savedInstanceState .getInt("score");
            this.indexes = savedInstanceState.getIntArray("indexes");

        }

        this.playGame();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Bundle tempSave = new Bundle();

        tempSave.putIntArray("indexes", this.indexes);
        tempSave.putInt("current", this.currentRiddle);
        tempSave.putInt("bonus", this.bonusStreak);
        tempSave.putInt("score", this.acumScore);
    }

    private void initIndexesArray() {
        this.indexes = new int[10];
        int position = 0;

        while (position < this.indexes.length) {
            Double randomTaken = Math.floor(Math.random()) % this.riddleList.size();
            if (Arrays.asList(this.indexes).contains(randomTaken.intValue()) == false) {
                this.indexes[position] = randomTaken.intValue();
                position++;
            }
        }
    }

    private void playGame() {
        /**
         * Temporarily this method will set the riddles from the riddleList using the indexes
         * array. In the future, it will just iterate within the array recovered from the
         * database in SQLite
         */
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

            /*
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
            riddle.setText(this.riddleList.get(this.indexes[this.currentRiddle]).getRiddle());
            answer.setText("");
        }
    }

    public void fillRiddleVector() {
        /**
         / The first implementation of this method stored hardcoded all the riddles in a Vector of
         / riddles. Final implementation will recover the indexes stored at the String array from
         / a SQLite database, storage of the riddles.
         / A game consists in 10 riddles with a bonus score for each riddle answered in a row
         */
        XmlPullParser riddleLoader = getResources().getXml(R.xml.riddles);
        this.riddleList = new Vector<>();

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
         * SQLite table
         */
        /// TODO
    }

    public boolean checkAnswer() {
        EditText answerField = (EditText) findViewById(R.id.etAnswer);

        String proposedAnswer = "";
        String realAnswer = this.riddleList.get(this.indexes[this.currentRiddle]).getAnswer();

        if(answerField!=null)
            proposedAnswer = answerField.getText().toString();

        return (realAnswer.compareToIgnoreCase(proposedAnswer) == 0);
    }

}
