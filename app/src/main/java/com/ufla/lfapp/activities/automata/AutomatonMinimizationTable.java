package com.ufla.lfapp.activities.automata;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ufla.lfapp.activities.automata.customviews.SelectedRowTextView;
import com.ufla.lfapp.activities.automata.customviews.TitleTableTextView;
import com.ufla.lfapp.vo.machine.Automaton;
import com.ufla.lfapp.vo.machine.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by carlos on 2/1/17.
 */

public class AutomatonMinimizationTable {

    private Activity activity;
    private TableLayout tableLayout;
    private Automaton automaton;
    private boolean distinct[];
    private int rowIndex[];
    private Map<String, Pair<State, State>> indexToPairState;
    private Map<String, Integer> indexToRow;
    private Map<String, SortedSet<String>> pairStateToSubjects;
    private final int MIN_LENGTH_CELL = 5;
    private int lengthCell;

    public void selected(int row) {
        getIndexColumn(row).setSelect(true);
    }

    public int getRowNumber(String index) {
        return indexToRow.get(index);
    }

    public void removeSubject(String index, String subject) {
        SortedSet<String> subjects = pairStateToSubjects.get(index);
        subjects.remove(subject);
        setSubjects(getRowNumber(index));
    }

    public void clearDistints() {
        for (int i = 0; i < distinct.length; i++) {
            if (distinct[i]) {
                //String index = getIndex(i + 1);
                //indexToRow.remove(index);
                //indexToPairState.remove(index);
                tableLayout.getChildAt(i).setVisibility(View.GONE);
            }
        }
    }

    public void addToSubjects(String index, String subject) {
        SortedSet<String> subjects = pairStateToSubjects.get(index);
        if (subjects == null) {
            subjects = new TreeSet<>();
            subjects.add(subject);
            pairStateToSubjects.put(index, subjects);
            setSubjects(getRowNumber(index));
            return;
        }
        subjects.add(subject);
        setSubjects(getRowNumber(index));
    }

    public SortedSet<String> getSubjects(String index) {
        return pairStateToSubjects.get(index);
    }

    public Pair<State, State> getStatesByIndex(String index) {
        return indexToPairState.get(index);
    }

    public Pair<State, State> getStatesByRow(int row) {
        return indexToPairState.get(getIndex(row));
    }

    public void setDefaultReason(int row) {
        TextView textView = getColumnInRow(row, Column.REASON);
        textView.setText("");
    }

    public void setDefaultDistinct(int row) {
        TextView textView = getColumnInRow(row, Column.DISTINCT);
        distinct[row] = false;
        textView.setText(Symbols.CHECK_MARK);
    }

    public void setReason(int row, String reason) {
        TextView textView = getColumnInRow(row, Column.REASON);
        textView.setText(reason);
        textView.setTextColor(Color.RED);
        Paint paint = textView.getPaint();
        int widthView = (int) paint.measureText(reason) + 5;
        if (widthView > lengthCell - MIN_LENGTH_CELL) {
            lengthCell = widthView + MIN_LENGTH_CELL;
            reloadLengthCells();
        }
    }

    public boolean isDistinctFinalAndNotFinal(int row) {
        Pair<State, State> pairState = indexToPairState.get(getIndex(row));
        if (automaton.isFinalState(pairState.first) && !automaton.isFinalState(pairState.second)) {
            setDistinct(row);
            setReason(row, pairState.first.getName() + " " + Symbols.BELONGS_TO + " F, "
                + pairState.second.getName() + " " + Symbols.NOT_BELONGS_TO + " F");
            return true;
        } else if (!automaton.isFinalState(pairState.first) &&
                automaton.isFinalState(pairState.second)) {
            setDistinct(row);
            setReason(row, pairState.second.getName() + " " + Symbols.BELONGS_TO + " F, "
                    + pairState.first.getName() + " " + Symbols.NOT_BELONGS_TO + " F");
            return true;
        }
        return false;
    }

    public enum Column {

        INDEX,
        DISTINCT,
        SUBJECTS,
        REASON

    }

    public AutomatonMinimizationTable(Activity activity, Automaton automaton) {
        this.activity = activity;
        this.automaton = automaton;
        init();
    }

