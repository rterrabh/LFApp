package com.ufla.lfapp.utils;

import android.app.Activity;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ufla.lfapp.R;



/**
 * Example of writing an input method for a soft lfapp_keyboard.  This code is
 * focused on simplicity over completeness, so it should in no way be considered
 * to be a complete soft lfapp_keyboard implementation.  Its purpose is to provide
 * a basic example for how you would get started writing an input method, to
 * be fleshed out as appropriate.
 */
public class LFAppKeyboard extends InputMethodService {



    private OnKeyboardStateChangedListener listener;
    private KeyboardView mKeyboardView;
    private Activity mHostActivity;
    private boolean move = true;
    private int offsetBegin = -1;

    private KeyboardView.OnKeyboardActionListener mOnKeyboardActionListener =
            new KeyboardView.OnKeyboardActionListener() {

        public final static int CODE_SHIFT_LOWERCASE = 0;
        public final static int CODE_BACK = 1;
        public final static int CODE_ARROW = 2;
        public final static int CODE_PIPE = 3;
        public final static int CODE_LAMBDA = 4;
        public final static int CODE_ENTER = 5;
        public final static int CODE_SHIFT_UPPERCASE = 6;


        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            // NOTE We can say '<Key android:codes="49,50" ... >' in the xml file; all codes
            // come in keyCodes, the first in this list in primaryCode
            // Get the EditText and its Editable
            View focusCurrent = mHostActivity.getWindow().getCurrentFocus();
            if(focusCurrent == null || !focusCurrent.getClass().equals(AppCompatEditText.class) ) {
                return;
            }
            EditText editText = (EditText) focusCurrent;
            int pos = editText.getBaseline()+editText.getScrollY();
            if (pos > mKeyboardView.getBaseline()) {
                editText.scrollTo(0, mKeyboardView.getBaseline()-10-editText.getBaseline());
            }
            Editable editable = editText.getText();
            int start = editText.getSelectionStart();
            int end = editText.getSelectionEnd();
            // Apply the key to the edittext
            if(editable == null || start == -1) {
                return;
            }
            boolean delectedSelected = false;
            if(primaryCode != CODE_SHIFT_LOWERCASE && primaryCode != CODE_SHIFT_UPPERCASE
                    && editText.isSelected() && end != -1 && start != end) {
                if(start > end) {
                    int aux = start;
                    start = end;
                    end = aux;
                }
                editable.delete(start, end);
                editText.setSelection(start);
                delectedSelected = true;
            }

            switch(primaryCode) {
                case CODE_SHIFT_LOWERCASE:  mKeyboardView.setKeyboard(new Keyboard(mHostActivity,
                        R.xml.lfapp_keyboard_uppercase)); break;
                case CODE_BACK:
                    if(!delectedSelected && start > 0) {
                        editable.delete(start - 1, start);
                    } break;
                case CODE_ARROW: editable.insert(start, " -> "); break;
                case CODE_PIPE: editable.insert(start, " | "); break;
                case CODE_LAMBDA: editable.insert(start, "λ "); break;
                case CODE_ENTER: editable.insert(start, "\n"); break;
                case CODE_SHIFT_UPPERCASE:  mKeyboardView.setKeyboard(new Keyboard(mHostActivity,
                        R.xml.lfapp_keyboard)); break;
                default: editable.insert(start, Character.toString((char) primaryCode)); break;

            }
            if (pos > mKeyboardView.getBaseline()) {
                editText.scrollTo(0, mKeyboardView.getBaseline()-10-editText.getBaseline());
            }

        }

        @Override
        public void onPress(int i) {
        }

        @Override
        public void onRelease(int i) {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeUp() {
        }
    };

