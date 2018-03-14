package com.ufla.lfapp.activities.machine.pda.pdatocfg;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.AppCompatActivityContext;
import com.ufla.lfapp.views.machine.layout.NonEditPDAutomatonLayout;
import com.ufla.lfapp.utils.UtilActivities;
import com.ufla.lfapp.utils.MyPoint;
import com.ufla.lfapp.core.machine.pda.PushdownAutomaton;

import java.io.Serializable;
import java.util.Map;

public class PDAToCFGStage1Activity extends AppCompatActivityContext {

    private static final String PUSHDOWN_AUTOMATON_EXTRA = "pushdownAutomaton";
    private static final String LABEL_TO_POINT_EXTRA = "labelToPoint";

    private PushdownAutomaton pushdownAutomaton;
    private Map<String, MyPoint> labelToPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdato_cfgstage1);
        readIntent();
        setView();
    }

    public void setView() {
        PDAToCFGStage1Model model = new PDAToCFGStage1Model(pushdownAutomaton);
        NonEditPDAutomatonLayout nonEditPDAutomatonLayout = new NonEditPDAutomatonLayout(this);
        nonEditPDAutomatonLayout.drawPDAWithLabel(pushdownAutomaton, labelToPoint);
        nonEditPDAutomatonLayout.removeSpaces();
        ( (ViewGroup) nonEditPDAutomatonLayout.getRootView()).removeAllViews();
        ((ViewGroup) nonEditPDAutomatonLayout.getParent()).setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        ConstraintLayout rootAutomataLayout = (ConstraintLayout) findViewById(R.id.pda);
        rootAutomataLayout.addView((View) nonEditPDAutomatonLayout.getParent());
        NonEditPDAutomatonLayout nonEditPDAutomatonExtLayout = new NonEditPDAutomatonLayout(this);
        nonEditPDAutomatonExtLayout.drawAutomatonExtendWithLabelMyPoint(model.getPdaExt(),
                labelToPoint);
        nonEditPDAutomatonExtLayout.removeSpaces();
        ( (ViewGroup) nonEditPDAutomatonExtLayout.getRootView()).removeAllViews();
        ((ViewGroup) nonEditPDAutomatonExtLayout.getParent()).setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        rootAutomataLayout = (ConstraintLayout) findViewById(R.id.pdaExtend);
        rootAutomataLayout.addView((View) nonEditPDAutomatonExtLayout.getParent());
        TextView tvNewTransitions = (TextView) findViewById(R.id.newTransitions);
        tvNewTransitions.setText(model.getNewTransitionsToString());
        TextView tvAlgol = (TextView) findViewById(R.id.algol);
        tvAlgol.setText(Html.fromHtml(getResources().getString(R.string.algolPDAToCFGStage1)));

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
        Intent intent = new Intent(getBaseContext(), PDAToCFGStage2Activity.class);
        intent.putExtras(params);
        startActivity(intent);
        finish();
    }

}
