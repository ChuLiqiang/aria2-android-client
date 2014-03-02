package tk.igeek.aria2.adapter;

import java.util.ArrayList;
import java.util.List;

import tk.igeek.aria2.Server;
import tk.igeek.aria2.android.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ServerItemAdapter extends ArrayAdapter<Server> {

	
	private Context context;
    private int layoutResourceId;   
    private List<Server> data = new ArrayList<Server>();;
	
	public ServerItemAdapter(Context context, int textViewResourceId,
			List<Server> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.layoutResourceId = textViewResourceId;
		this.data = objects;
		Log.i("aria2", "init ServerItemAdapter!");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
		 View row = convertView;
         ServerItemHolder holder = null;
       
        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ServerItemHolder();
            holder.file_index= (TextView)row.findViewById(R.id.server_item_file_index);
            row.setTag(holder);
        }
        else
        {
            holder = (ServerItemHolder)row.getTag();
        }
       
        Server server = data.get(position);
        holder.file_index.setText("fileIndex:" + server.index);
        return row;
	}
	
	static class ServerItemHolder
    {
        TextView file_index;
    }
	
	public void updateItems(List<Server> newServerList)
	{
		data.clear();
		data.addAll(newServerList);
		
	}
	

}
