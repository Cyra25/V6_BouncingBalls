package com.example.v6_bouncingballs;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainGame extends AppCompatActivity{
    int num =0;
    private float speedX;
    private float speedY;
    public float[] speedsX = new float[5];
    public float[] speedsY= new float[5];
//    Collections.shuffle(numbers);
    public ArrayList<TextView> bubbleList = new ArrayList<>();
    public int screenWidth, screenHeight;
    private int radius = 100;
    boolean continueGame = true;
    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private Equations equationsManager = new Equations();
    private String[][] equations;
    public String[] selectedEqns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_game);

        WindowManager windowManager = getWindowManager();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int screenWidth = size.x;
        final int screenHeight = size.y;
        equations = equationsManager.getEquations();
        TextView blueBubble = (TextView) findViewById(R.id.blueBubble);
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.main_layout);
        Button stop = (Button) findViewById(R.id.stop);

        float[] speeds = new float[]{5.0f, -5.0f};

        //creating the bubble
        for (int i = 0; i < 5; i++) {
            Random random = new Random();
            TextView bubble = new TextView(this);
            bubble.setTextSize(15);
            bubble.setBackground(getDrawable(R.drawable.soapbubble_blue));
            //shows which group of number(index) the equation is from
            final int numberGrp = 0;
            //numberGrp ++;
            //shows which equation(index) of that group
            int rndEqn = random.nextInt((equations[numberGrp].length-1) + 1);
            String eqnsToSet = equations[numberGrp][rndEqn];
            bubble.setText(eqnsToSet);
            selectedEqns = new String[]{};
//            selectedEqns[eqnsToSet]
            bubble.setGravity(Gravity.CENTER);
            constraintLayout.addView(bubble);
            bubble.getLayoutParams().height = 200;
            bubble.getLayoutParams().width = 200;
            bubble.setBackground(getDrawable(R.drawable.soapbubble_blue));
            bubble.setX(random.nextInt(1000 + 1));
            bubble.setY(random.nextInt(800 + 1));
            speedsX[i] = speeds[random.nextInt(2)];
            speedsY[i] = speeds[random.nextInt(2)];
            bubbleList.add(bubble);
//            Button stop = (Button) findViewById(R.id.stop);
//            stop.setOnClickListener(this);
//            bubble.setOnTouchListener(checkAns);
            bubble.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
//                    numberGrp
                    return false;
                }
            });
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        changePosition(screenWidth, screenHeight);
                    }
                });
            }
        },0,20);
        this.setContentView(constraintLayout);
    }

    public void changePosition(int screenWidth, int screenHeight){
        num = 0;
        while (num<5) {
            if (bubbleList.get(num).getX()+2*radius > screenWidth) {
                speedsX[num] = speedsX[num] * -1;
            } else if (bubbleList.get(num).getX() < 0) {
                speedsX[num] = speedsX[num] * -1;
            }
            else if (bubbleList.get(num).getY()+2*radius > screenHeight-60) {
                speedsY[num] = speedsY[num] * -1;
            } else if (bubbleList.get(num).getY() < 0) {
                speedsY[num] = speedsY[num] * -1;
            }
            bubbleList.get(num).setX(bubbleList.get(num).getX() + speedsX[num]);
            bubbleList.get(num).setY(bubbleList.get(num).getY() + speedsY[num]);
            bubbleList.get(num).getX();
            bubbleList.get(num).getY();
            num++;
        }
    }
    View.OnTouchListener checkAns = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
//                bubble.getTextId
                return true;
            }
            return false;
        }
    };

}
