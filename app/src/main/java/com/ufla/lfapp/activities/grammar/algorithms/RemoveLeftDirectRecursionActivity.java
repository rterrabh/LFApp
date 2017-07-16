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
import com.ufla.lfapp.core.grammar.Rule;

import java.util.ArrayList;

/**
 * Created by root on 25/07/16.
 */
public class RemoveLeftDirectRecursionActivity extends HeaderGrammarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_remove_left_direct_recursion);
        super.onCreate(savedInstanceState);
        removingTheImmediateLeftRecursion(getGrammar());
    }


    public void removingTheImmediateLeftRecursion(final Grammar g) {
        Grammar gc = (Grammar) g.clone();
        AcademicSupport academicSupport = new AcademicSupport();

        //Realiza comentários sobre o processo
        StringBuilder comments = new StringBuilder();
        comments.append(getString(R.string.rem_left_direct_recursion_comments));

        //Realiza processo
        gc = gc.removingTheImmediateLeftRecursion(gc, academicSupport);
        academicSupport.setResult(gc);

        //Insere o resultado do processo
        TextView resultOfProcess = (TextView)
                findViewById(R.id.ResultGrammarWithoutLeftDirectRecursion);
        resultOfProcess.setText(Html.fromHtml(academicSupport.getResult()));

        if (academicSupport.getSituation()) {
            //Insere comentários
            academicSupport.setComments(comments.toString());
            TextView commentsOfProcess = (TextView) findViewById(R.id.CommentsDirectLeftRecursion);
            commentsOfProcess.setText(academicSupport.getComments());

            //Primeiro passo do processo
            TextView step1 = (TextView) findViewById(R.id.Step1DirectLeftRecursion);
            step1.setText(R.string.rem_left_direct_recursion_step_1);
            TextView pseudo = (TextView) findViewById(R.id.PseudoDirectLeftRecursionAlgorithm);
            pseudo.setText(Html.fromHtml(getLeftRecursionAlgorithm()));
            TableLayout tableOfRecursion = (TableLayout)
                    findViewById(R.id.tableWithDirectLeftRecursion);
            //printGrammarWithoutOldRules(g, tableOfRecursion, academicSupport);
            printGrammarWithRecursiveRules(g, tableOfRecursion, academicSupport);

            //Segundo passo do processo
            TextView step2 = (TextView) findViewById(R.id.Step2DirectLeftRecursion);
            step2.setText(R.string.rem_left_direct_recursion_step_2);
            TableLayout tableOfResult = (TableLayout) findViewById(R.id.tableWithoutDirectLeftRecursion);
            UtilActivities.printGrammarWithNewRules(gc, tableOfResult,
                    academicSupport, this);
        } else {
            TextView commentsOfProcess = (TextView) findViewById(R.id.CommentsDirectLeftRecursion);
            commentsOfProcess.setText(R.string.rem_left_direct_recursion_comments_2);
        }

    }

    /**
     * Método que retorna o pseudocódigo do algortmo para remoção de recursão direta
     * @return
     */
    public String getLeftRecursionAlgorithm() {
        return getString(R.string.rem_left_direct_recursion_algol);
    }

    /**
     * Método que imprime a gramática com as regras irregulares recursivas em destaque
     * @param grammar
     * @param table
     * @param academic
     */
    private void printGrammarWithRecursiveRules(final Grammar grammar, TableLayout table,
                                                final AcademicSupport academic) {
        TableRow row0 = new TableRow(this);
        TextView left = new TextView(this);
        TextView arrow0 = new TextView(this);
        left.setText(grammar.getInitialSymbol());
        arrow0.setText(" -> ");
        row0.addView(left);
        row0.addView(arrow0);
        int contViews = 2;
        ArrayList<String> variables = new ArrayList<>(grammar.getVariables());
        for (Rule element : grammar.getRules()) {
            if (element.getLeftSide().equals(grammar.getInitialSymbol())) {
                TextView right = new TextView(this);
                TextView pipe = new TextView(this);
                pipe.setText(" | ");
                if (academic.getIrregularRules().contains(element)) {
                    String firstElement = getFirstElement(element.getRightSide());
                    right.setText(Html.fromHtml("<font color=" + getResources()
                            .getColor(R.color.Red) + ">" + firstElement + "</font>" +
                            element.getRightSide().substring(firstElement.length())));
                } else {
                    right.setText(element.getRightSide());
                }
                row0.addView(right);
                row0.addView(pipe);
                contViews += 2;
            }
        }
        row0.removeViewAt(contViews - 1);
        table.addView(row0);
        String teste = grammar.getVariables().toString();
        for (int i = 0; i < variables.size(); i++) {
            for (String variable : grammar.getVariables()) {
                if (!variable.equals(grammar.getInitialSymbol()) && variables.get(i).equals(variable)) {
                    TableRow row1 = new TableRow(this);
                    TextView tv0 = new TextView(this);
                    tv0.setText(variable);
                    row1.addView(tv0);
                    TextView arrow1 = new TextView(this);
                    arrow1.setText("->");
                    row1.addView(arrow1);
                    contViews = 2;
                    for (Rule element : grammar.getRules()) {
                        if (variable.equals(element.getLeftSide())) {
                            TextView pipe = new TextView(this);
                            pipe.setText(" | ");
                            TextView tv1 = new TextView(this);
                            if (academic.getIrregularRules().contains(element)) {
                                String firstElement = getFirstElement(element.getRightSide());
                                tv1.setText(Html.fromHtml("<font color=" + getResources()
                                        .getColor(R.color.Red) + ">" + firstElement + "</font>"
                                        + element.getRightSide().substring(firstElement.length())));
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

    /**
     * Método que retorna o primeiro elemento de uma sentença
     * @param element
     * @return
     */
    private String getFirstElement(final String element) {
        StringBuilder firstElement = new StringBuilder();
        firstElement.append(element.charAt(0));
        for (int i = 1; i < element.length() && Character.isDigit(element.charAt(i)); i++) {
            firstElement.append(element.charAt(i));
        }
        return firstElement.toString();
    }

}
