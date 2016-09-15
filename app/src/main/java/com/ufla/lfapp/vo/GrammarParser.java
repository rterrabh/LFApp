package com.ufla.lfapp.vo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;


public class GrammarParser {

    //"[["+TERMINAL_REGEX+"]*"+"["+VARIABLE_REGEX+"]*]*";
    public static final String LAMBDA = "λ";
    public static final String VARIABLE_REGEX = "([A-Z](([0-9]*)|('?)))";
    public static final String TERMINAL_REGEX = "[a-z]";
    public static final String RULE_ELEMENT_LEFT_REGEX = "(" + TERMINAL_REGEX + "|" +
            VARIABLE_REGEX + ")+";
    public static final String RULE_ELEMENT_RIGHT_REGEX = "(" + RULE_ELEMENT_LEFT_REGEX + "|"
            + LAMBDA + ")";
    public static final String RULE_REGEX = "\\s*(" + RULE_ELEMENT_LEFT_REGEX + "\\p{Blank}*->" +
            "\\p{Blank}*" + RULE_ELEMENT_RIGHT_REGEX + "(\\p{Blank}*\\|\\p{Blank}*" +
            RULE_ELEMENT_RIGHT_REGEX + ")*)\\s*";
    public static final String GRAMMAR_REGEX = "(" + RULE_REGEX + "" +
            "(\\p{Blank}*\\n\\p{Blank}*" + RULE_REGEX + ")*)";

    private GrammarParser() {

    }

    /**
     * Extrai variáveis da gramática
     *
     * @param txt : gramática informada
     * @return : variáveis extraídas
     */
    public static SortedSet<String> extractVariablesFromFull(String txt) {
        SortedSet<String> variables = new TreeSet<>();
        for (int i = 0; i < txt.length(); ) {
            if (Character.isUpperCase(txt.charAt(i))) {
                int k = i+1;
                while (k != txt.length() && (Character.isDigit(txt.charAt(k)) ||
                        txt.charAt(k) == '\'')) {
                    k++;
                }
                variables.add(txt.substring(i, k));
                i = k;
            } else {
                i++;
            }
        }
        return variables;
    }

    /**
     * Extrai terminais da gramática
     *
     * @param txt : gramática informada
     * @return : terminais extraídos
     */
    public static SortedSet<String> extractTerminalsFromFull(String txt) {
        SortedSet<String> terminals = new TreeSet<>();
        for (int i = 0; i < txt.length(); i++) {
            if (Character.isLowerCase(txt.charAt(i))) {
                terminals.add(Character.toString(txt.charAt(i)));
            }
        }
        return terminals;
    }

    /**
     * Extrai regras da gramática
     *
     * @param txt : gramática informada
     * @return : regras extraídas
     */
    public static SortedSet<Rule> extractRulesFromFull(String txt) {
        SortedSet<Rule> rules = new TreeSet<>(new RuleComparator(extractInitialSymbolFromFull(txt)));
        Rule rule = new Rule();
        String[] auxRule;
        for (String x : txt.trim().split("\n")) {
            auxRule = x.split("->");
            rule.setLeftSide(auxRule[0].trim());
            //ArrayIndexOutOfBoundsException
            String[] rulesOnRightSide = auxRule[1].split("[|]");
            for (int i = 0; i < rulesOnRightSide.length; i++) {
                rulesOnRightSide[i] = rulesOnRightSide[i].trim();
                if (!rulesOnRightSide[i].isEmpty()) {
                    rule.setRightSide(rulesOnRightSide[i]);
                    rules.add(new Rule(rule));
                }
            }
        }
        return rules;
    }

    /**
     * Extrai símbolo inicial da gramática
     *
     * @param txt : gramática informada
     * @return : símbolo inicial extraído
     */
    public static String extractInitialSymbolFromFull(String txt) {
        int i = 0;
        while (txt.charAt(i) != ' ' && txt.charAt(i) != '-') {
            i++;
        }
        return txt.substring(0, i);
    }


