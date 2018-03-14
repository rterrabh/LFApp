package com.ufla.lfapp.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;

import com.ufla.lfapp.core.machine.Machine;
import com.ufla.lfapp.core.machine.MachineType;
import com.ufla.lfapp.core.machine.dotlang.GraphAdapter;
import com.ufla.lfapp.core.machine.fsa.FSATransitionFunction;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomaton;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomatonDAO;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomatonGUI;
import com.ufla.lfapp.persistence.contract.table.GrammarContract;
import com.ufla.lfapp.persistence.contract.table.GridPositionContract;
import com.ufla.lfapp.persistence.contract.table.MachineContract;
import com.ufla.lfapp.persistence.contract.table.MachineDotLanguageContract;
import com.ufla.lfapp.persistence.contract.table.MachineFinalStateContract;
import com.ufla.lfapp.persistence.contract.table.MachineStatePositionContract;
import com.ufla.lfapp.persistence.contract.table.MachineTransitionContract;
import com.ufla.lfapp.persistence.contract.table.StateContract;
import com.ufla.lfapp.persistence.contract.table.SymbolContract;
import com.ufla.lfapp.persistence.contract.table.TransitionContract;
import com.ufla.lfapp.persistence.contract.view.MachineAlphabetViewContract;
import com.ufla.lfapp.persistence.contract.view.MachineFinalStateViewContract;
import com.ufla.lfapp.persistence.contract.view.MachineStatePositionViewContract;
import com.ufla.lfapp.persistence.contract.view.MachineTransitionViewContract;
import com.ufla.lfapp.core.machine.dotlang.DotLanguage;
import com.ufla.lfapp.core.machine.State;

import java.sql.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.crypto.Mac;

/**
 * Created by carlos on 03/08/16.
 */
public class DbAcess {

    private FeedReaderDbHelper mDbHelper;

    public DbAcess(Context context) {
        mDbHelper = new FeedReaderDbHelper(context);
    }

