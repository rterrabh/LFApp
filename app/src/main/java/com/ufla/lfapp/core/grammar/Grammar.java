package com.ufla.lfapp.core.grammar;

import android.text.Html;
import android.text.Spanned;

import com.ufla.lfapp.R;
import com.ufla.lfapp.utils.Symbols;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static com.ufla.lfapp.utils.ResourcesContext.*;

public class Grammar implements Cloneable {

    public static final String LAMBDA = "λ";
    //public static final String RULE_SEPARATOR = "|";
    //public static final String RULE_PRODUCTION = "->";
    public static final String CHOMSKY_PREFIX = "T";
    public static final String RECURSIVE_REMOVAL_PREFIX = "R";

    // attributes
    private Set<String> variables;
    private Set<String> terminals;
    private String initialSymbol;
    private Set<Rule> rules;


    protected Grammar() {
        super();
        this.variables = new LinkedHashSet<>();
        this.terminals = new LinkedHashSet<>();
        this.rules = new LinkedHashSet<>();
    }

    //Construtor base
    public Grammar(Set<String> variables, Set<String> terminals, String initialSymbol,
                   Set<Rule> rules) {
        super();
        setVariables(variables);
        setTerminals(terminals);
        setInitialSymbol(initialSymbol);
        setRules(rules);
    }

    public Grammar(String[] variables, String[] terminals,
                   String initialSymbol, String[] rules) {
        this(new LinkedHashSet<>(Arrays.asList(variables)),
                new LinkedHashSet<>(Arrays.asList(terminals)),
                initialSymbol, new LinkedHashSet<Rule>());
        Rule r = new Rule();
        String[] auxRule;
        for (String x : rules) {
            auxRule = x.split("->");
            r.setLeftSide(auxRule[0].trim());
            String[] rulesOnTheRightSide = auxRule[1].split("[|]");
            for (String production : rulesOnTheRightSide) {
                production = production.trim();
                if (!production.isEmpty()) {
                    r.setRightSide(production);
                    this.rules.add(new Rule(r));
                }
            }
        }
    }

    public Grammar(String txt) {
        this.variables = GrammarParser.extractVariablesFromFull(txt);
        this.terminals = GrammarParser.extractTerminalsFromFull(txt);
        this.rules = GrammarParser.extractRulesFromFull(txt);
        setInitialSymbol(GrammarParser.extractInitialSymbolFromFull(txt));
    }

    // methods
    public Set<String> getVariables() {
        return new LinkedHashSet(variables);
    }

    public Set<String> getTerminals() {
        return  new LinkedHashSet(terminals);
    }

    public String getInitialSymbol() {
        return initialSymbol;
    }

    public Set<Rule> getRules() {
        return new LinkedHashSet(rules);
    }

    public void setVariables(Set<String> set) {
        this.variables = new LinkedHashSet<>(set);
    }

    public void setTerminals(Set<String> set) {
        this.terminals = new LinkedHashSet<>(set);
    }

    public void setInitialSymbol(String string) {
        this.initialSymbol = string;
    }

    public void setRules(Set<Rule> set) {
        this.rules = new LinkedHashSet<>();
        for (Rule r : set) {
            this.rules.add((Rule) r.clone());
        }
    }

    public void insertVariable(String newVariable) {
        this.variables.add(newVariable);
    }

    public boolean containsVariable(String variable) {
        return variables.contains(variable);
    }

    public void removeVariable(String variable) {
        this.variables.remove(variable);
    }

    public void insertTerminal(String newTerminal) {
        this.terminals.add(newTerminal);
    }

    public void removeTerminal(String terminal) {
        terminals.remove(terminal);
    }

    public void insertRule(String leftSide, String rightSide) {
        this.rules.add(new Rule(leftSide, rightSide));
    }

    public void insertVariables(Collection<String> variables) {
        this.variables.addAll(variables);
    }

    public void insertRules(Collection<Rule> rules) {
        this.rules.addAll(rules);
    }

    public void removeRules(Collection<Rule> rules) {
        this.rules.removeAll(rules);
    }

    public void insertRule(Rule r) {
        rules.add(r);
    }

    public void removeRule(String leftSide, String rightSide) {
        this.rules.remove(new Rule(leftSide, rightSide));
    }

    @Override
    public Object clone() {
        return new Grammar(this.variables, this.terminals, this.initialSymbol, this.rules);
    }

    // algorithms

    /** Verifica se a gramática possui recursão no símbolo inicial.
     * @return true se a gramática possui recursão no símbolo inicial, caso contrário, false.
     */
    public boolean initialSymbolIsRecursive() {
        for (Rule rule : getRules()) {
            if (rule.getRightSide().contains(initialSymbol)) {
                return true;
            }
        }
        return false;
    }

    /** Verifica se a gramática possui recursão no símbolo inicial.
     * @return true se a gramática possui recursão no símbolo inicial, caso contrário, false.
     */
    public Set<Rule> getRulesWithRecursiveOnInitialSymbol() {
        Set<Rule> rules = new LinkedHashSet<>();
        for (Rule rule : getRules()) {
            if (rule.getRightSide().contains(initialSymbol)) {
                rules.add((Rule) rule.clone());
            }
        }
        return rules;
    }

    /**
     * @param g gramática livre de contexto
     * @return : gramática livre de contexto sem recursão no símbolo inicial
     */
    public Grammar getGrammarWithInitialSymbolNotRecursive(final Grammar g,
                                                           final AcademicSupport academicSupport) {
        Grammar gc = (Grammar) g.clone();
        String align = "justify";
        String comments =
                new StringBuilder("<p align=")
                        .append(align)
                        .append('>')
                        .append(getString(R.string.initial_symbol_not_recursive))
                        .append(gc.getInitialSymbol())
                        .append(" ⇒<sup>∗</sup> αSβ.<br><br>")
                        .toString();
        Map<Integer, String> problems = new LinkedHashMap<>();
        String initialSymbol = gc.getInitialSymbol();
        boolean insert = false;
        int counter = 1;
        final String recursionFound = getString(R.string.recursion_found_initial);
        for (Rule rule : gc.getRules()) {
            if (rule.getRightSide().contains(initialSymbol)) {
                insert = true;
                problems.put(counter,  recursionFound + ": " + rule.getLeftSide() +
                        " -> " + rule.getRightSide() + "\n");
                counter++;
            }
        }
        boolean situation;
        StringBuilder solutionDescription = new StringBuilder();

        final String[] parameters =
                getString(
                        R.string.recursion_initial_symbol_solution_descr_parameters
                ).split("#");
        if (insert) {
            situation = true;

            solutionDescription
                    .append(parameters[0])
                    .append("G = (V, Σ, P, ")
                    .append(gc.getInitialSymbol())
                    .append(')')
                    .append(parameters[1])
                    .append(gc.getInitialSymbol())
                    .append(parameters[2])
                    .append("G' = (V ∪ {")
                    .append(gc.getInitialSymbol())
                    .append("' }, Σ, P ∪ {")
                    .append(gc.getInitialSymbol())
                    .append("' → ")
                    .append(gc.getInitialSymbol())
                    .append("}, ")
                    .append(gc.getInitialSymbol())
                    .append("' ),")
                    .append(parameters[3])
                    .append(gc.getInitialSymbol())
                    .append(parameters[4])
                    .append("</p><br>");
            Rule r = new Rule(initialSymbol + "'", initialSymbol);
            gc.insertRule(r);
            gc.setInitialSymbol(initialSymbol + "'");
            academicSupport.insertNewRule(r);
        } else {
            situation = false;
        }
        gc.insertVariable(gc.getInitialSymbol());

        //seta feedback acadêmico no objeto
        academicSupport.setComments(comments);
        academicSupport.setFoundProblems(problems);
        academicSupport.setResult(gc);
        academicSupport.setSituation(situation);
        academicSupport.setSolutionDescription(solutionDescription.toString());
        return gc;
    }

