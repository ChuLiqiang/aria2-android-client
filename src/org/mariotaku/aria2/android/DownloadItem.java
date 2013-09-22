package org.mariotaku.aria2.android;

public class DownloadItem
{
	public String name;
	public String status;
	public String size;
	
	 public DownloadItem(){
        super();
    }
	
	public DownloadItem(String name, String status, String size)
	{
		super();
		this.name = name;
		this.status = status;
		this.size = size;
	}
	
	
}
