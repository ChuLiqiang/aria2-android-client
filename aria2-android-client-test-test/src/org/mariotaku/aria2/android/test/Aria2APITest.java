package org.mariotaku.aria2.android.test;

import org.mariotaku.aria2.Aria2API;
import org.mariotaku.aria2.DownloadUris;
import org.mariotaku.aria2.Options;
import org.mariotaku.aria2.Version;

import android.util.Log;

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
		
	}

}
