package com.example.coper.rggame.activities;

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

    public void onClickMenuButton(View v){
        Intent intention = null;

        switch(v.getId()){
            case R.id.playButton:
                intention = new Intent(this, PlayActivity.class);
                break;
            case R.id.scoresButton:
                intention = new Intent(this, ScoresActivity.class);
            break;
            case R.id.settingsButton:
                intention = new Intent(this, SettingsActivity.class);
            break;
        }

        startActivity(intention);
    }

    public void onClickSettingsButton(View v){

        startActivity(new Intent(this, SettingsActivity.class));

    }

}
