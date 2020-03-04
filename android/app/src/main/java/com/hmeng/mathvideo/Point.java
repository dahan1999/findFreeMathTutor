package com.hmeng.mathvideo;

public class Point {
    private float x,y;

    public Point(float posX, float posY) {
        this.x = posX;
        this.y = posY;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
