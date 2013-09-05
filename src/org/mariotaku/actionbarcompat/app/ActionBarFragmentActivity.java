package org.mariotaku.actionbarcompat.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class ActionBarFragmentActivity extends FragmentActivity {

	private ActionBarCompat mActionBarCompat = ActionBarCompat.getInstance(this);
	private boolean mActionBarInitialized = false;

	@Override
	public MenuInflater getMenuInflater() {
		return mActionBarCompat.getMenuInflater(super.getMenuInflater());
	}

	public ActionBar getSupportActionBar() {
		if (!mActionBarInitialized) {
			mActionBarInitialized = mActionBarCompat.setCustomTitleView();
		}
		return mActionBarCompat;
		
	}

	@Override
	public void invalidateOptionsMenu() {
		//super.invalidateOptionsMenu();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mActionBarCompat.requestCustomTitleView();
		super.onCreate(savedInstanceState);
	}

	public void setSupportProgressBarIndeterminateVisibility(boolean visible) {

	}
	
	/**
	 * Base action bar-aware implementation for
	 * {@link Activity#onCreateOptionsMenu(android.view.Menu)}.
	 * 
	 * Note: marking menu items as invisible/visible is not currently supported.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean retValue = false;
		retValue |= mActionBarCompat.hideMenuInActionBar(menu);
		retValue |= super.onCreateOptionsMenu(menu);
		return retValue;
	}

	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		if (!mActionBarInitialized) {
			mActionBarInitialized = mActionBarCompat.setCustomTitleView();
		}
		super.onPostCreate(savedInstanceState);
	}

	@Override
	public void onTitleChanged(CharSequence title, int color) {
		mActionBarCompat.setTitle(title);
		super.onTitleChanged(title, color);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		if (!mActionBarInitialized) {
			mActionBarInitialized = mActionBarCompat.setCustomTitleView();
		}
	}

	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		if (!mActionBarInitialized) {
			mActionBarInitialized = mActionBarCompat.setCustomTitleView();
		}
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		if (!mActionBarInitialized) {
			mActionBarInitialized = mActionBarCompat.setCustomTitleView();
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mActionBarCompat.setTitle(title);
		super.setTitle(title);
	}

	@Override
	public void setTitle(int titleId) {
		mActionBarCompat.setTitle(titleId);
		super.setTitle(titleId);
	}

}
