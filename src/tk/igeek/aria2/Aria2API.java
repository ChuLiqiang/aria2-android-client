package tk.igeek.aria2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import android.util.Log;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("unchecked")
public class Aria2API {

	private XMLRPCClient mClient;

	public Aria2API() {
		init("localhost", 6800);
	}

	public Aria2API(String host) {
		init(host, 6800);
	}
	
	public Aria2API(String host,int port) {
		init(host, port);
	}
	
	public Aria2API(String host,int port,String username, String password) {
		init(host, port,username,password);
	}

	/**
	 * This method adds new HTTP(S)/FTP/BitTorrent Magnet URI. uris is of type
	 * array and its element is URI which is of type string. For BitTorrent
	 * Magnet URI, uris must have only one element and it should be BitTorrent
	 * Magnet URI. URIs in uris must point to the same file. If you mix other
	 * URIs which point to another file, aria2 does not complain but download
	 * may fail.
	 * 
	 * @deprecated Using default options compiled in aria2 is not recommend,
	 *             please use {@link #addUri(DownloadUuris, GlobalOptions)} instead.
	 * 
	 * @see DownloadUris
	 * 
	 * @return GID of registered download.
	 */
	public String addUri(DownloadUris uris) {

		Object result = callMethod("aria2.addUri", new Object[] { uris.getUris() });
		
		return (String)result;
	}

	/**
	 * This method adds new HTTP(S)/FTP/BitTorrent Magnet URI. uris is of type
	 * array and its element is URI which is of type string. For BitTorrent
	 * Magnet URI, uris must have only one element and it should be BitTorrent
	 * Magnet URI. URIs in uris must point to the same file. If you mix other
	 * URIs which point to another file, aria2 does not complain but download
	 * may fail.
	 * 
	 * @see GlobalOptions
	 * 
	 * @see DownloadUris
	 * 
	 * @return GID of registered download.
	 */
	public String addUri(DownloadUris uris, GlobalOptions options) {

		Object result = callMethod("aria2.addUri", new Object[] { uris.getUris(), options.get() });

		return (String)result;
	}

	/**
	 * This method adds new HTTP(S)/FTP/BitTorrent Magnet URI. uris is of type
	 * array and its element is URI which is of type string. For BitTorrent
	 * Magnet URI, uris must have only one element and it should be BitTorrent
	 * Magnet URI. URIs in uris must point to the same file. If you mix other
	 * URIs which point to another file, aria2 does not complain but download
	 * may fail. If position is given as an integer starting from 0, the new
	 * download is inserted at position in the waiting queue. If position is
	 * larger than the size of the queue, it is appended at the end of the
	 * queue.
	 * 
	 * @see GlobalOptions
	 * 
	 * @see DownloadUris
	 * 
	 * @return GID of registered download.
	 */
	public String addUri(DownloadUris uris, GlobalOptions options, int position) {
		if (position < 0)
			throw new IllegalArgumentException("position can't be a negative value!");
		Object result = callMethod("aria2.addUri", new Object[] { uris.getUris(), options.get(),
				position });
		
		return (String)result;
	}
	
	/**
	 * This method adds BitTorrent download by uploading ".torrent" file. If 
	 * you want to add BitTorrent Magnet URI, use aria2.addUri() method instead.
	 * @param byte[] torrent file
	 * @return GID of registered download
	 */
	public String addTorrent(byte[] torrent) {
		
		String result = (String)callMethod("aria2.addTorrent",torrent);
		
		return result;
	}
	
	/**
	 * This method adds Metalink download by uploading ".metalink" file.
	 * @param bytes metalink file
	 */
	public void addMetalink(byte[] metalink)
	{
		callMethod("aria2.addMetalink",metalink);
	}

	/**
	 * Pauses the download denoted by gid. This method behaves just like
	 * {@link #pause(int)} except that this method pauses download without any
	 * action which takes time such as contacting BitTorrent tracker.
	 * 
	 * @return GID of paused download.
	 */
	public String forcePause(String gid) {

		Object result = callMethod("aria2.forcePause", gid);
		
		return (String) result;
	}

