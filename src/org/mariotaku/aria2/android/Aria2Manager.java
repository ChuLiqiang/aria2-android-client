package org.mariotaku.aria2.android;

import java.util.Timer;
import java.util.TimerTask;

import org.mariotaku.aria2.Aria2API;
import org.mariotaku.aria2.DownloadUris;
import org.mariotaku.aria2.Options;
import org.mariotaku.aria2.Version;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

public class Aria2Manager implements Aria2Message
{
	private Aria2API aria2;
	private String aria2Host = "192.168.1.1";
	private Context _context;
	
	private Timer mGlobalStatRefreshTimer;
	private Handler _mStatusRefreshHandler;
	public Aria2Manager(Context context,Handler mStatusRefreshHandler)
	{
		_context = context;
		_mStatusRefreshHandler = mStatusRefreshHandler;
		
		aria2Host = getHost();
		aria2 = new Aria2API(aria2Host);
		
	}
	
	public void UpdateHost() {
		String nowAria2Host = getHost();
		if(!nowAria2Host.equals(aria2Host))
		{
			aria2Host = nowAria2Host;
			aria2 = null;
			aria2 = new Aria2API(aria2Host);
		}
	}
	
	public void StartUpdateGlobalStat()
	{
		mGlobalStatRefreshTimer = new Timer();
		mGlobalStatRefreshTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Message globalstat_msg = new Message();
				globalstat_msg.what = GLOBAL_STAT_REFRESHED;
				try
				{
					globalstat_msg.obj = aria2.getGlobalStat();
				}
				catch (Exception e)
				{
					
				}
				_mStatusRefreshHandler.sendMessage(globalstat_msg);

			}

		}, 0, 1000);
	}
	
	public void StopUpdateGlobalStat()
	{
		mGlobalStatRefreshTimer.cancel();
		mGlobalStatRefreshTimer = null;
	}
	
	public String GetVersionInfo()
	{
		StringBuilder version = new StringBuilder();
		Version versionInfo = aria2.getVersion();
		version.append("Version : " + versionInfo.version + "\n");
		Object[] values = versionInfo.enabledFeatures;
		StringBuilder features = new StringBuilder();
		for (Object value : values) {
			features.append(value + "\n");
		}
		features.delete(features.length() - 1, features.length());
		version.append("Enabled features : \n" + features.toString());
		
		return version.toString();
	}
	
	public String GetSessionInfo()
	{
		StringBuilder session_info = new StringBuilder();
		session_info.append("Session ID : " + aria2.getSessionInfo().sessionId);
		return session_info.toString();
	}
	
	public String GetStatus()
	{
		return String.valueOf(aria2.tellStatus(7, "gid").gid);
	}
	
	public String AddUri()
	{
		
		String returnValue = aria2.addUri(
							new DownloadUris(
									"http://releases.ubuntu.com/11.10/ubuntu-11.10-desktop-i386.iso.torrent"));	
		return "Return value : " + returnValue;
	}
	
	public String AddUri(String uri)
	{
		
		String returnValue = aria2.addUri(
							new DownloadUris(
									uri));	
		return "Return value : " + returnValue;
	}
	
	private String getHost() {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(_context);
		String prefKeyHost= sharedPref.getString(SettingsActivity.PREF_KEY_HOST,"");
		return prefKeyHost;
	}	
	
	


}
