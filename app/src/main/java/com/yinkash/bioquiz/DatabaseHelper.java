package com.yinkash.bioquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.yinkash.bioquiz.models.User;
import com.yinkash.bioquiz.models.Result;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "COMP211P";

    //Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static final String DATABASE_NAME = "bioquiz.db";

    //Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_SCORES = "scores";

    //COLUMN NAMES
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    private static final String COLUMN_USER_ID = "userid";
    private static final String COLUMN_SCORE = "score";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private SQLiteDatabase db;

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USER_TABLE =
                String.format("CREATE TABLE %s (" +
                                "%s integer primary key," +
                                "%s text," +
                                "%s text," +
                                "%s text," +
                                "%s text);)",
                        TABLE_USERS,
                        COLUMN_ID,
                        COLUMN_NAME,
                        COLUMN_EMAIL,
                        COLUMN_USERNAME,
                        COLUMN_PASSWORD
                );

        String CREATE_SCORE_TABLE =
                String.format("CREATE TABLE %s (" +
                                "%s integer primary key," +
                                "%s text," +
                                "%s text);)",
                        TABLE_SCORES,
                        COLUMN_ID,
                        COLUMN_USER_ID,
                        COLUMN_SCORE
                );

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_SCORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Delete existing tables
        String query = "DROP TABLE IF EXISTS" + TABLE_USERS;
        db.execSQL(query);

        query = "DROP TABLE IF EXISTS" + TABLE_SCORES;
        db.execSQL(query);

        //create database again
        this.onCreate(db);
    }

    public User getUserById(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                null,
                COLUMN_ID + " = ?",
                new String[]{id.toString()},
                null,
                null,
                null,
                null
        );

        User user = null;

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            user = new User(
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
            user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            cursor.close();
        }
        db.close();
        return user;
    }

    public User getUserByUserName(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                null,
                COLUMN_USERNAME + " = ?",
                new String[]{username},
                null,
                null,
                null,
                null
        );

        User user = null;

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            user = new User(
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD))
            );
            user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            cursor.close();
        }
        db.close();
        return user;
    }

    //Creates a new user
    public void createUser(User user) {
        if (getUserByUserName(user.getUserName()) == null) {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(COLUMN_NAME, user.getName());
            values.put(COLUMN_EMAIL, user.getEmail());
            values.put(COLUMN_USERNAME, user.getUserName());
            values.put(COLUMN_PASSWORD, user.getPassword());

            db.insert(TABLE_USERS, null, values);
            db.close();
        } else {
            Log.d(TAG, "addUser: " + user.getUserName() + " already exists in the database.");
        }
    }

    // Create a new result
    public void createResult(Result result) {
        if (getUserById(result.getUserId()) != null) {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_ID, result.getUserId());
            values.put(COLUMN_SCORE, result.getScore());

            db.insert(TABLE_SCORES, null, values);
            db.close();
        } else {
            Log.d(TAG, "addAttempt: The username of the attempt made is not a valid username.");
        }
    }

    // Getting All Results
    public ArrayList<Result> getAllResults() {
        ArrayList<Result> results = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_SCORES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Result result = new Result(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_SCORE))
                );
                result.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                results.add(result);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return results;
    }

//    public String publishLeaderboard(String testEmail1, String testUname1) {
//
//        //IT WORKS
//        String testUname = testUname1;
//        String testEmail = testEmail1;
//        db = this.getReadableDatabase();
//
//        Cursor c = db.query(
//                TABLE_SCORES,
//                null,
//                String.format("%s = '%s'", COLUMN_EMAIL, testEmail),
//                null,
//                null,
//                null,
//                COLUMN_SCORE + " DESC"
//        );
//
//        String result = "";
//
//        int cID = c.getColumnIndex(COLUMN_ID);
//        int cEmail = c.getColumnIndex(COLUMN_EMAIL);
//        int cScore = c.getColumnIndex(COLUMN_SCORE);
//
//        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
//            result = result + "Result for user " + testUname + " " + c.getString(2) + "\n";
//        }
//
//        c.close();
//        db.close();
//        return result;
//    }

}