	/**
	 * This method is equal to calling {@link #forcePause(int)} for every
	 * active/waiting download.
	 * 
	 * @return "OK" if succeed.
	 */
	public String forcePauseAll() {
		return (String) callMethod("aria2.forcePauseAll");
	}

	/**
	 * Removes the download denoted by gid. This method behaves just like
	 * {@link #remove(int)} except that this method removes download without any
	 * action which takes time such as contacting BitTorrent tracker.
	 * 
	 * @return GID of removed download.
	 */
	public String forceRemove(String gid) {

		Object result = callMethod("aria2.forceRemove", gid);

		return (String) result;
	}

	/**
	 * Shutdowns aria2. It behaves like {@link #shutdown()} except that any
	 * actions which takes time such as contacting BitTorrent tracker are
	 * skipped.
	 * 
	 * @return "OK" if succeed.
	 */
	public String forceShutdown() {
		return (String) callMethod("aria2.forceShutdown");
	}

	/**
	 * @return global statistics such as overall download and upload speed.
	 */
	public GlobalStat getGlobalStat() {
		return new GlobalStat((HashMap<String, Object>) callMethod("aria2.getGlobalStat"));
	}

	/**
	 * @return session information.
	 */
	public SessionInfo getSessionInfo() {

		return new SessionInfo((HashMap<String, Object>) callMethod("aria2.getSessionInfo"));
	}

	/**
	 * @return version of the program and the list of enabled features.
	 */
	public Version getVersion() {

		HashMap<String, Object> versionInfo = (HashMap<String, Object>) callMethod("aria2.getVersion");
		
		return new Version(versionInfo);
	}

	/**
	 * Pauses the download denoted by gid. The status of paused download becomes
	 * "paused". If the download is active, the download is placed on the first
	 * position of waiting queue. As long as the status is "paused", the
	 * download is not started. To change status to "waiting", use {@link
	 * unpause(int)} method.
	 * 
	 * @return GID of paused download.
	 */
	public String pause(String gid) {

		Object result = callMethod("aria2.pause", gid);

		return (String)result;
	}

	/**
	 * This method is equal to calling {@link #pause(int)} for every
	 * active/waiting download.
	 * 
	 * @return "OK" if succeed.
	 */
	public String pauseAll() {
		return (String) callMethod("aria2.pauseAll");
	}

	/**
	 * Removes the download denoted by gid. If specified download is in
	 * progress, it is stopped at first. The status of removed download becomes
	 * "removed".
	 * 
	 * @return GID of removed download.
	 */
	public String remove(String gid) {

		Object result = callMethod("aria2.remove", gid);

		return (String)result;
	}

	/**
	 * Removes completed/error/removed download denoted by gid from memory.
	 * 
	 * @return "OK" if succeed.
	 */
	public String removeDownloadResult(String gid) {
		return (String) callMethod("aria2.removeDownloadResult", gid);
	}

	/**
	 * Shutdowns aria2.
	 * 
	 * @return "OK" if succeed.
	 */
	public String shutdown() {

		return (String) callMethod("aria2.shutdown");
	}

	/**
	 * This method returns download progress of the download denoted by gid. If
	 * it is specified, the response contains only keys in keys array. If keys
	 * is empty or not specified, the response contains all keys. This is useful
	 * when you just want specific keys and avoid unnecessary transfers. For
	 * example, {@code tellStatus(1, "gid", "status");} returns
	 * {@link Status#gid} and {@link Status#status} key.
	 * 
	 * @return {@link Status}
	 */
	public Status tellStatus(String gid, String... keys) {

		Status status = new Status((HashMap<String, Object>)callMethod("aria2.tellStatus",gid, keys));
		
		return status;
	}
	
	

