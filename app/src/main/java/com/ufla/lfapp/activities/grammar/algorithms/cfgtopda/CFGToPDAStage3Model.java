package com.ufla.lfapp.activities.grammar.algorithms.cfgtopda;

import com.ufla.lfapp.utils.Symbols;
import com.ufla.lfapp.core.grammar.Grammar;
import com.ufla.lfapp.core.grammar.Rule;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.pda.PDAExtTransitionFunction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by carlos on 3/8/17.
 */

public class CFGToPDAStage3Model extends CFGToPDAStage2Model {

    protected Grammar grammar;
    protected Map<Rule, PDAExtTransitionFunction> newTransitions;

    public CFGToPDAStage3Model(Grammar grammar) {
        super();
        this.grammar = grammar;
        newTransitions = new HashMap<>();
        generateTransitions();
    }

    private void generateTransitions() {
        State q0 = pushdownAutomatonExtend.getState("q0");
        State q1 = pushdownAutomatonExtend.getState("q1");
        Set<PDAExtTransitionFunction> transitions = pushdownAutomatonExtend
                .getTransitionFunctions();
        Set<Rule> rulesS = grammar.getRules(grammar.getInitialSymbol());
        for (Rule rule : rulesS) {
            List<String> symbols = rule.getListOfSymbolsOnRightSide();
            String readSymbol = symbols.get(0);
            symbols.remove(0);
            if (symbols.isEmpty()) {
                symbols.add(Symbols.LAMBDA);
            }
            PDAExtTransitionFunction newTransition =
                    new PDAExtTransitionFunction(q0, readSymbol, q1, symbols,
                    Symbols.LAMBDA);
            transitions.add(newTransition);
            newTransitions.put(rule, newTransition);
        }
    }

    public String rulesTransitions() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Rule, PDAExtTransitionFunction> entry : newTransitions.entrySet()) {
            sb.append(entry.getKey().toString())
                    .append("  =>  ")
                    .append(entry.getValue().toString())
                    .append('\n');
        }
        return sb.substring(0, sb.length() - 1);
    }

}
