package com.ufla.lfapp.activities.machine.pda;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.AppCompatActivityContext;
import com.ufla.lfapp.activities.grammar.GrammarActivity;
import com.ufla.lfapp.activities.machine.HistoricalGraphActivity;
import com.ufla.lfapp.activities.machine.fsa.EditFiniteStateAutomatonActivity;
import com.ufla.lfapp.activities.machine.pda.pdatocfg.PDAToCFGStage1Activity;
import com.ufla.lfapp.core.grammar.Grammar;
import com.ufla.lfapp.core.machine.MachineType;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.dotlang.DotLanguage;
import com.ufla.lfapp.core.machine.dotlang.GraphAdapter;
import com.ufla.lfapp.core.machine.pda.PDAToGrammar;
import com.ufla.lfapp.core.machine.pda.PushdownAutomaton;
import com.ufla.lfapp.core.machine.pda.PushdownAutomatonExtend;
import com.ufla.lfapp.persistence.DbAcess;
import com.ufla.lfapp.utils.MyPoint;
import com.ufla.lfapp.utils.ObjectSerializerHelper;
import com.ufla.lfapp.utils.UtilActivities;
import com.ufla.lfapp.views.machine.layout.EditPDAutomatonLayout;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.ufla.lfapp.activities.machine.fsa.ArrayAdapterGraph.GRAPH_ADAPTER_EXTRA;

public class EditPushdownAutomatonActivity extends AppCompatActivityContext {

    public static final String PUSHDOW_AUTOMATON_EXTEND = "PushdownAutomatonExtend";
    public static final String EDIT_PUSHDOW_AUTOMATON = "EditPushdownAutomaton";
    public static final String EDIT_PUSHDOW_AUTOMATON_POINTS = "EditPushdownAutomatonPoints";

    private static final String GRAMMAR_EXTRA = "grammar";
    private static final String PUSHDOWN_AUTOMATON_EXTRA = "pushdownAutomaton";
    private static final String PDA_AUTOMATON_EXTRA = "PDAAutomaton";
    private static final String LABEL_TO_POINT_EXTRA = "labelToPoint";
    private static final String WORD_EXTRA = "word";


