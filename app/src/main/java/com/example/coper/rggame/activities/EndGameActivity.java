package com.example.coper.rggame.activities;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.coper.rggame.POJO.Sex;
import com.example.coper.rggame.POJO.User;
import com.example.coper.rggame.R;
import com.example.coper.rggame.tools.MyOpenHelper;

public class EndGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        this.drawScreen();

        this.saveData();
    }

    private void saveData(){
        SharedPreferences inGame_prefs = getSharedPreferences("ingame_preferences",MODE_PRIVATE);
        SharedPreferences.Editor edit = inGame_prefs.edit();

        edit.remove("current_riddle");
        edit.apply();

        SharedPreferences game_prefs = getSharedPreferences("user_preferences", MODE_PRIVATE);
        MyOpenHelper database = new MyOpenHelper(this);

        User recording = new User();
        Bitmap imagen;

        if (game_prefs.getInt("sex", 0) == Sex.Woman.ordinal())
            imagen = ((BitmapDrawable) ResourcesCompat.getDrawable(getResources(),
                    R.drawable.ui_default_batgirl, null)).getBitmap();
        else
            imagen = ((BitmapDrawable) ResourcesCompat.getDrawable(getResources(),
                    R.drawable.ui_default_batman, null)).getBitmap();

        recording.setName(game_prefs.getString("userName", game_prefs.getString("name", "")));
        recording.setProfilePic(imagen);

        database.insert(recording, inGame_prefs.getInt("score", 0));
    }

    private void drawScreen(){
        SharedPreferences inGame_prefs = getSharedPreferences("ingame_preferences",MODE_PRIVATE);
        TextView congratsTitle = (TextView) findViewById(R.id.tvWinOrDie);
        TextView congratsMessage = (TextView) findViewById(R.id.tvWinOrDieMessage);

        if (congratsTitle != null && congratsMessage != null)
            if (inGame_prefs.getBoolean("winResult", true)){
                congratsTitle.setText(getResources().getString(R.string.congratsTitle));
                congratsMessage.setText(getResources().getString(R.string.congratsMessage));
            }else{
                congratsTitle.setText(getResources().getString(R.string.failTitle));
                congratsMessage.setText(getResources().getString(R.string.failMessage));
            }
    }

}
