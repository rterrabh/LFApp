package com.ufla.lfapp.activities.machine.pda.pdatocfg;

import com.ufla.lfapp.core.machine.pda.PDATransitionFunction;
import com.ufla.lfapp.utils.Symbols;
import com.ufla.lfapp.core.machine.pda.PushdownAutomaton;
import com.ufla.lfapp.core.machine.pda.PushdownAutomatonExtend;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.pda.PDAExtTransitionFunction;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

/**
 * Created by carlos on 3/8/17.
 */

public class PDAToCFGStage1Model {


    protected PushdownAutomaton pda;
    protected PushdownAutomatonExtend pdaExt;
    protected Set<PDAExtTransitionFunction> newTransitions;

    public PDAToCFGStage1Model(PushdownAutomaton pda) {
        this.pda = pda;
        pdaExt = extendPA(pda);
    }

    private PushdownAutomatonExtend extendPA(PushdownAutomaton pdAutomaton) {
        PushdownAutomatonExtend pdaExt = new PushdownAutomatonExtend(pdAutomaton);
        SortedSet<String> stackAlphabet = pdAutomaton.getStackAlphabet();
        newTransitions = new LinkedHashSet<>();
        Set<PDATransitionFunction> transitionFunction = pdAutomaton.getTransitionFunctions();
        for (PDATransitionFunction tFPA : transitionFunction) {
            if (tFPA.getPops().equals(Symbols.LAMBDA)) {
                State currentState = pdaExt.getState(tFPA.getCurrentState().getName());
                State futureState = pdaExt.getState(tFPA.getFutureState().getName());
                for (String symbol : stackAlphabet) {
                    List<String> stacking = new ArrayList<>();
                    if (!tFPA.getStacking().equals(Symbols.LAMBDA)) {
                        stacking.add(tFPA.getStacking());
                    }
                    stacking.add(symbol);
                    newTransitions.add(new PDAExtTransitionFunction(currentState, tFPA.getSymbol(),
                            futureState, stacking, symbol));
                }
            }
        }
        pdaExt.getTransitionFunctions().addAll(newTransitions);
        return pdaExt;
    }

    public PushdownAutomaton getPda() {
        return pda;
    }

    public PushdownAutomatonExtend getPdaExt() {
        return pdaExt;
    }

    public Set<PDAExtTransitionFunction> getNewTransitions() {
        return newTransitions;
    }

    public String getNewTransitionsToString() {
        StringBuilder sb = new StringBuilder();
        for (PDAExtTransitionFunction tfpdaExt : newTransitions) {
            sb.append('•')
                    .append(tfpdaExt.toString())
                    .append('\n');
        }
        return sb.substring(0, sb.length() - 1);
    }

}
