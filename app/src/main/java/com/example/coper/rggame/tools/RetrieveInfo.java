package com.example.coper.rggame.tools;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;

import com.example.coper.rggame.POJO.Scoring;
import com.example.coper.rggame.activities.ScoresActivity;
import com.google.gson.Gson;

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


public class RetrieveInfo extends AsyncTask<String, Integer, Vector<Scoring>> {

    private ScoresActivity instancia;

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
            //should be 1 only


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

    public void setScoresActivity(ScoresActivity sa) {
        this.instancia = sa;
    }
}
