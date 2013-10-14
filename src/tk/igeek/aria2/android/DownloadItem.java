package tk.igeek.aria2.android;

import java.io.File;


import tk.igeek.aria2.Status;
import tk.igeek.aria2.android.utils.CommonUtils;


public class DownloadItem
{
	public String gid;
	public String name;
	public String status;
	public String size;
	public int progress;
	public String downloadSpeed;
	public String uploadSpeed;
	public boolean havaBittorrent = false;
	
	public DownloadItem(){
        super();
    }

	public DownloadItem(Status statusTemp)
	{
		if(statusTemp.files == null)
		{
			this.name = "unknown";
		}
		else
		{
			this.name = CommonUtils.getFileNameFromPath(statusTemp.getFiles().get(0).path);
		}
		this.status = statusTemp.status;
		this.size = CommonUtils.formatSizeString(statusTemp.completedLength) + " of " + CommonUtils.formatSizeString(statusTemp.totalLength);
		double completedLength = Double.valueOf(statusTemp.completedLength);
		double totalLength = Double.valueOf(statusTemp.totalLength);
		this.progress = (int) ((completedLength / totalLength) * 100);
		this.gid = statusTemp.gid;
		if(statusTemp.bittorrent != null)
		{
			havaBittorrent = true;
		}
		downloadSpeed = statusTemp.downloadSpeed;
		uploadSpeed = statusTemp.uploadSpeed;
	}
	
	
}
