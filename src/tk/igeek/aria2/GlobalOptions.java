package tk.igeek.aria2;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import android.R.bool;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;

public class GlobalOptions extends CommonItem implements Parcelable{

	
	public String download_result = null;
	public String log = null;
	public String log_level = null;
	public String max_concurrent_downloads = null;
	public String max_download_result = null;
	public String max_overall_download_limit = null;
	public String max_overall_upload_limit = null;
	public String save_cookies = null;
	public String save_session = null;
	public String server_stat_of = null;

	/**
	 * Use this proxy server for all protocols. To erase previously defined
	 * proxy, use "". You can override this setting and specify a proxy server
	 * for a particular protocol using http_proxy, https_proxy and ftp_proxy
	 * options. This affects all URIs. The format of PROXY is
	 * [http://][USER:PASSWORD@]HOST[:PORT].
	 */
	public String all_proxy = null;

	/**
	 * Set password for {@link #all_proxy) option.
	 */
	public String all_proxy_passwd = null;

	/**
	 * Set user for {@link #all_proxy} option.
	 */
	public String all_proxy_user = null;

	/**
	 * Restart download from scratch if the corresponding control file doesn’t
	 * exist. Default: false
	 * 
	 * @see #auto_file_renaming
	 */
	public String allow_overwrite = null;

	/**
	 * If false is given, aria2 aborts download when a piece length is different
	 * from one in a control file. If true is given, you can proceed but some
	 * download progress will be lost. Default: false
	 */
	public String allow_piece_length_change = null;

	/**
	 * Always resume download. If true is given, aria2 always tries to resume
	 * download and if resume is not possible, aborts download. If false is
	 * given, when all given URIs do not support resume or aria2 encounters N
	 * URIs which does not support resume (N is the value specified using
	 * {@link #max_resume_failure_tries} option), aria2 downloads file from
	 * scratch. Default: true
	 * 
	 * @see #max_resume_failure_tries
	 */
	public String always_resume = null;

	/**
	 * Enable asynchronous DNS. Default: true
	 */
	public String async_dns = null;

	/**
	 * Rename file name if the same file already exists. This option works only
	 * in HTTP(S)/FTP download. The new file name has a dot and a
	 * number(1..9999) appended. Default: true
	 */
	public String auto_file_renaming = null;

	/**
	 * Enable Local Peer Discovery. If a private flag is set in a torrent, aria2
	 * doesn’t use this feature for that download even if true is given.
	 * Default: false
	 */
	public String bt_enable_lpd = null;

	/**
	 * Comma separated list of BitTorrent tracker’s announce URI to remove. You
	 * can use special value "*" which matches all URIs, thus removes all
	 * announce URIs.
	 * 
	 * @see #bt_tracker
	 */
	public String bt_exclude_tracker = null;

	/**
	 * Specify the external IP address to report to a BitTorrent tracker.
	 * Although this function is named "external", it can accept any kind of IP
	 * addresses. IPADDRESS must be a numeric IP address.
	 */
	public String bt_external_ip = null;

	/**
	 * If true is given, after hash check using {@link #check_integrity} option
	 * and file is complete, continue to seed file. If you want to check file
	 * and download it only when it is damaged or incomplete, set this option to
	 * false. This option has effect only on BitTorrent download. Default:
	 * {@value #bt_hash_check_seed}
	 */
	public String bt_hash_check_seed = null;

	/**
	 * Specify maximum number of files to open in each BitTorrent download.
	 * Default: 100
	 */
	public String bt_max_open_files = null;

	/**
	 * Specify the maximum number of peers per torrent. 0 means unlimited. See
	 * also bt_request_peer_speed_limit option. Default: {@value #bt_max_peers}
	 */
	public String bt_max_peers = null;

	/**
	 * Download metadata only. The file(s) described in metadata will not be
	 * downloaded. This option has effect only when BitTorrent Magnet URI is
	 * used. Default: false
	 * 
	 * @see #bt_save_metadata
	 */
	public String bt_metadata_only = null;

