package com.ufla.lfapp.activities.machine.fsa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v4.util.Pair;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.machine.pda.EditPushdownAutomatonActivity;
import com.ufla.lfapp.activities.machine.tm.EditTMEnumActivity;
import com.ufla.lfapp.activities.machine.tm.EditTMMultiTapesActivity;
import com.ufla.lfapp.activities.machine.tm.EditTMMultiTracksActivity;
import com.ufla.lfapp.activities.machine.tm.EditTuringMachineActivity;
import com.ufla.lfapp.core.machine.MachineType;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.dotlang.DotLanguage;
import com.ufla.lfapp.core.machine.dotlang.GraphAdapter;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomaton;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomatonGUI;
import com.ufla.lfapp.persistence.DbAcess;
import com.ufla.lfapp.utils.MyPoint;
import com.ufla.lfapp.utils.ResourcesContext;
import com.ufla.lfapp.views.graph.layout.EditGraphLayout;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by carlos on 15/07/17.
 */

public class ArrayAdapterGraph extends ArrayAdapter<GraphAdapter>  {


    private static final float sizeReferenceMin = 0.45f;
    private static final String FINITE_STATE_AUTOMATON_EXTRA = "FiniteStateAutomaton";
    private static final String FINITE_STATE_AUTOMATON_GUI_EXTRA =
            "FiniteStateAutomatonGUI";
    private static final String[] parameters =
            ResourcesContext.getString(
                    R.string.array_adapter_automata_parameters
            ).split("#");

    public ArrayAdapterGraph(Context context, List<GraphAdapter> graphAdapterList) {
        super(context, R.layout.automaton_item_view_text, graphAdapterList);
    }

    public View getView(int position, final View convertView, ViewGroup parent) {
        final GraphAdapter graphAdapter = getItem(position);
        LayoutInflater buckysInflater = LayoutInflater.from(getContext());
        View view = buckysInflater.inflate(R.layout.automaton_item_view_graph, parent,
                false);
        EditText editTextLabel;
        editTextLabel = (EditText) view.findViewById(R.id.editTextLabel);
        editTextLabel.setText(graphAdapter.dotLanguage.getLabel());
        editTextLabel.setEnabled(true);
        editTextLabel.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                graphAdapter.dotLanguage.setLabel(v.getText().toString());
                return false;
            }
        });
        view.findViewById(R.id.copyAutomaton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copy(graphAdapter);
            }
        });
        view.findViewById(R.id.deleteAutomaton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapterGraph.this.remove(graphAdapter);
                new DbAcess(getContext()).deleteMachineDotLanguage(graphAdapter.dotLanguage.getId());
            }
        });
        ((TextView) view.findViewById(R.id.creationDate)).setText(parameters[0] +
                new SimpleDateFormat(parameters[1]).format(graphAdapter.dotLanguage.getCreationDate()));
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
                        Intent intent = new Intent(context, ItemGraphActivity.class );
                        intent.putExtra(ItemGraphActivity.GRAPH_ADAPTER_EXTRA, graphAdapter);
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
        editGraphLayout.drawGraph(graphAdapter);
        editGraphLayout.removeSpaces(minWidth, minHeight);
        layoutForGraph.addView(editGraphLayout.getRootView());
        View rootView = editGraphLayout.getRootView();
        ViewGroup.LayoutParams params = rootView.getLayoutParams();
        params.height = minHeight;
        rootView.setLayoutParams(params);
        view.invalidate();
        editGraphLayout.invalidate();
        return view;
    }

    public void copyFSA(DotLanguage dotLanguage) {
        Context context = getContext();
        Intent intent = new Intent(context, EditFiniteStateAutomatonActivity.class);
        Pair<FiniteStateAutomaton, Map<State, MyPoint>> pairFSA = dotLanguage.toFSA();
        intent.putExtra(FINITE_STATE_AUTOMATON_EXTRA,
                new FiniteStateAutomatonGUI(pairFSA.first, pairFSA.second));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public static final String GRAPH_ADAPTER_EXTRA = "graphAdapter";

    public void copyGraph(GraphAdapter graphAdapter, Class activity) {
        Context context = getContext();
        Intent intent = new Intent(context, activity);
        intent.putExtra(GRAPH_ADAPTER_EXTRA, graphAdapter);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public void copy(GraphAdapter graphAdapter) {
        Context context = getContext();
        if (graphAdapter.dotLanguage.getMachineType() == MachineType.FSA) {
            copyFSA(graphAdapter.dotLanguage);
        }
        switch (graphAdapter.dotLanguage.getMachineType()) {
            case FSA:
                copyFSA(graphAdapter.dotLanguage);
                return;
            case PDA:
            case PDA_EXT:
                copyGraph(graphAdapter, EditPushdownAutomatonActivity.class);
                return;
            case TM:
                copyGraph(graphAdapter, EditTuringMachineActivity.class);
                return;
            case TM_MULTI_TAPE:
                copyGraph(graphAdapter, EditTMMultiTapesActivity.class);
                return;
            case TM_MULTI_TRACK:
                copyGraph(graphAdapter, EditTMMultiTracksActivity.class);
                return;
            case TM_ENUM:
                copyGraph(graphAdapter, EditTMEnumActivity.class);
                return;
        }

    }

}