    /**
     * @param g gramática livre de contexto
     * @return : gramática livre de contexto essencialmente não contrátil
     */
    public Grammar getGrammarEssentiallyNoncontracting(final Grammar g,
                                                       final AcademicSupport academicSupport) {
        Grammar gc = (Grammar) g.clone();
        Set<String> nullable = new LinkedHashSet<>();
        Set<String> prev = new LinkedHashSet<>();
        Set<Rule> setOfRules = new LinkedHashSet<>();
        boolean nullableVars = false;

        // nullable = nullable U A -> . | A E V
        Map<Integer, String> foundProblems = new LinkedHashMap<>();
        int counter = 1;
        final String[] parameters =
                getString(
                        R.string.esentially_noncontracting_problems_parameters
                ).split("#");
        for (Rule element : gc.getRules()) {
            if (element.getRightSide().equals(LAMBDA)) {
                nullable.add(element.getLeftSide());
                nullableVars = true;
                if (!element.getLeftSide().equals(gc.getInitialSymbol())) {
                    academicSupport.insertIrregularRule(element);
                }
                foundProblems.put(counter, new StringBuilder(parameters[0])
                        .append(element)
                        .append(parameters[1])
                        .toString());
                counter++;
            } else {
                Rule r = new Rule(element.getLeftSide(), element.getRightSide());
                setOfRules.add(r);
            }
        }
        academicSupport.setSituation(nullableVars);
        academicSupport.insertOnFirstSet(nullable, "Lambda");
        academicSupport.insertOnSecondSet(prev, "Lambda");
        do {
            prev.addAll(nullable);
            for (Rule element : gc.getRules()) {
                if (GrammarParser.prevContainsVariable(prev, element.getRightSide())) {
                    nullable.add(element.getLeftSide());
                }
            }
            academicSupport.insertOnFirstSet(nullable, "Lambda");
            academicSupport.insertOnSecondSet(prev, "Lambda");
        } while (!prev.equals(nullable));

        Set<Rule> newSetOfRules = new LinkedHashSet<>();
        for (Rule element : setOfRules) {
            String aux = GrammarParser.combination(element.getRightSide(), nullable);
            aux = aux.substring(0, aux.length() - 3);
            String[] productionsOnRightSide = aux.split("[|]");
            for (int i = 0; i < productionsOnRightSide.length; i++) {
                productionsOnRightSide[i] = productionsOnRightSide[i].trim();
                if (productionsOnRightSide[i].length() > 0 && !productionsOnRightSide[i].equals("")) {
                    Rule r = new Rule(element.getLeftSide(), productionsOnRightSide[i]);
                    newSetOfRules.add(r);
                    if (!g.getRules().contains(r)) {
                        academicSupport.insertNewRule(r);
                    }
                }
            }
        }

        if (nullable.contains(gc.getInitialSymbol())) {
            Rule r = new Rule(gc.getInitialSymbol(), LAMBDA);
            newSetOfRules.add(r);

        }

        //seta feedback acadêmico no objeto
        academicSupport.setFoundProblems(foundProblems);
        academicSupport.setResult(gc);

        gc.setRules(newSetOfRules);

        return gc;
    }

    public Rule getLambdaRule(String variable) {
        Set<Rule> rulesVariable = getRules(variable);
        for (Rule rule : rulesVariable) {
            if (rule.getRightSide().equals(LAMBDA)) {
                return rule;
            }
        }
        return null;
    }

    public boolean isStringOnlyContainsThat(String str, Set<String> symbols) {
        int n = str.length();
        for (int i = 0; i < n; i++) {
            String symbol = Character.toString(str.charAt(i));
            if (!symbols.contains(symbol)) {
                return false;
            }
        }
        return true;
    }

    public boolean isStringBeginWith(String str, String beginSymbol,
                                     Set<String> symbolsSkippable) {
        int ind = 0;
        int n = str.length();
        String symbol;
        do {
            symbol = str.substring(ind, ind + 1);
            ind++;
        } while (symbolsSkippable.contains(symbol) && ind < n);
        return symbol.equals(beginSymbol);
    }

    public Set<Rule> getRulesWithFirstProduces(String variable, Set<String> symbols,
                                               Set<String> symbolsSkippable) {
        Set<Rule> rulesProduces = new LinkedHashSet<>();
        for (String symbol : symbols) {
            rulesProduces.addAll(getRulesWithFirstProduces(variable, symbol, symbolsSkippable));
        }
        return rulesProduces;
    }

    public boolean existRule(Rule rule) {
        return rules.contains(rule);
    }

    public Set<Rule> getRulesWithFirstProduces(String variable, String symbol,
                                               Set<String> symbolsSkippable) {
        Set<Rule> rulesProduces = new LinkedHashSet<>();
        Set<Rule> rulesVariable = getRules(variable);
        for (Rule rule: rulesVariable) {
            if (isStringBeginWith(rule.getRightSide(), symbol, symbolsSkippable)) {
                rulesProduces.add(rule);
            }
        }
        return rulesProduces;
    }

    public Set<Rule> getRulesThatOnlyGenerates(String variable, Set<String> symbols) {
        Set<Rule> rulesGenerates = new LinkedHashSet<>();
        Set<Rule> rulesVariable = getRules(variable);
        for (Rule rule: rulesVariable) {
            if (isStringOnlyContainsThat(rule.getRightSide(), symbols)) {
                rulesGenerates.add(rule);
            }
        }
        return rulesGenerates;
    }

