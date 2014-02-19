package tk.igeek.aria2.android;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;


import tk.igeek.aria2.Aria2API;
import tk.igeek.aria2.DownloadUris;
import tk.igeek.aria2.GlobalStat;
import tk.igeek.aria2.GlobalOptions;
import tk.igeek.aria2.Status;
import tk.igeek.aria2.Version;
import tk.igeek.aria2.android.manager.Aria2ConnectionInfo;
import tk.igeek.aria2.android.manager.PreferencesManager;
import tk.igeek.aria2.android.utils.Base64;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.os.Process;

public class Aria2Manager implements Aria2UIMessage,Aria2APIMessage
{
	private Aria2API _aria2 = null;
	
	Aria2ConnectionInfo _aria2ConnectionInfo = new Aria2ConnectionInfo(); 
	
	Messenger _mRefreshHandler = null;
	
	private boolean  _updating_status = false;
	
	public void setCurretnRefreshHandler(Messenger refreshHandler)
	{
		_mRefreshHandler = refreshHandler;
	}
	private PreferencesManager _preferencesManager = null;
	public Aria2Manager(PreferencesManager preferencesManager)
	{
		Log.i("aria2", "init Aria2Manager!");
		_preferencesManager = preferencesManager;
	}
	
	protected void addMetalink(File file) throws IOException
	{
		byte[] bytes = fileToByte(file);
		_aria2.addMetalink(bytes);
	}

	public void addTorrnet(File file) throws IOException 
	{
		byte[] bytes = fileToByte(file);
		_aria2.addTorrent(bytes);
	}

	private byte[] fileToByte(File file) throws FileNotFoundException,
			IOException
	{
		InputStream inputStream = null;
		inputStream = new FileInputStream(file);
		
		byte[] bytes;
		byte[] buffer = new byte[8192];
		int bytesRead;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
		bytes = output.toByteArray();
		return bytes;
	}

	public void InitHost(Aria2ConnectionInfo newAria2ConnectionInfo) {
		Log.i("aria2", "init config!");
		
		if(_aria2ConnectionInfo.configIsChange(newAria2ConnectionInfo))
		{
			Log.i("aria2", "config is changed!");
			
			_aria2ConnectionInfo = newAria2ConnectionInfo;
			try
			{
				if(_aria2ConnectionInfo.canUseAuthentication())
				{
					_aria2 = new Aria2API(_aria2ConnectionInfo._host,_aria2ConnectionInfo._port,_aria2ConnectionInfo._username,_aria2ConnectionInfo._password);
				}
				else
				{
					Log.i("aria2", "host:" + _aria2ConnectionInfo._host + " port:" + _aria2ConnectionInfo._port);
					_aria2 = new Aria2API(_aria2ConnectionInfo._host,_aria2ConnectionInfo._port);
				}
				
			}catch(Exception e)
			{
				_aria2 = null;
				throw new IllegalArgumentException("Aria2 config is error!");
				
			}
			
		}
	}
	
	private void getAllGlobalAndTaskStatus() throws RemoteException
	{
		_updating_status = true;
		/* Notify main UI that we are about to update all status */
		Message sendToUIThreadMsg = new Message();
		sendToUIThreadMsg.what = START_REFRESHING_ALL_STATUS;
		_mRefreshHandler.send(sendToUIThreadMsg);
		
		GlobalStat stat = null;		
		try
		{
			stat = getGlobalStatMessage();
			
		}catch (Exception e) {
			Log.e("aria2", "aria2 get global stat error!",e);
			sendToUIThreadMsg = new Message();
			handlerError(GET_GLOBAL_STAT,sendToUIThreadMsg);
		}
		
		try
		{
			getAllStatusMessage(stat);
		}catch (Exception e) {
			Log.e("aria2", "aria2 get all status error!",e);
		}
		
		/* Notify main UI finish of updating all status */
		sendToUIThreadMsg = new Message();
		sendToUIThreadMsg.what = FINISH_REFRESHING_ALL_STATUS;
		_mRefreshHandler.send(sendToUIThreadMsg);
		_updating_status = false;
	}
	
