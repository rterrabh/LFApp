package com.ufla.lfapp.activities.grammar.algorithms.cfgtopda;

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
import com.ufla.lfapp.activities.machine.pda.EditPushdownAutomatonActivity;
import com.ufla.lfapp.views.machine.layout.EditPDAutomatonLayout;
import com.ufla.lfapp.views.machine.layout.NonEditPDAutomatonLayout;
import com.ufla.lfapp.core.machine.pda.PushdownAutomatonExtend;

public class CFGToPDAActivity extends AppCompatActivityContext {

    public static final String PUSHDOW_AUTOMATON = "PushdownAutomatonExtend";

    private EditPDAutomatonLayout editGraphLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cfgto_pda);
        Intent intent = getIntent();
        if (intent == null || intent.getExtras() == null
                || intent.getExtras().getSerializable(PUSHDOW_AUTOMATON) == null) {
            Toast.makeText(this, R.string.exception_pda_not_found, Toast.LENGTH_LONG)
                    .show();
            return;
        }
        Bundle bundle = intent.getExtras();
        final PushdownAutomatonExtend pushdownAutomaton =  (PushdownAutomatonExtend)
                bundle.getSerializable(PUSHDOW_AUTOMATON);
        editGraphLayout = new NonEditPDAutomatonLayout(this);
        editGraphLayout.drawPushdownAutomaton(pushdownAutomaton);
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

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pushdownAutomaton == null) {
                    return;
                }
                Context context = CFGToPDAActivity.this;
                Intent intent = new Intent(context, EditPushdownAutomatonActivity.class);
                intent.putExtra(PUSHDOW_AUTOMATON, pushdownAutomaton);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
        findViewById(R.id.copyAutomaton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pushdownAutomaton == null) {
                    return;
                }
                Context context = CFGToPDAActivity.this;
                Intent intent = new Intent(context, EditPushdownAutomatonActivity.class);
                intent.putExtra(PUSHDOW_AUTOMATON, pushdownAutomaton);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
    }

}
