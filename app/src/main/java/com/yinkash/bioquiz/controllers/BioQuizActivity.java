package com.yinkash.bioquiz.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yinkash.bioquiz.DatabaseHelper;
import com.yinkash.bioquiz.R;
import com.yinkash.bioquiz.models.Result;
import com.yinkash.bioquiz.models.User;
import com.yinkash.bioquiz.models.Question;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BioQuizActivity extends AppCompatActivity {

    private static final String TAG = "BioQuizActivity";
    private static final String KEY_INDEX = "index";

    private final DatabaseHelper db = new DatabaseHelper(this);

    private TextView mQuestionStringTextView;
    private TextView mQuestionNumberTextView;
    private TextView mScoreTextView;
    private ListView mQuestionListView;

    private Question[] mQuestionBank = new Question[]{
            new Question(0, "Is the sky blue?", true),
            new Question(1, "Are dogs reptiles?", false),
            new Question(2, "Can plants photosynthesise?", true),
            new Question(3, "Are sharks fish?", true),
            new Question(4, "The atmosphere is made up of 42% Oxygen", false),
    };

    private int currentQuestionId = 0;
    private int totalAnswered = 0;
    private int totalSkipped = 0;
    private int totalCheated = 0;
    private int score = 0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_quiz);

        mQuestionStringTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionNumberTextView = (TextView) findViewById(R.id.question_number_text_view);
        mScoreTextView = (TextView) findViewById(R.id.score_text_view);
        mQuestionListView = (ListView) findViewById(R.id.lvQuestionList);

        // Extract all question strings from array
        List<String> questionStrings = new ArrayList<>();
        for (Question question : mQuestionBank) {
            questionStrings.add("Question "+ (question.getId()+1));
        }

        // Add questionStrings to list view
        ArrayAdapter adapter = new ArrayAdapter<>(
                this,
                R.layout.activity_listview,
                questionStrings.toArray()
        );
        mQuestionListView.setAdapter(adapter);

        // On question select, update current question
        mQuestionListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                if (mQuestionListView.getChildAt(position).isEnabled()) {
                    setQuestion(mQuestionBank[position]);
                }
            }
        });

        Button trueButton = (Button) findViewById(R.id.true_button);
        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                nextQuestion();
            }
        });

        Button falseButton = (Button) findViewById(R.id.false_button);
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                nextQuestion();
            }
        });

        Button skipButton = (Button) findViewById(R.id.skip_button);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalSkipped++;
                nextQuestion();
            }
        });

        Button cheatButton = (Button) findViewById(R.id.cheat_button);
        cheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean questionAnswer = mQuestionBank[currentQuestionId].getAnswer();
                Intent i = CheatActivity.newIntent(BioQuizActivity.this, questionAnswer);
                startActivity(i);
                totalCheated++;
                nextQuestion();
            }
        });

        if (savedInstanceState != null) {
            currentQuestionId = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        setQuestion(mQuestionBank[currentQuestionId]);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, currentQuestionId);
    }

    private void setQuestion(Question question) {
        currentQuestionId = question.getId();
        mQuestionStringTextView.setText(question.getQuestionString());
        mQuestionNumberTextView.setText(String.format("Question %d/%d", question.getId() + 1, mQuestionBank.length));
        mScoreTextView.setText(String.format("Score: %d", score));
    }

    private void nextQuestion() {
        mQuestionListView.getChildAt(currentQuestionId).setEnabled(false);
        mQuestionListView.getChildAt(currentQuestionId).setClickable(false);

        int totalCompleted = (totalAnswered + totalSkipped + totalCheated);
        if (totalCompleted == mQuestionBank.length) {
            saveScore();
        } else {
            int nextAvailableQuestion = currentQuestionId;

            while (!mQuestionListView.getChildAt(nextAvailableQuestion).isEnabled()) {
                nextAvailableQuestion = (nextAvailableQuestion + 1) % mQuestionBank.length;
                mQuestionListView.setNextFocusDownId(nextAvailableQuestion);
            }
            setQuestion(mQuestionBank[nextAvailableQuestion]);
        }
    }

    private void checkAnswer(boolean userAnswer) {
        boolean questionAnswer = mQuestionBank[currentQuestionId].getAnswer();

        int messageResId = R.string.incorrect_toast;

        if (userAnswer == questionAnswer) {
            messageResId = R.string.correct_toast;
            score++;
        }
        totalAnswered++;

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    private void saveScore() {
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String spUserName = sharedPref.getString("username", "");

        User retrievedUser = db.getUserByUserName(spUserName);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

        Result result = new Result(retrievedUser.getId(), score);
        result.setCreatedOn(dateFormat.format(new Date()));
        db.saveResult(result);

        Intent intent = new Intent(BioQuizActivity.this, LeaderboardActivity.class);
        startActivity(intent);
    }
}
