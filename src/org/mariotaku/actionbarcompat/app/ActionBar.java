package org.mariotaku.actionbarcompat.app;

import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public abstract interface ActionBar {

	public abstract View getCustomView();

	public abstract int getDisplayOptions();

	public abstract int getHeight();

	/**
	 * Returns a {@link MenuInflater} for use when inflating menus. The
	 * implementation of this method in {@link ActionBarHelperBase} returns a
	 * wrapped menu inflater that can read action bar metadata from a menu
	 * resource pre-Honeycomb.
	 */
	public abstract MenuInflater getMenuInflater(MenuInflater inflater);

	public abstract int getNavigationItemCount();

	public abstract int getNavigationMode();

	public abstract int getSelectedNavigationIndex();

	public abstract CharSequence getSubtitle();

	public abstract CharSequence getTitle();

	public abstract void hide();

	public abstract boolean hideMenuInActionBar(Menu menu);

	public abstract boolean isShowing();

	public abstract void setBackgroundDrawable(Drawable paramDrawable);

	public abstract void setCustomView(int resId);

	public abstract void setCustomView(View view);

	public abstract void setCustomView(View view, LayoutParams params);

	public abstract void setDisplayHomeAsUpEnabled(boolean showHomeAsUp);

	public abstract void setDisplayOptions(int options);

	public abstract void setDisplayOptions(int options, int mask);

	public abstract void setDisplayShowCustomEnabled(boolean showCustom);

	public abstract void setDisplayShowHomeEnabled(boolean sShowHome);

	public abstract void setDisplayShowTitleEnabled(boolean showTitle);

	public abstract void setDisplayUseLogoEnabled(boolean useLogo);

	public abstract void setIcon(Drawable icon);

	public abstract void setIcon(int resId);

	public abstract void setLogo(Drawable logo);

	public abstract void setLogo(int resId);

	public abstract void setNavigationMode(int paramInt);

	public abstract void setSelectedNavigationItem(int item);

	public abstract void setSubtitle(CharSequence subtitle);

	public abstract void setSubtitle(int resId);

	public abstract void setTitle(CharSequence title);

	public abstract void setTitle(int resId);

	public abstract void show();

}
