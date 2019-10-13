package com.jjoseba.pecsmobile.ui.viewpager;

import androidx.viewpager.widget.ViewPager;
import android.view.View;


public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.05f;
    private static final float MIN_ALPHA = 0.2f;

    private float easeInCubic(float position, float start, float end) {
        return (end-start)*(position)*position*position + start;
    }

    private boolean fromRight = true;
    public void setAnimateFromRight(boolean animateFromRight){
        fromRight = animateFromRight;
    }

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 0) { // [-1,0]

            // Use the default slide transition when moving to the left page
            view.setAlpha(1);
            view.setTranslationX(pageWidth * -position);
            view.setScaleX(1);
            view.setScaleY(1);

        } else if (position <= 1) { // (0,1]

            // Fade the page out.
            view.setAlpha( easeInCubic(position, 1, MIN_ALPHA));
            // Counteract the default slide transition
            view.setTranslationX(pageWidth * -position - ((pageWidth/2) * (position) ) + (( (fromRight?3:1)* pageWidth/4) * (position) ));

            // Scale the page down (between MIN_SCALE and 1)
            //float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
            //float positionX = easeInCubic(position, -pageWidth/4, -pageWidth);

            float scaleFactor = easeInCubic(position, 1, MIN_SCALE);
            //view.setTranslationX(positionX);
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);




        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }
}