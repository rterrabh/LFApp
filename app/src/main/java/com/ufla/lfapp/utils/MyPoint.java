package com.ufla.lfapp.utils;

import android.graphics.Point;

import java.io.Serializable;

/**
 * Created by carlos on 12/14/16.
 */
public class MyPoint implements Serializable {
    public int x;
    public int y;

    public MyPoint() {

    }

    public MyPoint(MyPoint myPoint) {
        x = myPoint.x;
        y = myPoint.y;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyPoint myPoint = (MyPoint) o;

        if (x != myPoint.x) return false;
        return y == myPoint.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

}
