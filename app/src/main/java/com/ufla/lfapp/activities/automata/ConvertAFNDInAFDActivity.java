package com.ufla.lfapp.activities.automata;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.automata.customviews.TitleTableTextView;
import com.ufla.lfapp.activities.automata.graph.layout.EditGraphLayout;
import com.ufla.lfapp.activities.automata.graph.layout.GestureListenerForNonEditAutomaton;
import com.ufla.lfapp.activities.automata.graph.layout.NonEditGraphLayout;
import com.ufla.lfapp.vo.machine.Automaton;
import com.ufla.lfapp.vo.machine.AutomatonGUI;
import com.ufla.lfapp.vo.machine.State;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

public class ConvertAFNDInAFDActivity extends AppCompatActivity {

    private static final String LAMBDA = "λ";
    private static final String TRANSACTION = "δ";
    private static final String EMPTY_SET = "∅";
    private EditGraphLayout editGraphLayoutIn;
    private EditGraphLayout editGraphLayoutOut;
    private boolean inAFNDLamdaToAFND;
    private AutomatonGUI automatonGUIEdit;
    private AutomatonGUI automatonGUIAFND;
    private AutomatonGUI automatonGUIAFD;
    private State[] states;
    private String[] alphabet;
    private LinearLayout tableLayoutViews;
    private TableLayout tableLayoutIn;
    private Space spaceBetweenTables;
    private TableLayout tableLayoutOut;
    private TableRow[] tableRowsOut;
    private SortedMap<Pair<State, String>, SortedSet<State>> transitionTableAFNDLambda;
    private SortedMap<Pair<State, String>, SortedSet<State>> transitionTableAFND;
    private Deque<SortedSet<State>> statesAFDToCreated;
    private Deque<SortedSet<State>> statesAFDCreated;
    private Set<String> statesAFD;
    private int actualX;
    private int actualY;
    private int minLenghtTableLenght = 5;
    private int maxLenghtTableLayout;
    int deslocX = 0;
    private int maxX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_afndin_afd);
        Intent intent = getIntent();
        if (intent == null || intent.getExtras() == null
                || intent.getExtras().getSerializable("AutomatonGUIEdit") == null) {
            Toast.makeText(this, "Erro! Autômato não foi encontrado!", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        Bundle bundle = intent.getExtras();
        automatonGUIEdit = (AutomatonGUI) bundle.getSerializable("AutomatonGUIEdit");
        editGraphLayoutIn = new NonEditGraphLayout(this);
        editGraphLayoutIn.drawAutomaton(automatonGUIEdit);
        editGraphLayoutIn.getRootView().setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        ((ConstraintLayout) findViewById(R.id.automatonIn))
                .addView(editGraphLayoutIn.getRootView());
        editGraphLayoutOut = new NonEditGraphLayout(this);
        defineHorizontalScrollViews();
        newTablesLayout();
        if (automatonGUIEdit.isAFNDLambda()) {
            Automaton automaton = automatonGUIEdit.AFNDLambdaToAFND();
            automatonGUIAFND = new AutomatonGUI(automaton, automaton.getStatesPointsFake());
            automatonGUIAFND.setInitialStatesFromAFNDLambdaToAFD(
                    automaton.getInitialStatesFromAFNDLambdaToAFD());
            automaton = automatonGUIAFND.AFNDtoAFD();
            automatonGUIAFD = new AutomatonGUI(automaton, automaton.getStatesPointsFake());
            editGraphLayoutOut.drawAutomaton(automatonGUIAFND);
            preencherTableAFNDLambdaIn();
            preencherTableAFNDOut();
        } else if (automatonGUIEdit.isAFND()) {
            Automaton automaton = automatonGUIAFND.AFNDtoAFD();
            automatonGUIAFD = new AutomatonGUI(automaton, automaton.getStatesPointsFake());
            editGraphLayoutOut.drawAutomaton(automatonGUIAFD);
            inAFNDLamdaToAFND = false;
            preencherTableAFNDIn();
            preencherTableAFDOut();
            addTextViewAFNDInfo();
        }
        final AutomatonGUI automatonGUIAFDRef = automatonGUIAFD;
        editGraphLayoutOut.getRootView().setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        ((ConstraintLayout) findViewById(R.id.automatonOut))
                .addView(editGraphLayoutOut.getRootView());
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = (int) (size.y * 0.45f);
        int width = (int) (size.x / 2.0f);
        ((ConstraintLayout) findViewById(R.id.automatons)).setMinHeight(height);
        ((ConstraintLayout) findViewById(R.id.automatons)).setMaxHeight(height);
        editGraphLayoutIn.resizeTo(width,  height);
        editGraphLayoutOut.resizeTo(width, height);
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
        findViewById(R.id.copyAutomaton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (automatonGUIAFDRef == null) {
                    return;
                }
                Context context = ConvertAFNDInAFDActivity.this;
                Intent intent = new Intent(context, EditAutomataActivity.class );
                Automaton automaton = automatonGUIAFDRef.getAutomatonWithStatesNameSimplify();
                intent.putExtra("Automaton", new AutomatonGUI(automaton,
                        automaton.getStatesPointsFake()));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });

        findViewById(R.id.end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConvertAFNDInAFDActivity.this.end();
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConvertAFNDInAFDActivity.this.back();
            }
        });

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConvertAFNDInAFDActivity.this.next();
            }
        });

        findViewById(R.id.algol).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConvertAFNDInAFDActivity.this.algol();
            }
        });
    }

    public void changeAFNDLambdaToAFND() {
        inAFNDLamdaToAFND = false;
        newTablesLayout();
        preencherTableAFNDIn();
        preencherTableAFDOut();
        addTextViewAFNDInfo();
        editGraphLayoutOut.drawAutomaton(automatonGUIAFD);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = (int) (size.y * 0.45f);
        int width = (int) (size.x / 2.0f);
        editGraphLayoutOut.resizeTo(width, height);
    }