    /**
     * Create a custom lfapp_keyboard, that uses the KeyboardView (with resource id
     * <var>viewid</var>) of the <var>host</var> activity,
     * and load the lfapp_keyboard layout from xml file <var>layoutid</var>
     * (see {@link Keyboard} for description).
     * Note that the <var>host</var> activity must have a <var>KeyboardView</var> in its layout
     * (typically aligned with the bottom of the activity).
     * Note that the lfapp_keyboard layout xml file may include key codes for navigation; see the
     * constants in this class for their values.
     * Note that to enable EditText's to use this custom lfapp_keyboard, call the
     * {@link #registerEditText(int)}.
     *
     * @param host The hosting activity.
     * @param viewid The id of the KeyboardView.
     * @param layoutid The id of the xml file containing the lfapp_keyboard layout.
     */
    public LFAppKeyboard(Activity host, int viewid, int layoutid) {
        mHostActivity = host;
        mKeyboardView = (KeyboardView) mHostActivity.findViewById(viewid);
        mKeyboardView.setKeyboard(new Keyboard(mHostActivity, layoutid));
        mKeyboardView.setPreviewEnabled(false); // NOTE Do not show the preview balloons
        mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);
        // Hide the standard lfapp_keyboard initially
        mHostActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        listener = (OnKeyboardStateChangedListener) host;
    }

    /** Returns whether the CustomKeyboard is visible. */
    public boolean isLFAppKeyboardVisible() {
        return mKeyboardView.getVisibility() == View.VISIBLE;

    }

    /** Make the CustomKeyboard visible, and hide the system lfapp_keyboard for view v. */
    public void showLFAppKeyboard(View v) {
        mKeyboardView.setVisibility(View.VISIBLE);
        mKeyboardView.setEnabled(true);
        if(v != null) {
            listener.OnDisplay(v, mKeyboardView);
            ((InputMethodManager) mHostActivity.getSystemService(Activity.
                    INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /** Make the CustomKeyboard invisible. */
    public void hideLFAppKeyboard() {
        mKeyboardView.setVisibility(View.GONE);
        mKeyboardView.setEnabled(false);
        listener.OnHide(mKeyboardView);
    }

    /**
     * Register <var>EditText<var> with resource id <var>resid</var> (on the hosting activity) for
     * using this custom lfapp_keyboard.
     *
     * @param resid The resource id of the EditText that registers to the custom lfapp_keyboard.
     */
    public void registerEditText(int resid) {
        // Find the EditText 'resid'
        EditText edittext= (EditText) mHostActivity.findViewById(resid);
        // Make the custom lfapp_keyboard appear
        edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            // NOTE By setting the on focus listener, we can show the custom lfapp_keyboard when
            // the edit box gets focus, but also hide it when the edit box loses focus
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    showLFAppKeyboard(v);

                } else {
                    hideLFAppKeyboard();
                }
            }
        });
        edittext.setOnClickListener(new View.OnClickListener() {
            // NOTE By setting the on click listener, we can show the custom lfapp_keyboard again,
            // by tapping on an edit box that already had focus (but that had the lfapp_keyboard
            // hidden).
            @Override
            public void onClick(View v) {
                showLFAppKeyboard(v);
            }
        });
        // Disable standard lfapp_keyboard hard way
        // NOTE There is also an easy way: 'edittext.setInputType(InputType.TYPE_NULL)' (but you
        // will not have a cursor, and no 'edittext.setCursorVisible(true)' doesn't work )
        edittext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                EditText edittext = (EditText) v;
                int offsetFinish = -1;
                int offsetMove = -1;
                int offsetActual = edittext.getSelectionStart();

                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    offsetBegin = edittext.getOffsetForPosition(event.getX(), event.getY());
                }
                if(event.getAction() == MotionEvent.ACTION_MOVE) {
                    move = true;
                    offsetMove = edittext.getOffsetForPosition(event.getX(), event.getY());
                }
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    offsetFinish = edittext.getOffsetForPosition(event.getX(), event.getY());
                }
                int inType = edittext.getInputType();       // Backup the input type
                edittext.setInputType(InputType.TYPE_NULL); // Disable standard lfapp_keyboard
                edittext.onTouchEvent(event);               // Call native handler
                edittext.setInputType(inType);

                if (event.getAction() == MotionEvent.ACTION_MOVE && offsetBegin != -1
                        && offsetMove != -1) {
                    edittext.setSelection(offsetBegin, offsetMove);
                    edittext.setSelected(true);
                } else if (event.getAction() == MotionEvent.ACTION_UP && offsetFinish != -1) {
                    if(move && offsetBegin != -1) {
                        edittext.setSelection(offsetBegin, offsetFinish);
                        edittext.setSelected(true);
                    } else {
                        edittext.setSelection(offsetFinish);
                    }
                    offsetBegin = -1;
                    move = false;
                } else {
                    edittext.setSelection(offsetActual);
                }

                return true; // Consume touch event
            }
        });
        // Disable spell check (hex strings look like words to Android)
        edittext.setInputType(edittext.getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }

}
