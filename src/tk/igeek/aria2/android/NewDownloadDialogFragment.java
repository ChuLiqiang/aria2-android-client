package tk.igeek.aria2.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


public class NewDownloadDialogFragment extends DialogFragment {

	/* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NewDownloadDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }
	
	private String downloadUri;

	// Use this instance of the interface to deliver action events
    NewDownloadDialogListener mListener;
	
	public String getDownloadUri()
	{
		return downloadUri;
	}

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NewDownloadDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
	
	 @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		 
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		 // Get the layout inflater
		 LayoutInflater inflater = getActivity().getLayoutInflater();
		 final View dialogView = inflater.inflate(R.layout.new_download, null);
		 // Inflate and set the layout for the dialog
		 // Pass null as the parent view because its going in the dialog layout
		 builder.setView(dialogView)
		 // Add action buttons
           .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int id) {
            	   EditText text = (EditText)dialogView.findViewById(R.id.addUrl); 
            	   downloadUri = text.getText().toString();
            	   // Send the positive button event back to the host activity
                   mListener.onDialogPositiveClick(NewDownloadDialogFragment.this);
               }
           })
           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                   NewDownloadDialogFragment.this.getDialog().cancel();
               }
           });      
		 
		 return builder.create();

    }
    
    


}
