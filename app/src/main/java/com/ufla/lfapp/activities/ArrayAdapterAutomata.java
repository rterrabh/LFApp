package com.ufla.lfapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.graph.layout.EditGraphLayout;
import com.ufla.lfapp.persistence.DbAcess;
import com.ufla.lfapp.vo.machine.AutomatonGUI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by carlos on 12/7/16.
 */

public class ArrayAdapterAutomata extends ArrayAdapter<AutomatonGUI> {

    private static String CLEAN_BUTTON = "clean";
    public static final int GRAPH_STYLE_FIXER = 0;
    public static final int GRAPH_STYLE_SCROLLABLE = 1;
    public static int GRAPH_STYLE = GRAPH_STYLE_SCROLLABLE;
    private static float sizeReferenceMin = 0.65f;

    public ArrayAdapterAutomata(Context context, List<AutomatonGUI> automatonGUIs) {
        super(context, R.layout.automaton_item_view_text, automatonGUIs);
    }

    private View getViewText(int position, View convertView, ViewGroup parent) {
        final AutomatonGUI singleAutomatonItem = getItem(position);
        LayoutInflater buckysInflater = LayoutInflater.from(getContext());
        View view = buckysInflater.inflate(R.layout.automaton_item_view_text, parent,
                false);
        EditText editTextLabel;
        editTextLabel = (EditText) view.findViewById(R.id.editTextLabel);
        editTextLabel.setText(singleAutomatonItem.getLabel());
        editTextLabel.setEnabled(true);
        editTextLabel.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                singleAutomatonItem.setLabel(v.getText().toString());
                return false;
            }
        });
        view.findViewById(R.id.copyAutomaton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getContext();
                Intent intent = new Intent(context, EditAutomataActivity.class);
                intent.putExtra("Automaton", singleAutomatonItem);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
        view.findViewById(R.id.deleteAutomaton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapterAutomata.this.remove(singleAutomatonItem);
                new DbAcess(getContext()).deleteMachineDotLanguage(singleAutomatonItem.getId());
            }
        });
        ((TextView) view.findViewById(R.id.creationDate)).setText("Criado em: " +
                new SimpleDateFormat("dd/MM/yyyy").format(singleAutomatonItem.getCreationDate()));
        ((TextView) view.findViewById(R.id.states)).setText("Conjunto de estados: " +
                singleAutomatonItem.getStates());
        ((TextView) view.findViewById(R.id.alphabet)).setText("Alfabeto: " +
                singleAutomatonItem.getAlphabet());
        ((TextView) view.findViewById(R.id.initialState)).setText("Estado inicial: " +
                singleAutomatonItem.getInitialState());
        ((TextView) view.findViewById(R.id.finalStates)).setText("Conjunto dos estados finais: " +
                singleAutomatonItem.getFinalStates());
        TableLayout transictionTable = (TableLayout) view.findViewById(R.id.transictionTable);
        List<String> states = new ArrayList<>(singleAutomatonItem.getStates());
        Collections.sort(states);
        TableRow tr = new TableRow(getContext());
        tr.setLayoutParams(new LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        tr.addView(new TextView(getContext()));
        for (String state : states) {
            TextView textView = new TextView(getContext());
            textView.setText(state);
            textView.setPadding(5, 5, 5, 5);
            textView.setBackgroundResource(R.drawable.cell_shape);
            tr.addView(textView);
        }
        transictionTable.addView(tr);
        for (int i = 0; i < states.size(); i++) {
            tr = new TableRow(getContext());
            tr.setLayoutParams(new LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            TextView textView = new TextView(getContext());
            textView.setText(states.get(i));
            textView.setPadding(5, 5, 5, 5);
            textView.setBackgroundResource(R.drawable.cell_shape);
            tr.addView(textView);
            for (int j = 0; j < states.size(); j++) {
                textView = new TextView(getContext());
                textView.setText(singleAutomatonItem.getSymbols(states.get(i), states.get(j))
                        .toString());
                textView.setPadding(5, 5, 5, 5);
                textView.setBackgroundResource(R.drawable.cell_shape);
                tr.addView(textView);
            }
            transictionTable.addView(tr);
        }

        return view;
    }

    private View getViewGraph(int position, View convertView, ViewGroup parent) {
        final AutomatonGUI singleAutomatonItem = getItem(position);
        LayoutInflater buckysInflater = LayoutInflater.from(getContext());
        View view = buckysInflater.inflate(R.layout.automaton_item_view_graph, parent,
                false);
        EditText editTextLabel;
        editTextLabel = (EditText) view.findViewById(R.id.editTextLabel);
        editTextLabel.setText(singleAutomatonItem.getLabel());
        editTextLabel.setEnabled(true);
        editTextLabel.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                singleAutomatonItem.setLabel(v.getText().toString());
                return false;
            }
        });
        view.findViewById(R.id.copyAutomaton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getContext();
                Intent intent = new Intent(context, EditAutomataActivity.class);
                intent.putExtra("Automaton", singleAutomatonItem);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
        view.findViewById(R.id.deleteAutomaton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapterAutomata.this.remove(singleAutomatonItem);
                new DbAcess(getContext()).deleteMachineDotLanguage(singleAutomatonItem.getId());
            }
        });
        ((TextView) view.findViewById(R.id.creationDate)).setText("Criado em: " +
                new SimpleDateFormat("dd/MM/yyyy").format(singleAutomatonItem.getCreationDate()));
        LinearLayout layoutForGraph = (LinearLayout) view.findViewById(R.id.layoutForGraph);
        EditGraphLayout editGraphLayout = null;
        if (GRAPH_STYLE == GRAPH_STYLE_FIXER) {
            Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y / 2;
            editGraphLayout = new EditGraphLayout(getContext());
            editGraphLayout.drawAutomaton(singleAutomatonItem);
            editGraphLayout.removeSpaces();
            int widthOfLayout = editGraphLayout.getWidthLayout();
            int heightOfLayout = editGraphLayout.getHeightLayout();
            float sizeReference = Math.min((float) width / widthOfLayout,
                    (float) height / heightOfLayout);
            sizeReference = Math.min(sizeReferenceMin, sizeReference);
            editGraphLayout = new EditGraphLayout(getContext(), sizeReference) {
                @Override
                public boolean onTouchEvent(MotionEvent e) {
                    return false;
                }
            };
        } else if (GRAPH_STYLE == GRAPH_STYLE_SCROLLABLE) {
            editGraphLayout = new EditGraphLayout(getContext(), sizeReferenceMin) {
                @Override
                public boolean onTouchEvent(MotionEvent e) {
                    return false;
                }
            };
        }
        editGraphLayout.drawAutomaton(singleAutomatonItem);
        editGraphLayout.removeSpaces();
        layoutForGraph.addView(editGraphLayout.getRootView());
        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getViewGraph(position, convertView, parent);
    }

}
