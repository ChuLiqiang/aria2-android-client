package tk.igeek.aria2.adapter;

import tk.igeek.aria2.Status;
import tk.igeek.aria2.android.R;
import tk.igeek.aria2.android.utils.CommonUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GeneralFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_general, container, false);
		Bundle extras = getActivity().getIntent().getExtras();
		Status status = null;
		if (extras != null) {
			status = extras.getParcelable("downloadItemInfo");
		}
		
		if (status != null) {
			
			//set tranfer 
			TextView downloadItemInfoStatus = (TextView)rootView.findViewById(R.id.download_item_info_status);
			downloadItemInfoStatus.setText(status.status);
			
			TextView downloadItemInfoDownloaded = (TextView)rootView.findViewById(R.id.download_item_info_downloaded);
			downloadItemInfoDownloaded.setText(CommonUtils.formatSizeString(status.completedLength));
			
			
			
			TextView downloadItemInfoDownloadedSpeed = (TextView)rootView.findViewById(R.id.download_item_info_downloadspeed);
			downloadItemInfoDownloadedSpeed.setText(CommonUtils.formatSizeString(status.downloadSpeed) + "/s");
			
			TextView downloadItemInfoSeeds = (TextView)rootView.findViewById(R.id.download_item_info_seeds);
			downloadItemInfoSeeds .setText(status.numSeeders);
			
			TextView downloadItemInfoError = (TextView)rootView.findViewById(R.id.download_item_info_error);
			downloadItemInfoError.setText(status.errorCode);
			
			TextView downloadItemInfoUploaded = (TextView)rootView.findViewById(R.id.download_item_info_uploaded);
			downloadItemInfoUploaded.setText(CommonUtils.formatSizeString(status.uploadLength));
			
			TextView downloadItemInfoUploadSpeed = (TextView)rootView.findViewById(R.id.download_item_info_uploadspeed);
			downloadItemInfoUploadSpeed.setText(CommonUtils.formatSizeString(status.uploadSpeed) + "/s");
			
			TextView downloadItemInfoEta = (TextView)rootView.findViewById(R.id.download_item_info_eta);
			downloadItemInfoEta.setText(status.getETA());
			
			
			TextView downloadItemInfoShareRatio = (TextView)rootView.findViewById(R.id.download_item_info_share_ratio);
			downloadItemInfoShareRatio.setText(status.getRatio()); 
			
			//set information
			TextView downloadItemInfoName = (TextView)rootView.findViewById(R.id.download_item_info_name);
			downloadItemInfoName.setText(status.getName());
			
			TextView downloadItemInfoTotalSize= (TextView)rootView.findViewById(R.id.download_item_info_total_size);
			downloadItemInfoTotalSize.setText(CommonUtils.formatSizeString(status.totalLength));
			
			TextView downloadItemInfoHash = (TextView)rootView.findViewById(R.id.download_item_info_hash);
			downloadItemInfoHash.setText(status.infoHash);
			
			TextView downloadItemInfoPieces = (TextView)rootView.findViewById(R.id.download_item_info_pieces);
			downloadItemInfoPieces.setText(status.numPieces);
			
			TextView downloadItemInfoPath = (TextView)rootView.findViewById(R.id.download_item_info_path);
			downloadItemInfoPath.setText(status.dir);
			
			
			
			
			
			
		}
		
		return rootView;
	}
}
