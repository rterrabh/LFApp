package com.ufla.lfapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ufla.lfapp.utils.ResourcesContext;


/**
 * Created by lfapp on 08/06/17.
 */

public abstract class AppCompatActivityContext extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ResourcesContext.updateContext(this);
    }

}
