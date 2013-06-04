package no.perandersen.moodclient.fragments;

import no.perandersen.moodclient.R;
import no.perandersen.moodclient.system.MoodApplication;
import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
    
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("time_sleepLog") || key.equals("time_eveningLog")) {
            // call MoodApplication.registerAlarms();
           MoodApplication app = (MoodApplication) getActivity().getApplicationContext();
           app.registerAlarms();
        }
    }
}
