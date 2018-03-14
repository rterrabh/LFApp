package com.ufla.lfapp.activities.grammar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.AboutActivity;
import com.ufla.lfapp.activities.AppCompatActivityContext;
import com.ufla.lfapp.activities.grammar.menu.MenuActivity;
import com.ufla.lfapp.utils.Algorithm;
import com.ufla.lfapp.persistence.DbAcess;
import com.ufla.lfapp.core.grammar.GrammarParser;
import com.ufla.lfapp.utils.ResourcesContext;

public class GrammarActivity extends AppCompatActivityContext {

    protected static final String GRAMMAR_EXTRA = "grammar";
    protected static final String WORD_EXTRA = "word";
    protected static final String ALGORITHM_EXTRA = "algorithm";
    protected static final String INPUT_GRAMMAR_PREFERENCES = "inputGrammar";
    protected static final String INPUT_WORD_PREFERENCES = "inputWord";

    private RelativeLayout screenGrammar;
    private LinearLayout form, buttons;
    private EditText inputGrammar, inputWord;
    private View historical, about, labelGrammar, labelWord, insertLambda, insertPipe,
    insertArrow, confirmGrammar;
    private boolean isInputGrammarOnFocus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float dpi = metrics.density;
        setContentView(R.layout.activity_main);
        screenGrammar = (RelativeLayout) findViewById(R.id.screenGrammar);
        form = (LinearLayout) findViewById(R.id.form);
        buttons = (LinearLayout) findViewById(R.id.buttons);
        labelGrammar = findViewById(R.id.labelGrammar);
        labelWord = findViewById(R.id.labelWord);
        inputGrammar = (EditText) findViewById(R.id.inputGrammar);
        inputWord = (EditText) findViewById(R.id.inputWord);
        insertLambda = findViewById(R.id.insertLambda);
        insertPipe = findViewById(R.id.insertPipe);
        insertArrow = findViewById(R.id.insertArrow);
        confirmGrammar = findViewById(R.id.confirmGrammar);
        historical = findViewById(R.id.historical);
        about = findViewById(R.id.about);
        getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_ADJUST_RESIZE);
        screenGrammar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = screenGrammar.getRootView().getHeight() - screenGrammar.getHeight();
                int contentViewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getHeight();
                boolean isShowKeyboard = heightDiff > contentViewTop;
                if (isShowKeyboard && inputGrammar.hasFocus()) {
                    historical.setVisibility(View.GONE);
                    about.setVisibility(View.GONE);
                    labelWord.setVisibility(View.GONE);
                    inputWord.setVisibility(View.GONE);
                } else {
                    historical.setVisibility(View.VISIBLE);
                    about.setVisibility(View.VISIBLE);
                    labelWord.setVisibility(View.VISIBLE);
                    inputWord.setVisibility(View.VISIBLE);
                }
                screenGrammar.invalidate();
            }
        });
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        inputGrammar.setText(preferences.getString(INPUT_GRAMMAR_PREFERENCES, ""));
        inputWord.setText(preferences.getString(INPUT_WORD_PREFERENCES, ""));
        Intent intent = getIntent();
        if (intent != null
                && intent.getExtras() != null
                && intent.getExtras().getString(GRAMMAR_EXTRA) != null) {
            inputGrammar.setText(intent.getExtras().getString(GRAMMAR_EXTRA));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString(INPUT_GRAMMAR_PREFERENCES, inputGrammar.getText().toString());
        editor.putString(INPUT_WORD_PREFERENCES, inputWord.getText().toString());
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString(INPUT_GRAMMAR_PREFERENCES, inputGrammar.getText().toString());
        editor.putString(INPUT_WORD_PREFERENCES, inputWord.getText().toString());
        editor.apply();
    }


    public void insertLambda(View view) {
        int selection = inputGrammar.getSelectionStart();
        inputGrammar.setText(inputGrammar.getText()
                .insert(selection, " λ"));
        inputGrammar.setSelection(selection + 2);
    }

    public void insertArrow(View view) {
        int selection = inputGrammar.getSelectionStart();
        inputGrammar.setText(inputGrammar.getText()
                .insert(selection, " -> "));
        inputGrammar.setSelection(selection + 4);
    }

    public void insertPipe(View view) {
        int selection = inputGrammar.getSelectionStart();
        inputGrammar.setText(inputGrammar.getText()
                .insert(selection, " | "));
        inputGrammar.setSelection(selection + 3);
    }

    public void clear(View view) {
        inputGrammar.getText().clear();
    }

    public void confirmGrammar(View view) {
        String txtGrammar = inputGrammar.getText().toString();
        String word = inputWord.getText().toString();
        //Toast.makeText(this, "oi", Toast.LENGTH_LONG);
        if (!txtGrammar.isEmpty()) {
            StringBuilder reason = new StringBuilder();
            Toast.makeText(this, txtGrammar, Toast.LENGTH_LONG);
            if (GrammarParser.verifyInputGrammar(txtGrammar) &&
                    GrammarParser.inputValidate(txtGrammar, reason)) {
                new DbAcess(this).putGrammar(txtGrammar);
                Bundle params = new Bundle();
                params.putString(GRAMMAR_EXTRA, txtGrammar);
                params.putString(WORD_EXTRA, word);
                params.putInt(ALGORITHM_EXTRA, Algorithm.NONE.getValue());
                Intent intent = new Intent(this, MenuActivity.class);
                intent.putExtras(params);
                startActivity(intent);
            } else {
                if (reason.length() == 0) {
                    reason.append(ResourcesContext.getString(R.string.exception_invalid_grammar));
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.warning);
                builder.setMessage(reason);
                builder.setNegativeButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();
            }
        }
    }

    public void startAboutActivity(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void startHistoricalGrammarsActivity(View view) {
        Intent intent = new Intent(this, HistoricalGrammarsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
