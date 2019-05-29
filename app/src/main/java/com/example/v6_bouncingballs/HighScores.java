package com.example.v6_bouncingballs;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class HighScores extends AppCompatActivity {

    private TextView firstEasy, secondEasy, thirdEasy, fourthEasy, fifthEasy,
            firstHard, secondHard, thirdHard, fourthHard, fifthHard;
    private TextView[] allTextViewsEasy;
    private TextView[] allTextViewsHard;
    private ArrayList<Integer> highScoresEasy = new ArrayList<Integer>();
    private ArrayList<Integer> highScoresHard = new ArrayList<Integer>();

    private SharedPreferences preferences;
    private ConstraintLayout highScoresLayout;
    private int background;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_scores);

        preferences = getSharedPreferences("value", MODE_PRIVATE);
        highScoresLayout = (ConstraintLayout) findViewById(R.id.highScoresLayout);


        // easy score text views
        firstEasy = (TextView) findViewById(R.id.firstEasy);
        secondEasy = (TextView) findViewById(R.id.secondEasy);
        thirdEasy = (TextView) findViewById(R.id.thirdEasy);
        fourthEasy = (TextView) findViewById(R.id.fourthEasy);
        fifthEasy = (TextView) findViewById(R.id.fifthEasy);

        // hard score text views
        firstHard = (TextView) findViewById(R.id.firstHard);
        secondHard = (TextView) findViewById(R.id.secondHard);
        thirdHard = (TextView) findViewById(R.id.thirdHard);
        fourthHard = (TextView) findViewById(R.id.fourthHard);
        fifthHard = (TextView) findViewById(R.id.fifthHard);

        allTextViewsEasy = new TextView[]{firstEasy, secondEasy, thirdEasy, fourthEasy, fifthEasy};
        allTextViewsHard = new TextView[]{firstHard, secondHard, thirdHard, fourthHard, fifthHard};

        highScoresEasy = FileHelper.readDataEasy(this);

        //show on screen
        System.out.println(highScoresEasy + "Easy");
        int num = highScoresEasy.size();
        for (int i = 0; i < num; ++i) {
            allTextViewsEasy[i].setText((i + 1) + "     ---     " + highScoresEasy.get(i) + "pts");
        }

        highScoresHard = FileHelper.readDataHard(this);
        System.out.println(highScoresHard + "Hard");

        int num2 = highScoresHard.size();
        for (int i = 0; i < num2; ++i) {
            allTextViewsHard[i].setText((i + 1) + "     ---     " + highScoresHard.get(i) + "pts");
        }
    }

    // handles backgrounds and bubbles
    @Override
    protected void onStart() {
        super.onStart();

        //change background
        background = preferences.getInt("background number", 0);
        if (background == 0) {
            highScoresLayout.setBackgroundResource(R.drawable.blue_background);
        } else if (background == 1) {
            highScoresLayout.setBackgroundResource(R.drawable.black_background);
        } else if (background == 2) {
            highScoresLayout.setBackgroundResource(R.drawable.yellow_background);
        } else if (background == 3) {
            highScoresLayout.setBackgroundResource(R.drawable.orange_background);
        } else if (background == 4) {
            highScoresLayout.setBackgroundResource(R.drawable.red_background);
        } else if (background == 5) {
            highScoresLayout.setBackgroundResource(R.drawable.pink_background);
        }
    }
    //on click of back button
    public void goToMainMenu(View view){
        Intent intent = new Intent(this, MainActivity.class );
        startActivity(intent);
    }



}