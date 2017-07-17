package com.ufla.lfapp.activities.machine.tm;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.AppCompatActivityContext;
import com.ufla.lfapp.activities.machine.HistoricalGraphActivity;
import com.ufla.lfapp.core.machine.MachineType;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.dotlang.DotLanguage;
import com.ufla.lfapp.core.machine.dotlang.GraphAdapter;
import com.ufla.lfapp.core.machine.tm.var.TuringMachineMultiTape;
import com.ufla.lfapp.persistence.DbAcess;
import com.ufla.lfapp.utils.MyPoint;
import com.ufla.lfapp.utils.ObjectSerializerHelper;
import com.ufla.lfapp.views.machine.layout.EditTMMultiTapeLayout;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.ufla.lfapp.activities.machine.fsa.ArrayAdapterGraph.GRAPH_ADAPTER_EXTRA;

public class EditTMEnumActivity extends AppCompatActivityContext {

    public static final String TURING_MACHINE_ENUM = "turingMachineEnum";
    public static final String TURING_MACHINE_POINTS_ENUM = "turingMachinePointsEnum";

    private static final String TURING_MACHINE_EXTRA = "turingMachine";
    private static final String LABEL_TO_POINT_EXTRA = "labelToPoint";
    private static final String WORD_EXTRA = "word";
    private static final int  NUM_TAPES = 2;

    private EditTMMultiTapeLayout editMachineLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editMachineLayout = new EditTMMultiTapeLayout(this);
        setContentView(editMachineLayout.getRootView());
        editMachineLayout.setNumTapes(NUM_TAPES);
        Intent intent = getIntent();
        if (intent != null
                && intent.getSerializableExtra(TURING_MACHINE_ENUM) != null) {
//            editMachineLayout.drawPushdownAutomaton((PushdownAutomatonExtend) intent
//                    .getSerializableExtra(TURING_MACHINE_ENUM));
        }  else if (intent != null
                && intent.getSerializableExtra(GRAPH_ADAPTER_EXTRA) != null) {
            editMachineLayout.drawGraph((GraphAdapter) intent
                    .getSerializableExtra(GRAPH_ADAPTER_EXTRA));
        } else {
            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.remove(TURING_MACHINE_ENUM);
//            editor.remove(TURING_MACHINE_POINTS_ENUM);
//            editor.apply();
            String pushdownAutomatonStr = preferences.getString(TURING_MACHINE_ENUM, null);
            String labelToPointStr = preferences.getString(TURING_MACHINE_POINTS_ENUM, null);
            if (pushdownAutomatonStr != null && labelToPointStr != null) {
                TuringMachineMultiTape turingMachine = (TuringMachineMultiTape)
                        ObjectSerializerHelper
                                .stringToObject(pushdownAutomatonStr);
                Map<String, MyPoint> labelToPoint = (Map<String, MyPoint>) ObjectSerializerHelper
                        .stringToObject(labelToPointStr);
                if (turingMachine != null && labelToPoint != null) {
                    editMachineLayout.drawTMWithLabel(turingMachine, labelToPoint);
                }
            }
        }
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
        DotLanguage dotLanguage = new DotLanguage(turingMachine,
                stateToPoint);
        dotLanguage.setMachineType(MachineType.TM_ENUM);
        new DbAcess(this).putMachineDotLanguage(dotLanguage);
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString(TURING_MACHINE_ENUM, ObjectSerializerHelper
                .objectToString(turingMachine));
        editor.putString(TURING_MACHINE_POINTS_ENUM, ObjectSerializerHelper
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
        getMenuInflater().inflate(R.menu.menu_tm_enum, menu);
        return true;
    }

    private boolean clear() {
        editMachineLayout.clear();
        return true;
    }

    private boolean history() {
        Intent intent = new Intent(this, HistoricalGraphActivity.class);
        intent.putExtra(HistoricalGraphActivity.MACHINE_TYPE_SEL, MachineType.TM_ENUM);
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
                verifyEntryTM();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void verifyEntryTM() {
        final TuringMachineMultiTape turingMachine = editMachineLayout.getTuringMachine();
        final Map<String, MyPoint> labelToPoint = editMachineLayout.getMapLabelToPoint();
        if (turingMachine == null || labelToPoint == null) {
            Toast.makeText(EditTMEnumActivity.this,
                    "Erro! Máquina incorreta!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (turingMachine.getInitialState() == null) {
            Toast.makeText(EditTMEnumActivity.this,
                    "Erro! Estado inicial indefinido!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(EditTMEnumActivity.this,
                ProcessWordTMEnumActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(TURING_MACHINE_EXTRA, turingMachine);
        bundle.putSerializable(LABEL_TO_POINT_EXTRA, (Serializable) labelToPoint);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
