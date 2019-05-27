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

import static com.example.v6_bouncingballs.Position.grid1;
import static com.example.v6_bouncingballs.Position.grid2;
import static com.example.v6_bouncingballs.Position.grid3;
import static com.example.v6_bouncingballs.Position.grid4;
import static com.example.v6_bouncingballs.Position.grid5;
import static com.example.v6_bouncingballs.Position.grid6;
import static com.example.v6_bouncingballs.Position.grid7;
import static com.example.v6_bouncingballs.Position.grid8;

public class MainGame extends AppCompatActivity implements View.OnTouchListener{
    ConstraintLayout mainLayout;
    private int grid1centerX,grid1centerY;
    private Position grid1,grid2,grid3,grid4,grid5,grid6;
    public boolean correctAns, touchedRightArea, checkAsc, overlap;
    boolean continueGame = true;
    private int idTest,viewId, indexSelected,
            rndInteger1x, rndInteger1y, rndInteger2x, rndInteger2y,
            screenWidth, screenHeight, idCount, radius, diameter, randomX, randomY, toCompare, lives;
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
    public int[] randomXlist, randomYlist;
    public TextView scoreView, endScore;
    LinearLayout statusScreen;
    public ImageView crossed1, crossed2, crossed3, crossed4;
    public ImageView crossedList[];
    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private Equations equationsManager = new Equations();
    public SharedPreferences difficulty;

    Random rndGrid = new Random();

    //AM kali
    private float touchX, touchY;
    private int addScoreCount = 0;


    //no touching zones
    Position crossed1Position;
    Position crossed2Position;
    @SuppressLint("ClickableViewAccessibility")
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
        final int screenWidth = size.x;
        final int screenHeight = size.y - 80;


        gridCentersX =new int[]{0, screenWidth/4, screenWidth/2, 3* screenWidth/4};
        gridCentersY = new int[]{0, screenHeight/2};
        crossed1 = (ImageView) findViewById(R.id.crossed1);
        crossed2 = (ImageView) findViewById(R.id.crossed2);
        crossedList = new ImageView[]{crossed1, crossed2, crossed3, crossed4};
        //get grid center

