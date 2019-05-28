package com.example.v6_bouncingballs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainGame extends AppCompatActivity implements View.OnTouchListener{
    ConstraintLayout mainLayout;
    private int grid1centerX,grid1centerY;
    private Position grid1,grid2,grid3,grid4,grid5,grid6;
    public boolean correctAns, touchedRightArea, checkAsc, overlap;
    boolean continueGame = true;
    public int screenWidth, screenHeight;
    private int rndInteger1x, rndInteger1y, rndInteger2x, rndInteger2y,
             idCount, radius, diameter, randomX, randomY, toCompare, lives,
            currentBubbleId,bubbleCenterX, bubbleCenterY;
    private float distanceX, distanceY, distance;
    int compareCount = 0;
    int num =0;
    int score = 0;
    TextView livesView;
    float[] speeds;
    public float[] speedsX = new float[7];
    public float[] speedsY= new float[7];
    TextView winOrLose;
    private String[][] equations;
    public Button backToMain;
    public ArrayList<String> selectedEqns;
    public int[] idsInFile;
    public int[] gridCentersX, gridCentersY;
    public Position[] grids;
    public ArrayList<TextView> bubbleList = new ArrayList<>();
    public int[] randomXlist, randomYlist,bubbleCenterXs, bubbleCenterYs;
    public TextView scoreView, endScore;
    LinearLayout statusScreen;
    public ImageView crossed1, crossed2, crossed3, crossed4;
    public ImageView crossedList[];
    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private Equations equationsManager = new Equations();
    public SharedPreferences difficulty;

    Random rndGrid = new Random();
    private Position bubblePosition;

    //no touching zones
    Position crossed1Position;
    Position crossed2Position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_game);

        radius = 100;
        diameter = radius * 2;
        lives = 3;
        //get the size of the screen and set the screenWidth and screenHeight
        WindowManager windowManager = getWindowManager();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y - 80;


        gridCentersX =new int[]{0, screenWidth/4, screenWidth/2, 3* screenWidth/4};
        gridCentersY = new int[]{0, screenHeight/2};
        crossed1 = (ImageView) findViewById(R.id.crossed1);
        crossed2 = (ImageView) findViewById(R.id.crossed2);
        crossedList = new ImageView[]{crossed1, crossed2, crossed3, crossed4};
        //get grid center

        rndInteger1x = rndGrid.nextInt(4);
        rndInteger1y = rndGrid.nextInt(2);
        rndInteger2x = rndGrid.nextInt(4);
        rndInteger2y = rndGrid.nextInt(2);
        while (rndInteger2x == rndInteger1x && rndInteger1y == rndInteger2y) {
            rndInteger2x = rndGrid.nextInt(4);
            rndInteger2y = rndGrid.nextInt(2);
        }

        crossed1.setX(gridCentersX[rndInteger1x]);
        crossed1.setY(gridCentersY[rndInteger1y]);
        crossed1.getLayoutParams().height = screenHeight/2;
        crossed1.getLayoutParams().width = screenWidth/4;
        crossed1.setVisibility(View.VISIBLE);

        crossed1Position = crossedArea(crossed1);

        crossed2.setX(gridCentersX[rndInteger2x]);
        crossed2.setY(gridCentersY[rndInteger2y]);
        crossed2.getLayoutParams().height = screenHeight/2;
        crossed2.getLayoutParams().width = screenWidth/4;
        crossed2.setVisibility(View.VISIBLE);
        crossed2Position = crossedArea(crossed2);

        //get the equations
        difficulty = getSharedPreferences("difficulty", Context.MODE_PRIVATE);
        String difficultyChose = difficulty.getString("difficulty", "");
        if (difficultyChose.equals("easy")){
            equations = equationsManager.getNumbersGrp();
        }
        else if (difficultyChose.equals("hard")){
            equations = equationsManager.getEquations();
        }



        //get IDs
        mainLayout = (ConstraintLayout) findViewById(R.id.main_layout);
        scoreView = (TextView) findViewById(R.id.scoreTextView);
        statusScreen = (LinearLayout) findViewById(R.id.statusScreen);
        endScore = (TextView) findViewById(R.id.scoreOver);
        livesView = (TextView) findViewById(R.id.lives) ;
        winOrLose = (TextView) findViewById(R.id.winOrLose);
        backToMain = (Button) findViewById(R.id.backToMain);

        mainLayout.setBackground(getDrawable(R.drawable.black_background));
        speeds = new float[]{5.0f, -5.0f};
        grids = new Position[]{grid1, grid2, grid3, grid4, grid5, grid6};
        idsInFile = new int[]{2131165219, 2131165220, 2131165221, 2131165222,
                2131165223,2131165224,2131165225};
        selectedEqns = new ArrayList<>();

        //set which grids cannot be touched and make them disappear after some time
