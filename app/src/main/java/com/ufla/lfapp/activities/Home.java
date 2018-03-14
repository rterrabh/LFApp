package com.ufla.lfapp.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.machine.pda.EditPushdownAutomatonActivity;
import com.ufla.lfapp.activities.machine.tm.EditTMEnumActivity;
import com.ufla.lfapp.activities.machine.tm.EditTMMultiTapesActivity;
import com.ufla.lfapp.activities.machine.tm.EditTMMultiTracksActivity;
import com.ufla.lfapp.activities.machine.tm.EditTuringMachineActivity;
import com.ufla.lfapp.activities.machine.fsa.EditFiniteStateAutomatonActivity;
import com.ufla.lfapp.activities.grammar.GrammarActivity;

import java.util.Map;

public class Home extends AppCompatActivityContext {

    public static boolean CLEAR_PREFERENCES = true;

    public void clearPreferences(Context context) {
        String[] preferencesFiles = new String[] {
                "EditFiniteStateAutomatonActivity",
                "EditPushdownAutomatonActivity",
                "EditTMEnumActivity",
                "EditTMMultiTapesActivity",
                "EditTMMultiTracksActivity",
                "EditTuringMachineActivity",
                "EditTuringMachineActivity",
                "EditTuringMachineActivity",
                "GrammarActivity"
        };

        for (String prefFile : preferencesFiles) {
            System.out.println(context.getSharedPreferences(
                    prefFile, MODE_PRIVATE).edit().clear().commit());
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (CLEAR_PREFERENCES) {
            clearPreferences(this);
            CLEAR_PREFERENCES = false;
        }
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
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.mainConstraintLayout);
        constraintLayout.requestLayout();
    }


    public void goToFSAActivity(View view) {
        Intent intent = new Intent(this, EditFiniteStateAutomatonActivity.class);
        startActivity(intent);
    }

    public void goToPDAActivity(View view) {
        Intent intent = new Intent(this, EditPushdownAutomatonActivity.class);
        startActivity(intent);
    }

    public void goToGrammarActivity(View view) {
        Intent intent = new Intent(this, GrammarActivity.class);
        startActivity(intent);
    }

    public void goToTuringMachineActivity(View view) {
        Intent intent = new Intent(this, EditTuringMachineActivity.class);
        startActivity(intent);
    }

    public void goToTuringMachineMultiTrackActivity(View view) {
        Intent intent = new Intent(this, EditTMMultiTracksActivity.class);
        startActivity(intent);
    }

    public void goToTuringMachineMultiTapeActivity(View view) {
        Intent intent = new Intent(this, EditTMMultiTapesActivity.class);
        startActivity(intent);
    }

    public void goToTuringMachineEnumActivity(View view) {
        Intent intent = new Intent(this, EditTMEnumActivity.class);
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
        builder.setTitle(R.string.exit);
        builder.setMessage(R.string.exit_lfapp);
        builder.setPositiveButton(R.string.yes, dialogClickListener);
        builder.setNegativeButton(R.string.no, dialogClickListener);
        builder.show();
        //}
    }

}
