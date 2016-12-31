package com.yinkash.bioquiz.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.yinkash.bioquiz.DatabaseHelper;
import com.yinkash.bioquiz.R;
import com.yinkash.bioquiz.models.Result;
import com.yinkash.bioquiz.models.User;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        final DatabaseHelper db = new DatabaseHelper(this);

        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Integer currentUserId = sharedPref.getInt("userId", -1);

        User currentUser = db.getUserById(currentUserId);
        List<Result> resultList = db.getAllResultsByUserId(currentUserId);

        List<String> formattedList = new ArrayList<>();
        for (Result result : resultList) {
            formattedList.add(
                String.format("%s %s", currentUser.getName(), result.getScore())
            );
        }

        ListView results = (ListView) findViewById(R.id.lvLeaderboard);
        ArrayAdapter adapter = new ArrayAdapter<>(
                this,
                R.layout.activity_listview,
                formattedList.toArray()
        );

        results.setAdapter(adapter);
    }
}
