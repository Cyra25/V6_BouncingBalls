package com.example.v6_bouncingballs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainGame extends AppCompatActivity implements View.OnTouchListener{
    ConstraintLayout mainLayout;
    private TextView grid1,grid2,grid3,grid4,grid5,grid6;
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
    public boolean touchedRightArea;
    public boolean checkAsc;
    public float[] speedsX = new float[7];
    public float[] speedsY= new float[7];
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
    public TextView[] grids;
    public SharedPreferences difficulty;

    Random rndGrid = new Random();

    //AM kali
    private float touchX, touchY;
    private int addScoreCount = 0;

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
        difficulty = getSharedPreferences("difficulty", Context.MODE_PRIVATE);
        String difficultyChose = difficulty.getString("difficulty", "");
        System.out.println(difficultyChose);
        if (difficultyChose.equals("easy")){
            equations = equationsManager.getNumbersGrp();
        }
        else if (difficultyChose.equals("hard")){
            equations = equationsManager.getEquations();
        }



        //get IDs
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.main_layout);
//        Button stop = (Button) findViewById(R.id.stop);
        grid1= (TextView)findViewById(R.id.grid1);
        grid2= (TextView)findViewById(R.id.grid2);
        grid3= (TextView)findViewById(R.id.grid3);
        grid4= (TextView)findViewById(R.id.grid4);
        grid5= (TextView)findViewById(R.id.grid5);
        grid6= (TextView)findViewById(R.id.grid6);
        mainLayout = (ConstraintLayout) findViewById(R.id.main_layout);
        scoreView = (TextView) findViewById(R.id.scoreTextView);

        speeds = new float[]{5.0f, -5.0f};
        grids = new TextView[]{grid1, grid2, grid3, grid4, grid5, grid6};
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


