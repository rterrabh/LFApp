package com.ufla.lfapp.core.grammar;

import com.ufla.lfapp.R;
import com.ufla.lfapp.utils.HtmlColors;
import com.ufla.lfapp.utils.HtmlTags;
import com.ufla.lfapp.utils.ResourcesContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by carlos on 30/07/16.
 */
public abstract class AbstractAcademicSupportRLR {

    private List<String> orderVariables;
    protected Grammar originalGrammar;

    AbstractAcademicSupportRLR() {
        this.orderVariables = new ArrayList<>();
    }

    public void setOriginalGrammar(Grammar originalGrammar) {
        this.originalGrammar = originalGrammar;
    }

    public void handleEmptyGrammarTransformations(List<String> grammarTransformations) {
        if (grammarTransformations.isEmpty()) {
            grammarTransformations.add(ResourcesContext.getString(R.string.no_convert_this_step));
        }
    }

    public List<String> getOrderVariables() {
        return orderVariables;
    }

    public void setOrderVariables(List<String> orderVariables) {
        this.orderVariables = orderVariables;
    }

    String getGrammarTransformation(Grammar grammar, Set<Rule>
            ruleWithProblems, Set<Rule> newRules) {
        StringBuilder txtGrammar = new StringBuilder();
        txtGrammar.append(ResourcesContext.getString(R.string.replace_rues_acd));
        txtGrammar.append(grammar.toStringHtmlWithColorInSpecialRules(ruleWithProblems,
                HtmlColors.RED));
        insertVariablesInOrderVariables(newRules);
        txtGrammar.append(ResourcesContext.getString(R.string.new_grammar));
        Grammar newGrammar = trasformGrammar(grammar, ruleWithProblems, newRules);
        txtGrammar.append(newGrammar.toStringHtmlWithColorInSpecialRules(newRules,
                HtmlColors.BLUE));
        txtGrammar.append(HtmlTags.BREAK_LINE);
        return txtGrammar.toString();
    }

    String getGrammarTransformationTerra(Grammar grammar, Set<Rule>
            ruleWithProblems, Set<Rule> newRules, boolean
                                                 imediateLeftRecursive) {
        StringBuilder txtGrammar = new StringBuilder();
        if (imediateLeftRecursive) {
            txtGrammar.append(ResourcesContext.getString(R.string.remove_recursion_acd));
        } else {
            txtGrammar.append(ResourcesContext.getString(R.string.remove_indirect_left_recursion_terra));
        }
        txtGrammar.append(grammar.toStringHtmlWithColorInSpecialRules(ruleWithProblems,
                HtmlColors.RED));
        insertVariablesInOrderVariables(newRules);
        txtGrammar.append(ResourcesContext.getString(R.string.new_grammar));
        Grammar newGrammar = trasformGrammar(grammar, ruleWithProblems, newRules);
        txtGrammar.append(newGrammar.toStringHtmlWithColorInSpecialRules(newRules,
                HtmlColors.BLUE));
        txtGrammar.append(HtmlTags.BREAK_LINE);
        return txtGrammar.toString();
    }

    private void insertVariablesInOrderVariables(Set<Rule> newRules) {
        for (Rule rule : newRules) {
            if (!orderVariables.contains(rule.getLeftSide())) {
                orderVariables.add(rule.getLeftSide());
            }
        }
    }

    Grammar trasformGrammar(Grammar grammar, Set<Rule> ruleWithProblems,
                            Set<Rule> newRules) {
        Grammar gc = (Grammar) grammar.clone();
        gc.removeRules(ruleWithProblems);
        gc.insertRules(newRules);
        gc.insertVariables(orderVariables);
        return gc;
    }

}
