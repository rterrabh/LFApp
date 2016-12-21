package com.ufla.lfapp.vo.grammar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by root on 05/07/16.
 */
public class AcademicSupportFNG extends AcademicSupportRemoveLeftRecursiveAbstract {

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

    public List<Set<Rule>> getNewRulesStage2() {
        return newRulesStage2;
    }

    public List<Set<Rule>> getDeleteRulesStage2() {
        return deleteRulesStage2;
    }

    public List<Set<Rule>> getNewRulesStage3() {
        return newRulesStage3;
    }

    public List<Set<Rule>> getDeleteRulesStage3() {
        return deleteRulesStage3;
    }

    private Grammar getGrammarAfterStage2() {
        Grammar gc = (Grammar) originalGrammar.clone();
        for(int i = 0; i < newRulesStage2.size(); i++) {
            gc = trasformGrammar(gc, deleteRulesStage2.get(i), newRulesStage2.get(i));
        }
        return gc;
    }

    public List<String> getGrammarTransformationsStage2() {
        List<String> grammarTransformationsStage2 = new ArrayList<>();
        Grammar gc = (Grammar) originalGrammar.clone();
        for(int i = 0; i < newRulesStage2.size(); i++) {
            grammarTransformationsStage2.add("1."+(grammarTransformationsStage2
                    .size()+1)+" "+getGrammarTransformation(gc,
                    deleteRulesStage2.get(i), newRulesStage2.get(i), false));
            gc = trasformGrammar(gc, deleteRulesStage2.get(i), newRulesStage2.get(i));
        }
        handleEmptyGrammarTransformations(grammarTransformationsStage2);
        return grammarTransformationsStage2;
    }

    public List<String> getGrammarTransformationsStage3() {
        List<String> grammarTransformationsStage3 = new ArrayList<>();
        Grammar gc = getGrammarAfterStage2();
        for(int i = 0; i < newRulesStage3.size(); i++) {
            grammarTransformationsStage3.add("2."+(grammarTransformationsStage3
                    .size()+1)+" "+getGrammarTransformation(gc,
                    deleteRulesStage3.get(i), newRulesStage3.get(i), false));
            gc = trasformGrammar(gc, deleteRulesStage3.get(i), newRulesStage3.get(i));
        }
        handleEmptyGrammarTransformations(grammarTransformationsStage3);
        return grammarTransformationsStage3;
    }

    public void addGrammarTransformationStage2(Set<Rule> ruleWithProblems, Set<Rule> newRules) {
        deleteRulesStage2.add(new HashSet<>(ruleWithProblems));
        newRulesStage2.add(new HashSet<>(newRules));
    }

    public void addGrammarTransformationStage3(Set<Rule> ruleWithProblems, Set<Rule> newRules) {
        deleteRulesStage3.add(new HashSet<>(ruleWithProblems));
        newRulesStage3.add(new HashSet<>(newRules));
    }


}
