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
import com.ufla.lfapp.activities.utils.Algorithm;
import com.ufla.lfapp.vo.GrammarParser;

public class MainActivity extends AppCompatActivity {

    private EditText inputGrammar, inputWord;
//    private LFAppKeyboard mCustomKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.inputGrammar = (EditText) findViewById(R.id.inputGrammar);
        this.inputWord = (EditText) findViewById(R.id.inputWord);
//        mCustomKeyboard= new LFAppKeyboard(this, R.id.lfappKeyboardView, R.xml.lfapp_keyboard);
//        mCustomKeyboard.registerEditText(R.id.inputGrammar);
//        mCustomKeyboard.registerEditText(R.id.inputWord);
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        inputGrammar.setText(preferences.getString("inputGrammar", ""));
        inputWord.setText(preferences.getString("inputWord", ""));
    }

    @Override
    protected void onStop() {
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString("inputGrammar", inputGrammar.getText
                ().toString());
        editor.putString("inputWord", inputWord.getText
                ().toString());
        editor.apply();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString("inputGrammar", inputGrammar.getText
                ().toString());
        editor.putString("inputWord", inputWord.getText
                ().toString());
        editor.apply();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
//        if(mCustomKeyboard.isLFAppKeyboardVisible()) {
//            mCustomKeyboard.hideLFAppKeyboard();
//        } else {
//            super.onBackPressed();
//        }
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.cancel();
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.cancel();
                        break;
                }
            }
        };
        //Solicita confirmação de saída do lfapp
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sair");
        builder.setMessage("Sair do LFApp?");
        builder.setPositiveButton("Sim", dialogClickListener);
        builder.setNegativeButton("Não", dialogClickListener);
        builder.show();
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
            if (GrammarParser.verifyInputGrammar(txtGrammar) &&
                    GrammarParser.inputValidate(txtGrammar, reason)) {
                Bundle params = new Bundle();
                params.putString("grammar", txtGrammar);
                params.putString("word", word);
                params.putInt("algorithm", Algorithm.NONE.getValue());
                Intent intent = new Intent(this, MenuActivity.class);
                intent.putExtras(params);
                startActivity(intent);
            } else {
                if(reason.length() == 0) {
                    reason.append("Gramática inválida.");
                }
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
                builder.show();
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
