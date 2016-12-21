package com.ufla.lfapp.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;

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
import com.ufla.lfapp.vo.machine.Automaton;
import com.ufla.lfapp.vo.machine.AutomatonDAO;
import com.ufla.lfapp.vo.machine.AutomatonGUI;
import com.ufla.lfapp.vo.machine.DotLanguage;
import com.ufla.lfapp.vo.machine.TransitionFunction;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by carlos on 03/08/16.
 */
public class DbAcess {

    private  FeedReaderDbHelper mDbHelper;

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

    private long getStateId(String stateName) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = { StateContract.Columns._ID };
        String selection = StateContract.Columns.NAME + " = ?";
        String[] selectionArgs = { stateName };
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

    private long putState(String stateName) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(StateContract.Columns.NAME, stateName);

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

    public void putAutomaton(AutomatonDAO automatonDAO) {
        for (String state : automatonDAO.getStates()) {
            long stateId = getStateId(state);
            if (stateId == -1) {
                stateId = putState(state);
            }
            automatonDAO.putStateId(state, stateId);
        }

        String initialState = automatonDAO.getInitialState();
        Long initialStateId = (initialState == null) ? null :
                automatonDAO.getStateId(initialState);
        long machineId = putMachineDB(initialStateId, automatonDAO.getLabel(),
                automatonDAO.getCreationDate().getTime(), automatonDAO.getContUses());
        automatonDAO.setId(machineId);
        for (String finalState : automatonDAO.getFinalStates()) {
            putMachineFinalState(machineId, automatonDAO.getStateId(finalState));
        }
        for (String symbol : automatonDAO.getAlphabet()) {
            long symbolId = getSymbolId(symbol);
            if (symbolId == -1) {
                symbolId = putSymbol(symbol);
            }
            automatonDAO.putSymbolId(symbol, symbolId);
        }
        for (Point gridPosition : automatonDAO.getStateGridPositions().values()) {
            long gridPositionId = getGridPositionId(gridPosition.x, gridPosition.y);
            if (gridPositionId == -1) {
                gridPositionId = putGridPosition(gridPosition);
            }
            automatonDAO.putGridPositionId(gridPosition, gridPositionId);
        }
        for (Map.Entry<String, Point> entry : automatonDAO.getStateGridPositions().entrySet()) {
            putMachineStatePosition(machineId, automatonDAO.getStateId(entry.getKey()),
                    automatonDAO.getGridPositionId(entry.getValue()));
        }
        for (TransitionFunction t : automatonDAO.getTransitionFunctions()) {
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

    private Map<String, Point> getStatesGridPoints (long machineId) {
        Map<String, Point> stateGridPositions = new HashMap<>();
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
                    stateGridPositions.put(c.getString(c.getColumnIndex(
                            MachineStatePositionViewContract.Columns.STATE)), p);
                } while (c.moveToNext());
            }
            c.close();
        }
        db.close();
        return stateGridPositions;
    }

    private SortedSet<String> getFinalStates (long machineId) {
        SortedSet<String> finalStates = new TreeSet<>();
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
                    finalStates.add(c.getString(c.getColumnIndex(
                            MachineFinalStateViewContract.Columns.STATE)));
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

    private SortedSet<TransitionFunction>  getTransitionFunctions (long machineId) {
        SortedSet<TransitionFunction> transitionFunctions = new TreeSet<>();
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
                    transitionFunctions.add(new TransitionFunction(
                            c.getString(c.getColumnIndex(
                                    MachineTransitionViewContract.Columns.CURRENT_STATE)),
                            c.getString(c.getColumnIndex(
                                MachineTransitionViewContract.Columns.SYMBOL)),
                            c.getString(c.getColumnIndex(
                                MachineTransitionViewContract.Columns.FUTURE_STATE))));
                } while (c.moveToNext());
            }
            c.close();
        }
        db.close();
        return transitionFunctions;
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

    public AutomatonGUI getAutomaton(long machineId) {
        Map<String, Point> statesGridPoints = getStatesGridPoints(machineId);
        SortedSet<String> states = new TreeSet<>(statesGridPoints.keySet());
        SortedSet<String> finalStates = getFinalStates(machineId);
        SortedSet<TransitionFunction> transitionFunctions = getTransitionFunctions(machineId);
        MachineTable machineTable = getMachineTable(machineId);
        AutomatonGUI automatonGUI = new AutomatonGUI(new Automaton(states,
                machineTable.initialState, finalStates, transitionFunctions), statesGridPoints);
        automatonGUI.setLabel(machineTable.label);
        automatonGUI.setId(machineTable.id);
        automatonGUI.setContUses(machineTable.contUses);
        automatonGUI.setCreationDate(machineTable.creationDate);
        return automatonGUI;
    }

    public List<AutomatonGUI> getAutomatons() {
        List<AutomatonGUI> automatonGUIs = new ArrayList<>();
        for (Long machineId : getIdsLastMachine()) {
            automatonGUIs.add(getAutomaton(machineId));
        }
        return automatonGUIs;
    }

    public void putAutomatonGUI(AutomatonGUI automatonGUI) {
        putMachineDotLanguage(DotLanguage.parseDotLanguage(automatonGUI));
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
        db.insert(MachineDotLanguageContract.TABLE_NAME,
                null,
                values);
        db.close();
    }

    public AutomatonGUI getAutomatonGUI(long id) {
        DotLanguage dotLanguage = getMachineDotLanguage(id);
        return (dotLanguage == null) ? null : dotLanguage.toAutomatonGUI();
    }

    public DotLanguage getMachineDotLanguage(long id) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = { MachineDotLanguageContract.Columns._ID,
                MachineDotLanguageContract.Columns.DOT_LANGUAGE,
                MachineDotLanguageContract.Columns.LABEL,
                MachineDotLanguageContract.Columns.CREATION_TIME,
                MachineDotLanguageContract.Columns.CONT_USES };
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
            }
            c.close();
        }
        db.close();
        if (graph == null) {
            return null;
        }
        return new DotLanguage(id, graph, label, contUses, creationDate);
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

    public AutomatonGUI getAutomatonGUIByLabel(String label) {
        DotLanguage dotLanguage = getMachineDotLanguageByLabel(label);
        return (dotLanguage == null) ? null : dotLanguage.toAutomatonGUI();
    }

    public DotLanguage getMachineDotLanguageByLabel(String label) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = { MachineDotLanguageContract.Columns._ID,
                MachineDotLanguageContract.Columns.DOT_LANGUAGE,
                MachineDotLanguageContract.Columns.LABEL,
                MachineDotLanguageContract.Columns.CREATION_TIME,
                MachineDotLanguageContract.Columns.CONT_USES };
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
            }
            c.close();
        }
        db.close();
        if (graph == null) {
            return null;
        }
        return new DotLanguage(id, graph, label, contUses, creationDate);
    }

    public List<DotLanguage> getMachineDotLanguages() {
        List<DotLanguage> dotLanguages = new ArrayList<>();
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
                    dotLanguages.add(new DotLanguage(id, graph, label, contUses, creationDate));
                    } while (c.moveToNext());
            }
            c.close();
        }
        db.close();
        return dotLanguages;
    }

    public List<AutomatonGUI> getAutomatonGUIs() {
        List<DotLanguage> dotLanguages = getMachineDotLanguages();
        List<AutomatonGUI> automatonGUIs = new ArrayList<>();
        for (DotLanguage dotLanguage : dotLanguages) {
            automatonGUIs.add(dotLanguage.toAutomatonGUI());
        }
        return automatonGUIs;
    }


    public List<AutomatonGUI> getLastAutomatonGUIs() {
        List<DotLanguage> dotLanguages = getLastMachineDotLanguages();
        List<AutomatonGUI> automatonGUIs = new ArrayList<>();
        for (DotLanguage dotLanguage : dotLanguages) {
            automatonGUIs.add(dotLanguage.toAutomatonGUI());
        }
        return automatonGUIs;
    }

    public List<DotLanguage> getLastMachineDotLanguages() {
        List<DotLanguage> dotLanguages = new ArrayList<>();
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
                    dotLanguages.add(new DotLanguage(id, graph, label, contUses, creationDate));
                } while (c.moveToNext());
            }
            c.close();
        }
        db.close();
        return dotLanguages;
    }

    public void putGrammar(String grammar) {
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
        Map<String, Integer> result = new HashMap<>();
        if (c != null ) {
            if (c.moveToFirst()) {
                int columnIndexGrammar = c.getColumnIndex
                        (GrammarContract.Columns.COLUMN_GRAMMAR_GRAMMAR);
                int columnIndexId = c.getColumnIndex(GrammarContract.Columns.COLUMN_GRAMMAR_ID);
                do {
                    result.put(c.getString(columnIndexGrammar), c.getInt(columnIndexId));
                } while (c.moveToNext());
            }
            c.close();
        }

        db.close();
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

    public boolean updateMachineDotLanguage(AutomatonGUI automatonGUI) {
        return updateMachineDotLanguage(DotLanguage.parseDotLanguage(automatonGUI));
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
