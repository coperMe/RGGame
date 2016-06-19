package com.example.coper.rggame.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.TabHost;

import com.example.coper.rggame.POJO.Scoring;
import com.example.coper.rggame.R;
import com.example.coper.rggame.tools.MyOpenHelper;
import com.example.coper.rggame.tools.RecAdapter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

/**
 * @author David García Molino
 */

public class ScoresActivity extends AppCompatActivity {

    private RecyclerView recyclerLocal, recyclerFriends;
    private RecyclerView.LayoutManager lManagerLocal, lManagerFriends;

    private TabHost tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We ask for an indeterminate progress bar
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_scores);

        this.setProgressBarIndeterminate(true);

        if(savedInstanceState == null) {
            tabs = (TabHost) findViewById(R.id.tabHost);

            MyOpenHelper db = new MyOpenHelper(this);
            Vector<Scoring> data = db.extractAll();
            db.close();

            Vector<Scoring> friendsData = this.getFriendssHighscores();

            if (tabs != null) {
                tabs.setup();

                tabs.addTab(tabs.newTabSpec("tabLocal").setIndicator("Local highscores").setContent(R.id.rvLocalScores));
                tabs.addTab(tabs.newTabSpec("tabFriends").setIndicator("Friends highscores").setContent(R.id.rvFriendsScores));

                lManagerLocal = new LinearLayoutManager(this);
                recyclerLocal = (RecyclerView) findViewById(R.id.rvLocalScores);
                recyclerLocal.setAdapter(new RecAdapter(this, data));
                recyclerLocal.setLayoutManager(lManagerLocal);

                lManagerFriends = new LinearLayoutManager(this);
                recyclerFriends = (RecyclerView) findViewById(R.id.rvFriendsScores);
                recyclerFriends.setAdapter(new RecAdapter(this, friendsData));
                recyclerFriends.setLayoutManager(lManagerFriends);
            }
        }
    }

    private boolean checkConnection(){
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);

        if (manager != null){
            NetworkInfo info = manager.getActiveNetworkInfo();

            if (info.isAvailable() && info.isConnected())
                return true;
            else
                return false;
        }else{
            return false;
        }
    }

    private Vector<Scoring> getFriendssHighscores(){
        Vector<Scoring> data = new Vector<Scoring>();

        SharedPreferences user_prefs = getSharedPreferences("user_preferences", MODE_PRIVATE);

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http");
        builder.authority("wwtbamandroid.appspot.com");
        builder.appendPath("rest/highscores");
        builder.appendQueryParameter("name", user_prefs.getString("name", ""));

        /////////////
        RecyclerView friendList_rc = (RecyclerView) findViewById(R.id.rvFriendsList);

        URL destination = null;
        try {
            destination = new URL(builder.build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            if (destination != null) {
                HttpURLConnection connection = (HttpURLConnection) destination.openConnection();
                connection.setRequestMethod("GET");

                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                int status = connection.getResponseCode();
                if (status != -1){

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
