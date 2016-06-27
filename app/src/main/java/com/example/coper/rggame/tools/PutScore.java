package com.example.coper.rggame.tools;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;

import com.example.coper.rggame.POJO.Scoring;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * @author David García Molino
 */

public class PutScore extends AsyncTask<Scoring, Integer, Void> {

    @Override
    protected Void doInBackground(Scoring... params) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http");
        builder.authority("wwtbamandroid.appspot.com");
        builder.appendPath("rest/friends");
        builder.appendQueryParameter("name", params[0].getUser().getName());
        builder.appendQueryParameter("score", String.valueOf(params[0].getScore()));

        URL destination = null;
        try {
            destination = new URL(builder.build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) destination.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            connection.setRequestMethod("PUT");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        //return is needed
        return null;
    }
}
