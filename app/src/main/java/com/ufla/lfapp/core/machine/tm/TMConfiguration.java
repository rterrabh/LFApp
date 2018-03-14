package com.ufla.lfapp.core.machine.tm;

import com.ufla.lfapp.core.machine.Configuration;
import com.ufla.lfapp.core.machine.State;

/**
 * Created by carlos on 4/19/17.
 */

public class TMConfiguration extends Configuration {

    private String tape;
    private int index;

    public TMConfiguration(Configuration previous, State state, int depth,
                           String tape, int index) {
        super(previous, state, depth);
        this.tape = tape;
        this.index = index;
    }

    public TMConfiguration(Configuration previous, State state, int depth,
                           StringBuilder tape, int index) {
        this(previous, state, depth, tape.toString(), index);
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TMConfiguration that = (TMConfiguration) o;

        if (index != that.index) return false;
        return tape != null ? tape.equals(that.tape) : that.tape == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (tape != null ? tape.hashCode() : 0);
        result = 31 * result + index;
        return result;
    }

    @Override
    public String toString() {
        return new StringBuffer("TMConfiguration{")
                .append("configuration=")
                .append(super.toString())
                .append(", tape='")
                .append(tape)
                .append('\'')
                .append(", index=")
                .append(index)
                .append('}')
                .toString();
    }

}
