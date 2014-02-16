package tk.igeek.aria2.adapter;


import tk.igeek.aria2.Status;
import tk.igeek.aria2.android.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PeersFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_peers, container, false);
		
		Bundle extras = getActivity().getIntent().getExtras();
		Status status = null;
		if (extras != null) {
			status = extras.getParcelable("downloadItemInfo");
		}
		
		if (status != null) {
			if(status.isTorrent())
			{
				
			}
		}
		
		return rootView;
	}
}
