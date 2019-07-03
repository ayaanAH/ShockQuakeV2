package com.example.shockquakev2;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
    }

    public static class QuakePreferenceFragment extends PreferenceFragment implements
            Preference.OnPreferenceChangeListener
    {

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);
            Preference minMag = findPreference(getString(R.string.preference_minimag_key));
            Preference limit = findPreference(getString(R.string.preference_limit_key));
            Preference orderBy = findPreference(getString(R.string.preference_orderby_key));
            bindPreferenceSummaryToValue(minMag);
            bindPreferenceSummaryToValue(limit);
            bindPreferenceSummaryToValue(orderBy);
        }

        private void bindPreferenceSummaryToValue(Preference preference)
        {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = sharedPreferences.getString(preference.getKey(),"");
            onPreferenceChange(preference, preferenceString);

        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value)
        {
            String  stringValue = value.toString();
            if (value instanceof ListPreference)
            {
                ListPreference listPreference = (ListPreference)preference;
                int preferenceIndex = listPreference.findIndexOfValue(stringValue);
                if (preferenceIndex>=0)
                {

                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[preferenceIndex]);

                }
            }
            else
            {

                preference.setSummary(stringValue);

            }
            return true;
        }
    }

}
