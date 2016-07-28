package com.ufla.lfapp.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AcademicSupportForRemoveLeftRecursion {

    private List<String> orderVariables;
    private List<String> grammarTransformationsStage1;
    private List<String> grammarTransformationsStage2;
    private List<String> grammarTransformationsStage3;



    public AcademicSupportForRemoveLeftRecursion() {
        grammarTransformationsStage1 = new ArrayList<>();
        grammarTransformationsStage2 = new ArrayList<>();
        grammarTransformationsStage3 = new ArrayList<>();
    }

    //get methods
    public List<String> getGrammarTransformationsStage1() {
        return grammarTransformationsStage1;
    }

    public List<String> getGrammarTransformationsStage2() {
        return grammarTransformationsStage2;
    }

    public List<String> getGrammarTransformationsStage3() {
        return grammarTransformationsStage3;
    }

    public List<String> getOrderVariables() {
        return orderVariables;
    }

    public void setOrderVariables(List<String> orderVariables) {
        this.orderVariables = orderVariables;
    }

    public void verifyGrammarTransformations() {
        if(grammarTransformationsStage1.isEmpty()) {
            grammarTransformationsStage1.add("Não há transformações à serem " +
                    "realizadas nesta etapa!<br/>");
        }
        if(grammarTransformationsStage2.isEmpty()) {
            grammarTransformationsStage2.add("Não há transformações à serem " +
                    "realizadas nesta etapa!<br/>");
        }
        if(grammarTransformationsStage3.isEmpty()) {
            grammarTransformationsStage3.add("Não há transformações à serem " +
                    "realizadas nesta etapa!<br/>");
        }
    }

    //add methods
    public void addGrammarTransformationStage1(Grammar grammar, Set<Rule>
            ruleWithProblems, Set<Rule> newRules, boolean
            imediateLeftRecursive) {
        grammarTransformationsStage1.add("2."+(grammarTransformationsStage1
                .size()+1)+" "+getGrammarTransformation(grammar,
                ruleWithProblems, newRules, imediateLeftRecursive));
    }

    public void addGrammarTransformationStage2(Grammar grammar, Set<Rule>
            ruleWithProblems, Set<Rule> newRules) {
        grammarTransformationsStage2.add("3."+(grammarTransformationsStage2
                .size()+1)+" "+getGrammarTransformation(grammar,
                ruleWithProblems, newRules, false));
    }

    public void addGrammarTransformationStage3(Grammar grammar, Set<Rule>
            ruleWithProblems, Set<Rule> newRules) {
        grammarTransformationsStage3.add("4."+(grammarTransformationsStage3
                .size()+1)+" "+getGrammarTransformation(grammar,
                ruleWithProblems, newRules, false));
    }

    private String getGrammarTransformation(Grammar grammar, Set<Rule>
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
        txtGrammar.append("Novas regras:<br/>");
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

    private Grammar trasformGrammar(Grammar grammar, Set<Rule>
            ruleWithProblems, Set<Rule> newRules) {
        Grammar gc = (Grammar) grammar.clone();
        gc.removeRules(ruleWithProblems);
        gc.insertRules(newRules);
        gc.insertVariables(orderVariables);
        return gc;
    }

    private String insertGrammarWithModifiedColorRules(Grammar grammar,
                                                       Set<Rule>
            ruleForModifiedColor, String color) {
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
