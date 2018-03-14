package com.ufla.lfapp.views.machine.transition;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ufla.lfapp.R;
import com.ufla.lfapp.core.machine.Machine;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.fsa.FSATransitionFunction;
import com.ufla.lfapp.core.machine.pda.PDATransitionFunction;
import com.ufla.lfapp.utils.Symbols;
import com.ufla.lfapp.views.graph.EdgeView;
import com.ufla.lfapp.views.graph.layout.EditGraphLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by carlos on 3/1/17.
 */

public class FSATransitionView extends EdgeView {

    private final static String INITIAL_LABEL = Symbols.LAMBDA;

    @Override
    protected String[] getLabelLines() {
        String[] labelLines = TMMultiTrackTransitionView.arrayTrim(label.split("[,\n]"));
        if (labelLines.length == 0) {
            setInitialLabel();
            return  TMMultiTrackTransitionView.arrayTrim(label.split("[,\n]"));
        }
        return labelLines;
    }

    @Override
    public void setLabel(String label) {
        Set<String> lastAlphabet = new HashSet<>(Arrays.asList(getLabelLines()));
        super.setLabel(label);
        String[] alphabet = getLabelLines();
        List<String> listAlphabet = new ArrayList<>();
        for (int i = 0; i < alphabet.length-1; i++) {
            if (!lastAlphabet.contains(alphabet[i])) {
                listAlphabet.add(alphabet[i]);
            }
        }
        listAlphabet.add(alphabet[alphabet.length-1]);
        EditGraphLayout editGraphLayout = getParentEditGraphLayout();
        editGraphLayout.updateAlphabet(listAlphabet);
    }

    @Override
    public void setInitialLabel() {
        label = INITIAL_LABEL;
    }

    public FSATransitionView(Context context, boolean fastEdition) {
        super(context, fastEdition);
    }

    public FSATransitionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FSATransitionView(Context context) {
        super(context);
    }

    @Override
    public void onLongPressAction(MotionEvent e) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE );
        final LinearLayout dialogEdge = (LinearLayout) inflater.inflate(R.layout.dialog_label_fsa_transition,
                null);
        final EditText labelEdge = (EditText) dialogEdge.findViewById(R.id.label_fsa_transition);
        labelEdge.setText(label);
        labelEdge.setEnabled(true);
        labelEdge.setSelectAllOnFocus(true);
        final AlertDialog alertDialog = builder.setView(dialogEdge)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        setLabel(labelEdge.getText().toString());
                        if (label.isEmpty()) {
                            setLabel(EMPTY_LABEL);
                        }
                        dialog.cancel();

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        //  Your code when user clicked on Cancel
                    }
                })
                .create();
        labelEdge.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    alertDialog.getWindow().setSoftInputMode(WindowManager
                            .LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        alertDialog.show();
    }

    public Set<FSATransitionFunction> getTransitionFuctionsFSA(Machine machine) {
        State currentState = machine.getState(vertices.first.getLabel());
        State futureState = machine.getState(vertices.second.getLabel());
        Set<FSATransitionFunction> transitionFunctions = new HashSet<>();
        String[] transitions  = PDATransitionView.clearArray
                (TMMultiTrackTransitionView.arrayTrim(label.split("[,\n]")));
        for (String symbol : transitions) {
            transitionFunctions.add(new FSATransitionFunction(currentState, symbol,
                    futureState));
        }
        return transitionFunctions;
    }

}
