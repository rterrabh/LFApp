package com.ufla.lfapp.activities.grammar.algorithms;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.grammar.HeaderGrammarActivity;
import com.ufla.lfapp.core.grammar.AcademicSupport;
import com.ufla.lfapp.core.grammar.AcademicSupportFNG;
import com.ufla.lfapp.core.grammar.AcademicSupportRLR;
import com.ufla.lfapp.core.grammar.Grammar;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by carlos on 25/07/17.
 */

public class GreibachNormalFormTerraActivity extends HeaderGrammarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_greibach_normal_form_terra);
        super.onCreate(savedInstanceState);
        Log.d("REM_LEFT_REC", "INITIAL_ACTIVITY");
        removingLeftRecursion(getGrammar());
        fng(new Grammar(grammar));
    }

    @Override
    protected Grammar getGrammar() {
        switch(algorithm) {
            case GREIBACH_NORMAL_FORM:
                Grammar g = new Grammar(grammar);
                g = g.getGrammarWithInitialSymbolNotRecursive(g, new AcademicSupport());
                g = g.getGrammarEssentiallyNoncontracting(g, new AcademicSupport());
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
        onBackPressed();
    }

    public void fng(final Grammar g) {
        AcademicSupport academic = new AcademicSupport();
        Grammar gAux = g.getGrammarWithInitialSymbolNotRecursive(g, new AcademicSupport());
        gAux = gAux.getGrammarEssentiallyNoncontracting(gAux, new AcademicSupport());
        gAux = gAux.getGrammarWithoutChainRules(gAux, new AcademicSupport());
        gAux = gAux.getGrammarWithoutNoTerm(gAux, new AcademicSupport());
        gAux = gAux.getGrammarWithoutNoReach(gAux, new AcademicSupport());
        gAux = gAux.FNC(gAux, new AcademicSupport());
        AcademicSupportFNG academicSupportFNG = new AcademicSupportFNG();
        Grammar gc = gAux.FNGTerra(gAux, academic, academicSupportFNG);
        academic.setResult(gc);

        //Coloca resultado na tela
        ((TextView) findViewById(R.id.GreibachNormalFormResult)).setText(Html
                .fromHtml(academic.getResult()));
        if (academic.getSituation()) {
            //Insere os comentários na tela
            TextView commentsOfFNG = (TextView) findViewById(R.id.FNGComentarios2);
            commentsOfFNG.append(Html
                    .fromHtml(getString(R.string.greibach_normal_formal_comments)));
            ((TextView) findViewById(R.id.step1GreibachNormalForm)).setText(Html
                    .fromHtml(getResources().getString(R.string.removing_left_recursive_terra_algol_p2)));
            for(String transformation : academicSupportFNG
                    .getGrammarTransformationsStage2()) {
                TextView textView = new TextView(this);
                textView.setTextColor(getResources().getColor(R.color.Black));
                textView.setSingleLine(false);
                textView.setText(Html.fromHtml(transformation));
                TableRow tableRow = new TableRow(this);
                tableRow.addView(textView);
                ((TableLayout) findViewById(R.id.step1GreibachNormalFormAcademicSupport))
                        .addView(tableRow);
            }

            ((TextView) findViewById(R.id.step2GreibachNormalForm)).setText(Html
                    .fromHtml(getResources().getString(R.string.removing_left_recursive_terra_algol_p3)));
            for(String transformation : academicSupportFNG
                    .getGrammarTransformationsStage3()) {
                TextView textView = new TextView(this);
                textView.setTextColor(getResources().getColor(R.color.Black));
                textView.setText(Html.fromHtml(transformation));
                TableRow tableRow = new TableRow(this);
                tableRow.addView(textView);
                ((TableLayout) findViewById(R.id.step2GreibachNormalFormAcademicSupport))
                        .addView(tableRow);
            }
        } else {
            ((TextView) findViewById(R.id.FNGComentarios2)).setText(R.string.greibach_normal_formal_already);
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

        if (academicSupport.getSituation()) {

            //Realiza o primeiro passo do processo (Ordenação das variáveis)
            ((TextView) findViewById(R.id.step1RemovalLeftRecursion)).setText(R.string.remove_left_recursion_step_1);
            TableLayout tableOfSortedVariables = (TableLayout) findViewById(R.id.tableOfSortedVariables);
            tableOfSortedVariables.setShrinkAllColumns(true);
            final String[] TABLE_HEADERS = getString(R.string.remove_left_recursion_table_header).split("#");
            printMap(tableOfSortedVariables, gc, sortedVariables, TABLE_HEADERS[0], TABLE_HEADERS[1]);

            //Realiza segundo passo do processo (Destaca recursões encontradas)
            ((TextView) findViewById(R.id.step2RemovalLeftRecursion)).setText(Html
                    .fromHtml(getResources().getString(R.string.removing_left_recursive_terra_algol_p1)));
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

}