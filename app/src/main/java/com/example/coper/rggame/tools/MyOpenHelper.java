package com.example.coper.rggame.tools;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

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
        SQLiteDatabase database = getWritableDatabase();

        /// Users table
        database.execSQL("CREATE TABLE Users (" +
                "userId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "profileImage BLOB, " +
                "name TEXT)");

        /// Scores table
        database.execSQL("CREATE TABLE Scores (" +
                "scoreId INTEGER PRIMARY KEY AUTOINCREMENT" +
                "userId INTEGER," +
                "score INTEGER" +
                "FOREIGN KEY userId REFERENCES Users(userId))");

        database.close();
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
                          cursor.getInt(1) +
                          //stream.toByteArray().toString() + ", " +
                          cursor.getInt(0) + ", " +
                          Integer.toString(score) +")");

        database.close();
    }


    public Vector extractAll(int quantity){

        return new Vector();
    }
}
