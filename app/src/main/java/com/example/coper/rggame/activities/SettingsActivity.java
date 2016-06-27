package com.example.coper.rggame.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.coper.rggame.POJO.Difficulty;
import com.example.coper.rggame.POJO.Sex;
import com.example.coper.rggame.R;
import com.example.coper.rggame.tools.FriendAdapter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
//facebook button


/**
 * @author David García Molino
 */

public class SettingsActivity extends AppCompatActivity {

    private Context context = this;
  private RecyclerView recView;
  private int imageId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //initialize facebook sdk
    //FacebookSdk.sdkInitialize(getApplicationContext());

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

      //////
      recView.setAdapter(new FriendAdapter(this, new ArrayList<String>()));
      recView.setLayoutManager(new LinearLayoutManager(this));
    } else {
      if(name != null && difficulty != null && sex != null && profileImage != null) {
        this.imageId = savedInstanceState.getInt("profileImage");
        Bitmap image = BitmapFactory.decodeResource( getApplicationContext().getResources(),
                this.imageId );

        profileImage.setImageBitmap(image);
        name.setText(savedInstanceState.getString("name"));
        sex.setSelection(savedInstanceState.getInt("sex"));
        difficulty.setSelection(savedInstanceState.getInt("difficulty"));
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
    RecyclerView rvFriends = (RecyclerView) findViewById(R.id.rvFriendsList);
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

      /**
       *To review
       */

      Set<String> list = new HashSet<String>();
      for (int i = 0; i < recView.getLayoutManager().getItemCount(); i++){
        String friend;
        RecyclerView.Adapter thisOne = recView.getAdapter();
        list.add("");
      }
      editor.putStringSet("friendsList",list);
    }

    editor.apply();
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
            .setPositiveButton("Username", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog,int id) {
                  Intent intent = new Intent(context, AddFriendByNameActivity.class);
                  startActivityForResult(intent, 0);

              }
            });

    // create alert dialog
    AlertDialog alertDialog = alertDialogBuilder.create();

    // show it
    alertDialog.show();
  }

  private void addFriendInServer (String friendName) throws IOException {
    SharedPreferences prefs = getSharedPreferences("user_preferences", MODE_PRIVATE);
    Uri.Builder builder = new Uri.Builder();
    builder.scheme("http");
    builder.authority("wwtbamandroid.appspot.com");
    builder.appendPath("rest/highscores");

    URL destination = null;
    try {
      destination = new URL(builder.build().toString());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    HttpURLConnection connection = (HttpURLConnection) destination.openConnection();
    connection.setRequestMethod("POST");
    connection.setDoOutput(true);
    String coding = "UTF-8";

    String params =
            URLEncoder.encode("name", coding) + "=" +
                    URLEncoder.encode(prefs.getString("name", ""), coding) + "&" +
                    URLEncoder.encode("friend_name", coding) + "=" +
                    URLEncoder.encode(friendName,coding);
    OutputStream stream = connection.getOutputStream();
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream, "UTF-8"));
    writer.write(params);
    writer.flush();
    writer.close();
    stream.close();
  }
}