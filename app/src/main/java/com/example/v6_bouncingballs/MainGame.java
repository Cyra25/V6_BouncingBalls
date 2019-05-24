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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainGame extends AppCompatActivity implements View.OnTouchListener{
    private View grid1,grid2,grid3,grid4,grid5,grid6;
    int num =0;
    public boolean correctAns;
    private float speedX;
    private float speedY;
    int compareCount;
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
    public ArrayList<String> selectedEqns = new ArrayList<>();
    public int[] idsInFile = new int[]{R.id.bubble1, R.id.bubble2, R.id.bubble3, R.id.bubble4,
            R.id.bubble4,R.id.bubble5,R.id.bubble6};
    public View[] grids;
//    public int[] grids = new int[]{grid1, grid2, grid3, grid4, grid5};

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
//        checkArea();
//        checkAns();

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
    public void checkAns(){
//        checkArea();
        bubbleList.get(0).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (touchedWrongArea){
                    bubbleList.get(0);
                    if (checkAsc = compareCount == 0){
                        compareCount++;
                        correctAns = true;
                        addScore();
                        return correctAns = true;
                    }

                }
                return correctAns = false;
            }
        });
        bubbleList.get(1).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (touchedWrongArea){
                    bubbleList.get(1);
                    if (checkAsc = compareCount == 1){
                        compareCount++;
                        correctAns = true;
                        addScore();
                        return correctAns = true;
                    }
                }
                return correctAns = false;
            }
        });
        bubbleList.get(2).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (touchedWrongArea){
                    bubbleList.get(2);
                    if (checkAsc = compareCount == 2){
                        compareCount++;
                        correctAns = true;
                        addScore();
                        return correctAns = true;
                    }
                }
                return correctAns = false;
            }
        });
        bubbleList.get(3).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (touchedWrongArea){
                    bubbleList.get(3);
                    if (checkAsc = compareCount == 3){
                        compareCount++;
                        correctAns = true;
                        addScore();
                        return correctAns = true;
                    }
                }
                return correctAns = false;
            }
        });
        bubbleList.get(4).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (touchedWrongArea){
                    bubbleList.get(4);
                    if (checkAsc = compareCount == 4){
                        compareCount++;
                        correctAns = true;
                        addScore();
                        return correctAns = true;
                    }
                }
                return correctAns = false;
            }
        });

    }

    public void checkArea(){
        grid1.setOnTouchListener(new View.OnTouchListener() {
                                     @Override
                                     public boolean onTouch(View v, MotionEvent event) {
                                         if (rndInteger1 == 0 || rndInterger2 == 0) {
                                             touchedWrongArea = false;
                                         } else {
                                             touchedWrongArea = true;
                                         }
                                         return touchedWrongArea;
                                     }
                                 });
        grid2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (rndInteger1 == 1 || rndInterger2 == 1) {
                    touchedWrongArea = false;
                } else {
                    touchedWrongArea = true;
//                System.out.println("Grid1 is clicked" + "\ntouchedWrongArea = "+touchedWrongArea);

                }
                return touchedWrongArea;
            }
        });
        grid3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (rndInteger1 == 2 || rndInterger2 == 2) {
                    touchedWrongArea = false;
                } else {
                    touchedWrongArea = true;
//                System.out.println("Grid1 is clicked" + "\ntouchedWrongArea = "+touchedWrongArea);

                }
                return touchedWrongArea;
            }
        });
        grid4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (rndInteger1 == 3 || rndInterger2 == 3) {
                    touchedWrongArea = false;
                } else {
                    touchedWrongArea = true;
//                System.out.println("Grid1 is clicked" + "\ntouchedWrongArea = "+touchedWrongArea);

                }
                return touchedWrongArea;
            }
        });
        grid5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (rndInteger1 == 4 || rndInterger2 == 4) {
                    touchedWrongArea = false;
                } else {
                    touchedWrongArea = true;
//                System.out.println("Grid1 is clicked" + "\ntouchedWrongArea = "+touchedWrongArea);

                }
                return touchedWrongArea;
            }
        });
        grid6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (rndInteger1 == 5 || rndInterger2 == 5) {
                    touchedWrongArea = false;
                } else {
                    touchedWrongArea = true;
//                System.out.println("Grid1 is clicked" + "\ntouchedWrongArea = "+touchedWrongArea);

                }
                return touchedWrongArea;
            }
        });
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
