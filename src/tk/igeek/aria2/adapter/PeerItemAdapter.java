package tk.igeek.aria2.adapter;

import java.util.ArrayList;
import java.util.List;

import tk.igeek.aria2.Peer;
import tk.igeek.aria2.android.R;
import tk.igeek.aria2.android.utils.CommonUtils;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PeerItemAdapter extends ArrayAdapter<Peer> {

	
	private Context context;
    private int layoutResourceId;   
    private List<Peer> data = new ArrayList<Peer>();;
	
	public PeerItemAdapter(Context context, int textViewResourceId,
			List<Peer> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.layoutResourceId = textViewResourceId;
		this.data = objects;
		Log.i("aria2", "init PeerItemAdapter!");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
		 View row = convertView;
         PeerItemHolder holder = null;
       
        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new PeerItemHolder();
            holder.ip = (TextView)row.findViewById(R.id.peer_item_ip);
            holder.port = (TextView)row.findViewById(R.id.peer_item_port);
            holder.speed = (TextView)row.findViewById(R.id.peer_item_speed);
            row.setTag(holder);
        }
        else
        {
            holder = (PeerItemHolder)row.getTag();
        }
       
        Peer peer = data.get(position);
        holder.ip.setText("IP:" + peer.ip);
        holder.port.setText("Port:" + peer.port);
        String speedInfo = context.getString(R.string.item_speed_format,
					CommonUtils.formatSpeedString(peer.downloadSpeed),
					CommonUtils.formatSpeedString(peer.uploadSpeed));
        holder.speed.setText("Speed:"+speedInfo);
        return row;
	}
	
	static class PeerItemHolder
    {
        TextView ip;
        TextView port;
        TextView speed;
    }
	
	public void updateItems(List<Peer> newPeerList)
	{
		data.clear();
		data.addAll(newPeerList);
		
	}

	
	
	

}
