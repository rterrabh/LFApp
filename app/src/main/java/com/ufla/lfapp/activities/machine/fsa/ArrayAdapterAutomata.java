package com.ufla.lfapp.activities.machine.fsa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.GestureDetector;
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
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomatonGUI;
import com.ufla.lfapp.utils.ResourcesContext;
import com.ufla.lfapp.views.graph.layout.EditGraphLayout;
import com.ufla.lfapp.persistence.DbAcess;
import com.ufla.lfapp.core.machine.State;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by carlos on 12/7/16.
 */

public class ArrayAdapterAutomata extends ArrayAdapter<FiniteStateAutomatonGUI> {


    private static final String CLEAN_BUTTON = "clean";
    private static final float sizeReferenceMin = 0.45f;
    private static final String FINITE_STATE_AUTOMATON_EXTRA = "FiniteStateAutomaton";
    private static final String FINITE_STATE_AUTOMATON_GUI_EXTRA =
            "FiniteStateAutomatonGUI";
    private static final String[] parameters =
            ResourcesContext.getString(
                    R.string.array_adapter_automata_parameters
            ).split("#");

    public ArrayAdapterAutomata(Context context, List<FiniteStateAutomatonGUI> automatonGUIs) {
        super(context, R.layout.automaton_item_view_text, automatonGUIs);
    }

    private View getViewText(int position, View convertView, ViewGroup parent) {
        final FiniteStateAutomatonGUI singleAutomatonItem = getItem(position);
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
                Intent intent = new Intent(context, EditFiniteStateAutomatonActivity.class);
                intent.putExtra(FINITE_STATE_AUTOMATON_EXTRA, singleAutomatonItem);
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

        ((TextView) view.findViewById(R.id.creationDate)).setText(parameters[0] +
                new SimpleDateFormat(parameters[1]).format(singleAutomatonItem.getCreationDate()));
        ((TextView) view.findViewById(R.id.states)).setText(parameters[2] +
                singleAutomatonItem.getStates());
        ((TextView) view.findViewById(R.id.alphabet)).setText(parameters[3] +
                singleAutomatonItem.getAlphabet());
        ((TextView) view.findViewById(R.id.initialState)).setText(parameters[4] +
                singleAutomatonItem.getInitialState());
        ((TextView) view.findViewById(R.id.finalStates)).setText(parameters[5] +
                singleAutomatonItem.getFinalStates());
        TableLayout transictionTable = (TableLayout) view.findViewById(R.id.transictionTable);
        List<State> states = new ArrayList<>(singleAutomatonItem.getStates());
        Collections.sort(states);
        TableRow tr = new TableRow(getContext());
        tr.setLayoutParams(new LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        tr.addView(new TextView(getContext()));
        for (State state : states) {
            TextView textView = new TextView(getContext());
            textView.setText(state.getName());
            textView.setPadding(5, 5, 5, 5);
            textView.setBackgroundResource(R.drawable.cell_shape);
            tr.addView(textView);
        }
        transictionTable.addView(tr);
        for (int i = 0; i < states.size(); i++) {
            tr = new TableRow(getContext());
            tr.setLayoutParams(new LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            TextView textView = new TextView(getContext());
            textView.setText(states.get(i).getName());
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
        final FiniteStateAutomatonGUI singleAutomatonItem = getItem(position);
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
                Intent intent = new Intent(context, EditFiniteStateAutomatonActivity.class);
                intent.putExtra(FINITE_STATE_AUTOMATON_EXTRA, singleAutomatonItem);
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
        ((TextView) view.findViewById(R.id.creationDate)).setText(parameters[0] +
                new SimpleDateFormat(parameters[1]).format(singleAutomatonItem.getCreationDate()));
        LinearLayout layoutForGraph = (LinearLayout) view.findViewById(R.id.layoutForGraph);
        EditGraphLayout editGraphLayout = null;
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int minWidth = size.x;
        int minHeight = size.y / 4;
        final GestureDetector gestureDetectorItem = new GestureDetector(getContext(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        Context context = getContext();
                        Intent intent = new Intent(context, ItemAutomatonActivity.class );
                        intent.putExtra(FINITE_STATE_AUTOMATON_GUI_EXTRA, singleAutomatonItem);
                        context.startActivity(intent);
                        return true;
                    }
                });
        editGraphLayout = new EditGraphLayout(getContext(), sizeReferenceMin) {
            @Override
            public boolean onTouchEvent(MotionEvent e) {
                return gestureDetectorItem.onTouchEvent(e);
            }
        };
        editGraphLayout.drawAutomaton(singleAutomatonItem);
        editGraphLayout.removeSpaces(minWidth, minHeight);
        layoutForGraph.addView(editGraphLayout.getRootView());
        View rootView = editGraphLayout.getRootView();
        LayoutParams params = rootView.getLayoutParams();
        params.height = minHeight;
        rootView.setLayoutParams(params);
        view.invalidate();
        editGraphLayout.invalidate();
        return view;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getViewGraph(position, convertView, parent);
    }

}
