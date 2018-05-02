package com.eniacs_team.rutamurcielago;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by rapuc on 5/1/18.
 * WebView with overrided performClick
 */

public class TouchableWebView extends WebView {

    public TouchableWebView(Context context) {
        super(context);
    }

    /**
     * Constructs a new WebView with layout parameters.
     *
     * @param context a Context object used to access application assets
     * @param attrs an AttributeSet passed to our parent
     */
    public TouchableWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }
}