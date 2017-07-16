package com.ufla.lfapp.activities.machine.fsa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.AppCompatActivityContext;
import com.ufla.lfapp.views.graph.layout.EditGraphLayout;
import com.ufla.lfapp.views.graph.layout.NonEditGraphLayout;
import com.ufla.lfapp.persistence.DbAcess;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomatonGUI;

public class RegexToAutomataActivity extends AppCompatActivityContext {

    private static final String AUTOMATON_GUI_REGEX_EXTRA =
            "AutomatonGUIRegex";
    private static final String AUTOMATON_GUI_EDIT_EXTRA =
            "AutomatonGUIEdit";
    private static final String FINITE_STATE_AUTOMATON_EXTRA =
            "FiniteStateAutomaton";

    private EditGraphLayout editGraphLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regex_to_automata);
        Intent intent = getIntent();
        if (intent == null || intent.getExtras() == null
                || intent.getExtras().getSerializable(AUTOMATON_GUI_REGEX_EXTRA) == null) {
            Toast.makeText(this, R.string.exception_automaton_not_found, Toast.LENGTH_LONG)
                    .show();
            return;
        }
        Bundle bundle = intent.getExtras();
        final FiniteStateAutomatonGUI automatonGUIRegex =  (FiniteStateAutomatonGUI)
                bundle.getSerializable(AUTOMATON_GUI_REGEX_EXTRA);
        final FiniteStateAutomatonGUI automatonGUIEdit = (FiniteStateAutomatonGUI)
                bundle.getSerializable(AUTOMATON_GUI_EDIT_EXTRA);
        editGraphLayout = new NonEditGraphLayout(this);
        editGraphLayout.drawAutomaton(automatonGUIRegex);
        //editGraphLayout.removeSpaces();
        editGraphLayout.getRootView().setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        ConstraintLayout rootAutomataLayout = (ConstraintLayout) findViewById(R.id.automata);
        rootAutomataLayout.addView(editGraphLayout.getRootView());
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        editGraphLayout.resizeTo(size.x, size.y);

        findViewById(R.id.saveAutomaton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (automatonGUIRegex != null) {
                    new DbAcess(RegexToAutomataActivity.this).putAutomatonGUI(automatonGUIRegex);
                }
                Context context = RegexToAutomataActivity.this;
                Intent intent = new Intent(context, EditFiniteStateAutomatonActivity.class);
                intent.putExtra(FINITE_STATE_AUTOMATON_EXTRA, automatonGUIEdit);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (automatonGUIEdit == null) {
                    return;
                }
                Context context = RegexToAutomataActivity.this;
                Intent intent = new Intent(context, EditFiniteStateAutomatonActivity.class);
                intent.putExtra(FINITE_STATE_AUTOMATON_EXTRA, automatonGUIEdit);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
        findViewById(R.id.copyAutomaton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (automatonGUIRegex == null) {
                    return;
                }
                Context context = RegexToAutomataActivity.this;
                Intent intent = new Intent(context, EditFiniteStateAutomatonActivity.class);
                intent.putExtra(FINITE_STATE_AUTOMATON_EXTRA, automatonGUIRegex);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
    }



}
