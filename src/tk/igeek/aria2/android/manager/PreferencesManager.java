package tk.igeek.aria2.android.manager;

import tk.igeek.aria2.android.SettingsActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

public class PreferencesManager {
	
	Context _context;
	public PreferencesManager(Context context)
	{
		Log.i("aria2", "init Aria2Manager!");
		_context = context;
	}
	
	public int GetInterval()
	{
		int interval = Integer.valueOf(getPreferences(SettingsActivity.PREF_KEY_REFRESH_INTERVAL))*1000;
		if (interval <2000 || interval > 60000) {
			interval = 3000;
		}
		return interval;
	}
	
	public String getPreferences(String key) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(_context);
		String prefKey= sharedPref.getString(key,"");
		return prefKey;
	}
	
	public String getHost() {
		String prefKeyHost = getPreferences(SettingsActivity.PREF_KEY_HOST);
		
		if(prefKeyHost.equals(""))
		{
			throw new IllegalArgumentException("Please config host address first!");
		}
		
		return prefKeyHost;
	}
	
	public int getPort() {
		String prefKeyPort = getPreferences(SettingsActivity.PREF_KEY_PORT);
		
		if(prefKeyPort.equals(""))
		{
			throw new IllegalArgumentException("pealse initial host port!");
		}
		int port = -1;
		
		try
		{
			port = Integer.valueOf(prefKeyPort);
		}catch (Exception e) {
			throw new IllegalArgumentException("initial host port error!");
		}
		
		return port;
	}
}
