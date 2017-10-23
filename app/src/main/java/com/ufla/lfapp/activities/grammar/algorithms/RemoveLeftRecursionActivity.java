package com.ufla.lfapp.activities.grammar.algorithms;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.grammar.HeaderGrammarActivity;
import com.ufla.lfapp.core.grammar.AcademicSupport;
import com.ufla.lfapp.core.grammar.AcademicSupportRLR;
import com.ufla.lfapp.core.grammar.Grammar;

import java.util.LinkedHashMap;
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
            case GREIBACH_NORMAL_FORM:
                setTitle(getResources().getString(R.string.lfapp_gnf_title)
                        + " - 7/8");
                break;
            case REMOVE_LEFT_RECURSION:
                setTitle(getResources().getString(R.string.lfapp_left_recursion_title)
                        + " - 7/7");
                break;
        }
    }

    @Override
    protected Grammar getGrammar() {
        switch(algorithm) {
            case REMOVE_LEFT_RECURSION:
            case GREIBACH_NORMAL_FORM:
                Grammar g = new Grammar(grammar);
                g = g.getGrammarWithInitialSymbolNotRecursive(g, new
                        AcademicSupport());
                g = g.getGrammarEssentiallyNoncontracting(g, new
                        AcademicSupport());
                g = g.getGrammarWithoutChainRules(g, new AcademicSupport());
                g = g.getGrammarWithoutNoTerm(g, new AcademicSupport());
                g = g.getGrammarWithoutNoReach(g, new AcademicSupport());
                return g.FNC(g, new AcademicSupport());
            default: return super.getGrammar();
        }
    }

    public void back(View view) {
        changeActivity(this, ChomskyNormalFormActivity.class);
    }

    public void next(View view) {
        switch(algorithm) {
            case REMOVE_LEFT_RECURSION:
                onBackPressed();
                break;
            case GREIBACH_NORMAL_FORM:
                changeActivity(this, GreibachNormalFormActivity.class);
                break;
        }
    }

    public void removingLeftRecursion(final Grammar g) {
        Grammar gc = (Grammar) g.clone();

        //Realiza processo
        AcademicSupport academicSupport = new AcademicSupport();
        AcademicSupportRLR academicSupportLR = new
                AcademicSupportRLR();
        Map<String, String> sortedVariables = new LinkedHashMap<>();
        gc = gc.removingLeftRecursionTerra(gc, academicSupport, sortedVariables,
                academicSupportLR);
        academicSupport.setResult(gc);
        TextView resultGrammar = (TextView) findViewById(R.id.RemovalLeftRecursion);
        resultGrammar.setText(Html.fromHtml(academicSupport.getResult()));

        if (academicSupport.getSituation()) {
            //Realiza comentários sobre o processo
            TextView text = (TextView) findViewById(R.id.commentsOfRemovalLeftRecursion);
            text.setText(R.string.remove_left_recursion_comments);
            academicSupport.setComments(text.toString());

            //Realiza o primeiro passo do processo (Ordenação das variáveis)
            ((TextView) findViewById(R.id.step1RemovalLeftRecursion)).setText(R.string.remove_left_recursion_step_1);
            TableLayout tableOfSortedVariables = (TableLayout) findViewById(R.id.tableOfSortedVariables);
            tableOfSortedVariables.setShrinkAllColumns(true);
            final String[] TABLE_HEADERS = getString(R.string.remove_left_recursion_table_header).split("#");
            printMap(tableOfSortedVariables, gc, sortedVariables, TABLE_HEADERS[0], TABLE_HEADERS[1]);

            //Realiza segundo passo do processo (Destaca recursões encontradas)
            ((TextView) findViewById(R.id.step2RemovalLeftRecursion)).setText(Html
                    .fromHtml(getResources().getString(R.string.removing_left_recursive_algol_p1)));
            //step2.setText("(2) Localizar as recursões.");


           /*TableLayout tableOfIrregularRules = (TableLayout) findViewById(R
                   .id.tableOfLeftRecursion);
           printGrammarWithoutOldRules(g, tableOfIrregularRules,
                   academicSupport);*/
            TableLayout removalLeftRecursion = (TableLayout) findViewById(R.id
                    .step2RemovalLeftRecursionAcademicSupport);
            for(String transformation : academicSupportLR
                    .getGrammarTransformationsStage1()) {
                TextView textView = new TextView(this);
                textView.setTextColor(getResources().getColor(R.color.Black));
                textView.setSingleLine(false);
                textView.setText(Html.fromHtml(transformation));
                TableRow tableRow = new TableRow(this);
                tableRow.addView(textView);
                removalLeftRecursion.addView(tableRow);
            }

            //Realiza terceiro passo do processo (Destaca as mudanças finais)
           /*TextView step3 = (TextView) findViewById(R.id
                   .step3RemovalLeftRecursion);
           step3.setText("(3) Alterar as regras");
           TableLayout tableOfNewRules = (TableLayout) findViewById(R.id.tableWithoutLeftRecursion);
           printGrammarWithNewRules(gc, tableOfNewRules, academicSupport);*/


        } else {
            TextView text = (TextView) findViewById(R.id.commentsOfRemovalLeftRecursion);
            if (academicSupport.getSolutionDescription().isEmpty()) {
                text.setText(R.string.remove_left_recursion_dont_have);
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
