package com.ufla.lfapp.vo.machine;

import java.io.Serializable;

/**
 * Created by carlos on 1/24/17.
 */

public class State implements Comparable<State>, Serializable {

    private String name;

    public State(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;

        return name != null ? name.equals(state.name) : state.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return (name == null) ? "null" : name;
    }

    public State copy() {
        return new State(name);
    }

    @Override
    public int compareTo(State another) {
        return name.compareTo(another.name);
    }
}
