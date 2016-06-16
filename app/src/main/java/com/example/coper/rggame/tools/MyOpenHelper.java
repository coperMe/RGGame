package com.example.coper.rggame.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.coper.rggame.POJO.Scoring;
import com.example.coper.rggame.POJO.User;

import java.io.ByteArrayOutputStream;
import java.util.Vector;

/**
 * @author David García Molino
 */
public class MyOpenHelper extends SQLiteOpenHelper {

    public MyOpenHelper(Context context) {
        super(context, "ResquizzDatabase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * @author David García Molino
         */
        /// Users table
        db.execSQL("CREATE TABLE Users ( userId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                        "profileImage BLOB, " +
                                        "name TEXT)");

        /// Scores table
        db.execSQL("CREATE TABLE Scores ( scoreId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                         "userId INTEGER, " +
                                         "score INTEGER, " +
                                         "FOREIGN KEY (userId) REFERENCES Users(userId))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(User user, int score){
        SQLiteDatabase database = getWritableDatabase();

        Cursor cursor = database.rawQuery("SELECT userId, scoreId" +
                                          "FROM Users, Scores" +
                                          "WHERE name = " + user.getName(), null);
        cursor.moveToFirst();
        database.execSQL("INSERT INTO Scores " +
                         "VALUES ( null, " +
                                   cursor.getInt(1) + ", " +
                                   cursor.getInt(0) + ", " +
                                   score +")");

        cursor.close();
        database.close();
    }

    public void insert(String username, int score){
        /**
         * This method checks two possible scenarios.
         * The first one is that the username that we get as a parameter already exists in our
         * Users table in the database. Then, we just get the userId and we put the score in the
         * Scores table.
         * The second one is that the username that we get as a parameter, don't exist in our
         * database. In this case, we put this new user in the database and then, we recover the
         * userId and we insert the score.
         */
        SQLiteDatabase database = getWritableDatabase();

        ContentValues userInsertion = new ContentValues();
        ContentValues scoresInsertion = new ContentValues();

        Cursor cursor = database.rawQuery( "SELECT userId" +
                                           "FROM Users" +
                                           "WHERE name = " + username, null);
        cursor.moveToFirst();

        if(cursor.getCount() != 0) {
            scoresInsertion.put("userId", cursor.getInt(0));
            scoresInsertion.put("score", score);
            database.insert(getDatabaseName(), null, userInsertion);
            /*
            database.execSQL( "INSERT INTO Scores " +
                              "VALUES ( null, " +
                              cursor.getInt(0) + ", " +
                              score + ")" );
            */
        }else {
            /// TODO (the part of adding the userProfile image is missing)

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            String imagePath = "";
            Bitmap image  = BitmapFactory.decodeFile(imagePath);

            //image.compress(Bitmap.CompressFormat.PNG, 0, stream);

            //byte [] imageByted = stream.toByteArray();
            byte [] imageByted = new byte[1];

            userInsertion.put("profileImage", imageByted);
            userInsertion.put("name", username);
            database.insert(getDatabaseName(), null, userInsertion);
            /*
            database.execSQL("INSERT INTO Users" +
                    "VALUES ( null, " +
                    stream.toByteArray().toString() + ", " +
                    username + ")");
            */
            cursor = database.rawQuery( "SELECT userId" +
                                        "FROM Users" +
                                        "WHERE name = " + username, null );

            scoresInsertion.put("userId", cursor.getInt(0));
            scoresInsertion.put("score", score);
            database.insert(getDatabaseName(), null, scoresInsertion);
            /*
            database.execSQL("INSERT INTO Scores " +
                             "VALUES ( null, " +
                                       cursor.getInt(0) + ", " +
                                       score +")");
            */
        }

        cursor.close();
        database.close();
    }


    public Vector<Scoring> extractAll() {
        Vector<Scoring> recovered = new Vector<Scoring>();
        SQLiteDatabase database = getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT profileImage, name, score" +
                "FROM Users u INNER JOIN Scores s" +
                "ON u.userId = s.userId", null);

        if (cursor.moveToFirst()){
            do {
                byte[] imageByte = cursor.getBlob(0);
                Bitmap image = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);

                recovered.add(new Scoring(new User(image,
                        cursor.getString(1)),
                        cursor.getInt(2)));
            } while (cursor.moveToNext());

            cursor.close();
            database.close();

            return recovered;
        }else {
            cursor.close();
            database.close();

            return new Vector<Scoring>();
        }
    }
}