    /**
     * @param g gramática livre de contexto
     * @return : gramática livre de contexto sem regras da cadeia
     */
    public Grammar getGrammarWithoutChainRules(final Grammar g,
                                               final AcademicSupport academicSupport) {
        Grammar gc = (Grammar) g.clone();

        // primeiramente, deve-se construir os subconjuntos
        Map<String, Set<String>> setOfChains = new LinkedHashMap<>();
        for (String variable : gc.getVariables()) {
            // conjunto que representa o chain de determinada variável
            Set<String> chain = new LinkedHashSet<>();
            Set<String> prev = new LinkedHashSet<>();
            Set<String> newSet;
            chain.add(variable);
            do {
                newSet = GrammarParser.chainMinusPrev(chain, prev);
                prev.addAll(chain);
                for (String variableInNew : newSet) {
                    for (Rule element : gc.getRules(variableInNew)) {
                        if (element.getRightSide().length() == 1 &&
                                Character.isUpperCase(element.getRightSide().charAt(0))) {
                            chain.add(element.getRightSide());
                            academicSupport.insertIrregularRule(element);
                        }
                    }
                }
            } while (!chain.equals(prev));
            setOfChains.put(variable, chain);
            Set<String> setOfVariables = new LinkedHashSet<>();
            setOfVariables.add(variable);
            academicSupport.insertOnFirstSet(setOfVariables, "Chain");
            academicSupport.insertOnSecondSet(chain, "Chain");
        }
        // iterações sobre os conjuntos de chains
        Set<Rule> newSetOfRules = new LinkedHashSet<>();
        for (String variable : gc.getVariables()) {
            Set<String> chainsOfVariable = setOfChains.get(variable);
            for (String variableChain : chainsOfVariable) {
                for (Rule element : gc.getRules()) {
                    if (element.getLeftSide().equals(variableChain)) {
                        if (element.getRightSide().length() != 1 ||
                                !Character.isUpperCase(element.getRightSide().charAt(0))) {
                            Rule r = new Rule(variable, element.getRightSide());
                            newSetOfRules.add(r);
                            if (chainsOfVariable.size() != 1 && !gc.getRules().contains(r)) {
                                academicSupport.insertNewRule(r);
                            }
                        }
                    }
                }
            }
        }

        gc.setRules(newSetOfRules);
        return gc;
    }

    /**
     * @param g gramática livre de contexto
     * @return : gramática livre de contexto sem símbolos não terminais
     */
    public Grammar getGrammarWithoutNoTerm(final Grammar g, final AcademicSupport academicSupport) {
        Set<String> term = new LinkedHashSet<>();
        Set<String> prev = new LinkedHashSet<>();
        Grammar gc = (Grammar) g.clone();

        // preenche conjunto term com as variáveis que são terminais
        for (Rule element : gc.getRules()) {
            if (element.getRightSide().length() == 1 && (!element.getRightSide().equals("|")) &&
                    (Character.isLowerCase(element.getRightSide().charAt(0)) ||
                            element.getRightSide().charAt(0) == '.')) {
                if (!term.contains(element.getLeftSide())) {
                    term.add(element.getLeftSide());
                }
            }
        }

        academicSupport.insertOnFirstSet(term, "TERM");
        academicSupport.insertOnSecondSet(prev, "TERM");
        do {
            prev.addAll(term);
            for (Rule element : gc.getRules()) {
                boolean insertOnTerm = true;
                for (int j = 0; j < element.getRightSide().length() && insertOnTerm; j++) {
                    if (Character.isLowerCase(element.getRightSide().charAt(j)) &&
                            !gc.getTerminals().contains(Character.toString(element.getRightSide().charAt(j)))) {
                        insertOnTerm = false;
                    } else if (Character.isUpperCase(element.getRightSide().charAt(j))
                            && !prev.contains(Character.toString(element.getRightSide().charAt(j)))) {
                        insertOnTerm = false;
                    }
                }
                if (insertOnTerm) {
                    if (!term.contains(element.getLeftSide())) {
                        term.add(element.getLeftSide());

                    }
                }
            }
            academicSupport.insertOnFirstSet(term, "TERM");
            academicSupport.insertOnSecondSet(prev, "TERM");
        } while (!term.equals(prev));

        if (term.size() != gc.getVariables().size()) {
            academicSupport.setSituation(true);
        } else {
            academicSupport.setSituation(false);
        }

        Grammar aux = new Grammar();
        aux.setVariables(prev);
        aux.setRules(GrammarParser.updateRules(prev, gc, academicSupport));
        aux.setTerminals(GrammarParser.updateTerminals(aux));
        aux.removeTerminal(Grammar.LAMBDA);
        gc.setVariables(aux.getVariables());
        gc.setTerminals(aux.getTerminals());
        gc.setRules(aux.getRules());

        return gc;
    }

    /**
     * @param g gramática livre de contexto
     * @return : gramática livre de contexto sem símbolos não alcançáveis
     */
    public Grammar getGrammarWithoutNoReach(final Grammar g, final AcademicSupport academicSupport) {
        Set<String> reach = new LinkedHashSet<>();
        Set<String> prev = new LinkedHashSet<>();
        Set<String> newSet = new LinkedHashSet<>();
        Grammar gc = (Grammar) g.clone();
        reach.add(gc.getInitialSymbol());
        newSet.add(gc.getInitialSymbol());
        academicSupport.insertOnFirstSet(reach, "REACH");
        academicSupport.insertOnSecondSet(prev, "REACH");
        academicSupport.insertOnThirdSet(newSet, "REACH");
        academicSupport.setSituation(true);
        do {
            prev.addAll(reach);
            for (String element : newSet) {
                for (Rule secondElement : gc.getRules()) {
                    if (secondElement.getLeftSide().equals(element)) {
                        reach.addAll(GrammarParser.variablesInW(reach, secondElement.getRightSide()));
                    }
                }
            }
            newSet = GrammarParser.reachMinusPrev(reach, prev);
            academicSupport.insertOnFirstSet(reach, "REACH");
            academicSupport.insertOnSecondSet(prev, "REACH");
            academicSupport.insertOnThirdSet(newSet, "REACH");
        } while (!reach.equals(prev));
        Grammar aux = new Grammar();
        aux.setVariables(prev);
        aux.setInitialSymbol(gc.getInitialSymbol());
        aux.setRules(GrammarParser.updateRules(prev, gc, academicSupport));
        aux.setTerminals(GrammarParser.updateTerminals(aux));
        aux.removeTerminal(Grammar.LAMBDA);
        gc.setVariables(aux.getVariables());
        gc.setTerminals(aux.getTerminals());
        gc.setRules(aux.getRules());
        return aux;
    }


    public Map<String, Rule> rulesProducesOnlyOneTerminal() {
        Map<String, Rule> rulesProdOOneTerm = new LinkedHashMap<>();
        Map<String, Set<String>> rulesMapLeftToRight = getRulesMapLeftToRight();
        for (Rule rule : rules) {
            if (rule.producesOnlyOneTerminal() &&
                    rulesMapLeftToRight.get(rule.getLeftSide()).size()==1) {
                rulesProdOOneTerm.put(rule.getRightSide(), rule);
            }
        }
        return rulesProdOOneTerm;
    }

    public Map<String, Rule> rulesProducesOnlyOneBinaryRightSide() {
        Map<String, Rule> rulesProdOOneBRightSide = new LinkedHashMap<>();
        Map<String, Set<String>> rulesMapLeftToRight = getRulesMapLeftToRight();
        for (Rule rule : rules) {
            List<String> rightSides = new ArrayList<>(rulesMapLeftToRight.get(rule.getLeftSide()));
            if (rightSides.size() == 1 &&
                    Rule.getNumberOfSymbolsInRightSide(rightSides.get(0)) == 2) {
                rulesProdOOneBRightSide.put(rule.getRightSide(), rule);
            }
        }
        return rulesProdOOneBRightSide;
    }

    public Map<String, Integer> getRulesMapLeftToCont() {
        Map<String, Integer> rulesMapLeftToCont = new LinkedHashMap<>();
        for (Rule rule : rules) {
            if (rulesMapLeftToCont.containsKey(rule.getLeftSide())) {
                rulesMapLeftToCont.put(rule.getLeftSide(),
                        rulesMapLeftToCont.get(rule.getLeftSide())+1);
            } else {
                rulesMapLeftToCont.put(rule.getLeftSide(), 1);
            }
        }
        return rulesMapLeftToCont;
    }

