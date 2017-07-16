package com.ufla.lfapp.core.machine.dotlang;

import com.ufla.lfapp.core.machine.State;

import java.io.Serializable;

/**
 * Created by carlos on 15/07/17.
 */

public class Edge implements Serializable {

    public State current;
    public State future;
    public String label;

    public Edge() {

    }

    @Override
    public String toString() {
        return String.format("(%s, %s) -> %s", current, future, label);
    }
}
