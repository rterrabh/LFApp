package com.ufla.lfapp.core.grammar;

import com.ufla.lfapp.R;
import com.ufla.lfapp.utils.ResourcesContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by carlos on 30/07/16.
 */
public abstract class AcademicSupportRemoveLeftRecursiveAbstract {

    private List<String> orderVariables;
    Grammar originalGrammar;

    AcademicSupportRemoveLeftRecursiveAbstract() {
        this.orderVariables = new ArrayList<>();
    }

    void setOriginalGrammar(Grammar originalGrammar) {
        this.originalGrammar = originalGrammar;
    }

    void handleEmptyGrammarTransformations(List<String> grammarTransformations) {
        if(grammarTransformations.isEmpty()) {
            grammarTransformations.add(ResourcesContext.getString(R.string.no_convert_this_step));
        }
    }

    List<String> getOrderVariables() {
        return orderVariables;
    }

    void setOrderVariables(List<String> orderVariables) {
        this.orderVariables = orderVariables;
    }

    String getGrammarTransformation(Grammar grammar, Set<Rule>
            ruleWithProblems, Set<Rule> newRules, boolean
                                                    imediateLeftRecursive) {
        StringBuilder txtGrammar = new StringBuilder();
        if(imediateLeftRecursive) {
            txtGrammar.append(ResourcesContext.getString(R.string.remove_recursion_acd));
        } else {
            txtGrammar.append(ResourcesContext.getString(R.string.replace_rues_acd));
        }
        txtGrammar.append(insertGrammarWithModifiedColorRules(grammar,
                ruleWithProblems, "red"));
        insertVariablesInOrderVariables(newRules);
        txtGrammar.append(ResourcesContext.getString(R.string.new_grammar));
        txtGrammar.append(insertGrammarWithModifiedColorRules(trasformGrammar
                (grammar, ruleWithProblems, newRules), newRules, "blue"));
        txtGrammar.append("<br/>");
        return txtGrammar.toString();
    }

    private void insertVariablesInOrderVariables(Set<Rule> newRules) {
        for(Rule rule : newRules) {
            if(!orderVariables.contains(rule.getLeftSide())) {
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

    private String insertGrammarWithModifiedColorRules
            (Grammar grammar, Set<Rule> ruleForModifiedColor, String color) {
        StringBuilder txtGrammar = new StringBuilder();
        for (String variable : orderVariables) {
            if (variable.length() > 1) {
                txtGrammar.append(variable.charAt(0));
                txtGrammar.append("<sub>").append(variable.substring(1))
                        .append("</sub>");
            } else {
                txtGrammar.append(variable);
            }
            txtGrammar.append(" ->");
            for(Rule rule : grammar.getRules(variable)) {
                txtGrammar.append(' ');
                if(ruleForModifiedColor.contains(rule)) {
                    txtGrammar.append("<font color=\"").append(color)
                            .append("\">").append(rule
                            .getRightSideToHtml()).append("</font>");
                } else {
                    txtGrammar.append(rule.getRightSide());
                }
                txtGrammar.append(" |");
            }
            txtGrammar.deleteCharAt(txtGrammar.length() - 1);
            txtGrammar.append("<br>");
        }
        return txtGrammar.toString();

    }
}