    public static boolean verifyInputGrammar(String txtGrammar) {
        return txtGrammar.matches(GRAMMAR_REGEX);
    }


    public static boolean inputValidate(final String txtGrammar, final StringBuilder reason) {
        Set<String> setOfVariables = new HashSet<>();

        StringTokenizer token1 = new StringTokenizer(txtGrammar, "\n");
        while (token1.hasMoreTokens()) {
            String rule = token1.nextToken().trim();
            StringTokenizer sides = new StringTokenizer(rule, "->");
            int cont = 0;
            while (sides.hasMoreTokens() && cont == 0) {
                String leftSide = sides.nextToken().trim();
                setOfVariables.add(leftSide);
                cont++;
            }
        }

        StringBuilder variable = new StringBuilder();
        for (int i = 0; i < txtGrammar.length(); i++) {
            if (Character.isLetter(txtGrammar.charAt(i)) && Character.isUpperCase(txtGrammar.charAt(i))) {
                variable.append(txtGrammar.charAt(i));
                int j = i + 1;
                while (j < txtGrammar.length() && Character.isDigit(txtGrammar.charAt(j))) {
                    variable.append(txtGrammar.charAt(j++));
                }
                i = j;
                if (!setOfVariables.contains(variable.toString())) {
                    reason.append("Não foram atribuídas produções à variável '")
                            .append(variable).append("'.");
                    return false;
                }
            }
            variable.delete(0, variable.length());
        }
        return true;
    }


    /**
     * Verifica se a gramática informada é regular
     *
     * @param g : Gramática inserida
     * @return true or false
     */
    public static boolean regularGrammar(final Grammar g, final StringBuilder academic) {
        int counter = 0;
        for (Rule r : g.getRules()) {
            for (int i = 0; i < r.getRightSide().length(); i++) {
                if (!g.getVariables().contains(Character.toString(r.getRightSide().charAt(i)))) {
                    continue;
                }
                counter++;
            }

            if (counter == 1) {
                if (!g.getVariables().contains(Character.toString(r.getRightSide().charAt(0))) &&
                        !g.getVariables().contains(Character.toString(r.getRightSide()
                                .charAt(r.getRightSide().length() - 1)))) {
                    academic.append("- Na gramática informada, a ").append(r)
                            .append(" não pertence ao conjunto das gramáticas regulares.\n");
                    return false;
                }
            } else if (counter > 1) {
                academic.append("- Na gramática informada, a regra ").append(r)
                        .append(" não pertence ao conjunto das gramáticas regulares.\n");
                return false;
            }
            counter = 0;
        }
        return true;
    }


    /**
     * Verifica se a gramática informada é livre de contexto
     *
     * @param g : Gramática inserida
     * @return true or false
     */
    public static boolean contextFreeGrammar(final Grammar g, StringBuilder academic) {
        for (Rule r : g.getRules()) {
            if (!g.getVariables().contains(r.getLeftSide())) {
                academic.append("- Na gramática informada, a regra ").append(r).
                        append("não pertence ao conjunto das gramáticas livres de contexto.\n");
                return false;
            }
        }
        return true;
    }

    /**
     * Verifica se a gramática informada é sensível ao contexto
     *
     * @param g : gramática inserida
     * @return
     */
    private static boolean contextSensibleGrammar(final Grammar g, StringBuilder academic) {
        for (Rule element : g.getRules()) {
            if (!containsSentence(g, element.getLeftSide()) || !containsSentence(g, element.getRightSide())
                    || element.getRightSide().length() < element.getLeftSide().length()) {
                academic.append("- Na gramática informada, a regra ").append(element)
                        .append("não pertence ao conjunto das gramáticas sensíveis contexto.\n");
                return false;
            }
        }
        return true;
    }

