package com.example.coper.rggame.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DialogTitle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.coper.rggame.POJO.Difficulty;
import com.example.coper.rggame.POJO.Scoring;
import com.example.coper.rggame.POJO.Sex;
import com.example.coper.rggame.R;
import com.example.coper.rggame.tools.RecAdapter;

import java.util.Vector;
//facebook button
import com.facebook.FacebookSdk;

/**
 * @author David García Molino
 */

public class SettingsActivity extends AppCompatActivity {

    private RecyclerView recView;
    private int imageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_settings);

        recView = (RecyclerView) findViewById(R.id.rvFriendsList);

        EditText name = (EditText) findViewById(R.id.etName);
        ImageView profileImage = (ImageView) findViewById(R.id.ivUserImage);
        Spinner difficulty = (Spinner) findViewById(R.id.sDifficultySpinner);
        Spinner sex = (Spinner) findViewById(R.id.sSexSpinner);

        if(savedInstanceState == null){
            Bitmap image;
            SharedPreferences preferences = getSharedPreferences("user_preferences",MODE_PRIVATE);

            if(difficulty != null && name != null && sex != null && profileImage != null) {
                difficulty.setSelection(Difficulty.Medium.ordinal());

                name.setText(preferences.getString("name",""));

                difficulty.setSelection(preferences.getInt("difficulty", Difficulty.Medium.ordinal()));
                sex.setSelection(preferences.getInt("sex", 0));

                if(sex.getSelectedItemPosition() == Sex.Woman.ordinal()) {
                    this.imageId = R.drawable.ui_default_batgirl;
                    image = BitmapFactory.decodeResource( getApplicationContext().getResources(),
                                                          this.imageId );
                }else {
                    this.imageId = R.drawable.ui_default_batman;
                    image = BitmapFactory.decodeResource( getApplicationContext().getResources(),
                                                          this.imageId);
                }

                profileImage.setImageBitmap(image);
            }

            recView.setAdapter(new RecAdapter(this, new Vector<Scoring>()));
            recView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            Bitmap image;
            if(name != null && difficulty != null && sex != null && profileImage != null) {
                name.setText(savedInstanceState.getString("name"));
                difficulty.setSelection(savedInstanceState.getInt("difficulty"));
                sex.setSelection(savedInstanceState.getInt("sex"));
                this.imageId = savedInstanceState.getInt("profileImage");
                image = BitmapFactory.decodeResource( getApplicationContext().getResources(),
                                                      this.imageId);
                profileImage.setImageBitmap(image);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        EditText name = (EditText) findViewById(R.id.etName);
        Spinner difficulty = (Spinner) findViewById(R.id.sDifficultySpinner);
        Spinner sex = (Spinner) findViewById(R.id.sSexSpinner);
        ImageView profile = (ImageView) findViewById(R.id.ivUserImage);
        Bundle tempSave = new Bundle();

        if(name != null && difficulty != null && sex != null) {
            tempSave.putString("name", name.getText().toString());
            tempSave.putInt("difficulty", difficulty.getSelectedItemPosition());
            tempSave.putInt("sex", sex.getSelectedItemPosition());
            tempSave.putInt("profileImage", this.imageId);
        }

        SharedPreferences preferences = getSharedPreferences("user_preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if(name != null && sex != null && difficulty != null && profile != null) {
            editor.putString("userName", name.getText().toString());
            editor.putInt("sex", sex.getSelectedItemPosition());
            editor.putInt("difficulty", difficulty.getSelectedItemPosition());
            editor.putInt("profileImage", this.imageId);
        }

        editor.apply();
    }

    public void onClickAddFriendButton(View v){
        DialogTitle dt = new DialogTitle(this);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Add a friend");

        // set dialog message
        alertDialogBuilder
                .setMessage("Select how you want to add a friend")
                .setCancelable(true)
                .setNegativeButton("Facebook",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ///TODO

                    }
                })
                .setPositiveButton("Proximity",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        ///TODO

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void onClickSaveButton(View v){
        EditText etName = (EditText) findViewById(R.id.etName);
        Spinner spin = (Spinner) findViewById(R.id.sDifficultySpinner);

        SharedPreferences preferences = getSharedPreferences("game_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if(etName != null && spin != null) {
            editor.putString("userName", etName.getText().toString());
            editor.putInt("difficulty", spin.getSelectedItemPosition());
        }

        editor.apply();
    }
}
