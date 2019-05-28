package com.example.v6_bouncingballs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ConstraintLayout chooseDiff, menuLayout, mainMenu;
    public int background;
    private Button play, easyMode, hardMode, setting, easyStart, hardStart;
    private TextView easyDesc, hardDesc;
    public SharedPreferences difficulty;
    public SharedPreferences.Editor editor;
    public Button backToMain;
    public SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainMenu = (ConstraintLayout) findViewById(R.id.mainMenu);
        play = (Button) findViewById(R.id.play);
        setting = (Button) findViewById(R.id.setting);
        chooseDiff = (ConstraintLayout) findViewById(R.id.chooseDiff);
        menuLayout = (ConstraintLayout) findViewById(R.id.menuLayout);
        easyMode = (Button) findViewById(R.id.easyMode);
        hardMode = (Button) findViewById(R.id.hardMode);
        easyDesc = (TextView) findViewById(R.id.easyDesc);
        hardDesc = (TextView) findViewById(R.id.hardDesc);
        easyStart = (Button) findViewById(R.id.easyStart);
        hardStart = (Button) findViewById(R.id.hardStart);
        backToMain = (Button) findViewById(R.id.backToMain);
        play.setOnClickListener(this);
        easyMode.setOnClickListener(this);
        hardMode.setOnClickListener(this);
        easyStart.setOnClickListener(this);
        hardStart.setOnClickListener(this);

        difficulty = getSharedPreferences("difficulty", Context.MODE_PRIVATE);
        preferences = getSharedPreferences("value", MODE_PRIVATE);

    }
    // handles backgrounds and bubbles
    @Override
    protected void onStart() {
        super.onStart();

        //change background
        background = preferences.getInt("background number", 0);
        if (background == 0) {
            mainMenu.setBackgroundResource(R.drawable.blue_background);
        } else if (background == 1) {
            mainMenu.setBackgroundResource(R.drawable.black_background);
        } else if (background == 2) {
            mainMenu.setBackgroundResource(R.drawable.yellow_background);
        } else if (background == 3) {
            mainMenu.setBackgroundResource(R.drawable.orange_background);
        } else if (background == 4) {
            mainMenu.setBackgroundResource(R.drawable.red_background);
        } else if (background == 5) {
            mainMenu.setBackgroundResource(R.drawable.pink_background);
        }
    }
    @Override
    public void onClick(View v) {
        Button clicked = (Button) v;
        switch (clicked.getId()){
            case (R.id.play):
                System.out.println("This is play button");
                menuLayout.setVisibility(View.INVISIBLE);
                chooseDiff.setVisibility(View.VISIBLE);
                break;
            case(R.id.easyMode):
                menuLayout.setVisibility(View.INVISIBLE);
                chooseDiff.setVisibility(View.INVISIBLE);
                easyDesc.setVisibility(View.VISIBLE);
                easyStart.setVisibility(View.VISIBLE);
                break;
            case(R.id.hardMode):
                menuLayout.setVisibility(View.INVISIBLE);
                chooseDiff.setVisibility(View.INVISIBLE);
                hardDesc.setVisibility(View.VISIBLE);
                hardStart.setVisibility(View.VISIBLE);
                break;
            case (R.id.easyStart):
                editor = difficulty.edit();
                editor.putString("difficulty", "easy");
                editor.apply();
                starting(v);
                break;
            case(R.id.hardStart):
                editor = difficulty.edit();
                editor.putString("difficulty", "hard");
                editor.apply();
                starting(v);
                break;
            case (R.id.setting):
                toSetting(v);
        }

    }

    //on click of the highscores button
    public void goToHighScores(View view){
        Intent intent3 = new Intent(this, HighScores.class );
        startActivity(intent3);
    }

    public void starting(View view){
        Intent intent = new Intent(this, MainGame.class);
        startActivity(intent);
    }

    public void toMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void toSetting(View view){
        Intent intent2 = new Intent(this, Settings.class);
        startActivity(intent2);
    }

    public void pauseGame(View view) {
    }
}