	/**
	 * This method returns the list of active downloads. The response is of type
	 * array and its element is the same struct returned by aria2.tellStatus() method.
	 * For keys parameter, please refer to aria2.tellStatus() method.
	 * 
	 * @return {@link Status}
	 */
	public ArrayList<Status> tellActive(String... keys)
	{
		Object[] statusObjects = (Object[])callMethod("aria2.tellActive",keys);
		
		return getStatus(statusObjects);
		
	}

	
	/**
	 * This method returns the list of waiting download, including paused downloads.
	 * @param offset is of type integer and specifies the offset from the download waiting at the front.
	 * @param num is of type integer and specifies the number of downloads to be returned.
	 * @param keys
	 * @return {@link Status}
	 */
	 public ArrayList<Status> tellWaiting(int offset,int num,String... keys)
	 {
		 Object[] statusObjects = (Object[])callMethod("aria2.tellWaiting",offset,num,keys);
		 
		 return getStatus(statusObjects);
	 }

	 /**
	 * This method returns the list of stopped download. 
	 * @param offset is of type integer and specifies the offset from the download waiting at the front.
	 * @param num is of type integer and specifies the number of downloads to be returned.
	 * @param keys
	 * @return {@link Status}
	 */
	 public ArrayList<Status> tellStopped(int offset,int num,String... keys)
	 {
		 Object[] statusObjects = (Object[])callMethod("aria2.tellStopped",offset,num,keys);
		 
		 return getStatus(statusObjects);
	 }
	 
	 
	/**
	 * Changes the status of the download denoted by gid from "paused" to
	 * "waiting". This makes the download eligible to restart.
	 * 
	 * @return GID of unpaused download.
	 */
	public String unpause(String gid) {

		Object result = callMethod("aria2.unpause",gid);
		return (String)result;
	}

	/**
	 * This method is equal to calling {@link #unpause(int)} for every
	 * active/waiting download.
	 * 
	 * @return "OK" if succeed.
	 */
	public String unpauseAll() {
		return (String) callMethod("aria2.unpauseAll");
	}
	
	/**
	 * This method purges completed/error/removed downloads to free memory.
	 * @return "OK" if succeed.
	 */
	public String purgeDownloadResult() {
		return (String) callMethod("aria2.purgeDownloadResult");
	}
	
	/**
	 * This method returns global options. The response is of type struct. 
	 * Its key is the name of option. The value type is string. Note that 
	 * this method does not return options which have no default value and 
	 * have not been set by the command-line options, configuration files 
	 * or RPC methods. Because global options are used as a template for 
	 * the options of newly added download, the response contains keys 
	 * returned by aria2.getOption() method.
	 */
	public GlobalOptions getGlobalOption()
	{
		HashMap<String, Object> globalOption= (HashMap<String, Object>)callMethod("aria2.getGlobalOption");
		return new GlobalOptions(globalOption);
	}
	
	/**
	 * This method changes global options dynamically. options is of type struct.
	 * @param options
	 * @return "OK" if succeed
	 */
	public String changeGlobalOption(GlobalOptions options)
	{
		return (String) callMethod("aria2.changeGlobalOption",new Object[] { options.get() });
	}
	
	private Object callMethod(String method, Object... args){
		
		Object response = null;
		
		try {
			response = mClient.callEx(method, args);
		} catch (IllegalArgumentException e) {
			Log.e("aria2", "IllegalArgumentException", e);
			throw new Aria2Exception("xml prc illegal argument!");
		} catch (IllegalStateException e) {
			Log.e("aria2", "IllegalStateException", e);
			throw new Aria2Exception("xml prc illegal State!");
		} catch (XMLRPCException e) {
			Log.e("aria2", "XMLRPCException", e);
			throw new Aria2Exception("xml prc exception!");
		} finally {

		}
		
		return response;
	}

	private void init(String host, int port) {
		mClient = new XMLRPCClient("http://" + host + ":" + port + "/rpc");

	}
	
	private void init(String host, int port, String username, String password) {
		mClient = new XMLRPCClient("http://" + host + ":" + port + "/rpc",username,password);

	}
	
	private ArrayList<Status> getStatus(Object[] statusObjects)
	{
		ArrayList<Status> status = new ArrayList<Status>();
		
		for (Object object : statusObjects)
		{
			HashMap<String, Object> statusObject = (HashMap<String, Object>) object;
			Status statusTemp = new Status(statusObject);
			status.add(statusTemp);
		}
		
		return status;
	}

	
}
