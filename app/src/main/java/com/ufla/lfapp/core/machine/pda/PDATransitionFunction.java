package com.ufla.lfapp.core.machine.pda;

import com.ufla.lfapp.core.machine.TransitionFunction;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.fsa.FSATransitionFunction;
import com.ufla.lfapp.utils.Symbols;

import java.io.Serializable;

public class PDATransitionFunction
        extends FSATransitionFunction
        implements Serializable {

    private String stacking;
    private String pops;

    public PDATransitionFunction(State currentState, String symbol, State futureState,
                                 String stacking, String pops) {
        super(currentState, symbol, futureState);
        this.stacking = stacking;
        this.pops = pops;
    }

    public PDATransitionFunction(PDATransitionFunction PDATransitionFunction) {
        this(PDATransitionFunction.getCurrentState(), PDATransitionFunction.getSymbol(),
                PDATransitionFunction.getFutureState(), PDATransitionFunction.getStacking(),
                PDATransitionFunction.getPops());
    }


    public boolean popsLambda() {
        return getPops().equals(Symbols.LAMBDA);
    }

    public boolean equalsPops(String pops) {
        return this.pops.equals(pops);
    }

    public void setStacking(String stacking) {
        this.stacking = stacking;
    }

    public String getStacking() {
        return stacking;
    }

    public String getPops() {
        return pops;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PDATransitionFunction that = (PDATransitionFunction) o;

        if (stacking != null ? !stacking.equals(that.stacking) : that.stacking != null)
            return false;
        return pops != null ? pops.equals(that.pops) : that.pops == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (stacking != null ? stacking.hashCode() : 0);
        result = 31 * result + (pops != null ? pops.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(TransitionFunction another) {
        int result = super.compareTo(another);
        if (result != 0) return result;
        if (!(another instanceof PDATransitionFunction)) return 1;

        PDATransitionFunction anotherPA = (PDATransitionFunction) another;
        result = pops.compareTo(anotherPA.pops);
        if (result != 0) return result;
        return stacking.compareTo(anotherPA.stacking);
    }

    @Override
    public String toString() {
        return "δ(" + this.getCurrentState() + ", " + this.getSymbol() + ", " + this.getPops() + ") = {[" +
                this.getFutureState() + ", " + this.getStacking() + "]}";
    }

    public String getLabel() {
        return symbol + ' ' + pops + '/' + stacking;
    }


}
