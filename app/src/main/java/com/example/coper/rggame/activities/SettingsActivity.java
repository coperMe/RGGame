package com.example.coper.rggame.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.coper.rggame.POJO.Difficulty;
import com.example.coper.rggame.POJO.Scoring;
import com.example.coper.rggame.R;
import com.example.coper.rggame.tools.RecAdapter;

import java.util.Vector;

/**
 * @author David García Molino
 */

public class SettingsActivity extends AppCompatActivity {

  private RecyclerView recView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    recView = (RecyclerView) findViewById(R.id.rvFriendsList);

    Spinner difficulty = (Spinner) findViewById(R.id.sDifficultySpinner);

    if(savedInstanceState == null){
      if(difficulty != null)
        difficulty.setSelection(Difficulty.Medium.ordinal());

      recView.setAdapter(new RecAdapter(this, new Vector<Scoring>()));
      recView.setLayoutManager(new LinearLayoutManager(this));

    } else {
      EditText name = (EditText) findViewById(R.id.etName);

      if(name != null && difficulty != null) {
        name.setText(savedInstanceState.getString("name"));
        difficulty.setSelection(savedInstanceState.getInt("difficulty"));
      }
    }
  }

  @Override
  protected void onPause() {
    super.onPause();

    EditText name = (EditText) findViewById(R.id.etName);
    Spinner difficulty = (Spinner) findViewById(R.id.sDifficultySpinner);
    Bundle tempSave = new Bundle();

    if(name != null && difficulty != null) {
      tempSave.putString("name", name.getText().toString());
      tempSave.putInt("difficulty", difficulty.getSelectedItemPosition());
    }
  }

  public void onClickAddFriendButton(View v){

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
}