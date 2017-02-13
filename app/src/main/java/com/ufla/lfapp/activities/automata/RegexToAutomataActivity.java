package com.ufla.lfapp.activities.automata;

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
import com.ufla.lfapp.activities.automata.graph.layout.EditGraphLayout;
import com.ufla.lfapp.activities.automata.graph.layout.NonEditGraphLayout;
import com.ufla.lfapp.persistence.DbAcess;
import com.ufla.lfapp.vo.machine.AutomatonGUI;

public class RegexToAutomataActivity extends AppCompatActivity {

    private EditGraphLayout editGraphLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regex_to_automata);
        Intent intent = getIntent();
        if (intent == null || intent.getExtras() == null
                || intent.getExtras().getSerializable("AutomatonGUIRegex") == null) {
            Toast.makeText(this, "Erro! Autômato não foi encontrado!", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        Bundle bundle = intent.getExtras();
        final AutomatonGUI automatonGUIRegex =  (AutomatonGUI)
                bundle.getSerializable("AutomatonGUIRegex");
        final AutomatonGUI automatonGUIEdit = (AutomatonGUI)
                bundle.getSerializable("AutomatonGUIEdit");
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
                Intent intent = new Intent(context, EditAutomataActivity.class);
                intent.putExtra("Automaton", automatonGUIEdit);
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
                Intent intent = new Intent(context, EditAutomataActivity.class);
                intent.putExtra("Automaton", automatonGUIEdit);
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
                Intent intent = new Intent(context, EditAutomataActivity.class);
                intent.putExtra("Automaton", automatonGUIRegex);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
    }



}
