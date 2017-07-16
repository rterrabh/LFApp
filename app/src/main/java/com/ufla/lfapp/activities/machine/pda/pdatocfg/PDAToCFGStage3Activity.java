package com.ufla.lfapp.activities.machine.pda.pdatocfg;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.AppCompatActivityContext;
import com.ufla.lfapp.views.machine.layout.NonEditPDAutomatonLayout;
import com.ufla.lfapp.utils.UtilActivities;
import com.ufla.lfapp.utils.UtilSpannableTags;
import com.ufla.lfapp.utils.MyPoint;
import com.ufla.lfapp.core.machine.pda.PushdownAutomaton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PDAToCFGStage3Activity extends AppCompatActivityContext {

    public static String SPAN_RULE_2;
    public static String SPAN_RULE_3;

    private static final String PUSHDOWN_AUTOMATON_EXTRA = "pushdownAutomaton";
    private static final String LABEL_TO_POINT_EXTRA = "labelToPoint";

    private PushdownAutomaton pushdownAutomaton;
    private Map<String, MyPoint> labelToPoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdato_cfgstage3);
        readIntent();
        try {
            SPAN_RULE_2 = getResources()
                    .getString(R.string.algolPDATOCFGRule2);
            SPAN_RULE_3 = getResources()
                    .getString(R.string.algolPDATOCFGRule3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setView();
    }



    public void setView() {
        PDAToCFGStage3Model model = new PDAToCFGStage3Model(pushdownAutomaton);
        NonEditPDAutomatonLayout nonEditPDAutomatonLayout = new NonEditPDAutomatonLayout(this);
        nonEditPDAutomatonLayout.drawAutomatonExtendWithLabelMyPoint(model.getPdaExt(), labelToPoint);
        nonEditPDAutomatonLayout.removeSpaces();
        ( (ViewGroup) nonEditPDAutomatonLayout.getRootView()).removeAllViews();
        ((ViewGroup) nonEditPDAutomatonLayout.getParent()).setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        ConstraintLayout rootAutomataLayout = (ConstraintLayout) findViewById(R.id.pdaExt);
        rootAutomataLayout.addView((View) nonEditPDAutomatonLayout.getParent());
        TextView tvGrammarAct = (TextView) findViewById(R.id.grammarActual);
        tvGrammarAct.setText(model.grammarToString());
        model.defineNewRules();
/*
        ListView lvNewTransitions = (ListView) findViewById(R.id.newTransitions);
        List<String> newTransitionsStr = new ArrayList<>();
        String nextRule = null;
        while ((nextRule = model.nextNewTransition()) != null) {
            newTransitionsStr.add(nextRule);
        }
        newTransitionsStr.add("");
        ArrayAdapterTransition arrayAdapterTransition =
                new ArrayAdapterTransition(this, newTransitionsStr);
        lvNewTransitions.setAdapter(arrayAdapterTransition);
*/
        LinearLayout lvNewTransitions = (LinearLayout) findViewById(R.id.newTransitions);
        List<String> newTransitionsStr = new ArrayList<>();
        String nextRule = null;
        while ((nextRule = model.nextNewTransition()) != null) {
            TextView textView = new TextView(this);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textView.setTextColor(Color.BLACK);
            try {
                textView.setText(UtilSpannableTags.decode(nextRule));
            } catch (Exception e) {
                e.printStackTrace();
            }
            lvNewTransitions.addView(textView);
        }

        TextView tvGrammar = (TextView) findViewById(R.id.grammar);
        tvGrammar.setText(model.grammarToString());
        TextView tvAlgol = (TextView) findViewById(R.id.algol);
        try {
            tvAlgol.setText(UtilSpannableTags.decode(getResources()
                    .getString(R.string.algolPDAToCFGStage3)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readIntent() {
        Intent intent = getIntent();
        Map<String, MyPoint> points = (Map<String, MyPoint>) intent.getExtras()
                .getSerializable(LABEL_TO_POINT_EXTRA);
        if (points == null) {
        } else {
            UtilActivities.logMap(points);
        }

        if (intent != null
                && intent.getExtras() != null
                && intent.getExtras().getSerializable(PUSHDOWN_AUTOMATON_EXTRA) != null
                && intent.getExtras().getSerializable(LABEL_TO_POINT_EXTRA) != null) {
            Bundle dados = intent.getExtras();
            pushdownAutomaton = (PushdownAutomaton) dados.getSerializable(PUSHDOWN_AUTOMATON_EXTRA);
            labelToPoint = (Map<String, MyPoint>) dados.getSerializable(LABEL_TO_POINT_EXTRA);
        }
    }

    public void next(View view) {
        Bundle params = new Bundle();
        params.putSerializable(PUSHDOWN_AUTOMATON_EXTRA, pushdownAutomaton);
        params.putSerializable(LABEL_TO_POINT_EXTRA, (Serializable) labelToPoint);
        Intent intent = new Intent(getBaseContext(), PDAToCFGStage4Activity.class);
        intent.putExtras(params);
        startActivity(intent);
        finish();
    }

    public void back(View view) {
        Bundle params = new Bundle();
        params.putSerializable(PUSHDOWN_AUTOMATON_EXTRA, pushdownAutomaton);
        params.putSerializable(LABEL_TO_POINT_EXTRA, (Serializable) labelToPoint);
        Intent intent = new Intent(getBaseContext(), PDAToCFGStage2Activity.class);
        intent.putExtras(params);
        startActivity(intent);
        finish();
    }
}
