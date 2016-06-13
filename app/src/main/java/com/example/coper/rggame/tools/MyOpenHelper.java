package com.example.coper.rggame.tools;

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
        db.execSQL("CREATE TABLE Users (" +
                "userId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "profileImage BLOB, " +
                "name TEXT)");

        /// Scores table
        db.execSQL("CREATE TABLE Scores (" +
                "scoreId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER, " +
                "score INTEGER, " +
                "FOREIGN KEY (userId) REFERENCES Users(userId))");

        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(User user, int score){
        SQLiteDatabase database = getWritableDatabase();

        Cursor cursor = database.rawQuery("SELECT userId, scoreId" +
                                          "FROM Users, Scores" +
                                          "WHERE name = " + user.getName(), null);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        user.getProfilePic().compress(Bitmap.CompressFormat.PNG, 0, stream);

        database.execSQL("INSERT INTO Users " +
                "VALUES ( null, " +
                          cursor.getInt(1) + ", " +
                          //stream.toByteArray().toString() + ", " +
                          cursor.getInt(0) + ", " +
                          Integer.toString(score) +")");

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


        Cursor cursor = database.rawQuery( "SELECT userId" +
                                           "FROM Users" +
                                           "WHERE name = " + username, null);

        if(cursor.getCount() != 0)
            database.execSQL("INSERT INTO Scores " +
                             "VALUES ( null, " +
                                       cursor.getInt(0) + ", " +
                                       score +")");
        else {
            /// TODO (the part of adding the userProfile image is missing)
            database.execSQL("INSERT INTO Users" +
                             "VALUES ( null, " +
                                      "null, " +
                                       username + ")");

            cursor = database.rawQuery( "SELECT userId" +
                                        "FROM Users" +
                                        "WHERE name = " + username, null );

            database.execSQL("INSERT INTO Scores " +
                             "VALUES ( null, " +
                                       cursor.getInt(0) + ", " +
                                       score +")");
        }

        cursor.close();
        database.close();
    }


    public Vector<Scoring> extractAll(){
        Vector<Scoring> recovered = new Vector<Scoring>();
        SQLiteDatabase database = getReadableDatabase();

        Cursor cursor = database.rawQuery( "SELECT profileImage, name, score" +
                                           "FROM Users u, Scores s" +
                                           "WHERE u.userId = s.userId", null );

        while (cursor.moveToNext()) {
            byte [] imageByte = cursor.getBlob(0);
            Bitmap image = BitmapFactory.decodeByteArray(imageByte,0,imageByte.length);

            recovered.add(new Scoring( new User( image,
                                                 cursor.getString(1),
                                                 null,
                                                 null ),
                                       cursor.getInt(2)));
        }

        cursor.close();
        database.close();

        return recovered;
    }
}
