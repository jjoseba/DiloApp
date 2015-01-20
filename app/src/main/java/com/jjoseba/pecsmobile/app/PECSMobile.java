package com.jjoseba.pecsmobile.app;

import android.app.Application;

import com.jjoseba.pecsmobile.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Joseba on 20/01/2015.
 */
public class PECSMobile extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/billy.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }

}
