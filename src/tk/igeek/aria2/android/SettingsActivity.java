/**
 * 
 */
package tk.igeek.aria2.android;

import tk.igeek.aria2.android.service.Aria2APIMessage;
import tk.igeek.aria2.android.service.Aria2Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
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
		doBindService();
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
	
	boolean connectionChange = false;
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(PREF_KEY_HOST) || key.equals(PREF_KEY_PORT) || key.equals(PREF_KEY_USERNAME) || key.equals(PREF_KEY_PASSWORD)) {
		    Preference connectionPref = findPreference(key);
		    
		    // Set summary to be the user-description for the selected value
		    connectionPref.setSummary(sharedPreferences.getString(key, ""));
		    connectionChange = true;
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
	
	@Override
	protected void onDestroy() {
		if(connectionChange)
		{
			sendToAria2APIHandlerMsg(Aria2APIMessage.INIT_HOST);
			connectionChange = false;
		}
		doUnbindService();
		super.onDestroy();
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
	
	public boolean sendToAria2APIHandlerMsg(int msgType)
	{
		if(mService == null)
		{
			return false;
		}
		Message sendToAria2APIHandlerMsg = new Message();
		sendToAria2APIHandlerMsg.what = msgType;
		try {
			mService.send(sendToAria2APIHandlerMsg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	 /** Flag indicating whether we have called bind on the service. */
    boolean mIsBound;
	/** Messenger for communicating with service. */
    Messenger mService = null;
    
	/**
     * Class for interacting with the main interface of the service.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
    	public void onServiceConnected(ComponentName className,
    			IBinder service) {
    		// This is called when the connection with the service has been
    		// established, giving us the service object we can use to
    		// interact with the service.  We are communicating with our
    		// service through an IDL interface, so get a client-side
    		// representation of that from the raw service object.
    		mService = new Messenger(service);
    	}

    	public void onServiceDisconnected(ComponentName className) {
    		// This is called when the connection with the service has been
    		// unexpectedly disconnected -- that is, its process crashed.
    		mService = null;
    		
    	}
    };
    
    void doBindService() {
    	// Establish a connection with the service.  We use an explicit
    	// class name because there is no reason to be able to let other
    	// applications replace our component.
    	bindService(new Intent(this, 
    			Aria2Service.class), mConnection, Context.BIND_AUTO_CREATE);
    	mIsBound = true;
    }
    
    void doUnbindService() {
    	if (mIsBound) {
    		// Detach our existing connection.
    		unbindService(mConnection);
    		mIsBound = false;
    	}
    }
	
}
