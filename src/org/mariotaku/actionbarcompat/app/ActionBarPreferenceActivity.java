package org.mariotaku.actionbarcompat.app;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ActionBarPreferenceActivity extends PreferenceActivity {

	ActionBarCompat mActionBarCompat = ActionBarCompat.getInstance(this);

	public ActionBar getSupportActionBar() {
		return mActionBarCompat;
	}

	@Override
	public void invalidateOptionsMenu() {
		// super.invalidateOptionsMenu();
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