	/**
	 * Set minimum level of encryption method. If several encryption methods are
	 * provided by a peer, aria2 chooses the lowest one which satisfies the
	 * given level. Possible Values: plain, arc4. Default: plain
	 */
	public String bt_min_crypto_level = null;

	/**
	 * Try to download first and last pieces of each file first. This is useful
	 * for previewing files. The argument can contain 2 keywords: head and tail.
	 * To include both keywords, they must be separated by comma. These keywords
	 * can take one parameter, SIZE. For example, if head=SIZE is specified,
	 * pieces in the range of first SIZE bytes of each file get higher priority.
	 * tail=SIZE means the range of last SIZE bytes of each file. SIZE can
	 * include K or M(1K = 1024, 1M = 1024K). If SIZE is omitted, SIZE=1M is
	 * used. Example : {@code head[=SIZE],tail[=SIZE]}
	 */
	public String bt_prioritize_piece = null;

	
	public String bt_remove_unselected_file = null;
	
	/**
	 * If the whole download speed of every torrent is lower than SPEED, aria2
	 * temporarily increases the number of peers to try for more download speed.
	 * Configuring this option with your preferred download speed can increase
	 * your download speed in some cases. You can append K or M(1K = 1024, 1M =
	 * 1024K). Default: 50K
	 */
	public String bt_request_peer_speed_limit = null;

	/**
	 * If true is given, aria2 doesn’t accept and establish connection with
	 * legacy BitTorrent handshake(BitTorrent protocol). Thus aria2 always uses
	 * Obfuscation handshake. Default: false
	 */
	public String bt_require_crypto = null;

	/**
	 * Save metadata as .torrent file. This option has effect only when
	 * BitTorrent Magnet URI is used. The filename is hex encoded info hash with
	 * suffix .torrent. The directory to be saved is the same directory where
	 * download file is saved. If the same file already exists, metadata is not
	 * saved. Default: false
	 * 
	 * @see #bt_metadata_only
	 */
	public String bt_save_metadata = null;

	/**
	 * Seed previously downloaded files without verifying piece hashes. Default:
	 * false
	 */
	public String bt_seed_unverified = null;

	/**
	 * Stop BitTorrent download if download speed is 0 in consecutive SEC
	 * seconds. If 0 is given, this feature is disabled. Default: 0
	 */
	public String bt_stop_timeout = null;

	/**
	 * Comma separated list of additional BitTorrent tracker’s announce URI.
	 * These URIs are not affected by {@link #bt_exclude_tracker} option because
	 * they are added after URIs in {@link #bt_exclude_tracker} option are
	 * removed.
	 */
	public String bt_tracker = null;

	/**
	 * Set the connect timeout in seconds to establish connection to tracker.
	 * After the connection is established, this option makes no effect and
	 * {@link #bt_tracker_timeout} option is used instead. Default: 60
	 */
	public String bt_tracker_connect_timeout = null;

	/**
	 * Set the interval in seconds between tracker requests. This completely
	 * overrides interval value and aria2 just uses this value and ignores the
	 * min interval and interval value in the response of tracker. If 0 is set,
	 * aria2 determines interval based on the response of tracker and the
	 * download progress. Default: 0
	 */
	public String bt_tracker_interval = null;

	/**
	 * Set timeout in seconds. Default: 60
	 */
	public String bt_tracker_timeout = null;

	/**
	 * Check file integrity by validating piece hashes or a hash of entire file.
	 * This option has effect only in BitTorrent, Metalink downloads with
	 * checksums or HTTP(S)/FTP downloads with --checksum option. If piece
	 * hashes are provided, this option can detect damaged portions of a file
	 * and re-download them. If a hash of entire file is provided, hash check is
	 * only done when file has been already download. This is determined by file
	 * length. If hash check fails, file is re-downloaded from scratch. If both
	 * piece hashes and a hash of entire file are provided, only piece hashes
	 * are used. Default: false
	 */
	public String check_integrity = null;

