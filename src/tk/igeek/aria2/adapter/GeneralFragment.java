package tk.igeek.aria2.adapter;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import tk.igeek.aria2.Status;
import tk.igeek.aria2.android.Aria2UIMessage;
import tk.igeek.aria2.android.DownloadItemInfoActivity;
import tk.igeek.aria2.android.IIncomingHandler;
import tk.igeek.aria2.android.IncomingHandler;
import tk.igeek.aria2.android.R;
import tk.igeek.aria2.android.manager.PreferencesManager;
import tk.igeek.aria2.android.service.Aria2APIMessage;
import tk.igeek.aria2.android.utils.CommonUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GeneralFragment extends Fragment implements IIncomingHandler {

	private Messenger mMessenger = null;
	private Handler mRefreshHandler = null;
	private Status status = null;
	private PreferencesManager _preferencesManager = null;
	private int commandType = Aria2APIMessage.ERROR_COMMAND;
	private String mCurrentGid = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle extras = getActivity().getIntent().getExtras();
		if (extras != null) {
			status = extras.getParcelable("downloadItemInfo");
		}
		if (status != null) {
			mCurrentGid = status.gid;
			commandType = Aria2APIMessage.TELL_STATUS;
		}
		mRefreshHandler = new IncomingHandler(this);
		mMessenger = new Messenger(mRefreshHandler);
		_preferencesManager = new PreferencesManager(getActivity());
		super.onCreate(savedInstanceState);
	}

	
	private TextView downloadItemInfoStatus = null; 
	private TextView downloadItemInfoDownloaded = null;
	private TextView downloadItemInfoDownloadedSpeed = null;
	private TextView downloadItemInfoSeeds = null;
	private TextView downloadItemInfoError = null;
	private TextView downloadItemInfoUploaded = null;
	private TextView downloadItemInfoUploadSpeed = null;
	private TextView downloadItemInfoEta = null;
	private TextView downloadItemInfoShareRatio = null;
	private TextView downloadItemInfoName = null;
	private TextView downloadItemInfoTotalSize = null;
	private TextView downloadItemInfoHash = null;
	private TextView downloadItemInfoPieces = null;
	private TextView downloadItemInfoPath = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_general, container, false);
		
		
		if (status != null) {
 			downloadItemInfoStatus = (TextView)rootView.findViewById(R.id.download_item_info_status);
			downloadItemInfoDownloaded = (TextView)rootView.findViewById(R.id.download_item_info_downloaded);
			downloadItemInfoDownloadedSpeed = (TextView)rootView.findViewById(R.id.download_item_info_downloadspeed);
			downloadItemInfoSeeds = (TextView)rootView.findViewById(R.id.download_item_info_seeds);
			downloadItemInfoError = (TextView)rootView.findViewById(R.id.download_item_info_error);
			downloadItemInfoUploaded = (TextView)rootView.findViewById(R.id.download_item_info_uploaded);
			downloadItemInfoUploadSpeed = (TextView)rootView.findViewById(R.id.download_item_info_uploadspeed);
			downloadItemInfoEta = (TextView)rootView.findViewById(R.id.download_item_info_eta);
			downloadItemInfoShareRatio = (TextView)rootView.findViewById(R.id.download_item_info_share_ratio);
			downloadItemInfoName = (TextView)rootView.findViewById(R.id.download_item_info_name);
			downloadItemInfoTotalSize= (TextView)rootView.findViewById(R.id.download_item_info_total_size);
			downloadItemInfoHash = (TextView)rootView.findViewById(R.id.download_item_info_hash);
			downloadItemInfoPieces = (TextView)rootView.findViewById(R.id.download_item_info_pieces);
			downloadItemInfoPath = (TextView)rootView.findViewById(R.id.download_item_info_path);

			UpdateStatus(status);
			
		}
		
		return rootView;
	}
	
	private void UpdateStatus(Status newStatus)
	{
		//set tranfer
		downloadItemInfoStatus.setText(newStatus.status);
		downloadItemInfoDownloaded.setText(CommonUtils.formatSizeString(newStatus.completedLength));
		downloadItemInfoDownloadedSpeed.setText(CommonUtils.formatSizeString(newStatus.downloadSpeed) + "/s");
		downloadItemInfoSeeds .setText(newStatus.numSeeders);
		downloadItemInfoError.setText(newStatus.errorCode);
		downloadItemInfoUploaded.setText(CommonUtils.formatSizeString(newStatus.uploadLength));
		downloadItemInfoUploadSpeed.setText(CommonUtils.formatSizeString(newStatus.uploadSpeed) + "/s");
		downloadItemInfoEta.setText(newStatus.getETA());
		downloadItemInfoShareRatio.setText(newStatus.getRatio()); 

		//set information
		downloadItemInfoName.setText(newStatus.getName());
		downloadItemInfoTotalSize.setText(CommonUtils.formatSizeString(newStatus.totalLength));
		downloadItemInfoHash.setText(newStatus.infoHash);
		downloadItemInfoPieces.setText(newStatus.numPieces);
		downloadItemInfoPath.setText(newStatus.dir);
	}
	
	private CountDownLatch finishSignal = null;
	private Timer mGlobalStatRefreshTimer = null;
	
	public void StartUpdateStat()
	{
		Log.i("aria2", "start update stat!");
		
		int interval = _preferencesManager.GetInterval();
		
		mGlobalStatRefreshTimer = new Timer();
		mGlobalStatRefreshTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				DownloadItemInfoActivity downloadItemInfoActivity = (DownloadItemInfoActivity)getActivity();
				boolean canSend = downloadItemInfoActivity.serverIsInit();
				if(canSend == false)
				{
					Log.i("aria2", "server not init!");
					return;
				}
				
				finishSignal = new CountDownLatch(1);
				
				downloadItemInfoActivity.sendToAria2APIHandlerMsg(commandType,mCurrentGid,mMessenger);
				
				try
				{
					finishSignal.await();
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				Log.i("aria2", "have update general info ui send request to server!");
			}
		}, 0, interval);
		
	}
	
	public void StopUpdateStat()
	{
		if(mGlobalStatRefreshTimer != null)
		{
			mGlobalStatRefreshTimer.cancel();
			mGlobalStatRefreshTimer = null;
			Log.i("aria2", "general info stop update stat timer!");
		}
	}
	
	@Override
	public void onResume() {
		StartUpdateStat();
		super.onResume();
	}

	@Override
	public void onPause() {
		StopUpdateStat();
		super.onPause();
	}

	@Override
	public void handleMessage(Message msg, Handler handler) {
		
		Log.i("aria2", "general ui handler get msg:" + msg.what);
		try
		{
			switch (msg.what) 
			{
			case Aria2UIMessage.STATUS_REFRESHED:
				{
					Status newStatus = (Status)msg.obj;
					UpdateStatus(newStatus);
					finishSignal.countDown();
				}
				break;
			}
		}catch (Exception e) {
			Log.e("aria2", "general ui handler is error!",e);
		}
	}
}
