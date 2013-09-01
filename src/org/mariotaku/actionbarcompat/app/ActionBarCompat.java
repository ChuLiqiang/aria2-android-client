package org.mariotaku.actionbarcompat.app;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public abstract class ActionBarCompat {

	public static ActionBarCompat getInstance(Activity activity) {
		if (activity == null) return null;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return new ActionBarCompatICS(activity);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return new ActionBarCompatHoneycomb(activity);
		} else {
			return new ActionBarCompatBase(activity);
		}
	}

	public abstract boolean initCompat();

	public abstract boolean initActionBar();

	public abstract void setCustomView(View view);

	public abstract void setCustomView(View view, LayoutParams params);

	public abstract void setCustomView(int resId);

	public abstract void setIcon(int resId);

	public abstract void setIcon(Drawable icon);

	public abstract void setLogo(int resId);

	public abstract void setLogo(Drawable logo);

	public abstract void setSelectedNavigationItem(int item);

	public int getSelectedNavigationIndex() {
		return 0;
	}

	public int getNavigationItemCount() {
		return 0;
	}

	public abstract void setTitle(CharSequence title);

	public abstract void setTitle(int resId);

	public abstract void setSubtitle(CharSequence subtitle);

	public abstract void setSubtitle(int resId);

	public abstract void setDisplayOptions(int options);

	public abstract void setDisplayOptions(int options, int mask);

	public abstract void setDisplayUseLogoEnabled(boolean useLogo);

	public abstract void setDisplayShowHomeEnabled(boolean sShowHome);

	public abstract void setDisplayHomeAsUpEnabled(boolean showHomeAsUp);

	public abstract void setDisplayShowTitleEnabled(boolean showTitle);

	public abstract void setDisplayShowCustomEnabled(boolean showCustom);

	public abstract void setBackgroundDrawable(Drawable paramDrawable);

	public abstract View getCustomView();

	public abstract CharSequence getTitle();

	public abstract CharSequence getSubtitle();

	public abstract int getNavigationMode();

	public abstract void setNavigationMode(int paramInt);

	public abstract int getDisplayOptions();

	public abstract int getHeight();

	public abstract void show();

	public abstract void hide();

	public abstract boolean isShowing();

	/**
	 * Returns a {@link MenuInflater} for use when inflating menus. The
	 * implementation of this method in {@link ActionBarHelperBase} returns a
	 * wrapped menu inflater that can read action bar metadata from a menu
	 * resource pre-Honeycomb.
	 */
	public abstract MenuInflater getMenuInflater(MenuInflater inflater);

	/**
	 * Sets the indeterminate loading state of the item with ID
	 * {@link R.id.menu_refresh}. (where the item ID was menu_refresh).
	 */
	public abstract void setRefreshActionItemState(boolean refreshing);

	public abstract void setStarActionItemState(boolean starred);

	public abstract boolean hideMenuInActionBar(Menu menu);

}
