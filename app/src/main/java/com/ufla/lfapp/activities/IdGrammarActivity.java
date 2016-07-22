package com.ufla.lfapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.vo.AcademicSupport;
import com.ufla.lfapp.vo.Grammar;
import com.ufla.lfapp.vo.GrammarParser;

/**
 * Created by root on 18/07/16.
 */
public class IdGrammarActivity extends HeaderGrammarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_id_grammar);
        super.onCreate(savedInstanceState);
        grammarType(new Grammar(grammar));
    }

    /**
     * Método que classifica a gramática passada como argumento em GR, GLC, GSC ou GI
     * @param g : gramática de entrada
     */
    public void grammarType(final Grammar g) {

        TableLayout tableGrammarType = (TableLayout) findViewById(R.id
                .tableGrammarType);
        if (tableGrammarType != null) {
            tableGrammarType.setStretchAllColumns(true);
        }
        //LINHA 1
        TableRow row0 = new TableRow(this);
        TextView tv0_0 = new TextView(this);
        tv0_0.setText("(3) GR");
        TextView tv0_1 = new TextView(this);
        tv0_1.setText("u ∈ V");
        TextView tv0_2 = new TextView(this);
        tv0_2.setText("v ∈ λ | Σ | ΣV");
        row0.addView(tv0_0);
        row0.addView(tv0_1);
        row0.addView(tv0_2);
        row0.setBackgroundColor(getResources().getColor(R.color.DarkGray));
        tableGrammarType.addView(row0);

        //LINHA 2
        TableRow row1 = new TableRow(this);
        TextView tv1_0 = new TextView(this);
        tv1_0.setText("(2) GLC");
        TextView tv1_1 = new TextView(this);
        tv1_1.setText("u ∈ V");
        TextView tv1_2 = new TextView(this);
        tv1_2.setText(Html.fromHtml("ν ∈ (V ∪ Σ)<sup>*</sup>"));
        row1.addView(tv1_0);
        row1.addView(tv1_1);
        row1.addView(tv1_2);
        row1.setBackgroundColor(getResources().getColor(R.color.Gainsboro));
        tableGrammarType.addView(row1);

        //LINHA 3
        TableRow row2 = new TableRow(this);
        TextView tv2_0 = new TextView(this);
        tv2_0.setText("(1) GSC");
        TextView tv2_1 = new TextView(this);
        tv2_1.setText(Html.fromHtml("u ∈ (V U Σ)<sup>+</sup>"));
        TextView tv2_2 = new TextView(this);
        tv2_2.setText(Html.fromHtml("v ∈ (V ∪ Σ)<sup>+</sup>"));
        row2.addView(tv2_0);
        row2.addView(tv2_1);
        row2.addView(tv2_2);
        row2.setBackgroundColor(getResources().getColor(R.color.DarkGray));
        tableGrammarType.addView(row2);

        //LINHA 4
        TableRow row3 = new TableRow(this);
        TextView tv3_0 = new TextView(this);
        tv3_0.setText("(0) GI");
        TextView tv3_1 = new TextView(this);
        tv3_1.setText(Html.fromHtml("u ∈ (V U Σ)<sup>+</sup>"));
        TextView tv3_2 = new TextView(this);
        tv3_2.setText(Html.fromHtml("v ∈ (V ∪ Σ)<sup>*</sup >"));
        row3.addView(tv3_0);
        row3.addView(tv3_1);
        row3.addView(tv3_2);
        row3.setBackgroundColor(getResources().getColor(R.color.Gainsboro));
        tableGrammarType.addView(row3);

        AcademicSupport academic = new AcademicSupport();

        StringBuilder comments = new StringBuilder();
        academic.setSolutionDescription(GrammarParser.classifiesGrammar(g, comments));

        TextView commentsOfSolution = (TextView) findViewById(R.id.comments);
        if(commentsOfSolution != null) {
            commentsOfSolution.setText(Html.fromHtml("<b><font color=red>Resultado:</b><br>"
                    + comments + "<br>" + academic.getSolutionDescription()));
        }
    }
}
