package com.ufla.lfapp.core.machine.pda;

import com.ufla.lfapp.utils.Symbols;
import com.ufla.lfapp.core.grammar.Grammar;
import com.ufla.lfapp.core.grammar.Rule;
import com.ufla.lfapp.core.machine.State;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by carlos on 3/3/17.
 */

public class PDAToGrammar {

    private PDAToGrammar() {

    }

    private static PushdownAutomatonExtend extendPA(PushdownAutomaton pdAutomaton) {
        PushdownAutomatonExtend pdaExt = new PushdownAutomatonExtend(pdAutomaton);
        SortedSet<String> stackAlphabet = pdAutomaton.getStackAlphabet();
        Set<PDAExtTransitionFunction> newTFPDAExt = new TreeSet<>();
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
                    newTFPDAExt.add(new PDAExtTransitionFunction(currentState, tFPA.getSymbol(),
                            futureState, stacking, symbol));
                }
            }
        }
        pdaExt.getTransitionFunctions().addAll(newTFPDAExt);
        return pdaExt;
    }

    public static Grammar toGrammar(PushdownAutomaton pdAutomaton) {
        PushdownAutomatonExtend pdaExt = extendPA(pdAutomaton);
        Set<String> variables = new TreeSet<>();
        Set<String> terminals = pdAutomaton.getAlphabet();
        String initialSymbol = "S";
        Set<Rule> rules = new LinkedHashSet<>();

        SortedSet<State> finalStates = pdaExt.getFinalStates();
        variables.add("S");
        for (State state : finalStates) {
            String rightSide = "<q0," + Symbols.LAMBDA + "," +
                    state.getName() + ">";
            variables.add(rightSide);
            rules.add(new Rule("S", rightSide));
        }

        SortedSet<State> states = pdaExt.getStates();
        Set<PDAExtTransitionFunction> tFPDAExts = pdaExt.getTransitionFunctions();
        for (PDAExtTransitionFunction tFPDAExt : tFPDAExts) {
            String qi = tFPDAExt.getCurrentState().getName();
            String qj = tFPDAExt.getFutureState().getName();
            String pops = tFPDAExt.getPops();
            String symbol = tFPDAExt.getSymbol();
            if (tFPDAExt.getStacking().size() == 1) {
                String stacking = tFPDAExt.getStacking().get(0);
                for (State state : states) {
                    String qk = state.getName();
                    String leftSide = "<" + qi + "," + pops + "," + qk + ">";
                    String rightSide = "<" + qj + "," + stacking + "," + qk + ">";
                    variables.add(leftSide);
                    variables.add(rightSide);
                    rightSide = symbol + rightSide;
                    rules.add(new Rule(leftSide, rightSide));

                }
            } else {
                String stack0 = tFPDAExt.getStacking().get(0);
                String stack1 = tFPDAExt.getStacking().get(1);
                for (State stateK : states) {
                    String qk = stateK.getName();
                    for (State stateN : states) {
                        String qn = stateN.getName();
                        String leftSide = "<" + qi + "," + pops + "," + qk + ">";
                        String rightSideAux = "<" + qj + "," + stack0 + "," + qn + ">";
                        variables.add(rightSideAux);
                        String rightSide = "<" + qn + "," + stack1 + "," + qk + ">";
                        variables.add(rightSide);
                        rightSide = symbol + rightSideAux + rightSide;
                        rules.add(new Rule(leftSide, rightSide));
                        variables.add(leftSide);
                    }
                }
            }
        }


        for (State state : states) {
            String qk = state.getName();
            String leftSide = "<" + qk + "," + Symbols.LAMBDA + "," + qk + ">";
            variables.add(leftSide);
            rules.add(new Rule(leftSide, Symbols.LAMBDA));
        }

        return new Grammar(variables, terminals, initialSymbol, rules);
    }
}
