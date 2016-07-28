package com.ufla.lfapp.activities;

import android.os.Bundle;
import android.text.Html;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.utils.UtilActivities;
import com.ufla.lfapp.vo.AcademicSupport;
import com.ufla.lfapp.vo.Grammar;
import com.ufla.lfapp.vo.Rule;

import java.util.ArrayList;
import java.util.Collections;

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
        comments.append("\t\tRecursividade direta à esquerda pode produzir “loops infinitos” " +
                "em analisadores sintáticos descendentes (top-down).");

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
            step1.setText("(1) O primeiro passo é identificar a recursão.");
            TextView pseudo = (TextView) findViewById(R.id.PseudoDirectLeftRecursionAlgorithm);
            pseudo.setText(Html.fromHtml(getLeftRecursionAlgorithm()));
            TableLayout tableOfRecursion = (TableLayout)
                    findViewById(R.id.tableWithDirectLeftRecursion);
            //printGrammarWithoutOldRules(g, tableOfRecursion, academicSupport);
            printGrammarWithRecursiveRules(g, tableOfRecursion, academicSupport);

            //Segundo passo do processo
            String algoritmo = "para i = 1 até n faça\n    se " +
                    "A<sub><small>i</sub></small> possui recursão direta à " +
                    "esquerda então\n        elimine a recursão em " +
                    "A<sub><small>i</sub></small>\n    para j = i+1 até n " +
                    "faça\n        para cada produção em " +
                    "A<sub><small>j</sub></small> faça\n            se " +
                    "A<sub><small>j</sub></small> -> " +
                    "<sub><small>i</sub></small>Y então\n                " +
                    "substitua A<sub><small>j</sub></small> -> " +
                    "<sub><small>i</sub></small>Y por " +
                    "A<sub><small>j</sub></small> -> "  +
                    "                    T<sub><small>1</sub></small>Y | " +
                    "T<sub><small>2</sub></small>Y | ... | " +
                    "T<sub><small>k</sub></small>Y, onde " +
                    "A<sub><small>i</sub></small> -> T<sub><small>1</sub> | " +
                    "T<sub><small>2</sub> | ... | T<sub><small>k</sub> são " +
                    "todas as produções atuais de A<sub><small>i</sub></small>";
            TextView step2 = (TextView) findViewById(R.id.Step2DirectLeftRecursion);
            step2.setText("(2) O segundo passo é resolver a recursão.");
            TableLayout tableOfResult = (TableLayout) findViewById(R.id.tableWithoutDirectLeftRecursion);
            UtilActivities.printGrammarWithNewRules(gc, tableOfResult,
                    academicSupport, this);
        } else {
            TextView commentsOfProcess = (TextView) findViewById(R.id.CommentsDirectLeftRecursion);
            commentsOfProcess.setText("A gramática inserida não possui recursão direta à esquerda.");
        }

    }

    /**
     * Método que retorna o pseudocódigo do algortmo para remoção de recursão direta
     * @return
     */
    public String getLeftRecursionAlgorithm() {
        StringBuilder algol = new StringBuilder();
        algol.append("Suponha a regra genérica diretamente recursiva à esq.:<br>");
        algol.append("A → Aμ<sub><small>1</small></sub> | Aμ<sub><small>2</small></sub> | ... | Aμ<sub><small>m</small></sub> | ν<sub><small>1</small></sub> | ν<sub><small>2</small></sub> | ... | ν<sub><small>n</small></sub><br><br>");
        algol.append("Regra equivalente não-recursiva à esquerda:<br>");
        algol.append("A → ν<sub><small>1</small></sub> | ν<sub><small>2</small></sub> | ... | ν<sub><small>n</small></sub> | ν<sub><small>1</small></sub>Z | ν<sub><small>2</small></sub>Z | ... | ν<sub><small>n</small></sub>Z<br>");
        algol.append("Z → μ<sub><small>1</small></sub>Z | μ<sub><small>2</small></sub>Z | ... | μ<sub><small>m</small></sub>Z | μ<sub><small>1</small></sub> | μ<sub><small>2</small></sub> | ... | μ<sub><small>m</small></sub><br><br>");
        return algol.toString();
    }

    /**
     * Método que imprime a gramática com as regras irregulares recursivas em destaque
     * @param grammar
     * @param table
     * @param academic
     */
    private void printGrammarWithRecursiveRules(final Grammar grammar, TableLayout table, final AcademicSupport academic) {
        TableRow row0 = new TableRow(this);
        TextView left = new TextView(this);
        TextView arrow0 = new TextView(this);
        left.setText(grammar.getInitialSymbol());
        arrow0.setText(" -> ");
        row0.addView(left);
        row0.addView(arrow0);
        int contViews = 2;
        ArrayList<String> variables = new ArrayList<>(grammar.getVariables());
        Collections.sort(variables);
        for (Rule element : grammar.getRules()) {
            if (element.getLeftSide().equals(grammar.getInitialSymbol())) {
                TextView right = new TextView(this);
                TextView pipe = new TextView(this);
                pipe.setText(" | ");
                if (academic.getIrregularRules().contains(element)) {
                    String firstElement = getFirstElement(element.getRightSide());
                    right.setText(Html.fromHtml("<font color=" + getResources().getColor(R.color.Red) + ">" + firstElement + "</font>" +  element.getRightSide().substring(firstElement.length())));
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
                                tv1.setText(Html.fromHtml("<font color=" + getResources().getColor(R.color.Red) + ">" + firstElement + "</font>" + element.getRightSide().substring(firstElement.length())));
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
