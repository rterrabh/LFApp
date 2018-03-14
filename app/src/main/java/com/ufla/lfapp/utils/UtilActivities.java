package com.ufla.lfapp.utils;

import android.content.Context;
import android.text.Html;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.core.grammar.AcademicSupport;
import com.ufla.lfapp.core.grammar.Grammar;
import com.ufla.lfapp.core.grammar.Rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by root on 21/07/16.
 */
public class UtilActivities {

    public static void logMap(Map<? extends Object, ? extends Object> mapa) {
        StringBuilder sb = new StringBuilder('[');
        for (Map.Entry<? extends Object, ? extends Object> entry : mapa.entrySet()) {
            sb.append(" {")
                    .append(entry.getKey().toString())
                    .append(',')
                    .append(entry.getValue().toString())
                    .append("},");
        }
        sb.deleteCharAt(sb.length() - 1);

    }

    private UtilActivities() {

    }

    /**
     * Método que imprime a gramática com as regras irregulares em destaque
     * @param table
     * @param academic
     */
    public static void printGrammarWithoutOldRules(final Grammar grammar,
                                              TableLayout table,
                                             final AcademicSupport academic,
                                             Context context) {
        TableRow row0 = new TableRow(context);
        TextView left = new TextView(context);
        TextView arrow0 = new TextView(context);
        left.setText(grammar.getInitialSymbol());
        arrow0.setText(" → ");
        row0.addView(left);
        row0.addView(arrow0);
        int contViews = 2;
        for (Rule element : grammar.getRules(grammar.getInitialSymbol())) {
            TextView right = new TextView(context);
            TextView pipe = new TextView(context);
            pipe.setText(" | ");
            if (academic.getIrregularRules().contains(element)) {
                setSubscriptItem(right, element.getRightSide());
                right.setTextColor(context.getResources().getColor(R.color
                        .Red));
            } else if (containsDigit(element.getRightSide())) {
                setSubscriptItem(right, element.getRightSide());
            }else {
                right.setText(element.getRightSide());
            }
            row0.addView(right);
            row0.addView(pipe);
            contViews += 2;
        }
        row0.removeViewAt(contViews - 1);
        table.addView(row0);

        for (String variable : grammar.getVariables()) {
            if (!variable.equals(grammar.getInitialSymbol())) {
                TableRow row1 = new TableRow(context);
                TextView tv0 = new TextView(context);
                setSubscriptItem(tv0, variable);
                row1.addView(tv0);
                TextView arrow1 = new TextView(context);
                arrow1.setText("→");
                row1.addView(arrow1);
                contViews = 2;
                for (Rule element : grammar.getRules()) {
                    if (variable.equals(element.getLeftSide())) {
                        TextView pipe = new TextView(context);
                        pipe.setText(" | ");
                        TextView tv1 = new TextView(context);
                        if (academic.getIrregularRules().contains(element)) {
                            setSubscriptItem(tv1, element.getRightSide());
                            tv1.setTextColor(context.getResources()
                                    .getColor(R.color.Red));
                        } else if(containsDigit(element.getRightSide())) {
                            setSubscriptItem(tv1, element.getRightSide());
                        } else {
                            tv1.setText(element.getRightSide());
                        }
                        row1.addView(tv1);
                        row1.addView(pipe);
                        contViews += 2;
                    }
                }
                row1.removeViewAt(contViews - 1);
                table.addView(row1);
            }
        }
    }

    public static void setSubscriptItem(TextView tv, String element) {
        for (int i = 0; i < element.length(); ) {
            if (Character.isDigit(element.charAt(i))) {
                int index = getNumberOfDigits(i, element);
                tv.append(Html.fromHtml("<sub><small>" + element.substring(i, index) + "</small></sub>"));
                i = index;
            } else {
                StringBuilder text = new StringBuilder();
                text.append(element.charAt(i));
                tv.append(text.toString());
                i++;
            }
        }
    }

    private static boolean containsDigit(String sentence) {
        boolean test = false;
        for (int i = 0; i < sentence.length() && !test; i++) {
            if (Character.isDigit(sentence.charAt(i))) {
                test = true;
            }
        }
        return test;
    }

    /**
     * Método que conta o número de dígitos a partir do índice fornecido
     * @param index
     * @param element
     * @return
     */
    private static int getNumberOfDigits(int index, String element) {
        int i = index;
        for (; i < element.length() && Character.isDigit(element.charAt(i)); i++);
        //i = (i == element.length() ? (i - 1) : (i));
        return i;
    }

