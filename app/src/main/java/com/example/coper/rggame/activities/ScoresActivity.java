package com.example.coper.rggame.activities;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TabHost;

import com.example.coper.rggame.POJO.Scoring;
import com.example.coper.rggame.R;
import com.example.coper.rggame.tools.MyOpenHelper;
import com.example.coper.rggame.tools.RecAdapter;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Vector;

/**
 * @author David García Molino
 */

public class ScoresActivity extends AppCompatActivity {

    static public Vector<Scoring> friendsData = new Vector<Scoring>();

    private RecyclerView recyclerLocal, recyclerFriends;
    private RecyclerView.LayoutManager lManagerLocal, lManagerFriends;

    private TabHost tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scores);

        // We ask for an indeterminate progress bar
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        //this.setProgressBarIndeterminate(true);

        if (savedInstanceState == null) {
            tabs = (TabHost) findViewById(R.id.tabHost);

            Vector<Scoring> data = this.getLocalData();
            /*
            Vector<Scoring> friendsData = new Vector<Scoring>();
            try {
                friendsData = this.getRemoteData();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            */
            this.getRemoteData();

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

    private Vector<Scoring> getLocalData() {

        MyOpenHelper db = new MyOpenHelper(this);
        Vector<Scoring> local = db.extractAll();
        db.close();

        return local;
    }

    private void getRemoteData() {


        SharedPreferences user_prefs = getSharedPreferences("user_preferences", MODE_PRIVATE);
        //in postexecute it should pass the scores to our array
        RetrieveInfo ri = new RetrieveInfo();
        ri.execute(user_prefs.getString("name", ""));

    }
/*
    private boolean checkConnection() {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);

        if (manager != null) {
            NetworkInfo info = manager.getActiveNetworkInfo();

            if (info.isAvailable() && info.isConnected())
                return true;
            else
                return false;
        } else {
            return false;
        }
    }*/
/*
    public void addFriendScore(Scoring s){
        this.friendsData.add(s);
    }

    public Vector<Scoring> getFriendsData(){
        return this.friendsData;
    }*/
class RetrieveInfo extends AsyncTask<String, Integer, Vector<Scoring>> {

    @Override
    protected Vector<Scoring> doInBackground(String... params) {
        //fixing
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http");
        builder.authority("wwtbamandroid.appspot.com");
        builder.appendPath("rest/highscores");
        builder.appendQueryParameter("name", params[0]);

        URL destination = null;
        try {
            destination = new URL(builder.build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Vector<Scoring> data = new Vector<Scoring>();
        try {

            HttpURLConnection connection = (HttpURLConnection) destination.openConnection();
            connection.setRequestMethod("GET");
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            Gson gson = new Gson();

            Scoring[] res = gson.fromJson(reader, Scoring[].class);
            for (Scoring elem : res) {
                data.add(elem);
            }


        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception exc) {
            System.err.println("FileNotFound");
        }

        return data;

    }

    @Override
    protected void onPostExecute(Vector<Scoring> result) {
        super.onPostExecute(result);

        Vector<Scoring> vector = ScoresActivity.friendsData;
        for (Scoring aux : result) {
            vector.add(aux);
        }
    }

    //refresh progress
    @Override
    protected void onProgressUpdate(Integer... values) {

    }

}


}