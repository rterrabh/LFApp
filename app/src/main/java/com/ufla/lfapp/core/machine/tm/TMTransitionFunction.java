package com.ufla.lfapp.core.machine.tm;

import com.ufla.lfapp.core.machine.TransitionFunction;
import com.ufla.lfapp.core.machine.fsa.FSATransitionFunction;
import com.ufla.lfapp.core.machine.State;

import java.io.Serializable;

/**
 * Created by carlos on 4/2/17.
 */



public class TMTransitionFunction
        extends FSATransitionFunction
        implements Serializable {

    private String writeSymbol;
    private TMMove move;

    @Override
    public int compareTo(TransitionFunction another) {
        int result = super.compareTo(another);
        if (result != 0) {
            return result;
        }
        if ( !(another instanceof TMTransitionFunction)) {
            return -1;
        }
        TMTransitionFunction t = (TMTransitionFunction) another;
        result = writeSymbol.compareTo(t.writeSymbol);
        if (result != 0) return result;
        return move.compareTo(t.move);
    }

    public String getLabel() {
        return symbol + '/' + writeSymbol + ' ' + move;
    }

    public TMTransitionFunction(State currentState, String symbol, State futureState,
                                String writeSymbol, TMMove move) {
        super(currentState, symbol, futureState);
        this.writeSymbol = writeSymbol;
        this.move = move;
    }

    public String getWriteSymbol() {
        return writeSymbol;
    }

    public void setWriteSymbol(String writeSymbol) {
        this.writeSymbol = writeSymbol;
    }

    public TMMove getMove() {
        return move;
    }

    public void setMove(TMMove move) {
        this.move = move;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TMTransitionFunction that = (TMTransitionFunction) o;

        if (writeSymbol != null ? !writeSymbol.equals(that.writeSymbol) : that.writeSymbol != null)
            return false;
        return move == that.move;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (writeSymbol != null ? writeSymbol.hashCode() : 0);
        result = 31 * result + (move != null ? move.hashCode() : 0);
        return result;
    }


}
