package tk.igeek.aria2.android;

import java.util.ArrayList;
import java.util.List;

import tk.igeek.aria2.android.utils.CommonUtils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DownloadItemAdapter extends ArrayAdapter<DownloadItem>
{


	private Context context;
    private int layoutResourceId;   
    private List<DownloadItem> data = new ArrayList<DownloadItem>();;  


	public DownloadItemAdapter(Context context, int textViewResourceId,
			List<DownloadItem> objects)
	{
		super(context, textViewResourceId, objects);
		this.context = context;
		this.layoutResourceId = textViewResourceId;
		this.data = objects;
		Log.i("aria2", "init DownloadItemAdapter!");
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
            holder.speed = (TextView)row.findViewById(R.id.download_item_speed);
            holder.size = (TextView)row.findViewById(R.id.download_item_size);
            holder.progressBar = (ProgressBar)row.findViewById(R.id.download_item_progress_bar); 
            row.setTag(holder);
        }
        else
        {
            holder = (DownloadItemHolder)row.getTag();
        }
       
        DownloadItem downloadItem = data.get(position);
        holder.name.setText(downloadItem.name);
        holder.status.setText("Status:" + downloadItem.status);
        
        if(downloadItem.status.equals("active"))
        {
	        holder.speed.setVisibility(View.VISIBLE);
			String speedInfo = context.getString(R.string.item_speed_format,
					CommonUtils.formatSpeedString(downloadItem.downloadSpeed),
					CommonUtils.formatSpeedString(downloadItem.uploadSpeed));
			holder.speed.setText("Speed:"+speedInfo);
		}
        else
        {
        	holder.speed.setVisibility(View.GONE);
        }
		
		
		
        holder.size.setText("Size:" + downloadItem.size); 
        holder.progressBar.setProgress(downloadItem.progress);
        return row;
		
	}
	
	static class DownloadItemHolder
    {
        TextView name;
        TextView status;
        TextView speed;
        TextView size;
        ProgressBar progressBar;
    }

	public void updateItems(List<DownloadItem> downloadItemsNew)
	{
		data.clear();
		data.addAll(downloadItemsNew);
		
	}
}
