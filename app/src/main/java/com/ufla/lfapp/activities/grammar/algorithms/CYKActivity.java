package com.ufla.lfapp.activities.grammar.algorithms;

import android.os.Bundle;
import android.text.Html;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.grammar.HeaderGrammarActivity;
import com.ufla.lfapp.utils.UtilActivities;
import com.ufla.lfapp.core.grammar.AcademicSupport;
import com.ufla.lfapp.core.grammar.Grammar;
import com.ufla.lfapp.core.grammar.GrammarParser;
import com.ufla.lfapp.core.grammar.Rule;

import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by root on 25/07/16.
 */
public class CYKActivity extends HeaderGrammarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cyk);
        super.onCreate(savedInstanceState);
        if (word!= null && !word.equals("")) {
            cyk(new Grammar(grammar), word);
        }
    }

    public void cyk(final Grammar g, final String word) {
        Grammar gc = (Grammar) g.clone();
        AcademicSupport academic = new AcademicSupport();
        StringBuilder comments = new StringBuilder();

        TableLayout cykTableResult =  (TableLayout) findViewById(R.id.CYKTabelaResultado);
        cykTableResult.setShrinkAllColumns(true);
        if (!gc.isFNC()) {
            comments.append(getString(R.string.cyk_comments));

            //Parser da gramática inserida em FNC
            gc = gc.getGrammarWithInitialSymbolNotRecursive(gc, academic);
            gc = gc.getGrammarEssentiallyNoncontracting(gc, academic);
            gc = gc.getGrammarWithoutChainRules(gc, academic);
            gc = gc.getGrammarWithoutNoTerm(gc, academic);
            gc = gc.getGrammarWithoutNoReach(gc, academic);
            gc = gc.FNC(gc, academic);
        }

        //Realiza o processo CYK
        Set<String>[][] cykOfGrammar = gc.CYK(gc, word);
        String[][] cykOut = GrammarParser.turnsTreesetOnArray(cykOfGrammar, word);

        //Coloca na tela o resultado do CYK
        printCYKTable(cykTableResult, word.length(), cykOut, word.length(), word.length(), 20);

        //Inicia a explicação do funcionamento do algoritmo de CYK
        TableLayout explanationTable = (TableLayout) findViewById(R.id.CYKTabelaExplicacao);

        //explicando linha 1 CYK
        TableLayout auxTable = new TableLayout(this);
        TableRow row = new TableRow(this);
        TextView explanationTextView = new TextView(this);
        TableRow explanationTableRow = new TableRow(this);

        explanationTextView.setText(R.string.cyk_step_1);
        explanationTableRow.addView(explanationTextView);
        explanationTable.addView(explanationTableRow);

        //configura a primeira busca na tabela
        String[] firstCell;
        StringBuilder aux;
        if (word.length() > 1) {
            firstCell = cykOut[word.length()][0].split(" ");
            explanationTextView = new TextView(this);
            explanationTableRow = new TableRow(this);
            aux = new StringBuilder();
            explanationTextView.append(
                    getResources().getString(R.string.cyk_there_any_rule)+
                    firstCell[0] + " " +
                            getResources().getString(R.string.directly)
                            + "? ");
            explanationTextView.append((checkRules(gc, firstCell[0],
                    aux))
                    ? getResources().getString(R.string.yes)
                    : getResources().getString(R.string.no)
            );
            explanationTextView.append(".\n" + aux.toString());
            explanationTableRow.addView(explanationTextView);
            explanationTable.addView(explanationTableRow);
        }

        for (int i = word.length() - 1; i <= word.length(); i++) {
            TableRow newRow = new TableRow(this);
            for (int j = 0; j < word.length(); j++) {
                if (!cykOut[i][j].isEmpty()) {
                    TextView tv = new TextView(this);
                    setColorOfTheCell(tv, i, j);
                    tv.setPadding(10, 10, 10, 10);
                    if ((i == word.length() && j == 0) || (i == word.length() - 1 && j == 0 )) {
                        tv.setBackgroundColor(getResources().getColor(R.color.SkyBlue));
                    }
                    UtilActivities.setSubscriptItem(tv, cykOut[i][j]);
                    //tv.setText(cykTable[i][j]);
                    newRow.addView(tv);
                } else {
                    TextView tv = new TextView(this);
                    tv.setPadding(10, 10, 10, 10);
                    newRow.addView(tv);
                }
            }
            auxTable.addView(newRow);
        }
        row.addView(auxTable);
        explanationTable.addView(row);

        //passo 2 explicação
        auxTable = new TableLayout(this);
        row = new TableRow(this);
        explanationTextView = new TextView(this);
        explanationTableRow = new TableRow(this);
        explanationTextView.setText(R.string.cyk_step_2);
        explanationTableRow.addView(explanationTextView);
        explanationTable.addView(explanationTableRow);

        //configura segunda busca na tela
        String[] secondCell;
        if (word.length() > 2) {
            explanationTextView = new TextView(this);
            explanationTableRow = new TableRow(this);
            firstCell = cykOut[word.length() - 1][0].split(" ");
            secondCell = cykOut[word.length() - 1][1].split(" ");
            aux = new StringBuilder();
            explanationTextView.append(Html.fromHtml(
                    getResources().getString(R.string.cyk_there_any_rule) +
                    permutationOfVariables(firstCell, secondCell) + "? "));
            explanationTextView.append(Html.fromHtml((checkRules(g, firstCell, secondCell,
                    aux))
                    ? getResources().getString(R.string.yes)
                    : getResources().getString(R.string.no)
            ));
            explanationTextView.append(Html.fromHtml(".<br>" + aux.toString()));
            explanationTableRow.addView(explanationTextView);
            explanationTable.addView(explanationTableRow);
        }

        for (int i = word.length() - 2; i <= word.length(); i++) {
            TableRow newRow = new TableRow(this);
            for (int j = 0; j < word.length(); j++) {
                if (!cykOut[i][j].isEmpty()) {
                    TextView tv = new TextView(this);
                    setColorOfTheCell(tv, i, j);
                    tv.setPadding(10, 10, 10, 10);
                    if (((i == word.length() - 1 && (j == 0 || j == 1))) ||
                            (i == word.length() - 2 && j == 0)) {
                        tv.setBackgroundColor(getResources().getColor(R.color.SkyBlue));
                    }
                    UtilActivities.setSubscriptItem(tv, cykOut[i][j]);
                    //tv.setText(cykTable[i][j]);
                    newRow.addView(tv);
                } else {
                    TextView tv = new TextView(this);
                    tv.setPadding(10, 10, 10, 10);
                    newRow.addView(tv);
                }
            }
            auxTable.addView(newRow);
        }
        row.addView(auxTable);
        explanationTable.addView(row);


        //passo 3 explicação
        auxTable = new TableLayout(this);
        row = new TableRow(this);
        explanationTextView = new TextView(this);
        explanationTableRow = new TableRow(this);
        explanationTextView.setText(R.string.cyk_step_3);
        explanationTableRow.addView(explanationTextView);
        explanationTable.addView(explanationTableRow);


        //configura terceira busca na tela
        if (word.length() > 3) {
            explanationTextView = new TextView(this);
            explanationTableRow = new TableRow(this);
            firstCell = cykOut[word.length() - 1][0].split(" ");
            secondCell = cykOut[word.length() - 2][1].split(" ");
            String[] thirdCell = cykOut[word.length() - 2][0].split(" ");
            String[] fourthCell = cykOut[word.length() - 1][2].split(" ");
            aux = new StringBuilder();
            explanationTextView.append(Html.fromHtml(
                    getResources().getString(R.string.cyk_there_any_rule) +
                    permutationOfVariables(firstCell, secondCell) + ", " +
                    permutationOfVariables(thirdCell, fourthCell) + "? "));
            explanationTextView.append(Html.fromHtml((checkRules(g, firstCell, secondCell, aux) |
                    (checkRules(g, thirdCell, fourthCell, aux)))
                    ? getResources().getString(R.string.yes)
                    : getResources().getString(R.string.no)
            ));
            explanationTextView.append(Html.fromHtml(".<br>" + aux.toString()));
            explanationTableRow.addView(explanationTextView);
            explanationTable.addView(explanationTableRow);
        }

        for (int i = word.length() - 3; i <= word.length(); i++) {
            TableRow newRow = new TableRow(this);
            for (int j = 0; j < word.length(); j++) {
                if (!cykOut[i][j].isEmpty()) {
                    TextView tv = new TextView(this);
                    setColorOfTheCell(tv, i, j);
                    tv.setPadding(10, 10, 10, 10);
                    if ((i == word.length() - 1 && (j == 0 || j == 1 || j == 2)) ||
                            ((i == word.length() - 2 && (j == 0 || j == 1))) ||
                            (i == word.length() - 3 && j == 0)) {
                        tv.setBackgroundColor(getResources().getColor(R.color.SkyBlue));
                    }
                    UtilActivities.setSubscriptItem(tv, cykOut[i][j]);
                    //tv.setText(cykTable[i][j]);
                    newRow.addView(tv);
                } else {
                    TextView tv = new TextView(this);
                    tv.setPadding(10, 10, 10, 10);
                    newRow.addView(tv);
                }
            }
            auxTable.addView(newRow);
        }
        row.addView(auxTable);
        explanationTable.addView(row);


        explanationTextView = new TextView(this);
        explanationTableRow = new TableRow(this);

        if (word.length() >= 4) {
            explanationTextView.setText(R.string.cyk_and_so_on);
            explanationTableRow.addView(explanationTextView);
            explanationTable.addView(explanationTableRow);
        }
    }

    private void printCYKTable(TableLayout table, final int edge, final String[][] cykTable,
                               final int lines, final int columns, final int padding) {
        for (int i = lines - edge; i <= lines; i++) {
            TableRow row = new TableRow(this);
            for (int j = 0; j < columns; j++) {
                if (!cykTable[i][j].isEmpty()) {
                    TextView tv = new TextView(this);
                    setColorOfTheCell(tv, i, j);
                    tv.setPadding(padding, padding, padding, padding);
                    UtilActivities.setSubscriptItem(tv, cykTable[i][j]);
                    //tv.setText(cykTable[i][j]);
                    row.addView(tv);
                } else {
                    TextView tv = new TextView(this);
                    tv.setPadding(padding, padding, padding, padding);
                    row.addView(tv);
                }
            }
            table.addView(row);
        }
    }

    private boolean checkRules(final Grammar g, final String sentence,
                               final StringBuilder newSentence) {
        boolean flag = false;
        Iterator<Rule> it = g.getRules().iterator();
        while (it.hasNext()) {
            Rule r = it.next();
            if (r.getRightSide().equals(sentence)) {
                flag = true;
                newSentence.append( r.toString() + ", ");
            }
        }
        if (newSentence.length() != 0) {
            newSentence.delete(newSentence.length() - 2, newSentence.length());
            newSentence.append(".");
        }
        return flag;
    }

    private boolean checkRules(final Grammar g, final String[] firstCell,
                               final String[] secondCell, final StringBuilder sentence) {
        boolean flag = false;
        for (int i = 0; i < firstCell.length; i++) {
            firstCell[i] = firstCell[i].trim();
            for (int j = 0; j < secondCell.length; j++) {
                secondCell[j] = secondCell[j].trim();
                String newSentence = new String(firstCell[i] + secondCell[j]);
                for (Rule element : g.getRules()) {
                    if (element.getRightSide().equals(sentence)) {
                        sentence.append(element + ", ");
                        flag = true;
                    }
                }
            }
        }
        if (sentence.length() != 0) {
            sentence.delete(sentence.length() - 2, sentence.length());
            sentence.append(".");
        }
        return flag;
    }

    /**
     * Método que seta a cor de uma célula em determinada tabela entre darkGray ou Gainsboro
     * @param cell
     * @param i
     * @param j
     */
    private void setColorOfTheCell(TextView cell, int i, int j) {
        if (i % 2 == 0) {
            if (j % 2 == 0) {
                cell.setBackgroundColor(getResources().getColor(R.color.DarkGray));
            } else {
                cell.setBackgroundColor(getResources().getColor(R.color.Gainsboro));
            }
        } else {
            if (j % 2 == 0) {
                cell.setBackgroundColor(getResources().getColor(R.color.Gainsboro));
            } else {
                cell.setBackgroundColor(getResources().getColor(R.color.DarkGray));
            }
        }
    }

    private String permutationOfVariables(final String[] firstCell, final String[] secondCell) {
        StringBuilder newSentence = new StringBuilder();
        String cell1 = new String();
        for (int i = 0; i < firstCell.length; i++) {
            cell1 += firstCell[i];
        }
        cell1 = cell1.replace("{", "");
        cell1 = cell1.replace("}", "");

        StringTokenizer token1 = new StringTokenizer(cell1, ",");
        int cont = 1;
        while (token1.hasMoreTokens()) {
            String primeiroToken = token1.nextToken().toString();
            primeiroToken.trim();

            String cell2 = new String();
            for (int i = 0; i < secondCell.length; i++) {
                cell2 += secondCell[i];
            }
            cell2 = cell2.replace("{", "");
            cell2 = cell2.replace("}", "");


            StringTokenizer token2 = new StringTokenizer(cell2, ",");
            while (token2.hasMoreTokens()) {
                String segundoToken = token2.nextToken().toString();
                segundoToken.trim();

                if (!firstCell.equals("-") && !secondCell.equals("-")) {
                    if (primeiroToken.length() > 1 && Character.isDigit(primeiroToken.charAt(1))) {
                        StringBuilder aux = new StringBuilder();
                        aux.append(primeiroToken.charAt(0));
                        aux.append("<sub>" + primeiroToken.substring(1) + "</sub>");
                        primeiroToken = aux.toString();
                    }
                    if (segundoToken.length() > 1 && Character.isDigit(segundoToken.charAt(1))) {
                        StringBuilder aux = new StringBuilder();
                        aux.append(segundoToken.charAt(0));
                        aux.append("<sub>" + segundoToken.substring(1) + "</sub>");
                        segundoToken = aux.toString();
                    }
                    newSentence.append(primeiroToken + segundoToken + ", ");
                    if (cont == 6 || cont == 12 || cont == 18) {
                        newSentence.append("<br>");
                    }
                    cont++;
                }

            }
        }

        newSentence.delete(newSentence.length() - 2, newSentence.length());
        return newSentence.toString();
    }

}
