package tk.igeek.aria2.android;

import tk.igeek.aria2.Status;
import tk.igeek.aria2.adapter.TabsPagerAdapter;
import tk.igeek.aria2.android.service.Aria2Service;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class DownloadItemInfoActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	
	// Tab titles
	private String[] tabs = { "General", "Trackers","Servers","Files" };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_item_info);
		
		doBindService();
		
		// Initialization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);		

		
		Bundle extras = getIntent().getExtras();
		
		if (extras != null) {
			Status status = extras.getParcelable("downloadItemInfo");
			if(status != null)
			{
				if(status.isTorrent())
				{
					tabs[2] = "Peers";
				}
			}
		}
		
		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
	}

	
	
	@Override
	protected void onDestroy() {
		doUnbindService();
		super.onDestroy();
	}



	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
		
	}
	
	public boolean sendToAria2APIHandlerMsg(int msgType,Object msgObj,Messenger messenger)
	{
		if(mService == null)
		{
			return false;
		}
		
		Message sendToAria2APIHandlerMsg = new Message();
		sendToAria2APIHandlerMsg.what = msgType;
		sendToAria2APIHandlerMsg.obj = msgObj;
		sendToAria2APIHandlerMsg.replyTo = messenger;
		try {
			mService.send(sendToAria2APIHandlerMsg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean sendToAria2APIHandlerMsg(int msgType,Messenger messenger)
	{
		if(mService == null)
		{
			return false;
		}
		Message sendToAria2APIHandlerMsg = new Message();
		sendToAria2APIHandlerMsg.what = msgType;
		sendToAria2APIHandlerMsg.replyTo = messenger;
		try {
			mService.send(sendToAria2APIHandlerMsg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	
		/** Flag indicating whether we have called bind on the service. */
    boolean mIsBound;
	/** Messenger for communicating with service. */
    Messenger mService = null;
    
	/**
     * Class for interacting with the main interface of the service.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
    	public void onServiceConnected(ComponentName className,
    			IBinder service) {
    		// This is called when the connection with the service has been
    		// established, giving us the service object we can use to
    		// interact with the service.  We are communicating with our
    		// service through an IDL interface, so get a client-side
    		// representation of that from the raw service object.
    		mService = new Messenger(service);
    	}

    	public void onServiceDisconnected(ComponentName className) {
    		// This is called when the connection with the service has been
    		// unexpectedly disconnected -- that is, its process crashed.
    		mService = null;
    		
    	}
    };
    
    void doBindService() {
    	// Establish a connection with the service.  We use an explicit
    	// class name because there is no reason to be able to let other
    	// applications replace our component.
    	bindService(new Intent(this, 
    			Aria2Service.class), mConnection, Context.BIND_AUTO_CREATE);
    	mIsBound = true;
    }
    
    void doUnbindService() {
    	if (mIsBound) {
    		// Detach our existing connection.
    		unbindService(mConnection);
    		mIsBound = false;
    	}
    }

}
