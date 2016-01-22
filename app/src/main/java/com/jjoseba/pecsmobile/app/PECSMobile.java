package com.jjoseba.pecsmobile.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.activity.PrefsActivity;
import com.jjoseba.pecsmobile.util.FileUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class PECSMobile extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static boolean SHOW_ADD_BUTTON_CARD = true;
    public static boolean SHOW_TEMP_TEXT_BUTTON_CARD = true;
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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
        SHOW_ADD_BUTTON_CARD = prefs.getBoolean(PrefsActivity.SHOW_ADD_CARD, SHOW_ADD_BUTTON_CARD);
        SHOW_TEMP_TEXT_BUTTON_CARD = prefs.getBoolean(PrefsActivity.SHOW_TEMPTEXT_CARD, SHOW_TEMP_TEXT_BUTTON_CARD);
        if (SHOW_ADD_BUTTON_CARD) CUSTOM_BUTTON_CARDS++;
        if (SHOW_TEMP_TEXT_BUTTON_CARD) CUSTOM_BUTTON_CARDS++;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //We sum all the custom cards
        CUSTOM_BUTTON_CARDS = 0;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
        SHOW_ADD_BUTTON_CARD = prefs.getBoolean(PrefsActivity.SHOW_ADD_CARD, SHOW_ADD_BUTTON_CARD);
        SHOW_TEMP_TEXT_BUTTON_CARD = prefs.getBoolean(PrefsActivity.SHOW_TEMPTEXT_CARD, SHOW_TEMP_TEXT_BUTTON_CARD);
        if (SHOW_ADD_BUTTON_CARD) CUSTOM_BUTTON_CARDS++;
        if (SHOW_TEMP_TEXT_BUTTON_CARD) CUSTOM_BUTTON_CARDS++;
    }
}
