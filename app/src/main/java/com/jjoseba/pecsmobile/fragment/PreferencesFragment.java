package com.jjoseba.pecsmobile.fragment;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.activity.PrefsActivity;

import java.util.ArrayList;


public class PreferencesFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    ArrayList<CheckBoxPreference> displayModes = new ArrayList<>();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PreferencesFragment.
     */
    public static PreferencesFragment newInstance() {
        return new PreferencesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);

        displayModes.add((CheckBoxPreference) getPreferenceManager()
                .findPreference(PrefsActivity.DISPLAYMODE_TEXT));
        displayModes.add((CheckBoxPreference) getPreferenceManager()
                .findPreference(PrefsActivity.DISPLAYMODE_CARD));
        displayModes.add((CheckBoxPreference) getPreferenceManager()
                .findPreference(PrefsActivity.DISPLAYMODE_BASIC));
        displayModes.add((CheckBoxPreference) getPreferenceManager()
                .findPreference(PrefsActivity.DISPLAYMODE_TEXTADV));

        for (CheckBoxPreference cbp : displayModes) {
            cbp.setOnPreferenceClickListener(this);
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        for (CheckBoxPreference cbp : displayModes) {
            //We set checked only the preference clicked
            cbp.setChecked(cbp.getKey().equals(preference.getKey()));
        }
        return false;
    }
}
