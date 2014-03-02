package tk.igeek.aria2.adapter;


import java.util.ArrayList;
import java.util.List;

import tk.igeek.aria2.Peer;
import tk.igeek.aria2.Server;
import tk.igeek.aria2.Status;
import tk.igeek.aria2.android.Aria2UIMessage;
import tk.igeek.aria2.android.DownloadItemInfoActivity;
import tk.igeek.aria2.android.IIncomingHandler;
import tk.igeek.aria2.android.IncomingHandler;
import tk.igeek.aria2.android.R;
import tk.igeek.aria2.android.service.Aria2APIMessage;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class PeersFragment extends Fragment implements IIncomingHandler {

	private Messenger mMessenger = null;
	private Handler mRefreshHandler = null;
	private Status status = null;
	private String mCurrentGid = null;
	private int commandType = Aria2APIMessage.ERROR_COMMAND;
	
	private PeerItemAdapter peerAdapter = null;
	private List<Peer> peerItems = null;
	
	private ServerItemAdapter serverAdapter = null;
	private List<Server> serverItems = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		Bundle extras = getActivity().getIntent().getExtras();
		if (extras != null) {
			status = extras.getParcelable("downloadItemInfo");
		}
		if (status != null) {
			mCurrentGid = status.gid; 
			if(status.isTorrent())
			{
				commandType = Aria2APIMessage.GET_PEERS;
				
			}
			else
			{
				commandType = Aria2APIMessage.GET_SERVERS;
			}
		}
		
		mRefreshHandler = new IncomingHandler(this);
		mMessenger = new Messenger(mRefreshHandler);
		super.onCreate(savedInstanceState);
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = null; 
		
		if(commandType == Aria2APIMessage.GET_PEERS)
		{
			rootView = inflater.inflate(R.layout.fragment_peers, container, false);
			peerItems = new ArrayList<Peer>();
			peerAdapter = new PeerItemAdapter(getActivity(),R.layout.peer_item,peerItems);
			ListView listview = (ListView) rootView.findViewById(R.id.download_item_info_peers_list_view);
			listview.setAdapter(peerAdapter);
		} 
		else if(commandType == Aria2APIMessage.GET_SERVERS)
		{
			rootView = inflater.inflate(R.layout.fragment_servers, container, false);
			serverItems = new ArrayList<Server>();
			serverAdapter = new ServerItemAdapter(getActivity(),serverItems);
			ExpandableListView expListView = (ExpandableListView) rootView.findViewById(R.id.download_item_info_servers_list_view);;
			expListView.setAdapter(serverAdapter);
		}
		return rootView;
	}

	@Override
	public void onResume() {
		DownloadItemInfoActivity downloadItemInfoActivity = (DownloadItemInfoActivity)getActivity();
		downloadItemInfoActivity.sendToAria2APIHandlerMsg(commandType,mCurrentGid,mMessenger);
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void handleMessage(Message msg, Handler handler) {
		Log.i("aria2", "peers ui handler get msg:" + msg.what);
		try
		{
			switch (msg.what) 
			{
			case Aria2UIMessage.PEERS_REFRESHED:
				{
					ArrayList<Peer> newPeerList = (ArrayList<Peer>)msg.obj; 
					peerAdapter.updateItems(newPeerList);
					peerAdapter.notifyDataSetChanged();
				}
				break;
			case Aria2UIMessage.SERVERS_REFRESHED:
				{
					ArrayList<Server> newServerList = (ArrayList<Server>)msg.obj;
					serverAdapter.updateItems(newServerList);
					serverAdapter.notifyDataSetChanged();
				}
				break;
			}
		}catch (Exception e) {
			Log.e("aria2", "peers ui handler is error!",e);
		}
	}
	

	
}
