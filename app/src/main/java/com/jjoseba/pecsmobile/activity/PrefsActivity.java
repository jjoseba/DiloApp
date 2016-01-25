package com.jjoseba.pecsmobile.activity;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.jjoseba.pecsmobile.fragment.PreferencesFragment;

public class PrefsActivity extends FragmentActivity {

    public static final String SHOW_ADD_CARD = "prefShowAddCard";
    public static final String SHOW_TEMPTEXT_CARD = "prefTempTextCard";
    public static final String DISPLAYMODE_TEXT = "prefDisplayModeText";
    public static final String DISPLAYMODE_CARD = "prefDisplayModeCards";

    private PreferencesFragment mPrefsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);
        }

        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mPrefsFragment = PreferencesFragment.newInstance();
        mFragmentTransaction.replace(android.R.id.content, mPrefsFragment);
        mFragmentTransaction.commit();
    }
}
