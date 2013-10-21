package tk.igeek.aria2.android;

import tk.igeek.aria2.GlobalOptions;
import android.content.Intent;
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
		globalOptions = null;
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
	    
	    getPreferenceScreen().getSharedPreferences()
	            .unregisterOnSharedPreferenceChangeListener(this);
	    super.onPause();    
	    
	}
	
	@Override
	public void finish() {
	  // Prepare data intent 
	  Intent data = new Intent();
	  if(globalOptions != null)
	  {
		  data.putExtra("changeGlobalOptions",globalOptions);
	  }
	  // Activity finished ok, return the data
	  setResult(RESULT_OK, data);
	  super.finish();
	} 
}