	/**
	 * Download file only when the local file is older than remote file. This
	 * function only works with HTTP(S) downloads only. It does not work if file
	 * size is specified in Metalink. It also ignores Content-Disposition
	 * header. If a control file exists, this option will be ignored. This
	 * function uses If-Modified-Since header to get only newer file
	 * conditionally. When getting modification time of local file, it uses user
	 * supplied filename(see --out option) or filename part in URI if --out is
	 * not specified. To overwrite existing file, --allow-overwrite is required.
	 * Default: false
	 */
	public String conditional_get = null;

	/**
	 * Set the connect timeout in seconds to establish connection to
	 * HTTP/FTP/proxy server. After the connection is established, this option
	 * makes no effect and --timeout option is used instead. Default: 60
	 */
	public String connect_timeout = null;

	/**
	 * Continue downloading a partially downloaded file. Use this option to
	 * resume a download started by a web browser or another program which
	 * downloads files sequentially from the beginning. Currently this option is
	 * only applicable to HTTP(S)/FTP downloads.
	 */
	public String continue_download = null;

	/**
	 * The directory to store the downloaded file. Default : /sdcard/Download
	 */
	public String dir = null;

	/**
	 * If true is given, aria2 just checks whether the remote file is available
	 * and doesn’t download data. This option has effect on HTTP/FTP download.
	 * BitTorrent downloads are canceled if true is specified. Default: false
	 */
	public String dry_run = null;

	/**
	 * Enable IPv6 name resolution in asynchronous DNS resolver. This option
	 * will be ignored when {@link #async_dns} is false. Default: false
	 */
	public String enable_async_dns6 = null;

	/**
	 * Enable HTTP/1.1 persistent connection. Default: true
	 */
	public String enable_http_keep_alive = null;

	/**
	 * Enable HTTP/1.1 pipelining.<br>
	 * <br>
	 * 
	 * Default: <i>false</i>
	 */
	public String enable_http_pipelining = null;

	/**
	 * Enable Peer Exchange extension. If a private flag is set in a torrent,
	 * this feature is disabled for that download even if true is given.<br>
	 * <br>
	 * 
	 * Default: <i>true</i>
	 */
	public String enable_peer_exchange = null;

	/**
	 * Specify file allocation method. none doesn’t pre-allocate file space.
	 * prealloc pre-allocates file space before download begins. This may take
	 * some time depending on the size of the file. If you are using newer file
	 * systems such as ext4 (with extents support), btrfs, xfs or NTFS(MinGW
	 * build only), falloc is your best choice. It allocates large(few GiB)
	 * files almost instantly. Don’t use falloc with legacy file systems such as
	 * ext3 and FAT32 because it takes almost same time as prealloc and it
	 * blocks aria2 entirely until allocation finishes. falloc may not be
	 * available if your system doesn’t have <b>{@code posix_fallocate()}</b>
	 * function.<br>
	 * <br>
	 * Possible Values: <i>none</i>, <i>prealloc</i>, <i>falloc</i> <br>
	 * Default: <i>prealloc</i>
	 */
	public String file_allocation = null;

	/**
	 * If true or mem is specified, when a file whose suffix is ".meta4" or
	 * ".metalink" or content type of "application/metalink4+xml" or
	 * "application/metalink+xml" is downloaded, aria2 parses it as a metalink
	 * file and downloads files mentioned in it. If mem is specified, a metalink
	 * file is not written to the disk, but is just kept in memory. If false is
	 * specified, the action mentioned above is not taken.<br>
	 * <br>
	 * Possible Values : <i>true</i>, <i>false</i>, <i>mem</i><br>
	 * Default: <i>true</i>
	 */
	public String follow_metalink = null;

	/**
	 * If true or mem is specified, when a file whose suffix is ".torrent" or
	 * content type is "application/x-bittorrent" is downloaded, aria2 parses it
	 * as a torrent file and downloads files mentioned in it. If mem is
	 * specified, a torrent file is not written to the disk, but is just kept in
	 * memory. If false is specified, the action mentioned above is not taken.<br>
	 * <br>
	 * Possible Values : <i>true</i>, <i>false</i>, <i>mem</i><br>
	 * Default: <i>true</i>
	 */
	public String follow_torrent = null;

