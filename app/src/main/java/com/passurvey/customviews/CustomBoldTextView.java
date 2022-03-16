package com.passurvey.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomBoldTextView extends TextView {
    public CustomBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomBoldTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Avenir-Roman.ttf");
        setTypeface(tf);
        setLineSpacing(0.0f, 1.0f);
    }
}
