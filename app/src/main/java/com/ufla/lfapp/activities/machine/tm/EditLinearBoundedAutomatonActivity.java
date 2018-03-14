package com.ufla.lfapp.activities.machine.tm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.AppCompatActivityContext;

public class EditLinearBoundedAutomatonActivity extends AppCompatActivityContext {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_linear_bounded_automaton);
    }
}
