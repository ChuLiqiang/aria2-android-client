package org.mariotaku.aria2.android;

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

import org.mariotaku.aria2.Aria2API;
import org.mariotaku.aria2.DownloadUris;
import org.mariotaku.aria2.GlobalStat;
import org.mariotaku.aria2.Options;
import org.mariotaku.aria2.Version;
import org.mariotaku.aria2.Status;
import org.mariotaku.aria2.android.utils.Base64;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.os.Process;

public class Aria2Manager implements Aria2UIMessage,Aria2APIMessage
{
	private Aria2API _aria2 = null;
	private String _host = null;
	private int _port = 6800;
	private String _username = null;
	private String _password = null;
	private Context _context = null;
	
	private Timer mGlobalStatRefreshTimer = null;
	private Handler _mRefreshHandler = null;
	
	public Handler _mHandler = null;
	HandlerThread _aria2APIHandlerThread = null;
	
	public Aria2Manager(Context context,Handler mRefreshHandler)
	{
		Log.i("aria2", "init Aria2Manager!");
		_context = context;
		_mRefreshHandler = mRefreshHandler;
	}
	
	public void StartAria2Handler()
	{
		_aria2APIHandlerThread = new HandlerThread("Aria2 API Handler Thread"); 
		_aria2APIHandlerThread.start();
		
		Looper mLooper = _aria2APIHandlerThread.getLooper(); 
		_mHandler = new Handler(mLooper)
		{
			public void handleMessage(Message msg)
			{
				Message sendToUIThreadMsg = new Message();
				Log.i("aria2", "aria2 manager handler get msg:" + msg.what);
				try
				{
					switch (msg.what)
					{
					case GET_VERSION_INFO:
						sendToUIThreadMsg.what = VERSION_INFO_REFRESHED;
						sendToUIThreadMsg.obj = GetVersionInfo();
						_mRefreshHandler.sendMessage(sendToUIThreadMsg);
						break;
					case GET_SESSION_INFO:
						sendToUIThreadMsg.what = SESSION_INFO_REFRESHED;
						sendToUIThreadMsg.obj = GetSessionInfo();
						_mRefreshHandler.sendMessage(sendToUIThreadMsg);
						break;
					case ADD_URI:
						sendToUIThreadMsg.what = DOWNLOAD_INFO_REFRESHED;
						if(msg.obj == null)
						{
							sendToUIThreadMsg.obj = AddUri();
						}
						else
						{
							sendToUIThreadMsg.obj = AddUri((String)msg.obj);
						}
						_mRefreshHandler.sendMessage(sendToUIThreadMsg);
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
							if(msg.obj == null)
							{
								return;
							}
							CountDownLatch finishSignal = (CountDownLatch)msg.obj;
							getAllGlobalAndTaskStatus();
							Log.i("aria2 handler", "end GET_ALL_GLOBAL_AND_TASK_STATUS!");
							finishSignal.countDown();
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
		};
		
		Process.setThreadPriority(_aria2APIHandlerThread.getThreadId(),Process.THREAD_PRIORITY_BACKGROUND);
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

	public void InitHost() {
		Log.i("aria2", "init config!");
		String nowHost = getHost();
		int nowPort = getPort();
		String nowUsername = getPreferences(SettingsActivity.PREF_KEY_USERNAME);
		String nowPassword = getPreferences(SettingsActivity.PREF_KEY_PASSWORD);
		if(configIsChange(nowHost,nowPort,nowUsername,nowPassword))
		{
			Log.i("aria2", "config is changed!");
			_host = nowHost;
			_port = nowPort;
			_username = nowUsername;
			_password = nowPassword;
			
			try
			{
				if(canUseAuthentication(nowUsername,nowPassword))
				{
					_aria2 = new Aria2API(_host,_port,_username,_password);
				}
				else
				{
					_aria2 = new Aria2API(_host,_port);
				}
				
			}catch(Exception e)
			{
				_aria2 = null;
				throw new IllegalArgumentException("Aria2 config is error!");
				
			}
			
		}
	}
	
	private int getPort() {
		String prefKeyPort = getPreferences(SettingsActivity.PREF_KEY_PORT);
		
		if(prefKeyPort.equals(""))
		{
			throw new IllegalArgumentException("pealse initial host port!");
		}
		int port = -1;
		
		try
		{
			port = Integer.valueOf(prefKeyPort);
		}catch (Exception e) {
			throw new IllegalArgumentException("initial host port error!");
		}
		
		return port;
	}

	private boolean canUseAuthentication(String nowUsername,String nowPassword)
	{
		if(nowUsername.equals("") || nowPassword.equals(""))
		{
			return false;
		}
		return true;
	}
	
	private boolean configIsChange(String nowHost,int nowPort,String nowUsername,String nowPassword)
	{
		if(!nowHost.equals(_host))
		{
			return true;
		}
		
		if(nowPort != _port)
		{
			return true;
		}
			
		if(!nowUsername.equals(_username))
		{
			return true;
		}
		
		if(!nowUsername.equals(_password))
		{
			return false;
		}
		
		return false;
	}
	
	public void StartUpdateGlobalStat()
	{
		Log.i("aria2", "start update global stat!");
		checkAria2();
		mGlobalStatRefreshTimer = new Timer();
		mGlobalStatRefreshTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Log.i("aria2 Timer", "start get all global and task status!");
				CountDownLatch finishSignal = new CountDownLatch(1);
				sendToAria2APIHandlerMsg(GET_ALL_GLOBAL_AND_TASK_STATUS,finishSignal);
				try
				{
					finishSignal.await();
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				Log.i("aria2 Timer", "end get all global and task status!");
				
			}

			

		}, 0, 1000);
	}
	
	private void getAllGlobalAndTaskStatus()
	{
		GlobalStat stat = null;
		try
		{
			stat = getGlobalStatMessage();
			
		}catch (Exception e) {
			Log.e("aria2", "aria2 get global stat error!",e);
			Message sendToUIThreadMsg = new Message();
			handlerError(GET_GLOBAL_STAT,sendToUIThreadMsg);
		}
		
		try
		{
			getAllStatusMessage(stat);
		}catch (Exception e) {
			Log.e("aria2", "aria2 get all status error!",e);
		}
	}
	
	public GlobalStat getGlobalStatMessage()
	{
		Message sendToUIThreadMsg = new Message();
		sendToUIThreadMsg.what = GLOBAL_STAT_REFRESHED;
		GlobalStat stat = _aria2.getGlobalStat();
		sendToUIThreadMsg.obj = stat;
		_mRefreshHandler.sendMessage(sendToUIThreadMsg);
		return stat;
	}
	
	private void getAllStatusMessage(GlobalStat statNew)
	{												
		Message sendToUIThreadMsg = new Message();
		sendToUIThreadMsg.what = ALL_STATUS_REFRESHED;
		ArrayList<Status> activeList = _aria2.tellActive();
		ArrayList<Status> waitingList = _aria2.tellWaiting(0,Integer.valueOf(statNew.numWaiting));
		ArrayList<Status> stopList = _aria2.tellStopped(0,Integer.valueOf(statNew.numStopped));
		ArrayList<ArrayList<Status>> list = new ArrayList<ArrayList<Status>>();
		
		list.add(activeList);
		list.add(waitingList);
		list.add(stopList);
		
		sendToUIThreadMsg.obj = list;
		_mRefreshHandler.sendMessage(sendToUIThreadMsg);
	}
	
	public void sendToAria2APIHandlerMsg(int msgType)
	{
		Message sendToAria2APIHandlerMsg = new Message();
		sendToAria2APIHandlerMsg.what = msgType;
		_mHandler.sendMessage(sendToAria2APIHandlerMsg);
	}
	
	public void sendToAria2APIHandlerMsg(int msgType,Object msgObj)
	{
		Message sendToAria2APIHandlerMsg = new Message();
		sendToAria2APIHandlerMsg.what = msgType;
		sendToAria2APIHandlerMsg.obj = msgObj;
		_mHandler.sendMessage(sendToAria2APIHandlerMsg);
	}
	
	private void handlerError(int comeMessage,Message sendToUIThreadMsg)
	{
		String errorInfo = "aria2 network errors!";
		switch (comeMessage)
		{
			case GET_GLOBAL_STAT:
				errorInfo = "get aria2 global stat error!please check setting!";
				sendErrorInfoToUiThreadAndStopUpdateGlobalStat(sendToUIThreadMsg, errorInfo);
				break;
			case ADD_URI:
				errorInfo = "add uri error!";
				sendErrorInfoToUiThread(sendToUIThreadMsg, errorInfo);
				break;
				
		}
		
	}

	private void sendErrorInfoToUiThreadAndStopUpdateGlobalStat(Message sendToUIThreadMsg,
			String errorInfo)
	{
		sendToUIThreadMsg.what = SHOW_ERROR_INFO_STOP_UPDATE_GLOBAL_STAT;
		sendToUIThreadMsg.obj = errorInfo;
		_mRefreshHandler.sendMessage(sendToUIThreadMsg);
	}	
	
	private void sendErrorInfoToUiThread(Message sendToUIThreadMsg,
			String errorInfo)
	{
		sendToUIThreadMsg.what = SHOW_ERROR_INFO;
		sendToUIThreadMsg.obj = errorInfo;
		_mRefreshHandler.sendMessage(sendToUIThreadMsg);
	}
	
	public void StopUpdateGlobalStat()
	{
		if(mGlobalStatRefreshTimer != null)
		{
			mGlobalStatRefreshTimer.cancel();
			mGlobalStatRefreshTimer = null;
			Log.i("aria2", "aria2 stop update GlobalStat timer!");
		}
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

	
	
	private String GetStatus()
	{
		checkAria2();
		return String.valueOf(_aria2.tellStatus("7", "gid").gid);
	}
	
	private String AddUri()
	{
		checkAria2();
		String returnValue = _aria2.addUri(
							new DownloadUris(
									"http://releases.ubuntu.com/11.10/ubuntu-11.10-desktop-i386.iso.torrent"));	
		return "Return value : " + returnValue;
	}
	
	private String AddUri(String uri)
	{
		checkAria2();
		String returnValue = _aria2.addUri(
							new DownloadUris(
									uri));	
		return "Return value : " + returnValue;
	}
	
	private String getHost() {
		String prefKeyHost = getPreferences(SettingsActivity.PREF_KEY_HOST);
		
		if(prefKeyHost.equals(""))
		{
			throw new IllegalArgumentException("pealse initial host address!");
		}
		
		return prefKeyHost;
	}

	private String getPreferences(String key) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(_context);
		String prefKey= sharedPref.getString(key,"");
		return prefKey;
	}	
	
	private void checkAria2()
	{
		if(_aria2 == null)
		{
			throw new IllegalArgumentException("Aria2 init is error!please check setting!");
		}
	}
	
	public void StopAria2Handler()
	{
	
		if(_aria2APIHandlerThread != null)
		{
			_aria2APIHandlerThread.quit();
		}
	}
	
	


}