    /**
     * Cria a tabela
     */
    private void defineTable() {
        tableLayout = new TableLayout(activity);
        tableLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private String biggestStateName(State states[]) {
        String biggestStateName = "";
        for (State state : states) {
            String stateName = state.getName();
            if (stateName.length() > biggestStateName.length()) {
                biggestStateName = stateName;
            }
        }
        return biggestStateName;
    }

    private void updateCellLengthStateName(State states[], Paint paint) {
        String biggestStateName = biggestStateName(states);
        String cellStr = biggestStateName + " " + Symbols.BELONGS_TO + " F, "
                + biggestStateName + " " + Symbols.NOT_BELONGS_TO + " F";
        int length = (int) paint.measureText(cellStr) + 5;
        if (length > lengthCell) {
            lengthCell = length;
        }
    }

    /**
     * Inicializa a tabela de minimização
     */
    private void init() {
        defineTable();
        TableRow row = new TableRow(activity);
        TextView titleTableIndice = getTitleTableTextView("Índice");
        Paint paint = titleTableIndice.getPaint();
        row.addView(titleTableIndice);
        row.addView(getTitleTableTextView("D[i, j] ="));
        row.addView(getTitleTableTextView("S[i, j] ="));
        row.addView(getTitleTableTextView("Motivo"));
        tableLayout.addView(row);
        SortedSet<State> statesSet = automaton.getStates();
        State states[] = statesSet.toArray(new State[statesSet.size()]);
        indexToPairState = new HashMap<>();
        indexToRow = new HashMap<>();
        pairStateToSubjects = new HashMap<>();
        for (int i = 0; i < states.length - 1; i++) {
            for (int j = i + 1; j < states.length; j++) {
                row = new TableRow(activity);
                String index = getIndex(states[i], states[j]);
                indexToPairState.put(index, Pair.create(states[i], states[j]));
                indexToRow.put(index, tableLayout.getChildCount());
                row.addView(getTextViewSelection(index));
                row.addView(getTextViewTableIn(Symbols.CHECK_MARK));
                row.addView(getTextViewTableIn(Symbols.EMPTY_SET));
                row.addView(getTextViewTableIn(""));
                tableLayout.addView(row);
            }
        }
        distinct = new boolean[tableLayout.getChildCount()];
        updateCellLengthStateName(states, paint);
        reloadLengthCells();
    }

    public int getMaxRow() {
        return tableLayout.getChildCount();
    }

    public TableLayout getTableLayout() {
        return tableLayout;
    }

    /**
     * Gera o rótulo do par de estados para a coluna índice da tabela de minimização.
     *
     * @param firstState primeiro estado do par de estados à ser gerado rótulo
     * @param secondState segundo estado do par de estados à ser gerado rótulo
     * @return rótulo do par de estados
     */
    public String getIndex(State firstState, State secondState) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[')
                .append(firstState.getName())
                .append(", ")
                .append(secondState.getName())
                .append(']');
        return stringBuilder.toString();
    }

    /**
     * Gera o rótulo do par de estados para a coluna índice da tabela de minimização.
     *
     * @param statePair par de estados à ser gerado rótulo
     * @return rótulo do par de estados
     */
    public String getIndex(Pair<State, State> statePair) {
        return getIndex(statePair.first, statePair.second);
    }

    public List<Integer> setSubjectsDistincts(String index) {
        List<Integer> rowChanges = new ArrayList<>();
        SortedSet<String> subjects = getSubjects(index);
        if (subjects == null) {
            return rowChanges;
        }
        for (String subject : subjects) {
            if (!isDistinct(subject)){
                int subjectRow = getRowNumber(subject);
                Log.d("distinto subject", getIndex(subjectRow));
                setDistinct(subjectRow);
                setReason(subjectRow, index);
                rowChanges.add(subjectRow);
                rowChanges.addAll(setSubjectsDistincts(subject));
            }
        }
        return rowChanges;
    }

    public TableRow getRow(int row) {
        return (TableRow) tableLayout.getChildAt(row);
    }

    /**
     * Retorna a linha referente ao indice do par de estados
     * @param indice
     * @return
     */
    public TableRow getRow(String indice) {
        Integer row = indexToRow.get(indice);
        if (row == null) {
            return null;
        }
        return getRow(row);
    }

    /**
     * Retorna a TextViewSelection com o indice do par de estados referente a linha informada.
     *
     * @param row linha referente ao indice do par
     * @return TextViewSelection com o indice do par de estados referente a linha informada.
     */
    public SelectedRowTextView getIndexColumn(int row) {
        return (SelectedRowTextView) getRow(row).getChildAt(Column.INDEX.ordinal());
    }

    /**
     * Retorna o TextView de uma determinada linha e coluna da tabela de minimização.
     *
     * @param row linha referente ao TextView
     * @param column coluna referente ao TextView
     * @return TextView localizado na posição indicada
     */
    public TextView getColumnInRow(int row, Column column) {
        return (TextView) getRow(row).getChildAt(column.ordinal());
    }

    public void clearRow(int row) {
        TableRow tableRow = getRow(row);
        ((SelectedRowTextView) tableRow.getChildAt(Column.INDEX.ordinal())).setSelect(false);
        TextView textView = (TextView) tableRow.getChildAt(Column.DISTINCT.ordinal());
        String distinct = textView.getText().toString();
        if (!distinct.equals(Symbols.CHECK_MARK)) {
            distinct = distinct.substring(distinct.length() - 1);
            textView.setText(distinct);
            textView.setTextColor(Color.BLACK);
        }
        ((TextView) tableRow.getChildAt(Column.SUBJECTS.ordinal())).setTextColor(Color.BLACK);
        ((TextView) tableRow.getChildAt(Column.REASON.ordinal())).setTextColor(Color.BLACK);
    }

    public void setDistinct(int row) {
        TextView textView = (TextView) getRow(row).getChildAt(Column.DISTINCT.ordinal());
        String distinct = textView.getText().toString() + " → " + Symbols.CROSS_MARK;
        textView.setText(distinct);
        textView.setTextColor(Color.RED);
        this.distinct[row] = true;
        //indexToPairState.remove(getIndex(row));
    }

    public boolean isDistinct(int row) {
        return distinct[row];
    }

    public boolean isDistinct(String index) {
        return distinct[getRowNumber(index)];
    }

    public String getIndex(int row) {
        return getIndexColumn(row).getText().toString();
    }

    public boolean isRowGone(int row) {
        return getRow(row).getVisibility() == View.GONE;
    }

    public int firstNotGone() {
        int n = tableLayout.getChildCount();
        for (int i = 1; i < n; i++) {
            if (!isRowGone(i)) {
                return i;
            }
        }
        return -1;
    }
    public void setSubjects(int row) {
        TextView textView = (TextView) getRow(row).getChildAt(Column.SUBJECTS.ordinal());
        SortedSet<String> subjects = pairStateToSubjects.get(getIndex(row));
        if (subjects == null || subjects.isEmpty()) {
            textView.setText(Symbols.EMPTY_SET);
            return;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("{ ");
            for (String index : subjects) {
                Pair<State, State> states = indexToPairState.get(index);
                sb.append(getIndex(states))
                        .append(", ");
            }
            sb.deleteCharAt(sb.length() - 2)
                    .append('}');
            textView.setText(sb.toString());
        }
        textView.setTextColor(Color.RED);
        updateLengthCellsValue(textView);
        reloadLengthCells();
    }

    private void updateLengthCellsValue(TextView textView) {
        int widthView = textView.getMeasuredWidth();
        if (widthView > lengthCell) {
            lengthCell = widthView;
        }
    }

    private void reloadLengthCells() {
        int n = tableLayout.getChildCount();
        for (int i = 0; i < n; i++) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
            int n2 = tableRow.getChildCount();
            for (int j = 0; j < n2; j++) {
                TextView textView = (TextView) tableRow.getChildAt(j);
                textView.setWidth(lengthCell);
                textView.invalidate();
            }
        }
    }

