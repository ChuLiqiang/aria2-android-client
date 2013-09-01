package org.mariotaku.actionbarcompat.app;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ActionBarPreferenceActivity extends PreferenceActivity {

	ActionBarCompat mActionBarCompat = ActionBarCompat.getInstance(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mActionBarCompat.initCompat();
		super.onCreate(savedInstanceState);
		mActionBarCompat.initActionBar();
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

	public ActionBarCompat getActionBarCompat() {
		return mActionBarCompat;
	}

}
