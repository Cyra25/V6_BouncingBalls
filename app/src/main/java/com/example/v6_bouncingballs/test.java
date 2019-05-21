package com.example.v6_bouncingballs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class test extends AppCompatActivity {
    int maxX, maxY;
    public Point point;
    //    public int x,y;
    private TextView ball;
    private float speedX = 0.1f ;
    private float speedY = 0.1f ;
    int num;
    public ArrayList<TextView> bubbleList = new ArrayList<>();
    public int centerX, centerY;
    private float lefttopX,leftTopY;
    public TextView firstBall;
    public int screenWidth, screenHeight;
    private int radius = 50;
    private Paint paint;
    public Point centerPoint;
    // Initialise Class
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    float testSpeedX = 0.1f;
    float testSpeedY = 0.1f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_game);
        create();



        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        maxX = screenWidth;
        screenHeight = size.y;
        maxY = screenHeight;


    }
    public void create(){

        TextView blueBubble = (TextView) findViewById(R.id.blueBubble);
        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.main_layout);

        //creating the bubble
        for (int numOfBubbles = 0; numOfBubbles < 5; numOfBubbles++) {
            TextView bubble = new TextView(this);
            bubble.setTextSize(15);
            bubble.setBackground(getDrawable(R.drawable.soapbubble_blue));
            bubble.setText("3 x 2 - 5");
            bubble.setGravity(Gravity.CENTER);
            constraintLayout.addView(bubble);
            bubble.getLayoutParams().height = 200;
            bubble.getLayoutParams().width = 200;
            bubbleList.add(bubble);
            this.setContentView(constraintLayout);


            //setting x and y position for each bubble
            for (int bubblesInList = 0; bubblesInList < bubbleList.size(); bubblesInList++){
                Random random = new Random();
//                lefttopX = random.nextInt(1000 + 1);
//                leftTopY = random.nextInt(1000 + 1);
                leftTopY = 0;
                lefttopX = 0;
                bubbleList.get(bubblesInList).setX(lefttopX);
                bubbleList.get(bubblesInList).setY(leftTopY);
                // Start timer
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                for (int bubblesInList = 0; bubblesInList < bubbleList.size(); bubblesInList++){
                                    System.out.println(bubbleList.get(bubblesInList).getX() + 2* radius > maxX
                                    );
                                    System.out.println(bubbleList.get(bubblesInList).getX() < 0);
                                    System.out.println(bubbleList.get(bubblesInList).getY() + 2* radius > maxY);
                                    System.out.println(bubbleList.get(bubblesInList).getY() < 0);
                                    System.out.println(lefttopX);
                                    System.out.println(leftTopY);

                                    if (bubbleList.get(bubblesInList).getX() + 2* radius > screenWidth){
                                        speedX = speedX * -1;
//                                        lefttopX = -lefttopX;
                                         testSpeedX = -0.1f;
                                    }
                                    else if (bubbleList.get(bubblesInList).getX() < 0){
                                        speedX = speedX * -1;

//                                        lefttopX = -lefttopX;
                                    }

                                    if (bubbleList.get(bubblesInList).getY() + 2* radius > screenHeight-2*radius){
                                        speedY = speedY * -1;

//                                        leftTopY = -leftTopY;
                                        testSpeedY = -0.1f;
                                    }
                                    else if (bubbleList.get(bubblesInList).getY() < 0){
                                        speedY = speedY * -1;
//                                        leftTopY = -leftTopY;
                                    }
//                                    lefttopX = lefttopX +speedX*2;
//                                    leftTopY = leftTopY -speedY*2;
                                    lefttopX = lefttopX +testSpeedX;
                                    leftTopY = leftTopY +testSpeedY;

                                    bubbleList.get(bubblesInList).setX(lefttopX);
                                    bubbleList.get(bubblesInList).setY(leftTopY);
                                }
                            }
                        });
                    }
                }, 0, 20);
            }


//        newTextView.setRadius(80);
//        newTextView.setCenterX(lefttopX+newTextView.getRadius());
//        newTextView.setCenterY(leftTopY+newTextView.getRadius());
//            newTextView.setCenterPoint(newTextView.getCenterX(), newTextView.getCenterY());
//            newTextView.setCoords(lefttopX, 0, newTextView.getRadius());
//            newTextView.setX(80);
//            newTextView.setY(80);
//            speedX = 0;
//            speedY = 0;
//            setSpeed(speedX,speedY);
//            newTextView.setSpeed(speedX,speedY);
//            bubbleList.add(newTextView);

//            System.out.println("printing the bubbleList" + bubbleList.size());
//            System.out.println(ball.getCenterPoint());
        }
    }



    private void update() {


    }

}
