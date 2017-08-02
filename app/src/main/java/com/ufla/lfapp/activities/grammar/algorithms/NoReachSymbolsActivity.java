package com.ufla.lfapp.activities.grammar.algorithms;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.grammar.HeaderGrammarActivity;
import com.ufla.lfapp.utils.UtilActivities;
import com.ufla.lfapp.core.grammar.AcademicSupport;
import com.ufla.lfapp.core.grammar.Grammar;
import com.ufla.lfapp.core.grammar.Rule;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by root on 25/07/16.
 */
public class NoReachSymbolsActivity extends HeaderGrammarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_no_reach_symbols);
        super.onCreate(savedInstanceState);
        setTitle();
        removingNotReachableSymbols(getGrammar());
    }

    private void setTitle() {
        switch(algorithm) {
            case CHOMSKY_NORMAL_FORM:
                setTitle(getResources().getString(R.string.lfapp_cnf_title)
                        + " - 5/6");
                break;
            case GREIBACH_NORMAL_FORM:
                setTitle(getResources().getString(R.string.lfapp_gnf_title)
                        + " - 5/7");
                break;
            case REMOVE_LEFT_RECURSION:
                setTitle(getResources().getString(R.string.lfapp_left_recursion_title)
                        + " - 5/7");
                break;
        }
    }

    @Override
    protected Grammar getGrammar() {
        switch(algorithm) {
            case CHOMSKY_NORMAL_FORM:
            case REMOVE_LEFT_RECURSION:
            case GREIBACH_NORMAL_FORM:
                Grammar g = new Grammar(grammar);
                g = g.getGrammarWithInitialSymbolNotRecursive(g, new
                        AcademicSupport());
                g = g.getGrammarEssentiallyNoncontracting(g, new
                        AcademicSupport());
                g = g.getGrammarWithoutChainRules(g, new AcademicSupport());
                return g.getGrammarWithoutNoTerm(g, new AcademicSupport());
            default: return super.getGrammar();
        }
    }

    public void back(View view) {
        changeActivity(this, NoTermSymbolsActivity.class);
    }

    public void next(View view) {
        changeActivity(this, ChomskyNormalFormActivity.class);
    }

    public void removingNotReachableSymbols(final Grammar g) {
        Grammar gc = (Grammar) g.clone();
        AcademicSupport academic = new AcademicSupport();
        StringBuilder comments = new StringBuilder();

        //Realiza processo
        gc = gc.getGrammarWithoutNoReach(gc, academic);
        academic.setResult(gc);

        //Realiza comentários sobre o processo
        comments.append(getString(R.string.reach_cnf_comments));

        //Mostra o resultado do processo
        TextView tableOfResult = (TextView) findViewById(R.id.ResultNoReach);
        tableOfResult.setText(Html.fromHtml(academic.getResult()));

        if (academic.getSituation()) {
            //Configura comentários
            academic.setComments(comments.toString());
            TextView commentsOfNoReach = (TextView) findViewById(R.id.CommentsNoReach);
            commentsOfNoReach.setText(academic.getComments());

            //Primeiro passo do processo (Construção dos Conjuntos)
            TextView step1 = (TextView) findViewById(R.id.NoReachStep1);
            step1.setText(getString(R.string.reach_cnf_step_1)
                    + gc.getInitialSymbol() + ".");
            TextView pseudo = (TextView) findViewById(R.id.PseudoReachAlgorithm);
            pseudo.setText(Html.fromHtml(getReachAlgorithm()));
            TableLayout tableOfSets = (TableLayout) findViewById(R.id.tableOfSetsNoReach);
            tableOfSets.setShrinkAllColumns(true);
            tableOfSets.setBackgroundColor(getResources().getColor(R.color.Gainsboro));
            printTableOfReachSets(tableOfSets, academic, "REACH", "PREV", "NEW");

            //Segundo passo do processo (Eliminar os símbolos não terminais)
            if (academic.getIrregularRules().size() != 0) {
                TextView step2 = (TextView) findViewById(R.id.NoReachStep2);
                List<String> array = UtilActivities.convertSetToArray
                        (academic.getFirstSet
                        ().get(academic.getFirstSet().size() - 1), gc);
                String variables = array.toString();
                variables = variables.replace("[", "{");
                variables = variables.replace("]", "}");
                String othersVariables = UtilActivities.selectOthersVariables(g, academic.getFirstSet().get(academic.getFirstSet().size() - 1));
                step2.setText(getString(R.string.reach_cnf_step_2) + variables + ", i.e., " + othersVariables +".");
                TableLayout tableResult = (TableLayout) findViewById(R.id.tableOfIrregularRulesReach);
                UtilActivities.printOldGrammarOfTermAndReach(g, tableResult,
                        academic, this);
            } else {
                TextView step2 = (TextView) findViewById(R.id.NoReachStep2);
                step2.setText(R.string.reach_cnf_step_2_1);
            }

        } else {
            TextView commentsOfNoReach = (TextView) findViewById(R.id.CommentsNoReach);
            commentsOfNoReach.setText(R.string.reach_cnf_step_comments_2);
        }

    }

    /**
     * Método que retorna o pseudocódigo do algoritmo  REACH
     * @return
     */
    public String getReachAlgorithm() {
        StringBuilder algol = new StringBuilder();
        algol.append(getString(R.string.reach_cnf_algol));
        return algol.toString();
    }

    /**
     * Método que imprime a tabela dos conjuntos utilizados durante a execução do algoritmo REACH
     * @param academic : informaçãos coletadas durante a execução do algoritmo
     */
    private void printTableOfReachSets(TableLayout table, final AcademicSupport academic,
                                       String nameOfFirstSet, String nameOfSecondSet,
                                       String nameOfThirdSet) {
        int i = 0;
        TableRow header = new TableRow(this);
        TextView htv0 = new TextView(this);
        htv0.setText("");
        htv0.setPadding(10, 10, 10, 10);
        htv0.setTextColor(getResources().getColor(R.color.Black));
        TextView htv1 = new TextView(this);
        htv1.setText(nameOfFirstSet);
        htv1.setPadding(10, 10, 10, 10);
        htv1.setBackgroundColor(getResources().getColor(R.color.DarkGray));
        htv1.setTextColor(getResources().getColor(R.color.Black));
        TextView htv2 = new TextView(this);
        htv2.setText(nameOfSecondSet);
        htv2.setPadding(10, 10, 10, 10);
        htv2.setTextColor(getResources().getColor(R.color.Black));
        TextView htv3 = new TextView(this);
        htv3.setText(nameOfThirdSet);
        htv3.setPadding(10, 10, 10, 10);
        htv3.setBackgroundColor(getResources().getColor(R.color.DarkGray));
        htv3.setTextColor(getResources().getColor(R.color.Black));
        header.addView(htv0);
        header.addView(htv1);
        header.addView(htv2);
        header.addView(htv3);
        table.addView(header);

        Iterator<Set<Rule>> iteratorOfFirstCell = (Iterator) academic.getFirstSet().iterator();
        Iterator<Set<Rule>> iteratorOfSecondCell = (Iterator) academic.getSecondSet().iterator();
        Iterator<Set<Rule>> iteratorOfThirdCell = (Iterator) academic.getThirdSet().iterator();

        while (iteratorOfSecondCell.hasNext()) {
            Set<Rule> firstSet = iteratorOfFirstCell.next();
            Set<Rule> secondSet = iteratorOfSecondCell.next();
            Set<Rule> thirdSet = iteratorOfThirdCell.next();

            String set1 = firstSet.toString();
            set1 = set1.replace("[", "{");
            set1 = set1.replace("]", "}");

            String set2 = secondSet.toString();
            set2 = set2.replace("[", "{");
            set2 = set2.replace("]", "}");

            String set3 = thirdSet.toString();
            set3 = set3.replace("[", "{");
            set3 = set3.replace("]", "}");

            TextView tv0 = new TextView(this);
            tv0.setText("(" + i + ")");
            tv0.setPadding(10, 10, 10, 10);
            tv0.setTextColor(getResources().getColor(R.color.Black));

            TextView tv1 = new TextView(this);
            tv1.setText(set1);
            tv1.setPadding(10, 10, 10, 10);
            tv1.setBackgroundColor(getResources().getColor(R.color.DarkGray));
            tv1.setTextColor(getResources().getColor(R.color.Black));

            TextView tv2 = new TextView(this);
            tv2.setText(set2);
            tv2.setPadding(10, 10, 10, 10);
            tv2.setTextColor(getResources().getColor(R.color.Black));

            TextView tv3 = new TextView(this);
            tv3.setText(set3);
            tv3.setPadding(10, 10, 10, 10);
            tv3.setTextColor(getResources().getColor(R.color.Black));
            tv3.setBackgroundColor(getResources().getColor(R.color.DarkGray));

            TableRow row = new TableRow(this);
            row.addView(tv0);
            row.addView(tv1);
            row.addView(tv2);
            row.addView(tv3);

            table.addView(row);
            i++;
        }
    }

}