    public boolean isFNC() {
        for (Rule rule : rules) {
            if (!rule.isFnc(initialSymbol)) {
                return false;
            }
        }
        return true;
    }

    public Set<Rule> getRulesWithInitialSymbolFirst() {
        Set<Rule> rulesInit = new LinkedHashSet<>();
        if (initialSymbol == null) {
            return rules;
        }
        for (Rule rule : getRules()) {
            if (rule.getLeftSide().equals(initialSymbol)) {
                rulesInit.add(rule);
            }
        }
        for (Rule rule : getRules()) {
            if (!rule.getLeftSide().equals(initialSymbol)) {
                rulesInit.add(rule);
            }
        }
        return rulesInit;
    }


    /**
     * @param g gramática livre de contexto
     * @return : gramática livre de contexto na Forma Normal de Chomsky
     */
    public Grammar FNC(final Grammar g, final AcademicSupport academic) {
        Grammar gc = (Grammar) g.clone();
        if (!gc.isFNC()) {
            gc = getGrammarWithInitialSymbolNotRecursive(gc, new AcademicSupport());
            gc = getGrammarEssentiallyNoncontracting(gc, new AcademicSupport());
            gc = getGrammarWithoutChainRules(gc, new AcademicSupport());
            gc = getGrammarWithoutNoTerm(gc, new AcademicSupport());
            gc = getGrammarWithoutNoReach(gc, new AcademicSupport());
            if (!gc.getRules().equals(g.getRules())) {
                System.out.println(gc);
            }
            academic.setSituation(true);

            Set<Rule> newSetOfRules = new LinkedHashSet<>();
            Set<Rule> deleteRules = new LinkedHashSet<>();
            int contChomskyVariables = 1;
            Map<String, Rule> rulesProdOOneTerm = gc.rulesProducesOnlyOneTerminal();
            // Rules bigger than 1 symbol and have terminals
            for (Rule rule : gc.rules) {
                if (rule.getNumberOfSymbolsInRightSide() > 1 && rule.containsTerminalOnRightSide()) {
                    deleteRules.add(rule);
                    academic.insertIrregularRule(rule);
                    StringBuilder sb = new StringBuilder(rule.getRightSide());
                    int i = 0;
                    while (true) {
                        while (i < sb.length() && !Character.isLowerCase(sb.charAt(i))) {
                            i++;
                        }
                        if (i == sb.length()) {
                            break;
                        }
                        Rule newRule = rulesProdOOneTerm.get(Character
                                .toString(sb.charAt(i)));
                        if (newRule != null) {
                            sb.deleteCharAt(i);
                            sb.insert(i, newRule.getLeftSide());
                        } else {
                            String leftSide = Character.toUpperCase(sb.charAt(i)) + "'";
                            while (gc.variables.contains(leftSide)) {
                                leftSide = CHOMSKY_PREFIX + contChomskyVariables;
                                contChomskyVariables++;
                            }
                            gc.variables.add(leftSide);
                            newRule = new Rule(leftSide, Character.toString(sb.charAt(i)));
                            sb.deleteCharAt(i);
                            sb.insert(i, leftSide);
                            rulesProdOOneTerm.put(newRule.getRightSide(), newRule);
                            newSetOfRules.add(newRule);
                            academic.insertNewRule(newRule);
                        }
                    }
                    Rule newRule = new Rule(rule.getLeftSide(), sb.toString());
                    newSetOfRules.add(newRule);
                    academic.insertNewRule(newRule);
                }
            }
            gc.rules.removeAll(deleteRules);
            gc.rules.addAll(newSetOfRules);
            deleteRules.clear();
            newSetOfRules.clear();
            Map<String, Rule> rulesProdOOneRightSide = gc.rulesProducesOnlyOneBinaryRightSide();
            for (Rule rule : gc.getRulesWithInitialSymbolFirst()) {
                if (rule.getNumberOfSymbolsInRightSide() > 2) {
                    deleteRules.add(rule);
                    academic.insertIrregularRule(rule);
                    List<String> listRightSide = rule.getListOfSymbolsOnRightSide();
                    while (listRightSide.size() > 2) {
                        String strRightSide = listRightSide.get(listRightSide.size()-2) +
                                listRightSide.get(listRightSide.size()-1);
                        listRightSide.remove(listRightSide.size()-1);
                        listRightSide.remove(listRightSide.size()-1);
                        Rule newRule = rulesProdOOneRightSide.get(strRightSide);
                        if (newRule != null) {
                            listRightSide.add(newRule.getLeftSide());
                        } else {
                            String leftSide;
                            do {
                                leftSide = CHOMSKY_PREFIX + contChomskyVariables;
                                contChomskyVariables++;
                            } while (gc.variables.contains(leftSide));
                            gc.variables.add(leftSide);
                            listRightSide.add(leftSide);
                            newRule = new Rule(leftSide, strRightSide);
                            newSetOfRules.add(newRule);
                            rulesProdOOneRightSide.put(strRightSide, newRule);
                            academic.insertNewRule(newRule);
                        }
                    }
                    String strRightSide = listRightSide.get(0) + listRightSide.get(1);
                    Rule newRule = new Rule(rule.getLeftSide(), strRightSide);
                    newSetOfRules.add(newRule);
                    academic.insertNewRule(newRule);
                }
            }
            gc.rules.removeAll(deleteRules);
            gc.rules.addAll(newSetOfRules);
        }
        return gc;
    }

