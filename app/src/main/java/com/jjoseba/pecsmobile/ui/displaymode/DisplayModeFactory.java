package com.jjoseba.pecsmobile.ui.displaymode;

import android.content.SharedPreferences;

import com.jjoseba.pecsmobile.activity.PrefsActivity;

public class DisplayModeFactory {

    private SharedPreferences prefs;
    public DisplayModeFactory(SharedPreferences prefs){
        this.prefs = prefs;
    }

    public DisplayModeStrategy getCurrentDisplayMode(){
        if (prefs.getBoolean(PrefsActivity.DISPLAYMODE_CARD, true))
            return new DisplayCardsStrategy();
        else if (prefs.getBoolean(PrefsActivity.DISPLAYMODE_TEXT, true))
            return new DisplayTextStrategy();
        else
            return new DisplayBasicStrategy();
    }
}
