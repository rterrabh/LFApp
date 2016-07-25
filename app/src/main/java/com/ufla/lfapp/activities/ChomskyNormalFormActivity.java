package com.ufla.lfapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.utils.UtilActivities;
import com.ufla.lfapp.vo.AcademicSupport;
import com.ufla.lfapp.vo.Grammar;

/**
 * Created by root on 25/07/16.
 */
public class ChomskyNormalFormActivity extends HeaderGrammarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_chomsky_normal_form);
        super.onCreate(savedInstanceState);
        fnc(getGrammar());
    }

    @Override
    protected Grammar getGrammar() {
        switch(algorithm) {
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
                params.putInt("algorithm", Algorithm.NONE.getValue());
                Intent intent = new Intent(this, ChomskyNormalFormMenuActivity
                        .class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtras(params);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void fnc(final Grammar g) {
        AcademicSupport academic = new AcademicSupport();
        Grammar gAux = (Grammar) g.clone();
        Grammar gc = g.FNC(g, academic);
        academic.setResult(gc);

        StringBuilder comments = new StringBuilder();

        //Realiza comentários sobre o processo
        comments.append("\t\tUma GLC G = (V,Σ,P,S) está na Forma Normal de Chomsky se suas regras tem uma das seguintes formas:\n" +
                "\t\t- A -> BC\t onde B, C ∈ V − {S}\n" +
                "\t\t- A -> a\t onde a ∈ Σ\n" +
                "\t\t- S → λ");

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
            step1FNC.setText("(1) Identificar as regras que não estão na Forma Normal de Chomsky.");
            TableLayout redGrammar = (TableLayout) findViewById(R.id.Passo2FNC_Resultado);
            UtilActivities.printGrammarWithoutOldRules(gAux, redGrammar,
                    academic, this);

            //Realiza o terceiro passo do processo (Mostra gramática com destaques em FNC)
            TextView step2FNC = (TextView) findViewById(R.id.Passo3FNC);
            step2FNC.setText("(2) Transformar tais regras em um dos formatos válidos.");
            TableLayout blueGrammar  = (TableLayout) findViewById(R.id.Passo3FNC_Resultado);
            UtilActivities.printGrammarWithNewRules(gc, blueGrammar,
                    academic, this);
        } else {
            TextView commentsOfFNC = (TextView) findViewById(R.id.ComentariosFNC6);
            commentsOfFNC.setText("A gramática inserida já está na Forma Normal de Chomsky.");
        }
    }

}