    /**
     * Verifica se existe recursão direta à esquerda.
     *
     * @return true se existe recursão direta à esquerda, caso contrário, false.
     */
    public boolean haveImmediateLeftRecursion() {
        for (Rule element : getRules()) {
            if (element.existsLeftRecursion()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Conta a quantidade de variáveis que possuem recursão direta à esquerda.
     *
     * @return quantidade de variáveis que possuem recursão direta à esquerda
     */
    public int numberOfVariablesWithImmediateLeftRecursion() {
        int numberOfVariables = 0;
        for (Rule element : getRules()) {
            if (element.existsLeftRecursion()) {
                numberOfVariables++;
            }
        }
        return numberOfVariables;
    }

    /**
     * Remove a recursão direta à esquerda da gramática.
     *
     * @param g        gramática
     * @param academic informações de suporte acadêmico
     * @return gramática sem recursão direta à esquerda
     */
    public Grammar removingTheImmediateLeftRecursion(final Grammar g,
                                                     final AcademicSupport academic) {

        Grammar gc = (Grammar) g.clone();
        if (haveImmediateLeftRecursion()) {
            academic.setSituation(true);
            int numberOfVariablesForRemovingLeftRecursion = 1;
            Set<Rule> rulesWithLeftRecursion = new LinkedHashSet<>();
            Set<Rule> rulesWithoutLeftRecursion = new LinkedHashSet<>();
            Set<Rule> newSetOfRules;
            List<String> variablesAux = new ArrayList<>(gc.getVariables());
            variablesAux.remove(gc.getInitialSymbol());
            variablesAux.add(0, gc.getInitialSymbol());
            for (String variable : variablesAux) {
                if (isVariableForRemovingLeftRecursion(variable)) {
                    continue;
                }
                for (Rule element : gc.getRules(variable)) {
                    if (element.existsLeftRecursion()) {
                        rulesWithLeftRecursion.add(element);
                    } else {
                        rulesWithoutLeftRecursion.add(element);
                    }
                }
                //BEGIN - remoção da recursão à esquerda direta
                if (!rulesWithLeftRecursion.isEmpty()) {
                    newSetOfRules = new LinkedHashSet<>();
                    String leftSide = RECURSIVE_REMOVAL_PREFIX +
                            String.valueOf(numberOfVariablesForRemovingLeftRecursion);
                    for (Rule element : rulesWithLeftRecursion) {
                        academic.insertIrregularRule(element);
                        Rule r = new Rule(leftSide, element.getRightSide().substring(1));
                        academic.insertNewRule(r);
                        newSetOfRules.add(r);
                        r = new Rule(leftSide,
                                element.getRightSide().substring(1) + leftSide);
                        academic.insertNewRule(r);
                        newSetOfRules.add(r);
                    }
                    for (Rule element : rulesWithoutLeftRecursion) {
                        Rule r = new Rule(element.getLeftSide(),
                                element.getRightSide() + leftSide);
                        newSetOfRules.add(r);
                        academic.insertNewRule(r);
                    }
                    newSetOfRules.addAll(gc.rules);
                    newSetOfRules.removeAll(rulesWithLeftRecursion);
                    gc.rules = newSetOfRules;
                    numberOfVariablesForRemovingLeftRecursion++;
                }
                rulesWithLeftRecursion.clear();
                rulesWithoutLeftRecursion.clear();
                //END - remoção da recursão à esquerda direta
            }
            for (int i = 1; i < numberOfVariablesForRemovingLeftRecursion; i++) {
                gc.insertVariable(RECURSIVE_REMOVAL_PREFIX + String.valueOf(i));
            }
        }
        return gc;
    }

    public boolean produces(List<String> variables, String variable) {
        for (Rule rule : getRules(variable)) {
            if (rule.isChainRule()) {
                for (String variableAux : variables) {
                    if (rule.getRightSide().equals(variableAux)) {
                        return true;
                    }
                }
                variables.add(variable);
                produces(variables, rule.getRightSide());
            }
        }
        return false;
    }

    public boolean haveCyclesWithoutLambdaProductions() {
        for (String variable : getVariables()) {
            for (Rule rule : getRules(variable)) {
                if (rule.isChainRule()) {
                    List<String> variables = new ArrayList<>();
                    variables.add(variable);
                    if (produces(variables, rule.getRightSide())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isIsoledVariable(String variable) {
        for (Rule rule : getRules()) {
            if (rule.rightSideContainsSymbol(variable)) {
                return false;
            }
        }
        return true;
    }

    public boolean haveProductionsLambdaIgnoringIsoledVariable() {
        for (Rule rule : getRules(initialSymbol)) {
            if (rule.producesLambda()) {
                if (!isIsoledVariable(initialSymbol)) {
                    return true;
                }
            }
        }
        for (Rule rule : getRulesExcept(initialSymbol)) {
            if (rule.producesLambda()) {
                return true;
            }
        }
        return false;
    }

    private List<String> getOrderVariablesForRemoveLeftRecursion() {
        List<String> variablesAux = new ArrayList<>(variables);
        variablesAux.remove(initialSymbol);
        variablesAux.add(0, initialSymbol);
//        int indicePrioridade = 1;
//        boolean recursion;
//        for (int i = 1; i < variablesAux.size(); i++) {
//            recursion = false;
//            for (Rule rule : getRules(variablesAux.get(i))) {
//                if (rule.existsLeftRecursion()) {
//                    recursion = true;
//                    break;
//                }
//            }
//            if (!recursion) {
//                Collections.swap(variablesAux, i, indicePrioridade);
//                indicePrioridade++;
//            }
//        }
        return variablesAux;
    }

    public Grammar removingLeftRecursion
            (final Grammar g, final AcademicSupport academic,
             final Map<String, String> sortedVariables,
             final AcademicSupportForRemoveLeftRecursion academicLR) {
        Grammar gc = (Grammar) g.clone();
        academicLR.setOriginalGrammar((Grammar) g.clone());
        if (GrammarParser.checksGrammar(gc, academic)) {
            int order = 1;
            int numberOfVariablesForRemovingLeftRecursing = 1;
            Set<Rule> rulesWithLeftRecursion = new LinkedHashSet<>();
            Set<Rule> rulesWithoutLeftRecursion = new LinkedHashSet<>();
            Set<Rule> newSetOfRules;
            Set<Rule> removalRules = new LinkedHashSet<>();
            Deque<String> variableOrder = new LinkedList<>();
            List<String> variablesAux = getOrderVariablesForRemoveLeftRecursion();
            academicLR.setOrderVariables(new ArrayList<>(variablesAux));
            for (String variable : variablesAux) {
                if (isVariableForRemovingLeftRecursion(variable)) {
                    continue;
                }
                //BEGIN - remoção da recursão à esquerda indireta
                Set<Rule> indirLeftRec =  new LinkedHashSet<>();
                do {
                    indirLeftRec.clear();
                    for (Rule element : gc.getRules(variable)) {
                        String firstVar = element.getFirstVariableOfRightSide();
                        if (firstVar != null) {
                            boolean hasRecDIr = false;
                            for (Rule rul : gc.getRules(firstVar)) {
                                if (rul.existsLeftRecursion()) {
                                    hasRecDIr = true;
                                    break;
                                }
                            }
                            if (!hasRecDIr) {
                                indirLeftRec.add(element);
                            }
                        }
                    }
                    if (!indirLeftRec.isEmpty()) {
                        newSetOfRules = new LinkedHashSet<>();
                        for (Rule element : indirLeftRec) {
                            boolean hasRecDir = false;
                            String firstVar = element.getFirstVariableOfRightSide();
                            academic.insertIrregularRule(element);
                            System.out.println("FIRST_VAR");
                            System.out.println(firstVar);
                            String leftSide = element.getLeftSide();
                            int begin = firstVar.length();
                            String finalRightSide = element.getRightSide().substring(begin);
                            for (Rule rul : gc.getRules(firstVar)) {
                                String rightSideBegin = rul.getRightSide();
                                Rule r = new Rule(leftSide, rightSideBegin + finalRightSide);
                                System.out.println("NEW RULE: " + r);
                                academic.insertNewRule(r);
                                newSetOfRules.add(r);
                            }
                        }
                        System.out.println("INDIR_RULES");
                        System.out.println(indirLeftRec);
                        System.out.println("NEW_RULES");
                        System.out.println(newSetOfRules);
                        academicLR.addGrammarTransformationStage1(
                                indirLeftRec, newSetOfRules, false);
                        gc.rules.addAll(newSetOfRules);
                        gc.rules.removeAll(indirLeftRec);
                    }
                } while (!indirLeftRec.isEmpty());

                //END - remoção da recursão à esquerda indireta
                for (Rule element : gc.getRules(variable)) {
                    if (element.existsLeftRecursion()) {
                        rulesWithLeftRecursion.add(element);
                    } else {
                        rulesWithoutLeftRecursion.add(element);
                    }
                }
                //BEGIN - remoção da recursão à esquerda direta
                if (!rulesWithLeftRecursion.isEmpty()) {
                    newSetOfRules = new LinkedHashSet<>();
                    String leftSide = RECURSIVE_REMOVAL_PREFIX +
                            String.valueOf(numberOfVariablesForRemovingLeftRecursing);
                    variableOrder.offer(leftSide);
                    for (Rule element : rulesWithLeftRecursion) {
                        academic.insertIrregularRule(element);
                        Rule r = new Rule(leftSide, element.getRightSide().substring(1));
                        academic.insertNewRule(r);
                        newSetOfRules.add(r);
                        r = new Rule(leftSide,
                                element.getRightSide().substring(1) + leftSide);
                        academic.insertNewRule(r);
                        newSetOfRules.add(r);
                    }
                    for (Rule element : rulesWithoutLeftRecursion) {
                        Rule r = new Rule(element.getLeftSide(),
                                element.getRightSide() + leftSide);
                        newSetOfRules.add(r);
                        academic.insertNewRule(r);
                    }
                    academicLR.addGrammarTransformationStage1(
                            rulesWithLeftRecursion, newSetOfRules, true);
//                    newSetOfRules.addAll(gc.rules);
//                    newSetOfRules.removeAll(rulesWithLeftRecursion);
//                    gc.rules = newSetOfRules;
                    gc.rules.addAll(newSetOfRules);
                    gc.rules.removeAll(rulesWithLeftRecursion);
                    numberOfVariablesForRemovingLeftRecursing++;
                }
                rulesWithLeftRecursion.clear();
                rulesWithoutLeftRecursion.clear();
                //END - remoção da recursão à esquerda direta
                //BEGIN - Propagação - remoção da recursão à esquerda indireta
                int orderAux = order;
                for (String variableAux : variablesAux) {
                    if (isVariableForRemovingLeftRecursion(variableAux)) {
                        continue;
                    }
                    if (orderAux > 0) {
                        orderAux--;
                        continue;
                    }
                    newSetOfRules = new LinkedHashSet<>();
                    for (Rule element : gc.getRules(variableAux)) {
                        if (element.getFirstVariableOfRightSide() != null &&
                                element.getFirstVariableOfRightSide().equals(variable)) {
                            removalRules.add(element);
                            academic.insertIrregularRule(element);
                            for (Rule elementAux : gc.getRules(variable)) {
                                Rule r = new Rule(element.getLeftSide(),
                                        elementAux.getRightSide() +
                                                element.getRightSide().substring(variable.length()));
                                academic.insertNewRule(r);
                                newSetOfRules.add(r);
                            }
                        }
                    }
                    if (!newSetOfRules.isEmpty()) {
                        academicLR.addGrammarTransformationStage1(
                                removalRules, newSetOfRules, false);
//                        newSetOfRules.addAll(gc.rules);
//                        newSetOfRules.removeAll(removalRules);
//                        gc.rules = newSetOfRules;
                        gc.rules.addAll(newSetOfRules);
                        gc.rules.removeAll(removalRules);
                        removalRules.clear();
                    }
                }
                //END - Propagação - remoção da recursão à esquerda indireta
                variableOrder.push(variable);
                sortedVariables.put(variable, String.valueOf(order));
                order++;
            }
            academicLR.setOrderVariablesFNG(variableOrder);
            for (int i = 1; i < numberOfVariablesForRemovingLeftRecursing; i++) {
                gc.insertVariable(RECURSIVE_REMOVAL_PREFIX + String.valueOf(i));
            }
            if (!academic.getIrregularRules().isEmpty()) {
                academic.setSituation(true);
            }
        }
        return gc;

    }

     public boolean isVariableForRemovingLeftRecursion(String variable) {
        if (variable != null && variable.length() > 0 &&
                Character.toString(variable.charAt(0)).equals(RECURSIVE_REMOVAL_PREFIX)) {
            for (int i = 1; i < variable.length(); i++) {
                if (!Character.isDigit(variable.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public Set<Rule> getRules(String variable) {
        Set<Rule> rulesOfVariable = new LinkedHashSet<>();
        for (Rule rule : rules) {
            if (rule.getLeftSide().equals(variable)) {
                rulesOfVariable.add(rule);
            }
        }
        return rulesOfVariable;
    }

    public Set<Rule> getRulesExcept(String variable) {
        Set<Rule> rulesOfVariable = new LinkedHashSet<>();
        for (Rule rule : rules) {
            if (!rule.getLeftSide().equals(variable)) {
                rulesOfVariable.add(rule);
            }
        }
        return rulesOfVariable;
    }


    public Set<Rule> getRulesThatProduces(String rightSide) {
        Set<Rule> rulesOfVariable = new LinkedHashSet<>();
        for (Rule rule : rules) {
            if (rule.getRightSide().contains(rightSide)) {
                rulesOfVariable.add(rule);
            }
        }
        return rulesOfVariable;
    }

    public Set<String> getLeftSidesThatProduces(String rightSide) {
        Set<String> lefSides = new LinkedHashSet<>();
        for (Rule rule : rules) {
            if (rule.getRightSide().equals(rightSide)) {
                lefSides.add(rule.getLeftSide());
            }
        }
        return lefSides;
    }

    public boolean isFNG() {
        for (Rule rule : this.rules) {
            if (!rule.isFng(this.initialSymbol)) {
                return false;
            }
        }
        return true;
    }

    public List<String> getIndirectLeftRecursionOfVariable(String variable,
                                                           List<String>
                                                                   variablesWithLeftRecursion) {
        List<String> indirectLeftRecursion = new ArrayList<>();
        for (String var : variablesWithLeftRecursion) {
            if (variable.equals(var)) {
                continue;
            }
            for (Rule rule : getRules(var)) {
                if (rule.getFirstVariableOfRightSide().equals(variable)) {
                    indirectLeftRecursion.add(var);
                }
            }
        }
        return indirectLeftRecursion;
    }

    public Map<String, List<String>> getVariablesMapToIndirectLeftRecursion
            (List<String> variables, List<String> variablesWithLeftRecursion) {
        Map<String, List<String>> variablesMapToIndirectLeftRecursion = new
                LinkedHashMap<>();
        for (String varible : variables) {
            variablesMapToIndirectLeftRecursion.put(varible,
                    getIndirectLeftRecursionOfVariable(varible,
                            variablesWithLeftRecursion));
        }
        return variablesMapToIndirectLeftRecursion;
    }

    public List<String> getVariablesWithDirectLeftRecursion() {
        List<String> variablesWithLeftRecursion = new ArrayList<>();
        boolean existRecursion;
        for (String variable : this.variables) {
            existRecursion = false;
            for (Rule rule : getRules(variable)) {
                if (rule.existsLeftRecursion()) {
                    existRecursion = true;
                    break;
                }
            }
            if (existRecursion) {
                variablesWithLeftRecursion.add(variable);
            }
        }
        return variablesWithLeftRecursion;
    }

    public List<String> getVariablesWithoutDirectLeftRecursion() {
        List<String> variablesWithoutLeftRecursion = new ArrayList<>();
        boolean existRecursion;
        for (String variable : this.variables) {
            existRecursion = false;
            for (Rule rule : getRules(variable)) {
                if (rule.existsLeftRecursion()) {
                    existRecursion = true;
                    break;
                }
            }
            if (!existRecursion) {
                variablesWithoutLeftRecursion.add(variable);
            }
        }
        return variablesWithoutLeftRecursion;
    }

    public void sortVariables(List<String> variables) {
        Collections.sort(variables);
        if (variables.contains(this.initialSymbol)) {
            variables.remove(this.initialSymbol);
            variables.add(0, this.initialSymbol);
        }
    }

    public List<String> getBestOrderVariablesForRemovingLeftRecursion
            (final AcademicSupportFNG academicSupportFng) {
        List<String> orderVariables = new ArrayList<>();
        List<String> orderVariablesAux = new ArrayList<>
                (getVariablesWithoutDirectLeftRecursion());
        sortVariables(orderVariablesAux);
        orderVariables.addAll(orderVariablesAux);
        //orderVariablesAux.Get
        List<String> variablesWithLeftRecursion = new ArrayList<>();
        boolean existRecursion;
        boolean existRecursionInitialSimbol = false;
        for (Rule rule : getRules(getInitialSymbol())) {
            if (rule.existsLeftRecursion()) {
                existRecursionInitialSimbol = true;
                break;
            }
        }
        if (existRecursionInitialSimbol) {
            variablesWithLeftRecursion.add(this.initialSymbol);
        } else {
            orderVariables.add(getInitialSymbol());
        }
        for (String variable : this.variables) {
            if (variable.equals(this.initialSymbol)) {
                continue;
            }
            existRecursion = false;
            for (Rule rule : getRules(variable)) {
                if (rule.existsLeftRecursion()) {
                    existRecursion = true;
                    break;
                }
            }
            if (existRecursion) {
                variablesWithLeftRecursion.add(variable);
            } else {
                orderVariables.add(variable);
            }
        }
//		academicSupportFng.addListVariables(orderVariables);
        //List<String> orderVariablesAux = new ArrayList<>();
        Map<String, List<String>> indirectLeftRecursion = new LinkedHashMap<>();
        for (String variable : variablesWithLeftRecursion) {
            //indirectLeftRecursion.put(variable,
            //indirectLeftRecursionOfVariable(variable,
            //variablesWithLeftRecursion));
        }
        return null;
    }

    public Grammar FNG(final Grammar g, final AcademicSupport academic) {
        Grammar gAux = (Grammar) g.clone();
        if (gAux.isFNG()) {
            return gAux;
        }
        gAux = g.getGrammarWithInitialSymbolNotRecursive(gAux, academic);
        gAux = gAux.getGrammarEssentiallyNoncontracting(gAux, academic);
        gAux = gAux.getGrammarWithoutChainRules(gAux, academic);
        gAux = gAux.getGrammarWithoutNoTerm(gAux, academic);
        gAux = gAux.getGrammarWithoutNoReach(gAux, academic);
        gAux = gAux.FNC(gAux, academic);
        return gAux.FNG(gAux, academic, new AcademicSupportFNG());
    }

    public Grammar FNG(final Grammar g, final AcademicSupport academic,
                       final AcademicSupportFNG academicSupportFNG) {
        AcademicSupportForRemoveLeftRecursion academicSupportRLF =
                new AcademicSupportForRemoveLeftRecursion();
        Grammar gc = removingLeftRecursion(g, new AcademicSupport(),
                new LinkedHashMap<String, String>(), academicSupportRLF);
        academicSupportFNG.setOriginalGrammar((Grammar) gc.clone());
        academicSupportRLF.getGrammarTransformationsStage1();
        Set<Rule> newSetOfRules;
        Set<Rule> removalRules = new LinkedHashSet<>();
        Deque<String> variableOrder = academicSupportRLF.getOrderVariablesFNG();
        academicSupportFNG.setOrderVariables(academicSupportRLF.getOrderVariables());
        if (gc.isFNG()) {
            return gc;
        }
        //BEGIN - Propagação volta - remoção da recursão à esquerda indireta
        String variable;
        while (variableOrder.peek() != null) {
            variable = variableOrder.pop();
            newSetOfRules = new LinkedHashSet<>();
            for (String variableAux : gc.getVariables()) {
                for (Rule element : gc.getRules(variable)) {
                    if (element.getFirstVariableOfRightSide() != null &&
                            element.getFirstVariableOfRightSide().equals(variableAux)) {
                        removalRules.add(element);
                        academic.insertIrregularRule(element);
                        for (Rule elementAux : gc.getRules(variableAux)) {
                            Rule r = new Rule(element.getLeftSide(),
                                    elementAux.getRightSide() +
                                            element.getRightSide().substring(variableAux.length()));
                            academic.insertNewRule(r);
                            newSetOfRules.add(r);
                        }
                    }
                }
            }
            if (!newSetOfRules.isEmpty()) {
                if (variable.startsWith(RECURSIVE_REMOVAL_PREFIX) &&
                        variable.length() > 1) {
                    academicSupportFNG.addGrammarTransformationStage3(
                            removalRules, newSetOfRules);
                } else {
                    academicSupportFNG.addGrammarTransformationStage2(
                            removalRules, newSetOfRules);
                }
//                    newSetOfRules.addAll(gc.rules);
//                    newSetOfRules.removeAll(removalRules);
//                    gc.rules = newSetOfRules;
                gc.rules.addAll(newSetOfRules);
                gc.rules.removeAll(removalRules);
                removalRules.clear();
            }
        }
        //END - Propagação volta - remoção da recursão à esquerda indireta
        if (!academic.getIrregularRules().isEmpty()) {
            academic.setSituation(true);
        }

        return gc;
    }


    // ALGORITMO DE RECONHECIMENTO CYK
    public static Set<String>[][] CYK(Grammar g, String word) {
        // inicializando a tabela
        g = g.FNC(g, new AcademicSupport());

        Set<String>[][] X = new LinkedHashSet[word.length() + 1][word.length()];
        for (int i = 0; i < word.length() + 1; i++) {
            for (int j = 0; j < word.length(); j++) {
                X[i][j] = new LinkedHashSet<>();
            }
        }

        // inserindo a palavra na base da tabela
        for (int i = 0; i < word.length(); i++) {
            X[word.length()][i].add(Character.toString(word.charAt(i)));
        }

        // preenchendo a primeira linha da tabela
        for (int j = 0; j < word.length(); j++) {
            X[word.length() - 1][j] = g.getLeftSidesThatProduces(Character.toString(word.charAt(j)));
        }

        for (int i = word.length()-2; i > -1; i--) {
            for (int j = 0; j < i+1; j++) {
                for (int k = i+1; k < word.length(); k++) {
                    for (String firstCell : X[k][j]) {
                        for (String secondCell : X[word.length()-k+i][word.length()-k+j]) {
                            X[i][j].addAll(g.getLeftSidesThatProduces(firstCell +
                                    secondCell));
                        }
                    }
                }
            }
        }

        return X;
    }

    public Map<String, Set<String>> getRulesMapLeftToRight() {
        variables.remove(Symbols.LAMBDA);
        Map<String, Set<String>> rulesMapLeftToRight = new LinkedHashMap<>();
        rulesMapLeftToRight.put(initialSymbol, new LinkedHashSet<String>());
        Set<String> variables = new LinkedHashSet<>(getVariables());
        variables.remove(initialSymbol);
        for (String variable: variables) {
            rulesMapLeftToRight.put(variable, new LinkedHashSet<String>());
        }
        for (Rule rule : rules) {
            Set<String> rightSideSet = rulesMapLeftToRight.get(rule.getLeftSide());
            if (rightSideSet == null) {
                rightSideSet = new LinkedHashSet<>();
                rulesMapLeftToRight.put(rule.getLeftSide(), rightSideSet);
            }
            rightSideSet.add(rule.getRightSide());
        }
        Set<String> deleteEntry = new HashSet<>();
        for (Map.Entry<String, Set<String>> entry : rulesMapLeftToRight.entrySet()) {
            if (entry.getValue().isEmpty()) {
                deleteEntry.add(entry.getKey());
            }
        }
        for (String str : deleteEntry) {
            rulesMapLeftToRight.remove(str);
        }
        return rulesMapLeftToRight;
    }

    public Map<String, Set<Rule>> getRulesMapLeftToRule() {
        Map<String, Set<Rule>> rulesMapLeftToRule = new LinkedHashMap<>();
        Set<Rule> rightSide;
        rulesMapLeftToRule.put(initialSymbol, new LinkedHashSet<Rule>());
        for (Rule rule : rules) {
            if (rulesMapLeftToRule.containsKey(rule.getLeftSide())) {
                rightSide = rulesMapLeftToRule.get(rule.getLeftSide());
                rightSide.add(rule);
                rulesMapLeftToRule.put(rule.getLeftSide(), rightSide);
            } else {
                rightSide = new LinkedHashSet<>();
                rightSide.add(rule);
                rulesMapLeftToRule.put(rule.getLeftSide(), rightSide);
            }
        }
        return rulesMapLeftToRule;
    }

    public String toStringRulesMapLeftToRight() {
        StringBuilder sb = new StringBuilder();
        Map<String, Set<String>> leftToRigth = getRulesMapLeftToRight();
        SortedMap<String, SortedSet<String>> leftToRigthSorted = new TreeMap<>();
        for (String left : leftToRigth.keySet()) {
            leftToRigthSorted.put(left, new TreeSet<>(leftToRigth.get(left)));
        }
        for (String left : leftToRigthSorted.keySet()) {
            sb.append(left).append(" -> ");
            for (String right : leftToRigthSorted.get(left)) {
                sb.append(right).append(" | ");
            }
            sb.delete(sb.length() - 3, sb.length());
            sb.append("\n");
        }
        return sb.toString();
    }

    private char varActual = 'A' - 1;
    private int contVar = 0;

    public String nextVariable() {
        varActual++;
        if (varActual > 'Z') {
            contVar++;
            varActual = 'A' - 1;
        }
        if (contVar > 0) {
            return "" + varActual + contVar;
        } else {
            return Character.toString(varActual);
        }
    }

    private void changeVariables(Map<String, String> variablesChanged) {
        for (Map.Entry<String, String> entry : variablesChanged.entrySet()) {
            variables.remove(entry.getKey());
            variables.add(entry.getValue());
            for (Rule rule : rules) {
                rule.setLeftSide(rule.getLeftSide().replace(entry.getKey(), entry.getValue()));
                rule.setRightSide(rule.getRightSide().replace(entry.getKey(), entry.getValue()));
            }
        }
    }

    public void simplifyVariables() {
        Map<String, String> variablesChanged = new HashMap<>();
        String nextVariable = nextVariable();
        int lenght = variables.size() / 26;
        for (String var : variables) {
            if (var.length() > nextVariable.length() + lenght) {
                while (variables.contains(nextVariable)) {
                    nextVariable = nextVariable();
                }
                variablesChanged.put(var, nextVariable);
                nextVariable = nextVariable();
            }
        }
        changeVariables(variablesChanged);
    }

    public Map<String, Set<String>> getRulesMapRightToLeft() {
        Map<String, Set<String>> rulesMapRightToLeft = new LinkedHashMap<>();
        Set<String> leftSide;
        for (Rule rule : rules) {
            if (rulesMapRightToLeft.containsKey(rule.getRightSide())) {
                leftSide = rulesMapRightToLeft.get(rule.getRightSide());
                leftSide.add(rule.getLeftSide());
                rulesMapRightToLeft.put(rule.getRightSide(), leftSide);
            } else {
                leftSide = new LinkedHashSet<>();
                leftSide.add(rule.getLeftSide());
                rulesMapRightToLeft.put(rule.getRightSide(), leftSide);
            }
        }
        return rulesMapRightToLeft;
    }

    public String toStringRulesMapRightToLeft() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Set<String>> entry : getRulesMapRightToLeft().entrySet()) {
            sb.append(entry.getKey()).append(" -> ");
            for (String leftSide : entry.getValue()) {
                sb.append(leftSide).append(" | ");
            }
            sb.delete(sb.length() - 3, sb.length());
            sb.append("\n");
        }
        return sb.toString();
    }

    private String toStringCollection(Set<String> collection) {
        StringBuilder sb = new StringBuilder("{ ");
        for (String o : collection) {
            sb.append(o)
                    .append(", ");
        }
        sb.deleteCharAt(sb.length() - 2);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public String toString() {
        final String[] parameters =
                getString(
                        R.string.grammar_to_string_parameters
                ).split("#");
        StringBuilder sb = new StringBuilder();
        sb.append(parameters[0]).append(toStringCollection(variables)).append('\n');
        sb.append(parameters[1]).append(toStringCollection(terminals)).append('\n');
        sb.append(parameters[2]).append(this.initialSymbol).append('\n');
        sb.append(parameters[3]).append('\n').append(toStringRulesMapLeftToRight());
        return sb.toString();
    }

    private String replaceStringtoHtml(String str) {
        str = str.replace("<", "&lt;");
        str = str.replace(">", "&gt;");
        return str.replace("\n", "<br>");
    }

    private String toHtmlCollection(Set<String> collection) {
        return replaceStringtoHtml(toStringCollection(collection));
    }

    public Spanned toHtmlFormated() {
        AcademicSupport academic = new AcademicSupport();
        academic.setResult(this);
        return Html.fromHtml(academic.getResult());
    }

    public Spanned toHtml() {
        final String[] parameters =
                getString(
                        R.string.grammar_to_string_parameters
                ).split("#");
        StringBuilder sb = new StringBuilder()
                .append("<b>")
                .append(parameters[0])
                .append("</b>")
                .append(toHtmlCollection(variables))
                .append("<br>")
                .append("<b>")
                .append(parameters[1])
                .append("</b>")
                .append(toHtmlCollection(terminals))
                .append("<br>")
                .append("<b>")
                .append(parameters[2])
                .append("</b>")
                .append(replaceStringtoHtml(initialSymbol))
                .append("<br>")
                .append("<b>")
                .append(parameters[3])
                .append("</b><br>")
                .append(replaceStringtoHtml(toStringRulesMapLeftToRight()));
        return Html.fromHtml(sb.toString());
    }

}