        rndInteger1x = rndGrid.nextInt(gridCentersX.length);
        rndInteger1y = rndGrid.nextInt(gridCentersY.length);
        while (rndInteger2x == rndInteger1x && rndInteger1y == rndInteger2y) {
            rndInteger2x = rndGrid.nextInt(gridCentersX.length);
            rndInteger2y = rndGrid.nextInt(gridCentersY.length);
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
        crossed1Position = crossedArea(crossed2);

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

        mainLayout.setOnTouchListener(this);

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
            randomX = random.nextInt(1000 + 1);
            randomY = random.nextInt(800 + 1);

            randomXlist[i] = randomX;
            randomYlist[i] = randomY;
            while (overlap && toCompare >=0 && i>=1){
                toCompare--;
                overlap = checkOverlap(randomXlist[i], randomXlist[toCompare], randomYlist[i], randomYlist[toCompare]);
            }
            randomXlist[i] = randomX;
            randomYlist[i] = randomY;
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
            return grid7;
        }else{
            return Position.grid8;
        }
//        (crossed.getX() == gridCentersX[3] && crossed.getY() == gridCentersY[1])
    }

    public boolean checkOverlap(float x1, float y1, float x2, float y2) {
        // If you use this, make sure the x and y values are the center of the circles.
        distanceX = x1 - x2;
        distanceY = y1 - y2;
        distance = distanceX*distanceX + distanceY*distanceY;
        if (diameter*diameter <= distance){
            overlap = true;
        }else {
            overlap = false;
        }
        return overlap;
    }

    private PointF getCenter(){
        return new PointF(screenWidth/2f, screenHeight/2f);
    }

    public Position getPosition(float x, float y){
        PointF center = getCenter();

        if(y < center.y){
            if(x < (center.x/2))
                return grid1;

            else if (x > (center.x/2) && x < center.x)
                return grid2;

            else if (x > center.x && x < (center.x + (center.x/2)))
                return grid3;

            else if (x > (center.x + (center.x/2)))
                return grid4;
        }

        else if (y > center.y){
            if(x < (center.x/2))
                return grid5;

            else if (x > (center.x/2) && x < center.x)
                return grid6;

            else if (x > center.x && x < (center.x + (center.x/2)))
                return grid7;

            else if (x > (center.x + (center.x/2)))
                return Position.grid8;
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

                score++;
            } else if (!touchedRightArea && correctAns) {
                System.out.println("Wrong area, You lose!");
                gameOver();
                System.out.println("You lose");
            } else if (!correctAns && touchedRightArea) {
                System.out.println("Wrong answer, You lose!");
                gameOver();
                System.out.println("You lose");
            }


            scoreView.setText("Score: "+ score);
            addScoreCount = 0;
        }
    }

    public void gameOver(){
        if (lives >0){
            lives--;
            livesView.setText("Lives: "+ lives);
        }else {
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

        Position position = getPosition(event.getX(), event.getY());

        switch(position){
            case grid1:
                System.out.println(crossed1Position + " " + crossed2Position);
                if (crossed1Position == grid1 || crossed2Position == grid1) {
                    touchedRightArea = false;
                } else {
                    touchedRightArea = true;
                }
                System.out.println("Printing grid1 " + touchedRightArea);
                System.out.println("\n");
                System.out.print(grid1);
                break;

            case grid2:
                System.out.println(crossed1Position + " " + crossed2Position);
                if (crossed1Position == grid2 || crossed2Position == grid2) {
                    touchedRightArea = false;
                } else {
                    touchedRightArea = true;
                }
                System.out.println("Printing grid2 " + touchedRightArea);
                System.out.println("\n");
                System.out.print(grid2);
                break;

            case grid3:
                System.out.println(crossed1Position + " " + crossed2Position);
                if (crossed1Position == grid3 || crossed2Position == grid3) {
                    touchedRightArea = false;
                } else {
                    touchedRightArea = true;
                }
                System.out.println("Printing grid3 " + touchedRightArea);
                System.out.println("\n");
                System.out.print(grid3);
                break;

            case grid4:
                System.out.println(crossed1Position + " " + crossed2Position);
                if (crossed1Position == grid4 || crossed2Position == grid4) {
                    touchedRightArea = false;
                } else {
                    touchedRightArea = true;
                }
                System.out.println("Printing grid4 " + touchedRightArea);
                System.out.println("\n");
                System.out.print(grid4);
                break;

            case grid5:
                System.out.println(crossed1Position + " " + crossed2Position);
                if (crossed1Position == grid5 || crossed2Position == grid5) {
                    touchedRightArea = false;
                } else {
                    touchedRightArea = true;
                }
                System.out.println("Printing grid5 " + touchedRightArea);
                System.out.println("\n");
                System.out.print(grid5);
                break;

            case grid6:
                System.out.println(crossed1Position + " " + crossed2Position);
                if (crossed1Position == grid6 || crossed2Position == grid6) {
                    touchedRightArea = false;
                } else {
                    touchedRightArea = true;
                }
                System.out.println("Printing grid6 " + touchedRightArea);
                System.out.println("\n");
                System.out.print(grid6);
                break;

            case grid7:
                System.out.println(crossed1Position + " " + crossed2Position);
                if (crossed1Position == grid7 || crossed2Position == grid7) {
                    touchedRightArea = false;
                } else {
                    touchedRightArea = true;
                }
                System.out.println("Printing grid7 " + touchedRightArea);
                System.out.println("\n");
                System.out.print(grid7);
                break;

            case grid8:
                System.out.println(crossed1Position + " " + crossed2Position);
                if (crossed1Position == grid8 || crossed2Position == grid8) {
                    touchedRightArea = false;
                } else {
                    touchedRightArea = true;
                }
                System.out.println("Printing grid8 " + touchedRightArea);
                System.out.println("\n");
                System.out.print("this is grid 8 "+Position.grid8);
                break;

            case MIDDLE:
                break;
        }

        switch (v.getId()){
            case R.id.bubble1:
                if (checkAsc = compareCount == 0){
                    System.out.println("B1 - correct answer");
                    System.out.println("\n");
                    correctAns = true;
                }
                else {
                    correctAns = false;
                }
                bubbleList.get(compareCount).setVisibility(View.INVISIBLE);
                compareCount++;
                break;
            case R.id.bubble2:
                if (checkAsc = compareCount == 1){
                    System.out.println("B2 - correct answer");
                    System.out.println("\n");
                    correctAns = true;
                }
                else {
                    correctAns = false;
                }
                bubbleList.get(compareCount).setVisibility(View.INVISIBLE);
                compareCount++;
                break;
            case R.id.bubble3:
                if (checkAsc = compareCount == 2){
                    System.out.println("B3 - correct answer");
                    System.out.println("\n");
                    correctAns = true;
                }
                else {
                    correctAns = false;
                }
                bubbleList.get(compareCount).setVisibility(View.INVISIBLE);
                compareCount++;
                break;
            case R.id.bubble4:
                if (checkAsc = compareCount == 3){
                    System.out.println("B4 - correct answer");
                    System.out.println("\n");
                    correctAns = true;
                }
                else {
                    correctAns = false;
                }
                bubbleList.get(compareCount).setVisibility(View.INVISIBLE);
                compareCount++;
                break;
            case R.id.bubble5:
                if (checkAsc = compareCount == 4){
                    System.out.println("B5 - correct answer");
                    System.out.println("\n");
                    bubbleList.get(4).setVisibility(View.INVISIBLE);
                    correctAns = true;
                }
                else {
                    correctAns = false;
                }
                bubbleList.get(compareCount).setVisibility(View.INVISIBLE);
                compareCount++;
                break;
            case R.id.bubble6:
                if (checkAsc = compareCount == 5){
                    System.out.println("B6 - correct answer");
                    System.out.println("\n");
                    correctAns = true;
                }
                else {
                    correctAns = false;
                }
                bubbleList.get(compareCount).setVisibility(View.INVISIBLE);
                compareCount++;
                break;
            case R.id.bubble7:
                if (checkAsc = compareCount == 6){
                    System.out.println("B7 - correct answer");
                    System.out.println("\n");
                    correctAns = true;
                }
                else {
                    correctAns = false;
                }
                bubbleList.get(compareCount).setVisibility(View.INVISIBLE);
                compareCount++;
                win();
                break;
        }

        addScore(v);

        return false;
    }

    public void win(){
        endScore.setText("Score: "+score);
        winOrLose.setText(getString(R.string.winStatus));
        statusScreen.setVisibility(View.VISIBLE);
    }

    public void toMenu(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
