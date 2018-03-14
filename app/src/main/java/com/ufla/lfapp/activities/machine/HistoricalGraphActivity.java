package com.ufla.lfapp.activities.machine;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.AppCompatActivityContext;
import com.ufla.lfapp.activities.machine.fsa.ArrayAdapterAutomata;
import com.ufla.lfapp.activities.machine.fsa.ArrayAdapterGraph;
import com.ufla.lfapp.activities.machine.fsa.HistoricalAutomataActivity;
import com.ufla.lfapp.core.machine.MachineType;
import com.ufla.lfapp.core.machine.dotlang.Edge;
import com.ufla.lfapp.core.machine.dotlang.GraphAdapter;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomatonGUI;
import com.ufla.lfapp.persistence.DbAcess;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;

/**
 * Created by carlos on 15/07/17.
 */

public class HistoricalGraphActivity extends AppCompatActivityContext  {

    public static final String MACHINE_TYPE_SEL = "machineType";
    private List<GraphAdapter> graphAdapters;
    private List<String> graphLAbels;
    private ArrayAdapter listAdapterGraph;
    private int pageLength = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_automata);
        Intent intent = getIntent();
        if (intent != null
                && intent.getSerializableExtra(MACHINE_TYPE_SEL) != null) {
            try {
                graphAdapters = new DbAcess(HistoricalGraphActivity.this)
                        .getLastGraph((MachineType)
                                intent.getSerializableExtra(MACHINE_TYPE_SEL));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //Log.d("initial load", "" + graphAdapters.size());
        graphLAbels = new ArrayList<>();
        for (GraphAdapter graphAdapter : graphAdapters) {
            graphLAbels.add(graphAdapter.dotLanguage.getLabel());
        }
        listAdapterGraph = new ArrayAdapterGraph(this, graphAdapters);
        ListView listAutomaton = (ListView) findViewById(R.id.automatons);
        listAutomaton.setAdapter(listAdapterGraph);
//        listAutomaton.setOnScrollListener(new EndlessScrollListener() {
//            @Override
//            public boolean onLoadMore(int page, int totalItemsCount) {
//                // Triggered only when new data needs to be appended to the list
//                // Add whatever code is needed to append new items to your AdapterView
//                //Log.d("itens, page, pageLenght", "" + totalItemsCount + ", " + page + ", "
//                + HistoricalAutomataActivity.this.pageLength);
//                List<FiniteStateAutomatonGUI> loadedAutomatonGUIs =
//                        new DbAcess(HistoricalAutomataActivity.this)
//                                .getAutomatonGUIsOnPage(page, HistoricalAutomataActivity.this.pageLength);
//
//                //Log.d("loaded itens", "" + loadedAutomatonGUIs.size());
//                if (loadedAutomatonGUIs.isEmpty()) {
//                    return false;
//                }
//                for (FiniteStateAutomatonGUI automatonGUI: loadedAutomatonGUIs) {
//                    HistoricalAutomataActivity.this.graphLAbels.add(automatonGUI.getLabel());
//                }
//                HistoricalAutomataActivity.this.listAdapterGraph.addAll(loadedAutomatonGUIs);
//                HistoricalAutomataActivity.this.listAdapterGraph.notifyDataSetChanged();
//                // or loadNextDataFromApi(totalItemsCount);
//                return true; // ONLY if more data is actually being loaded; false otherwise.
//            }
//        });
    }


    public GraphAdapter getGraphWithId(long id) {
        for (GraphAdapter graphAdapter : graphAdapters) {
            if (graphAdapter.dotLanguage.getId() == id) {
                return graphAdapter;
            }
        }
        return null;
    }

    private void update() {
        DbAcess dbAcess = new DbAcess(HistoricalGraphActivity.this);
        for (int i = 0; i < graphAdapters.size(); i++) {
            String newLabel = graphAdapters.get(i).dotLanguage.getLabel();
            if (newLabel != null && !newLabel.equals(graphLAbels.get(i))) {
                dbAcess.updateMachineDotLanguage(graphAdapters.get(i).dotLanguage);
            }
        }
                /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                DbAcess dbAcess = new DbAcess(HistoricalAutomataActivity.this);
                for (int i = 0; i < graphAdapters.size(); i++) {
                    String newLabel = graphAdapters.get(i).getIndex();
                    if (!newLabel.equals(graphLAbels.get(i))) {
                        dbAcess.updateMachineDotLanguage(graphAdapters.get(i));
                    }
                }
            }
        }).start();*/
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        update();
        super.onDestroy();
    }


    public void cleanHistoricalAutomaton (View view) {
        listAdapterGraph.clear();
        new DbAcess(this).cleanHistoryMachineDotLanguage();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_historical_automata, menu);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
