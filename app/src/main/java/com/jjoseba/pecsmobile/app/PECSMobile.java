package com.jjoseba.pecsmobile.app;

import android.app.Application;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.util.FileUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class PECSMobile extends Application {

    public static final boolean SHOW_ADD_BUTTON_CARD = true;
    public static final boolean SHOW_TEMP_TEXT_BUTTON_CARD = true;
    public static final int DISPLAY_MODE_CARDS = 1;
    public static final int DISPLAY_MODE_TEXT = 2;
    public static final int DISPLAY_MODE = DISPLAY_MODE_TEXT;
    public static int CUSTOM_BUTTON_CARDS;

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/billy.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        //We set the StoragePath
        FileUtils.initialize(getApplicationContext());

        //We sum all the custom cards
        CUSTOM_BUTTON_CARDS = 0;
        if (SHOW_ADD_BUTTON_CARD) CUSTOM_BUTTON_CARDS++;
        if (SHOW_TEMP_TEXT_BUTTON_CARD) CUSTOM_BUTTON_CARDS++;
    }

}
