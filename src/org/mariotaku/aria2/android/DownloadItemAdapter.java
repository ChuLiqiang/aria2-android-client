package org.mariotaku.aria2.android;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DownloadItemAdapter extends ArrayAdapter<DownloadItem>
{

	Context context;
    int layoutResourceId;   
    List<DownloadItem> data = null;
	
	public DownloadItemAdapter(Context context, int textViewResourceId,
			List<DownloadItem> downloadItems)
	{
		super(context, textViewResourceId, downloadItems);
		// TODO Auto-generated constructor stub
		 this.layoutResourceId = textViewResourceId;
         this.context = context;
         this.data = downloadItems;
	}
	
	
			
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		
		 View row = convertView;
         DownloadItemHolder holder = null;
       
        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new DownloadItemHolder();
            holder.name = (TextView)row.findViewById(R.id.download_item_name);
            holder.status = (TextView)row.findViewById(R.id.download_item_status);
            holder.size = (TextView)row.findViewById(R.id.download_item_size);
            row.setTag(holder);
        }
        else
        {
            holder = (DownloadItemHolder)row.getTag();
        }
       
        DownloadItem downloadItem = data.get(position);
        holder.name.setText(downloadItem.name);
        holder.status.setText(downloadItem.status);
        holder.size.setText(downloadItem.size); 
        return row;
		
	}
	
	



	static class DownloadItemHolder
    {
        TextView name;
        TextView status;
        TextView size;
    }
}
