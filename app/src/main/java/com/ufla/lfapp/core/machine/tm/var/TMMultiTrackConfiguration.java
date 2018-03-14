package com.ufla.lfapp.core.machine.tm.var;

import com.ufla.lfapp.core.machine.Configuration;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.utils.StringUtils;

import java.util.Arrays;

/**
 * Created by carlos on 4/19/17.
 */

public class TMMultiTrackConfiguration extends Configuration {

    private String[] tapes;
    private int index;

    public TMMultiTrackConfiguration(Configuration previous, State state, int depth,
                                     String[] tapes, int index) {
        super(previous, state, depth);
        this.tapes = tapes;
        this.index = index;
    }

    public TMMultiTrackConfiguration(Configuration previous, State state, int depth,
                                     StringBuilder[] tapes, int index) {
        this(previous, state, depth, StringUtils.toString(tapes), index);
    }

    public String[] getTapes() {
        return tapes;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TMMultiTrackConfiguration that = (TMMultiTrackConfiguration) o;

        if (index != that.index) return false;
        return Arrays.deepEquals(tapes, that.tapes);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.deepHashCode(tapes);
        result = 31 * result + index;
        return result;
    }

    @Override
    public String toString() {
        return new StringBuffer("TMMultiTrackConfiguration{")
                .append("configuration=")
                .append(super.toString())
                .append(", tapes=")
                .append(Arrays.deepToString(tapes))
                .append(", index=")
                .append(index)
                .append('}')
                .toString();
    }
}
