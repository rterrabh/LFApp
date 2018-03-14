package com.ufla.lfapp.activities.machine.pda.pdatocfg;

import com.ufla.lfapp.utils.Symbols;
import com.ufla.lfapp.core.grammar.Grammar;
import com.ufla.lfapp.core.grammar.Rule;
import com.ufla.lfapp.core.machine.pda.PushdownAutomaton;
import com.ufla.lfapp.core.machine.State;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by carlos on 3/15/17.
 */

public class PDAToCFGStage2Model extends PDAToCFGStage1Model {

    protected Grammar grammar;
    protected Set<Rule> rules;

    public PDAToCFGStage2Model(PushdownAutomaton pda) {
        super(pda);
        newTransitions = null;
        grammar = getGrammar();
    }

    private Grammar getGrammar() {
        Set<String> variables = new TreeSet<>();
        Set<String> terminals = pdaExt.getAlphabet();
        String initialSymbol = Symbols.INITIAL_SYMBOL_GRAMMAR;
        rules = new LinkedHashSet<>();

        SortedSet<State> finalStates = pdaExt.getFinalStates();
        variables.add(Symbols.INITIAL_SYMBOL_GRAMMAR);
        for (State state : finalStates) {
            String rightSide = "<q0, " + Symbols.LAMBDA + ", " +
                    state.getName() + ">";
            variables.add(rightSide);
            rules.add(new Rule(Symbols.INITIAL_SYMBOL_GRAMMAR, rightSide));
        }

        return new Grammar(variables, terminals, initialSymbol, rules);
    }

    public String rulesToString() {
        StringBuilder sb = new StringBuilder();
        for (Rule rule : rules) {
            sb.append(rules)
                    .append("\n");
        }
        return sb.substring(0, sb.length()-1);
    }

    public String grammarToString() {
        return grammar.toString();
    }

}
