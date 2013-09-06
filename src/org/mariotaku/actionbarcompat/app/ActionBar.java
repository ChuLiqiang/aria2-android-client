package org.mariotaku.actionbarcompat.app;

import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public interface ActionBar {

	public View getCustomView();

	public int getDisplayOptions();

	public int getHeight();

	public int getNavigationItemCount();

	public int getNavigationMode();

	public int getSelectedNavigationIndex();

	public CharSequence getSubtitle();

	public CharSequence getTitle();

	public void hide();

	public boolean hideMenuInActionBar(Menu menu);

	public boolean isShowing();

	public void setBackgroundDrawable(Drawable paramDrawable);

	public void setCustomView(int resId);

	public void setCustomView(View view);

	public void setCustomView(View view, LayoutParams params);

	public void setDisplayHomeAsUpEnabled(boolean showHomeAsUp);

	public void setDisplayOptions(int options);

	public void setDisplayOptions(int options, int mask);

	public void setDisplayShowCustomEnabled(boolean showCustom);

	public void setDisplayShowHomeEnabled(boolean sShowHome);

	public void setDisplayShowTitleEnabled(boolean showTitle);

	public void setDisplayUseLogoEnabled(boolean useLogo);

	public void setIcon(Drawable icon);

	public void setIcon(int resId);

	public void setLogo(Drawable logo);

	public void setLogo(int resId);

	public void setNavigationMode(int paramInt);

	public void setSelectedNavigationItem(int item);

	public void setSubtitle(CharSequence subtitle);

	public void setSubtitle(int resId);

	public void setTitle(CharSequence title);

	public void setTitle(int resId);

	public void show();

	/**
	 * Returns a {@link MenuInflater} for use when inflating menus. The
	 * implementation of this method in {@link ActionBarHelperBase} returns a
	 * wrapped menu inflater that can read action bar metadata from a menu
	 * resource pre-Honeycomb.
	 */
	MenuInflater getMenuInflater(MenuInflater inflater);

}
