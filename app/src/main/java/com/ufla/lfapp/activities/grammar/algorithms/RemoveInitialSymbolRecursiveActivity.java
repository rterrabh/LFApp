package com.ufla.lfapp.activities.grammar.algorithms;

import android.os.Bundle;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.grammar.HeaderGrammarActivity;
import com.ufla.lfapp.core.grammar.AcademicSupport;
import com.ufla.lfapp.core.grammar.Grammar;
import com.ufla.lfapp.core.grammar.Rule;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by root on 21/07/16.
 */
public class RemoveInitialSymbolRecursiveActivity extends HeaderGrammarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_remove_initial_symbol_recursive);
        super.onCreate(savedInstanceState);
        setTitle();
        removingInitialRecursiveSymbol(getGrammar());
    }

    private void setTitle() {
        switch(algorithm) {
            case CHOMSKY_NORMAL_FORM:
                setTitle(getResources().getString(R.string.lfapp_cnf_title)
                        + " - 1/6");
                break;
            case GREIBACH_NORMAL_FORM:
                setTitle(getResources().getString(R.string.lfapp_gnf_title)
                        + " - 1/7");
                break;
            case REMOVE_LEFT_RECURSION:
                setTitle(getResources().getString(R.string.lfapp_left_recursion_title)
                        + " - 1/7");
                break;
        }
    }

    public void back(View view) {
        onBackPressed();
    }

    public void next(View view) {
        changeActivity(this, EmptyProductionActivity.class);
    }

    /**
     * Método que realiza a etapa de remoção do símbolo recursivo inicial e acrescenta as informações
     * acadêmicas.
     * @param g : gramática
     */
    public void removingInitialRecursiveSymbol(final Grammar g) {
        AcademicSupport academicSupport = new AcademicSupport();
        Grammar gc = g.getGrammarWithInitialSymbolNotRecursive(g,
                academicSupport);

        TableLayout table = (TableLayout) findViewById(R.id.TableRecursiveInitialSymbol);
        assert table != null;
        table.setShrinkAllColumns(true);
        setContentInTable(table, academicSupport, gc, g);


        TextView step1_2 = (TextView) findViewById(R.id.Algoritmo1);
        if (academicSupport.getSituation()) {
            StringBuilder academicInfo = new StringBuilder(academicSupport.getComments());
            for (int i = 1; i < academicSupport.getFoundProblems().size(); i++) {
                academicInfo.append(academicSupport.getFoundProblems().get(i));
            }
            academicInfo.append(academicSupport.getSolutionDescription());
            assert step1_2 != null;
            step1_2.setText(Html.fromHtml(academicInfo.toString()));
        } else {
            assert step1_2 != null;
            step1_2.setText(Html.fromHtml(getString(R.string.rem_initial_rec_step_1_1)
                    + ' ' + g.getInitialSymbol() + " ⇒<sup>*</sup> αSβ. "
                    + getString(R.string.rem_initial_rec_step_1_2)));
        }
    }

    private TextView getPipeTextView() {
        return getTextView(" | ");
    }

    private TextView getArrowTextView() {
        return getTextView(" → ");
    }

    private TextView getTextView(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        return tv;
    }

    private void addVarOnTable(final TableLayout table, final AcademicSupport academic,
                               final Grammar g, String var) {
        TableRow row = new TableRow(this);
        row.addView(getTextView(var));
        row.addView(getArrowTextView());
        int contViews = 2;
        for (Rule element : g.getRules(var)) {
            TextView right = getTextView(element.getRightSide());
            if (academic.getInsertedRules().contains(element)) {
                right.setTextColor(getResources().getColor(R.color.Blue));
            } else if (academic.getIrregularRules().contains(element)) {
                right.setTextColor(getResources().getColor(R.color.Red));
            }
            row.addView(right);
            row.addView(getPipeTextView());
            contViews += 2;
        }
        row.removeViewAt(contViews - 1);
        table.addView(row);
    }

    /**
     * Método que coloca a gramática passada como argumento no TableLayout destacando as alterações
     * realizadas.
     * @param table : TableLayout
     * @param academic : Objeto que armazena as informações acadêmicas
     * @param g : gramática passada
     */
    private void setContentInTable(TableLayout table, AcademicSupport academic, final Grammar g, final Grammar oldGrammar) {
        /*TableRow row = new TableRow(this);
        TextView left = new TextView(this);
        TextView arrow = new TextView(this);*/
        ArrayList<String> variablesOrdenated = new ArrayList<>(g.getVariables());
        addVarOnTable(table, academic, g, g.getInitialSymbol());
        variablesOrdenated.remove(g.getInitialSymbol());
        for (String var : variablesOrdenated) {
            addVarOnTable(table, academic, g, var);
        }
        /*
        if (!g.getInitialSymbol().equals(oldGrammar.getInitialSymbol())) {
            variablesOrdenated.remove(g.getInitialSymbol());
            left.setText(g.getInitialSymbol());
            arrow.setText(" → ");
            row.addView(left);
            row.addView(arrow);
            int contViews = 2;
            for (Rule element : g.getRules()) {
                if (element.getLeftSide().equals(g.getInitialSymbol())) {
                    TextView right = new TextView(this);
                    TextView pipe = new TextView(this);
                    pipe.setText(" | ");
                    if (academic.getInsertedRules().contains(element)) {
                        right.setTextColor(getResources().getColor(R.color.Blue));
                        right.setText(element.getRightSide());
                    } else if (academic.getIrregularRules().contains(element)) {
                        right.setTextColor(getResources().getColor(R.color.Red));
                        right.setText(element.getRightSide());
                    } else {
                        right.setText(element.getRightSide());
                    }
                    row.addView(right);
                    row.addView(pipe);
                    contViews += 2;
                }
            }
            row.removeViewAt(contViews - 1);
            table.addView(row);
        }


        left = new TextView(this);
        left.setText(oldGrammar.getInitialSymbol());
        row = new TableRow(this);
        row.addView(left);
        row.addView(arrow);
        int contViews = 2;
        for (Rule element : g.getRules()) {
            if (element.getLeftSide().equals(oldGrammar.getInitialSymbol())) {
                TextView right = new TextView(this);
                TextView pipe = new TextView(this);
                pipe.setText(" | ");
                if (academic.getInsertedRules().contains(element)) {
                    right.setTextColor(getResources().getColor(R.color.Blue));
                    right.setText(element.getRightSide());
                } else if (academic.getIrregularRules().contains(element)) {
                    right.setTextColor(getResources().getColor(R.color.Red));
                    right.setText(element.getRightSide());
                } else {
                    right.setText(element.getRightSide());
                }
                row.addView(right);
                row.addView(pipe);
                contViews += 2;
            }
        }
        row.removeViewAt(contViews - 1);
        table.addView(row);


        for (int i = 0; i < variablesOrdenated.size(); i++) {
            for (String variable : g.getVariables()) {
                if (!variable.equals(g.getInitialSymbol()) && variablesOrdenated.get(i).equals(variable)) {
                    TableRow row1 = new TableRow(this);
                    TextView tv0 = new TextView(this);
                    tv0.setText(variable);
                    row1.addView(tv0);
                    TextView arrow1 = new TextView(this);
                    arrow1.setText("->");
                    row1.addView(arrow1);
                    contViews = 2;
                    for (Rule element : g.getRules()) {
                        if (variable.equals(element.getLeftSide())) {
                            TextView pipe = new TextView(this);
                            pipe.setText(" | ");
                            TextView tv1 = new TextView(this);
                            if (academic.getInsertedRules().contains(element)) {
                                tv1.setTextColor(getResources().getColor(R.color.Blue));
                                tv1.setText(element.getRightSide());
                            } else if (academic.getIrregularRules().contains(element)) {
                                tv1.setTextColor(getResources().getColor(R.color.Red));
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
        }*/
    }


}
