package com.jjoseba.pecsmobile.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.activity.PrefsActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PreferencesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PreferencesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreferencesFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    ArrayList<CheckBoxPreference> displayModes = new ArrayList<>();

    public PreferencesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PreferencesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PreferencesFragment newInstance() {
        PreferencesFragment fragment = new PreferencesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);

        displayModes.add((CheckBoxPreference) getPreferenceManager()
                .findPreference(PrefsActivity.DISPLAYMODE_TEXT));
        displayModes.add((CheckBoxPreference) getPreferenceManager()
                .findPreference(PrefsActivity.DISPLAYMODE_CARD));

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
