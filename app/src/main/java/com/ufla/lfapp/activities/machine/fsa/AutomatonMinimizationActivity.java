package com.ufla.lfapp.activities.machine.fsa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.AppCompatActivityContext;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomaton;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomatonGUI;
import com.ufla.lfapp.views.graph.layout.EditGraphLayout;
import com.ufla.lfapp.views.graph.layout.NonEditGraphLayout;
import com.ufla.lfapp.views.machine.layout.GestureListenerForNonEditAutomaton;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class AutomatonMinimizationActivity extends AppCompatActivityContext {

    private enum Stage {

        FINAL_AND_NOT_FINAL,
        MINIMIZATION

    }

    private static final String AUTOMATON_GUI_EDIT_EXTRA = "AutomatonGUIEdit";
    private static final String FINITE_STATE_AUTOMATON_EXTRA = "FiniteStateAutomaton";

    private Stage stage;
    private EditGraphLayout editGraphLayoutIn;
    private EditGraphLayout editGraphLayoutOut;
    private FiniteStateAutomatonGUI automatonGUIEdit;
    private FiniteStateAutomatonGUI automatonGUIMinimization;
    private LinearLayout tableLayoutViews;
    private SortedSet<String> alphabet;
    private AutomatonMinimizationTable automatonMinimizationTable;
    private int actualRow = 1;
    private int beginMinimization;
    private List<Integer> lastRowChanges;
    private Deque<List<Integer>> changes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automaton_minimization);
        Intent intent = getIntent();
        if (intent == null || intent.getExtras() == null
                || intent.getExtras().getSerializable(AUTOMATON_GUI_EDIT_EXTRA) == null) {
            Toast.makeText(this, R.string.exception_automaton_not_found, Toast.LENGTH_LONG)
                    .show();
            return;
        }
        stage = Stage.FINAL_AND_NOT_FINAL;
        Bundle bundle = intent.getExtras();
        automatonGUIEdit = (FiniteStateAutomatonGUI) bundle.getSerializable(AUTOMATON_GUI_EDIT_EXTRA);
        alphabet = automatonGUIEdit.getAlphabet();
        editGraphLayoutIn = new NonEditGraphLayout(this);
        editGraphLayoutIn.drawAutomaton(automatonGUIEdit);
        editGraphLayoutIn.getRootView().setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        ((ConstraintLayout) findViewById(R.id.automatonIn))
                .addView(editGraphLayoutIn.getRootView());
        defineHorizontalScrollViews();
        automatonMinimizationTable = new AutomatonMinimizationTable(this, automatonGUIEdit);
        tableLayoutViews.addView(automatonMinimizationTable.getTableLayout());
        FiniteStateAutomaton finiteStateAutomaton = automatonGUIEdit.getMinimizeAutomaton();
        automatonGUIMinimization = new FiniteStateAutomatonGUI(finiteStateAutomaton, finiteStateAutomaton.getStatesPointsFake());
        editGraphLayoutOut = new NonEditGraphLayout(this);
        editGraphLayoutOut.drawAutomaton(automatonGUIMinimization);
        editGraphLayoutOut.getRootView().setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        ((ConstraintLayout) findViewById(R.id.automatonOut))
                .addView(editGraphLayoutOut.getRootView());
        final FiniteStateAutomatonGUI automatonGUIMinimizationRef = automatonGUIMinimization;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        ConstraintLayout automatonsLayout = (ConstraintLayout) findViewById(R.id.automatons);
        int automatonsHeight = (int) (size.y * 0.45f);
        int automatonsWidth = (int) (size.x / 2.0f);
        automatonsLayout.setMinimumHeight(automatonsHeight);
        automatonsLayout.setMinHeight(automatonsHeight);
        automatonsLayout.setMaxHeight(automatonsHeight);
        ConstraintLayout buttonsLayout = (ConstraintLayout) findViewById(R.id.tableButtons);
        int buttonsHeight = (int) (size.y * 0.10f);
        buttonsLayout.setMaxHeight(buttonsHeight);
        final GestureDetector gestureDetectorIn = new GestureDetector(this,
                new GestureListenerForNonEditAutomaton(this, editGraphLayoutIn));
        final GestureDetector gestureDetectorOut = new GestureDetector(this,
                new GestureListenerForNonEditAutomaton(this, editGraphLayoutOut));
        editGraphLayoutIn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetectorIn.onTouchEvent(event);
            }
        });
        editGraphLayoutOut.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetectorOut.onTouchEvent(event);
            }
        });
        editGraphLayoutIn.resizeTo(automatonsWidth,  automatonsHeight);
        editGraphLayoutOut.resizeTo(automatonsWidth, automatonsHeight);
        findViewById(R.id.copyAutomaton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (automatonGUIMinimizationRef == null) {
                    return;
                }
                Context context = AutomatonMinimizationActivity.this;
                Intent intent = new Intent(context, EditFiniteStateAutomatonActivity.class );
                FiniteStateAutomaton finiteStateAutomaton = automatonGUIMinimizationRef;
                intent.putExtra(FINITE_STATE_AUTOMATON_EXTRA, new FiniteStateAutomatonGUI(finiteStateAutomaton,
                        finiteStateAutomaton.getStatesPointsFake()));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });

        findViewById(R.id.end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutomatonMinimizationActivity.this.end();
            }
        });

        findViewById(R.id.previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutomatonMinimizationActivity.this.back();
            }
        });

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutomatonMinimizationActivity.this.next();
            }
        });

        findViewById(R.id.algol).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutomatonMinimizationActivity.this.algol();
            }
        });
    }

    public void defineHorizontalScrollViews() {
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this);
        horizontalScrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        ScrollView scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        horizontalScrollView.addView(scrollView);
        tableLayoutViews = new LinearLayout(this);
        tableLayoutViews.setOrientation(LinearLayout.VERTICAL);
        tableLayoutViews.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        scrollView.addView(tableLayoutViews);
        ((ConstraintLayout) findViewById(R.id.table)).addView(horizontalScrollView);
    }


    public void end() {
        while (actualRow < automatonMinimizationTable.getMaxRow()
                || !stage.equals(Stage.MINIMIZATION)) {
            next();
        }
    }


    private void backMinimization() {
        //Log.d("back", "INICIO");
        if (changes.isEmpty()) {
            actualRow = beginMinimization;
            Toast.makeText(this, R.string.exception_not_previous_op, Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        List<Integer> actualChanges = changes.pop();
        int lastRow = actualChanges.get(0);
        String actualIndex = automatonMinimizationTable.getIndex(lastRow);
        //Log.d("back", actualIndex);
        automatonMinimizationTable.clearRow(lastRow);
        if (automatonMinimizationTable.isDistinct(lastRow)) {
            for (Integer row : actualChanges) {
                //Log.d("remove distinct", automatonMinimizationTable.getIndex(row));
                automatonMinimizationTable.clearRow(row);
                automatonMinimizationTable.setDefaultDistinct(row);
                automatonMinimizationTable.setDefaultReason(row);
            }
        } else {
            int n = actualChanges.size();
            for (int i = 1; i < n; i++) {
                String index = automatonMinimizationTable.getIndex(actualChanges.get(i));
                //Log.d("remove subject", index + ", " + actualIndex);
                automatonMinimizationTable.removeSubject(index, actualIndex);
            }
        }
        lastRowChanges.clear();
        actualRow = changes.isEmpty() ? beginMinimization : changes.peek().get(0);
        //Log.d("changesBack", "" + changes.size());
        //Log.d("back", "INICIO");
    }

    public void back() {
        if (actualRow >= automatonMinimizationTable.getMaxRow()) {
            actualRow--;
        }
        automatonMinimizationTable.clearRow(actualRow);
        if (stage.equals(Stage.FINAL_AND_NOT_FINAL)) {
            if (actualRow == 1) {
                Toast.makeText(this, R.string.min_fsa_not_previous_step, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            actualRow--;
            automatonMinimizationTable.clearRow(actualRow);
            automatonMinimizationTable.setDefaultDistinct(actualRow);
            automatonMinimizationTable.setDefaultReason(actualRow);
        } else {
            backMinimization();
        }
        automatonMinimizationTable.selected(actualRow);
    }

    public void nextFinalAndNotFinal() {
        if (actualRow > 1) {
            automatonMinimizationTable.clearRow(actualRow - 1);
        }
        if (actualRow == automatonMinimizationTable.getMaxRow()) {
            stage = Stage.MINIMIZATION;
            lastRowChanges = new ArrayList<>();
            changes = new ArrayDeque<>();
            lastRowChanges.add(actualRow - 1);
            actualRow = 1;
            jumpDistincts();
            beginMinimization = actualRow;
            automatonMinimizationTable.clearDistints();
            Toast.makeText(this, R.string.min_fsa_step_1_compl,
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        automatonMinimizationTable.selected(actualRow);
        automatonMinimizationTable.isDistinctFinalAndNotFinal(actualRow);
        actualRow++;
    }

    public void jumpDistincts() {
        int maxRow = automatonMinimizationTable.getMaxRow();
        while (actualRow < maxRow && automatonMinimizationTable.
                isDistinct(actualRow)) {
            if (actualRow > 1) {
                automatonMinimizationTable.clearRow(actualRow - 1);
            }
            actualRow++;
        }
    }

    public void clearChanges() {
        for (Integer row : lastRowChanges) {
            automatonMinimizationTable.clearRow(row);
        }
        lastRowChanges.clear();
    }

    public Pair<State, State> getFutureStates(Pair<State, State> fromStates, String symbol) {
        State stateA = automatonGUIEdit.getFutureStateOfTransition(fromStates.first, symbol);
        State stateB = automatonGUIEdit.getFutureStateOfTransition(fromStates.second, symbol);
        if (stateA.compareTo(stateB) > 0) {
            return Pair.create(stateB, stateA);
        }
        return Pair.create(stateA, stateB);
    }

    public void nextMinimization() {
        clearChanges();
        if (actualRow == automatonMinimizationTable.getMaxRow()) {
            Toast.makeText(this, R.string.mim_fsa_compl, Toast.LENGTH_SHORT)
                .show();
            return;
        }
        lastRowChanges.add(actualRow);
        automatonMinimizationTable.selected(actualRow);
        Pair<State, State> states = automatonMinimizationTable.getStatesByRow(actualRow);
        String actualIndex = automatonMinimizationTable.getIndex(actualRow);
        boolean actualIsDistinct = false;
        //Log.d("next", actualIndex);
        SortedSet<String> subjects = new TreeSet<>();
        for (String symbol : alphabet) {
            Pair<State, State> futureStates = getFutureStates(states, symbol);
            if (futureStates.first.equals(futureStates.second)) {
                continue;
            }
            String index = automatonMinimizationTable.getIndex(futureStates.first,
                    futureStates.second);
            if (automatonMinimizationTable.isDistinct(index)) {
                //Log.d("distinto", automatonMinimizationTable.getIndex(actualRow));
                automatonMinimizationTable.setDistinct(actualRow);
                automatonMinimizationTable.setReason(actualRow, symbol);
                actualIsDistinct = true;
                lastRowChanges.addAll(automatonMinimizationTable.setSubjectsDistincts(actualIndex));
                break;
            } else {
                if (!actualIndex.equals(index)) {
                    subjects.add(index);
                }
            }
        }
        if (!actualIsDistinct) {
            for (String index : subjects) {
                //Log.d("adiciona subject", index + ", " + actualIndex);
                automatonMinimizationTable.addToSubjects(index, actualIndex);
                lastRowChanges.add(automatonMinimizationTable.getRowNumber(index));
            }
        }
        changes.push(new ArrayList<>(lastRowChanges));
        actualRow++;
        jumpDistincts();
        //Log.d("changes", "" + changes.size());
        //Log.d("next", "FIM");
    }


    public void next() {
        if (stage.equals(Stage.FINAL_AND_NOT_FINAL)) {
            nextFinalAndNotFinal();
        } else {
            nextMinimization();
        }
    }

    public void algol() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        AlertDialog dialogAlgol =
                builder.setView(inflater.inflate(R.layout.dialog_algol_automaton_minimization, null))
                    .setNegativeButton(R.string.close, null)
                        .setTitle(getString(R.string.minim_fsa_title))
                .create();
        dialogAlgol.show();
        TextView tvAlgol = (TextView) dialogAlgol.findViewById(R.id.tvAlgol);
        tvAlgol.setText(Html.fromHtml(getResources().getString(
                R.string.algol_automaton_minimization)));
        tvAlgol.setEnabled(true);

    }
}
