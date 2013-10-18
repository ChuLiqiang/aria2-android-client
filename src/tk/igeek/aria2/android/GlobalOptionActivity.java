package tk.igeek.aria2.android;

import tk.igeek.aria2.GlobalOptions;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class GlobalOptionActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener
{
	private GlobalOptions globalOptions = null;
	
	@Override
	public void onCreate(Bundle savedInstaceState)
	{
		super.onCreate(savedInstaceState);
		addPreferencesFromResource(R.xml.global_option_preferences);
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
		if(globalOptions == null)
		{
			globalOptions  = new GlobalOptions();
		}
		
		globalOptions.setField(key,sharedPreferences);
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    getPreferenceScreen().getSharedPreferences()
	            .registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	protected void onPause() {
	    super.onPause();
	    getPreferenceScreen().getSharedPreferences()
	            .unregisterOnSharedPreferenceChangeListener(this);
	}
}
