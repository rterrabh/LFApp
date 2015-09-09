package vo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/**
 * Created by juventino on 26/08/15.
 */
public class AcademicSupport {

    private String comments;
    private boolean situation;
    private Map<Integer, String> foundProblems;
    private String result;
    private String solutionDescription;
    private Set<Rule> insertedRules;
    private Set<Rule> irregularRules;
    private ArrayList<Set<String>> firstSet;
    private ArrayList<Set<String>> secondSet;
    private ArrayList<Set<String>> thirdSet;


    public AcademicSupport() {
        this.comments = new String();
        this.situation = false;
        this.foundProblems = new HashMap<>();
        this.result = new String();
        this.solutionDescription = new String();
        this.insertedRules = new HashSet<>();
        this.irregularRules = new HashSet<>();
        this.firstSet = new ArrayList<>();
        this.secondSet = new ArrayList<>();
        this.thirdSet = new ArrayList<>();
    }

    public AcademicSupport(String comments, boolean situation, HashMap foundProblems, Grammar g, String solutionDescription, Set<Rule> insertedRules, Set<Rule> irregularRules,
                            ArrayList<Set<String>> firtSet, ArrayList<Set<String>> secondSet, ArrayList<Set<String>> thirdSet) {
        this.comments = comments;
        this.situation = situation;
        this.foundProblems = foundProblems;
        setResult(g);
        this.solutionDescription = solutionDescription;
        this.insertedRules = insertedRules;
        this.irregularRules = irregularRules;
        this.firstSet = firtSet;
        this.secondSet = secondSet;
        this.thirdSet = thirdSet;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean getSituation() {
        return situation;
    }

    public void setSituation(boolean situation) {
        this.situation = situation;
    }

    public Map<Integer, String> getFoundProblems() {
        return foundProblems;
    }

    public void setFoundProblems(Map<Integer, String> foundProblems) {
        this.foundProblems = foundProblems;
    }

    public String getResult() {
        return result;
    }

    public void setResult(Grammar g) {
        this.result = formatResultGrammar(g);
    }

    public String getSolutionDescription() {
        return solutionDescription;
    }

    public void setSolutionDescription(String solutionDescription) {
        this.solutionDescription = solutionDescription;
    }

    public Set<Rule> getInsertedRules() {
        return insertedRules;
    }

    public void setInsertedRules(Set<Rule> insertedRules) {
        this.insertedRules = insertedRules;
    }

    public String formatResultGrammar(final Grammar g) {
        StringBuilder txtGrammar = new StringBuilder(g.getInitialSymbol() + " ->");
        for (Rule element : g.getRules()) {
            if (element.getLeftSide().equals(g.getInitialSymbol())) {
                txtGrammar.append(" " + element.getRightSide() + " |");
            }
        }
        txtGrammar.deleteCharAt(txtGrammar.length() - 1);
        txtGrammar.append("\n");
        for (String variable : g.getVariables()) {
            if (!variable.equals(g.getInitialSymbol())) {
                txtGrammar.append(variable + " ->");
                for (Rule element : g.getRules()) {
                    if (variable.equals(element.getLeftSide())) {
                        txtGrammar.append(" " + element.getRightSide() + " |");
                    }
                }
                txtGrammar.deleteCharAt(txtGrammar.length() - 1);
                txtGrammar.append("\n");
            }
        }
        return txtGrammar.toString();
    }

    public void insertNewRule(Rule newRule) {
        this.insertedRules.add(newRule);
    }

    public void insertIrregularRule(Rule irregularRule) {
        this.irregularRules.add(irregularRule);
    }

    public Set<Rule> getIrregularRules() {
        return irregularRules;
    }

    public void setIrregularRules(Set<Rule> irregularRules) {
        this.irregularRules = irregularRules;
    }

    public ArrayList<Set<String>> getFirstSet() {
        return firstSet;
    }

    public void setFirstSet(ArrayList<Set<String>> firstSet) {
        this.firstSet = firstSet;
    }

    public ArrayList<Set<String>> getSecondSet() {
        return secondSet;
    }

    public void setSecondSet(ArrayList<Set<String>> secondSet) {
        this.secondSet = secondSet;
    }


    public ArrayList<Set<String>> getThirdSet() {
        return thirdSet;
    }

    public void setThirdSet(ArrayList<Set<String>> thirdSet) {
        this.thirdSet = thirdSet;
    }

    public void insertOnFirstSet(Set<String>  currentSet, String decision) {
        if (decision.equals("Lambda") || decision.equals("TERM") || decision.equals("REACH")) {
            if (!verifySet(firstSet, currentSet)) {
                Set<String> aux = new HashSet<>();
                aux.addAll(currentSet);
                firstSet.add(aux);
            }
        } else if (decision.equals("Chain")) {
            Set<String> aux = new HashSet<>();
            aux.addAll(currentSet);
           firstSet.add(aux);
        }
    }

    public void insertOnSecondSet(Set<String> currentSet, String decision) {
        if (decision.equals("Lambda") || decision.equals("TERM") || decision.equals("REACH")) {
            if (!verifySet(secondSet, currentSet)) {
                Set<String> aux = new HashSet<>();
                aux.addAll(currentSet);
                secondSet.add(aux);
            }
        } else if (decision.equals("Chain")) {
            Set<String> aux = new HashSet<>();
            aux.addAll(currentSet);
            secondSet.add(aux);
        }
    }

    public void insertOnThirdSet(Set<String> currentSet, String decision) {
        if (decision.equals("Lambda") || decision.equals("TERM") || decision.equals("REACH")) {
            if (!verifySet(thirdSet, currentSet)) {
                Set<String> aux = new HashSet<>();
                aux.addAll(currentSet);
                thirdSet.add(aux);
            }
        } else if (decision.equals("Chain")) {
            Set<String> aux = new HashSet<>();
            aux.addAll(currentSet);
            thirdSet.add(aux);
        }
        String teste = thirdSet.toString();
    }

    private boolean verifySet(ArrayList<Set<String>> setOfVariables, Set<String> currentSet) {
        boolean aux = false;
        if (setOfVariables.size() != 0) {
            for (int i = 0; i < setOfVariables.size() && !false; i++) {
                Set<String> auxSet = setOfVariables.get(setOfVariables.size() - 1);
                if (auxSet.equals(currentSet)) {
                    aux = true;
                }
            }
        }
        return false;
    }
}


