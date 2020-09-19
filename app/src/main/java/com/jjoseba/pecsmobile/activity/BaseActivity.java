package com.jjoseba.pecsmobile.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.fragment.app.FragmentActivity;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class BaseActivity extends FragmentActivity {

    SharedPreferences prefs;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }
}
