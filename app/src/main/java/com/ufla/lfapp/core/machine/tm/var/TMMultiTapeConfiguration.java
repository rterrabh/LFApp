package com.ufla.lfapp.core.machine.tm.var;

import com.ufla.lfapp.core.machine.Configuration;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.utils.StringUtils;

import java.util.Arrays;

/**
 * Created by carlos on 4/19/17.
 */

public class TMMultiTapeConfiguration extends Configuration {

    private String[] tapes;
    private int[] indexes;

    public TMMultiTapeConfiguration(Configuration previous, State state, int depth,
                                    String[] tapes, int[] indexes) {
        super(previous, state, depth);
        this.tapes = tapes;
        this.indexes = indexes;
    }

    public TMMultiTapeConfiguration(Configuration previous, State state, int depth,
                                    StringBuilder[] tapes, int[] indexes) {
        this(previous, state, depth, StringUtils.toString(tapes), indexes);
    }

    public String[] getTapes() {
        return tapes;
    }

    public int[] getIndexes() {
        return indexes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TMMultiTapeConfiguration that = (TMMultiTapeConfiguration) o;

        if (!Arrays.deepEquals(tapes, that.tapes)) return false;
        return Arrays.equals(indexes, that.indexes);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.deepHashCode(tapes);
        result = 31 * result + Arrays.hashCode(indexes);
        return result;
    }

    @Override
    public String toString() {
        return new StringBuffer("TMMultiTapeConfiguration{")
                .append("configuration=")
                .append(super.toString())
                .append("tapes=")
                .append(Arrays.deepToString(tapes))
                .append(", indexes=")
                .append(Arrays.toString(indexes))
                .append('}')
                .toString();
    }
}
