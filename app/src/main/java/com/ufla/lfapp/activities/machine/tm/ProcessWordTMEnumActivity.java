package com.ufla.lfapp.activities.machine.tm;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.AppCompatActivityContext;
import com.ufla.lfapp.activities.machine.fsa.EditFiniteStateAutomatonActivity;
import com.ufla.lfapp.core.machine.tm.var.TuringMachineMultiTape;
import com.ufla.lfapp.core.machine.tm.var.TMMultiTapeSimulator;
import com.ufla.lfapp.utils.MyPoint;
import com.ufla.lfapp.views.machine.configuration.MultiTapeView;
import com.ufla.lfapp.views.machine.layout.EditTMMultiTapeLayout;

import java.util.Map;

public class ProcessWordTMEnumActivity extends AppCompatActivityContext {

    private static final String TURING_MACHINE_EXTRA = "turingMachine";
    private static final String LABEL_TO_POINT_EXTRA = "labelToPoint";
    private static final String WORD_EXTRA = "word";

    private TuringMachineMultiTape turingMachine;
    private Map<String, MyPoint> labelToPoint;
    private String word;
    private TMMultiTapeSimulator simulator;
    private LinearLayout configurations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_word_tmenum);
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

        configurations = (LinearLayout) findViewById(R.id.configurations);
        simulator = new TMMultiTapeSimulator(turingMachine);
        nextWordAction();
    }

    public void readIntent() {
        Intent intent = getIntent();
        if (intent != null
                && intent.getExtras() != null
                && intent.getExtras().getSerializable(TURING_MACHINE_EXTRA) != null
                && intent.getExtras().getSerializable(LABEL_TO_POINT_EXTRA) != null) {
            Bundle dados = intent.getExtras();
            turingMachine = (TuringMachineMultiTape) dados.getSerializable(TURING_MACHINE_EXTRA);
            labelToPoint = (Map<String, MyPoint>) dados.getSerializable(LABEL_TO_POINT_EXTRA);
        }
    }

    public void nextWordAction() {
        TMMultiTapeSimulator.Configuration configuration = simulator.nextWord();
        if (simulator.contConfiguration == 1000) {
            Toast.makeText(ProcessWordTMEnumActivity.this,
                    "Alerta! Foram processadas 1000 configurações e não foi escrito o caracter " +
                            "separator de palavras (\"#\") na fita de saída!\nPode ser que esta " +
                            "MT Enumeradora não está definida corretamente.",
                    Toast.LENGTH_LONG).show();
        }
        MultiTapeView tapeView = new MultiTapeView(this);
        tapeView.setTapes(configuration.getTapes());
        tapeView.setIndex(configuration.getIndex());
        tapeView.setState(configuration.getState());
        tapeView.setInitialState(configuration.isInitialState());
        tapeView.setFinalState(configuration.isFinalState());
        tapeView.finalize();
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this);
        horizontalScrollView.addView(tapeView);
        configurations.addView(horizontalScrollView);
    }

    public void nextWord(View view) {
        nextWordAction();
    }

}
