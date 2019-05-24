package com.example.v6_bouncingballs;

import android.annotation.SuppressLint;
import android.graphics.Color;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainGame extends AppCompatActivity{
    private View grid1,grid2,grid3,grid4,grid5,grid6;
    int num =0;
    public boolean correctAns;
    private float speedX;
    private float speedY;
    private int idTest;
    int viewId;
    int compareCount = 0;
    public int indexSelected;
    public TextView scoreView;
    float[] speeds;
    public boolean touchedWrongArea;
    public boolean checkAsc;
    public float[] speedsX = new float[5];
    public float[] speedsY= new float[5];
//    Collections.shuffle(numbers);
    public ArrayList<TextView> bubbleList = new ArrayList<>();
    public int screenWidth, screenHeight;
    private int radius = 100;
    boolean continueGame = true;
    int rndInteger1, rndInterger2;
    int score = 0;
    private Handler handler = new Handler();
    private Timer timer = new Timer();
    int idCount;
    private Equations equationsManager = new Equations();
    private String[][] equations;
    public ArrayList<String> selectedEqns;
    public int[] idsInFile;
    public View[] grids;

    Random rndGrid = new Random();


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_game);

        //get the size of the screen and set the screenWidth and screenHeight
        WindowManager windowManager = getWindowManager();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int screenWidth = size.x;
        final int screenHeight = size.y;

        //get the equations
        equations = equationsManager.getEquations();

        //get IDs
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.main_layout);
//        Button stop = (Button) findViewById(R.id.stop);
        grid1= (View)findViewById(R.id.grid1);
        grid2= (View)findViewById(R.id.grid2);
        grid3= (View)findViewById(R.id.grid3);
        grid4= (View)findViewById(R.id.grid4);
        grid5= (View)findViewById(R.id.grid5);
        grid6= (View)findViewById(R.id.grid6);
        scoreView = (TextView) findViewById(R.id.scoreTextView);

        speeds = new float[]{5.0f, -5.0f};
        grids = new View[]{grid1, grid2, grid3, grid4, grid5, grid6};
        idsInFile = new int[]{2131165219, 2131165220, 2131165221, 2131165222,
                2131165223,2131165224,2131165225};
        selectedEqns = new ArrayList<>();

        //set which grids cannot be touched and make them disappear after some time
        rndInteger1 = rndGrid.nextInt((grids.length));
        rndInterger2 = rndGrid.nextInt(grids.length);
        while (rndInterger2 == rndInteger1){
            rndInterger2 = rndGrid.nextInt(grids.length);
        }
        grids[rndInteger1].setBackgroundColor(Color.BLUE);
        grids[rndInterger2].setBackgroundColor(Color.BLUE);
        grids[rndInteger1].postDelayed(new Runnable() {
            @Override
            public void run() {
                grids[rndInteger1].setVisibility(View.INVISIBLE);
            }
        },2000);
        grids[rndInterger2].postDelayed(new Runnable() {
            @Override
            public void run() {
                grids[rndInterger2].setVisibility(View.INVISIBLE);
            }
        },2000);


        grid1.setOnTouchListener(touchListener1);
        grid2.setOnTouchListener(touchListener2);
        grid3.setOnTouchListener(touchListener3);
        grid4.setOnTouchListener(touchListener4);
        grid5.setOnTouchListener(touchListener5);
        grid6.setOnTouchListener(touchListener6);

        //creating the bubble
        for (int i = 0; i < 5; i++) {
            Random random = new Random();
            final TextView bubble = new TextView(this);

            //set the size of the text
            bubble.setTextSize(15);

            //set the ID for the bubble
            bubble.setId(idsInFile[idCount]);
            idCount++;

            //shows which group of number(index) the equation is from
            int numberGrp = 0;

            //Set the equation
            int rndEqn = random.nextInt((equations[i].length-1));
            String eqnsToSet = equations[i][rndEqn];
            bubble.setText(eqnsToSet);
            selectedEqns.add(eqnsToSet);

            //set the equation text to center
            bubble.setGravity(Gravity.CENTER);
            constraintLayout.addView(bubble);

            //set the parameter(Width and height) for the bubble
            bubble.getLayoutParams().height = 200;
            bubble.getLayoutParams().width = 200;

            //set background for the bubble
            bubble.setBackground(getDrawable(R.drawable.soapbubble_blue));

            //set X and Y for the bubble
            bubble.setX(random.nextInt(1000 + 1));
            bubble.setY(random.nextInt(800 + 1));

            //set Speed
            speedsX[i] = speeds[random.nextInt(2)];
            speedsY[i] = speeds[random.nextInt(2)];
            bubbleList.add(bubble);
            numberGrp = i;
        }

        bubbleList.get(0).setOnTouchListener(bubbleTouchListener1);
        bubbleList.get(1).setOnTouchListener(bubbleTouchListener2);
        bubbleList.get(2).setOnTouchListener(bubbleTouchListener3);
        bubbleList.get(3).setOnTouchListener(bubbleTouchListener4);
        bubbleList.get(4).setOnTouchListener(bubbleTouchListener5);

        //change the position
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

    public void addScore(){
        if (correctAns){
            score++;
        }
        System.out.println("Score is "+score);
        scoreView.setText("Score: "+ score);
    }

    public void changePosition(int screenWidth, int screenHeight){
        num = 0;
        while (num<5) {
            if (bubbleList.get(num).getX()+2*radius > screenWidth) {
                speedsX[num] = speedsX[num] * -1;
            } else if (bubbleList.get(num).getX() < 0) {
                speedsX[num] = speedsX[num] * -1;
            }
            if (bubbleList.get(num).getY()+2*radius > screenHeight-60) {
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


    View.OnTouchListener touchListener1 = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (rndInteger1 == 0 || rndInterger2 == 0) {
                touchedWrongArea = false;
            } else {
                touchedWrongArea = true;
            }
            return touchedWrongArea;
        }
    };

    View.OnTouchListener touchListener2 = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (rndInteger1 == 1 || rndInterger2 == 1) {
                touchedWrongArea = false;
            } else {
                touchedWrongArea = true;
            }
            return touchedWrongArea;
        }
    };

    View.OnTouchListener touchListener3 = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (rndInteger1 == 2 || rndInterger2 == 2) {
                touchedWrongArea = false;
            } else {
                touchedWrongArea = true;
            }
            return touchedWrongArea;
        }
    };

    View.OnTouchListener touchListener4 = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (rndInteger1 == 3 || rndInterger2 == 3) {
                touchedWrongArea = false;
            } else {
                touchedWrongArea = true;
            }
            return touchedWrongArea;
        }
    };

    View.OnTouchListener touchListener5 = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (rndInteger1 == 4 || rndInterger2 == 4) {
                touchedWrongArea = false;
            } else {
                touchedWrongArea = true;
            }
            return touchedWrongArea;
        }
    };

    View.OnTouchListener touchListener6 = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (rndInteger1 == 5 || rndInterger2 == 5) {
                touchedWrongArea = false;
            } else {
                touchedWrongArea = true;
            }
            return touchedWrongArea;
        }
    };

    View.OnTouchListener bubbleTouchListener1 = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            System.out.println("checking before first if, b1 touched the balloon");
            if (touchedWrongArea){
                System.out.println("checking after first if, b1 touchedWrongArea true");
                if (checkAsc = compareCount == 0){
                    System.out.println("checking after second if, b1 equal to the index");
                    compareCount++;
                    correctAns = true;
                    addScore();
                }
                else {
                    correctAns = false;
                }
            }
            else {
                System.out.println("b1 Touched the wrong area false");
                correctAns = false;
            }
            return correctAns;
        }
    };

    View.OnTouchListener bubbleTouchListener2 = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            System.out.println("checking before first if, b2 touched the balloon");
            if (touchedWrongArea){
                System.out.println("checking after first if, b2 touchedWrongArea true");
                if (checkAsc = compareCount == 1){
                    System.out.println("checking after second if, b2 equal to the index");
                    compareCount++;
                    correctAns = true;
                    addScore();
                }
                else {
                    correctAns = false;
                }
            }
            else {
                System.out.println("b2 Touched the wrong area false");
                correctAns = false;
            }
            return correctAns;
        }
    };

    View.OnTouchListener bubbleTouchListener3 = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            System.out.println("checking before first if, b3 touched the balloon");
            if (touchedWrongArea){
                System.out.println("checking after first if, b3 touchedWrongArea true");
                if (checkAsc = compareCount == 2){
                    System.out.println("checking after second if, b3 equal to the index");
                    compareCount++;
                    correctAns = true;
                    addScore();
                }
                else {
                    correctAns = false;
                }
            }
            else {
                System.out.println("b3 Touched the wrong area false");
                correctAns = false;
            }
            return correctAns;
        }
    };

    View.OnTouchListener bubbleTouchListener4 = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            System.out.println("checking before first if, b4 touched the balloon");
            if (touchedWrongArea){
                System.out.println("checking after first if, b4 touchedWrongArea true");
                if (checkAsc = compareCount == 3){
                    System.out.println("checking after second if, b4 equal to the index");
                    compareCount++;
                    correctAns = true;
                    addScore();
                }
                else {
                    correctAns = false;
                }
            }
            else {
                System.out.println("b4 Touched the wrong area false");
                correctAns = false;
            }
            return correctAns;
        }
    };

    View.OnTouchListener bubbleTouchListener5 = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            System.out.println("checking before first if, b5 touched the balloon");
            if (touchedWrongArea){
                System.out.println("checking after first if, b5 touchedWrongArea true");
                if (checkAsc = compareCount == 4){
                    System.out.println("checking after second if, b5 equal to the index");
                    compareCount++;
                    correctAns = true;
                    addScore();
                }
                else {
                    correctAns = false;
                }
            }
            else {
                System.out.println("b5 Touched the wrong area false");
                correctAns = false;
            }
            return correctAns;
        }
    };


}