    /**
     * Verifica se a gramática informada é irrestrita
     *
     * @param g : gramática inserida
     * @return
     */
    private static boolean unrestrictedGrammar(final Grammar g, StringBuilder academic) {
        for (Rule element : g.getRules()) {
            if ((!containsSentence(g, element.getLeftSide()) || element.getLeftSide().equals(Grammar.LAMBDA)) ||
                    (!containsSentence(g, element.getRightSide()) && !element.getRightSide().equals(Grammar.LAMBDA))) {
                academic.append("- Na gramática informada, a regra ").append(element)
                        .append("não pertence ao conjunto das gramáticas irrestritas.\n");
                return false;
            }

        }
        return true;
    }

    /**
     * Verifica se uma sentença está contida no conjunto de variáveis e no conjunto de terminais
     *
     * @param g
     * @param sentence
     * @return
     */
    private static boolean containsSentence(final Grammar g, String sentence) {
        boolean contains = true;
        StringBuilder element = new StringBuilder();
        for (int i = 0; i < sentence.length(); i++) {
            if (!Character.isDigit(sentence.charAt(i))) {
                element.append(sentence.charAt(i));
                int j = i + 1;
                while (j < sentence.length() && Character.isDigit(sentence.charAt(j))) {
                    element.append(sentence.charAt(j));
                    j++;
                }
                if (!(g.getVariables().contains(element.toString()) || g.getTerminals().contains(element.toString()))) {
                    contains = false;
                }
                element.delete(0, element.length());
            }
        }
        return contains;
    }

    /**
     * Classifica a gramática
     *
     * @return Classificação da gramática
     */
    public static String classifiesGrammar(final Grammar g, final StringBuilder academic) {
        if (regularGrammar(g, academic)) {
            return "Logo, a gramática inserida é uma Gramática Regular (GR).";
        }
        if (contextFreeGrammar(g, academic)) {
            return "Logo, a gramática inserida é uma Gramática Livre de Contexto (GLC).";
        }
        if (contextSensibleGrammar(g, academic)) {
            return "Logo, a gramática inserida é uma Gramática Sensível ao Contexto (GSC).";
        }
        if (unrestrictedGrammar(g, academic)) {
            return "Logo, a gramática inserida é uma Gramática Irrestrita GI.";
        }
        return "A gramática informada é inexistente.";
    }


    private static Set<Set<Integer>> combinationsP(List<Integer> groupSize) {
        Set<Set<Integer>> combinations = new HashSet<>();
        for (int i = 1; i <= groupSize.size(); i++) {
            combinations.addAll(combinationsIntern(groupSize, i));
        }
        return combinations;
    }

    private static Set<Set<Integer>> combinationsIntern(List<Integer> groupSize, int k) {

        Set<Set<Integer>> allCombos = new HashSet<>();
        // base cases for recursion
        if (k == 0) {
            // There is only one combination of size 0, the empty team.
            allCombos.add(new HashSet<Integer>());
            return allCombos;
        }
        if (k > groupSize.size()) {
            // There can be no teams with size larger than the group size,
            // so return allCombos without putting any teams in it.
            return allCombos;
        }

        // Create a copy of the group with one item removed.
        List<Integer> groupWithoutX = new ArrayList<>(groupSize);
        Integer x = groupWithoutX.remove(groupWithoutX.size() - 1);

        Set<Set<Integer>> combosWithoutX = combinationsIntern(groupWithoutX, k);
        Set<Set<Integer>> combosWithX = combinationsIntern(groupWithoutX, k - 1);
        for (Set<Integer> combo : combosWithX) {
            combo.add(x);
        }
        allCombos.addAll(combosWithoutX);
        allCombos.addAll(combosWithX);
        return allCombos;
    }

    public static String combination(String rightSide, Set<String> nullableVariables) {
        List<Integer> indiceCombinations = new ArrayList<>();
        for (int j = 0; j < rightSide.length(); j++) {
            if (nullableVariables.contains(Character.toString(rightSide.charAt(j)))) {
                indiceCombinations.add(j);
            }
        }
        Set<Set<Integer>> combinations = combinationsP(indiceCombinations);
        StringBuilder newProduction = new StringBuilder(rightSide + " | ");
        char[] productionArray = rightSide.toCharArray();
        boolean emptyProduction = true;
        for (int i = 0; i < productionArray.length; i++) {
            if (!indiceCombinations.contains(i)) {
                newProduction.append(productionArray[i]);
                emptyProduction = false;
            }
        }
        if (!emptyProduction) {
            newProduction.append(" | ");
        }
        for (Set<Integer> combination : combinations) {
            for (int i = 0; i < productionArray.length; i++) {
                if (!indiceCombinations.contains(i) || combination.contains(i)) {
                    newProduction.append(productionArray[i]);
                }
            }
            newProduction.append(" | ");
        }
        return newProduction.toString();
    }


