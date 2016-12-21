package com.ufla.lfapp.vo.grammar;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AcademicSupportForRemoveLeftRecursion
        extends AcademicSupportRemoveLeftRecursiveAbstract {

    private Deque<String> orderVariablesFNG;
    private List<Set<Rule>> newRulesStage1;
    private List<Set<Rule>> deleteRulesStage1;
    private List<Boolean> isImediateLeftRecursiveStage1;


    public AcademicSupportForRemoveLeftRecursion() {
        newRulesStage1 = new ArrayList<>();
        deleteRulesStage1 = new ArrayList<>();
        isImediateLeftRecursiveStage1 = new ArrayList<>();
        orderVariablesFNG = new LinkedList<>();
    }


    //get methods
    public List<Set<Rule>> getNewRulesStage1() {
        return newRulesStage1;
    }

    public List<Set<Rule>> getDeleteRulesStage1() {
        return deleteRulesStage1;
    }

    public List<Boolean> getIsImediateLeftRecursiveStage1() {
        return isImediateLeftRecursiveStage1;
    }

    public Deque<String> getOrderVariablesFNG() {
        return orderVariablesFNG;
    }

    public void setOrderVariablesFNG(Deque<String> orderVariablesFNG) {
        this.orderVariablesFNG = orderVariablesFNG;
    }

    public List<String> getGrammarTransformationsStage1() {
        List<String> grammarTransformationsStage1 = new ArrayList<>();
        Grammar gc = (Grammar) originalGrammar.clone();
        for(int i = 0; i < newRulesStage1.size(); i++) {
            grammarTransformationsStage1.add("2."+(grammarTransformationsStage1
                    .size()+1)+" "+getGrammarTransformation(gc,
                    deleteRulesStage1.get(i), newRulesStage1.get(i),
                    isImediateLeftRecursiveStage1.get(i)));
            gc = trasformGrammar(gc, deleteRulesStage1.get(i), newRulesStage1.get(i));
        }
        handleEmptyGrammarTransformations(grammarTransformationsStage1);
        return grammarTransformationsStage1;
    }

    //add methods
    public void addGrammarTransformationStage1(Set<Rule> ruleWithProblems, Set<Rule> newRules,
                                               boolean imediateLeftRecursive) {
        deleteRulesStage1.add(new HashSet<>(ruleWithProblems));
        newRulesStage1.add(new HashSet<>(newRules));
        isImediateLeftRecursiveStage1.add(imediateLeftRecursive);
    }

}