    private long getGridPositionId(int x, int y) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = { GridPositionContract.Columns._ID };
        String selection = GridPositionContract.Columns.X + " = ? AND "
                + GridPositionContract.Columns.Y + " = ?";
        String[] selectionArgs = { Integer.toString(x), Integer.toString(y) };
        Cursor c = db.query(
                GridPositionContract.TABLE_NAME,              // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                     // The sort order
        );
        long gridPositionId = -1;
        if (c != null) {
            if (c.moveToFirst()) {
                gridPositionId = c.getLong(c.getColumnIndex(GridPositionContract.Columns._ID));
            }
            c.close();
        }
        db.close();
        return gridPositionId;
    }

    private long putGridPosition(Point point) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GridPositionContract.Columns.X, point.x);
        values.put(GridPositionContract.Columns.Y, point.y);

        // Insert the new row, returning the primary key value of the new row
        long gridPositionId = db.insert(GridPositionContract.TABLE_NAME,
                null,
                values);
        db.close();
        return gridPositionId;
    }

    private long getTransitionId(long currentState, long symbol, long futureState) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = { TransitionContract.Columns._ID };
        String selection = TransitionContract.Columns.CURRENT_STATE + " = ? AND "
                + TransitionContract.Columns.SYMBOL + " = ? AND "
                + TransitionContract.Columns.FUTURE_STATE + " = ?" ;
        String[] selectionArgs = { Long.toString(currentState), Long.toString(symbol),
                Long.toString(futureState) };
        Cursor c = db.query(
                TransitionContract.TABLE_NAME,              // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                     // The sort order
        );
        long transitionId = -1;
        if (c != null) {
            if (c.moveToFirst()) {
                transitionId = c.getLong(c.getColumnIndex(TransitionContract.Columns._ID));
            }
            c.close();
        }
        db.close();
        return transitionId;
    }

    private long putTransition(long currentState, long symbol, long futureState) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TransitionContract.Columns.CURRENT_STATE, currentState);
        values.put(TransitionContract.Columns.SYMBOL, symbol);
        values.put(TransitionContract.Columns.FUTURE_STATE, futureState);

        // Insert the new row, returning the primary key value of the new row
        long transitionId = db.insert(TransitionContract.TABLE_NAME,
                null,
                values);
        db.close();
        return transitionId;
    }

    private long getSymbolId(String symbolName) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = { SymbolContract.Columns._ID };
        String selection = SymbolContract.Columns.NAME + " = ?";
        String[] selectionArgs = { symbolName };
        Cursor c = db.query(
                SymbolContract.TABLE_NAME,              // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                     // The sort order
        );
        long symbolId = -1;
        if (c != null) {
            if (c.moveToFirst()) {
                symbolId = c.getLong(c.getColumnIndex(SymbolContract.Columns._ID));
            }
            c.close();
        }
        db.close();
        return symbolId;
    }

    private long putSymbol(String symbolName) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SymbolContract.Columns.NAME, symbolName);

        // Insert the new row, returning the primary key value of the new row
        long symbolId = db.insert(SymbolContract.TABLE_NAME,
                null,
                values);
        db.close();
        return symbolId;
    }

    private long getStateId(State state) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = { StateContract.Columns._ID };
        String selection = StateContract.Columns.NAME + " = ?";
        String[] selectionArgs = { state.getName() };
        Cursor c = db.query(
                StateContract.TABLE_NAME,               // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                     // The sort order
        );
        long stateId = -1;
        if (c != null) {
            if (c.moveToFirst()) {
                stateId = c.getLong(c.getColumnIndex(StateContract.Columns._ID));
            }
            c.close();
        }
        db.close();
        return stateId;
    }

    private long putState(State state) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(StateContract.Columns.NAME, state.getName());

        // Insert the new row, returning the primary key value of the new row
        long stateId = db.insert(StateContract.TABLE_NAME,
               null,
                values);
        db.close();
        return stateId;
    }

    private long putMachineDB(Long initialState, String label, long creationTime, Integer contUses) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MachineContract.Columns.CREATION_TIME, creationTime);
        if (initialState != null) {
            values.put(MachineContract.Columns.INITIAL_STATE, initialState);
        }
        if (label != null) {
            values.put(MachineContract.Columns.LABEL, label);
        }
        if (contUses != null) {
            values.put(MachineContract.Columns.CONT_USES, contUses);
        }

        // Insert the new row, returning the primary key value of the new row
        long machineId = db.insert(MachineContract.TABLE_NAME,
                null,
                values);
        db.close();
        return machineId;
    }

    private long putMachineFinalState(long machineId, long stateId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MachineFinalStateContract.Columns.MACHINE, machineId);
        values.put(MachineFinalStateContract.Columns.STATE, stateId);

        // Insert the new row, returning the primary key value of the new row
        long machineFinalStateId = db.insert(MachineFinalStateContract.TABLE_NAME,
                null,
                values);
        db.close();
        return machineFinalStateId;
    }

    private long putMachineStatePosition(long machineId, long stateId, long positionId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MachineStatePositionContract.Columns.MACHINE, machineId);
        values.put(MachineStatePositionContract.Columns.STATE, stateId);
        values.put(MachineStatePositionContract.Columns.POSITION, positionId);

        // Insert the new row, returning the primary key value of the new row
        long machineStatePositionId = db.insert(MachineStatePositionContract.TABLE_NAME,
                null,
                values);
        db.close();
        return machineStatePositionId;
    }

    private long putMachineTransition(long machineId, long transitionId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MachineTransitionContract.Columns.MACHINE, machineId);
        values.put(MachineTransitionContract.Columns.TRANSITION, transitionId);

        // Insert the new row, returning the primary key value of the new row
        long machineTransitionId = db.insert(MachineTransitionContract.TABLE_NAME,
                null,
                values);
        db.close();
        return machineTransitionId;
    }

    public void putAutomaton(FiniteStateAutomatonDAO automatonDAO) {
        for (State state : automatonDAO.getStates()) {
            long stateId = getStateId(state);
            if (stateId == -1) {
                stateId = putState(state);
            }
            automatonDAO.putStateId(state, stateId);
        }

        State initialState = automatonDAO.getInitialState();
        Long initialStateId = (initialState == null) ? null :
                automatonDAO.getStateId(initialState);
        long machineId = putMachineDB(initialStateId, automatonDAO.getLabel(),
                automatonDAO.getCreationDate().getTime(), automatonDAO.getContUses());
        automatonDAO.setId(machineId);
        for (State finalState : automatonDAO.getFinalStates()) {
            putMachineFinalState(machineId, automatonDAO.getStateId(finalState));
        }
        for (String symbol : automatonDAO.getAlphabet()) {
            long symbolId = getSymbolId(symbol);
            if (symbolId == -1) {
                symbolId = putSymbol(symbol);
            }
            automatonDAO.putSymbolId(symbol, symbolId);
        }
        for (Point gridPosition : automatonDAO.getStateGridPoint().values()) {
            long gridPositionId = getGridPositionId(gridPosition.x, gridPosition.y);
            if (gridPositionId == -1) {
                gridPositionId = putGridPosition(gridPosition);
            }
            automatonDAO.putGridPositionId(gridPosition, gridPositionId);
        }
        for (Map.Entry<State, Point> entry : automatonDAO.getStateGridPoint().entrySet()) {
            putMachineStatePosition(machineId, automatonDAO.getStateId(entry.getKey()),
                    automatonDAO.getGridPositionId(entry.getValue()));
        }
        for (FSATransitionFunction t : automatonDAO.getTransitionFunctions()) {
            long currentState = automatonDAO.getStateId(t.getCurrentState());
            long symbol = automatonDAO.getSymbolId(t.getSymbol());
            long futureState = automatonDAO.getStateId(t.getFutureState());
            long transitionId = getTransitionId(currentState, symbol, futureState);
            if (transitionId == -1) {
                transitionId = putTransition(currentState, symbol, futureState);
            }
            automatonDAO.putTransitionId(t, transitionId);
        }
        for (long transictionId : automatonDAO.getTransitionsId()) {
            putMachineTransition(machineId, transictionId);
        }
    }

    private SortedMap<State, Point> getStatesGridPoints (long machineId) {
        SortedMap<State, Point> stateGridPositions = new TreeMap<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {MachineStatePositionViewContract.Columns.STATE,
                MachineStatePositionViewContract.Columns.X,
                MachineStatePositionViewContract.Columns.Y };
        String selection = MachineStatePositionViewContract.Columns.MACHINE + " = ?";
        String[] selectionArgs = { Long.toString(machineId) };
        Cursor c = db.query(
                MachineStatePositionViewContract.VIEW_NAME, // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                     // The sort order
        );
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    Point p = new Point();
                    p.x = c.getInt(c.getColumnIndex(MachineStatePositionViewContract.Columns.X));
                    p.y = c.getInt(c.getColumnIndex(MachineStatePositionViewContract.Columns.Y));
                    stateGridPositions.put(new State(c.getString(c.getColumnIndex(
                            MachineStatePositionViewContract.Columns.STATE))), p);
                } while (c.moveToNext());
            }
            c.close();
        }
        db.close();
        return stateGridPositions;
    }

    private SortedSet<State> getFinalStates (long machineId) {
        SortedSet<State> finalStates = new TreeSet<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = { MachineFinalStateViewContract.Columns.STATE };
        String selection = MachineFinalStateViewContract.Columns.MACHINE + " = ?";
        String[] selectionArgs = { Long.toString(machineId) };
        Cursor c = db.query(
                MachineFinalStateViewContract.VIEW_NAME,     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                     // The sort order
        );
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    finalStates.add(new State(c.getString(c.getColumnIndex(
                            MachineFinalStateViewContract.Columns.STATE))));
                } while (c.moveToNext());
            }
            c.close();
        }
        db.close();
        return finalStates;
    }

    private SortedSet<String> getAlphabet (long machineId) {
        SortedSet<String> alphabet = new TreeSet<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = { MachineAlphabetViewContract.Columns.ALPHABET };
        String selection = MachineFinalStateViewContract.Columns.MACHINE + " = ?";
        String[] selectionArgs = { Long.toString(machineId) };
        Cursor c = db.query(
                MachineAlphabetViewContract.VIEW_NAME,     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                     // The sort order
        );
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    alphabet.add(c.getString(c.getColumnIndex(
                            MachineAlphabetViewContract.Columns.ALPHABET)));
                } while (c.moveToNext());
            }
            c.close();
        }
        db.close();
        return alphabet;
    }

    private SortedSet<FSATransitionFunction>  getTransitionFunctions (long machineId) {
        SortedSet<FSATransitionFunction> FSATransitionFunctions = new TreeSet<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = { MachineTransitionViewContract.Columns.CURRENT_STATE,
                MachineTransitionViewContract.Columns.SYMBOL,
                MachineTransitionViewContract.Columns.FUTURE_STATE };
        String selection = MachineTransitionViewContract.Columns.MACHINE + " = ?";
        String[] selectionArgs = { Long.toString(machineId) };
        Cursor c = db.query(
                MachineTransitionViewContract.VIEW_NAME,     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                     // The sort order
        );
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    FSATransitionFunctions.add(new FSATransitionFunction(
                            new State(c.getString(c.getColumnIndex(
                                    MachineTransitionViewContract.Columns.CURRENT_STATE))),
                            c.getString(c.getColumnIndex(
                                MachineTransitionViewContract.Columns.SYMBOL)),
                            new State(c.getString(c.getColumnIndex(
                                MachineTransitionViewContract.Columns.FUTURE_STATE)))));
                } while (c.moveToNext());
            }
            c.close();
        }
        db.close();
        return FSATransitionFunctions;
    }

    private class MachineTable {
        long id;
        String initialState;
        String label;
        Date creationDate;
        int contUses;
    }

    private MachineTable getMachineTable(long machineId) {
        MachineTable machineTable = new MachineTable();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = { MachineContract.Columns._ID,
                MachineContract.Columns.INITIAL_STATE,
                MachineContract.Columns.LABEL,
                MachineContract.Columns.CREATION_TIME,
                MachineContract.Columns.CONT_USES };
        String selection = MachineContract.Columns._ID + " = ?";
        String[] selectionArgs = { Long.toString(machineId) };
        Cursor c = db.query(
                MachineContract.TABLE_NAME,     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                     // The sort order
        );
        if (c != null) {
            if (c.moveToFirst()) {
                machineTable.id = c.getLong(c.getColumnIndex(MachineContract.Columns._ID));
                machineTable.initialState = c.getString(c.getColumnIndex(
                            MachineContract.Columns.INITIAL_STATE));
                machineTable.label = c.getString(c.getColumnIndex(MachineContract.Columns.LABEL));
                machineTable.creationDate = new Date(c.getLong(c.getColumnIndex(
                            MachineContract.Columns.CREATION_TIME)));
                machineTable.contUses = c.getInt(c.getColumnIndex(MachineContract.Columns.CONT_USES));
            }
            c.close();
        }
        db.close();
        return machineTable;
    }

    public List<Long> getIdsLastMachine() {
        List<Long> idsLastMachine = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = { MachineContract.Columns._ID };
        Cursor c = db.query(
                MachineContract.TABLE_NAME,     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                MachineContract.Columns._ID + " ASC",     // The sort order
                "10"
        );
        if (c != null) {
            if (c.moveToFirst()) {
                idsLastMachine.add(c.getLong(c.getColumnIndex(MachineContract.Columns._ID)));
            }
            c.close();
        }
        db.close();
        return idsLastMachine;
    }

    public FiniteStateAutomatonGUI getAutomaton(long machineId) {
        SortedMap<State, Point> statesGridPoints = getStatesGridPoints(machineId);
        SortedSet<State> states = new TreeSet<>(statesGridPoints.keySet());
        SortedSet<State> finalStates = getFinalStates(machineId);
        SortedSet<FSATransitionFunction> FSATransitionFunctions = getTransitionFunctions(machineId);
        MachineTable machineTable = getMachineTable(machineId);
        FiniteStateAutomatonGUI automatonGUI = new FiniteStateAutomatonGUI(new FiniteStateAutomaton(states,
                new State(machineTable.initialState), finalStates, FSATransitionFunctions), statesGridPoints);
        automatonGUI.setLabel(machineTable.label);
        automatonGUI.setId(machineTable.id);
        automatonGUI.setContUses(machineTable.contUses);
        automatonGUI.setCreationDate(machineTable.creationDate);
        return automatonGUI;
    }

    public List<FiniteStateAutomatonGUI> getAutomatons() {
        List<FiniteStateAutomatonGUI> automatonGUIs = new ArrayList<>();
        for (Long machineId : getIdsLastMachine()) {
            automatonGUIs.add(getAutomaton(machineId));
        }
        return automatonGUIs;
    }

    public void putAutomatonGUI(FiniteStateAutomatonGUI automatonGUI) {
        putMachineDotLanguage(new DotLanguage(automatonGUI, automatonGUI.getStateGridPositions()));
    }

    public void putMachineDotLanguage(DotLanguage dotLanguage) {
        if (existMachineDotLanguage(dotLanguage.getGraph())) {
            return;
        }
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MachineDotLanguageContract.Columns.DOT_LANGUAGE, dotLanguage.getGraph());
        values.put(MachineDotLanguageContract.Columns.LABEL, dotLanguage.getLabel());
        values.put(MachineDotLanguageContract.Columns.CREATION_TIME,
                Long.toString(dotLanguage.getCreationDate().getTime()));
        values.put(MachineDotLanguageContract.Columns.MACHINE_TYPE,
                dotLanguage.getMachineType().ordinal());
        db.insert(MachineDotLanguageContract.TABLE_NAME,
                null,
                values);
        db.close();
        controlSizeMachineDotLanguage();
    }

    public void controlSizeMachineDotLanguage() {
        Long id = null;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = { MachineDotLanguageContract.Columns._ID,
                MachineDotLanguageContract.Columns.DOT_LANGUAGE,
                MachineDotLanguageContract.Columns.LABEL,
                MachineDotLanguageContract.Columns.CREATION_TIME,
                MachineDotLanguageContract.Columns.CONT_USES };

        Cursor c = db.query(
                MachineDotLanguageContract.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                     // The sort order
        );
        if (c == null) {
            db.close();
            return;
        }
        int n = c.getCount();
        if (n > MAX_SIZE) {
            if (c.moveToFirst()) {
                id = c.getLong(c.getColumnIndex(
                        MachineDotLanguageContract.Columns._ID));
            }
        }
        db.close();
        if (id != null) {
            deleteGrammar(id);
        }
    }

    public FiniteStateAutomatonGUI getAutomatonGUI(long id) {
        DotLanguage dotLanguage = getMachineDotLanguage(id);
        return (dotLanguage == null) ? null : dotLanguage.newToAutomatonGUI();
    }

    public DotLanguage getMachineDotLanguage(long id) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = { MachineDotLanguageContract.Columns._ID,
                MachineDotLanguageContract.Columns.DOT_LANGUAGE,
                MachineDotLanguageContract.Columns.LABEL,
                MachineDotLanguageContract.Columns.CREATION_TIME,
                MachineDotLanguageContract.Columns.CONT_USES,
                MachineDotLanguageContract.Columns.MACHINE_TYPE };
        String selection = MachineContract.Columns._ID + " = ?";
        String[] selectionArgs = { Long.toString(id) };
        Cursor c = db.query(
                MachineDotLanguageContract.TABLE_NAME,     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                     // The sort order
        );
        String graph = null;
        String label = null;
        Date creationDate = null;
        MachineType machineType = null;
        int contUses = 1;
        if (c != null) {
            if (c.moveToFirst()) {
                graph = c.getString(c.getColumnIndex(
                        MachineDotLanguageContract.Columns.DOT_LANGUAGE));
                label = c.getString(c.getColumnIndex(
                        MachineDotLanguageContract.Columns.LABEL));
                creationDate = new Date(c.getLong(c.getColumnIndex(
                        MachineDotLanguageContract.Columns.CREATION_TIME)));
                contUses = c.getInt(c.getColumnIndex(
                        MachineDotLanguageContract.Columns.CONT_USES));
                int indMachineType = c.getInt(c.getColumnIndex(
                        MachineDotLanguageContract.Columns.MACHINE_TYPE));
                try {
                    machineType = MachineType.values()[indMachineType];
                } catch (Exception e) {

                }
            }
            c.close();
        }
        db.close();
        if (graph == null) {
            return null;
        }
        return new DotLanguage(id, graph, label, contUses, creationDate, machineType);
    }

    public boolean existMachineDotLanguage(String graph) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = { MachineDotLanguageContract.Columns._ID };
        String selection = MachineDotLanguageContract.Columns.DOT_LANGUAGE + " = ?";
        String[] selectionArgs = { graph };
        Cursor c = db.query(
                MachineDotLanguageContract.TABLE_NAME,     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                     // The sort order
        );
        boolean exist = false;
        if (c != null) {
            if (c.moveToFirst()) {
                exist = true;
            }
            c.close();
        }
        db.close();
        return exist;
    }

    public FiniteStateAutomatonGUI getAutomatonGUIByLabel(String label) {
        DotLanguage dotLanguage = getMachineDotLanguageByLabel(label);
        return (dotLanguage == null) ? null : dotLanguage.newToAutomatonGUI();
    }

    public DotLanguage getMachineDotLanguageByLabel(String label) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = { MachineDotLanguageContract.Columns._ID,
                MachineDotLanguageContract.Columns.DOT_LANGUAGE,
                MachineDotLanguageContract.Columns.LABEL,
                MachineDotLanguageContract.Columns.CREATION_TIME,
                MachineDotLanguageContract.Columns.CONT_USES,
                MachineDotLanguageContract.Columns.MACHINE_TYPE };
        String selection = MachineDotLanguageContract.Columns.LABEL + " = ?";
        String[] selectionArgs = { label };
        Cursor c = db.query(
                MachineDotLanguageContract.TABLE_NAME,     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                     // The sort order
        );
        String graph = null;
        long id = -1l;
        Date creationDate = null;
        int contUses = 1;
        MachineType machineType = null;
        if (c != null) {
            if (c.moveToFirst()) {
                graph = c.getString(c.getColumnIndex(
                        MachineDotLanguageContract.Columns.DOT_LANGUAGE));
                id = c.getLong(c.getColumnIndex(
                        MachineDotLanguageContract.Columns._ID));
                creationDate = new Date(c.getLong(c.getColumnIndex(
                        MachineDotLanguageContract.Columns.CREATION_TIME)));
                contUses = c.getInt(c.getColumnIndex(
                        MachineDotLanguageContract.Columns.CONT_USES));
                int indMachineType = c.getInt(c.getColumnIndex(
                        MachineDotLanguageContract.Columns.MACHINE_TYPE));
                try {
                    machineType = MachineType.values()[indMachineType];
                } catch (Exception e) {

                }
            }
            c.close();
        }
        db.close();
        if (graph == null) {
            return null;
        }
        return new DotLanguage(id, graph, label, contUses, creationDate, machineType);
    }

    public List<DotLanguage> getMachineDotLanguages() {
        List<DotLanguage> dotLanguages = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = { MachineDotLanguageContract.Columns._ID,
                MachineDotLanguageContract.Columns.DOT_LANGUAGE,
                MachineDotLanguageContract.Columns.LABEL,
                MachineDotLanguageContract.Columns.CREATION_TIME,
                MachineDotLanguageContract.Columns.CONT_USES,
                MachineDotLanguageContract.Columns.MACHINE_TYPE  };

        Cursor c = db.query(
                MachineDotLanguageContract.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                     // The sort order
        );
        if (c != null ) {
            if (c.moveToFirst()) {
                do {
                    String graph = c.getString(c.getColumnIndex(
                            MachineDotLanguageContract.Columns.DOT_LANGUAGE));
                    String label = c.getString(c.getColumnIndex(
                            MachineDotLanguageContract.Columns.LABEL));
                    long id = c.getLong(c.getColumnIndex(
                            MachineDotLanguageContract.Columns._ID));
                    Date creationDate = new Date(c.getLong(c.getColumnIndex(
                            MachineDotLanguageContract.Columns.CREATION_TIME)));
                    int contUses = c.getInt(c.getColumnIndex(
                            MachineDotLanguageContract.Columns.CONT_USES));
                    MachineType machineType = null;
                    int indMachineType = c.getInt(c.getColumnIndex(
                            MachineDotLanguageContract.Columns.MACHINE_TYPE));
                    try {
                        machineType = MachineType.values()[indMachineType];
                    } catch (Exception e) {

                    }
                    dotLanguages.add(new DotLanguage(id, graph, label, contUses, creationDate,
                            machineType));
                    } while (c.moveToNext());
            }
            c.close();
        }
        db.close();
        return dotLanguages;
    }

    public List<FiniteStateAutomatonGUI> getAutomatonGUIs() {
        List<DotLanguage> dotLanguages = getMachineDotLanguages();
        List<FiniteStateAutomatonGUI> automatonGUIs = new ArrayList<>();
        for (DotLanguage dotLanguage : dotLanguages) {
            automatonGUIs.add(dotLanguage.newToAutomatonGUI());
        }
        return automatonGUIs;
    }

    public List<FiniteStateAutomatonGUI> getLastAutomatonGUIs() {
        List<DotLanguage> dotLanguages = getLastMachineDotLanguages();
        List<FiniteStateAutomatonGUI> automatonGUIs = new LinkedList<>();
        for (DotLanguage dotLanguage : dotLanguages) {
            automatonGUIs.add(dotLanguage.newToAutomatonGUI());
        }
        return automatonGUIs;
    }

    public List<DotLanguage> getLastMachineDotLanguages() {
        LinkedList<DotLanguage> dotLanguages = new LinkedList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = { MachineDotLanguageContract.Columns._ID,
                MachineDotLanguageContract.Columns.DOT_LANGUAGE,
                MachineDotLanguageContract.Columns.LABEL,
                MachineDotLanguageContract.Columns.CREATION_TIME,
                MachineDotLanguageContract.Columns.CONT_USES,
                MachineDotLanguageContract.Columns.MACHINE_TYPE };

        Cursor c = db.query(
                MachineDotLanguageContract.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                MachineDotLanguageContract.Columns._ID + " ASC",
                "10"// The sort order
        );
        if (c != null ) {
            if (c.moveToFirst()) {
                do {
                    String graph = c.getString(c.getColumnIndex(
                            MachineDotLanguageContract.Columns.DOT_LANGUAGE));
                    String label = c.getString(c.getColumnIndex(
                            MachineDotLanguageContract.Columns.LABEL));
                    long id = c.getLong(c.getColumnIndex(
                            MachineDotLanguageContract.Columns._ID));
                    Date creationDate = new Date(c.getLong(c.getColumnIndex(
                            MachineDotLanguageContract.Columns.CREATION_TIME)));
                    int contUses = c.getInt(c.getColumnIndex(
                            MachineDotLanguageContract.Columns.CONT_USES));
                    int indMachineType = c.getInt(c.getColumnIndex(
                            MachineDotLanguageContract.Columns.MACHINE_TYPE));
                    MachineType machineType = MachineType.PDA;
                    try {
                        machineType = MachineType.values()[indMachineType];
                    } catch (Exception e) {

                    }
                    dotLanguages.addFirst(new DotLanguage(id, graph, label, contUses, creationDate,
                            machineType));
                } while (c.moveToNext());
            }
            c.close();
        }
        db.close();
        return dotLanguages;
    }

    public List<GraphAdapter> getLastGraph(MachineType machineTypeSel) {
        List<DotLanguage> dotLanguages = getLastMachineDotLanguages(machineTypeSel);
        List<GraphAdapter> graphAdapters = new ArrayList<>();
        for (DotLanguage dotLanguage : dotLanguages) {
            try {
                graphAdapters.add(dotLanguage.toGraphAdapter());
            } catch (Exception e) {
                    e.printStackTrace();
            }
        }
        return graphAdapters;
    }
    public List<DotLanguage> getLastMachineDotLanguages(MachineType machineTypeSel) {
        LinkedList<DotLanguage> dotLanguages = new LinkedList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = { MachineDotLanguageContract.Columns._ID,
                MachineDotLanguageContract.Columns.DOT_LANGUAGE,
                MachineDotLanguageContract.Columns.LABEL,
                MachineDotLanguageContract.Columns.CREATION_TIME,
                MachineDotLanguageContract.Columns.CONT_USES,
                MachineDotLanguageContract.Columns.MACHINE_TYPE };

        String selection = MachineDotLanguageContract.Columns.MACHINE_TYPE + " = ?";
        String[] selectionArgs = { String.valueOf(machineTypeSel.ordinal()) };

        Cursor c = db.query(
                MachineDotLanguageContract.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                MachineDotLanguageContract.Columns._ID + " ASC",
                "10"// The sort order
        );
        if (c != null ) {
            if (c.moveToFirst()) {
                do {
                    String graph = c.getString(c.getColumnIndex(
                            MachineDotLanguageContract.Columns.DOT_LANGUAGE));
                    String label = c.getString(c.getColumnIndex(
                            MachineDotLanguageContract.Columns.LABEL));
                    long id = c.getLong(c.getColumnIndex(
                            MachineDotLanguageContract.Columns._ID));
                    Date creationDate = new Date(c.getLong(c.getColumnIndex(
                            MachineDotLanguageContract.Columns.CREATION_TIME)));
                    int contUses = c.getInt(c.getColumnIndex(
                            MachineDotLanguageContract.Columns.CONT_USES));
                    int indMachineType = c.getInt(c.getColumnIndex(
                            MachineDotLanguageContract.Columns.MACHINE_TYPE));
                    MachineType machineType = MachineType.PDA;
                    try {
                        machineType = MachineType.values()[indMachineType];
                    } catch (Exception e) {

                    }
                    dotLanguages.addFirst(new DotLanguage(id, graph, label, contUses, creationDate,
                            machineType));
                } while (c.moveToNext());
            }
            c.close();
        }
        db.close();
        return dotLanguages;
    }

    public List<FiniteStateAutomatonGUI> getAutomatonGUIsOnPage(int page, int pageLength) {
        List<DotLanguage> dotLanguages = getMachineDotLanguagesOnPage(page, pageLength);
        List<FiniteStateAutomatonGUI> automatonGUIs = new ArrayList<>();
        for (DotLanguage dotLanguage : dotLanguages) {
            automatonGUIs.add(dotLanguage.newToAutomatonGUI());
        }
        return automatonGUIs;
    }

    public List<DotLanguage> getMachineDotLanguagesOnPage(int page, int pageLength) {
        List<DotLanguage> dotLanguages = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = { MachineDotLanguageContract.Columns._ID,
                MachineDotLanguageContract.Columns.DOT_LANGUAGE,
                MachineDotLanguageContract.Columns.LABEL,
                MachineDotLanguageContract.Columns.CREATION_TIME,
                MachineDotLanguageContract.Columns.CONT_USES,
                MachineDotLanguageContract.Columns.MACHINE_TYPE };

        int offset = page * pageLength;
        Cursor c = db.query(
                MachineDotLanguageContract.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                MachineDotLanguageContract.Columns._ID + " ASC",
                Integer.toString(offset) + ", " + Integer.toString(pageLength)
                // The sort order
        );
        if (c != null ) {
            if (c.moveToFirst()) {
                do {
                    String graph = c.getString(c.getColumnIndex(
                            MachineDotLanguageContract.Columns.DOT_LANGUAGE));
                    String label = c.getString(c.getColumnIndex(
                            MachineDotLanguageContract.Columns.LABEL));
                    long id = c.getLong(c.getColumnIndex(
                            MachineDotLanguageContract.Columns._ID));
                    Date creationDate = new Date(c.getLong(c.getColumnIndex(
                            MachineDotLanguageContract.Columns.CREATION_TIME)));
                    int contUses = c.getInt(c.getColumnIndex(
                            MachineDotLanguageContract.Columns.CONT_USES));
                    int indMachineType = c.getInt(c.getColumnIndex(
                            MachineDotLanguageContract.Columns.MACHINE_TYPE));
                    MachineType machineType = null;
                    try {
                        machineType = MachineType.values()[indMachineType];
                    } catch (Exception e) {

                    }
                    dotLanguages.add(new DotLanguage(id, graph, label, contUses, creationDate,
                            machineType));
                } while (c.moveToNext());
            }
            c.close();
        }
        db.close();
        return dotLanguages;
    }

    public void putGrammar(String grammar) {
        if (existGrammar(grammar)) {
            return;
        }
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(GrammarContract.Columns.COLUMN_GRAMMAR_GRAMMAR, grammar);

        // Insert the new row, returning the primary key value of the new row
       db.insert(GrammarContract.Columns.TABLE_GRAMMAR,
                GrammarContract.Columns.COLUMN_GRAMMAR_NULLABLE,
                values);
        db.close();
        controlSizeGrammar();
    }

    public boolean existGrammar(String grammar) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                GrammarContract.Columns.COLUMN_GRAMMAR_ID
        };
        String selection = GrammarContract.Columns.COLUMN_GRAMMAR_GRAMMAR + " = ?";
        String[] selectionArgs = { grammar };
        Cursor c = db.query(
                GrammarContract.Columns.TABLE_GRAMMAR,     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                     // The sort order
        );
        boolean exist = false;
        if (c != null) {
            if (c.moveToFirst()) {
                exist = true;
            }
            c.close();
        }
        db.close();
        return exist;
    }

    private static int MAX_SIZE = 200;

    public void controlSizeGrammar() {
        Integer id = null;
        String grammar = null;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                GrammarContract.Columns.COLUMN_GRAMMAR_ID,
                GrammarContract.Columns.COLUMN_GRAMMAR_GRAMMAR,
//                GrammarContract.Columns.COLUMN_NAME_UPDATED
        };

        Cursor c = db.query(
                GrammarContract.Columns.TABLE_GRAMMAR,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                     // The sort order
        );
        int n = c.getCount();
        if (n > MAX_SIZE) {
            if (c.moveToFirst()) {
                int columnIndexGrammar = c.getColumnIndex
                        (GrammarContract.Columns.COLUMN_GRAMMAR_GRAMMAR);
                int columnIndexId = c.getColumnIndex(GrammarContract.Columns.COLUMN_GRAMMAR_ID);
                id = c.getInt(columnIndexId);
                grammar = c.getString(columnIndexGrammar);
            }
        }
        db.close();
        if (id != null) {
            deleteGrammar(id);
        }
    }

    public Map<String, Integer> readGrammars() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                GrammarContract.Columns.COLUMN_GRAMMAR_ID,
                GrammarContract.Columns.COLUMN_GRAMMAR_GRAMMAR,
