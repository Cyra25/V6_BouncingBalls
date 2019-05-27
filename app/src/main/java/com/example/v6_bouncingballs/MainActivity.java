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
    private ConstraintLayout chooseDiff, menuLayout;
    private Button play, easyMode, hardMode, easyDesc, hardDesc, setting;
    public SharedPreferences difficulty;
    public SharedPreferences.Editor editor;
    public Button backToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = (Button) findViewById(R.id.play);
        setting = (Button) findViewById(R.id.setting);
        chooseDiff = (ConstraintLayout) findViewById(R.id.chooseDiff);
        menuLayout = (ConstraintLayout) findViewById(R.id.menuLayout);
        easyMode = (Button) findViewById(R.id.easyMode);
        hardMode = (Button) findViewById(R.id.hardMode);
        easyDesc = (Button) findViewById(R.id.easyDesc);
        hardDesc = (Button) findViewById(R.id.hardDesc);
        backToMain = (Button) findViewById(R.id.backToMain);

        play.setOnClickListener(this);
        easyMode.setOnClickListener(this);
        hardMode.setOnClickListener(this);
        easyDesc.setOnClickListener(this);
        hardDesc.setOnClickListener(this);

        difficulty = getSharedPreferences("difficulty", Context.MODE_PRIVATE);


    }

    @Override
    public void onClick(View v) {
        Button clicked = (Button) v;
        switch (clicked.getId()){
            case (R.id.play):
                menuLayout.setVisibility(View.INVISIBLE);
                chooseDiff.setVisibility(View.VISIBLE);
                break;
            case(R.id.easyMode):
                menuLayout.setVisibility(View.INVISIBLE);
                chooseDiff.setVisibility(View.INVISIBLE);
                easyDesc.setVisibility(View.VISIBLE);
                break;
            case(R.id.hardMode):
                menuLayout.setVisibility(View.INVISIBLE);
                chooseDiff.setVisibility(View.INVISIBLE);
                hardDesc.setVisibility(View.VISIBLE);
                break;
            case (R.id.easyDesc):
                System.out.println("Hello this is easy description");
                editor = difficulty.edit();
                editor.putString("difficulty", "easy");
                editor.apply();
                starting(v);
                break;
            case(R.id.hardDesc):
                System.out.println("Hello this is hard description");
                editor = difficulty.edit();
                editor.putString("difficulty", "hard");
                editor.apply();
                starting(v);
                break;
        }
    }

    public void starting(View view){
        Intent intent = new Intent(this, MainGame.class);
        startActivity(intent);
    }

    public void toMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
