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

    public Edge(State current, State future, String label) {
        this.current = current;
        this.future = future;
        this.label = label;
    }

    public Edge() {

    }

    @Override
    public String toString() {
        return String.format("(%s, %s) -> %s", current, future, label);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (current != null ? !current.equals(edge.current) : edge.current != null) return false;
        if (future != null ? !future.equals(edge.future) : edge.future != null) return false;
        return label != null ? label.equals(edge.label) : edge.label == null;

    }

    @Override
    public int hashCode() {
        int result = current != null ? current.hashCode() : 0;
        result = 31 * result + (future != null ? future.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        return result;
    }
}
