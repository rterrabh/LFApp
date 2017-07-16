package com.ufla.lfapp.views.machine.transition;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ufla.lfapp.R;
import com.ufla.lfapp.core.machine.Machine;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.tm.TMMove;
import com.ufla.lfapp.core.machine.tm.TMTransitionFunction;
import com.ufla.lfapp.utils.ResourcesContext;
import com.ufla.lfapp.views.graph.EdgeView;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by carlos on 3/1/17.
 */

public class TMTransitionView extends EdgeView {

    private final static String INITIAL_LABEL = ResourcesContext.getString(R.string.initial_label_tm);

    @Override
    protected String[] getLabelLines() {
        return TMMultiTrackTransitionView.arrayTrim(label.split("[,\n]"));
    }

    @Override
    public void setInitialLabel() {
        label = INITIAL_LABEL;
    }

    public TMTransitionView(Context context, boolean fastEdition) {
        super(context, false);
    }

    public TMTransitionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TMTransitionView(Context context) {
        super(context);
    }

    class IntegerWrapper {
        int value;
    }

    public boolean verifyTransition(String labelTrans) {
        String[] transitions = TMMultiTrackTransitionView.arrayTrim(labelTrans.split("[,\n]"));
        final Set<String> directions = new HashSet<>(
                Arrays.asList(TMMultiTapeTransitionView.getDirections()));
        for (int i = 0; i < transitions.length; i++) {
            String[] transitonTokens = transitions[i].split("[ /]");
            if (transitonTokens.length != 3
                    || !directions.contains(transitonTokens[2])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onLongPressAction(MotionEvent e) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE );
        final LinearLayout dialogEdge = (LinearLayout) inflater.inflate(
                R.layout.dialog_label_tm_transition_text, null);
        final EditText editLabel = (EditText) dialogEdge.findViewById(
                R.id.label);
        editLabel.setText(label);
        editLabel.setEnabled(true);
        editLabel.setSelectAllOnFocus(true);

        final AlertDialog alertDialog = builder.setView(dialogEdge)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String labelAux = editLabel.getText().toString();
                        if (!verifyTransition(labelAux)) {
                            Toast.makeText(TMTransitionView.this.getContext(),
                                    R.string.exception_transition_def,
                                    Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            setLabel(labelAux);
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
        alertDialog.show();
    }

    /*
    public void onLongPressAction(MotionEvent e) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE );
        final LinearLayout dialogEdge = (LinearLayout) inflater.inflate(
                R.layout.dialog_label_tm_transition, null);
        final EditText symbolReadTM = (EditText) dialogEdge.findViewById(
                R.id.symbol_read_tm_transition);
        final EditText symbolWriteTM = (EditText) dialogEdge.findViewById(
                R.id.symbol_write_tm_transition);
        final RadioGroup directionTM = (RadioGroup) dialogEdge.findViewById(
                R.id.direction_tm_transition);
        final IntegerWrapper idSelectedDirection = new IntegerWrapper();
        idSelectedDirection.value = -1;
        OnClickListener radioGroupListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Is the button now checked?
                boolean checked = ((RadioButton) v).isChecked();

                // Check which radio button was clicked
                switch(v.getId()) {
                    case R.id.radio_right:
                        if (checked)
                            idSelectedDirection.value = R.id.radio_right;
                            break;
                    case R.id.radio_left:
                        if (checked)
                            idSelectedDirection.value = R.id.radio_left;
                            break;
                    case R.id.radio_static:
                        if (checked)
                            idSelectedDirection.value = R.id.radio_static;
                        break;
                }
            }
        };
        int childs = directionTM.getChildCount();
        for (int i = 0; i < childs; i++) {
            directionTM.getChildAt(i).setOnClickListener(radioGroupListener);
        }
        String labelTokens[] = label.split("[ /]");
        symbolReadTM.setText(labelTokens[0]);
        symbolReadTM.setEnabled(true);
        symbolReadTM.setSelectAllOnFocus(true);
        symbolWriteTM.setText(labelTokens[1]);
        symbolWriteTM.setEnabled(true);
        symbolWriteTM.setSelectAllOnFocus(true);
        switch (labelTokens[2]) {
            case "D":
            case "R":
                idSelectedDirection.value =  R.id.radio_right;
                ((RadioButton) dialogEdge.findViewById(
                        R.id.radio_right)).setChecked(true);
                break;
            case "E":
            case "L":
                idSelectedDirection.value =  R.id.radio_left;
                ((RadioButton) dialogEdge.findViewById(
                        R.id.radio_left)).setChecked(true);
                break;
            case "S":
                idSelectedDirection.value =  R.id.radio_static;
                ((RadioButton) dialogEdge.findViewById(
                        R.id.radio_static)).setChecked(true);
                break;
        }
        directionTM.setEnabled(true);
        final AlertDialog alertDialog = builder.setView(dialogEdge)
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String labelAux = symbolReadTM.getText().toString();
                        String labelFinal = labelAux.isEmpty() ? "B" : labelAux;
                        labelFinal += "/";
                        labelAux = symbolWriteTM.getText().toString();
                        labelFinal += labelAux.isEmpty() ? "B" : labelAux;
                        labelFinal += " ";
                        labelFinal += ((RadioButton) dialogEdge.findViewById(
                                idSelectedDirection.value)).getText();
                        setLabel(labelFinal);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        //  Your code when user clicked on Cancel
                    }
                })
                .create();
        symbolReadTM.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    alertDialog.getWindow().setSoftInputMode(WindowManager
                            .LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        alertDialog.show();
        int idButtonPositive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).getId();
        directionTM.setNextFocusDownId(idButtonPositive);
        directionTM.setNextFocusForwardId(idButtonPositive);
    }*/

    public Set<TMTransitionFunction> getTransitionFuctionsTM(Machine machine) {
        State currentState = machine.getState(vertices.first.getLabel());
        State futureState = machine.getState(vertices.second.getLabel());
        Set<TMTransitionFunction> transitionFunctions = new HashSet<>();
        String[] transitions  = PDATransitionView.clearArray
                (TMMultiTrackTransitionView.arrayTrim(label.split("[,\n]")));
        for (String transition : transitions) {
            String transitionTokens[] = transition.trim().split("[ /]");
            transitionFunctions.add(new TMTransitionFunction(currentState, transitionTokens[0],
                    futureState, transitionTokens[1], TMMove.getInstance(transitionTokens[2])));
        }
        return transitionFunctions;
    }

}
