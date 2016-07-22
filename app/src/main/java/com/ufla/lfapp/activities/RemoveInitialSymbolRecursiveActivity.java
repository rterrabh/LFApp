package com.ufla.lfapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.vo.AcademicSupport;
import com.ufla.lfapp.vo.Grammar;
import com.ufla.lfapp.vo.Rule;

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
        removingInitialRecursiveSymbol(new Grammar(grammar));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.next:
                Bundle params = new Bundle();
                params.putString("grammar", grammar);
                params.putString("word", word);
                params.putInt("algorithm", algorithm.getValue());
                Intent intent = new Intent(this, EmptyProductionActivity.class);
                intent.putExtras(params);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            step1_2.setText(Html.fromHtml("A gramática inserida não possui regras do tipo "
                    + g.getInitialSymbol() + " ⇒<sup>*</sup>αSβ. Logo, nenhuma alteração foi realizada."));
        }
    }

    /**
     * Método que coloca a gramática passada como argumento no TableLayout destacando as alterações
     * realizadas.
     * @param table : TableLayout
     * @param academic : Objeto que armazena as informações acadêmicas
     * @param g : gramática passada
     */
    private void setContentInTable(TableLayout table, AcademicSupport academic, final Grammar g, final Grammar oldGrammar) {
        TableRow row0 = new TableRow(this);
        TextView left = new TextView(this);
        TextView arrow0 = new TextView(this);
        ArrayList<String> variablesOrdenated = new ArrayList<>(g.getVariables());
        Collections.sort(variablesOrdenated);
        variablesOrdenated.remove(oldGrammar.getInitialSymbol());
        left.setText(g.getInitialSymbol());
        arrow0.setText(" -> ");
        row0.addView(left);
        row0.addView(arrow0);
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
                row0.addView(right);
                row0.addView(pipe);
                contViews += 2;
            }
        }
        row0.removeViewAt(contViews - 1);
        table.addView(row0);

        left = new TextView(this);
        left.setText(oldGrammar.getInitialSymbol());
        arrow0 = new TextView(this);
        arrow0.setText(" -> ");
        row0 = new TableRow(this);
        row0.addView(left);
        row0.addView(arrow0);
        contViews = 2;
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
                row0.addView(right);
                row0.addView(pipe);
                contViews += 2;
            }
        }
        row0.removeViewAt(contViews - 1);
        table.addView(row0);


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
        }
    }


}
