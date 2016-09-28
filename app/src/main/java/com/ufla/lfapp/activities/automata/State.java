package com.ufla.lfapp.activities.automata;

/**
 * Created by carlos on 9/21/16.
 */
public class State {

    private String name;
    private float x;
    private float y;

    public State(String name, float x, float y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public State() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