//
//        mainLayout.setOnTouchListener(this);

        crossed1.postDelayed(new Runnable() {
            @Override
            public void run() {
                crossed1.setVisibility(View.INVISIBLE);
            }
        },2000);
        crossed2.postDelayed(new Runnable() {
            @Override
            public void run() {
                crossed2.setVisibility(View.INVISIBLE);
            }
        },2000);

        //creating the bubble
        idCount =0;
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
            mainLayout.addView(bubble);

            //set the parameter(Width and height) for the bubble
            bubble.getLayoutParams().height = 200;
            bubble.getLayoutParams().width = 200;

            //set background for the bubble
            bubble.setBackground(getDrawable(R.drawable.soapbubble_blue));

            //set X and Y for the bubble
            randomXlist =new int[7];
            randomYlist = new int[7];
            bubbleCenterXs = new int[7];
            bubbleCenterYs = new int[7];
            randomX = random.nextInt(1800 + 1);
            randomY = random.nextInt(800 + 1);

            randomXlist[i] = randomX;
            randomYlist[i] = randomY;
            bubbleCenterX = randomX + 100;
            bubbleCenterY = randomY + 100;
            bubbleCenterXs[i] = bubbleCenterX;
            bubbleCenterYs[i] = bubbleCenterY;
//            toCompare = i-1;
//            while (toCompare >=0 && i>=1){
//                System.out.println("This is in the while loop");
////                randomXlist[i] = randomX;
////                randomYlist[i] = randomY;
////                bubbleCenterX = randomX + 100;
////                bubbleCenterY = randomY + 100;
////                bubbleCenterXs[i] = bubbleCenterX;
////                bubbleCenterYs[i] = bubbleCenterY;
//                System.out.println(toCompare + ", "+i);
//                System.out.println(overlap);
//                System.out.println(randomXlist[i]+", "+randomYlist[i]+", "+
//                        bubbleCenterXs[i]+", "+bubbleCenterYs[i]);
//                System.out.println(randomXlist[toCompare]+", "+randomYlist[toCompare]+", "+
//                        bubbleCenterXs[toCompare]+", "+bubbleCenterYs[toCompare]);
//                overlap = checkOverlap(bubbleCenterXs[toCompare],bubbleCenterXs[i],
//                        bubbleCenterYs[toCompare], bubbleCenterYs[i]);
//                if (overlap) {
//                    randomX = random.nextInt(1000 + 1);
//                    randomY = random.nextInt(800 + 1);
//                    randomXlist[i] = randomX;
//                    randomYlist[i] = randomY;
//                    bubbleCenterX = randomX + 100;
//                    bubbleCenterY = randomY + 100;
//                    bubbleCenterXs[i] = bubbleCenterX;
//                    bubbleCenterYs[i] = bubbleCenterY;
//                }else {
//                    toCompare--;
//                }System.out.println("tocompare" +toCompare);
//            }
//            randomXlist[i] = randomX;
//            randomYlist[i] = randomY;
//            bubbleCenterX = randomX + 100;
//            bubbleCenterY = randomY + 100;
//            bubbleCenterXs[i] = bubbleCenterX;
//            bubbleCenterYs[i] = bubbleCenterY;
            bubble.setX(randomX);
            bubble.setY(randomY);
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
        this.setContentView(mainLayout);


    }

    public Position crossedArea(ImageView crossed){
        if (crossed.getX() == gridCentersX[0] && crossed.getY() == gridCentersY[0]){
            return Position.grid1;
        }else if (crossed.getX() == gridCentersX[1] && crossed.getY() == gridCentersY[0]){
            return Position.grid2;
        }else if (crossed.getX() == gridCentersX[2] && crossed.getY() == gridCentersY[0]){
            return Position.grid3;
        }else if (crossed.getX() == gridCentersX[3] && crossed.getY() == gridCentersY[0]){
            return Position.grid4;
        }else if (crossed.getX() == gridCentersX[0] && crossed.getY() == gridCentersY[1]){
            return Position.grid5;
        }else if (crossed.getX() == gridCentersX[1] && crossed.getY() == gridCentersY[1]){
            return Position.grid6;
        }else if (crossed.getX() == gridCentersX[2] && crossed.getY() == gridCentersY[1]){
            return Position.grid7;
        }else if (crossed.getX() == gridCentersX[3] && crossed.getY() == gridCentersY[1]){
            return Position.grid8;
        }
        return Position.MIDDLE;
    }

//    public boolean checkOverlap(float x1, float y1, float x2, float y2) {
//        System.out.println("This is overlap method");
//        distanceX = x1 - x2;
//        distanceY = y1 - y2;
//        distance = distanceX*distanceX + distanceY*distanceY;
//        System.out.println("This is distance "+distance);
//        System.out.println("This is radius sqr "+radius*radius);
//        System.out.println(radius*radius > distance);
//        if (radius*radius > distance){
//            overlap = true;
//        }else {
//            overlap = false;
//        }
//        System.out.println("In the overlap method, overlap = "+overlap);
//        return overlap;
//    }

    private PointF getCenter(){
        return new PointF(mainLayout.getWidth()/2f, mainLayout.getHeight()/2f);
    }

    public Position getPosition(float x, float y){
        PointF center = getCenter();

        if(y < center.y){
            if(x < (center.x/2))
                return Position.grid1;

            else if (x > (center.x/2) && x < center.x)
                return Position.grid2;

            else if (x > center.x && x < (center.x + (center.x/2)))
                return Position.grid3;

            else if (x > (center.x + (center.x/2)))
                return Position.grid4;
        }

        else if (y > center.y){
            if(x < (center.x/2))
                return Position.grid5;

            else if (x > (center.x/2) && x < center.x)
                return Position.grid6;

            else if (x > center.x && x < (center.x + (center.x/2)))
                return Position.grid7;

            else if (x > (center.x + (center.x/2)))
                return Position.grid8;
        }

        return Position.MIDDLE;
    }

