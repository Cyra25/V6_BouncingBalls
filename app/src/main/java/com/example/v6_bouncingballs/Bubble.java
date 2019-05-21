package com.example.v6_bouncingballs;

import android.graphics.Paint;
import android.graphics.Point;
import android.widget.TextView;

import java.util.ArrayList;

public class Bubble {
    int maxX, maxY;
    public Point point;
    //    public int x,y;
    private TextView ball;
    private int speedX ;
    private int speedY ;
    int num;
    public ArrayList<TextView> bubbleList = new ArrayList<>();
    public int centerX, centerY;
    private int lefttopX,leftTopY;
    public TextView firstBall;
    public int screenWidth, screenHeight;
    private int radius;
    private Paint paint;
    public Point centerPoint;

    public Bubble(){

    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
    public int getRadius() {
        return radius;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }
    public int getCenterX() {
        return centerX;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }
    public int getCenterY() {
        return centerY;
    }

    //the actual point(center) of the ball
    public void setCenterPoint(int centerX, int centerY) {
        centerPoint = new Point(centerX, centerY);
    }
    public Point getCenterPoint() {
        return centerPoint;
    }

    //set the point for the individual balls using the point on the left top corner of the ball
    public void setCoords(int x, int y, int radius){
        lefttopX = x;
        leftTopY = y;
        this.radius = radius;
    }


    public void setSpeed(int speedX, int speedY){
        this.speedX = speedX;
        this.speedY = speedY;
    }

    public void setMax(int maxX, int maxY){
        this.maxX = maxX;
        this.maxY = maxY;
    }
}
