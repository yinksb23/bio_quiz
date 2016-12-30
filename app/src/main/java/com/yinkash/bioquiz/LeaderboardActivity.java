package com.yinkash.bioquiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity {

    private static final String TAG = "COMP211P";
    private Button mShowScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        final DatabaseHelper db = new DatabaseHelper(this);

        mShowScores = (Button)findViewById(R.id.bShowScores);
        mShowScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Reading all attempts
                Log.d(TAG, "Reading all attempts...");
                ArrayList<Scores> scores = db.getAllScores();

                for (Scores score : scores) {
                    String log = "Name: " + score.getUname1() + " , Score: " + score.getSavedScore();
                    // Writing attempts to log
                    Log.d(TAG, log);
                }

            }
        });
    }
}
