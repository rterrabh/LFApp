package com.ufla.lfapp.core.machine.tm.var;

import com.ufla.lfapp.core.machine.TransitionFunction;
import com.ufla.lfapp.core.machine.State;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by carlos on 4/8/17.
 */

public abstract class AbstractTransitionFunctionTMMultiTape
        extends TransitionFunction
        implements Serializable {

    protected String[] readSymbols;
    protected String[] writeSymbols;

    @Override
    public int compareTo(TransitionFunction another) {
        int result = super.compareTo(another);
        if (result != 0) return result;
        AbstractTransitionFunctionTMMultiTape t =
                (AbstractTransitionFunctionTMMultiTape) another;
        result = readSymbols.length + writeSymbols.length
                - (t.readSymbols.length + t.writeSymbols.length);
        if (result != 0) {
            return result;
        }
        for (int i = 0; i < readSymbols.length; i++) {
            result = readSymbols[i].compareTo(t.readSymbols[i]);
            if (result != 0) {
                return result;
            }
            result = writeSymbols[i].compareTo(t.writeSymbols[i]);
            if (result != 0) {
                return result;
            }
        }
        return result;
    }

    public AbstractTransitionFunctionTMMultiTape(State currentState, State futureState,
                                                 int numTapes) {
        super(currentState, futureState);
        readSymbols = new String[numTapes];
        writeSymbols = new String[numTapes];
    }

    public AbstractTransitionFunctionTMMultiTape(State currentState, State futureState,
                                                 String[] readSymbols,
                                                 String[] writeSymbols) {
        super(currentState, futureState);
        this.readSymbols = readSymbols;
        this.writeSymbols = writeSymbols;
    }

    public int getNumTapes() {
        return readSymbols.length;
    }

    public boolean hasNumTapes(int numTapes) {
        if (readSymbols.length != numTapes || writeSymbols.length != numTapes) {
            return false;
        }
        return true;
    }

    @Override
    protected boolean isValidInstance() {
        if (!super.isValidInstance()) {
            return false;
        }
        if (readSymbols == null || writeSymbols == null) {
            return false;
        }
        if (readSymbols.length != writeSymbols.length) {
            return false;
        }
        int n = readSymbols.length;
        for (int i = 0; i < n; i++) {
            if (readSymbols[i] == null || writeSymbols[i] == null) {
                return false;
            }
        }
        return true;
    }

    // MÉTODOS ACESSORES
    public String[] getReadSymbols() {
        return readSymbols;
    }

    public String[] getWriteSymbols() {
        return writeSymbols;
    }

    public String getSymbols() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < readSymbols.length; i++) {
            sb.append(readSymbols[i]).append('/').append(writeSymbols[i]).append(' ');
        }
        return sb.substring(0, sb.length() - 1);
    }

    // MÉTODOS EQUALS
    public boolean equalsReadSymbols(String[] readSymbols) {
        return Arrays.deepEquals(this.readSymbols, readSymbols);
    }

    public void setReadSymbols(String[] readSymbols) {
        this.readSymbols = readSymbols;
    }

    public void setWriteSymbols(String[] writeSymbols) {
        this.writeSymbols = writeSymbols;
    }

    @Override

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AbstractTransitionFunctionTMMultiTape that = (AbstractTransitionFunctionTMMultiTape) o;

        if (!Arrays.deepEquals(readSymbols, that.readSymbols)) return false;
        return Arrays.deepEquals(writeSymbols, that.writeSymbols);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.deepHashCode(readSymbols);
        result = 31 * result + Arrays.deepHashCode(writeSymbols);
        return result;
    }

}
