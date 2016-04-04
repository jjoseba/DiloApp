package com.jjoseba.pecsmobile.ui;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

public class AutoFitTextView extends TextView {

    // Attributes
    private Paint mTestPaint;
    private float defaultTextSize;

    public AutoFitTextView(Context context) {
        super(context);
        initialize();
    }

    public AutoFitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize() {
        mTestPaint = new Paint();
        mTestPaint.set(this.getPaint());
        defaultTextSize = getTextSize();
    }

    /* Re size the font so the specified text fits in the text box
     * assuming the text box is the specified width.
     */
    private void refitText(String text, int textWidth) {

        if (textWidth <= 0 || text.isEmpty())
            return;

        int targetWidth = textWidth - this.getPaddingLeft() - this.getPaddingRight();
        String[] words = text.split("\\s+");

        float textSize = defaultTextSize;
        for (int i=0; i<words.length; i++){
            // text already fits with the xml-defined font size?
            mTestPaint.set(this.getPaint());
            mTestPaint.setTextSize(defaultTextSize);
            if(mTestPaint.measureText(words[i]) <= targetWidth) {
                textSize = Math.min(textSize, defaultTextSize);
                continue;
            }

            float hi = defaultTextSize;
            float lo = 2;
            final float threshold = 0.5f; // How close we have to be

            // adjust text size using binary search for efficiency
            while (hi - lo > threshold) {
                float size = (hi + lo) / 2;
                mTestPaint.setTextSize(size);
                if(mTestPaint.measureText(words[i]) >= targetWidth )
                    hi = size; // too big
                else
                    lo = size; // too small
            }
            textSize = Math.min(textSize, lo);
        }

        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int height = getMeasuredHeight();
        refitText(this.getText().toString(), parentWidth);
        this.setMeasuredDimension(parentWidth, height);
    }

    @Override
    protected void onTextChanged(final CharSequence text, final int start,
            final int before, final int after) {
        refitText(text.toString(), this.getWidth());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            refitText(this.getText().toString(), w);
        }
    }

}