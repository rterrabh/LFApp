package com.ufla.lfapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.automata.EditAutomataActivity;
import com.ufla.lfapp.activities.grammar.GrammarActivity;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int heighLfappLogo = (int) (metrics.heightPixels * 0.35f);
        ImageView lfappLogoView = (ImageView) findViewById(R.id.imageViewLFAppLogo);
        lfappLogoView.getLayoutParams().height = heighLfappLogo;
        lfappLogoView.requestLayout();
        int heighUflaLogo = (int) (metrics.heightPixels * 0.12f);
        int widthUflaLogo = (int) (metrics.widthPixels * 0.40f);
        ImageView uflaLogoView = (ImageView) findViewById(R.id.imageViewLogoUfla);
        ViewGroup.LayoutParams uflaLogoLayoutParams = uflaLogoView.getLayoutParams();
        uflaLogoLayoutParams.height = heighUflaLogo;
        uflaLogoLayoutParams.width = widthUflaLogo;
        uflaLogoView.requestLayout();
    }


    public void goToStateMachineActivity(View view) {
        Intent intent = new Intent(this, EditAutomataActivity.class);
        startActivity(intent);
    }

    public void goToGrammarActivity(View view) {
        Intent intent = new Intent(this, GrammarActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
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
        //}
    }

}
