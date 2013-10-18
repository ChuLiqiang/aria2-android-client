/**
 * 
 */
package tk.igeek.aria2.android;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.widget.*;

/**
 * @author Antoine-Ali Zarrouk <antoineali.zarrouk@viacesi.fr>
 * We are here using PreferenceActivity because we're targeting all devices from 2.2 (API8) to 4.2 (API17)
 * and we're only using simple parameters
 */
public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener{

	
	 public static final String PREF_KEY_HOST = "pref_key_host";
	 public static final String PREF_KEY_PORT = "pref_key_port";
	 public static final String PREF_KEY_USERNAME = "pref_key_username";
	 public static final String PREF_KEY_PASSWORD = "pref_key_password";
	 public static final String PREF_KEY_REFRESH_INTERVAL = "pref_key_refresh_interval";
	 
	 
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstaceState)
	{
		super.onCreate(savedInstaceState);
		addPreferencesFromResource(R.xml.preferences);
		
		/* Update summaries for prefs to reflect their current settings */
		updateCurrentSettings();
		
		/* Install listeners to pref changes for input validation purpose. */
		Preference pref = findPreference(PREF_KEY_REFRESH_INTERVAL);
		if (pref != null){
			pref.setOnPreferenceChangeListener(new OnPreferenceChangeListener(){
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					int value = Integer.valueOf(newValue.toString());
					boolean valid = (value >= 2 &&  value <= 60);
					if (!valid){
						Toast.makeText(getApplicationContext(), R.string.invalid_input, Toast.LENGTH_SHORT).show();
					}
					return valid;	
				}
				
			});
		}
	}
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		// TODO Update aria2 running config
		Toast toast = Toast.makeText(getApplicationContext(), "Back", Toast.LENGTH_SHORT);
		toast.show();
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(PREF_KEY_HOST) || key.equals(PREF_KEY_PORT) || key.equals(PREF_KEY_USERNAME) || key.equals(PREF_KEY_PASSWORD)) {
		    Preference connectionPref = findPreference(key);
		    
		    // Set summary to be the user-description for the selected value
		    connectionPref.setSummary(sharedPreferences.getString(key, ""));
		}
		if (key.equals(PREF_KEY_REFRESH_INTERVAL)) {
			updateCurrentSettings();
		}
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    getPreferenceScreen().getSharedPreferences()
	            .registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	protected void onPause() {
		getPreferenceScreen().getSharedPreferences()
	            .unregisterOnSharedPreferenceChangeListener(this);
	    super.onPause();
	    
	}
	
	private void updateCurrentSettings() {
		Preference pref = findPreference(PREF_KEY_REFRESH_INTERVAL);
		if (pref != null){
			/* Set to its default string with new settings appended. */
			pref.setSummary(R.string.pref_summary_refresh_interval);
			String newSummary = pref.getSummary() + " " + pref.getSharedPreferences().getString(PREF_KEY_REFRESH_INTERVAL, "");
			pref.setSummary(newSummary);
		}
		
	}
}
