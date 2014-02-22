package tk.igeek.aria2.adapter;


import tk.igeek.aria2.Status;
import tk.igeek.aria2.android.Aria2APIMessage;
import tk.igeek.aria2.android.DownloadItemInfoActivity;
import tk.igeek.aria2.android.IIncomingHandler;
import tk.igeek.aria2.android.IncomingHandler;
import tk.igeek.aria2.android.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PeersFragment extends Fragment implements IIncomingHandler {

	private Messenger mMessenger = null;
	private Handler mRefreshHandler = null;
	private Status status = null;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle extras = getActivity().getIntent().getExtras();
		if (extras != null) {
			status = extras.getParcelable("downloadItemInfo");
		}
		mRefreshHandler = new IncomingHandler(this);
		mMessenger = new Messenger(mRefreshHandler);
		super.onCreate(savedInstanceState);
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_peers, container, false);
		if (status != null) {
			if(status.isTorrent())
			{
				//DownloadItemInfoActivity downloadItemInfoActivity = (DownloadItemInfoActivity)getActivity();
				//downloadItemInfoActivity.sendToAria2APIHandlerMsg(Aria2APIMessage.GET_VERSION_INFO,mMessenger);
			}
			else
			{
				
			}
		}
		return rootView;
	}

	

	@Override
	public void handleMessage(Message msg, Handler handler) {
		
		int a = 3;
	}
	

	
}
