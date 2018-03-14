package com.ufla.lfapp.core.machine.pda;

import com.ufla.lfapp.utils.Symbols;
import com.ufla.lfapp.core.grammar.AcademicSupport;
import com.ufla.lfapp.core.grammar.Grammar;
import com.ufla.lfapp.core.grammar.Rule;
import com.ufla.lfapp.core.machine.State;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by carlos on 3/2/17.
 */

public class GrammarToPDAExt {

    private GrammarToPDAExt() {

    }

    public static PushdownAutomatonExtend toPDAutomatonExt(Grammar grammar) {
        State q0 = new State("q0");
        State q1 = new State("q1");
        SortedSet<State> states = new TreeSet<>();
        SortedSet<State> finalStates = new TreeSet<>();
        State initialState = q0;
        states.add(q0);
        states.add(q1);
        finalStates.add(q1);
        Set<PDAExtTransitionFunction> transitions = new HashSet<>();
        Grammar grammarFNG = grammar.FNGTerra(grammar, new AcademicSupport());
        grammarFNG.getVariables();
        Map<String, Set<Rule>> rules = grammarFNG.getRulesMapLeftToRule();
        Set<Rule> rulesS = rules.get(grammarFNG.getInitialSymbol());
        for (Rule rule : rulesS) {
            List<String> symbols = rule.getListOfSymbolsOnRightSide();
            String readSymbol = symbols.get(0);
            symbols.remove(0);
            if (symbols.isEmpty()) {
                symbols.add(Symbols.LAMBDA);
            }
            transitions.add(new PDAExtTransitionFunction(q0, readSymbol, q1, symbols,
                    Symbols.LAMBDA));
        }
        rules.remove(grammarFNG.getInitialSymbol());
        for (Map.Entry<String, Set<Rule>> entry : rules.entrySet()) {
            String leftSide = entry.getKey();
            for (Rule rule : entry.getValue()) {
                List<String> symbols = rule.getListOfSymbolsOnRightSide();
                String readSymbol = symbols.get(0);
                symbols.remove(0);
                if (symbols.isEmpty()) {
                    symbols.add(Symbols.LAMBDA);
                }
                transitions.add(new PDAExtTransitionFunction(q1, readSymbol, q1, symbols,
                        leftSide));
            }
        }
        return new PushdownAutomatonExtend(states, initialState, finalStates, transitions);
    }
}
