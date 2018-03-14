package com.ufla.lfapp.core.machine.pda;

import com.ufla.lfapp.core.machine.Configuration;
import com.ufla.lfapp.core.machine.State;

/**
 * Created by carlos on 4/19/17.
 */

public class PDAConfiguration extends Configuration {

    private String input;
    private String stack;
    private int index;

    public PDAConfiguration(Configuration previous, State state, int depth,
                            String input, String stack, int index) {
        super(previous, state, depth);
        this.input = input;
        this.stack = stack;
        this.index = index;
    }

    public PDAConfiguration(Configuration previous, State state, int depth,
                            StringBuilder input, StringBuilder stack, int index) {
        this(previous, state, depth, input.toString(), stack.toString(), index);
    }

    @Override
    public PDAConfiguration getPrevious() {
        return (PDAConfiguration) previous;
    }

    public String nextStackSymbol() {
        return nextStackSymbols(1);
    }

    public String nextStackSymbols(int num) {
        return stack.substring(stack.length() - num);
    }

    public String nextSymbol() {
        return nextSymbols(1);
    }

    public String nextSymbols(int num) {
        return input.substring(index, index + num);
    }

    public String getInput() {
        return input;
    }

    public String getStack() {
        return stack;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PDAConfiguration that = (PDAConfiguration) o;

        if (index != that.index) return false;
        if (input != null ? !input.equals(that.input) : that.input != null) return false;
        return stack != null ? stack.equals(that.stack) : that.stack == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (input != null ? input.hashCode() : 0);
        result = 31 * result + (stack != null ? stack.hashCode() : 0);
        result = 31 * result + index;
        return result;
    }

    @Override
    public String toString() {
        return new StringBuffer("PDAConfiguration{")
                .append("configuration=")
                .append(super.toString())
                .append(", input='")
                .append(input)
                .append('\'')
                .append(", stack='")
                .append(stack)
                .append('\'')
                .append(", index=")
                .append(index)
                .append('}')
                .toString();
    }

}
