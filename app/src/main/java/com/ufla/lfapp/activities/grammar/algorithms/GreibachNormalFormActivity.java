package com.ufla.lfapp.activities.grammar.algorithms;

import android.os.Bundle;
import android.text.Html;
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

import java.util.HashMap;

/**
 * Created by carlos on 30/07/16.
 */
public class GreibachNormalFormActivity extends HeaderGrammarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_greibach_normal_form);
        super.onCreate(savedInstanceState);
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
                g = g.FNC(g, new AcademicSupport());
                return g.removingLeftRecursion
                        (g, new AcademicSupport(), new HashMap<String, String>(),
                        new AcademicSupportRLR());
            default: return super.getGrammar();
        }
    }

    public void back(View view) {
        changeActivity(this, RemoveLeftRecursionActivity.class);
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
                    .fromHtml(getResources().getString(R.string.removing_left_recursive_algol_p2)));
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
                    .fromHtml(getResources().getString(R.string.removing_left_recursive_algol_p3)));
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

}
