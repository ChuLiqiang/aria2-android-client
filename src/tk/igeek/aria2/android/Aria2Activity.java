package tk.igeek.aria2.android;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.io.File;




import tk.igeek.aria2.Aria2API;
import tk.igeek.aria2.DownloadUris;
import tk.igeek.aria2.GlobalStat;
import tk.igeek.aria2.Options;
import tk.igeek.aria2.Status;
import tk.igeek.aria2.Version;
import tk.igeek.aria2.android.DownloadItemDialogFragment.DownloadItemDialogListener;
import tk.igeek.aria2.android.NewDownloadDialogFragment.NewDownloadDialogListener;
import tk.igeek.aria2.android.utils.CommonUtils;


import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ar.com.daidalos.afiledialog.FileChooserDialog;
public class Aria2Activity extends ActionBarActivity 
						   implements OnClickListener,Aria2UIMessage,Aria2APIMessage,
						   NewDownloadDialogListener,DownloadItemDialogListener{

	
	private Aria2Manager _aria2Manager = null;
	
	private ListView downloadListView = null;
	
	private DownloadItemAdapter adapter = null;
	
	private List<DownloadItem> downloadItems = null;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		_aria2Manager = new Aria2Manager(this,mRefreshHandler);
		_aria2Manager.StartAria2Handler();
		
		downloadItems = new ArrayList<DownloadItem>();
		adapter = new DownloadItemAdapter(Aria2Activity.this,R.layout.download_item,downloadItems);
		downloadListView = (ListView)findViewById(R.id.download_list_view);
		downloadListView.setAdapter(adapter);
		downloadListView.setOnItemLongClickListener(mMessageLongClickedHandler);
		
	}

	private OnItemLongClickListener mMessageLongClickedHandler = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id)
		{
			DownloadItemDialogFragment downloadItemDialogFragment = new DownloadItemDialogFragment();
			
			Bundle args = new Bundle();
			
			args.putString("itemStatus",downloadItems.get(position).status);
			args.putString("itemGid",downloadItems.get(position).gid);
			args.putBoolean("havaBittorrent", downloadItems.get(position).havaBittorrent);
			
			downloadItemDialogFragment.setArguments(args);
			downloadItemDialogFragment.show(getSupportFragmentManager(), "DownloadItemDialogFragment");
			return false;
		}

		
	};
	
	@Override
	public void onStart() {
		super.onStart();
		try
		{
			_aria2Manager.InitHost();
			_aria2Manager.StartUpdateGlobalStat();
			
		}
		catch(Exception e)
		{
			Toast.makeText(Aria2Activity.this,e.getMessage(),Toast.LENGTH_LONG).show();
		}	 
	}

	@Override
	public void onStop() {
		_aria2Manager.StopUpdateGlobalStat();
		super.onStop();
	}
	
	@Override
	public void onDestroy()
	{
		 if(_aria2Manager != null)
		 {
			 _aria2Manager.StopAria2Handler();
		 }
		 super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.add_torrent:
				addTorrent();
				break;
			case R.id.add_metalink:
				addMetalink();
				break;				
			case R.id.add_url:
				showDownloadDialog();
				break;
			case R.id.pause_download:
				_aria2Manager.sendToAria2APIHandlerMsg(PAUSE_ALL_DOWNLOAD);
				break;
			case R.id.resume_download:
				_aria2Manager.sendToAria2APIHandlerMsg(RESUME_ALL_DOWNLOAD);
				break;
			case R.id.remove_download:
				_aria2Manager.sendToAria2APIHandlerMsg(PURGE_DOWNLOAD);
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

	private void addMetalink()
	{
		FileChooserDialog dialog = new FileChooserDialog(this);
    	dialog.setFilter(".*metalink");
		dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {
			public void onFileSelected(Dialog source, File file) {
				source.hide();
				Toast toast = Toast.makeText(source.getContext(), "File selected: " + file.getName(), Toast.LENGTH_LONG);
				toast.show();
				_aria2Manager.sendToAria2APIHandlerMsg(ADD_METALINK,file);
			}
			public void onFileSelected(Dialog source, File folder, String name) {
				source.hide();
				Toast toast = Toast.makeText(source.getContext(), "File created: " + folder.getName() + "/" + name, Toast.LENGTH_LONG);
				toast.show();
			}
		});
		dialog.show();
	}

	private void addTorrent()
	{
		FileChooserDialog dialog = new FileChooserDialog(this);
    	dialog.setFilter(".*torrent");
		dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {
			public void onFileSelected(Dialog source, File file) {
				source.hide();
				Toast toast = Toast.makeText(source.getContext(), "File selected: " + file.getName(), Toast.LENGTH_LONG);
				toast.show();
				_aria2Manager.sendToAria2APIHandlerMsg(ADD_TORRENT,file);
			}
			public void onFileSelected(Dialog source, File folder, String name) {
				source.hide();
				Toast toast = Toast.makeText(source.getContext(), "File created: " + folder.getName() + "/" + name, Toast.LENGTH_LONG);
				toast.show();
			}
		});
		dialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	}
	
	@Override
	public void onClick(View v) {
		
		try 
		{
			switch (v.getId()) 
			{
				
			}
		}catch (Exception e) {
			Toast.makeText(Aria2Activity.this,e.getMessage(), Toast.LENGTH_LONG).show();
		}

		

	}

	private Handler mRefreshHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Log.i("aria2", "aria2 ui handler get msg:" + msg.what);
			try
			{
				switch (msg.what) 
				{
					case GLOBAL_STAT_REFRESHED:
						if (msg.obj == null) return;
						GlobalStat stat = (GlobalStat) msg.obj;
						String subtitle = getString(R.string.global_speed_format,
								CommonUtils.formatSpeedString(stat.downloadSpeed),
								CommonUtils.formatSpeedString(stat.uploadSpeed));
						getSupportActionBar().setSubtitle(subtitle);
						break;
					case ALL_STATUS_REFRESHED:
						if (msg.obj == null) return;
						
						List<DownloadItem> downloadItemsNew = new ArrayList<DownloadItem>();
						
						ArrayList<ArrayList<Status>> statusList = (ArrayList<ArrayList<Status>>)msg.obj;
						Iterator<ArrayList<Status>> itStatusList = statusList.iterator();
						while (itStatusList.hasNext())
						{
							List<Status> status = itStatusList.next();
							Iterator<Status> it = status.iterator();
							while (it.hasNext())
							{
								Status statusTemp = it.next();
								DownloadItem downloadItem = new DownloadItem(statusTemp);
								downloadItemsNew.add(downloadItem);
							}
						}
						
						adapter.updateItems(downloadItemsNew);
						adapter.notifyDataSetChanged(); 
						break;
					case SHOW_ERROR_INFO_STOP_UPDATE_GLOBAL_STAT:
						{
							_aria2Manager.StopUpdateGlobalStat();
							if (msg.obj == null) 
							{
								Toast.makeText(Aria2Activity.this,"error happend!please check setting!", Toast.LENGTH_LONG).show();
							}
							String errorInfo = (String)msg.obj; 
							Toast.makeText(Aria2Activity.this,errorInfo,Toast.LENGTH_LONG).show();
						}
						break;
					case SHOW_ERROR_INFO:
						{
							if (msg.obj == null) 
							{
								Toast.makeText(Aria2Activity.this,"error happend!", Toast.LENGTH_LONG).show();
							}
							String errorInfo = (String)msg.obj; 
							Toast.makeText(Aria2Activity.this,errorInfo,Toast.LENGTH_LONG).show();
						}
						break;
				}
			}catch (Exception e) {
				Log.e("aria2", "aria2 ui handler is error!",e);
			}
		}
		
	};
	
	public void showDownloadDialog() {
        // Create an instance of the dialog fragmnt and show it
        DialogFragment dialog = new NewDownloadDialogFragment();
        dialog.show(getSupportFragmentManager(), "NewDownloadFragment");
    }

	@Override
	public void onDialogPositiveClick(DialogFragment dialog)
	{
		try 
		{
			String uri = ((NewDownloadDialogFragment)dialog).getDownloadUri(); 
			_aria2Manager.sendToAria2APIHandlerMsg(ADD_URI,uri);
			
		}catch (Exception e) {
			Toast.makeText(Aria2Activity.this,e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onDialogActionClick(DialogFragment dialog)
	{
		try 
		{
			int action = ((DownloadItemDialogFragment)dialog).getAction();
			String gid = ((DownloadItemDialogFragment)dialog).getGid();
			_aria2Manager.sendToAria2APIHandlerMsg(action,gid);
			
		}catch (Exception e) {
			Toast.makeText(Aria2Activity.this,e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	



}