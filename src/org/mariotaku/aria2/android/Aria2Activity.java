package org.mariotaku.aria2.android;

import java.util.Timer;
import java.util.TimerTask;


import org.mariotaku.actionbarcompat.ActionBarFragmentActivity;
import org.mariotaku.aria2.Aria2API;
import org.mariotaku.aria2.DownloadUris;
import org.mariotaku.aria2.GlobalStat;
import org.mariotaku.aria2.Options;
import org.mariotaku.aria2.Version;
import org.mariotaku.aria2.android.utils.CommonUtils;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class Aria2Activity extends ActionBarFragmentActivity implements Constants,
		OnClickListener {

	private final static int GLOBAL_STAT_REFRESHED = 0;

	private Timer mGlobalStatRefreshTimer;

	private Aria2API aria2;

	private String aria2Ip = "192.168.1.1";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		/*
		getSupportActionBar().setIcon(R.drawable.ic_launcher);
		*/
		aria2 = new Aria2API(aria2Ip);

		findViewById(R.id.version).setOnClickListener(this);
		findViewById(R.id.session_info).setOnClickListener(this);
		findViewById(R.id.status).setOnClickListener(this);
		findViewById(R.id.run).setOnClickListener(this);

	}

	@Override
	public void onStart() {
		super.onStart();
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
				mStatusRefreshHandler.sendMessage(globalstat_msg);

			}

		}, 0, 1000);
		
	}

	@Override
	public void onStop() {
		mGlobalStatRefreshTimer.cancel();
		mGlobalStatRefreshTimer = null;
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_download, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.new_download:
				Toast.makeText(Aria2Activity.this,"click download", Toast.LENGTH_LONG).show();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		
		try 
		{
			switch (v.getId()) {
				case R.id.version:
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
					((TextView) v).setText(version.toString());
					break;
				case R.id.session_info:
					StringBuilder session_info = new StringBuilder();
					session_info.append("Session ID : " + aria2.getSessionInfo().sessionId);
					((TextView) v).setText(session_info.toString());
					break;
				case R.id.status:
					aria2.tellStatus(7, "gid");
					//((TextView) v).setText(String.valueOf(aria2.tellStatus(7, "gid").gid));
					break;
				case R.id.run:
					((TextView) v)
							.setText("Return value : "
									+ aria2.addUri(
											new DownloadUris(
													"http://releases.ubuntu.com/11.10/ubuntu-11.10-desktop-i386.iso.torrent"),
											new Options()));
					break;
			}
		}catch (Exception e) {
			Toast.makeText(Aria2Activity.this,"InterNet error!", Toast.LENGTH_LONG).show();
		}

		

	}

	private Handler mStatusRefreshHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case GLOBAL_STAT_REFRESHED:
					GlobalStat stat = (GlobalStat) msg.obj;
					if (msg.obj == null) return;
					String subtitle = getString(R.string.global_speed_format,
							CommonUtils.formatSpeedString(stat.downloadSpeed),
							CommonUtils.formatSpeedString(stat.uploadSpeed));
					getSupportActionBar().setSubtitle(subtitle);
					break;
			}
		}
	};

}