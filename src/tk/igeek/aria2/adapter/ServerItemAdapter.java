package tk.igeek.aria2.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tk.igeek.aria2.Server;
import tk.igeek.aria2.Servers;
import tk.igeek.aria2.android.R;
import tk.igeek.aria2.android.utils.CommonUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ServerItemAdapter extends BaseExpandableListAdapter{

	private Context _context = null;
    private List<Server> _listData = null; // header titles
    
	public ServerItemAdapter(Context _context, List<Server> _listData) {
		super();
		this._context = _context;
		this._listData = _listData;
	}

	@Override
	public int getGroupCount() {
		return _listData.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return _listData.get(groupPosition).serversList.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return _listData.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return _listData.get(groupPosition).serversList.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		Server server = (Server) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.server_item_group, null);
		}

		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.server_item_file_header);
		lblListHeader.setText(server.index);

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final Servers childText = (Servers) getChild(groupPosition, childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.server_item, null);
		}

		TextView serverItemServerUri = (TextView) convertView.findViewById(R.id.server_item_server_uri);
		serverItemServerUri.setText("uri:" + childText.uri);
		
		TextView serverItemCurrentUri = (TextView) convertView.findViewById(R.id.server_item_current_uri);
		serverItemCurrentUri.setText("current uri:" + childText.currentUri);
		
		TextView serverItemDownloadSpeed= (TextView) convertView.findViewById(R.id.server_item_download_speed);
		serverItemDownloadSpeed.setText("download:" + CommonUtils.formatSpeedString(childText.downloadSpeed));
		
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public void updateItems(List<Server> newPeerList)
	{
		_listData.clear();
		_listData.addAll(newPeerList);
		
	}

}
