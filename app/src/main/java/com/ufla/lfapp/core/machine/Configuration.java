package com.ufla.lfapp.core.machine;

/**
 * Created by carlos on 4/18/17.
 */

public abstract class Configuration {

    protected Configuration previous;
    private State state;
    private int depth;

    public Configuration(Configuration previous, State state, int depth) {
        this.previous = previous;
        this.state = state;
        this.depth = depth;
    }

    // MÉTODOS ACESSORES
    public Configuration getPrevious() {
        return previous;
    }

    public State getState() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Configuration that = (Configuration) o;

        if (depth != that.depth) return false;
        if (previous != null ? !previous.equals(that.previous) : that.previous != null)
            return false;
        return state != null ? state.equals(that.state) : that.state == null;

    }

    @Override
    public int hashCode() {
        int result = previous != null ? previous.hashCode() : 0;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + depth;
        return result;
    }

    @Override
    public String toString() {
        return new StringBuilder("Configuration{")
                .append("previous=")
                .append(previous)
                .append(", state=")
                .append(state)
                .append(", depth=")
                .append(depth)
                .append('}')
                .toString();
    }

}
