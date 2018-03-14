package com.ufla.lfapp.utils;

import android.inputmethodservice.KeyboardView;
import android.view.View;

/**
 * Created by root on 19/10/16.
 */
public interface OnKeyboardStateChangedListener {
    public void OnDisplay(View currentview, KeyboardView currentKeyboard);

    public void OnHide(KeyboardView currentKeyboard);
}
