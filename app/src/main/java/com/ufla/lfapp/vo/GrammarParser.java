package com.ufla.lfapp.vo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    public static Set<String> extractVariablesFromFull(String txt) {
        Set<String> variables = new HashSet<>();
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
    public static Set<String> extractTerminalsFromFull(String txt) {
        Set<String> terminals = new HashSet<>();
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
    public static Set<Rule> extractRulesFromFull(String txt) {
        Set<Rule> rules = new HashSet<>();
        Rule rule = new Rule();
        String[] auxRule;
        for (String x : txt.split("\n")) {
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
    public static Set<Rule> updateRules(Set<String> prev, Grammar g, final AcademicSupport academic) {
        Set<Rule> newRules = new HashSet<>();
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
    public static Set<String> updateTerminals(Grammar g) {
        Set<String> newTerminals = new HashSet<>();
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
     * Verifica a existência de determinada produção.
     *
     * @param leftSide
     * @param symbol
     * @param newSetOfRules
     * @return
     */
    public static boolean existsProduction(String leftSide, String symbol, Set<Rule> newSetOfRules) {
        for (Rule element : newSetOfRules) {
            if (element.getRightSide().equals(symbol)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retorna determinada variável.
     *
     * @param symbol
     * @param newSetOfRules
     * @return
     */
    public static String getVariable(String symbol, Set<Rule> newSetOfRules) {
        String variable = "";
        boolean found = false;
        for (Rule element : newSetOfRules) {
            String[] aux = element.getRightSide().split(" | ");
            for (int i = 0; i < aux.length && !found; i++) {
                aux[i] = aux[i].trim();
                if (aux[i].equals(symbol)) {
                    variable = element.getLeftSide();
                    found = true;
                }
            }
        }
        return variable;
    }

    /**
     * Verifica o tamanho de uma dada sentença.
     *
     * @param sentence
     * @return
     */
    static int sentenceSize(String sentence) {
        int count = 0;
        for (int i = 0; i < sentence.length(); i++) {
            if (Character.isLetter(sentence.charAt(i))) {
                count++;
            }
        }
        return count;
    }

    /**
     * Atualiza o número de inserções realizadas.
     *
     * @param newSetOfRules
     * @param contInsertions
     * @return
     */
    static int updateNumberOfInsertions(Set<Rule> newSetOfRules,
                                        int contInsertions) {
        int counter = 0;
        for (Rule element : newSetOfRules) {
            if (element.getLeftSide().contains("T")) {
                String aux = element.getLeftSide().substring(1);
                if (!aux.isEmpty() && Integer.parseInt(aux) > counter) {
                    counter = Integer.parseInt(aux);
                }
            }
        }
        return (counter + 1);
    }

    /**
     * Verifica se é possível realizar uma nova inserção.
     *
     * @param sentence
     * @return
     */
    static boolean canInsert(String sentence) {
        String[] productions = sentence.split(" | ");
        boolean insert = false;
        int contNumbers = 0;
        String target = productions[productions.length - 1];
        for (int i = 0; i < target.length(); i++) {
            if (Character.isDigit(target.charAt(i))) {
                contNumbers++;
            }
        }
        if ((contNumbers == 0 && target.length() != 2)
                || (contNumbers == 1 && target.length() != 3)
                || (contNumbers == 2 && target.length() != 4)) {
            insert = true;
        }
        return insert;
    }

    public static boolean isFNC(final Grammar gc) {
        for (Rule rule : gc.getRules()) {
            if (!rule.isFnc(gc.getInitialSymbol())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Realiza split de uma determinada sentença.
     *
     * @param newSentence
     * @return
     */
    static String splitSentence(String newSentence) {
        if (newSentence.charAt(0) != 'T') {
            newSentence = newSentence.substring(1);
        } else {
            newSentence = newSentence.substring(1);
            while (Character.isDigit(newSentence.charAt(0))) {
                newSentence = newSentence.substring(1);
            }
        }
        return newSentence;
    }

    /**
     * Realiza split de uma determinada sentença.
     *
     * @param newSentence
     * @return
     */
    static String splitSentence(int cont, String newSentence, int contInsertions) {
        String aux = "";
        if (newProductionSize(newSentence) == 2) {
            aux = newSentence;
        } else {
            aux = Character.toString(newSentence.charAt(0));
            if (newSentence.charAt(0) == 'T') {
                for (int i = 1; !Character.isLetter(newSentence.charAt(i)); i++) {
                    aux += Character.toString(newSentence.charAt(i));
                }
            }
            aux += "T" + (contInsertions + 1);
        }
        return aux;
    }

    /**
     * @param sentence
     * @return
     */
    static String partialSentence(String sentence) {
        String partial = "";
        if (sentence.charAt(0) == 'T') {
            partial += "T";
            for (int i = 1; !Character.isLetter(sentence.charAt(i)); i++) {
                partial += Character.toString(sentence.charAt(i));
            }
        } else {
            partial = Character.toString(sentence.charAt(0));
        }
        return partial;
    }

    /**
     * Verifica o tamanho de uma nova produção.
     *
     * @param newProduction
     * @return
     */
    private static int newProductionSize(String newProduction) {
        int count = 0;
        for (int i = 0; i < newProduction.length(); i++) {
            if (Character.isLetter(newProduction.charAt(i))) {
                count++;
            }
        }
        return count;
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
     * Verifica se a gramática dada está na forma normal de Greibach.
     *
     * @param rules
     * @return
     */
    static boolean isFNG(Set<Rule> rules) {
        boolean fng = true;
        Iterator<Rule> it = rules.iterator();
        while (it.hasNext() && fng) {
            Rule element = it.next();
            if (!Character.isLowerCase(element.getRightSide().charAt(0)) && !"." .equals(element.getRightSide())) {
                fng = false;
            }
        }
        return fng;
    }


    //cria novas regras realizando as substituições necessárias
    public static Set<Rule> createNewRules(String variable, Grammar gc, Map<String, String> variablesInOrder) {
        Set<Rule> newSetOfRules = new HashSet<>();
        for (Rule element : gc.getRules()) {
            String newProduction;
            if (variable.equals(element.getLeftSide())) {
                int leftValue = Integer.parseInt(variablesInOrder.get(variable));
                int rightValue;
                boolean test = true;
                for (int i = 0; i < element.getRightSide().length() && test; i++) {
                    if (Character.isLetter(element.getRightSide().charAt(i)) && Character.isUpperCase(element.getRightSide().charAt(i))) {
                        rightValue = Integer.parseInt(variablesInOrder.get(determinesRightSide(element.getRightSide(), i)));
                        if (leftValue > rightValue) {
                            test = false;
                            String searchVariable = determinesRightSide(element.getRightSide(), i);
                            for (Rule secondElement : gc.getRules()) {
                                if (searchVariable.equals(secondElement.getLeftSide())) {
                                    newProduction = element.getRightSide();
                                    newProduction = newProduction.replace(searchVariable, secondElement.getRightSide());
                                    Rule r = new Rule(element.getLeftSide(), newProduction);
                                    newSetOfRules.add(r);
                                }
                            }
                        }
                    } else if (Character.isLowerCase(element.getRightSide().charAt(i))) {
                        Rule r = new Rule(variable, element.getRightSide());
                        newSetOfRules.add(r);
                    }
                }
                if (test) {
                    Rule r = new Rule(variable, element.getRightSide());
                    newSetOfRules.add(r);
                }
            }
        }
        return newSetOfRules;
    }

    //determina qual variável é a primeira de determinada produção
    public static String determinesRightSide(String rightSide, int counter) {
        while (Character.isDigit(rightSide.charAt(counter))) {
            counter++;
        }
        String variable = Character.toString(rightSide.charAt(counter));
        boolean test = true;
        while (rightSide.length() > counter + 1 && test) {
            if (Character.isDigit(rightSide.charAt(counter + 1))) {
                variable += Character.toString(rightSide.charAt(counter + 1));
            }
            counter++;
            if (Character.isLetter(rightSide.charAt(counter))) {
                test = false;
            }
        }
        return variable;
    }


    /**
     * @param g: gramática livre de contexto
     * @return automaton: gramática livre de contexto convertida em autômato de pilha
     */
    public static PushdownAutomaton turnsGrammarToPushdownAutomata(final Grammar g) {
        Grammar gc = (Grammar) g.clone();
        AcademicSupport academic = new AcademicSupport();

        if (!isFNG(gc.getRules())) {
            gc = g.FNG(gc, academic);
        }

        PushdownAutomaton automaton = new PushdownAutomaton();

        if (isFNG(gc.getRules())) {
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


    public static Set<String>[][] fillOthersLines(Set<String>[][] x, Grammar g, int count, int line, int column, String word) {

        int counterLine = 1;
        int counterColumn = 1;
        int targetLine = word.length() - 3;
        int targetColumn = 0;
        int counterLineOfSecondElement = line - 1;
        int delimiter = word.length() - 2;
        int auxFlag = 1;

        int lineFirstElement = line;
        int lineSecondElement = line - 1;
        int columnFirstElement = column;
        int columnSecondElement = column + 1;


        while (count != word.length() + 1) {


            while (counterColumn + auxFlag != word.length()) {
                while (lineFirstElement >= delimiter) {
                    String firstCell = returnsAlphabeticSymbols(x[lineFirstElement][columnFirstElement]);
                    String secondCell = returnsAlphabeticSymbols(x[lineSecondElement][columnSecondElement]);
                    for (int counterOfFirstCell = 0; counterOfFirstCell < firstCell.length(); ) {
                        String sentence = "";
                        //sentence += Character.toString(firstCell.charAt(counterOfFirstCell)).trim();
                        sentence += firstCell.substring(counterOfFirstCell, getLengthOfSentence(firstCell, counterOfFirstCell));
                        for (int counterOfSecondCell = 0; counterOfSecondCell < secondCell.length(); ) {
                            //sentence += Character.toString(secondCell.charAt(counterOfSecondCell)).trim();
                            sentence += secondCell.substring(counterOfSecondCell, getLengthOfSentence(secondCell, counterOfSecondCell));
                            Set<String> aux = checksEquality(g, sentence);
                            x[targetLine][targetColumn].addAll(aux);
                            //sentence = sentence.substring(0, sentence.length() - 1);
                            sentence = sentence.substring(0, getIndexOfLastVariable(sentence));
                            counterOfSecondCell = updateCounter(secondCell, counterOfSecondCell);
                        }
                        counterOfFirstCell = updateCounter(firstCell, counterOfFirstCell);
                    }
                    lineFirstElement--;
                    lineSecondElement++;
                    columnSecondElement++;
                }
                targetColumn++;
                columnFirstElement++;
                lineFirstElement = line;
                lineSecondElement = line - counterLine;
                counterColumn++;
                columnSecondElement = column + counterColumn;
            }
            auxFlag++;
            counterLine++;
            counterLineOfSecondElement--;
            lineFirstElement = line;
            columnFirstElement = column;
            lineSecondElement = counterLineOfSecondElement;
            columnSecondElement = column + 1;
            targetColumn = 0;
            targetLine--;
            count++;
            delimiter--;
            counterColumn = 1;

        }
        return x;
    }

    static Set<String>[][] fillSecondLine(Set<String>[][] x, Grammar g,
                                          String word) {
        for (int j = 0; j < word.length() - 1; j++) {
            String firstCell = returnsAlphabeticSymbols(x[word.length() - 1][j]);
            String secondCell = returnsAlphabeticSymbols(x[word.length() - 1][j + 1]);
            int counterOfFirstCell = 0;
            while (counterOfFirstCell < firstCell.length()) {
                String sentence = "";
                //counterOfFirstCell = getBeginOfSentence(firstCell, counterOfFirstCell);
                sentence += firstCell.substring(counterOfFirstCell, getLengthOfSentence(firstCell, counterOfFirstCell));
                int counterOfSecondCell = 0;
                while (counterOfSecondCell < secondCell.length()) {
                    sentence += secondCell.substring(counterOfSecondCell, getLengthOfSentence(secondCell, counterOfSecondCell));
                    Set<String> aux = checksEquality(g, sentence);
                    x[word.length() - 2][j].addAll(aux);
                    sentence = sentence.substring(0, getIndexOfLastVariable(sentence));
                    counterOfSecondCell = updateCounter(secondCell, counterOfSecondCell);
                }
                counterOfFirstCell = updateCounter(firstCell, counterOfFirstCell);
            }
        }
        return x;
    }

    private static int updateCounter(final String cell, final int current) {
        int index = current;
        if (Character.isDigit(cell.charAt(index))) {
            while (index < cell.length() && Character.isDigit(index)) {
                index++;
            }
        } else {
            index++;
            while (index < cell.length() && Character.isDigit(cell.charAt(index))) {
                index++;
            }
        }
        return index;
    }

    private static int getIndexOfLastVariable(final String sentence) {
        int lenght = sentence.length() - 1;
        while (Character.isDigit(sentence.charAt(lenght))) {
            lenght--;
        }
        return lenght;
    }


    private static int getLengthOfSentence(final String cell, final int begin) {
        int end = begin + 1;
        while (end < cell.length() && Character.isDigit(cell.charAt(end))) {
            end++;
        }
        return end;
    }

    //remove caracteres que não sejam alfabéticos
    private static String returnsAlphabeticSymbols(Set<String> set) {
        String aux = "";
        for (String element : set) {
            if (Character.isLetter(element.charAt(0))) {
                aux += element;
            }
        }
        return aux;
    }

    //preenche a primeira linha da tabela
    static Set<String>[][] fillFirstLine(Set<String>[][] x, Grammar g, String word) {
        for (int j = 0; j < word.length(); j++) {
            x[word.length() - 1][j] = checksEquality(g,
                    Character.toString(word.charAt(j)));
        }
        return x;
    }

    private static Set<String> checksEquality(Grammar g, String letter) {
        Set<String> found = new TreeSet<>();
        for (Rule element : g.getRules()) {
            if (element.getRightSide().equals(letter)) {
                found.add(element.getLeftSide());
            }
        }
        return found;
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