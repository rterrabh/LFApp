package com.ufla.lfapp.activities.machine.tm;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.AppCompatActivityContext;
import com.ufla.lfapp.views.machine.configuration.TapeView;
import com.ufla.lfapp.views.graph.layout.NonEditGraphLayout;
import com.ufla.lfapp.utils.MyPoint;
import com.ufla.lfapp.core.machine.tm.TuringMachine;
import com.ufla.lfapp.core.machine.tm.TMSimulator;

import java.util.Map;

public class ProcessWordTMActivity extends AppCompatActivityContext {

    private static final String TURING_MACHINE_EXTRA = "turingMachine";
    private static final String LABEL_TO_POINT_EXTRA = "labelToPoint";
    private static final String WORD_EXTRA = "word";

    private TuringMachine turingMachine;
    private Map<String, MyPoint> labelToPoint;
    private String word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_word_tm);
        readIntent();
        setView();
    }

    private void setView() {
        NonEditGraphLayout nonEditGraphLayout = new NonEditGraphLayout(this);
        nonEditGraphLayout.drawTuringMachineWithLabel(turingMachine, labelToPoint);
        nonEditGraphLayout.removeSpaces();
        ( (ViewGroup) nonEditGraphLayout.getRootView()).removeAllViews();
        ((ViewGroup) nonEditGraphLayout.getParent()).setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        ConstraintLayout rootAutomataLayout = (ConstraintLayout) findViewById(R.id.fsa);
        rootAutomataLayout.addView((View) nonEditGraphLayout.getParent());
        TextView tvGrammarAct = (TextView) findViewById(R.id.word);
        tvGrammarAct.setText(word);
        TextView tvResult = (TextView) findViewById(R.id.result);

        LinearLayout lvNewTransitions = (LinearLayout) findViewById(R.id.configurations);
        TMSimulator simulator = new TMSimulator(turingMachine, word);
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
//        List<Spannable> spannables = simulator.getTapesConfigurations();
//        for (Spannable span : spannables) {
//            TextView textView = new TextView(this);
//            textView.setTextColor(Color.BLACK);
//            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//            textView.setText(span);
//            lvNewTransitions.addView(textView);
//        }
        TMSimulator.Configuration[] configurations = simulator.getConfigurations();
        String[] tapes = simulator.getTapes();
        for (int i = 0; i < tapes.length; i++) {
            TapeView tapeView = new TapeView(this);
            tapeView.setTape(tapes[i]);
            tapeView.setIndex(configurations[i].getIndex());
            tapeView.setState(configurations[i].getState());
            tapeView.setInitialState(configurations[i].isInitialState());
            tapeView.setFinalState(configurations[i].isFinalState());
            tapeView.finalize();
            lvNewTransitions.addView(tapeView);
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
                && intent.getExtras().getSerializable(TURING_MACHINE_EXTRA) != null
                && intent.getExtras().getSerializable(LABEL_TO_POINT_EXTRA) != null
                && intent.getExtras().getSerializable(WORD_EXTRA) != null) {
            Bundle dados = intent.getExtras();
            turingMachine = (TuringMachine) dados.getSerializable(TURING_MACHINE_EXTRA);
            labelToPoint = (Map<String, MyPoint>) dados.getSerializable(LABEL_TO_POINT_EXTRA);
            word = (String) dados.getSerializable(WORD_EXTRA);
        }
    }
}
