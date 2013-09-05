package org.mariotaku.actionbarcompat.app;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

@TargetApi(11)
class ActionBarCompatHoneycomb extends ActionBarCompat {

	private Activity mActivity;

	public ActionBarCompatHoneycomb(Activity activity) {
		mActivity = activity;
	}

	@Override
	public View getCustomView() {
		return mActivity.getActionBar().getCustomView();
	}

	@Override
	public int getDisplayOptions() {
		return mActivity.getActionBar().getDisplayOptions();
	}

	@Override
	public int getHeight() {
		return mActivity.getActionBar().getHeight();
	}

	@Override
	public MenuInflater getMenuInflater(MenuInflater inflater) {
		return inflater;
	}

	@Override
	public int getNavigationItemCount() {
		return mActivity.getActionBar().getNavigationItemCount();
	}

	@Override
	public int getNavigationMode() {
		return mActivity.getActionBar().getNavigationMode();
	}

	@Override
	public int getSelectedNavigationIndex() {
		return mActivity.getActionBar().getSelectedNavigationIndex();
	}

	@Override
	public CharSequence getSubtitle() {
		return mActivity.getActionBar().getSubtitle();
	}

	@Override
	public CharSequence getTitle() {
		return mActivity.getActionBar().getTitle();
	}

	@Override
	public void hide() {
		mActivity.getActionBar().hide();
	}

	@Override
	public boolean hideMenuInActionBar(Menu menu) {
		return false;
	}

	@Override
	public boolean requestCustomTitleView() {
		return true;
	}

	@Override
	public boolean isShowing() {
		return mActivity.getActionBar().isShowing();
	}

	@Override
	public void setBackgroundDrawable(Drawable background) {
		mActivity.getActionBar().setBackgroundDrawable(background);
	}

	@Override
	public boolean setCustomTitleView() {
		return true;
	}

	@Override
	public void setCustomView(int resId) {
		mActivity.getActionBar().setCustomView(resId);
	}

	@Override
	public void setCustomView(View view) {
		mActivity.getActionBar().setCustomView(view);
	}

	@Override
	public void setCustomView(View view, LayoutParams params) {
		ActionBar.LayoutParams p = new ActionBar.LayoutParams(params.width, params.height); 
		if (params instanceof ActionBar.LayoutParams) {
			p = (ActionBar.LayoutParams) params;
		}
		mActivity.getActionBar().setCustomView(view, p);
	}

	@Override
	public void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
		mActivity.getActionBar().setDisplayHomeAsUpEnabled(showHomeAsUp);
	}

	@Override
	public void setDisplayOptions(int options) {
		mActivity.getActionBar().setDisplayOptions(options);
	}

	@Override
	public void setDisplayOptions(int options, int mask) {
		mActivity.getActionBar().setDisplayOptions(options, mask);
	}

	@Override
	public void setDisplayShowCustomEnabled(boolean showCustom) {
		mActivity.getActionBar().setDisplayShowCustomEnabled(showCustom);
	}

	@Override
	public void setDisplayShowHomeEnabled(boolean showHome) {
		mActivity.getActionBar().setDisplayShowHomeEnabled(showHome);
	}

	@Override
	public void setDisplayShowTitleEnabled(boolean showTitle) {
		mActivity.getActionBar().setDisplayShowTitleEnabled(showTitle);
	}

	@Override
	public void setDisplayUseLogoEnabled(boolean useLogo) {
		mActivity.getActionBar().setDisplayUseLogoEnabled(useLogo);
	}

	@Override
	public void setIcon(Drawable icon) {
		mActivity.getActionBar().setIcon(icon);
	}

	@Override
	public void setIcon(int resId) {
		mActivity.getActionBar().setIcon(resId);
	}

	@Override
	public void setLogo(Drawable logo) {
		mActivity.getActionBar().setLogo(logo);
	}

	@Override
	public void setLogo(int resId) {
		mActivity.getActionBar().setLogo(resId);
	}

	@Override
	public void setNavigationMode(int paramInt) {
	}

	@Override
	public void setSelectedNavigationItem(int paramInt) {
	}

	@Override
	public void setSubtitle(CharSequence subtitle) {
		mActivity.getActionBar().setSubtitle(subtitle);
	}

	@Override
	public void setSubtitle(int resId) {
		mActivity.getActionBar().setSubtitle(resId);
	}

	@Override
	public void setTitle(CharSequence title) {
		mActivity.getActionBar().setTitle(title);
	}

	@Override
	public void setTitle(int resId) {
		mActivity.getActionBar().setTitle(resId);
	}

	@Override
	public void show() {
		mActivity.getActionBar().show();
	}
}