    /**
     * Verifica a existência de variáveis no conjunto Prev
     *
     * @param prev
     * @param element
     * @return
     */
    public static boolean prevContainsVariable(Set<String> prev, String element) {
        for (int i = 0; i < element.length(); i++) {
            if (!Character.isUpperCase(element.charAt(i)) || !prev.contains(Character.toString(element.charAt(i)))) {
                return false;
            }
        }
        return true;
    }


    /**
     * Retorna os elementos que estão no conjunto Chain mas não estão no conjunto Prev
     *
     * @param chain
     * @param prev
     * @return
     */
    public static Set<String> chainMinusPrev(Set<String> chain, Set<String> prev) {
        Set<String> aux = new HashSet<>();
        for (String element : chain) {
            if (!prev.contains(element)) {
                aux.add(element);
            }
        }
        return aux;
    }

    /**
     * Atualiza as regras da gramática após remover símbolos inúteis
     *
     * @param prev
     * @param g
     * @return
     */
    public static SortedSet<Rule> updateRules(SortedSet<String> prev, Grammar g,
                                              final AcademicSupport academic) {
        SortedSet<Rule> newRules = new TreeSet<>();
        for (Rule element : g.getRules()) {
            if (prev.contains(element.getLeftSide())) {
                String newRule = "";
                boolean insertOnNewRule = true;
                for (int j = 0; j < element.getRightSide().length() && insertOnNewRule; j++) {
                    if (Character.isUpperCase(element.getRightSide().charAt(j))) {
                        insertOnNewRule = prev.contains(Character.toString(element
                                .getRightSide().charAt(j)));
                    } else if (Character.isLowerCase(element.getRightSide().charAt(j))) {
                        insertOnNewRule = true;
                    } else if (element.getRightSide().charAt(j) == '.') {
                        insertOnNewRule = true;
                    } else {
                        insertOnNewRule = false;
                    }
                }
                if (insertOnNewRule) {
                    newRule += element.getRightSide();
                } else {
                    academic.insertIrregularRule(new Rule(element));
                }
                if (newRule.length() != 0) {
                    Rule r = new Rule();
                    r.setLeftSide(element.getLeftSide());
                    r.setRightSide(newRule);
                    newRules.add(r);
                }
            } else {
                academic.insertIrregularRule(new Rule(element));
            }
        }
        return newRules;
    }

    /**
     * Atualiza terminais da gramática após remover símbolos inúteis.
     *
     * @param g
     * @return
     */
    public static SortedSet<String> updateTerminals(Grammar g) {
        SortedSet<String> newTerminals = new TreeSet<>();
        for (Rule element : g.getRules()) {
            for (int i = 0; i < element.getRightSide().length(); i++) {
                if (Character.isLowerCase(element.getRightSide().charAt(i))) {
                    newTerminals.add(Character.toString(element.getRightSide()
                            .charAt(i)));
                }
            }
        }
        return newTerminals;
    }


    /**
     * Retorna os elementos que pertentem ao conjunto Reach mas não pertencem ao conjunto Prev
     *
     * @param reach
     * @param prev
     * @return
     */
    public static Set<String> reachMinusPrev(Set<String> reach, Set<String> prev) {
        Set<String> aux = new HashSet<>();
        for (String element : reach) {
            if (!prev.contains(element)) {
                aux.add(element);
            }
        }
        return aux;
    }

