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
import android.webkit.URLUtil;

public class Aria2Manager implements Aria2Message
{
	private Aria2API _aria2 = null;
	private String _aria2Host = null;
	private Context _context = null;
	
	private Timer mGlobalStatRefreshTimer = null;
	private Handler _mStatusRefreshHandler = null;
	
	public Aria2Manager(Context context,Handler mStatusRefreshHandler)
	{
		_context = context;
		_mStatusRefreshHandler = mStatusRefreshHandler;
	}
	
	public void InitHost() {
		String nowAria2Host = getHost();
		if(!nowAria2Host.equals(_aria2Host))
		{
			_aria2Host = nowAria2Host;
			try
			{
				_aria2 = new Aria2API(_aria2Host);
				
			}catch(Exception e)
			{
				_aria2 = null;
				throw new IllegalArgumentException("Aria2 host is error!");
				
			}
			
		}
	}
	
	public void StartUpdateGlobalStat()
	{
		checkAria2();
		
		mGlobalStatRefreshTimer = new Timer();
		mGlobalStatRefreshTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Message globalstat_msg = new Message();
				globalstat_msg.what = GLOBAL_STAT_REFRESHED;
				try
				{
					globalstat_msg.obj = _aria2.getGlobalStat();
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
		if(mGlobalStatRefreshTimer != null)
		{
			mGlobalStatRefreshTimer.cancel();
			mGlobalStatRefreshTimer = null;
		}
	}
	
	public String GetVersionInfo()
	{
		checkAria2();
		
		StringBuilder version = new StringBuilder();
		Version versionInfo = _aria2.getVersion();
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
		checkAria2();
		
		StringBuilder session_info = new StringBuilder();
		session_info.append("Session ID : " + _aria2.getSessionInfo().sessionId);
		return session_info.toString();
	}

	
	
	public String GetStatus()
	{
		checkAria2();
		return String.valueOf(_aria2.tellStatus(7, "gid").gid);
	}
	
	public String AddUri()
	{
		checkAria2();
		String returnValue = _aria2.addUri(
							new DownloadUris(
									"http://releases.ubuntu.com/11.10/ubuntu-11.10-desktop-i386.iso.torrent"));	
		return "Return value : " + returnValue;
	}
	
	public String AddUri(String uri)
	{
		checkAria2();
		String returnValue = _aria2.addUri(
							new DownloadUris(
									uri));	
		return "Return value : " + returnValue;
	}
	
	private String getHost() {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(_context);
		String prefKeyHost= sharedPref.getString(SettingsActivity.PREF_KEY_HOST,"");
		if(prefKeyHost == null)
		{
			throw new IllegalArgumentException("host address is null!");
		}
		
		if(prefKeyHost.equals(""))
		{
			throw new IllegalArgumentException("pealse initial host address!");
		}
		
		return prefKeyHost;
	}	
	
	private void checkAria2()
	{
		if(_aria2 == null)
		{
			throw new IllegalArgumentException("Aria2 init is error!please check setting!");
		}
	}
	
	


}
