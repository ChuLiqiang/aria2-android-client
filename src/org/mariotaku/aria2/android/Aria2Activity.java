package org.mariotaku.aria2.android;

import java.util.Timer;
import java.util.TimerTask;



import org.mariotaku.aria2.Aria2API;
import org.mariotaku.aria2.DownloadUris;
import org.mariotaku.aria2.GlobalStat;
import org.mariotaku.aria2.Options;
import org.mariotaku.aria2.Version;
import org.mariotaku.aria2.android.utils.CommonUtils;


import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class Aria2Activity extends ActionBarActivity implements OnClickListener,Aria2Message{

	
	private Aria2Manager _aria2Manager = null;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		_aria2Manager = new Aria2Manager(this,mStatusRefreshHandler);
		
		findViewById(R.id.version).setOnClickListener(this);
		findViewById(R.id.session_info).setOnClickListener(this);
		findViewById(R.id.status).setOnClickListener(this);
		findViewById(R.id.run).setOnClickListener(this);
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		_aria2Manager.UpdateHost();
		_aria2Manager.StartUpdateGlobalStat();
	}

	@Override
	public void onStop() {
		_aria2Manager.StopUpdateGlobalStat();
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.new_download:
				Toast.makeText(Aria2Activity.this,"click download", Toast.LENGTH_LONG).show();
				break;
			case R.id.action_exit:
				finish();
				break;
			case R.id.action_settings:
				startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
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
					((TextView) v).setText(_aria2Manager.GetVersionInfo());
					break;
				case R.id.session_info:
					((TextView) v).setText(_aria2Manager.GetSessionInfo());
					break;
				case R.id.status:
					/*
					aria2.tellStatus(7, "gid");
					*/
					//((TextView) v).setText(String.valueOf(aria2.tellStatus(7, "gid").gid));
					break;
				case R.id.run:
					((TextView) v)
							.setText(_aria2Manager.AddUri());
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