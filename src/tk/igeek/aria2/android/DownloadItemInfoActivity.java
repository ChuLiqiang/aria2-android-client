package tk.igeek.aria2.android;

import tk.igeek.aria2.Status;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DownloadItemInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_item_info);
		
		Bundle extras = getIntent().getExtras();
		Status status = null;
		if (extras != null) {
		    status = extras.getParcelable("downloadItemInfo");
		}
		if(status != null)
		{
			TextView downloadItemInfoName = (TextView)findViewById(R.id.download_item_info_name);
			downloadItemInfoName.setText(status.filesList.get(0).path);
			
		}
		
		
	}

	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

}
