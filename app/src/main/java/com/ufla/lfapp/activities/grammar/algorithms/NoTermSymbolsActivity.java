package com.ufla.lfapp.activities.grammar.algorithms;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.grammar.HeaderGrammarActivity;
import com.ufla.lfapp.utils.UtilActivities;
import com.ufla.lfapp.core.grammar.AcademicSupport;
import com.ufla.lfapp.core.grammar.Grammar;

import java.util.List;

/**
 * Created by root on 25/07/16.
 */
public class NoTermSymbolsActivity extends HeaderGrammarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_no_term_symbols);
        super.onCreate(savedInstanceState);
        setTitle();
        removingNotTerminalsSymbols(getGrammar());
    }

    private void setTitle() {
        switch(algorithm) {
            case CHOMSKY_NORMAL_FORM:
                setTitle(getResources().getString(R.string.lfapp_cnf_title)
                        + " - 4/6");
                break;
            case GREIBACH_NORMAL_FORM:
                setTitle(getResources().getString(R.string.lfapp_gnf_title)
                        + " - 4/7");
                break;
            case REMOVE_LEFT_RECURSION:
                setTitle(getResources().getString(R.string.lfapp_left_recursion_title)
                        + " - 4/7");
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
                return g.getGrammarWithoutChainRules(g, new AcademicSupport());
            default: return super.getGrammar();
        }
    }

    public void back(View view) {
        changeActivity(this, ChainRulesActivity.class);
    }

    public void next(View view) {
        changeActivity(this, NoReachSymbolsActivity.class);
    }

    public void removingNotTerminalsSymbols(final Grammar g) {
        AcademicSupport academicSupport = new AcademicSupport();
        //Realiza processo
        Grammar gc = g.getGrammarWithoutNoTerm(g, academicSupport);
        academicSupport.setResult(gc);

        //Realiza comentários sobre o processo
        StringBuilder comments = new StringBuilder();
        comments.append(getString(R.string.term_cnf_comments));

        //Configura a gramática de resultado
        TextView resultGrammar = (TextView) findViewById(R.id.ResultNoTerm);
        resultGrammar.setText(Html.fromHtml(academicSupport.getResult()));

        if (academicSupport.getSituation()) {
            //Configura os comentários
            academicSupport.setComments(comments.toString());
            TextView result = (TextView) findViewById(R.id.CommentsNoTerms);
            result.setText(Html.fromHtml(academicSupport.getComments()));

            //Configura o primeiro passo (Montar conjuntos)
            TextView creatingSetOfChains = (TextView) findViewById(R.id.NoTermStep1);
            creatingSetOfChains.setText(R.string.term_cnf_step_1);
            TextView pseudo = (TextView) findViewById(R.id.PseudoTermAlgorithm);
            pseudo.setText(Html.fromHtml(getTermAlgorithm()));
            TableLayout tableOfSets = (TableLayout) findViewById(R.id.TableOfSetsNoTerm);
            tableOfSets.setShrinkAllColumns(true);
            tableOfSets.setBackgroundColor(getResources().getColor(R.color.Gainsboro));
            UtilActivities.printTableOfSets(tableOfSets, academicSupport,
                    "TERM", "PREV", this);

            //Configura o segundo passo (Regras que foram removidas)
            TextView eliminatingNoTermRules = (TextView) findViewById(R.id.NoTermStep2);
            List<String> array = UtilActivities.convertSetToArray(academicSupport.getFirstSet().get(academicSupport.getFirstSet().size() - 1), gc);
            String variables = array.toString();
            variables = variables.replace("[", "{");
            variables = variables.replace("]", "}");
            String othersVariables = UtilActivities.selectOthersVariables(g,
                    academicSupport.getFirstSet().get(academicSupport.getFirstSet().size() - 1));
            eliminatingNoTermRules.setText(getString(R.string.term_cnf_step_2) + variables +", \n i.e., "+ othersVariables +".");
            TableLayout grammarWithoutOldRules = (TableLayout) findViewById(R.id.GrammarWithNoTerm);
            UtilActivities.printOldGrammarOfTermAndReach(g,
                    grammarWithoutOldRules, academicSupport, this);
        } else {
            TextView result = (TextView) findViewById(R.id.CommentsNoTerms);
            result.setText(R.string.term_cnf_result);
        }
    }

    /**
     * Método que retorna o pseudocódigo do algoritmo TERM
     * @return
     */
    public String getTermAlgorithm() {
        return getString(R.string.term_cnf_algol);
    }
}
