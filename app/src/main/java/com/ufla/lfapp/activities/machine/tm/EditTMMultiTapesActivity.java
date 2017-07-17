package com.ufla.lfapp.activities.machine.tm;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.AppCompatActivityContext;
import com.ufla.lfapp.activities.machine.HistoricalGraphActivity;
import com.ufla.lfapp.activities.machine.pda.EditPushdownAutomatonActivity;
import com.ufla.lfapp.core.machine.MachineType;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.dotlang.DotLanguage;
import com.ufla.lfapp.core.machine.dotlang.Edge;
import com.ufla.lfapp.core.machine.dotlang.GraphAdapter;
import com.ufla.lfapp.core.machine.tm.var.TuringMachineMultiTape;
import com.ufla.lfapp.persistence.DbAcess;
import com.ufla.lfapp.utils.MyPoint;
import com.ufla.lfapp.utils.ObjectSerializerHelper;
import com.ufla.lfapp.views.machine.layout.EditTMMultiTapeLayout;
import com.ufla.lfapp.views.machine.transition.PDATransitionView;
import com.ufla.lfapp.views.machine.transition.TMMultiTrackTransitionView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.ufla.lfapp.activities.machine.fsa.ArrayAdapterGraph.GRAPH_ADAPTER_EXTRA;

public class EditTMMultiTapesActivity extends AppCompatActivityContext {

    public static final String TURING_MACHINE_MULTI_TAPE = "turingMachineMultiTape";
    public static final String TURING_MACHINE_POINTS_MULTI_TAPE = "turingMachinePointsMultiTape";

    private static final String TURING_MACHINE_EXTRA = "turingMachine";
    private static final String LABEL_TO_POINT_EXTRA = "labelToPoint";
    private static final String WORD_EXTRA = "word";