    /**
     * @param reach
     * @param rightSide
     * @return
     */
    public static Set<String> variablesInW(Set<String> reach, String rightSide) {
        for (int i = 0; i < rightSide.length(); i++) {
            if (Character.isUpperCase(rightSide.charAt(i))) {
                reach.add(Character.toString(rightSide.charAt(i)));
            }
        }
        return reach;
    }

    /**
     * Verifica a existência de regras de cadeia.
     *
     * @param element
     * @return
     */
    public static boolean verifyChains(Rule element) {
        boolean chain = true;
        for (int i = 0; i < element.getRightSide().length() && chain == true; i++) {
            if (!element.getLeftSide().equals(Character.toString(element.getRightSide().charAt(i)))) {
                chain = false;
            }
        }
        return chain;
    }

    /**
     * verifica se gramática é não contrátil ou essencialmente não contrátil e se possui recursão no símbolo inicial
     *
     * @param g
     * @return
     */
    static boolean checksGrammar(final Grammar g, AcademicSupport academicSupport) {
        boolean grammarTest = true;
        for (Rule element : g.getRules()) {
            //verifica se é essencialmente não contrátil
            if (element.getRightSide().equals(Grammar.LAMBDA) && !g.getInitialSymbol().equals(element.getLeftSide())) {
                grammarTest = false;
                academicSupport.setSolutionDescription("A gramática inserida possui produções vazias.");
            }
        }
        //verifica se possui recursão no símbolo inicial
        for (Rule element : g.getRules()) {
            if (element.getLeftSide().equals(g.getInitialSymbol()) && element.getRightSide().contains(g.getInitialSymbol())) {
                grammarTest = false;
                academicSupport.setSolutionDescription("A gramática inserida possui recursão no símbolo inicial.");
            }
        }
        //verifica se as produções possuem ciclos
        for (Rule element : g.getRules()) {
            if (verifyChains(element)) {
                grammarTest = false;
                academicSupport.setSolutionDescription("A gramática inserida possui ciclos.\nA regra " + element + " é um ciclo.");
            }
        }
        return grammarTest;
    }


    /**
     * Retorna o índice do primeiro elemento de uma produção
     */
    static int indexOfFirstChar(String rightSide) {
        boolean firstCharacter = true;
        int i = 0;
        if (counterOfRightSide(rightSide) != 1) {
            i = 1;
            while (firstCharacter) {
                if (!Character.isDigit(rightSide.charAt(i))) {
                    firstCharacter = false;
                } else {
                    i++;
                }
            }
        }
        return i;
    }

    /**
     * @param rightSide : string
     * @return : retorna a quantidade de elementos na string
     */
    static int counterOfRightSide(String rightSide) {
        int i = 0;
        int j = 0;
        while (i != rightSide.length()) {
            if (Character.isLetter(rightSide.charAt(i))) {
                j++;
            }
            i++;
        }
        return j;
    }

    /**
     * Verifica se existe recursão direta
     *
     * @param
     */
    static boolean existsDirectRecursion(Grammar g) {
        boolean recursion = false;
        for (String variable : g.getVariables()) {
            for (Rule element : g.getRules()) {
                if (variable.equals(element.getLeftSide())) {
                    if (variable.equals(Character.toString(element.getRightSide().charAt(0)))) {
                        recursion = true;
                    }
                }
            }
        }
        return recursion;
    }

    /**
     * Retorna verdadeiro se existir recursão
     *
     * @param olderVariables
     */
    static boolean existsRecursion(final Grammar g, Map<String, String> variablesInOrder, Set<String> olderVariables) {
        boolean recursion = false;
        for (String variable : g.getVariables()) {
            if (olderVariables.contains(variable)) {
                for (Rule element : g.getRules()) {
                    if (variable.equals(element.getLeftSide()) && Character.isUpperCase(element.getRightSide().charAt(0))) {
                        int u = Integer.parseInt(variablesInOrder.get(variable));
                        String rightSide = getsFirstCharacter(element.getRightSide());
                        int v = Integer.parseInt(variablesInOrder.get(rightSide));
                        if (u >= v) {
                            recursion = true;
                        }
                    }
                }
            }
        }
        return recursion;
    }

