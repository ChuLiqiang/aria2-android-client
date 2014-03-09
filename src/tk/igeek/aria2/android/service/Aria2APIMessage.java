package tk.igeek.aria2.android.service;

public interface Aria2APIMessage
{
	final static int GET_GLOBAL_STAT = 0;
	final static int GET_VERSION_INFO = 1;
	final static int GET_SESSION_INFO = 2;
	final static int ADD_URI = 3;
	final static int GET_ALL_STATUS = 4;
	final static int PAUSE_ALL_DOWNLOAD = 5;
	final static int RESUME_ALL_DOWNLOAD = 6;
	final static int PURGE_DOWNLOAD = 7;
	final static int PAUSE_DOWNLOAD = 8;
	final static int RESUME_DOWNLOAD = 9;
	final static int REMOVE_DOWNLOAD = 10;
	final static int REMOVE_DOWNLOAD_RESULT = 11;
	final static int ADD_TORRENT = 12;
	final static int GET_ALL_GLOBAL_AND_TASK_STATUS = 13;
	final static int ADD_METALINK = 14;
	final static int GET_GLOBAL_OPTION = 15;
	final static int CHANGE_GLOBAL_OPTION = 16;
	final static int INIT_HOST = 17;
	final static int REMOVE_GET_GLOBAL_OPTION = 18;
	final static int GET_PEERS = 19;
	final static int GET_SERVERS = 20;
	final static int TELL_STATUS = 21;
	
	
	final static int ERROR_COMMAND = -1;
	
	
	
	
	
	
}
