package org.mariotaku.aria2.android;

import java.io.File;

import org.mariotaku.aria2.Status;
import org.mariotaku.aria2.android.utils.CommonUtils;


public class DownloadItem
{
	public String name;
	public String status;
	public String size;
	public int progress;
	 public DownloadItem(){
        super();
    }

	public DownloadItem(Status statusTemp)
	{
		
		if(statusTemp.files == null)
		{
			this.name = "unkow";
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
	}
	
	
}
