package com.example.ucsdschedulinghelper.ui.courseListView;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.ucsdschedulinghelper.R;

/**
 * Created by SKE on 10/11/15.
 */

/**
 * The class is not used for now.
 */
public class FocusChangeListener implements View.OnFocusChangeListener {
    private Context context;

    public FocusChangeListener(Context context) {
        this.context = context;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
