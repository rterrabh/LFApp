package com.ufla.lfapp.core.grammar;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by root on 05/07/16.
 */
public class AcademicSupportFNG extends AbstractAcademicSupportRLR {

    private List<Set<Rule>> newRulesStage2;
    private List<Set<Rule>> deleteRulesStage2;
    private List<Set<Rule>> newRulesStage3;
    private List<Set<Rule>> deleteRulesStage3;

    public AcademicSupportFNG() {
        newRulesStage2 = new ArrayList<>();
        deleteRulesStage2 = new ArrayList<>();
        newRulesStage3 = new ArrayList<>();
        deleteRulesStage3 = new ArrayList<>();
    }

    /**
     * Recupera a gramática após as transformações realizadas no estágio 2 do algoritmo da FNG.
     *
     * @return gramática após as transformações realizadas no estágio 2 do algoritmo da FNG
     */
    private Grammar getGrammarAfterStage2() {
        Grammar grammarClone = (Grammar) originalGrammar.clone();
        for (int i = 0; i < newRulesStage2.size(); i++) {
            grammarClone = trasformGrammar(grammarClone, deleteRulesStage2.get(i),
                    newRulesStage2.get(i));
        }
        return grammarClone;
    }

    /**
     * Recupera a lista de transformações realizadas no estágio 2 do algoritmo da FNG.
     *
     * @return lista de transformações realizadas no estágio 2 do algoritmo da FNG
     */
    public List<String> getGrammarTransformationsStage2() {
        List<String> grammarTransformationsStage2 = new ArrayList<>();
        Grammar grammarClone = (Grammar) originalGrammar.clone();
        for (int i = 0; i < newRulesStage2.size(); i++) {
            grammarTransformationsStage2.add("1." + (grammarTransformationsStage2
                    .size() + 1) + " " + getGrammarTransformation(grammarClone,
                    deleteRulesStage2.get(i), newRulesStage2.get(i)));
            grammarClone = trasformGrammar(grammarClone, deleteRulesStage2.get(i),
                    newRulesStage2.get(i));
        }
        handleEmptyGrammarTransformations(grammarTransformationsStage2);
        return grammarTransformationsStage2;
    }

    /**
     * Recupera a lista de transformações realizadas no estágio 3 do algoritmo da FNG.
     *
     * @return lista de transformações realizadas no estágio 3 do algoritmo da FNG
     */
    public List<String> getGrammarTransformationsStage3() {
        List<String> grammarTransformationsStage3 = new ArrayList<>();
        Grammar grammarAfterStage2 = getGrammarAfterStage2();
        for (int i = 0; i < newRulesStage3.size(); i++) {
            grammarTransformationsStage3.add("2." + (grammarTransformationsStage3
                    .size() + 1) + " " + getGrammarTransformation(grammarAfterStage2,
                    deleteRulesStage3.get(i), newRulesStage3.get(i)));
            grammarAfterStage2 = trasformGrammar(grammarAfterStage2, deleteRulesStage3.get(i),
                    newRulesStage3.get(i));
        }
        handleEmptyGrammarTransformations(grammarTransformationsStage3);
        return grammarTransformationsStage3;
    }

    /**
     * Adiciona um os conjuntos de regras problemáticas (deletadas) e novas regras do estágio 2 do
     * algoritmo da FNG.
     *
     * @param ruleWithProblems conjunto de regras problemáticas (deletadas) do estágio 2 do algoritmo da FNG
     * @param newRules         conjunto de novas regras do estágio 2 do algoritmo da FNG.
     */
    public void addGrammarTransformationStage2(Set<Rule> ruleWithProblems, Set<Rule> newRules) {
        deleteRulesStage2.add(new LinkedHashSet<>(ruleWithProblems));
        newRulesStage2.add(new LinkedHashSet<>(newRules));
    }

    /**
     * Adiciona um os conjuntos de regras problemáticas (deletadas) e novas regras do estágio 3 do
     * algoritmo da FNG.
     *
     * @param ruleWithProblems conjunto de regras problemáticas (deletadas) do estágio 3 do algoritmo da FNG
     * @param newRules         conjunto de novas regras do estágio 3 do algoritmo da FNG.
     */
    public void addGrammarTransformationStage3(Set<Rule> ruleWithProblems, Set<Rule> newRules) {
        deleteRulesStage3.add(new LinkedHashSet<>(ruleWithProblems));
        newRulesStage3.add(new LinkedHashSet<>(newRules));
    }


}
