package org.mariotaku.aria2.android;

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
	
	
}
