package com.passurvey.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;


public class CustomRadioButton extends RadioButton {

    public CustomRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomRadioButton(Context context) {
        super(context);
        init();
    }

    private void init() {
//        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "helvetica.ttf");
//        setTypeface(tf);
//        setLineSpacing(0.0f,1.0f);
    }
}
