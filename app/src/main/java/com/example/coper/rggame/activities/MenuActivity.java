package com.example.coper.rggame.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.coper.rggame.R;

/**
 * @author David García Molino
 */

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                //Intent intent = new Intent(this, SummaryActivity.class);
                Intent intent = new Intent(this, EndGameActivity.class);
                startActivity(intent);
            }
        }
    }

    public void onClickMenuButton(View v){
        Intent intention = null;

        switch(v.getId()){
            case R.id.playButton:
                intention = new Intent(this, PlayActivity.class);
                startActivityForResult(intention, 0);
                break;
            case R.id.scoresButton:
                intention = new Intent(this, ScoresActivity.class);
                startActivityForResult(intention, -1);
            break;
            case R.id.settingsButton:
                intention = new Intent(this, SettingsActivity.class);
                startActivityForResult(intention, -2);
            break;
        }
    }
}
