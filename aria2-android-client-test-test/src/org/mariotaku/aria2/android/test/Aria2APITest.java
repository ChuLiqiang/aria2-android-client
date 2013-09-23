package org.mariotaku.aria2.android.test;

import java.util.HashMap;

import org.mariotaku.aria2.Aria2API;
import org.mariotaku.aria2.DownloadUris;
import org.mariotaku.aria2.Files;
import org.mariotaku.aria2.Options;
import org.mariotaku.aria2.Status;
import org.mariotaku.aria2.Version;

import android.util.Log;
import android.webkit.URLUtil;

import junit.framework.TestCase;

public class Aria2APITest extends TestCase
{
	private Aria2API aria2;
	private String aria2Host = "10.16.131.12";
	
    protected void setUp() {
    	aria2 = new Aria2API(aria2Host);
    }

    
	public void testGetVersion() {
		
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
		String sVersion = version.toString();
		Log.d("Test Version", sVersion);
    }
	
	public void testAddUri()
	{
		String returnValue = aria2.addUri(
							new DownloadUris(
									"http://releases.ubuntu.com/11.10/ubuntu-11.10-desktop-i386.iso.torrent"));
		String ReturnString = "Return value : " + returnValue; 
		Log.d("aria2 Test",ReturnString);
	}
	
	public void testPauseAll()
	{
		String returnValue = aria2.pauseAll(); 
		Log.d("aria2 Test",returnValue);
	}
	
	public void testTellStatus()
	{
		Status status = aria2.tellStatus("7e9a2701db3ade17");
		//Status status = aria2.tellStatus("cc3628e95b70078b");
		Log.d("aria2 Test","gid:" + status.gid);
		Log.d("aria2 Test","status:" + status.status);
		Log.d("aria2 Test","completedLength:" + status.completedLength + "bytes");
		Log.d("aria2 Test","totalLength:" + status.totalLength + "bytes");
		Log.d("aria2 Test","bitfield:" + status.bitfield);
		Log.d("aria2 Test","downloadSpeed:" + status.downloadSpeed + "bytes/sec");
		Log.d("aria2 Test","uploadSpeed:" + status.uploadSpeed + "bytes/sec");
		Log.d("aria2 Test","dir:" + status.dir);
		Object[] files = status.files;
		Log.d("aria2 Test","files begin-------" );
		for (Object file: files)
		{
			Files fileItem =new Files((HashMap<String, Object>)file);
			Log.d("aria2 Test","file index:" + fileItem.index);
			Log.d("aria2 Test","     path:" + fileItem.path);
			Log.d("aria2 Test","     completedLength:" + fileItem.completedLength + "bytes");
			Log.d("aria2 Test","     length:" + fileItem.length + "bytes");
			Log.d("aria2 Test","     selected:" + fileItem.selected);
		}
		Log.d("aria2 Test","files end-------" );
		
		
		
	}
	
	public void testTellActive()
	{
		aria2.tellActive();
	}
	
	public void testTellWaiting()
	{
		aria2.tellWaiting(0, 10);
	}
	
	public void testTellStopped()
	{
		aria2.tellStopped(0, 10);
	}
	
	public void testUnpauseAll()
	{
		aria2.unpauseAll();
	}
	
	

}
