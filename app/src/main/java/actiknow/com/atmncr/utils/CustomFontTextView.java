package actiknow.com.atmncr.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomFontTextView extends TextView {

    public CustomFontTextView (Context context) {
        super (context);

        applyCustomFont (context);
    }

    public CustomFontTextView (Context context, AttributeSet attrs) {
        super (context, attrs);

        applyCustomFont (context);
    }

    public CustomFontTextView (Context context, AttributeSet attrs, int defStyle) {
        super (context, attrs, defStyle);

        applyCustomFont (context);
    }

    private void applyCustomFont (Context context) {
        Typeface customFont = FontCache.getTypeface ("Kozuka-Gothic.ttf", context);
        setTypeface (customFont);
    }
}