    /**
     * Método que imprime a gramática com as novas regras inseridas em destaque
     * @param table
     * @param academic
     */
    public static void printGrammarWithNewRules(final Grammar grammar,
                                                TableLayout table,
                                          final AcademicSupport academic,
                                          Context context) {
        TableRow row0 = new TableRow(context);
        TextView left = new TextView(context);
        TextView arrow0 = new TextView(context);
        left.setText(grammar.getInitialSymbol());
        arrow0.setText(" → ");
        row0.addView(left);
        row0.addView(arrow0);
        int contViews = 2;
        for (Rule element : grammar.getRules(grammar.getInitialSymbol())) {
            TextView right = new TextView(context);
            TextView pipe = new TextView(context);
            pipe.setText(" | ");
            if (academic.getInsertedRules().contains(element)) {
                setSubscriptItem(right, element.getRightSide());
                right.setTextColor(context.getResources().getColor(R.color
                        .Blue));
            } else if (containsDigit(element.getRightSide())) {
                setSubscriptItem(right, element.getRightSide());
            } else {
                right.append(element.getRightSide());
            }
            row0.addView(right);
            row0.addView(pipe);
            contViews += 2;
        }
        row0.removeViewAt(contViews - 1);
        table.addView(row0);

        for (String variable : grammar.getVariables()) {
            if (!variable.equals(grammar.getInitialSymbol())) {
                TableRow row1 = new TableRow(context);
                TextView tv0 = new TextView(context);
                setSubscriptItem(tv0, variable);
                row1.addView(tv0);
                TextView arrow1 = new TextView(context);
                arrow1.setText("→");
                row1.addView(arrow1);
                contViews = 2;
                for (Rule element : grammar.getRules(variable)) {
                        TextView pipe = new TextView(context);
                        pipe.setText(" | ");
                        TextView tv1 = new TextView(context);
                        if (academic.getInsertedRules().contains(element)) {
                            setSubscriptItem(tv1, element.getRightSide());
                            tv1.setTextColor(context.getResources()
                                    .getColor(R
                                    .color.Blue));
                        } else if (containsDigit(element.getRightSide())) {
                            setSubscriptItem(tv1, element.getRightSide());
                        } else {
                            tv1.append(element.getRightSide());
                        }
                        row1.addView(tv1);
                        row1.addView(pipe);
                        contViews += 2;
                }
                row1.removeViewAt(contViews - 1);
                table.addView(row1);
            }
        }
    }


    /**
     * Método que imprime a tabela dos conjuntos utilizados durante a execução do algoritmo
     * @param academic : informaçãos coletadas durante a execução do algoritmo
     */
    public static void printTableOfSets(TableLayout table, final
    AcademicSupport academic,
                                  String nameOfFirstSet, String 
                                          nameOfSecondSet, Context context) {
        int i = 0;
        TableRow header = new TableRow(context);
        TextView htv0 = new TextView(context);
        htv0.setText("");
        htv0.setPadding(10, 10, 10, 10);
        htv0.setTextColor((context).getResources().getColor(R.color.Black));
        TextView htv1 = new TextView(context);
        htv1.setText(nameOfFirstSet);
        htv1.setPadding(10, 10, 10, 10);
        htv1.setBackgroundColor((context).getResources().getColor(R.color
                .DarkGray));
        htv1.setTextColor((context).getResources().getColor(R.color.Black));
        TextView htv2 = new TextView(context);
        htv2.setText(nameOfSecondSet);
        htv2.setPadding(10, 10, 10, 10);
        htv2.setTextColor((context).getResources().getColor(R.color.Black));
        header.addView(htv0);
        header.addView(htv1);
        header.addView(htv2);
        table.addView(header);

        Iterator<Set<Rule>> iteratorOfFirstCell = (Iterator) academic.getFirstSet().iterator();
        Iterator<Set<Rule>> iteratorOfSecondCell = (Iterator) academic.getSecondSet().iterator();
        while (iteratorOfFirstCell.hasNext() && iteratorOfSecondCell.hasNext()) {
            Set<Rule> firstSet = iteratorOfFirstCell.next();
            Set<Rule> secondSet = iteratorOfSecondCell.next();

            String set1 = firstSet.toString();
            set1 = set1.replace("[", "{");
            set1 = set1.replace("]", "}");

            String set2 = secondSet.toString();
            set2 = set2.replace("[", "{");
            set2 = set2.replace("]", "}");

            TextView tv0 = new TextView(context);
            tv0.setText("(" + i + ")");
            tv0.setPadding(10, 10, 10, 10);
            tv0.setTextColor((context).getResources().getColor(R.color.Black));

            TextView tv1 = new TextView(context);
            tv1.setText(set1);
            tv1.setPadding(10, 10, 10, 10);
            tv1.setBackgroundColor((context).getResources().getColor(R.color.DarkGray));
            tv1.setTextColor((context).getResources().getColor(R.color.Black));

            TextView tv2 = new TextView(context);
            tv2.setText(set2);
            tv2.setPadding(10, 10, 10, 10);
            tv2.setTextColor((context).getResources().getColor(R.color.Black));

            TableRow row = new TableRow(context);
            row.addView(tv0);
            row.addView(tv1);
            row.addView(tv2);

            table.addView(row);
            i++;
        }
    }