//                GrammarContract.Columns.COLUMN_NAME_UPDATED
        };

        Cursor c = db.query(
                GrammarContract.Columns.TABLE_GRAMMAR,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                     // The sort order
        );
        int n = c.getCount();
        Deque<String> stackGrammar = new ArrayDeque<>(n);
        Deque<Integer> stackId = new ArrayDeque<>(n);
        if (c != null ) {
            if (c.moveToFirst()) {
                int columnIndexGrammar = c.getColumnIndex
                        (GrammarContract.Columns.COLUMN_GRAMMAR_GRAMMAR);
                int columnIndexId = c.getColumnIndex(GrammarContract.Columns.COLUMN_GRAMMAR_ID);
                do {
                    stackGrammar.push(c.getString(columnIndexGrammar));
                    stackId.push(c.getInt(columnIndexId));
                } while (c.moveToNext());
            }
            c.close();
        }
        db.close();
        Map<String, Integer> result = new LinkedHashMap<>();
        while(!stackGrammar.isEmpty()) {
            result.put(stackGrammar.pop(), stackId.pop());
        }
        return result;
    }

    public void deleteAutomaton(Long id) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.execSQL("PRAGMA foreign_keys = ON;");

        // Define 'where' part of query.
        String selection = MachineContract.Columns._ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { Long.toString(id) };
        // Issue SQL statement.
        db.delete(MachineContract.TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    public void cleanHistoryAutomaton() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON;");
        db.delete(MachineContract.TABLE_NAME, null, null);
        db.close();
    }

    public void deleteMachineDotLanguage(long id) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Define 'where' part of query.
        String selection = MachineDotLanguageContract.Columns._ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { Long.toString(id) };
        // Issue SQL statement.
        db.delete(MachineDotLanguageContract.TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    public boolean updateMachineDotLanguage(FiniteStateAutomatonGUI automatonGUI) {
        return updateMachineDotLanguage(new DotLanguage(automatonGUI,
                automatonGUI.getStateGridPositions()));
    }

    public boolean updateMachineDotLanguage(DotLanguage dotLanguage) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Define 'where' part of query.
        String selection = MachineDotLanguageContract.Columns._ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { Long.toString(dotLanguage.getId()) };
        // Issue SQL statement.
        ContentValues values = new ContentValues();
        values.put(MachineDotLanguageContract.Columns.DOT_LANGUAGE, dotLanguage.getGraph());
        values.put(MachineDotLanguageContract.Columns.LABEL, dotLanguage.getLabel());
        values.put(MachineDotLanguageContract.Columns.CREATION_TIME,
                Long.toString(dotLanguage.getCreationDate().getTime()));
        int rows = db.update(MachineDotLanguageContract.TABLE_NAME, values, selection, selectionArgs);
        db.close();
        return rows > 0;
    }

    public void cleanHistoryMachineDotLanguage() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(MachineDotLanguageContract.TABLE_NAME, null, null);
        db.close();
    }

    public void deleteGrammar(long id) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Define 'where' part of query.
        String selection = GrammarContract.Columns.COLUMN_GRAMMAR_ID
                + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(id) };
        // Issue SQL statement.
        db.delete(GrammarContract.Columns.TABLE_GRAMMAR, selection, selectionArgs);
        db.close();
    }

    public void cleanHistoryGrammar() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(GrammarContract.Columns.TABLE_GRAMMAR, null, null);
        db.close();
    }


}
