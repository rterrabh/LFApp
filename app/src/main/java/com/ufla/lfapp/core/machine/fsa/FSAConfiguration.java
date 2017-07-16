package com.ufla.lfapp.core.machine.fsa;

import com.ufla.lfapp.core.machine.Configuration;
import com.ufla.lfapp.core.machine.MachineType;
import com.ufla.lfapp.core.machine.State;

/**
 * Created by carlos on 4/19/17.
 */

public class FSAConfiguration extends Configuration {

    private String input;
    private int index;

    public FSAConfiguration(Configuration previous, State state,
                            int depth, String input, int index) {
        super(previous, state, depth);
        this.input = input;
        this.index = index;
    }

    public FSAConfiguration(Configuration previous, State state,
                            int depth, StringBuilder input, int index) {
        this(previous, state, depth, input.toString(), index);
    }


    public String getInput() {
        return input;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        FSAConfiguration that = (FSAConfiguration) o;

        if (index != that.index) return false;
        return input != null ? input.equals(that.input) : that.input == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (input != null ? input.hashCode() : 0);
        result = 31 * result + index;
        return result;
    }

    @Override
    public String toString() {
        return new StringBuffer("FSAConfiguration{")
                .append("configuration=")
                .append(super.toString())
                .append(", input='")
                .append(input)
                .append('\'')
                .append(", index=")
                .append(index)
                .append('}')
                .toString();
    }

}
