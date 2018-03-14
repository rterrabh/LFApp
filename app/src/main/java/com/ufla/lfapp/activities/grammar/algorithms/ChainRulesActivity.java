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
import java.util.Set;

/**
 * Created by root on 22/07/16.
 */
public class ChainRulesActivity extends HeaderGrammarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_chain_rules);
        super.onCreate(savedInstanceState);
        setTitle();
        removingChainRules(getGrammar());
    }

    private void setTitle() {
        switch(algorithm) {
            case CHOMSKY_NORMAL_FORM:
                setTitle(getResources().getString(R.string.lfapp_cnf_title)
                        + " - 3/6");
                break;
            case GREIBACH_NORMAL_FORM:
                setTitle(getResources().getString(R.string.lfapp_gnf_title)
                        + " - 3/8");
                break;
            case REMOVE_LEFT_RECURSION:
                setTitle(getResources().getString(R.string.lfapp_left_recursion_title)
                        + " - 3/7");
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
                return g.getGrammarEssentiallyNoncontracting(g, new
                        AcademicSupport());
            default: return super.getGrammar();
        }
    }

    public void back(View view) {
        changeActivity(this, EmptyProductionActivity.class);
    }

    public void next(View view) {
        changeActivity(this, NoTermSymbolsActivity.class);
    }

    public void removingChainRules(final Grammar g) {
        AcademicSupport academic = new AcademicSupport();

        //Realiza comentários sobre o processo
        StringBuilder comments = new StringBuilder();
        comments.append(getResources().getString(R.string.chain_rules_algol_comment));

        //Realiza processo
        Grammar gc = g.getGrammarWithoutChainRules(g, academic);
        academic.setResult(gc);

        //Configura a gramática de resultado
        TextView grammarResult = (TextView) findViewById(R.id.ResultGrammarWithoutChainRules);
        grammarResult.setText(Html.fromHtml(academic.getResult()));

        //Configura os comentários do processo
        academic.setComments(comments.toString());
        TextView commentsOfProcces =  (TextView) findViewById(R.id.CommentsChainRules);
        commentsOfProcces.setText(academic.getComments());

        //Configura o primeiro passo do processo
        TextView creatingSetOfChains =  (TextView) findViewById(R.id.RemovingChainRulesStep1);
        creatingSetOfChains.setText(R.string.algol_chain_rule_grammar_step_1);
        TextView pseudo =  (TextView) findViewById(R.id.PseudoChainAlgorithm);
        pseudo.setText(Html.fromHtml(getChainAlgorithm()));
        TableLayout tableOfChains = (TableLayout) findViewById(R.id.TableOfChains);
        tableOfChains.setShrinkAllColumns(true);
        final String[] HEADER_TABLE = getResources().getString(
                R.string.chain_rules_table_header
                ).split("#");
        printTableOfChains(tableOfChains, academic, HEADER_TABLE[0], HEADER_TABLE[1]);

        //configura o segundo passo do processo
        if (academic.getInsertedRules().size() != 0) {
            TextView creatingGrammarWithoutChains =  (TextView) findViewById(R.id.RemovingChainRulesStep2);
            creatingGrammarWithoutChains.setText(R.string.algol_chain_rule_grammar_step_2);
            TableLayout tableWithoutChains = (TableLayout) findViewById(R.id.GrammarWithChains);
            UtilActivities.printGrammarWithoutOldRules(g, tableWithoutChains,
                    academic, this);
        } else if (academic.getIrregularRules().size() != 0) {
            TextView creatingGrammarWithoutChains = (TextView) findViewById(R.id.RemovingChainRulesStep2);
            creatingGrammarWithoutChains.setText(R.string.algol_chain_rule_grammar_step_2_1);
        } else {
            TextView creatingGrammarWithoutChains = (TextView) findViewById(R.id.RemovingChainRulesStep2);
            creatingGrammarWithoutChains.setText(R.string.algol_chain_rule_grammar_step_2_2);
        }

        //Configura o terceiro passo do processo
        if (academic.getInsertedRules().size() != 0) {
            TextView creatingGrammarWithoutChains = (TextView) findViewById(R.id.RemovingChainRulesStep3);
            creatingGrammarWithoutChains.setText(R.string.algol_chain_rule_grammar_step_3);
            TableLayout tableWithoutChains = (TableLayout) findViewById(R.id.GrammarWithoutChains);
            UtilActivities.printGrammarWithNewRules(gc, tableWithoutChains,
                    academic, this);
        } else if (academic.getIrregularRules().size() != 0) {
            TextView creatingGrammarWithoutChains = (TextView) findViewById(R.id.RemovingChainRulesStep3);
            creatingGrammarWithoutChains.setText(R.string.algol_chain_rule_grammar_step_3_1);
        } else {
            TextView creatingGrammarWithoutChains = (TextView) findViewById(R.id.RemovingChainRulesStep3);
            creatingGrammarWithoutChains.setText(R.string.algol_chain_rule_grammar_step_3_2);
        }

    }

    /**
     * Método que retorna o pseudocódigo do algoritmo chain
     * @return
     */
    public String getChainAlgorithm() {
        return getResources().getString(R.string.chain_rule_algol);
    }

    /**
     * Método que imprime a tabela de chains
     * @param table
     * @param academic
     * @param nameOfFisrtCell
     * @param nameOfSecondCell
     */
    private void printTableOfChains(TableLayout table, final AcademicSupport academic, String nameOfFisrtCell, String nameOfSecondCell) {
        //Configura o cabeçalho da tabela
        TableRow header = new TableRow(this);
        TextView htv0 = new TextView(this);
        htv0.setText(nameOfFisrtCell);
        htv0.setTextColor(getResources().getColor(R.color.Black));
        htv0.setPadding(10, 10, 10, 10);

        TextView htv1 = new TextView(this);
        htv1.setText(nameOfSecondCell);
        htv1.setTextColor(getResources().getColor(R.color.Black));
        htv1.setPadding(10, 10, 10, 10);

        header.setBackgroundColor(getResources().getColor(R.color.DarkGray));
        header.addView(htv0);
        header.addView(htv1);
        table.addView(header);
        int i = 1;

        Iterator<Set<Rule>> iteratorOfFirstCell = (Iterator) academic.getFirstSet().iterator();
        Iterator<Set<Rule>> iteratorOfSecondCell = (Iterator) academic.getSecondSet().iterator();

        while (iteratorOfFirstCell.hasNext()) {
            Set<Rule> firstSet = iteratorOfFirstCell.next();
            Set<Rule> secondSet = iteratorOfSecondCell.next();

            String teste = firstSet.toString();
            String teste2 = secondSet.toString();

            String set1 = firstSet.toString();
            set1 = set1.replace("[", "(");
            set1 = set1.replace("]", ")");

            String set2 = secondSet.toString();
            set2 = set2.replace("[", "{");
            set2 = set2.replace("]", "}");

            TextView tv1 = new TextView(this);
            tv1.setText(set1);
            tv1.setPadding(10, 10, 10, 10);
            tv1.setTextColor(getResources().getColor(R.color.Black));

            TextView tv2 = new TextView(this);
            tv2.setText(set2);
            tv2.setPadding(10, 10, 10, 10);
            tv2.setTextColor(getResources().getColor(R.color.Black));

            TableRow row = new TableRow(this);
            if (i % 2 != 0) {
                row.setBackgroundColor(getResources().getColor(R.color.LightGrey));
            } else {
                row.setBackgroundColor(getResources().getColor(R.color.DarkGray));
            }

            row.addView(tv1);
            row.addView(tv2);
            i++;
            table.addView(row);
        }
    }



}
