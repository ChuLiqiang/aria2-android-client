package tk.igeek.aria2.android;

import java.io.File;


import tk.igeek.aria2.Status;
import tk.igeek.aria2.android.utils.CommonUtils;


public class DownloadItem
{
	private String gid;
	private String name;
	private String status;
	private String size;
	private int progress;
	private String downloadSpeed;
	private String uploadSpeed;
	private boolean haveBittorrent = false;
	
	private Status baseStatusInfo = null;
	
	

	public DownloadItem(){
        super();
    }

	public DownloadItem(Status statusTemp)
	{
		baseStatusInfo = statusTemp;
		this.name = statusTemp.getName();
		this.status = statusTemp.status;
		this.size = CommonUtils.formatSizeString(statusTemp.completedLength) + " of " + CommonUtils.formatSizeString(statusTemp.totalLength);
		double completedLength = Double.valueOf(statusTemp.completedLength);
		double totalLength = Double.valueOf(statusTemp.totalLength);
		this.progress = (int) ((completedLength / totalLength) * 100);
		this.gid = statusTemp.gid;
		if(statusTemp.bittorrent != null)
		{
			haveBittorrent = true;
		}
		downloadSpeed = statusTemp.downloadSpeed;
		uploadSpeed = statusTemp.uploadSpeed;
	}

	public String getGid() {
		return gid;
	}

	public String getName() {
		return name;
	}

	public String getStatus() {
		return status;
	}

	public String getSize() {
		return size;
	}

	public int getProgress() {
		return progress;
	}

	public String getDownloadSpeed() {
		return downloadSpeed;
	}

	public String getUploadSpeed() {
		return uploadSpeed;
	}

	public boolean isHaveBittorrent() {
		return haveBittorrent;
	}
	
	public Status getBaseStatusInfo() {
		return baseStatusInfo;
	}
	
	
}