	/**
	 * Set FTP password. This affects all URIs. If user name is embedded but
	 * password is missing in URI, aria2 tries to resolve password using .netrc.
	 * If password is found in .netrc, then use it as password. If not, use the
	 * password specified in this option. Default: ARIA2USER@
	 */
	public String ftp_passwd = null;

	/**
	 * Use the passive mode in FTP. If false is given, the active mode will be
	 * used. Default: true
	 */
	public String ftp_pasv = null;

	/**
	 * Use this proxy server for FTP. To erase previously defined proxy, use "".
	 * See also --all-proxy option. This affects all URIs. The format of PROXY
	 * is [http://][USER:PASSWORD@]HOST[:PORT]
	 */
	public String ftp_proxy = null;

	/**
	 * Set password for {@link #ftp_proxy} option.
	 */
	public String ftp_proxy_passwd = null;

	/**
	 * Set user for {@link #ftp-proxy} option.
	 */
	public String ftp_proxy_user = null;

	/**
	 * Reuse connection in FTP. Default: true
	 */
	public String ftp_reuse_connection = null;

	/**
	 * Set FTP transfer type.<br>
	 * <br>
	 * Possible Values: <i>binary</i>, <i>ascii</i> <br>
	 * Default: <i>binary</i>
	 */
	public String ftp_type = null;

	/**
	 * Set FTP user. This affects all URIs.<br>
	 * <br>
	 * Default: <i>anonymous</i>
	 */
	public String ftp_user = null;

	
	public String enable_mmap = null;
	public String force_save = null;
	public String hash_check_only = null;
	public String header = null;
	public String http_accept_gzip = null;
	public String http_auth_challenge = null;
	public String http_no_cache = null;
	public String http_passwd = null;
	public String http_proxy = null;
	public String http_proxy_passwd = null;
	public String http_proxy_user = null;
	public String http_user = null;
	public String https_proxy = null;
	public String https_proxy_passwd = null;
	public String https_proxy_user = null;
	public String lowest_speed_limit = null;
	public String max_connection_per_server = null;
	public String max_download_limit = null;
	public String max_file_not_found = null;
	public String max_resume_failure_tries = null;
	public String max_tries = null;
	public String max_upload_limit = null;
	public String metalink_base_uri = null;
	public String metalink_enable_unique_protocol = null;
	public String metalink_language = null;
	public String metalink_location = null;
	public String metalink_os = null;
	public String metalink_preferred_protocol = null;
	public String metalink_version = null;
	public String min_split_size = null;
	public String no_file_allocation_limit = null;
	public String no_netrc = null;
	public String no_proxy = null;
	public String parameterized_uri = null;
	public String piece_length = null;
	public String proxy_method = null;
	public String realtime_chunk_checksum = null;
	public String referer = null;
	public String remote_time = null;
	public String remove_control_file = null;
	public String retry_wait = null;
	public String reuse_uri = null;
	public String rpc_save_upload_metadata = null;
	public String seed_ratio = null;
	public String seed_time = null;
	public String split = null;
	public String stream_piece_selector = null;
	public String timeout = null;
	public String uri_selector = null;
	public String use_head = null;
	public String user_agent = null;
	
	public GlobalOptions() {

	}

	public GlobalOptions(HashMap<String, Object> data) {
		init(data);
	}
	