//        grid1.setOnTouchListener(touchListener1);
//        grid2.setOnTouchListener(touchListener2);
//        grid3.setOnTouchListener(touchListener3);
//        grid4.setOnTouchListener(touchListener4);
//        grid5.setOnTouchListener(touchListener5);
//        grid6.setOnTouchListener(touchListener6);
//
        grid1.setOnTouchListener(this);
        grid2.setOnTouchListener(this);
        grid3.setOnTouchListener(this);
        grid4.setOnTouchListener(this);
        grid5.setOnTouchListener(this);
        grid6.setOnTouchListener(this);

        //creating the bubble
        for (int i = 0; i < 7; i++) {
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

        bubbleList.get(0).setOnTouchListener(this);
        bubbleList.get(1).setOnTouchListener(this);
        bubbleList.get(2).setOnTouchListener(this);
        bubbleList.get(3).setOnTouchListener(this);
        bubbleList.get(4).setOnTouchListener(this);
        bubbleList.get(5).setOnTouchListener(this);
        bubbleList.get(6).setOnTouchListener(this);

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


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() != MotionEvent.ACTION_DOWN){
            return super.onTouchEvent(event);
        }

        Position position = getPosition(event.getX(), event.getY());

        switch(position){
            case TOP_LEFT1:
                break;

            case TOP_LEFT2:
                break;

            case TOP_RIGHT1:
                break;

            case TOP_RIGHT2:
                break;

            case BOTTOM_LEFT1:
                break;

            case BOTTOM_LEFT2:
                break;

            case BOTTOM_RIGHT1:
                break;

            case BOTTOM_RIGHT2:
                break;

            case MIDDLE:
                break;

        }

        return super.onTouchEvent(event);

    }

    private PointF getCenter(){
        return new PointF(mainLayout.getWidth()/2f, mainLayout.getHeight()/2f);
    }

    public Position getPosition(float x, float y){
        PointF center = getCenter();

        if(y < center.y){
            if(x < (center.x/2))
                return Position.TOP_LEFT1;

            else if (x > (center.x/2) && x < center.x)
                return Position.TOP_LEFT2;

            else if (x > center.x && x < (center.x + (center.x/2)))
                return Position.TOP_RIGHT1;

            else if (x > (center.x + (center.x/2)))
                return Position.TOP_RIGHT2;
        }

        else if (y > center.y){
            if(x < (center.x/2))
                return Position.BOTTOM_LEFT1;

            else if (x > (center.x/2) && x < center.x)
                return Position.BOTTOM_LEFT2;

            else if (x > center.x && x < (center.x + (center.x/2)))
                return Position.BOTTOM_RIGHT1;

            else if (x > (center.x + (center.x/2)))
                return Position.BOTTOM_RIGHT2;
        }

        return Position.MIDDLE;
    }

    public void makeItDisappear(View v){
        switch (v.getId()){
            case R.id.bubble1:
                bubbleList.get(0).setVisibility(View.INVISIBLE);
                break;
            case R.id.bubble2:
                bubbleList.get(1).setVisibility(View.INVISIBLE);
                break;
            case R.id.bubble3:
                bubbleList.get(2).setVisibility(View.INVISIBLE);
                break;
            case R.id.bubble4:
                bubbleList.get(3).setVisibility(View.INVISIBLE);
                break;
            case R.id.bubble5:
                bubbleList.get(4).setVisibility(View.INVISIBLE);
                break;
            case R.id.bubble6:
                bubbleList.get(5).setVisibility(View.INVISIBLE);
                break;
            case R.id.bubble7:
                bubbleList.get(6).setVisibility(View.INVISIBLE);
                break;
        }
    }

    public void addScore(View v){
        ++addScoreCount;
        if (addScoreCount == 2) {
            if (correctAns && touchedRightArea) {
                makeItDisappear(v);
                System.out.println(grid1.getVisibility() + "\n" +
                        grid2.getVisibility() + "\n" +
                        grid3.getVisibility() + "\n" +
                        grid4.getVisibility() + "\n" +
                        grid5.getVisibility() + "\n" +
                        grid6.getVisibility() + "\n");

                score++;
            } else if (touchedRightArea == false && correctAns == true) {
                System.out.println("Wrong area, You lose!");
            } else if (correctAns == false && touchedRightArea == true) {
                System.out.println("Wrong answer, You lose!");
            }


            scoreView.setText("Score: "+ score);
            addScoreCount = 0;
        }
        else {
            System.out.println("You lose");
        }

    }

    public void gameOver(View v){
//        v.setVisibility(View.INVISIBLE);
//        System.out.println("You lose");
//        if (touchedRightArea == false && correctAns == true){
//            System.out.println("Wrong area, You lose!");
//        }else if (correctAns == false && touchedRightArea == true){
//            System.out.println("Wrong answer, You lose!");
//        }
    }

    public void changePosition(int screenWidth, int screenHeight){
        num = 0;
        while (num<7) {
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        TextView view = (TextView) v;
        switch (view.getId()){
            case R.id.grid1:
                if (rndInteger1 == 0 || rndInterger2 == 0) {
                    touchedRightArea = false;
                } else {
                    touchedRightArea = true;
                }
                System.out.println("Printing grid1 " + touchedRightArea);
                System.out.println("\n");
                break;
            case R.id.grid2:
                if (rndInteger1 == 1 || rndInterger2 == 1) {
                    touchedRightArea = false;
                } else {
                    touchedRightArea = true;
                }
                System.out.println("Printing grid2 " + touchedRightArea);
                System.out.println("\n");
                break;
            case R.id.grid3:
                if (rndInteger1 == 2 || rndInterger2 == 2) {
                    touchedRightArea = false;
                } else {
                    touchedRightArea = true;
                }
                System.out.println("Printing grid3 " + touchedRightArea);
                System.out.println("\n");
                break;
            case R.id.grid4:
                if (rndInteger1 == 3 || rndInterger2 == 3) {
                    touchedRightArea = false;
                } else {
                    touchedRightArea = true;
                }
                System.out.println("Printing grid4 " + touchedRightArea);
                System.out.println("\n");
                break;
            case R.id.grid5:
                if (rndInteger1 == 4 || rndInterger2 == 4) {
                    touchedRightArea = false;
                } else {
                    touchedRightArea = true;
                }
                System.out.println("Printing grid5 " + touchedRightArea);
                System.out.println("\n");
                break;
            case R.id.grid6:
                if (rndInteger1 == 5 || rndInterger2 == 5) {
                    touchedRightArea = false;
                } else {
                    touchedRightArea = true;
                }
                System.out.println("Printing grid6 " + touchedRightArea);
                System.out.println("\n");
                break;
        }
        switch (view.getId()){
            case R.id.bubble1:
                if (checkAsc = compareCount == 0){
                    System.out.println("B1 - correct answer");
                    System.out.println("\n");
                    compareCount++;
                    bubbleList.get(0).setVisibility(View.INVISIBLE);
                    correctAns = true;
                }
                else {
                    correctAns = false;
                }break;
            case R.id.bubble2:
                if (checkAsc = compareCount == 1){
                    System.out.println("B2 - correct answer");
                    System.out.println("\n");
                    compareCount++;
                    correctAns = true;
                }
                else {
                    correctAns = false;
                }break;
            case R.id.bubble3:
                if (checkAsc = compareCount == 2){
                    System.out.println("B3 - correct answer");
                    System.out.println("\n");
                    compareCount++;
                    correctAns = true;
                }
                else {
                    correctAns = false;
                }break;
            case R.id.bubble4:
                if (checkAsc = compareCount == 3){
                    System.out.println("B4 - correct answer");
                    System.out.println("\n");
                    compareCount++;
                    correctAns = true;
                }
                else {
                    correctAns = false;
                }break;
            case R.id.bubble5:
                if (checkAsc = compareCount == 4){
                    System.out.println("B5 - correct answer");
                    System.out.println("\n");
                    compareCount++;
                    bubbleList.get(4).setVisibility(View.INVISIBLE);
                    correctAns = true;
                }
                else {
                    correctAns = false;
                }break;
            case R.id.bubble6:
                if (checkAsc = compareCount == 5){
                    System.out.println("B6 - correct answer");
                    System.out.println("\n");
                    compareCount++;
                    correctAns = true;
                }
                else {
                    correctAns = false;
                }
                break;
            case R.id.bubble7:
                if (checkAsc = compareCount == 6){
                    System.out.println("B7 - correct answer");
                    System.out.println("\n");
                    compareCount++;
                    correctAns = true;
                }
                else {
                    correctAns = false;
                }
                break;

        }

        addScore(v);
//        gameOver();
        return false;
    }


}