	public GlobalStat getGlobalStatMessage() throws RemoteException
	{
		Message sendToUIThreadMsg = new Message();
		sendToUIThreadMsg.what = GLOBAL_STAT_REFRESHED;
		GlobalStat stat = _aria2.getGlobalStat();
		sendToUIThreadMsg.obj = stat;
		_mRefreshHandler.send(sendToUIThreadMsg);
		return stat;
	}
	
	private void getAllStatusMessage(GlobalStat statNew) throws RemoteException
	{												
		Message sendToUIThreadMsg = new Message();
		sendToUIThreadMsg.what = ALL_STATUS_REFRESHED;

		ArrayList<ArrayList<Status>> list = new ArrayList<ArrayList<Status>>();		
		if (Integer.valueOf(statNew.numActive)>0) {
			ArrayList<Status> activeList = _aria2.tellActive();
			list.add(activeList);
		}
		if (Integer.valueOf(statNew.numWaiting)>0) {		
			ArrayList<Status> waitingList = _aria2.tellWaiting(0,Integer.valueOf(statNew.numWaiting));
			list.add(waitingList);
		}
		if (Integer.valueOf(statNew.numStopped)>0) {		
			ArrayList<Status> stopList = _aria2.tellStopped(0,Integer.valueOf(statNew.numStopped));		
			list.add(stopList);
		}
		sendToUIThreadMsg.obj = list;
		_mRefreshHandler.send(sendToUIThreadMsg);
	}
	
	 
	
	
	
	private void handlerError(int comeMessage,Message sendToUIThreadMsg) throws RemoteException
	{
		String errorInfo = "aria2 network errors!";
		switch (comeMessage)
		{
			case GET_GLOBAL_STAT:
				errorInfo = "Refresh error!";
				sendErrorInfoToUiThreadAndStopUpdateGlobalStat(sendToUIThreadMsg, errorInfo);
				break;
			case ADD_URI:
				errorInfo = "add uri error!";
				sendErrorInfoToUiThread(sendToUIThreadMsg, errorInfo);
				break;
			case ADD_TORRENT:
				errorInfo = "add torrent error!";
				sendErrorInfoToUiThread(sendToUIThreadMsg, errorInfo);	
				break;
			case ADD_METALINK:
				errorInfo = "add metalink error!";
				sendErrorInfoToUiThread(sendToUIThreadMsg, errorInfo);				
				break;
		}
		
	}

	private void sendErrorInfoToUiThreadAndStopUpdateGlobalStat(Message sendToUIThreadMsg,
			String errorInfo) throws RemoteException
	{
		sendToUIThreadMsg.what = SHOW_ERROR_INFO_STOP_UPDATE_GLOBAL_STAT;
		sendToUIThreadMsg.obj = errorInfo;
		_mRefreshHandler.send(sendToUIThreadMsg);
	}	
	
	private void sendErrorInfoToUiThread(Message sendToUIThreadMsg,
			String errorInfo) throws RemoteException
	{
		sendToUIThreadMsg.what = SHOW_ERROR_INFO;
		sendToUIThreadMsg.obj = errorInfo;
		_mRefreshHandler.send(sendToUIThreadMsg);
	}
	
	
	private String GetVersionInfo()
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
	
	private String GetSessionInfo()
	{
		checkAria2();
		
		StringBuilder session_info = new StringBuilder();
		session_info.append("Session ID : " + _aria2.getSessionInfo().sessionId);
		return session_info.toString();
	}

	private String AddUri(String uri)
	{
		checkAria2();
		String returnValue = _aria2.addUri(
							new DownloadUris(
									uri), _aria2.getGlobalOption());	
		return "Return value : " + returnValue;
	}
	
	

		
	
	private void checkAria2()
	{
		if(_aria2 == null)
		{
			throw new IllegalArgumentException("Aria2 init is error!please check setting!");
		}
	}
	
