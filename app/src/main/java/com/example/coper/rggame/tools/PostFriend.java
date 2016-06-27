package com.example.coper.rggame.tools;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * @author David García Molino
 */


public class PostFriend extends AsyncTask<String, Integer, Void> {


    //this method just post the user name and hes friend's name
    @Override
    protected Void doInBackground(String... params) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http");
        builder.authority("wwtbamandroid.appspot.com");
        builder.appendPath("rest/friends");
        builder.appendQueryParameter("name", params[0]);
        builder.appendQueryParameter("friend_name", params[1]);

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
            connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        //return is needed
        return null;
    }
}
