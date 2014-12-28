package com.jjoseba.pecsmobile.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Joseba on 28/12/2014.
 */
public class EnableableViewPager extends ViewPager {

    private boolean pagingEnabled;

    public EnableableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.pagingEnabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.pagingEnabled) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.pagingEnabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void setPagingEnabled(boolean enabled) {
        this.pagingEnabled = enabled;
    }

    public boolean isPagingEnabled(){
        return this.pagingEnabled;
    }
}