package com.ufla.lfapp.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by carlos on 30/07/16.
 */
public abstract class AcademicSupportRemoveLeftRecursiveAbstract {

    protected List<String> orderVariables;
    protected Grammar originalGrammar;

    public AcademicSupportRemoveLeftRecursiveAbstract() {
        this.orderVariables = new ArrayList<>();
    }

    public void setOriginalGrammar(Grammar originalGrammar) {
        this.originalGrammar = originalGrammar;
    }

    protected void handleEmptyGrammarTransformations(List<String> grammarTransformations) {
        if(grammarTransformations.isEmpty()) {
            grammarTransformations.add("Não há transformações à serem " +
                    "realizadas nesta etapa!<br/>");
        }
    }

    public List<String> getOrderVariables() {
        return orderVariables;
    }

    public void setOrderVariables(List<String> orderVariables) {
        this.orderVariables = orderVariables;
    }

    protected String getGrammarTransformation(Grammar grammar, Set<Rule>
            ruleWithProblems, Set<Rule> newRules, boolean
                                                    imediateLeftRecursive) {
        StringBuilder txtGrammar = new StringBuilder();
        if(imediateLeftRecursive) {
            txtGrammar.append("Remover recursão direta à esquerda:<br/>");
        } else {
            txtGrammar.append("Substituir regras (A<sub>i</sub> -> " +
                    "A<sub>j</sub>&#947;):<br/>");
        }
        txtGrammar.append(insertGrammarWithModifiedColorRules(grammar,
                ruleWithProblems, "red"));
        insertVariablesInOrderVariables(newRules);
        txtGrammar.append("Nova gramática:<br/>");
        txtGrammar.append(insertGrammarWithModifiedColorRules(trasformGrammar
                (grammar, ruleWithProblems, newRules), newRules, "blue"));
        txtGrammar.append("<br/>");
        return txtGrammar.toString();
    }

    protected void insertVariablesInOrderVariables(Set<Rule> newRules) {
        for(Rule rule : newRules) {
            if(!orderVariables.contains(rule.getLeftSide())) {
                orderVariables.add(rule.getLeftSide());
            }
        }
    }

    protected Grammar trasformGrammar(Grammar grammar, Set<Rule> ruleWithProblems,
                                    Set<Rule> newRules) {
        Grammar gc = (Grammar) grammar.clone();
        gc.removeRules(ruleWithProblems);
        gc.insertRules(newRules);
        gc.insertVariables(orderVariables);
        return gc;
    }

    protected String insertGrammarWithModifiedColorRules
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
