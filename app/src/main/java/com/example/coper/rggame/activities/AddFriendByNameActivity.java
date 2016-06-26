package com.example.coper.rggame.activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coper.rggame.R;
import com.example.coper.rggame.tools.PostFriend;

public class AddFriendByNameActivity extends AppCompatActivity {

    EditText name;
    Button add;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_by_name);

        name = (EditText) findViewById(R.id.txtName);
        add = (Button) findViewById(R.id.btAdd);
        cancel = (Button) findViewById(R.id.btCancel);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences game_prefs = getSharedPreferences("user_preferences", MODE_PRIVATE);
                String user = game_prefs.getString("name", "");
                String friend = name.getText().toString();

                new PostFriend().execute(user, friend);
                showToast("Friend added!");

                name.setText("");

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

    }

    public void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}