    private EditPDAutomatonLayout editPDAutomatonLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editPDAutomatonLayout = new EditPDAutomatonLayout(this);
        setContentView(editPDAutomatonLayout.getRootView());
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        Map<String, ?> mapPrefs = pref.getAll();
        System.out.println(getPackageName());
        System.out.println("\n\nPREFERENCES\n\n");
        for (Map.Entry<String, ?> entry : mapPrefs.entrySet()) {
            System.out.println("Key: " + entry.getKey() + "\nValue: " + entry.getValue());
        }
        Intent intent = getIntent();
        if (intent != null
                && intent.getSerializableExtra(PUSHDOW_AUTOMATON_EXTEND) != null) {
            editPDAutomatonLayout.drawPushdownAutomaton((PushdownAutomatonExtend) intent
                    .getSerializableExtra(PUSHDOW_AUTOMATON_EXTEND));
        }  else if (intent != null
                && intent.getSerializableExtra(GRAPH_ADAPTER_EXTRA) != null) {
            editPDAutomatonLayout.drawGraph((GraphAdapter) intent
                    .getSerializableExtra(GRAPH_ADAPTER_EXTRA));
        } else {
            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.remove(EDIT_PUSHDOW_AUTOMATON);
//            editor.remove(EDIT_PUSHDOW_AUTOMATON_POINTS);
//            editor.apply();
            String pushdownAutomatonStr = preferences.getString(EDIT_PUSHDOW_AUTOMATON, null);
            String labelToPointStr = preferences.getString(EDIT_PUSHDOW_AUTOMATON_POINTS, null);
            if (pushdownAutomatonStr != null && labelToPointStr != null) {
                PushdownAutomaton pushdownAutomaton = (PushdownAutomaton) ObjectSerializerHelper
                        .stringToObject(pushdownAutomatonStr);
                Map<String, MyPoint> labelToPoint = (Map<String, MyPoint>) ObjectSerializerHelper
                        .stringToObject(labelToPointStr);
                if (pushdownAutomaton != null && labelToPoint != null) {
                    editPDAutomatonLayout.drawPDAWithLabel(pushdownAutomaton, labelToPoint);
                }
            }
        }
    }

    private void saveData() {
        PushdownAutomaton pushdownAutomaton = editPDAutomatonLayout.getPushdownAutomaton();
        Map<String, MyPoint> labelToPoint = editPDAutomatonLayout.getMapLabelToPoint();
        if (pushdownAutomaton == null || labelToPoint == null
                || pushdownAutomaton.getStates().size() == 0) {
            return;
        }
        Map<State, MyPoint> stateToPoint = new HashMap<>();
        for (String stateStr : labelToPoint.keySet()) {
            stateToPoint.put(pushdownAutomaton.getState(stateStr),
                    labelToPoint.get(stateStr));
        }
        DotLanguage dotLanguage = new DotLanguage(pushdownAutomaton,
                stateToPoint);
        System.out.println(dotLanguage.getGraph());
        new DbAcess(this).putMachineDotLanguage(dotLanguage);
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString(EDIT_PUSHDOW_AUTOMATON, ObjectSerializerHelper
                .objectToString(pushdownAutomaton));
        editor.putString(EDIT_PUSHDOW_AUTOMATON_POINTS, ObjectSerializerHelper
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
        getMenuInflater().inflate(R.menu.menu_pd_automaton, menu);
        return true;
    }

    class BooleanWrapper {
        boolean value = false;
    }

    public void toGrammar() {
        PushdownAutomaton pushdownAutomaton = editPDAutomatonLayout.getCompletePushdownAutomaton();
        if (pushdownAutomaton != null) {
            final Grammar grammar = PDAToGrammar.toGrammar(pushdownAutomaton);
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE );
            final LinearLayout dialogGrammar = (LinearLayout) inflater.inflate(
                    R.layout.dialog_pda_to_grammar, null);
            final TextView tvGrammar = (TextView) dialogGrammar.findViewById(R.id.grammar);
            tvGrammar.setEnabled(true);
            tvGrammar.setText(grammar.toHtml());
            final BooleanWrapper simplify = new BooleanWrapper();
            final AlertDialog alertDialog = builder.setView(dialogGrammar)
                    .setNeutralButton(R.string.simplify_vars, null)
                    .setPositiveButton(R.string.copy_grammar, null)
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            //  Your code when user clicked on Cancel
                        }
                    })
                    .create();
            alertDialog.show();
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
                    new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!simplify.value) {
                        Toast.makeText(getBaseContext(),
                                R.string.need_simplify_vars,
                                Toast.LENGTH_SHORT).show();
                        grammar.simplifyVariables();
                        tvGrammar.setText(grammar.toHtml());
                        simplify.value = true;
                        return;
                    }
                    Bundle params = new Bundle();
                    params.putString(GRAMMAR_EXTRA, grammar.toStringRulesMapLeftToRight());
                    Intent intent = new Intent(getBaseContext(), GrammarActivity.class);
                    intent.putExtras(params);
                    startActivity(intent);
                    finish();
                    alertDialog.dismiss();
                }
            });
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            grammar.simplifyVariables();
                            tvGrammar.setText(grammar.toHtml());
                            simplify.value = true;
                        }
                    });
        }
    }

    public void toGrammarStepByStep() {
        PushdownAutomaton pushdownAutomaton = editPDAutomatonLayout.getCompletePushdownAutomaton();
        if (pushdownAutomaton != null) {
            Intent intent = new Intent(this,
                    PDAToCFGStage1Activity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(PUSHDOWN_AUTOMATON_EXTRA, pushdownAutomaton);
            Map<String, MyPoint> points = editPDAutomatonLayout.getMapLabelToPoint();
            UtilActivities.logMap(points);
            bundle.putSerializable(LABEL_TO_POINT_EXTRA, (Serializable) points);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private boolean clear() {
        editPDAutomatonLayout.clear();
        return true;
    }

    private boolean history() {
        Intent intent = new Intent(this, HistoricalGraphActivity.class);
        intent.putExtra(HistoricalGraphActivity.MACHINE_TYPE_SEL, MachineType.PDA);
        startActivity(intent);
        return true;
    }

    private boolean verifyEntry() {
        final PushdownAutomaton pushdownAutomaton = editPDAutomatonLayout.getPushdownAutomaton();
        final Map<String, MyPoint> labelToPoint = editPDAutomatonLayout.getMapLabelToPoint();
        if (pushdownAutomaton != null) {
            if (pushdownAutomaton.getInitialState() == null) {
                Toast.makeText(EditPushdownAutomatonActivity.this,
                        "Erro! Autômato com pilha não possui estado inicial!",
                        Toast.LENGTH_LONG).show();
                return true;
            }
            if (pushdownAutomaton.getFinalStates().isEmpty()) {
                Toast.makeText(EditPushdownAutomatonActivity.this,
                        "Erro! Autômato com pilha não possui estado final!",
                        Toast.LENGTH_LONG).show();
                return true;
            }
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            builder.setView(inflater.inflate(R.layout.dialog_entry, null))
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Dialog f = (Dialog) dialog;
                            EditText etWord = (EditText) f.findViewById(R.id.word);
                            final String word = etWord.getText().toString();
                            Intent intent = new Intent(EditPushdownAutomatonActivity.this,
                                    ProcessWordPDAActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(PDA_AUTOMATON_EXTRA, pushdownAutomaton);
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
//            case R.id.toGrammar:
//                toGrammar();
//                return true;
            case R.id.toGrammarStepByStep:
                toGrammarStepByStep();
                return true;
            case R.id.verifyEntry:
                //Verify
                return verifyEntry();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
