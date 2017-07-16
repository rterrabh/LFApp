package com.ufla.lfapp.activities.machine.pda.pdatocfg;

import com.ufla.lfapp.utils.Symbols;
import com.ufla.lfapp.core.grammar.Grammar;
import com.ufla.lfapp.core.grammar.Rule;
import com.ufla.lfapp.core.machine.pda.PushdownAutomaton;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.pda.PDAExtTransitionFunction;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by carlos on 3/15/17.
 */

public class PDAToCFGStage4Model extends PDAToCFGStage3Model {

    public PDAToCFGStage4Model(PushdownAutomaton pda) {
        super(pda);
        super.defineNewRules();
    }

    @Override
    public void defineNewRules() {
        Set<String> variables = new TreeSet<>();
        Set<String> terminals = pdaExt.getAlphabet();
        String initialSymbol = Symbols.INITIAL_SYMBOL_GRAMMAR;
        Set<Rule> rulesAux = new LinkedHashSet<>();
        rules = new LinkedHashSet<>();

        SortedSet<State> finalStates = pdaExt.getFinalStates();
        variables.add(Symbols.INITIAL_SYMBOL_GRAMMAR);
        for (State state : finalStates) {
            String rightSide = "<q0," + Symbols.LAMBDA + "," +
                    state.getName() + ">";
            variables.add(rightSide);
            rulesAux.add(new Rule(Symbols.INITIAL_SYMBOL_GRAMMAR, rightSide));
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
                    rulesAux.add(new Rule(leftSide, rightSide));

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
                        rulesAux.add(new Rule(leftSide, rightSide));
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

        rulesAux.addAll(rules);
        grammar = new Grammar(variables, terminals, initialSymbol, rulesAux);
    }

}
