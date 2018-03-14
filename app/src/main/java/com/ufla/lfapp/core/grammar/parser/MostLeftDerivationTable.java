package com.ufla.lfapp.core.grammar.parser;


import com.ufla.lfapp.core.grammar.Grammar;
import com.ufla.lfapp.core.grammar.GrammarParser;
import com.ufla.lfapp.core.grammar.Rule;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by carlos on 2/15/17.
 */

public class MostLeftDerivationTable {

    private Map<String, Integer> variableToIndice;
    private Map<String, Integer> terminalToIndice;
    private Set<Rule>[][] table;

    public MostLeftDerivationTable(Grammar grammar) {
        fillTable(grammar);
        //System.out.println(toString());
    }

    public Deque<Rule> getRules(String variable, String terminalLeft) {
        Deque<Rule> rules = new ArrayDeque<>();
        int y = variableToIndice.get(variable);
        Integer x = terminalToIndice.get(terminalLeft);
        rules.addAll(table[y][x]);
        if (!terminalLeft.equals(Grammar.LAMBDA)) {
            x = terminalToIndice.get(Grammar.LAMBDA);
            rules.addAll(table[y][x]);
        }
        return rules;
    }


    private void initTable(int lines, int columns) {
        table = new HashSet[lines][columns];
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < columns; j++) {
                table[i][j] = new HashSet<>();
            }
        }
    }

    private void setVariableIndices(Set<String> variables) {
        int indCont = 0;
        variableToIndice = new HashMap<>();
        for (String variable : variables) {
            variableToIndice.put(variable, indCont);
            indCont++;
        }
    }

    private void setTerminalIndices(Set<String> terminals) {
        int indCont = 0;
        terminalToIndice = new HashMap<>();
        for (String terminal : terminals) {
            terminalToIndice.put(terminal, indCont);
            indCont++;
        }
        terminalToIndice.put(Grammar.LAMBDA, indCont);
    }

    private Set<String> setNullable(Grammar grammar) {
        Set<String> nullable = new TreeSet<>();
        int x = terminalToIndice.get(GrammarParser.LAMBDA);
        for (Map.Entry<String, Integer> entry : variableToIndice.entrySet()) {
            String variable = entry.getKey();
            Rule ruleNullable = grammar.getLambdaRule(variable);
            if (ruleNullable != null) {
                nullable.add(variable);
                int y = entry.getValue();
                table[y][x].add(ruleNullable);
            }
        }
        Set<String> prevNullable = new HashSet<>();
        do {
            prevNullable.addAll(nullable);
            for (Map.Entry<String, Integer> entry : variableToIndice.entrySet()) {
                String variable = entry.getKey();
                if (prevNullable.contains(variable)) {
                    continue;
                }
                Set<Rule> rules = grammar.getRulesThatOnlyGenerates(variable, prevNullable);
                if (rules != null && !rules.isEmpty()) {
                    nullable.add(variable);
                    int y = entry.getValue();
                    table[y][x].addAll(rules);
                }
            }
        } while (!prevNullable.equals(nullable));
        return nullable;
    }

    private void fillTable(Grammar grammar) {
        Set<String> terminals = grammar.getTerminals();
        Set<String> variables = grammar.getVariables();
        initTable(variables.size(), terminals.size() + 1);
        setVariableIndices(variables);
        setTerminalIndices(terminals);
        Set<String> nullable = setNullable(grammar);
        for (String terminal : terminals) {
            Set<String> leftGenerate = new HashSet<>();
            int x = terminalToIndice.get(terminal);
            for (Map.Entry<String, Integer> entry : variableToIndice.entrySet()) {
                String variable = entry.getKey();
                Set<Rule> rules = grammar.getRulesWithFirstProduces(variable, terminal, nullable);
                if (rules != null && !rules.isEmpty()) {
                    leftGenerate.add(variable);
                    int y = entry.getValue();
                    table[y][x].addAll(rules);
                }
            }
            Set<String> leftGenerateNew;
            boolean rulesAdd;
            do {
                leftGenerateNew = new HashSet<>();
                rulesAdd = false;
                for (Map.Entry<String, Integer> entry : variableToIndice.entrySet()) {
                    String variable = entry.getKey();
                    Set<Rule> rules = grammar.getRulesWithFirstProduces(variable, leftGenerate,
                            nullable);
                    if (rules != null && !rules.isEmpty()) {
                        leftGenerateNew.add(variable);
                        int y = entry.getValue();
                        if (table[y][x].addAll(rules)) {
                            rulesAdd = true;
                        }
                    }
                }
                leftGenerate.addAll(leftGenerateNew);
            } while (!leftGenerateNew.isEmpty() && rulesAdd);
        }
        changeTable();
    }

    private void changeTable() {
        int lin = table.length;
        int col = table[0].length;
        Set<Rule> tableOrdered[][] = new TreeSet[lin][col];
        for (int i = 0; i < lin; i++) {
            for (int j = 0; j < col; j++) {
                tableOrdered[i][j] = new TreeSet<>(new RuleCompForLeftDerivation());
                tableOrdered[i][j].addAll(table[i][j]);
            }
        }
        table = tableOrdered;
    }

    /**
     * Retorna a lista de variáveis na ordem da criação da tabela.
     *
     * @return lista de variáveis na ordem da criação da tabela.
     */
    private List<String> getVariables() {
        int lin = table.length;
        String[] variables = new String[lin];
        for (Map.Entry<String, Integer> entry : variableToIndice.entrySet()) {
            variables[entry.getValue()] = entry.getKey();
        }
        return new ArrayList<>(Arrays.asList(variables));
    }

    /**
     * Retorna a lista de terminais na ordem da criação da tabela.
     *
     * @return lista de terminais na ordem da criação da tabela.
     */
    private List<String> getTerminals() {
        int lin = table[0].length;
        String[] terminals = new String[lin];
        for (Map.Entry<String, Integer> entry : terminalToIndice.entrySet()) {
            terminals[entry.getValue()] = entry.getKey();
        }
        return new ArrayList<>(Arrays.asList(terminals));
    }

    private class TableString {

        String[][] tableString;
        int maxLength;

        TableString() {
            init();
        }

        void updateLengthValue(String str) {
            int length = str.length();
            if (length > maxLength) {
                maxLength = length;
            }
        }

        String getStringCell(Set<Rule> cell) {
            if (cell == null || cell.isEmpty()) {
                return "{}";
            }
            StringBuilder sb = new StringBuilder();
            sb.append('{');
            for (Rule rule : cell) {
                sb.append(rule.getRightSide())
                        .append(", ");
            }
            int lastIndex = sb.length() - 1;
            sb.deleteCharAt(lastIndex);
            lastIndex--;
            sb.deleteCharAt(lastIndex);
            sb.append('}');
            String cellStr = sb.toString();
            updateLengthValue(cellStr);
            return cellStr;
        }

        void init() {
            maxLength = 2;
            int lin = table.length;
            int col = table[0].length;
            tableString = new String[lin][col];
            for (int i = 0; i < lin; i++) {
                for (int j = 0; j < col; j++) {
                    tableString[i][j] = getStringCell(table[i][j]);
                }
            }
            maxLength++;
            updateLengthCells();
        }

        String getStringWithMaxLength(String str) {
            StringBuilder sb = new StringBuilder(str);
            int length = sb.length();
            sb.ensureCapacity(maxLength);
            while (length < maxLength) {
                sb.append(' ');
                length++;
            }
            return sb.toString();
        }

        void updateLengthCells() {
            int lin = tableString.length;
            int col = tableString[0].length;
            for (int i = 0; i < lin; i++) {
                for (int j = 0; j < col; j++) {
                    tableString[i][j] = getStringWithMaxLength(tableString[i][j]);
                }
            }
        }

        List<String> getTerminalsMaxLength() {
            List<String> terminals = getTerminals();
            List<String> terminalsMaxLength = new ArrayList<>(terminals.size());
            for (String terminal : terminals) {
                terminalsMaxLength.add(getStringWithMaxLength(terminal));
            }
            return terminalsMaxLength;
        }

    }

    @Override
    public String toString() {
        int lin = table.length;
        int col = table[0].length;
        TableString tableString = new TableString();
        String tableStr[][] = tableString.tableString;
        List<String> variables = getVariables();
        List<String> terminals = tableString.getTerminalsMaxLength();
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        for (String terminal : terminals) {
            sb.append(terminal);
        }
        sb.append('\n');
        for (int i = 0; i < lin; i++) {
            sb.append(variables.get(i))
                    .append(' ');
            for (int j = 0; j < col; j++) {
                sb.append(tableStr[i][j]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
