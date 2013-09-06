package org.mariotaku.actionbarcompat.app;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Window;

public class ActionBarPreferenceActivity extends PreferenceActivity {

	ActionBarCompat mActionBarCompat = ActionBarCompat.getInstance(this);

	public ActionBar getSupportActionBar() {
		return mActionBarCompat;
	}

	public void invalidateSupportOptionsMenu() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			new MethodsCompat().invalidateOptionsMenu(this);
		} else {
			mActionBarCompat.invalidateOptionsMenu();
		}
	}
	
	public void requestSupportWindowFeature(int featureId) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			requestWindowFeature(featureId);
		} else {
			switch (featureId) {
				case Window.FEATURE_INDETERMINATE_PROGRESS:{
					mActionBarCompat.setProgressBarIndeterminateEnabled(true);
				}
			}
		}
	}
	

	public void setSupportProgressBarIndeterminateVisibility(boolean visible) {
		mActionBarCompat.setProgressBarIndeterminateVisibility(visible);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mActionBarCompat.requestCustomTitleView();
		super.onCreate(savedInstanceState);
		mActionBarCompat.setCustomTitleView();
	}

	@Override
	public void onTitleChanged(CharSequence title, int color) {
		mActionBarCompat.setTitle(title);
		super.onTitleChanged(title, color);
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
