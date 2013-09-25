package org.mariotaku.aria2.android;

public interface Aria2UIMessage
{
	final static int GLOBAL_STAT_REFRESHED = 0;
	final static int VERSION_INFO_REFRESHED = 1;
	final static int SESSION_INFO_REFRESHED = 2;
	final static int DOWNLOAD_INFO_REFRESHED = 3;
	final static int ALL_STATUS_REFRESHED = 4;
	
	
	
	final static int SHOW_ERROR_INFO = 50;
	final static int SHOW_ERROR_INFO_STOP_UPDATE_GLOBAL_STAT = 51;
}
