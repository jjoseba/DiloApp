package com.jjoseba.pecsmobile.ui.viewpager;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class EnableableViewPager extends ViewPager {

    private boolean pagingEnabled;

    public EnableableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.pagingEnabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.pagingEnabled && super.onTouchEvent(event);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.pagingEnabled && super.onInterceptTouchEvent(event);

    }

    public void setPagingEnabled(boolean enabled) {
        this.pagingEnabled = enabled;
    }

    public boolean isPagingEnabled(){
        return this.pagingEnabled;
    }
}