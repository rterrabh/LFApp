package com.ufla.lfapp.core.grammar;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by juventino on 26/08/15.
 */
public class AcademicSupport {

    private String comments;
    private boolean situation;
    private Map<Integer, String> foundProblems;
    private Grammar grammar;
    private String result;
    private String solutionDescription;
    private Set<Rule> insertedRules;
    private Set<Rule> irregularRules;
    private List<Set<String>> firstSet;
    private List<Set<String>> secondSet;
    private List<Set<String>> thirdSet;


    public AcademicSupport() {
        this("", false, new LinkedHashMap<Integer, String>(), "", "",
                new LinkedHashSet<Rule>(), new LinkedHashSet<Rule>(),
                new ArrayList<Set<String>>(), new ArrayList<Set<String>>(),
                new ArrayList<Set<String>>(), new Grammar());
    }

    public AcademicSupport(String comments, boolean situation,
                           Map<Integer, String> foundProblems,
                           Grammar resultGrammar, String solutionDescription,
                           Set<Rule> insertedRules,
                           Set<Rule> irregularRules, List<Set<String>> firtSet,
                           List<Set<String>> secondSet,
                           List<Set<String>> thirdSet,
                           Grammar grammar) {
        this.comments = comments;
        this.situation = situation;
        this.foundProblems = foundProblems;
        this.grammar = grammar;
        setResult(resultGrammar);
        this.solutionDescription = solutionDescription;
        this.insertedRules = insertedRules;
        this.irregularRules = irregularRules;
        this.firstSet = firtSet;
        this.secondSet = secondSet;
        this.thirdSet = thirdSet;
    }

    //Construtor base
    public AcademicSupport(String comments, boolean situation,
                           Map<Integer, String> foundProblems,
                           String result, String solutionDescription,
                           Set<Rule> insertedRules,
                           Set<Rule> irregularRules, List<Set<String>> firtSet,
                           List<Set<String>> secondSet,
                           List<Set<String>> thirdSet,
                           Grammar grammar) {
        this.comments = comments;
        this.situation = situation;
        this.foundProblems = foundProblems;
        this.grammar = grammar;
        this.result = result;
        this.solutionDescription = solutionDescription;
        this.insertedRules = insertedRules;
        this.irregularRules = irregularRules;
        this.firstSet = firtSet;
        this.secondSet = secondSet;
        this.thirdSet = thirdSet;
    }

    /**
     * Formata uma gramática em texto com para visualização.
     *
     * @param grammar gramática a ser formatada.
     * @return texto com gramática formatada
     */
    public String formatResultGrammar(final Grammar grammar) {
        StringBuilder txtGrammar = new StringBuilder(formatVariableRules(grammar.getInitialSymbol(),
                grammar));
        for (String variable : grammar.getVariables()) {
            if (variable.equals(grammar.getInitialSymbol())) {
                continue;
            }
            txtGrammar.append(formatVariableRules(variable, grammar));
        }
        return txtGrammar.toString();
    }

    /**
     * Formata uma variável e suas regras.
     *
     * @param variable variável a ser formatada
     * @param grammar  gramática da variável a ser formatada
     * @return variável e regras formatadas
     */
    private String formatVariableRules(String variable, final Grammar grammar) {
        StringBuilder formatedRules = new StringBuilder();
        System.out.println("VAR -> " + variable);
        formatedRules.append(formatElement(variable)).append(" →");
        for (Rule element : grammar.getRules(variable)) {
            System.out.println(element.toString());
            formatedRules.append(" ").append(formatElement
                    (element.getRightSide())).append(" |");
        }
        formatedRules.deleteCharAt(formatedRules.length() - 1).deleteCharAt(formatedRules.length()
                - 1).append("<br>");
        return formatedRules.toString();
    }

    /**
     * Formata um elemento de uma regra gramatical, caso este elemento for uma variável e tenha
     * digítos coloca os dígitos dentro da tag <sub>.
     *
     * @param element elemento a ser formatado
     * @return elemento formatado
     */
    private String formatElement(String element) {
        if (element.length() == 1) {
            return element;
        }
        StringBuilder formatedElement = new StringBuilder();
        boolean isDigit = false;
        for (int i = 0; i < element.length(); i++) {
            if (Character.isDigit(element.charAt(i)) && !isDigit) {
                formatedElement.append("<sub>").append(element.charAt(i));
                isDigit = true;
            } else if (Character.isDigit(element.charAt(i)) && isDigit) {
                formatedElement.append(element.charAt(i));
            } else if (!Character.isDigit(element.charAt(i)) && isDigit) {
                formatedElement.append("</sub>").append(element.charAt(i));
                isDigit = false;
            } else {
                formatedElement.append(element.charAt(i));
            }
        }
        if (isDigit) {
            formatedElement.append("</sub>");
        }
        return formatedElement.toString();
    }

    /**
     * Insere um nova regra no conjunto de novas regras.
     *
     * @param newRule nova regra a ser inserida
     */
    public void insertNewRule(Rule newRule) {
        this.insertedRules.add(newRule);
    }

    /**
     * Insere um regra irregular no conjunto de regras irregulares.
     *
     * @param irregularRule regra irregular a ser inserida
     */
    public void insertIrregularRule(Rule irregularRule) {
        this.irregularRules.add(irregularRule);
    }

    /**
     * Insere um conjunto na primeira lista de conjuntos.
     *
     * @param currentSet conjunto a ser inserido
     * @param decision
     */
    public void insertOnFirstSet(Set<String> currentSet, String decision) {
        firstSet.add(new LinkedHashSet<>(currentSet));
    }

    /**
     * Insere um conjunto na segunda lista de conjuntos.
     *
     * @param currentSet conjunto a ser inserido
     * @param decision
     */
    public void insertOnSecondSet(Set<String> currentSet, String decision) {
        secondSet.add(new LinkedHashSet<>(currentSet));
    }

    /**
     * Insere um conjunto na terceira lista de conjuntos.
     *
     * @param currentSet conjunto a ser inserido
     * @param decision
     */
    public void insertOnThirdSet(Set<String> currentSet, String decision) {
        thirdSet.add(new LinkedHashSet<>(currentSet));
    }


    // Métodos acessores

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

    public Grammar getGrammar() {
        return grammar;
    }

    public void setGrammar(Grammar grammar) {
        this.grammar = (Grammar) grammar.clone();
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

    public Set<Rule> getIrregularRules() {
        return irregularRules;
    }

    public void setIrregularRules(Set<Rule> irregularRules) {
        this.irregularRules = irregularRules;
    }

    public List<Set<String>> getFirstSet() {
        return firstSet;
    }

    public void setFirstSet(List<Set<String>> firstSet) {
        this.firstSet = firstSet;
    }

    public List<Set<String>> getSecondSet() {
        return secondSet;
    }

    public void setSecondSet(List<Set<String>> secondSet) {
        this.secondSet = secondSet;
    }

    public List<Set<String>> getThirdSet() {
        return thirdSet;
    }

    public void setThirdSet(List<Set<String>> thirdSet) {
        this.thirdSet = thirdSet;
    }

}