    //converte um set em arraylist ordenando-o com o simbolo inicial da gramatica na primeira posicao
    public static List<String> convertSetToArray(Set<String> set, Grammar
            grammar) {
        List<String> array = new ArrayList<>();
        for (String variable : set) {
            if (!variable.equals(grammar.getInitialSymbol())) {
                array.add(variable);
            }
        }
        array.add(0, grammar.getInitialSymbol());
        return array;
    }

    public static String selectOthersVariables(Grammar g, Set<String> set) {
        List<String> array = new ArrayList<>();
        for (String variable : g.getVariables()) {
            if (!set.contains(variable))
                array.add(variable);
        }
        String aux = array.toString();
        aux = aux.replace("[", "{");
        aux = aux.replace("]", "}");
        return aux;
    }

    /**
     * Método que imprime a gramática com as regras irregulares em destaque
     * @param grammar
     * @param table
     * @param academic
     */
    public static void printOldGrammarOfTermAndReach(final Grammar grammar,
                                                TableLayout table, final
                                                AcademicSupport academic,
                                                Context context) {
        TableRow row0 = new TableRow(context);
        TextView left = new TextView(context);
        TextView arrow0 = new TextView(context);
        int contViews = 0;
        boolean initialSymbolIrregular = false;
        for (Rule element : grammar.getRules(grammar.getInitialSymbol())) {
            if (academic.getIrregularRules().contains(element)) {
                initialSymbolIrregular = true;
            }
        }
        arrow0.setText(" → ");
        left.setText(grammar.getInitialSymbol());
        if (initialSymbolIrregular) {
            left.setTextColor((context).getResources().getColor(R.color.Red));
        }
        row0.addView(left);
        row0.addView(arrow0);
        contViews = 2;
        for (Rule element : grammar.getRules(grammar.getInitialSymbol())) {
            TextView right = new TextView(context);
            TextView pipe = new TextView(context);
            pipe.setText(" | ");
            if (academic.getIrregularRules().contains(element)) {
                right.setTextColor((context).getResources().getColor(R.color.Red));
                right.setText(element.getRightSide());
            } else {
                right.setText(element.getRightSide());
            }
            row0.addView(right);
            row0.addView(pipe);
            contViews += 2;
        }
        row0.removeViewAt(contViews - 1);
        table.addView(row0);

        for (String variable : grammar.getVariables()) {
            contViews = 0;
            if (!variable.equals(grammar.getInitialSymbol())) {
                TableRow row1 = new TableRow(context);
                TextView tv0 = new TextView(context);
                tv0.setText(variable);
                TextView arrow1 = new TextView(context);
                arrow1.setText("→");
                boolean irregularVariable = false;
                for (Rule element : grammar.getRules()) {
                    if (element.getLeftSide().equals(variable)) {
                        if (academic.getIrregularRules().contains(element) &&
                                !academic.getFirstSet().get(academic.getFirstSet().size() - 1)
                                        .contains(variable)) {
                            irregularVariable = true;
                        }
                    }
                }

                if (irregularVariable) {
                    tv0.setTextColor((context).getResources().getColor(R.color.Red));
                }
                row1.addView(tv0);
                row1.addView(arrow1);
                contViews += 2;

                for (Rule element : grammar.getRules()) {
                    if (variable.equals(element.getLeftSide())) {
                        TextView pipe = new TextView(context);
                        pipe.setText(" | ");
                        TextView tv1 = new TextView(context);
                        if (academic.getIrregularRules().contains(element)) {
                            tv1.setTextColor((context).getResources().getColor(R.color.Red));
                            tv1.setText(element.getRightSide());
                        } else {
                            tv1.setText(element.getRightSide());
                        }
                        row1.addView(tv1);
                        row1.addView(pipe);
                        contViews += 2;
                    }
                }
                row1.removeViewAt(contViews - 1);
                table.addView(row1);
            }
        }
    }

}
