package tk.igeek.aria2.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;

public class DownloadItemDialogFragment extends DialogFragment implements Aria2APIMessage
{
	
	private int action;
	private String gid;
	private Map<String, Integer> actionMap = new HashMap<String,Integer>();
	
	/* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface DownloadItemDialogListener {
        public void onDialogActionClick(DialogFragment dialog);
    }
    
    // Use this instance of the interface to deliver action events
    DownloadItemDialogListener mListener;
	
	
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DownloadItemDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
    
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		
		
		String status = getArguments().getString("itemStatus");
		initActionMap(status);
		String gid = getArguments().getString("itemGid");
		this.gid = gid;
		boolean havaBittorrent = getArguments().getBoolean("havaBittorrent");
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		List<CharSequence> actionList = new ArrayList<CharSequence>();

		if(status.equals("active"))
		{
			actionList.add(getString(R.string.pause));
		}
		
		if(status.equals("paused") || status.equals("waiting"))
		{
			actionList.add(getString(R.string.resume));
		}
		
		actionList.add(getString(R.string.remove));
		/*
		if(!status.equals("paused") && !status.equals("active") && !havaBittorrent)
		{
			actionList.add(getString(R.string.restart));
		}
		*/
		
		actionList.add(getString(R.string.cancel));
		
		
		
		final CharSequence[] items = actionList.toArray(new CharSequence[actionList
				.size()]);

		builder.setTitle(R.string.pick_download_item_action).setItems(items,
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						// The 'which' argument contains the index position
						// of the selected item
						String item = items[which].toString() ;
						if(actionMap.containsKey(item))
						{
							action = actionMap.get(item);
							mListener.onDialogActionClick(DownloadItemDialogFragment.this);
						}
						
					}
				});
		return builder.create();
	}

	private void initActionMap(String status)
	{
		actionMap.put(getString(R.string.pause),PAUSE_DOWNLOAD);
		actionMap.put(getString(R.string.resume),RESUME_DOWNLOAD);
		if(getType(status).equals("stopped"))
		{
			actionMap.put(getString(R.string.remove),REMOVE_DOWNLOAD_RESULT);
		}
		else 
		{
			actionMap.put(getString(R.string.remove),REMOVE_DOWNLOAD);
		}
		
	}

	// gets the type for the download as classified by the aria2 rpc calls
	String getType(String status) {
		String type = status;
		if (status.equals("paused"))
		{
			type = "waiting";
		}
		if(status.equals("error") || status.equals("removed") || status.equals("complete"))
		{
			type = "stopped";
		}
		return type;
	}; 
	
	public int getAction()
	{
		return action;
	}

	public String getGid()
	{
		return gid;
	}
}