    /**
     * Retorna a primeira variável da sentaça à esquerda
     */
    static String getsFirstCharacter(String rightSide) {
        boolean concatenate = true;
        String newRightSide = Character.toString(rightSide.charAt(0));
        for (int i = 1; i < rightSide.length() && concatenate; i++) {
            if (Character.isDigit(rightSide.charAt(i))) {
                newRightSide += Character.toString(rightSide.charAt(i));
            } else {
                concatenate = false;
            }
        }
        return newRightSide;
    }


    /**
     * Verifica se a gramática possui ciclos.
     *
     * @param g
     * @return
     */
    static boolean grammarWithCycles(final Grammar g) {
        boolean cycle = true;
        for (Rule element : g.getRules()) {
            if (verifyChains(element)) {
                cycle = false;
            }
        }
        return cycle;
    }

    /**
     * @param g: gramática livre de contexto
     * @return automaton: gramática livre de contexto convertida em autômato de pilha
     */
    public static PushdownAutomaton turnsGrammarToPushdownAutomata(final Grammar g) {
        Grammar gc = (Grammar) g.clone();
        AcademicSupport academic = new AcademicSupport();

        if (!gc.isFNG()) {
            gc = g.FNG(gc, academic);
        }

        PushdownAutomaton automaton = new PushdownAutomaton();

        if (gc.isFNG()) {
            //inicializando o automato
            //adiciona estado final
            Set<String> finalStates = new HashSet<>();
            finalStates.add("q1");
            automaton.setFinalStates(finalStates);

            //adiciona estado inicial
            automaton.setInitialState("q0");

            //adiciona os estados que serão utilizados
            Set<String> states = new HashSet<>();
            states.add("q0");
            states.add("q1");
            automaton.setStates(states);

            //adiciona alfabeto da pilha
            automaton.setStackAlphabet(gc.getVariables());

            //adiciona alfabeto
            automaton.setAlphabet(gc.getTerminals());

            //adicionando função de transição
            Set<TransitionFunctionPA> transitionTable = new HashSet<>();
            for (Rule element : gc.getRules()) {
                TransitionFunctionPA transitionFunction = new TransitionFunctionPA();
                if (element.getLeftSide().equals(gc.getInitialSymbol())) {
                    transitionFunction.setCurrentState("q0");
                    transitionFunction.setFutureState("q1");
                    transitionFunction.setPops(".");
                    transitionFunction.setSymbol(Character.toString(element.getRightSide().charAt(0)));
                    String stacking = ("." .equals(element.getRightSide()) ? (element.getRightSide()) : (element.getRightSide().substring(1)));
                    transitionFunction.setStacking(stacking);
                } else {
                    transitionFunction.setCurrentState("q1");
                    transitionFunction.setFutureState("q1");
                    transitionFunction.setPops(element.getLeftSide());
                    transitionFunction.setSymbol(Character.toString(element.getRightSide().charAt(0)));
                    transitionFunction.setStacking(element.getRightSide().substring(1));
                }
                transitionTable.add(transitionFunction);
            }
            automaton.setTransictionFunction(transitionTable);
        }

        return automaton;
    }

    public static String[][] turnsTreesetOnArray(Set<String>[][] CYK, String word) {
        String[][] cykOut = new String[word.length() + 1][word.length()];
        for (int i = 0; i < word.length() + 1; i++) {
            for (int j = 0; j < word.length(); j++) {
                if (j <= i) {
                    Set sentence = CYK[i][j];
                    String newSentence = sentence.toString();
                    if (i + 1 == word.length() + 1) {
                        newSentence = newSentence.replace("[", "");
                        newSentence = newSentence.replace("]", "");
                    } else {
                        newSentence = newSentence.replace("[", "{");
                        newSentence = newSentence.replace("]", "}");
                    }
                    newSentence = (newSentence.equals("{}") ? ("-") : (newSentence));
                    cykOut[i][j] = newSentence;
                } else {
                    cykOut[i][j] = "";
                }
            }
        }
        return cykOut;
    }

}