	public void SetGlobalOptionsActivity(Context _context)
	{
		Field[] fields = getClass().getFields();
		try {
			for (Field field : fields) {
				if (field.getModifiers() == Modifier.PUBLIC) {
					String name = field.getName();
					Object value = field.get(this);
					if (field.getType() == String.class) {
						if (value == null || "".equals(value)) continue;
					}
					
					name = "pref_key_" + name;
					
					Log.d("aria2 global options","name:" + name + " value:" + value);
			
					SharedPreferences.Editor sharedPref = PreferenceManager.getDefaultSharedPreferences(_context).edit();
					if(typeIsBoolean(name))
					{
						sharedPref.putBoolean(name, Boolean.valueOf((String) value));
					}
					else
					{
						sharedPref.putString(name,(String)value);
					}
					
					sharedPref.commit();

				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	private static final Set<String> BOOL_TYPE_VALUES = new HashSet<String>(Arrays.asList(
     new String[] {"pref_key_check_integrity","pref_key_continue","pref_key_check_certificate","pref_key_http_accept_gzip","pref_key_http_auth_challenge",
    		       "pref_key_http_no_cache","pref_key_enable_http_keep_alive","pref_key_enable_http_pipelining","pref_key_use_head","pref_key_ftp_pasv",
    		       "pref_key_ftp_reuse_connection","pref_key_dry_run","pref_key_no_netrc","pref_key_remote_time","pref_key_reuse_uri","pref_key_bt_enable_lpd",
    		       "pref_key_bt_hash_check_seed","pref_key_bt_metadata_only","pref_key_bt_remove_unselected_file","pref_key_bt_require_crypto","pref_key_bt_save_metadata",
    		       "pref_key_bt_seed_unverified","pref_key_enable_peer_exchange","pref_key_metalink_enable_unique_protocol","pref_key_show_files","pref_key_rpc_save_upload_metadata",
    		       "pref_key_allow_overwrite","pref_key_allow_piece_length_change","pref_key_always_resume","pref_key_async_dns","pref_key_auto_file_renaming",
    		       "pref_key_conditional_get","pref_key_enable_mmap","pref_key_force_save","pref_key_hash_check_only","pref_key_parameterized_uri","pref_key_realtime_chunk_checksump",
    		       "pref_key_remove_control_file"}
	));
	
	boolean typeIsBoolean(String name)
	{
		boolean ishava = BOOL_TYPE_VALUES.contains(name);
		return ishava;

	}

	public HashMap<String, Object> get() {
		Field[] fields = getClass().getFields();
		HashMap<String, Object> map = new HashMap<String, Object>();

		try {
			for (Field field : fields) {
				if (field.getModifiers() == Modifier.PUBLIC) {
					String name = field.getName();
					name = name.replaceAll("_", "-");
					if ("continue-download".equals(name)) name = "continue";
					Object value = field.get(this);
					if (field.getType() == String.class) {
						if (value == null || "".equals(value)) continue;
					}
					map.put(name, value);
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return map;
	}

	public void setField(String key, SharedPreferences sharedPreferences) 
	{
		Log.d("aria2 global options","change name:" + key);
		String value = null;
		if(typeIsBoolean(key))
		{
			boolean bValue = sharedPreferences.getBoolean(key,true);
			value = Boolean.toString(bValue);
		}
		else
		{
			value = sharedPreferences.getString(key,"");
		}
		String pref = "pref_key_";
		String name = key.substring(pref.length(),key.length());
		
		if ("continue".equals(name))
		{ 
			name = "continue_download";
		}
		try
		{
			Field field = getClass().getField(name);
			field.set(this, value);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		
		
	}

	
	
	public static final Parcelable.Creator<GlobalOptions> CREATOR =
			new Parcelable.Creator<GlobalOptions>(){

	    @Override
	    public GlobalOptions createFromParcel(Parcel source) {
	     return new GlobalOptions(source);
	    }
	
	    @Override
	    public GlobalOptions[] newArray(int size) {
	     return new GlobalOptions[size];
	    }
	};
	
	@Override
	public int describeContents()
	{

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		write(dest); 
		
	}
	
	 private void readFromParcel(Parcel in) 
	 {    
		read(in); 
     }  
	 
	 public GlobalOptions(Parcel source) {
		 readFromParcel(source);
	 }
	 
	 public void write(Parcel dest) {
		Field[] fields = getClass().getFields();

		try {
			for (Field field : fields) {
				if (field.getModifiers() == Modifier.PUBLIC) {
					Object value = field.get(this);
					dest.writeString((String) value);
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	 
	 public void read(Parcel in) {
		Field[] fields = getClass().getFields();

		try {
			for (Field field : fields) {
				if (field.getModifiers() == Modifier.PUBLIC) {
					field.set(this,in.readString());
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}