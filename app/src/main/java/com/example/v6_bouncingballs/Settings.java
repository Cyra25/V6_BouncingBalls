package com.example.v6_bouncingballs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.ArrayList;

public class Settings extends AppCompatActivity {
    private ArrayList<Integer> highScores = new ArrayList<Integer>();

    // set background
    private int background;
    private SharedPreferences preferences;
    private LinearLayout settingsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        preferences = getSharedPreferences("value", MODE_PRIVATE);
        settingsLayout = (LinearLayout) findViewById(R.id.settingsLayout);

        //to make all the scores in the list zero
        for (int i = 0; i < 5; ++i){
            highScores.add(0);
        }
    }

    // handles backgrounds
    @Override
    protected void onStart() {
        super.onStart();

        //change background
        background = preferences.getInt("background number", 0);
        if (background == 0) {
            settingsLayout.setBackgroundResource(R.drawable.blue_background);
        } else if (background == 1) {
            settingsLayout.setBackgroundResource(R.drawable.black_background);
        } else if (background == 2) {
            settingsLayout.setBackgroundResource(R.drawable.yellow_background);
        } else if (background == 3) {
            settingsLayout.setBackgroundResource(R.drawable.orange_background);
        } else if (background == 4) {
            settingsLayout.setBackgroundResource(R.drawable.red_background);
        } else if (background == 5) {
            settingsLayout.setBackgroundResource(R.drawable.pink_background);
        }
    }


    //on click of background 1
    public void changeToBackground1(View view){
        preferences.edit()
                .putInt("background number", 0)
                .apply();
        settingsLayout.setBackgroundResource(R.drawable.blue_background);
        Toast.makeText(this, "Background changed to blue", Toast.LENGTH_SHORT).show();
    }

    //on click of background 2
    public void changeToBackground2(View view){
        preferences.edit()
                .putInt("background number", 1)
                .apply();
        settingsLayout.setBackgroundResource(R.drawable.black_background);
        Toast.makeText(this, "Background changed to black", Toast.LENGTH_SHORT).show();
    }

    //on click of background 3
    public void changeToBackground3 (View view){
        preferences.edit()
                .putInt("background number", 2)
                .apply();
        settingsLayout.setBackgroundResource(R.drawable.yellow_background);
        Toast.makeText(this, "Background changed to yellow", Toast.LENGTH_SHORT).show();
    }

    //on click of background 4
    public void changeToBackground4 (View view){
        preferences.edit()
                .putInt("background number", 3)
                .apply();
        settingsLayout.setBackgroundResource(R.drawable.orange_background);
        Toast.makeText(this, "Background changed to orange", Toast.LENGTH_SHORT).show();
    }

    //on click of background 5
    public void changeToBackground5 (View view){
        preferences.edit()
                .putInt("background number", 4)
                .apply();
        settingsLayout.setBackgroundResource(R.drawable.red_background);
        Toast.makeText(this, "Background changed to red", Toast.LENGTH_SHORT).show();
    }

    //on click of background 6
    public void changeToBackground6 (View view){
        preferences.edit()
                .putInt("background number", 5)
                .apply();
        settingsLayout.setBackgroundResource(R.drawable.pink_background);
        Toast.makeText(this, "Background changed to pink", Toast.LENGTH_SHORT).show();
    }

    //on click of bubble 1
    public void changeToBubble1(View view){
        preferences.edit()
                .putInt("bubble number", 0)
                .apply();
        Toast.makeText(this, "bubble changed to blue", Toast.LENGTH_SHORT).show();
    }

    //on click of bubble2
    public void changeToBubble2(View view){
        preferences.edit()
                .putInt("bubble number", 1)
                .apply();
        Toast.makeText(this, "bubble changed to green", Toast.LENGTH_SHORT).show();
    }

    //on click of bubble 3
    public void changeToBubble3 (View view){
        preferences.edit()
                .putInt("bubble number", 2)
                .apply();
        Toast.makeText(this, "bubble changed to purple", Toast.LENGTH_SHORT).show();
    }

    //on click of bubble 4
    public void changeToBubble4 (View view){
        preferences.edit()
                .putInt("bubble number", 3)
                .apply();
        Toast.makeText(this, "bubble changed to red", Toast.LENGTH_SHORT).show();
    }

    //on click of bubble 5
    public void changeToBubble5 (View view){
        preferences.edit()
                .putInt("bubble number", 4)
                .apply();
        Toast.makeText(this, "bubble changed to yellow", Toast.LENGTH_SHORT).show();
    }

    //on click of lives 3
    public void changeLivesTo3(View view){
        preferences.edit()
                .putInt("number of lives", 3)
                .apply();
        Toast.makeText(this, "Number of lives changed to 3", Toast.LENGTH_SHORT).show();
    }

    //on click of lives 5
    public void changeLivesTo5(View view){
        preferences.edit()
                .putInt("number of lives", 5)
                .apply();
        Toast.makeText(this, "Number of lives changed to 5", Toast.LENGTH_SHORT).show();
    }

    //on click of lives 10
    public void changeLivesTo10 (View view){
        preferences.edit()
                .putInt("number of lives", 10)
                .apply();
        Toast.makeText(this, "Number of lives changed to 10", Toast.LENGTH_SHORT).show();
    }


    //on click of back button
    public void toMenu(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //on click of reset high scores
    public void resetHighScores(View view){
        FileHelper.writeDataEasy(highScores, this);
        FileHelper.writeDataHard(highScores, this);
        Toast.makeText(this, "High scores have been reset", Toast.LENGTH_SHORT).show();
    }
}


