package tk.igeek.aria2.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tk.igeek.aria2.Status;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
			TextView downloadItemInfoName = (TextView)findViewById(R.id.download_item_info_name_values);
			downloadItemInfoName.setText(status.getName());
			
			ListView listview = (ListView) findViewById(R.id.download_item_info_file_list_view);
			
			
			
			final ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < status.filesList.size(); ++i) {
				list.add(status.filesList.get(i).path);
			}	
			
			final StableArrayAdapter adapter = new StableArrayAdapter(this,android.R.layout.simple_list_item_1, list);
			listview.setAdapter(adapter);
				
			listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, final View view,
						int position, long id) {
					final String item = (String) parent.getItemAtPosition(position);
					view.animate().setDuration(2000).alpha(0)
					.withEndAction(new Runnable() {
						@Override
						public void run() {
							list.remove(item);
							adapter.notifyDataSetChanged();
							view.setAlpha(1);
						}
					});
				}

    });
			
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
	
	private class StableArrayAdapter extends ArrayAdapter<String> {

    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

    public StableArrayAdapter(Context context, int textViewResourceId,
        List<String> objects) {
      super(context, textViewResourceId, objects);
      for (int i = 0; i < objects.size(); ++i) {
        mIdMap.put(objects.get(i), i);
      }
    }

    @Override
    public long getItemId(int position) {
      String item = getItem(position);
      return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
      return true;
    }

  }

}
