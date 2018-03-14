package com.ufla.lfapp.activities.grammar.algorithms.cfgtopda;

import com.ufla.lfapp.utils.Symbols;
import com.ufla.lfapp.core.grammar.Grammar;
import com.ufla.lfapp.core.grammar.Rule;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.pda.PDAExtTransitionFunction;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by carlos on 3/8/17.
 */

public class CFGToPDAStage4Model extends CFGToPDAStage3Model {


    public CFGToPDAStage4Model(Grammar grammar) {
        super(grammar);
        this.newTransitions.clear();
        generateTransitions();
    }

    private void generateTransitions() {
        State q0 = pushdownAutomatonExtend.getState("q0");
        State q1 = pushdownAutomatonExtend.getState("q1");
        Set<PDAExtTransitionFunction> transitions = pushdownAutomatonExtend
                .getTransitionFunctions();
        Map<String, Set<Rule>> rules = grammar.getRulesMapLeftToRule();
        rules.remove(grammar.getInitialSymbol());
        for (Map.Entry<String, Set<Rule>> entry : rules.entrySet()) {
            String leftSide = entry.getKey();
            for (Rule rule : entry.getValue()) {
                List<String> symbols = rule.getListOfSymbolsOnRightSide();
                String readSymbol = symbols.get(0);
                symbols.remove(0);
                if (symbols.isEmpty()) {
                    symbols.add(Symbols.LAMBDA);
                }
                PDAExtTransitionFunction newTransition =
                        new PDAExtTransitionFunction(q1, readSymbol, q1, symbols,
                        leftSide);
                transitions.add(newTransition);
                newTransitions.put(rule, newTransition);
            }
        }
    }


}
