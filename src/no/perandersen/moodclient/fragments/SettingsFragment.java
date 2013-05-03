package no.perandersen.moodclient.fragments;

import no.perandersen.moodclient.R;
import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener{
	private AlarmManager am;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
    }
    
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("time_sleepLog")) {
            Preference connectionPref = findPreference(key);
            // Unset alarm and set it again using the new time 
            
        }
    }
}