//    public void makeItDisappear(View v){
//        switch (v.getId()){
//            case R.id.bubble1:
//                bubbleList.get(0).setVisibility(View.INVISIBLE);
//                break;
//            case R.id.bubble2:
//                bubbleList.get(1).setVisibility(View.INVISIBLE);
//                break;
//            case R.id.bubble3:
//                bubbleList.get(2).setVisibility(View.INVISIBLE);
//                break;
//            case R.id.bubble4:
//                bubbleList.get(3).setVisibility(View.INVISIBLE);
//                break;
//            case R.id.bubble5:
//                bubbleList.get(4).setVisibility(View.INVISIBLE);
//                break;
//            case R.id.bubble6:
//                bubbleList.get(5).setVisibility(View.INVISIBLE);
//                break;
//            case R.id.bubble7:
//                bubbleList.get(6).setVisibility(View.INVISIBLE);
//                break;
//        }
//    }

    public void addScore(View v){
            if (correctAns) {
                if((bubblePosition != crossed1Position) && (bubblePosition != crossed2Position)) {
                    score++;
                }else if (bubblePosition == crossed1Position || bubblePosition == crossed2Position){
                    lives--;
                    livesView.setText(R.string.lives+ lives);
                }
            }

            else{
                lives--;
                livesView.setText(R.string.lives);
            }
            scoreView.setText(R.string.scores+ score);
    }

    public void gameOver(){
        if(lives == 0){
            winOrLose.setText(getString(R.string.loseStatus));
            endScore.setText("Score: "+score);
            statusScreen.setVisibility(View.VISIBLE);
        }
    }

    public void changePosition(int screenWidth, int screenHeight){
        num = 0;
        while (num<7) {
            if (bubbleList.get(num).getX()+2* radius > screenWidth) {
                speedsX[num] = speedsX[num] * -1;
            } else if (bubbleList.get(num).getX() < 0) {
                speedsX[num] = speedsX[num] * -1;
            }
            if (bubbleList.get(num).getY()+2* radius > screenHeight) {
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

        if (event.getAction() != MotionEvent.ACTION_DOWN){
            return super.onTouchEvent(event);
        }

        bubblePosition = getPosition(v.getX(), v.getY());
        currentBubbleId = v.getId();
        if (currentBubbleId == bubbleList.get(0).getId()) {
            if (compareCount == 0) {
                correctAns = true;
            } else {
                correctAns = false;
            }
            bubbleList.get(compareCount).setVisibility(View.INVISIBLE);
            compareCount++;

        } else if (currentBubbleId == bubbleList.get(1).getId()) {
            if (compareCount == 1) {
                correctAns = true;
            } else {
                correctAns = false;
            }
            bubbleList.get(compareCount).setVisibility(View.INVISIBLE);
            compareCount++;

        } else if (currentBubbleId == bubbleList.get(2).getId()) {
            if (compareCount == 2) {
                correctAns = true;
            } else {
                correctAns = false;
            }
            bubbleList.get(compareCount).setVisibility(View.INVISIBLE);
            compareCount++;

        } else if (currentBubbleId == bubbleList.get(3).getId()) {
            if (compareCount == 3) {
                correctAns = true;
            } else {
                correctAns = false;
            }
            bubbleList.get(compareCount).setVisibility(View.INVISIBLE);
            compareCount++;

        } else if (currentBubbleId == bubbleList.get(4).getId()) {
            if (compareCount == 4) {
                correctAns = true;
            } else {
                correctAns = false;
            }
            bubbleList.get(compareCount).setVisibility(View.INVISIBLE);
            compareCount++;

        } else if (currentBubbleId == bubbleList.get(5).getId()) {
            if (compareCount == 5) {
                correctAns = true;
            } else {
                correctAns = false;
            }
            bubbleList.get(compareCount).setVisibility(View.INVISIBLE);
            compareCount++;

        } else if (currentBubbleId == bubbleList.get(6).getId()) {
            if (compareCount == 6) {
                correctAns = true;
            } else {
                correctAns = false;
            }
            bubbleList.get(compareCount).setVisibility(View.INVISIBLE);
            compareCount++;

        }
        addScore(v);
        win();

        return false;
    }

    public void win() {
        if (compareCount ==7){
            endScore.setText(R.string.scores + score);
            winOrLose.setText(getString(R.string.winStatus));
            statusScreen.setVisibility(View.VISIBLE);

        }
    }

    public void toMenu(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
