package com.ufla.lfapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.vo.AcademicSupport;
import com.ufla.lfapp.vo.AcademicSupportForRemoveLeftRecursion;
import com.ufla.lfapp.vo.Grammar;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 25/07/16.
 */
public class RemoveLeftRecursionActivity extends HeaderGrammarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_remove_left_recursion);
        super.onCreate(savedInstanceState);
        setTitle();
        removingLeftRecursion(getGrammar());
    }

    private void setTitle() {
        switch(algorithm) {
            case GREIBACH_NORMAL_FORM: setTitle("LFApp - FNG - 6/7"); break;
        }
    }

    @Override
    protected Grammar getGrammar() {
        switch(algorithm) {
            case GREIBACH_NORMAL_FORM:
                Grammar g = new Grammar(grammar);
                g = g.getGrammarWithInitialSymbolNotRecursive(g, new
                        AcademicSupport());
                g = g.getGrammarEssentiallyNoncontracting(g, new
                        AcademicSupport());
                g = g.getGrammarWithoutChainRules(g, new AcademicSupport());
                g = g.getGrammarWithoutNoTerm(g, new AcademicSupport());
                return g.getGrammarWithoutNoReach(g, new AcademicSupport());
            default: return super.getGrammar();
        }
    }

    public void back(View view) {
        changeActivity(this, NoReachSymbolsActivity.class);
    }

    public void next(View view) {
        onBackPressed();
    }

    public void removingLeftRecursion(final Grammar g) {
        Grammar gc = (Grammar) g.clone();

        //Realiza processo
        AcademicSupport academicSupport = new AcademicSupport();
        AcademicSupportForRemoveLeftRecursion academicSupportLR = new
                AcademicSupportForRemoveLeftRecursion();
        Map<String, String> sortedVariables = new HashMap<>();
        gc = gc.removingLeftRecursion(gc, academicSupport, sortedVariables,
                academicSupportLR);
        academicSupport.setResult(gc);
        TextView resultGrammar = (TextView) findViewById(R.id.RemovalLeftRecursion);
        resultGrammar.setText(Html.fromHtml(academicSupport.getResult()));

        if (academicSupport.getSituation()) {
            //Realiza comentários sobre o processo
            TextView text = (TextView) findViewById(R.id.commentsOfRemovalLeftRecursion);
            text.setText("A remoção de recursão à esquerda consiste em ordenar as variáveis da " +
                    "gramática e organizar as regras da forma que a variável do lado" +
                    " esquerdo sempre possua valor menor do que a variável do lado direito.");
            academicSupport.setComments(text.toString());

            //Realiza o primeiro passo do processo (Ordenação das variáveis)
            TextView step1 = (TextView) findViewById(R.id.step1RemovalLeftRecursion);
            step1.setText("(1) Ordenar as variáveis da gramática.");
            TableLayout tableOfSortedVariables = (TableLayout) findViewById(R.id.tableOfSortedVariables);
            tableOfSortedVariables.setShrinkAllColumns(true);
            printMap(tableOfSortedVariables, gc, sortedVariables, "Variável", "Valor");

            //Realiza segundo passo do processo (Destaca recursões encontradas)
            TextView step2 = (TextView) findViewById(R.id.step2RemovalLeftRecursion);
            //step2.setText("(2) Localizar as recursões.");
            step2.setText(Html.fromHtml(getResources().getString
                    (R.string.removing_left_recursive_algol_p1)));

           /*TableLayout tableOfIrregularRules = (TableLayout) findViewById(R
                   .id.tableOfLeftRecursion);
           printGrammarWithoutOldRules(g, tableOfIrregularRules,
                   academicSupport);*/
            ArrayAdapter<Spanned> listAdapter = new ArrayAdapter<>(this,
                    R.layout.row_text_view);
            for(String transformation : academicSupportLR
                    .getGrammarTransformationsStage1()) {
                listAdapter.add(Html.fromHtml(transformation));
            }
            ListView removalLeftRecursion = (ListView) findViewById(R.id
                    .step2RemovalLeftRecursionAcademicSupport);
            removalLeftRecursion.setAdapter(listAdapter);
            setListViewHeightBasedOnChildren(removalLeftRecursion);

            TextView step3 = (TextView) findViewById(R.id
                    .step3RemovalLeftRecursion);
            step3.setText(Html.fromHtml(getResources().getString
                    (R.string.removing_left_recursive_algol_p2)));
            listAdapter = new ArrayAdapter<>(this,
                    R.layout.row_text_view);
            for(String transformation : academicSupportLR
                    .getGrammarTransformationsStage2()) {
                listAdapter.add(Html.fromHtml(transformation));
            }
            removalLeftRecursion = (ListView) findViewById(R.id
                    .step3RemovalLeftRecursionAcademicSupport);
            removalLeftRecursion.setAdapter(listAdapter);
            setListViewHeightBasedOnChildren(removalLeftRecursion);

            TextView step4 = (TextView) findViewById(R.id
                    .step4RemovalLeftRecursion);
            step4.setText(Html.fromHtml(getResources().getString
                    (R.string.removing_left_recursive_algol_p3)));
            listAdapter = new ArrayAdapter<>(this,
                    R.layout.row_text_view);
            for(String transformation : academicSupportLR
                    .getGrammarTransformationsStage3()) {
                listAdapter.add(Html.fromHtml(transformation));
            }
            removalLeftRecursion = (ListView) findViewById(R.id
                    .step4RemovalLeftRecursionAcademicSupport);
            removalLeftRecursion.setAdapter(listAdapter);
            setListViewHeightBasedOnChildren(removalLeftRecursion);
            //Realiza terceiro passo do processo (Destaca as mudanças finais)
           /*TextView step3 = (TextView) findViewById(R.id
                   .step3RemovalLeftRecursion);
           step3.setText("(3) Alterar as regras");
           TableLayout tableOfNewRules = (TableLayout) findViewById(R.id.tableWithoutLeftRecursion);
           printGrammarWithNewRules(gc, tableOfNewRules, academicSupport);*/


        } else {
            TextView text = (TextView) findViewById(R.id.commentsOfRemovalLeftRecursion);
            if (academicSupport.getSolutionDescription().isEmpty()) {
                text.setText("A gramática inserida não possui recursão à esquerda.");
            } else {
                text.setText(academicSupport.getSolutionDescription());
            }

        }
    }

    /**
     * Método que imprime a tabela de variáveis ordenadas
     * @param table
     * @param g
     * @param map
     * @param firstValue
     * @param secondValue
     */
    private void printMap(TableLayout table, final Grammar g, final Map<String, String> map,
                          String firstValue, String secondValue) {
        //Configura cabeçalho
        TableRow header = new TableRow(this);
        header.setBackgroundColor(getResources().getColor(R.color.DarkGray));
        TextView htv0 = new TextView(this);
        htv0.setPadding(10, 10, 10, 10);
        htv0.setText(firstValue);
        TextView htv1 = new TextView(this);
        htv1.setPadding(10, 10, 10, 10);
        htv1.setText(secondValue);
        header.addView(htv0);
        header.addView(htv1);
        table.addView(header);

        int i = 1;
        while (i - 1 != map.size()) {
            for (String variable : g.getVariables()) {
                if ((map.get(variable) != null) && (Integer.parseInt(map.get(variable)) == i)) {
                    TableRow row = new TableRow(this);
                    TextView tv0 = new TextView(this);
                    tv0.setPadding(10, 10, 10, 10);
                    tv0.setText(variable);
                    TextView tv1 = new TextView(this);
                    tv1.setPadding(10, 10, 10, 10);
                    tv1.setText(map.get(variable));
                    if (i % 2 == 0) {
                        row.setBackgroundColor(getResources().getColor(R.color.DarkGray));
                    } else {
                        row.setBackgroundColor(getResources().getColor(R.color.Gainsboro));
                    }
                    row.addView(tv0);
                    row.addView(tv1);
                    table.addView(row);
                    i++;
                }
            }
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
