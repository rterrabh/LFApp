package com.ufla.lfapp.views.graph;

/**
 * Created by carlos on 1/16/17.
 */

public class BorderVertex {

    private boolean top;
    private boolean bottom;
    private boolean left;
    private boolean right;

    public BorderVertex(boolean left, boolean top, boolean right, boolean bottom ) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public boolean isBottom() {
        return bottom;
    }

    public void setBottom(boolean bottom) {
        this.bottom = bottom;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }
}
