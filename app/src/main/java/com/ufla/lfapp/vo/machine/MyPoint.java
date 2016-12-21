package com.ufla.lfapp.vo.machine;

import android.graphics.Point;

import java.io.Serializable;

/**
 * Created by carlos on 12/14/16.
 */
public class MyPoint implements Serializable {
    int x;
    int y;

    public MyPoint() {

    }

    public MyPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public MyPoint(Point point) {
        x = point.x;
        y = point.y;
    }

    public static MyPoint convertPoint(Point point) {
        return new MyPoint(point);
    }

    public Point toPoint() {
        return new Point(x, y);
    }
}
