package com.ufla.lfapp.activities.machine.tm;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.AppCompatActivityContext;
import com.ufla.lfapp.core.machine.tm.var.TuringMachineMultiTape;
import com.ufla.lfapp.core.machine.tm.var.TMMultiTapeSimulator;
import com.ufla.lfapp.utils.MyPoint;
import com.ufla.lfapp.views.machine.configuration.MultiTapeView;
import com.ufla.lfapp.views.machine.layout.EditTMMultiTapeLayout;

import java.util.Map;

public class ProcessWordTMMultiTapeActivity extends AppCompatActivityContext {

    private static final String TURING_MACHINE_EXTRA = "turingMachine";
    private static final String LABEL_TO_POINT_EXTRA = "labelToPoint";
    private static final String WORD_EXTRA = "word";

    private TuringMachineMultiTape turingMachine;
    private Map<String, MyPoint> labelToPoint;
    private String word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_word_tmmulti_tape);
        readIntent();
        setView();
    }

    private void setView() {
        EditTMMultiTapeLayout editTMMultiTapeLayout = new EditTMMultiTapeLayout(this);
        editTMMultiTapeLayout.setNumTapes(turingMachine.getNumTapes());
        editTMMultiTapeLayout.drawTMWithLabel(turingMachine, labelToPoint);
        editTMMultiTapeLayout.removeSpaces();
        ( (ViewGroup) editTMMultiTapeLayout.getRootView()).removeAllViews();
        ((ViewGroup) editTMMultiTapeLayout.getParent()).setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        ConstraintLayout rootAutomataLayout = (ConstraintLayout) findViewById(R.id.fsa);
        rootAutomataLayout.addView((View) editTMMultiTapeLayout.getParent());
        TextView tvGrammarAct = (TextView) findViewById(R.id.word);
        tvGrammarAct.setText(word);
        TextView tvResult = (TextView) findViewById(R.id.result);

        LinearLayout lvNewTransitions = (LinearLayout) findViewById(R.id.configurations);
        TMMultiTapeSimulator simulator =
                new TMMultiTapeSimulator(turingMachine, word);
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
        TMMultiTapeSimulator.Configuration[] configurations = simulator.getConfigurations();
        for (int i = 0; i < configurations.length; i++) {
            MultiTapeView tapeView = new MultiTapeView(this);
            tapeView.setTapes(configurations[i].getTapes());
            tapeView.setIndex(configurations[i].getIndex());
            tapeView.setState(configurations[i].getState());
            tapeView.setInitialState(configurations[i].isInitialState());
            tapeView.setFinalState(configurations[i].isFinalState());
            tapeView.finalize();
            lvNewTransitions.addView(tapeView);
        }
    }

    public void readIntent() {
        Intent intent = getIntent();
        if (intent != null
                && intent.getExtras() != null
                && intent.getExtras().getSerializable(TURING_MACHINE_EXTRA) != null
                && intent.getExtras().getSerializable(LABEL_TO_POINT_EXTRA) != null
                && intent.getExtras().getSerializable(WORD_EXTRA) != null) {
            Bundle dados = intent.getExtras();
            turingMachine = (TuringMachineMultiTape) dados.getSerializable(TURING_MACHINE_EXTRA);
            labelToPoint = (Map<String, MyPoint>) dados.getSerializable(LABEL_TO_POINT_EXTRA);
            word = (String) dados.getSerializable(WORD_EXTRA);
        }
    }
}