	public void handleMessage(Message msg, Handler handler) throws RemoteException
	{
		Message sendToUIThreadMsg = new Message();
		Log.i("aria2", "aria2 manager handler get msg:" + msg.what);
		try
		{
			switch (msg.what)
			{
			case INIT_HOST:
				Aria2ConnectionInfo aria2ConnectionInfo = new Aria2ConnectionInfo(_preferencesManager);
				InitHost(aria2ConnectionInfo);
				break;
			case GET_VERSION_INFO:
				sendToUIThreadMsg.what = VERSION_INFO_REFRESHED;
				sendToUIThreadMsg.obj = GetVersionInfo();
				_mRefreshHandler.send(sendToUIThreadMsg);
				break;
			case GET_SESSION_INFO:
				sendToUIThreadMsg.what = SESSION_INFO_REFRESHED;
				sendToUIThreadMsg.obj = GetSessionInfo();
				_mRefreshHandler.send(sendToUIThreadMsg);
				break;
			case ADD_URI:
				if(msg.obj != null)
				{
					sendToUIThreadMsg.what = DOWNLOAD_INFO_REFRESHED;
					sendToUIThreadMsg.obj = AddUri((String)msg.obj);
					_mRefreshHandler.send(sendToUIThreadMsg);
				}
				break;
			case PAUSE_ALL_DOWNLOAD:
				_aria2.pauseAll();
				break;
			case RESUME_ALL_DOWNLOAD:
				_aria2.unpauseAll();
				break;
			case PURGE_DOWNLOAD:
				_aria2.purgeDownloadResult();
				break;
			case PAUSE_DOWNLOAD:
				{
					if(msg.obj == null)
					{
						return;
					}
					String gid = (String)msg.obj;
					_aria2.pause(gid);
				}
				break;
			case RESUME_DOWNLOAD:
				{
					if(msg.obj == null)
					{
						return;
					}
					String gid = (String)msg.obj;
					_aria2.unpause(gid);
				}
				break;
			case REMOVE_DOWNLOAD:
				{
					if(msg.obj == null)
					{
						return;
					}
					String gid = (String)msg.obj;
					_aria2.remove(gid);
				}
				break;
			case REMOVE_DOWNLOAD_RESULT:
				{
					if(msg.obj == null)
					{
						return;
					}
					String gid = (String)msg.obj;
					_aria2.removeDownloadResult(gid);
				}
				break;
			case ADD_TORRENT:
				{
					if(msg.obj == null)
					{
						return;
					}
					File file = (File)msg.obj; 
					addTorrnet(file);
				}
				break;
			case ADD_METALINK:
				{
					if(msg.obj == null)
					{
						return;
					}
					File file = (File)msg.obj; 
					addMetalink(file);
				}
				break;
			case GET_ALL_GLOBAL_AND_TASK_STATUS:
				{
					Log.i("aria2 handler", "begin GET_ALL_GLOBAL_AND_TASK_STATUS!");
					if (_updating_status == false)
					{
						if(msg.obj == null)
						{
							getAllGlobalAndTaskStatus();
							return;
						}
						CountDownLatch finishSignal = (CountDownLatch)msg.obj;
						getAllGlobalAndTaskStatus();
						Log.i("aria2 handler", "end GET_ALL_GLOBAL_AND_TASK_STATUS!");
						finishSignal.countDown();
					}
				}
				break;
			case GET_GLOBAL_OPTION:
				{
					GlobalOptions globalOptions = _aria2.getGlobalOption();
					sendToUIThreadMsg.what =  MSG_GET_GLOBAL_OPTION_SUCCESS;
					sendToUIThreadMsg.obj = globalOptions;
					_mRefreshHandler.send(sendToUIThreadMsg);
				}
				break;
			case CHANGE_GLOBAL_OPTION:
				{
					if(msg.obj == null)
					{
						return;
					}
					GlobalOptions globalOptions = (GlobalOptions)msg.obj;
					_aria2.changeGlobalOption(globalOptions);
				}
				break;
			
			case REMOVE_GET_GLOBAL_OPTION:
				{
					handler.removeMessages(GET_GLOBAL_OPTION);
				}
				break;
			}
		}
		catch (Exception e)
		{
			Log.e("aria2", "aria2 manager handler is error!",e);
			
			handlerError(msg.what,sendToUIThreadMsg);
		}
		
	}
	
}
