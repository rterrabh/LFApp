package com.ufla.lfapp.core.machine.pda;

import com.ufla.lfapp.core.machine.TransitionFunction;
import com.ufla.lfapp.core.machine.fsa.FSATransitionFunction;
import com.ufla.lfapp.utils.Symbols;
import com.ufla.lfapp.core.machine.State;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by carlos on 3/2/17.
 */

public class PDAExtTransitionFunction
        extends FSATransitionFunction
        implements Serializable {

    private List<String> stacking;
    private String pops;

    public PDAExtTransitionFunction(State currentState, String symbol, State futureState,
                                    List<String> stacking, String pops) {
        super(currentState, symbol, futureState);
        this.stacking = stacking;
        this.pops = pops;
    }

    public String getLabel() {
        return symbol + ' ' + pops + '/' + stackingToString();
    }

    public PDAExtTransitionFunction(PDATransitionFunction tFPA,
                                    PushdownAutomatonExtend pdaExt) {
        super(pdaExt.getState(tFPA.getCurrentState().getName()), tFPA.getSymbol(),
                pdaExt.getState(tFPA.getFutureState().getName()));
        stacking = new ArrayList<>();
        stacking.add(tFPA.getStacking());
        pops = tFPA.getPops();
    }

    public List<String> getStacking() {
        return stacking;
    }

    public String getPops() {
        return pops;
    }

    public String stackingToString() {
        final StringBuilder sb = new StringBuilder();
        for (String str : stacking) {
            sb.append(str);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PDAExtTransitionFunction that = (PDAExtTransitionFunction) o;


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

        PDAExtTransitionFunction anotherPAExt = (PDAExtTransitionFunction) another;
        result = pops.compareTo(anotherPAExt.pops);
        if (result != 0) return result;
        int sizeStacking = stacking.size();
        int sizeStackingAnother = anotherPAExt.stacking.size();
        result = (sizeStacking - sizeStackingAnother);
        if (result != 0) return result;
        for (int i = 0; i < sizeStacking; i++) {
            result = stacking.get(i).compareTo(anotherPAExt.stacking.get(i));
            if (result != 0) return result;
        }
        return 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(Symbols.TRANSACTION);
        sb.append('(')
                .append(currentState)
                .append(", ")
                .append(symbol)
                .append(", ")
                .append(pops)
                .append(") = [ ")
                .append(futureState)
                .append(", ")
                .append(stackingToString())
                .append(']');
        return sb.toString();
    }
}