//    public void newTableLayoutIn() {
//        if (tableLayoutIn != null) {
//            horizontalScrollView.removeView(tableLayoutIn);
//        }
//        tableLayoutIn = new TableLayout(this);
//        tableLayoutIn.setLayoutParams(
//                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT));
//        horizontalScrollView.addView(tableLayoutIn);
//    }
//
//    public void newTableLayoutOut() {
//        if (tableLayoutOut != null) {
//            horizontalScrollView.removeView(tableLayoutIn);
//        }
//        tableLayoutOut = new TableLayout(this);
//        tableLayoutOut.setLayoutParams(
//                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT));
//        horizontalScrollView.addView(tableLayoutOut);
//    }

    public void addTextViewAFNDInfo() {
        Space space= new Space(this);
        space.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int height = (int) (metrics.heightPixels * 0.05f);
        space.setMinimumHeight(height);
        space.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        tableLayoutViews.addView(space);
        TextView textView = new TextView(this);
        textView.setTextColor(Color.BLACK);
        textView.setText("AFND => q0 = " + automatonGUIAFND.collectionToString(
                automatonGUIAFND.getInitialStatesFromAFNDLambdaToAFD()) + "; F = " +
                automatonGUIAFND.collectionToString(automatonGUIAFND.getFinalStates()));
        textView.setEnabled(true);
        textView.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        tableLayoutViews.addView(textView);
        tableLayoutViews.measure(0, 0);
        View parent = (View) tableLayoutViews.getParent();
        parent.measure(0, 0);
        parent = (View) parent.getParent();
        parent.measure(0, 0);
        parent = (View) parent.getParent();
        parent.measure(0, 0);
    }

    public boolean haveElementsInCommon(Set<State> set1, Set<State> set2) {
        for (State object : set1) {
            if (set2.contains(object)) {
                return true;
            }
        }
        return false;
    }

    public void addTextViewAFDInfo() {
        TextView textView = new TextView(this);
        textView.setTextColor(Color.BLACK);
        SortedSet<State> finalStatesAFND = automatonGUIAFND.getFinalStates();
        StringBuilder sbFinalStates = new StringBuilder();
        sbFinalStates.append('{');
        Iterator<SortedSet<State>> it = statesAFDCreated.descendingIterator();
        while (it.hasNext()) {
            SortedSet<State> states = it.next();
            for (State state : states) {
                if (finalStatesAFND.contains(state)) {
                    sbFinalStates.append(getLabelAFD(states));
                    sbFinalStates.append(", ");
                    break;
                }
            }
//            if (haveElementsInCommon(states, finalStatesAFND)) {
//                sbFinalStates.append(getLabelAFD(states));
//                sbFinalStates.append(',');
//            }
        }
        sbFinalStates.deleteCharAt(sbFinalStates.length() - 1);
        sbFinalStates.setCharAt(sbFinalStates.length() - 1, '}');
        String text = "AFD => q0 = " + getLabelAFD(statesAFDCreated.getLast()) +
                "; F = " + sbFinalStates.toString();
        textView.setText(text);
        textView.setEnabled(true);
        textView.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        tableLayoutViews.addView(textView);
        tableLayoutViews.measure(0, 0);
    }

    public void newTablesLayout() {
        if (tableLayoutIn != null) {
            tableLayoutViews.removeView(tableLayoutIn);
        }
        if (spaceBetweenTables != null) {
            tableLayoutViews.removeView(spaceBetweenTables);
        }
        if (tableLayoutOut != null) {
            tableLayoutViews.removeView(tableLayoutOut);
        }
        tableLayoutIn = new TableLayout(this);
        tableLayoutIn.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        tableLayoutViews.addView(tableLayoutIn);


        spaceBetweenTables= new Space(this);
        spaceBetweenTables.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int height = (int) (metrics.heightPixels * 0.02f);
        spaceBetweenTables.setMinimumHeight(height);
        tableLayoutViews.addView(spaceBetweenTables);
        tableLayoutOut = new TableLayout(this);
        tableLayoutOut.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        tableLayoutViews.addView(tableLayoutOut);
        tableLayoutViews.measure(0, 0);
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
        ((ConstraintLayout) findViewById(R.id.tables)).addView(horizontalScrollView);

//        ScrollView scrollViewOut = new ScrollView(this);
//        scrollViewOut.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));
//        horizontalScrollViewOut = new HorizontalScrollView(this);
//        horizontalScrollViewOut.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));
//        scrollViewOut.addView(horizontalScrollViewOut);
//        ((ConstraintLayout) findViewById(R.id.tableOut)).addView(scrollViewOut);
    }

    public void algolAFNDLambdaToAFND() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        builder.setView(inflater.inflate(R.layout.dialog_algol_afnd_lambda_to_afd, null))
                .setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();
    }

    public void algolAFNDToAFD() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        builder.setView(inflater.inflate(R.layout.dialog_algol_afnd_to_afd, null))
                .setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();
    }

    public void algol() {
        if (inAFNDLamdaToAFND) {
            algolAFNDLambdaToAFND();
        } else {
            algolAFNDToAFD();
        }
    }

    public void afndLambdaToAFND() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        builder.setView(inflater.inflate(R.layout.dialog_conf_afnd_lambda_to_afd, null))
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ConvertAFNDInAFDActivity.this.changeAFNDLambdaToAFND();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();
    }

    public void reloadLenghtTableLayout() {
        reloadLenghtTableLayoutIntern(tableLayoutIn);
        reloadLenghtTableLayoutIntern(tableLayoutOut);
    }

    public void reloadLenghtTableLayoutIntern(TableLayout tableLayout) {
        int n = tableLayout.getChildCount();
        for (int i = 0; i < n; i++) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
            int n2 = tableRow.getChildCount();
            for (int j = 0; j < n2; j++) {
                TextView textView = (TextView) tableRow.getChildAt(j);
                textView.setWidth(maxLenghtTableLayout);
            }
        }
    }

    public void actualizationLenghtViewsValue(TextView textView) {
        int widthView = textView.getMeasuredWidth();
        if (widthView > maxLenghtTableLayout - minLenghtTableLenght) {
            maxLenghtTableLayout = widthView + minLenghtTableLenght;
        }
    }

    public void verificationWithReloadLenght(TextView textView) {
        int widthView = textView.getMeasuredWidth();
        if (widthView > maxLenghtTableLayout - minLenghtTableLenght) {
            maxLenghtTableLayout = widthView + minLenghtTableLenght;
            reloadLenghtTableLayout();
        }
    }

    public TextView getTextViewTableIn(String label) {
        TextView textView = getTextViewTable(label);
        actualizationLenghtViewsValue(textView);
        textView.setWidth(maxLenghtTableLayout);
        return textView;
    }

    public TextView getTextViewTableOut(String label) {
        TextView textView = getTextViewTable(label);
        verificationWithReloadLenght(textView);
        textView.setWidth(maxLenghtTableLayout);
        return textView;
    }

    private TitleTableTextView getTitleTableTextView (String label) {
        TitleTableTextView titleTableTextView = new TitleTableTextView(this);
        titleTableTextView.setTextColor(Color.BLACK);
        titleTableTextView.setGravity(Gravity.CENTER);
        titleTableTextView.setText(label);
        //textViewTitleTable.setTypeface(null, Typeface.BOLD);
        titleTableTextView.measure(0, 0);
        verificationWithReloadLenght(titleTableTextView);
        titleTableTextView.setWidth(maxLenghtTableLayout);
        return titleTableTextView;
    }

    public TextView getTextViewTable(String label) {
        TextView textView = new TextView(this);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);
        textView.setText(label);
        textView.measure(0, 0);
        return textView;
    }

    public void preencherTableAFNDOut() {
        maxX = alphabet.length - 1;
        TableRow row = new TableRow(this);
        TextView textView = getTitleTableTextView(TRANSACTION + "ND");
        row.addView(textView);
        for (String symbol : alphabet) {
            if (symbol.equals(LAMBDA)) {
                continue;
            }
            textView = getTitleTableTextView(symbol);
            row.addView(textView);
        }
        tableLayoutOut.addView(row);
        tableRowsOut = new TableRow[states.length];
        int i = 0;
        for (State state : states) {
            row = new TableRow(this);
            textView = getTextViewTableOut(state.getName());
            row.addView(textView);
            tableLayoutOut.addView(row);
            tableRowsOut[i] = row;
            i++;
        }
        actualX = 0;
        actualY = 0;
        tableLayoutOut.measure(0, 0);
        tableLayoutOut.invalidate();
    }

    public void preencherTableAFDOut() {
        maxX = alphabet.length;
        TableRow row = new TableRow(this);
        TextView textView = getTitleTableTextView(TRANSACTION + "D");
        row.addView(textView);
        for (String symbol : alphabet) {
            if (symbol.equals(LAMBDA)) {
                continue;
            }
            textView = getTitleTableTextView(symbol);
            row.addView(textView);
        }
        tableLayoutOut.addView(row);
        actualX = 0;
        actualY = -1;
        tableLayoutOut.measure(0, 0);
        tableLayoutOut.invalidate();
    }

    public void preencherTableAFNDIn() {
        inAFNDLamdaToAFND = false;
        transitionTableAFND = automatonGUIAFND.getTransitionTableAFND();
        TableRow row = new TableRow(this);
        TextView textView = getTitleTableTextView(TRANSACTION + "ND");
        row.addView(textView);
        SortedSet<String> alphabetSet = automatonGUIAFND.getAlphabet();
        alphabet = alphabetSet.toArray(new String[alphabetSet.size()]);
        for (String symbol : alphabet) {
            textView = getTitleTableTextView(symbol);
            row.addView(textView);
        }
        tableLayoutIn.addView(row);
        SortedSet<State> stateSortedSet = automatonGUIAFND.getStates();
        states = stateSortedSet.toArray(new State[stateSortedSet.size()]);
        transitionTableAFND = automatonGUIEdit.getTransitionTableAFND();
        for (State state : states) {
            row = new TableRow(this);
            textView = getTextViewTableIn(state.getName());
            row.addView(textView);
            for (String symbol : alphabet) {
                SortedSet<State> statesReach = transitionTableAFND.get(
                        Pair.create(state, symbol));
                if (statesReach.isEmpty()) {
                    textView = getTextViewTableIn(EMPTY_SET);
                } else {
                    textView = getTextViewTableIn(automatonGUIEdit.collectionToString(statesReach));
                }
                row.addView(textView);
            }
            tableLayoutIn.addView(row);
        }
        reloadLenghtTableLayoutIntern(tableLayoutIn);
    }

    public void preencherTableAFNDLambdaIn() {
        inAFNDLamdaToAFND = true;
        TableRow row = new TableRow(this);
        TextView textView = getTitleTableTextView(TRANSACTION + "ND-" + LAMBDA);
        row.addView(textView);
        SortedSet<String> alphabetSet = automatonGUIEdit.getAlphabet();
        alphabet = alphabetSet.toArray(new String[alphabetSet.size()]);
        for (String symbol : alphabet) {
            textView = getTitleTableTextView(symbol);
            row.addView(textView);
        }
        textView = getTitleTableTextView("fecho-" + LAMBDA);
        row.addView(textView);
        tableLayoutIn.addView(row);
        SortedSet<State> stateSortedSet = automatonGUIEdit.getStates();
        states = stateSortedSet.toArray(new State[stateSortedSet.size()]);
        transitionTableAFNDLambda =
                automatonGUIEdit.getTransitionTableAFND();
        automatonGUIEdit.insertFechoLambda(transitionTableAFNDLambda);
        for (State state : states) {
            row = new TableRow(this);
            textView = getTextViewTableIn(state.getName());
            row.addView(textView);
            for (String symbol : alphabet) {
                SortedSet<State> statesReach = transitionTableAFNDLambda.get(
                        Pair.create(state, symbol));
                if (statesReach.isEmpty()) {
                    textView = getTextViewTableIn(EMPTY_SET);
                } else {
                    textView = getTextViewTableIn(automatonGUIEdit.collectionToString(statesReach));
                }
                row.addView(textView);
            }
            SortedSet<State> statesReach = transitionTableAFNDLambda.get(
                    Pair.create(state, Automaton.FECHO_LAMBDA));
            if (statesReach.isEmpty()) {
                textView = getTextViewTableIn(EMPTY_SET);
            } else {
                textView = getTextViewTableIn(automatonGUIEdit.collectionToString(statesReach));
            }
            row.addView(textView);
            tableLayoutIn.addView(row);
        }
        tableLayoutIn.measure(0, 0);
        tableLayoutIn.invalidate();
        reloadLenghtTableLayoutIntern(tableLayoutIn);
    }

    public void next() {
        if (inAFNDLamdaToAFND) {
            nextAFND();
        } else {
            nextAFD();
        }
    }

    public boolean isLastState() {
        return actualY == states.length - 1;
    }

    public boolean isLastSymbol() {
        return actualX + deslocX == alphabet.length - 1;
    }

    public boolean isLambda() {
        return  alphabet[actualX + deslocX].equals(LAMBDA);
    }

    public boolean isLastSymbolConsumedAndLambda() {
        return isLambda() && isLastState() && isLastSymbol();
    }

    public boolean isLastSymbolOfStateAndLambda() {
        return isLambda() && isLastSymbol();
    }

    public boolean allSymbolsConsumed() {
        return actualY == states.length;
    }

    public void nextAFND() {
        if (allSymbolsConsumed() || isLastSymbolConsumedAndLambda()) {
            afndLambdaToAFND();
            return;
        }
        if (isLastSymbolOfStateAndLambda()) {
            actualX = 0;
            deslocX = 0;
            actualY++;
            return;
        }
        if (isLambda()) {
            deslocX++;
        }
        int symbolPos = actualX + deslocX;
        SortedSet<State> statesAFND = new TreeSet<>();
        String symbol = alphabet[symbolPos];
        State actualState = states[actualY];
        SortedSet<State> newStates = new TreeSet<>();
        // Adicionando transições através do fecho-lambda mais uma leitura
        SortedSet<State> fechoLambda = transitionTableAFNDLambda.get(
                Pair.create(actualState, Automaton.FECHO_LAMBDA));
        for (State state : fechoLambda) {
            SortedSet<State> futureStates = transitionTableAFNDLambda.get(
                    Pair.create(state, symbol));
            for (State futureState : futureStates) {
                newStates.add(futureState);
            }
        }
        // Adicionando transições através de uma leitura e também do fecho-lambda após essa
        // leitura
        SortedSet<State> futureStates = transitionTableAFNDLambda.get(Pair.create(actualState, symbol));
        for (State state : futureStates) {
            newStates.add(state);
            for (State futureStateB : transitionTableAFNDLambda.get(Pair.create(state,
                    Automaton.FECHO_LAMBDA))) {
                newStates.add(futureStateB);
            }
        }
        TextView textView;
        if (newStates.isEmpty()) {
            textView = getTextViewTableOut(EMPTY_SET);
        } else {
            textView = getTextViewTableOut(automatonGUIEdit.collectionToString(newStates));
        }
        tableRowsOut[actualY].addView(textView);
        if (symbolPos == alphabet.length) {
            actualX = 0;
            deslocX = 0;
            actualY++;
        } else {
            actualX++;
        }
    }

    public String getLabelAFD(SortedSet<State> states) {
        if (states == null || states.isEmpty()) {
            return "-";
        }
        StringBuilder sbLabel = new StringBuilder();
        sbLabel.append('<');
        for (State state : states) {
            sbLabel.append(state.getName()).append(',');
        }
        sbLabel.setCharAt(sbLabel.length() - 1, '>');
        return sbLabel.toString();
    }

    public void nextAFD() {
        if (isEndOfConversionAFNDToAFD()) {
            Toast.makeText(this, "Conversão realizada\n", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (actualX == alphabet.length + 1) {
            addTextViewAFDInfo();
            actualX++;
            return;
        }
        if (actualY == -1) {
            actualY++;
            tableRowsOut = new TableRow[1];
            TableRow row = new TableRow(this);
            statesAFDCreated = new ArrayDeque<>();
            statesAFDToCreated = new ArrayDeque<>();
            statesAFD = new LinkedHashSet<>();
            tableRowsOut[actualY] = row;
            SortedSet<State> initial = automatonGUIAFND.getInitialStatesFromAFNDLambdaToAFD();
            String initialStr = getLabelAFD(initial);
            statesAFD.add(initialStr);
            row.addView(getTextViewTableOut(initialStr));
            tableLayoutOut.addView(row);
            statesAFDCreated.push(initial);
        } else {
            if (actualX == 0) {
                if (!statesAFDToCreated.isEmpty()) {
                    SortedSet<State> newState = statesAFDToCreated.poll();
                    statesAFDCreated.push(newState);
                    tableRowsOut[actualY].addView(getTextViewTableOut(getLabelAFD(newState)));
                }
            } else {
                SortedSet<State> fromState = statesAFDCreated.peek();
                SortedSet<State> toState = new TreeSet<>();
                for (State state : fromState) {
                    toState.addAll(automatonGUIAFND.getStatesReachWithSymbol(state,
                            alphabet[actualX - 1]));
                }
                String toStateStr = getLabelAFD(toState);
                tableRowsOut[actualY].addView(getTextViewTableOut(toStateStr));
                if (!toStateStr.equals("-") && !statesAFD.contains(toStateStr)) {
                    statesAFD.add(toStateStr);
                    statesAFDToCreated.offer(toState);
                }
            }
        }
        if (actualX == alphabet.length) {
            if (!statesAFDToCreated.isEmpty()) {
                actualY++;
                actualX = 0;
                TableRow newTableRowsOut[] = new TableRow[tableRowsOut.length + 1];
                for (int i = 0; i < tableRowsOut.length; i++) {
                    newTableRowsOut[i] = tableRowsOut[i];
                }
                tableRowsOut = newTableRowsOut;
                TableRow row = new TableRow(this);
                tableRowsOut[actualY] = row;
                tableLayoutOut.addView(row);
            } else {
                actualX++;
            }
        } else {
            actualX++;
        }
    }

    public void backAFND() {
        if (actualX == 0 && actualY == 0) {
            Toast.makeText(this, "Não é possível voltar.", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (actualX + deslocX == 0) {
            TextView textView = (TextView) tableRowsOut[actualY - 1].getChildAt(maxX);
            tableRowsOut[actualY - 1].removeView(textView);
            deslocX = 1;
            actualX = maxX - 1;
            actualY--;
        } else {
            TextView textView = (TextView) tableRowsOut[actualY].getChildAt(actualX + deslocX);
            tableRowsOut[actualY].removeView(textView);
            actualX--;
        }
    }

    public void backAFD() {
        if (actualX == alphabet.length + 2) {
            TextView textView = (TextView) tableLayoutViews.getChildAt(5);
            tableLayoutViews.removeView(textView);
            actualX--;
            return;
        }
        if (actualX == 0 && actualY < 1) {
            Toast.makeText(this, "Não é possível voltar.", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (actualX == 0) {
            TextView textView = (TextView) tableRowsOut[actualY - 1].getChildAt(alphabet.length);
            tableRowsOut[actualY - 1].removeView(textView);
            TableRow newTableRowsOut[] = new TableRow[tableRowsOut.length - 1];
            for (int i = 0; i < tableRowsOut.length - 1; i++) {
                newTableRowsOut[i] = tableRowsOut[i];
            }
            tableRowsOut = newTableRowsOut;
            actualX = alphabet.length;
            actualY--;
            return;
        }
        if (actualX == 1) {
            TextView textView = (TextView) tableRowsOut[actualY].getChildAt(0);
            tableRowsOut[actualY].removeView(textView);
            statesAFDToCreated.offerFirst(statesAFDCreated.pop());
            actualX--;
            return;
        }
        TextView textView = (TextView) tableRowsOut[actualY].getChildAt(actualX - 1);
        tableRowsOut[actualY].removeView(textView);
        actualX--;
    }

    public void back() {
        if (inAFNDLamdaToAFND) {
            backAFND();
        } else {
            backAFD();
        }
    }

    public boolean isEndOfConversionAFNDToAFD() {
        return actualX == alphabet.length + 2;
    }

    public void end() {
        if (inAFNDLamdaToAFND) {
            while (actualY < states.length - 1 || (deslocX + actualX) < maxX) {
                next();
            }
        } else {
            while (!isEndOfConversionAFNDToAFD()) {
                next();
            }
        }
    }



}
