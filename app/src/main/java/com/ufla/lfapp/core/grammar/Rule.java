package com.ufla.lfapp.core.grammar;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Rule implements Cloneable, Comparable<Rule> {

    //attributes
    private String leftSide;
    private String rightSide;

    //builders
    public Rule() {
        this("", "");
    }

    public Rule(Rule r) {
        this(r.getLeftSide(), r.getRightSide());
    }

    public Rule(String left, String right) {
        super();
        this.leftSide = left;
        this.rightSide = right;
    }

    //methods
    //accessors

    public String getLeftSide() {
        return leftSide;
    }

    public void setLeftSide(String leftSide) {
        this.leftSide = leftSide;
    }

    public String getRightSide() {
        return rightSide;
    }

    public void setRightSide(String rightSide) {
        this.rightSide = rightSide;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((leftSide == null) ? 0 : leftSide.hashCode());
        result = prime * result
                + ((rightSide == null) ? 0 : rightSide.hashCode());
        return result;
    }

    public boolean useTerm(String term) {
        return leftSide.contains(term) || rightSide.contains(term);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Rule other = (Rule) obj;
        if (leftSide == null) {
            if (other.leftSide != null) {
                return false;
            }
        } else if (!leftSide.equals(other.leftSide)) {
            return false;
        }
        if (rightSide == null) {
            if (other.rightSide != null) {
                return false;
            }
        } else if (!rightSide.equals(other.rightSide)) {
            return false;
        }
        return true;
    }

    @Override
    protected Object clone() {
        Rule rc = new Rule();

        rc.setLeftSide(this.leftSide);
        rc.setRightSide(this.rightSide);

        return rc;
    }

    @Override
    public String toString() {
        return this.leftSide + " → " + this.rightSide;
    }

    /**
     * Compara duas regras, onde primeiramente compara o seu lado esquerdo e
     * se forem iguais compara seu lado direito.
     *
     * @param another regra especificada à ser comparada.
     * @return inteiro positivo se a regra é maior que a especificada, 0 se
     * as regras são iguais e inteiro negativo se a regra é menor que a regra
     * especificada .
     */
    @Override
    public int compareTo(@NonNull Rule another) {
        if (!leftSide.equals(another.leftSide)) {
            return leftSide.compareTo(another.leftSide);
        }
        return rightSide.compareTo(another.rightSide);
    }

    public static boolean isDigitOrApostrophe(char c) {
        return Character.isDigit(c) || c == '\'';
    }

    /**
     * Verifica se o lado direito da regra contém um determinado símbolo.
     *
     * @param symbol símbolo a ser verificado se está contido no lado direito
     *               da regra.
     * @return true se o lado direito da regra contém o símbolo, e caso
     * contrário false.
     */
    public boolean rightSideContainsSymbol(String symbol) {
        if (rightSide.contains(symbol)) {
            int index = rightSide.indexOf(symbol);
            while (index != -1) {
                if (rightSide.length() == index + symbol.length()) {
                    return true;
                }
                if (rightSide.length() > index + symbol.length() &&
                        !isDigitOrApostrophe(rightSide.charAt(index + symbol.length()))) {
                    return true;
                }
                index += symbol.length();
                index = rightSide.indexOf(symbol, index);
            }
        }
        return false;
    }

    /**
     * Verifica se a regra está na forma normal de Chomsky (FNC).
     *
     * @param initialSymbol símbolo inicial da gramática na qual a regra está
     *                      contida.
     * @return true se a regra está na forma normal de Chomsky, e caso
     * contrário false.
     */
    public boolean isFnc(String initialSymbol) {
        if (rightSide.equals(Grammar.LAMBDA)) {
            return leftSide.equals(initialSymbol);
        }
        if (rightSideContainsSymbol(initialSymbol)) {
            return false;
        }
        if (rightSide.length() == 1) {
            return Character.isLowerCase(rightSide.charAt(0));
        }
        int index = 0;
        if (!Character.isUpperCase(rightSide.charAt(index++))) {
            return false;
        }
        while (isDigitOrApostrophe(rightSide.charAt(index))) {
            index++;
            if (index == rightSide.length()) {
                return false;
            }
        }
        if (!Character.isUpperCase(rightSide.charAt(index++))) {
            return false;
        }
        if (index == rightSide.length()) {
            return true;
        }
        while (index != rightSide.length() &&
                isDigitOrApostrophe(rightSide.charAt(index))) {
            index++;
        }
        return index == rightSide.length();
    }

    /**
     * Verifica se a regra está na forma normal de Greibach (FNG).
     *
     * @param initialSymbol símbolo inicial da gramática na qual a regra está
     *                      contida.
     * @return true se a regra está na forma normal de Greibach, e caso
     * contrário false.
     */
    public boolean isFng(String initialSymbol) {
        if (rightSide.equals(Grammar.LAMBDA)) {
            return leftSide.equals(initialSymbol);
        }
        if (rightSideContainsSymbol(initialSymbol)) {
            return false;
        }
        if (rightSide.length() == 1) {
            return Character.isLowerCase(rightSide.charAt(0));
        }
        if (Character.isLowerCase(rightSide.charAt(0))) {
            for (int i = 1; i < rightSide.length(); i++) {
                if (!(isDigitOrApostrophe(rightSide.charAt(i)) ||
                        Character.isUpperCase(rightSide.charAt(i)))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Verifica se há recursão direta à esquerda.
     *
     * @return true se há recursão direta à esquerda, e caso
     * contrário false.
     */
    public boolean existsLeftRecursion() {
        if (rightSide.length() > leftSide.length()) {
            return rightSide.startsWith(leftSide) &&
                    !isDigitOrApostrophe(rightSide.charAt(leftSide.length()));
        }
        return rightSide.length() == leftSide.length() &&
                rightSide.equals(leftSide);
    }

    /**
     * Busca a primeira variável no lado direito da regra.
     *
     * @return retorna a primeira variável no lado direito da regra, se a
     * regra começa com um terminal retorna null.
     */
    public String getFirstVariableOfRightSide() {
        if (!Character.isUpperCase(rightSide.charAt(0))) {
            return null;
        }
        int index = 0;
        while (index + 1 != rightSide.length() &&
                isDigitOrApostrophe(rightSide.charAt(index + 1))) {
            index++;
        }
        return rightSide.substring(0, index + 1);
    }


    /**
     * Busca o conjunto de símbolos presentes (terminais e não-terminais) no
     * lado direito
     * da regra.
     *
     * @return retorna o conjunto de símbolos presentes no lado direito da
     * regra.
     */
    public Set<String> getSymbolsOfRightSide() {
        return new HashSet<>(getListOfSymbolsOnRightSide());
    }

    public List<String> getListOfSymbolsOnRightSide() {
        List<String> symbolsOfRightSide = new ArrayList<>();
        if (rightSide.equals(Grammar.LAMBDA)) {
            symbolsOfRightSide.add(rightSide);
            return symbolsOfRightSide;
        }
        for (int i = 0; i < rightSide.length(); i++) {
            if (Character.isLowerCase(rightSide.charAt(i))) {
                symbolsOfRightSide.add(Character.toString(rightSide.charAt(i)));
            } else if (Character.isUpperCase(rightSide.charAt(i))) {
                int sizeOfSymbol = 1;
                while (i + sizeOfSymbol < rightSide.length() &&
                        isDigitOrApostrophe(rightSide.charAt(i + sizeOfSymbol))) {
                    sizeOfSymbol++;
                }
                symbolsOfRightSide.add(rightSide.substring(i, i + sizeOfSymbol));
                i += sizeOfSymbol - 1;
            }
        }
        return symbolsOfRightSide;
    }


    /**
     * Verifica se há recursão.
     *
     * @return true se há recursão, e caso
     * contrário false.
     */
    public boolean existsRecursion() {
        return rightSideContainsSymbol(leftSide);
    }

    /**
     * Verifica se produz lambda.
     *
     * @return true se produz lambda, e caso
     * contrário false.
     */
    public boolean producesLambda() {
        return rightSide.equals(Grammar.LAMBDA);
    }

    /**
     * Verifica se produz terminal diretamente.
     *
     * @return true se produz terminal diretamente, e caso
     * contrário false.
     */
    public boolean producesTerminalDirectly() {
        if (rightSide.equals(Grammar.LAMBDA)) {
            return false;
        }
        for (int i = 0; i < rightSide.length(); i++) {
            if (!Character.isLowerCase(rightSide.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifica se é uma regra de cadeia.
     *
     * @return true é uma regra de cadeia, e caso
     * contrário false.
     */
    public boolean isChainRule() {
        if (!Character.isUpperCase(rightSide.charAt(0))) {
            return false;
        }
        int index = 1;
        while (index != rightSide.length() &&
                isDigitOrApostrophe(rightSide.charAt(index))) {
            index++;
        }
        return index == rightSide.length();
    }

    public boolean producesOnlyOneTerminal() {
        return rightSide.length() == 1 && Character.isLowerCase(rightSide.charAt(0));
    }

    public boolean producesOnlyTerminal() {
        for (int i = 0; i < rightSide.length(); i++) {
            if (Character.isUpperCase(rightSide.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean containsTerminalOnRightSide() {
        for (int i = 0; i < rightSide.length(); i++) {
            if (Character.isLowerCase(rightSide.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public int getNumberOfSymbolsInRightSide() {
        return getNumberOfSymbolsInRightSide(rightSide);
    }

    public static int getNumberOfSymbolsInRightSide(String rightSide) {
        int cont = 0;
        for (int i = 0; i < rightSide.length(); i++) {
            if (Character.isLetter(rightSide.charAt(i))) {
                cont++;
            }
        }
        return cont;
    }


    public String getRightSideToHtml() {
        return GrammarParser.varToHtml(rightSide);
    }

}
