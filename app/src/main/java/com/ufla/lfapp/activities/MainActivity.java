package com.ufla.lfapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.ufla.lfapp.R;
import com.ufla.lfapp.vo.GrammarParser;

public class MainActivity extends AppCompatActivity {

    private EditText inputGrammar, inputWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.inputGrammar = (EditText) findViewById(R.id.inputGrammar);
        this.inputWord = (EditText) findViewById(R.id.inputWord);
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        inputGrammar.setText(preferences.getString("inputGrammar", ""));
        inputWord.setText(preferences.getString("inputWord", ""));

        Intent intent = getIntent();
        if (intent != null) {
            Bundle data = intent.getExtras();
            if (data != null) {
                Object grammar = data.get("grammar");
                if(grammar != null) {
                    this.inputGrammar.setText(grammar.toString());
                }
            }
        }
        System.out.println("onCreate-MainActivity");
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        System.out.println("onRestoreInstanceState-MainActivity");
        super.onRestoreInstanceState(savedInstanceState);
        this.inputGrammar.setText(savedInstanceState.getString
                ("inputGrammar"));
        this.inputWord.setText(savedInstanceState.getString
                ("inputWord"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString("inputGrammar", inputGrammar.getText
                ().toString());
        editor.putString("inputWord", inputWord.getText
                ().toString());
        editor.apply();
        System.out.println("onStop-MainActivity");
    }

    @Override
    protected void onDestroy() {
        System.out.println("onDestroy-MainActivity");
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString("inputGrammar", inputGrammar.getText
                ().toString());
        editor.putString("inputWord", inputWord.getText
                ().toString());
        editor.apply();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        System.out.println("onPause-MainActivity");
        super.onPause();
    }

    @Override
    protected void onResume() {
        System.out.println("onResume-MainActivity");
        super.onResume();
    }

    @Override
    protected void onStart() {
        System.out.println("onStart-MainActivity");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        System.out.println("onRestart-MainActivity");
        super.onRestart();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        System.out.println("onSaveInstanceState-MainActivity");
        savedInstanceState.putString("inputGrammar",
                String.valueOf(inputGrammar));
        savedInstanceState.putString("inputWord",
                String.valueOf(inputWord));

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        System.out.println("onBackPressed-MainActivity");

        super.onBackPressed();
    }

    public void insertLambda(View view) {
        int selection = inputGrammar.getSelectionStart();
        inputGrammar.setText(inputGrammar.getText()
                .insert(selection, " λ"));
        inputGrammar.setSelection(selection+2);
    }

    public void insertArrow(View view) {
        int selection = inputGrammar.getSelectionStart();
        inputGrammar.setText(inputGrammar.getText()
                .insert(selection, " -> "));
        inputGrammar.setSelection(selection+4);
    }

    public void insertPipe(View view) {
        int selection = inputGrammar.getSelectionStart();
        inputGrammar.setText(inputGrammar.getText()
                .insert(selection, " | "));
        inputGrammar.setSelection(selection+3);
    }

    public void confirmGrammar(View view) {
        String txtGrammar = inputGrammar.getText().toString();
        String word = inputWord.getText().toString();
        if (!txtGrammar.isEmpty()) {
            StringBuilder reason = new StringBuilder();
            AlertDialog alert;
            if (GrammarParser.verifyInputGrammar(txtGrammar)) {
                if (GrammarParser.inputValidate(txtGrammar, reason)) {
                    Bundle params = new Bundle();
                    params.putString("grammar", txtGrammar);
                    params.putString("word", word);
                    Intent intent = new Intent(this, OutActivity.class);
                    intent.putExtras(params);
                    startActivity(intent);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Aviso");
                    builder.setMessage(reason);
                    builder.setNegativeButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alert = builder.create();
                    alert.show();
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Aviso");
                builder.setMessage("Gramática inválida.");
                builder.setNegativeButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alert = builder.create();
                alert.show();
            }
        }
    }

    public void startAboutActivity(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.actionBack) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
