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

/**
 * Created by root on 25/07/16.
 */
public class ChomskyNormalFormActivity extends HeaderGrammarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_chomsky_normal_form);
        super.onCreate(savedInstanceState);
        setTitle();
        fnc(getGrammar());
    }

    private void setTitle() {
        switch(algorithm) {
            case CHOMSKY_NORMAL_FORM:
                setTitle(getResources().getString(R.string.lfapp_cnf_title)
                        + " - 6/6");
                break;
            case GREIBACH_NORMAL_FORM:
                setTitle(getResources().getString(R.string.lfapp_gnf_title)
                        + " - 6/7");
                break;
            case REMOVE_LEFT_RECURSION:
                setTitle(getResources().getString(R.string.lfapp_left_recursion_title)
                        + " - 6/7");
                break;
        }
    }

    @Override
    protected Grammar getGrammar() {
        switch(algorithm) {
            case GREIBACH_NORMAL_FORM:
            case REMOVE_LEFT_RECURSION:
            case CHOMSKY_NORMAL_FORM:
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
        switch (algorithm) {
            case CHOMSKY_NORMAL_FORM:
                onBackPressed();
                break;
            case REMOVE_LEFT_RECURSION:
                changeActivity(this, RemoveLeftRecursionActivity.class);
                break;
            case GREIBACH_NORMAL_FORM:
                changeActivity(this, GreibachNormalFormTerraActivity.class);
                break;
        }
    }

    public void fnc(final Grammar g) {
        AcademicSupport academic = new AcademicSupport();
        Grammar gAux = (Grammar) g.clone();
        Grammar gc = g.FNC(g, academic);
        academic.setResult(gc);

        StringBuilder comments = new StringBuilder();

        //Realiza comentários sobre o processo
        comments.append(getResources().getString(R.string.chomsky_normal_form_algol_comments));

        //Coloca resultado de FNC na tela
        TextView chomskyResult = (TextView) findViewById(R.id.FNCResultado);
        chomskyResult.setText(Html.fromHtml(academic.getResult()));

        if (academic.getSituation()) {
            //Coloca comentários do processo na tela
            academic.setComments(comments.toString());
            TextView commentsOfFNC = (TextView) findViewById(R.id.ComentariosFNC6);
            commentsOfFNC.setText(academic.getComments());

            //Realiza segundo passo do processo (Mostra gramática com destaques e sem estar em FNC)
            TextView step1FNC = (TextView) findViewById(R.id.Passo2FNC);
            step1FNC.setText(R.string.chomsky_normal_form_step_1);
            TableLayout redGrammar = (TableLayout) findViewById(R.id.Passo2FNC_Resultado);
            UtilActivities.printGrammarWithoutOldRules(gAux, redGrammar,
                    academic, this);

            //Realiza o terceiro passo do processo (Mostra gramática com destaques em FNC)
            TextView step2FNC = (TextView) findViewById(R.id.Passo3FNC);
            step2FNC.setText(R.string.chomsky_normal_form_step_2);
            TableLayout blueGrammar  = (TableLayout) findViewById(R.id.Passo3FNC_Resultado);
            UtilActivities.printGrammarWithNewRules(gc, blueGrammar,
                    academic, this);
        } else {
            TextView commentsOfFNC = (TextView) findViewById(R.id.ComentariosFNC6);
            commentsOfFNC.setText(R.string.chomsky_normal_form_already_cnf);
        }
    }

}
