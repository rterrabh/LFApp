package com.ufla.lfapp.core.machine.tm.var;

import com.ufla.lfapp.R;
import com.ufla.lfapp.core.machine.TransitionFunction;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.tm.TMMove;
import com.ufla.lfapp.utils.ResourcesContext;
import com.ufla.lfapp.utils.Symbols;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by carlos on 4/7/17.
 */

public class TMMultiTrackTransitionFunction
        extends AbstractTransitionFunctionTMMultiTape
        implements Serializable {

    private TMMove move;

    @Override
    public int compareTo(TransitionFunction another) {
        int result = super.compareTo(another);
        if (result != 0) return result;
        TMMultiTrackTransitionFunction t =
                (TMMultiTrackTransitionFunction) another;
        result = move.compareTo(t.move);
        return result;
    }

    public String getLabel() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < readSymbols.length; i++) {
            sb.append(readSymbols[i])
                    .append('/')
                    .append(writeSymbols[i])
                    .append(' ');
        }
        return sb.append(move)
                .append(']')
                .toString();
    }

    public TMMultiTrackTransitionFunction(State currentState, State futureState, int numTracks) {
        super(currentState, futureState, numTracks);
    }

    public TMMultiTrackTransitionFunction(State currentState, State futureState,
                                          String[] readSymbols,
                                          String[] writeSymbols,
                                          TMMove move) {
        super(currentState, futureState, readSymbols, writeSymbols);
        this.move = move;
        if (!isValidInstance()) {
            throw new RuntimeException(ResourcesContext.getString(R.string.exception_invalid_transition));
        }
    }

    protected boolean isValidInstance() {
        if (!super.isValidInstance()) {
            return false;
        }
        if (move == null) {
            return false;
        }
        return true;
    }

    // MÉTODOS ACESSORES
    public TMMove getMove() {
        return move;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TMMultiTrackTransitionFunction that = (TMMultiTrackTransitionFunction) o;

        return move == that.move;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (move != null ? move.hashCode() : 0);
        return result;
    }

    public void setMove(TMMove move) {
        this.move = move;
    }

    @Override
    public String toString() {
        return new StringBuilder(Symbols.TRANSACTION)
                .append('(')
                .append(getCurrentState().getName())
                .append(", ")
                .append(Arrays.deepToString(getReadSymbols()))
                .append(") = [")
                .append(getFutureState())
                .append(", ")
                .append(Arrays.deepToString(getWriteSymbols()))
                .append(", ")
                .append(getMove())
                .append(']')
                .toString();
    }

}
