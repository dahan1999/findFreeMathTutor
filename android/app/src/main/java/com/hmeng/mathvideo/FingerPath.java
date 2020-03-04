package com.hmeng.mathvideo;

import android.graphics.Path;

import java.util.Vector;

public class FingerPath {

    public int color;
    public int strokeWidth;
    public Path path;
    private Vector<Point> mStroke = new Vector<>();

    public FingerPath(int color, int strokeWidth, Path path) {
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.path = path;
    }
    public void addPoint(float x, float y) {
        this.mStroke.add(new Point(x,y));
    }
    public Vector<Point> getStroke() {
        return mStroke;
    }
}