    private EditTMMultiTapeLayout editMachineLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editMachineLayout = new EditTMMultiTapeLayout(this);
        setContentView(editMachineLayout.getRootView());
        Intent intent = getIntent();
        boolean init = false;
        if (intent != null
                && intent.getSerializableExtra(TURING_MACHINE_MULTI_TAPE) != null) {
//            editMachineLayout.drawPushdownAutomaton((PushdownAutomatonExtend) intent
//                    .getSerializableExtra(TURING_MACHINE_ENUM));
        }  else if (intent != null
                && intent.getSerializableExtra(GRAPH_ADAPTER_EXTRA) != null) {
            GraphAdapter graphAdapter = (GraphAdapter) intent
                    .getSerializableExtra(GRAPH_ADAPTER_EXTRA);
            int numTapes = 2;
            if (graphAdapter.edgeList.size() > 0) {
                Edge edge = graphAdapter.edgeList.get(0);
                String label = edge.label;
                int lastInd = label.indexOf(']');
                label = label.substring(1, lastInd).trim();
                String[] params = PDATransitionView.clearArray
                        (TMMultiTrackTransitionView.arrayTrim(label.split("[,]")));
                numTapes = params.length;
            }
            editMachineLayout.setNumTapes(numTapes);
            editMachineLayout.drawGraph(graphAdapter);
            init = true;
        } else {
            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.remove(TURING_MACHINE_MULTI_TAPE);
//            editor.remove(TURING_MACHINE_MULTI_TAPE);
//            editor.apply();
            String pushdownAutomatonStr = preferences.getString(TURING_MACHINE_MULTI_TAPE, null);
            String labelToPointStr = preferences.getString(TURING_MACHINE_POINTS_MULTI_TAPE, null);
            if (pushdownAutomatonStr != null && labelToPointStr != null) {
                TuringMachineMultiTape turingMachine = (TuringMachineMultiTape)
                        ObjectSerializerHelper
                                .stringToObject(pushdownAutomatonStr);
                Map<String, MyPoint> labelToPoint = (Map<String, MyPoint>) ObjectSerializerHelper
                        .stringToObject(labelToPointStr);
                if (turingMachine != null && labelToPoint != null) {
                    editMachineLayout.setNumTapes(turingMachine.getNumTapes());
                    editMachineLayout.drawTMWithLabel(turingMachine, labelToPoint);
                    init = true;
                }
            }
        }
        if (!init) {
            setNumTapes();
        }
    }

    private void setNumTapes() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        final EditTMMultiTapeLayout editTMMultiTapeLayout = editMachineLayout;
        AlertDialog alertDialog = builder.setView(inflater.inflate(R.layout.dialog_num_tapes_tm, null))
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog f = (Dialog) dialog;
                        EditText et = (EditText) f.findViewById(R.id.numTapes);
                        try {
                            editTMMultiTapeLayout.setNumTapes(Integer.parseInt(
                                    et.getText().toString()));
                        } catch (Exception e) {
                            editTMMultiTapeLayout.setNumTapes(2);
                        }
                        dialog.cancel();
                    }
                })
                .create();
        alertDialog.show();
        EditText numTapesEditText= (EditText) alertDialog.findViewById(R.id.numTapes);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(numTapesEditText, InputMethodManager.SHOW_IMPLICIT);

    }

    private void saveData() {
        TuringMachineMultiTape turingMachine = editMachineLayout.getTuringMachine();
        Map<String, MyPoint> labelToPoint = editMachineLayout.getMapLabelToPoint();
        if (turingMachine == null || labelToPoint == null
                || turingMachine.getStates().size() == 0) {
            return;
        }
        Map<State, MyPoint> stateToPoint = new HashMap<>();
        for (String stateStr : labelToPoint.keySet()) {
            stateToPoint.put(turingMachine.getState(stateStr),
                    labelToPoint.get(stateStr));
        }
        new DbAcess(this).putMachineDotLanguage(new DotLanguage(turingMachine,
                stateToPoint));
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString(TURING_MACHINE_MULTI_TAPE, ObjectSerializerHelper
                .objectToString(turingMachine));
        editor.putString(TURING_MACHINE_POINTS_MULTI_TAPE, ObjectSerializerHelper
                .objectToString((Serializable) labelToPoint));
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveData();
    }


    @Override
    protected void onDestroy() {
        saveData();
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tm_multi_tape, menu);
        return true;
    }

    private boolean clear() {
        editMachineLayout.clear();
        setNumTapes();
        return true;
    }

    private boolean history() {
        Intent intent = new Intent(this, HistoricalGraphActivity.class);
        intent.putExtra(HistoricalGraphActivity.MACHINE_TYPE_SEL, MachineType.TM_MULTI_TAPE);
        startActivity(intent);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                //NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.clear:
                return clear();
            case R.id.history:
                return history();
            case R.id.verifyEntryTM:
                //Verify
                return verifyEntryTM();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean verifyEntryTM() {
        final TuringMachineMultiTape turingMachine = editMachineLayout.getTuringMachine();
        final Map<String, MyPoint> labelToPoint = editMachineLayout.getMapLabelToPoint();
        if (turingMachine != null) {
            if (turingMachine.getInitialState() == null) {
                Toast.makeText(EditTMMultiTapesActivity.this,
                        "Erro! MT Multi-fitas não possui estado inicial!",
                        Toast.LENGTH_LONG).show();
                return true;
            }
            if (turingMachine.getFinalStates().isEmpty()) {
                Toast.makeText(EditTMMultiTapesActivity.this,
                        "Erro! MT Multi-fitas reconhecedora não possui estado final!",
                        Toast.LENGTH_LONG).show();
                return true;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            builder.setView(inflater.inflate(R.layout.dialog_entry, null))
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Dialog f = (Dialog) dialog;
                            EditText etWord = (EditText) f.findViewById(R.id.word);
                            final String word = etWord.getText().toString();
                            Intent intent = new Intent(EditTMMultiTapesActivity.this,
                                    ProcessWordTMMultiTapeActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(TURING_MACHINE_EXTRA, turingMachine);
                            bundle.putSerializable(LABEL_TO_POINT_EXTRA, (Serializable) labelToPoint);
                            bundle.putSerializable(WORD_EXTRA, word);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            dialog.cancel();
//                                    try {
//                                        if (automatonB.processEntry(word)) {
//                                            Toast.makeText(EditFiniteStateAutomatonActivity.this,
//                                                    "Aceita a palavra '" + word + "'", Toast.LENGTH_LONG)
//                                                    .show();
//                                        } else {
//                                            Toast.makeText(EditFiniteStateAutomatonActivity.this,
//                                                    "Rejeita a palavra '" + word + "'", Toast.LENGTH_LONG)
//                                                    .show();
//                                        }
//                                    } catch (Exception e) {
//                                        Toast.makeText(EditFiniteStateAutomatonActivity.this,
//                                                e.getMessage(), Toast.LENGTH_LONG)
//                                                    .show();
//                                        e.printStackTrace();
//                                    }

                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .create()
                    .show();
        }
        return true;
    }

}
