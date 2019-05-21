package com.example.v6_bouncingballs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class test extends AppCompatActivity {
    int maxX, maxY;
    public Point point;
    //    public int x,y;
    private int speedX ;
    Bubble bubbleInBubble;
    private int speedY ;
    public ArrayList<Bubble> bubbleList = new ArrayList<>();
    public int centerX, centerY;
    private int lefttopX,leftTopY;
    public int screenWidth, screenHeight;
    private int radius;
    private Paint paint;
    public Point centerPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_game);

        TextView blueBubble = (TextView) findViewById(R.id.blueBubble);
        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.main_layout);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        for (int numOfBubbles = 0; numOfBubbles < 5; numOfBubbles++) {
            Random rnd = new Random();
            lefttopX = new Random().nextInt(1000 + 1);
//          the code for making the speed of the ball random
//            speedX = rnd.nextInt(10);
//            speedY = rnd.nextInt(10);
            TextView newTextView = new TextView(this);
            newTextView.setTextSize(15);
            newTextView.setBackground(getDrawable(R.drawable.soapbubble_blue));
            newTextView.setText("3 x 2 - 5");
            newTextView.setGravity(Gravity.CENTER);
            constraintLayout.addView(newTextView);
            newTextView.getLayoutParams().height = 200;
            newTextView.getLayoutParams().width = 200;
            bubbleList.add(newTextView);
            this.setContentView(constraintLayout);
            update();

        Iterator itr = bubbleList.iterator();
        while (itr.hasNext()){
            Bubble bubbleInBubble = (Bubble) itr.next();
            bubbleInBubble.setMax(maxX,maxY);
        }

        for (int bubblesInList = 0; bubblesInList < bubbleList.size(); bubblesInList++){
            Random randomXY = new Random();
            lefttopX = new Random().nextInt(1000 + 1);
            leftTopY = new Random().nextInt(1000 + 1);
//            bubbleList.get(bubblesInList).setX(lefttopX);
//            bubbleList.get(bubblesInList).setY(leftTopY);
            bubbleInBubble.setCoords(80,80,40);
            bubbleInBubble.setSpeed(8,8);
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
    private void setSpeed(int speedX, int speedY){
        this.speedX = speedX;
        this.speedY = speedY;
    }
    private void update() {
        if (lefttopX + 2* radius > maxX){
            speedX = speedX * -1;
        }
        else if (lefttopX < 0){
            speedX = speedX * -1;
        }
        if (leftTopY + 2* radius > maxY){
            speedY = speedY * -1;
        }
        else if (leftTopY < 0){
            speedY = speedY * -1;
        }
        lefttopX = lefttopX +speedX;
        leftTopY = leftTopY -speedY;
    }

}