    private SelectedRowTextView getTextViewSelection(String label) {
        SelectedRowTextView selectedRowTextView = new SelectedRowTextView(activity);
        selectedRowTextView.setTextColor(Color.BLACK);
        selectedRowTextView.setGravity(Gravity.CENTER);
        selectedRowTextView.setText(label);
        selectedRowTextView.measure(0, 0);
        updateLengthCellsValue(selectedRowTextView);
        selectedRowTextView.setWidth(lengthCell);
        return selectedRowTextView;
    }

    private TitleTableTextView getTitleTableTextView (String label) {
        TitleTableTextView titleTableTextView = new TitleTableTextView(activity);
        titleTableTextView.setTextColor(Color.BLACK);
        titleTableTextView.setGravity(Gravity.CENTER);
        titleTableTextView.setText(label);
        //textViewTitleTable.setTypeface(null, Typeface.BOLD);
        titleTableTextView.measure(0, 0);
        updateLengthCellsValue(titleTableTextView);
        titleTableTextView.setWidth(lengthCell);
        return titleTableTextView;
    }

    private TextView getTextViewTableIn(String label) {
        TextView textView = getTextViewTable(label);
        updateLengthCellsValue(textView);
        textView.setWidth(lengthCell);
        return textView;
    }

    private TextView getTextViewTable(String label) {
        TextView textView = new TextView(activity);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);
        textView.setText(label);
        textView.measure(0, 0);
        return textView;
    }

}
