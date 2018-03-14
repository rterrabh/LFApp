package com.ufla.lfapp.activities.machine.pda;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.AppCompatActivityContext;
import com.ufla.lfapp.core.machine.pda.PushdownAutomaton;
import com.ufla.lfapp.core.machine.pda.PDASimulator;
import com.ufla.lfapp.utils.MyPoint;
import com.ufla.lfapp.views.machine.configuration.TapeStackView;
import com.ufla.lfapp.views.machine.layout.NonEditPDAutomatonLayout;

import java.util.Map;

public class ProcessWordPDAActivity extends AppCompatActivityContext {

    private static final String PDA_AUTOMATON_EXTRA = "PDAAutomaton";
    private static final String LABEL_TO_POINT_EXTRA = "labelToPoint";
    private static final String WORD_EXTRA = "word";

    private PushdownAutomaton pushdownAutomaton;
    private Map<String, MyPoint> labelToPoint;
    private String word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_word_pda);
        readIntent();
        setView();
    }

    private void setView() {
        NonEditPDAutomatonLayout nonEditGraphLayout = new NonEditPDAutomatonLayout(this);
        nonEditGraphLayout.drawPDAWithLabel(pushdownAutomaton, labelToPoint);
        nonEditGraphLayout.removeSpaces();
        ( (ViewGroup) nonEditGraphLayout.getRootView()).removeAllViews();
        ((ViewGroup) nonEditGraphLayout.getParent()).setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        ConstraintLayout rootAutomataLayout = (ConstraintLayout) findViewById(R.id.fsa);
        rootAutomataLayout.addView((View)nonEditGraphLayout.getParent());
        TextView tvGrammarAct = (TextView) findViewById(R.id.word);
        tvGrammarAct.setText(word);
        TextView tvResult = (TextView) findViewById(R.id.result);

        LinearLayout lvNewTransitions = (LinearLayout) findViewById(R.id.configurations);
        PDASimulator simulator = new PDASimulator(pushdownAutomaton, word);
        try {
            if (simulator.process()) {
                tvResult.setText(R.string.accept);
            } else {
                tvResult.setText(R.string.reject);
            }
        } catch (Exception e) {
            tvResult.setText(e.getMessage());
            e.printStackTrace();
        }
//        List<Spannable> spannables = simulator.getSpanConfigurations();
//        for (Spannable span : spannables) {
//            TextView textView = new TextView(this);
//            textView.setTextColor(Color.BLACK);
//            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//            textView.setText(span);
//            lvNewTransitions.addView(textView);
//        }
        String[] stacks = simulator.getStacks();
        PDASimulator.Configuration[] configurations =
                simulator.getsConfigurations();

        for (int i = 0; i < stacks.length; i++) {
            TapeStackView tapeStackView = new TapeStackView(this);
            tapeStackView.setTape(word);
            tapeStackView.setIndex(configurations[i].getIndex());
            tapeStackView.setState(configurations[i].getState());
            tapeStackView.setInitialState(configurations[i].isInitialState());
            tapeStackView.setFinalState(configurations[i].isFinalState());
            tapeStackView.setStack(stacks[i]);
            tapeStackView.finalize();
            lvNewTransitions.addView(tapeStackView);
        }
//        for (String conf : newTransitionsStr) {
//            TextView textView = new TextView(this);
//            textView.setTextColor(Color.BLACK);
//            textView.setText(conf);
//            lvNewTransitions.addView(textView);
//        }
    }

    public void readIntent() {
        Intent intent = getIntent();
        if (intent != null
                && intent.getExtras() != null
                && intent.getExtras().getSerializable(PDA_AUTOMATON_EXTRA) != null
                && intent.getExtras().getSerializable(LABEL_TO_POINT_EXTRA) != null
                && intent.getExtras().getSerializable(WORD_EXTRA) != null) {
            Bundle dados = intent.getExtras();
            pushdownAutomaton = (PushdownAutomaton) dados.getSerializable(PDA_AUTOMATON_EXTRA);
            labelToPoint = (Map<String, MyPoint>) dados.getSerializable(LABEL_TO_POINT_EXTRA);
            word = (String) dados.getSerializable(WORD_EXTRA);
        }
    }

}
