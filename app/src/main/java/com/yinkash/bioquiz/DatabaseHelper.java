package com.yinkash.bioquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by yinksb23 on 13/12/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TAG = "COMP211P";

    //Database Version
    private static final int DATABASE_VERSION= 1;

    //Database Name
    private static final String DATABASE_NAME = "contacts.db";//Name of database

    //Table Names
    private static final String TABLE_NAME = "contacts";
    private static final String TABLE_SCORES = "scores";

    //COLUMN NAMES
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_UNAME = "uname";
    private static final String COLUMN_PASS = "pass";
    private static final String COLUMN_SCORE = "score";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    SQLiteDatabase db;

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACTS_TABLE = "create table contacts" + "(id integer primary key," +
                "name text, email text, uname text, pass text);";

        String CREATE_SCORES_TABLE = "create table scores" + "(id integer primary key," +
                "email text, score integer);";

        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_SCORES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop older table if existed
        String query = "DROP TABLE IF EXISTS" + TABLE_NAME + TABLE_SCORES;
        db.execSQL(query);
        //create database again
        this.onCreate(db);
    }

    //Creates a new player
    public void insertContact(Contact c)
    {
        db= this.getWritableDatabase();

        //Verify there are no two users with the same Username
        boolean CONTACT_NOT_ADDED = !doesContactExist(c.getUname());

        if(CONTACT_NOT_ADDED)
        {
            ContentValues values = new ContentValues();

            String query = "select * from contacts";
            Cursor cursor = db.rawQuery(query, null);
            int count = cursor.getCount();

            values.put(COLUMN_ID, count);
            values.put(COLUMN_NAME, c.getName());
            values.put(COLUMN_EMAIL, c.getEmail());
            values.put(COLUMN_UNAME, c.getUname());
            values.put(COLUMN_PASS, c.getPass());

            db.insert(TABLE_NAME, null, values);
            db.close();
        } else{

            Log.d(TAG, "addUser: " + c.getUname() + " already exists in the database.");
        }
    }

    // Getting one user
    public Contact getContact(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID,
                        COLUMN_NAME, COLUMN_EMAIL, COLUMN_PASS}, COLUMN_UNAME + "=?",
                new String[]{username}, null, null, null, null);

        Contact contact = null;

        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
            contact = new Contact(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            cursor.close();
        }

        // return shop
        return contact;
    }

    // Getting All Users
    public ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setUname(cursor.getString(1));
                contact.setEmail(cursor.getString(2));
                contact.setPass(cursor.getString(3));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return contactList;
    }

    // Getting users Count
    public int getContactsCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    // Updating a user
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_UNAME, contact.getUname());
        values.put(COLUMN_EMAIL, contact.getEmail());
        values.put(COLUMN_PASS, contact.getPass());

        // updating row
        return db.update(TABLE_NAME, values, COLUMN_UNAME + " = ?",
                new String[]{contact.getUname()});
    }

    // Deleting a user
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_UNAME + " = ?",
                new String[] { contact.getUname() });
        db.close();
    }

    // Adding a new score
    public void addScore(Scores score) {
        SQLiteDatabase db = this.getWritableDatabase();

        boolean IS_VALID_ATTEMPT = !doesContactExist(score.getUname1());

        if (IS_VALID_ATTEMPT) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_EMAIL, score.getUname1()); // Username
            values.put(COLUMN_SCORE, score.getSavedScore()); // User score

            // Inserting Row
            db.insert(TABLE_SCORES, null, values);
            db.close(); // Closing database connection
            Log.d(TAG, "addAttempt: If loop insertion successful.");
        } else {
            Log.d(TAG, "addAttempt: The username of the attempt made is not a valid username.");
        }

    }

    // Getting All Attempts
    public ArrayList<Scores> getAllScores() {
        ArrayList<Scores> scoresList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_SCORES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Scores score = new Scores();
                score.setUname1(cursor.getString(1));
                score.setSavedScore(cursor.getInt(2));
                // Adding contact to list
                scoresList.add(score);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return scoresList;
    }

    // Getting attempts Count
    public int getScoresCount() {
        String countQuery = "SELECT * FROM " + TABLE_SCORES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public String searchPass (String y)
    {
        db = this.getReadableDatabase();
        Cursor cursor = db.query("contacts", null, " uname=?",
                new String[] { y }, null, null, null);

        //Cursor cursor1 = db.query()

        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String z= cursor.getString(cursor.getColumnIndex("pass"));
        cursor.close();
        db.close();
        return z;
    }

    public String publishLeaderboard(String testEmail1, String testUname1){

        //IT WORKS
        String testUname = testUname1;
        String testEmail = testEmail1;
        db = this.getReadableDatabase();
        String [] columns = new String[]{COLUMN_ID, COLUMN_EMAIL, COLUMN_SCORE};
        Cursor c = db.query(TABLE_SCORES, columns, COLUMN_EMAIL + "='" + testEmail + "'", null, null, null, COLUMN_SCORE+" DESC");
        String result = "";

        int cID = c.getColumnIndex(COLUMN_ID);
        int cEmail = c.getColumnIndex(COLUMN_EMAIL);
        int cScore = c.getColumnIndex(COLUMN_SCORE);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
        {
            result = result + "Scores for user " + testUname + " "  + c.getString(2) + "\n";
        }

        c.close();
        return result;
    }

    private boolean doesContactExist(String username) {
        return !(getContact(username) == null);
    }
}
