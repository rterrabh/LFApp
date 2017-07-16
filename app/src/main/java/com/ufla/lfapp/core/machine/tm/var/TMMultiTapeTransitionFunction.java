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

public class TMMultiTapeTransitionFunction
        extends AbstractTransitionFunctionTMMultiTape
        implements Serializable {

    private TMMove[] moves;

    @Override
    public int compareTo(TransitionFunction another) {
        int result = super.compareTo(another);
        if (result != 0) return result;
        if (!(another instanceof TMMultiTapeTransitionFunction)) {
            return -1;
        }
        TMMultiTapeTransitionFunction t =
                (TMMultiTapeTransitionFunction) another;
        result = moves.length - t.moves.length;
        if (result != 0) {
            return result;
        }
        for (int i = 0; i < moves.length; i++) {
            result = moves[i].compareTo(t.moves[i]);
            if (result != 0) {
                return result;
            }
        }
        return result;
    }


    public String getLabel() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < moves.length; i++) {
            sb.append(readSymbols[i])
                    .append('/')
                    .append(writeSymbols[i])
                    .append(' ')
                    .append(moves[i])
                    .append(", ");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.setCharAt(sb.length()-1, ']');
        return sb.toString();
    }

    public TMMultiTapeTransitionFunction(State currentState, State futureState, int numTapes) {
        super(currentState, futureState, numTapes);
        moves = new TMMove[numTapes];
    }

    public TMMultiTapeTransitionFunction(State currentState, State futureState,
                                         String[] readSymbols,
                                         String[] writeSymbols,
                                         TMMove[] moves) {
        super(currentState, futureState, readSymbols, writeSymbols);
        this.moves = moves;
        if (!isValidInstance()) {
            throw new RuntimeException(ResourcesContext.getString(R.string.exception_invalid_transition));
        }
    }

    public boolean hasNumTapes(int numTapes) {
        if (!super.hasNumTapes(numTapes)) {
            return false;
        }
        if (moves.length != numTapes) {
            return false;
        }
        return true;
    }

    protected boolean isValidInstance() {
        if (!super.isValidInstance()) {
            return false;
        }
        if (moves == null) {
            return false;
        }
        if (readSymbols.length != moves.length) {
            return false;
        }
        int n = readSymbols.length;
        for (int i = 0; i < n; i++) {
            if (moves[i] == null) {
                return false;
            }
        }
        return true;
    }

    // MÉTODOS ACESSORES
    public TMMove[] getMoves() {
        return moves;
    }

    public void setMoves(TMMove[] moves) {
        this.moves = moves;
    }

    public void setMove(int index, TMMove move) {
        this.moves[index] = move;
    }

    // MÉTODOS EQUALS
    public boolean equalsMoves(TMMove[] moves) {
        return Arrays.deepEquals(this.moves, moves);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TMMultiTapeTransitionFunction that = (TMMultiTapeTransitionFunction) o;

        return Arrays.deepEquals(moves, that.moves);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.deepHashCode(moves);
        return result;
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
                .append(Arrays.deepToString(getWriteSymbols()))
                .append(']')
                .toString();
    }

}
