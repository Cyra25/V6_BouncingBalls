package com.example.v6_bouncingballs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
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
            currentBubbleId,bubbleCenterX, bubbleCenterY, background, bubbleBackground, livesNumber;
    private float distanceX, distanceY, distance;
    int compareCount = 0;
    int num =0;
    int score = 0;
    TextView livesView;
    float[] speeds;
    public float[] speedsX = new float[7];
    public float[] speedsY= new float[7];
    private TextView winOrLose;
    private String[][] equations;
    private ArrayList<TextView> visibleBubbles = new ArrayList<>();
    public Button backToMain;
    public ArrayList<String> selectedEqns;
    public int[] idsInFile;
    public int[] gridCentersX, gridCentersY;
    public Position[] grids;
    public ArrayList<TextView> bubbleList = new ArrayList<>();
    public int[] randomXlist, randomYlist,bubbleCenterXs, bubbleCenterYs;
    public TextView scoreView, endScore;
    LinearLayout statusScreen;
    private SharedPreferences preferences;
    public ImageView crossed1, crossed2, crossed3, crossed4;
    public ImageView crossedList[];
    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private Equations equationsManager = new Equations();
    public SharedPreferences difficulty;
    public LinearLayout pausedScreen;
    // audios
    private SoundManager soundManager;
    private int correctAudio, loseAudio, winAudio, wrongAudio, warningAudio;
    Random rndGrid = new Random();
    private Position bubblePosition;
    public String difficultyChose;

    // save score for adding to High Scores
    private ArrayList<Integer> highScoresEasy = new ArrayList<Integer>();
    private ArrayList<Integer> highScoresHard = new ArrayList<Integer>();

    // for pause play
    private Button pausePlay;
    private Boolean paused = false;

    //no touching zones
    Position crossed1Position;
    Position crossed2Position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_game);

        radius = 100;
        diameter = radius * 2;
        //get the size of the screen and set the screenWidth and screenHeight
        WindowManager windowManager = getWindowManager();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y - 80;
        highScoresEasy = FileHelper.readDataEasy(this);
        highScoresHard = FileHelper.readDataEasy(this);
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

        preferences = getSharedPreferences("value", MODE_PRIVATE);
        difficulty = getSharedPreferences("difficulty", Context.MODE_PRIVATE);
        difficultyChose = difficulty.getString("difficulty", "");
        if (difficultyChose.equals("easy")){
            equations = equationsManager.getNumbersGrp();
        }
        else if (difficultyChose.equals("hard")){
            equations = equationsManager.getEquations();
        }
        //get IDs
        pausedScreen = (LinearLayout) findViewById(R.id.pausedScreen);
        pausePlay = (Button) findViewById(R.id.pausedPlay);
        mainLayout = (ConstraintLayout) findViewById(R.id.main_layout);
        scoreView = (TextView) findViewById(R.id.scoreTextView);
        statusScreen = (LinearLayout) findViewById(R.id.statusScreen);
        endScore = (TextView) findViewById(R.id.scoreOver);
        livesView = (TextView) findViewById(R.id.lives) ;
        winOrLose = (TextView) findViewById(R.id.winOrLose);
        backToMain = (Button) findViewById(R.id.backToMain);
        // handles audio
        soundManager = new SoundManager(this);

        correctAudio = soundManager.addSound(R.raw.bubblepop_sound);
        wrongAudio = soundManager.addSound(R.raw.wrong_sound);
        winAudio = soundManager.addSound(R.raw.win_sound);
        loseAudio = soundManager.addSound(R.raw.lose_sound);
        warningAudio = soundManager.addSound(R.raw.warning);
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
            bubble.setX(randomX);
            bubble.setY(randomY);
            //set Speed
            speedsX[i] = speeds[random.nextInt(2)];
            speedsY[i] = speeds[random.nextInt(2)];
            bubbleList.add(bubble);
            visibleBubbles.add(bubble);
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


    // handles backgrounds and bubbles
    @Override
    protected void onStart() {
        super.onStart();

        //change background
        background = preferences.getInt("background number", 0);
        if (background == 0) {
            mainLayout.setBackgroundResource(R.drawable.blue_background);
        } else if (background == 1) {
            mainLayout.setBackgroundResource(R.drawable.black_background);
        } else if (background == 2) {
            mainLayout.setBackgroundResource(R.drawable.yellow_background);
        } else if (background == 3) {
            mainLayout.setBackgroundResource(R.drawable.orange_background);
        } else if (background == 4) {
            mainLayout.setBackgroundResource(R.drawable.red_background);
        } else if (background == 5) {
            mainLayout.setBackgroundResource(R.drawable.pink_background);
        }

        //change dogs;
        bubbleBackground = preferences.getInt("dog number", 0);
        if (bubbleBackground == 0) {
            bubbleList.get(0).setBackgroundResource(R.drawable.soapbubble_blue);
            bubbleList.get(1).setBackgroundResource(R.drawable.soapbubble_blue);
            bubbleList.get(2).setBackgroundResource(R.drawable.soapbubble_blue);
            bubbleList.get(3).setBackgroundResource(R.drawable.soapbubble_blue);
            bubbleList.get(4).setBackgroundResource(R.drawable.soapbubble_blue);
            bubbleList.get(5).setBackgroundResource(R.drawable.soapbubble_blue);
            bubbleList.get(6).setBackgroundResource(R.drawable.soapbubble_blue);
        } else if (bubbleBackground == 1) {
            bubbleList.get(0).setBackgroundResource(R.drawable.soapbubble_green);
            bubbleList.get(1).setBackgroundResource(R.drawable.soapbubble_green);
            bubbleList.get(2).setBackgroundResource(R.drawable.soapbubble_green);
            bubbleList.get(3).setBackgroundResource(R.drawable.soapbubble_green);
            bubbleList.get(4).setBackgroundResource(R.drawable.soapbubble_green);
            bubbleList.get(5).setBackgroundResource(R.drawable.soapbubble_green);
            bubbleList.get(6).setBackgroundResource(R.drawable.soapbubble_green);
        } else if (bubbleBackground == 2) {
            bubbleList.get(0).setBackgroundResource(R.drawable.soapbubble_purple);
            bubbleList.get(1).setBackgroundResource(R.drawable.soapbubble_purple);
            bubbleList.get(2).setBackgroundResource(R.drawable.soapbubble_purple);
            bubbleList.get(3).setBackgroundResource(R.drawable.soapbubble_purple);
            bubbleList.get(4).setBackgroundResource(R.drawable.soapbubble_purple);
            bubbleList.get(5).setBackgroundResource(R.drawable.soapbubble_purple);
            bubbleList.get(6).setBackgroundResource(R.drawable.soapbubble_purple);
        } else if (bubbleBackground == 3) {
            bubbleList.get(0).setBackgroundResource(R.drawable.soapbubble_red);
            bubbleList.get(1).setBackgroundResource(R.drawable.soapbubble_red);
            bubbleList.get(2).setBackgroundResource(R.drawable.soapbubble_red);
            bubbleList.get(3).setBackgroundResource(R.drawable.soapbubble_red);
            bubbleList.get(4).setBackgroundResource(R.drawable.soapbubble_red);
            bubbleList.get(5).setBackgroundResource(R.drawable.soapbubble_red);
            bubbleList.get(6).setBackgroundResource(R.drawable.soapbubble_red);
        } else if (bubbleBackground == 4) {
            bubbleList.get(0).setBackgroundResource(R.drawable.soapbubble_yellow);
            bubbleList.get(1).setBackgroundResource(R.drawable.soapbubble_yellow);
            bubbleList.get(2).setBackgroundResource(R.drawable.soapbubble_yellow);
            bubbleList.get(3).setBackgroundResource(R.drawable.soapbubble_yellow);
            bubbleList.get(4).setBackgroundResource(R.drawable.soapbubble_yellow);
            bubbleList.get(5).setBackgroundResource(R.drawable.soapbubble_yellow);
            bubbleList.get(6).setBackgroundResource(R.drawable.soapbubble_yellow);
        }

        livesNumber = preferences.getInt("number of lives", 3);
        if (livesNumber == 3){
            lives = 3;
        }else if (livesNumber == 5){
            lives = 5;
        }else if (livesNumber == 10){
            lives = 10;
        }
        livesView.setText(getString(R.string.lives)+ lives);
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

    public void addScore(View v){
            if (correctAns) {
                if((bubblePosition != crossed1Position) && (bubblePosition != crossed2Position)) {
                    soundManager.play(correctAudio);
                    score++;
                }else if (bubblePosition == crossed1Position || bubblePosition == crossed2Position){
                    soundManager.play(wrongAudio);
                    lives--;
                    if (lives == 1){
                        soundManager.play(warningAudio);
                    }
                    livesView.setText(getString(R.string.lives)+ lives);
                }
            }

            else{
                soundManager.play(wrongAudio);
                lives--;
                livesView.setText(getString(R.string.lives)+ lives);
            }
            scoreView.setText(getString(R.string.scores)+ score);

            if (compareCount == 7){
                win();
                saveScore();
            }
            if (lives==0){
                gameOver();
                saveScore();
            }
    }

    public void win() {
        pausePlay.setVisibility(View.INVISIBLE);
            soundManager.play(winAudio);
            endScore.setText(getString(R.string.scores) + score);
            winOrLose.setText(getString(R.string.winStatus));
            statusScreen.setVisibility(View.VISIBLE);

    }
    public void gameOver(){
        pausePlay.setVisibility(View.INVISIBLE);
            soundManager.play(loseAudio);
            soundManager.play(loseAudio);
            for (int num = 0; num<bubbleList.size();num++) {
                bubbleList.get(num).setVisibility(View.INVISIBLE);
            }
            scoreView.setVisibility(View.INVISIBLE);
            livesView.setVisibility(View.INVISIBLE);
            winOrLose.setText(getString(R.string.loseStatus));
            endScore.setText(getString(R.string.scores)+score);
            statusScreen.setVisibility(View.VISIBLE);

    }
    //save score
    public void saveScore(){
        if (difficultyChose.equals("easy")){
            System.out.println(highScoresEasy + "now " + score );
            highScoresEasy.add(score);
            System.out.println(highScoresEasy);
            Collections.sort(highScoresEasy, Collections.reverseOrder());
            if(highScoresEasy.size() > 5){
                highScoresEasy.remove(highScoresEasy.size() - 1);
            }
            System.out.println(highScoresEasy + "later " + score);
            FileHelper.writeDataEasy(highScoresEasy, this);

        }else if (difficultyChose.equals("hard")) {

            System.out.println(highScoresHard + "now " + score);
            highScoresHard.add(score);
            System.out.println(highScoresHard);
            Collections.sort(highScoresHard, Collections.reverseOrder());
            if (highScoresHard.size() > 5) {
                highScoresHard.remove(highScoresHard.size() - 1);
            }
            System.out.println(highScoresHard + "later " + score);
            FileHelper.writeDataHard(highScoresHard, this);
        }
    }
    public void pauseGame(View view){
        if(paused){
            paused = false;
            pausedScreen.setVisibility(View.INVISIBLE);

            for (TextView tv : visibleBubbles){
                tv.setVisibility(View.VISIBLE);
            }

            // Start timer
            timer = new Timer();
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
            }, 0, 20);
        }

        else{
            paused = true;

            timer.cancel();
            timer = null;

            for (TextView tv : bubbleList){
                tv.setVisibility(View.INVISIBLE);
            }

            pausedScreen.setVisibility(View.VISIBLE);

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
            visibleBubbles.remove(0);
            compareCount++;

        } else if (currentBubbleId == bubbleList.get(1).getId()) {
            if (compareCount == 1) {
                correctAns = true;
            } else {
                correctAns = false;
            }
            bubbleList.get(compareCount).setVisibility(View.INVISIBLE);
            visibleBubbles.remove(0);
            compareCount++;

        } else if (currentBubbleId == bubbleList.get(2).getId()) {
            if (compareCount == 2) {
                correctAns = true;
            } else {
                correctAns = false;
            }
            bubbleList.get(compareCount).setVisibility(View.INVISIBLE);
            visibleBubbles.remove(0);
            compareCount++;

        } else if (currentBubbleId == bubbleList.get(3).getId()) {
            if (compareCount == 3) {
                correctAns = true;
            } else {
                correctAns = false;
            }
            bubbleList.get(compareCount).setVisibility(View.INVISIBLE);
            visibleBubbles.remove(0);
            compareCount++;

        } else if (currentBubbleId == bubbleList.get(4).getId()) {
            if (compareCount == 4) {
                correctAns = true;
            } else {
                correctAns = false;
            }
            bubbleList.get(compareCount).setVisibility(View.INVISIBLE);
            visibleBubbles.remove(0);
            compareCount++;

        } else if (currentBubbleId == bubbleList.get(5).getId()) {
            if (compareCount == 5) {
                correctAns = true;
            } else {
                correctAns = false;
            }
            bubbleList.get(compareCount).setVisibility(View.INVISIBLE);
            visibleBubbles.remove(0);
            compareCount++;

        } else if (currentBubbleId == bubbleList.get(6).getId()) {
            if (compareCount == 6) {
                correctAns = true;
            } else {
                correctAns = false;
            }
            bubbleList.get(compareCount).setVisibility(View.INVISIBLE);
            visibleBubbles.remove(0);
            compareCount++;

        }
        addScore(v);

        return false;
    }


    public void toMenu(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
