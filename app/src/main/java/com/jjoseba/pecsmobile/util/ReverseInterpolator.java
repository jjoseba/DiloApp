package com.jjoseba.pecsmobile.util;

import android.view.animation.Interpolator;

//based on: http://stackoverflow.com/questions/4120824/android-reversing-an-animation
public class ReverseInterpolator implements Interpolator {

    private final Interpolator mInterpolator;

    public ReverseInterpolator(Interpolator interpolator){
        mInterpolator = interpolator;
    }

    @Override
    public float getInterpolation(float input) {
        return 1 - mInterpolator.getInterpolation(input);
    }

}