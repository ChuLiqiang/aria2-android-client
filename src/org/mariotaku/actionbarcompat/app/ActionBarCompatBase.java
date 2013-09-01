package org.mariotaku.actionbarcompat.app;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


import org.mariotaku.actionbarcompat.menu.MenuCompat;
import org.mariotaku.actionbarcompat.menu.MenuItemCompat;
import org.mariotaku.aria2.android.R;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ActionBarCompatBase extends ActionBarCompat {

	private static final String MENU_RES_NAMESPACE = "http://schemas.android.com/apk/res/android";
	private static final String MENU_ATTR_ID = "id";
	private static final String MENU_ATTR_SHOW_AS_ACTION = "showAsAction";

	private Set<Integer> mActionItemIds = new HashSet<Integer>();

	private Activity mActivity;
	private View mCustomViewContainer;
	private View mCustomView;

	public ActionBarCompatBase(Activity activity) {
		mActivity = activity;
	}

	@Override
	public boolean initCompat() {
		mActivity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		return true;
	}

	@Override
	public boolean initActionBar() {
		mActivity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.actionbarcompat_common);

		mCustomViewContainer = (LinearLayout) mActivity
				.findViewById(R.id.actionbarcompat_custom_view_container);
		setTitle(mActivity.getTitle());

		// Add Home button
		setHomeButton();

		MenuCompat menu = new MenuCompat(mActivity);
		mActivity.onCreatePanelMenu(Window.FEATURE_OPTIONS_PANEL, menu);
		mActivity.onPrepareOptionsMenu(menu);
		for (int i = 0; i < menu.size(); i++) {
			MenuItem item = menu.getItem(i);
			if (mActionItemIds.contains(item.getItemId())) {
				addActionItemCompatFromMenuItem(item);
			}
		}
		return true;
	}

	@Override
	public void setRefreshActionItemState(boolean refreshing) {
		View refreshButton = mActivity.findViewById(R.id.actionbarcompat_menu_refresh);
		View refreshIndicator = mActivity.findViewById(R.id.actionbarcompat_menu_refresh_progress);

		if (refreshButton != null) {
			refreshButton.setVisibility(refreshing ? View.GONE : View.VISIBLE);
		}
		if (refreshIndicator != null) {
			refreshIndicator.setVisibility(refreshing ? View.VISIBLE : View.GONE);
		}
	}

	@Override
	public void setStarActionItemState(boolean starred) {
		ImageButton starButton = (ImageButton) mActivity
				.findViewById(R.id.actionbarcompat_menu_star);
		if (starButton != null) {
			starButton.setImageResource(starred ? R.drawable.ic_menu_actionbarcompat_star
					: R.drawable.ic_menu_actionbarcompat_star_off);
		}
	}

	/**
	 * Action bar helper code to be run in
	 * {@link Activity#onCreateOptionsMenu(android.view.Menu)}.
	 * 
	 * NOTE: This code will mark on-screen menu items as invisible.
	 */
	@Override
	public boolean hideMenuInActionBar(Menu menu) {
		// Hides on-screen action items from the options menu.
		for (int id : mActionItemIds) {
			menu.findItem(id).setVisible(false);
		}
		return true;
	}

	@Override
	public void setCustomView(View view) {
		setCustomView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	@Override
	public void setCustomView(View view, LayoutParams params) {
		((LinearLayout) mCustomViewContainer).removeAllViews();
		((LinearLayout) mCustomViewContainer).addView(view, params.width, params.height);
		mCustomView = view;
	}

	@Override
	public void setCustomView(int resId) {
		setCustomView(mActivity.getLayoutInflater().inflate(resId, null));
	}

	@Override
	public void setIcon(int resId) {
		((ImageButton) mActivity.findViewById(R.id.actionbarcompat_home)).setImageResource(resId);
	}

	@Override
	public void setIcon(Drawable icon) {
		((ImageButton) mActivity.findViewById(R.id.actionbarcompat_home)).setImageDrawable(icon);
	}

	@Override
	public void setLogo(int resId) {
	}

	@Override
	public void setLogo(Drawable logo) {
	}

	@Override
	public void setSelectedNavigationItem(int paramInt) {
	}

	@Override
	public int getSelectedNavigationIndex() {
		return 0;
	}

	@Override
	public int getNavigationItemCount() {
		return 0;
	}

	@Override
	public void setTitle(CharSequence title) {
		TextView mTitleView = (TextView) mActivity.findViewById(R.id.actionbarcompat_title);
		if (mTitleView != null) {
			mTitleView.setText(title);
		}
	}

	@Override
	public void setTitle(int resId) {
		setTitle(mActivity.getString(resId));
	}

	@Override
	public void setSubtitle(CharSequence subtitle) {
		TextView mSubtitleView = (TextView) mActivity.findViewById(R.id.actionbarcompat_subtitle);
		if (mSubtitleView == null) return;
		if (subtitle != null) {
			mSubtitleView.setVisibility(View.VISIBLE);
			mSubtitleView.setText(subtitle);
		} else {
			mSubtitleView.setVisibility(View.GONE);
			mSubtitleView.setText("");
		}
	}

	@Override
	public void setSubtitle(int resId) {
		setSubtitle(mActivity.getString(resId));
	}

	@Override
	public void setDisplayOptions(int options) {
	}

	@Override
	public void setDisplayOptions(int options, int mask) {
	}

	@Override
	public void setDisplayUseLogoEnabled(boolean useLogo) {
	}

	@Override
	public void setDisplayShowHomeEnabled(boolean showHome) {
		mActivity.findViewById(R.id.actionbarcompat_home).setVisibility(
				showHome ? View.VISIBLE : View.GONE);
	}

	@Override
	public void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
	}

	@Override
	public void setDisplayShowTitleEnabled(boolean enabled) {
		mActivity.findViewById(R.id.actionbarcompat_title_view).setVisibility(
				enabled ? View.VISIBLE : View.GONE);
	}

	@Override
	public void setDisplayShowCustomEnabled(boolean enabled) {
		mCustomViewContainer.setVisibility(enabled ? View.VISIBLE : View.GONE);
	}

	@Override
	public void setBackgroundDrawable(Drawable paramDrawable) {
	}

	@Override
	public View getCustomView() {
		return mCustomView;
	}

	@Override
	public CharSequence getTitle() {
		return null;
	}

	@Override
	public CharSequence getSubtitle() {
		return null;
	}

	@Override
	public int getNavigationMode() {
		return 0;
	}

	@Override
	public void setNavigationMode(int paramInt) {
	}

	@Override
	public int getDisplayOptions() {
		return 0;
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public void show() {
		mActivity.findViewById(R.id.actionbarcompat).setVisibility(View.VISIBLE);
	}

	@Override
	public void hide() {
		mActivity.findViewById(R.id.actionbarcompat).setVisibility(View.GONE);
	}

	@Override
	public boolean isShowing() {
		return (mActivity.findViewById(R.id.actionbarcompat).getVisibility() == View.VISIBLE);
	}

	private void setHomeButton() {
		// Add Home button
		MenuCompat tempMenu = new MenuCompat(mActivity);
		final MenuItemCompat homeItem = new MenuItemCompat(tempMenu, android.R.id.home, 0, "Home");
		ImageButton homeButton = (ImageButton) mActivity.findViewById(R.id.actionbarcompat_home);
		homeButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				mActivity.onMenuItemSelected(Window.FEATURE_OPTIONS_PANEL, homeItem);
			}
		});
	}

	/**
	 * Adds an action button to the compatibility action bar, using menu
	 * information from a {@link android.view.MenuItem}. If the menu item ID is
	 * <code>menu_refresh</code>, the menu item's state can be changed to show a
	 * loading spinner using
	 * {@link com.android.actionbarcompat.ActionBarHelperBase#setRefreshActionItemState(boolean)}
	 * .
	 */
	private View addActionItemCompatFromMenuItem(final MenuItem item) {
		final int itemId = item.getItemId();
		final LinearLayout actionMenu = (LinearLayout) mActivity
				.findViewById(R.id.actionbarcompat_menu_buttons);

		if (actionMenu == null) {
			return null;
		}

		// Create the button
		ImageButton actionButton = new ImageButton(mActivity, null, R.attr.actionbarCompatItemStyle);
		actionButton.setLayoutParams(new ViewGroup.LayoutParams((int) mActivity.getResources()
				.getDimension(R.dimen.actionbarcompat_button_width),
				ViewGroup.LayoutParams.FILL_PARENT));

		actionButton.setImageDrawable(item.getIcon());
		actionButton.setScaleType(ScaleType.CENTER);
		actionButton.setContentDescription(item.getTitle());

		if (itemId == R.id.menu_refresh) {
			actionButton.setId(R.id.actionbarcompat_menu_refresh);
		} else if (itemId == R.id.menu_star) {
			actionButton.setId(R.id.actionbarcompat_menu_star);
			actionButton.setImageResource(R.drawable.ic_menu_actionbarcompat_star_off);
		}

		actionButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				mActivity.onMenuItemSelected(Window.FEATURE_OPTIONS_PANEL, item);
			}
		});

		actionMenu.addView(actionButton);

		if (item.getItemId() == R.id.menu_refresh) {
			// Refresh buttons should be stateful, and allow for indeterminate
			// progress indicators,
			// so add those.
			ProgressBar indicator = new ProgressBar(mActivity, null,
					R.attr.actionbarCompatProgressIndicatorStyle);

			final int buttonWidth = mActivity.getResources().getDimensionPixelSize(
					R.dimen.actionbarcompat_button_width);
			final int buttonHeight = mActivity.getResources().getDimensionPixelSize(
					R.dimen.actionbarcompat_height);
			final int progressIndicatorWidth = buttonWidth / 2;

			LinearLayout.LayoutParams indicatorLayoutParams = new LinearLayout.LayoutParams(
					progressIndicatorWidth, progressIndicatorWidth);
			indicatorLayoutParams.setMargins((buttonWidth - progressIndicatorWidth) / 2,
					(buttonHeight - progressIndicatorWidth) / 2,
					(buttonWidth - progressIndicatorWidth) / 2, 0);
			indicator.setLayoutParams(indicatorLayoutParams);
			indicator.setVisibility(View.GONE);
			indicator.setId(R.id.actionbarcompat_menu_refresh_progress);
			actionMenu.addView(indicator);
		}

		return actionButton;
	}

	/**
	 * A {@link android.view.MenuInflater} that reads action bar metadata.
	 */
	private class MenuInflaterCompat extends MenuInflater {

		MenuInflater mInflater;

		public MenuInflaterCompat(Context context, MenuInflater inflater) {
			super(context);
			mInflater = inflater;
		}

		@Override
		public void inflate(int menuRes, Menu menu) {
			loadActionBarMetadata(menuRes);
			mInflater.inflate(menuRes, menu);
		}

		/**
		 * Loads action bar metadata from a menu resource, storing a list of
		 * menu item IDs that should be shown on-screen (i.e. those with
		 * showAsAction set to always or ifRoom).
		 * 
		 * @param menuResId
		 */
		private void loadActionBarMetadata(int menuResId) {
			XmlResourceParser parser = null;
			try {
				parser = mActivity.getResources().getXml(menuResId);

				int eventType = parser.getEventType();
				int itemId;
				int showAsAction;

				boolean eof = false;
				while (!eof) {
					switch (eventType) {
						case XmlPullParser.START_TAG:
							if (!parser.getName().equals("item")) {
								break;
							}

							itemId = parser.getAttributeResourceValue(MENU_RES_NAMESPACE,
									MENU_ATTR_ID, 0);
							if (itemId == 0) {
								break;
							}

							showAsAction = parser.getAttributeIntValue(MENU_RES_NAMESPACE,
									MENU_ATTR_SHOW_AS_ACTION, -1);
							if (showAsAction == MenuItem.SHOW_AS_ACTION_ALWAYS
									|| showAsAction == MenuItem.SHOW_AS_ACTION_IF_ROOM) {
								mActionItemIds.add(itemId);
							}
							break;

						case XmlPullParser.END_DOCUMENT:
							eof = true;
							break;
					}

					eventType = parser.next();
				}
			} catch (XmlPullParserException e) {
				throw new InflateException("Error inflating menu XML", e);
			} catch (IOException e) {
				throw new InflateException("Error inflating menu XML", e);
			} finally {
				if (parser != null) {
					parser.close();
				}
			}
		}
	}

	/**
	 * Returns a {@link android.view.MenuInflater} that can read action bar
	 * metadata on pre-Honeycomb devices.
	 */
	public MenuInflater getMenuInflater(MenuInflater superMenuInflater) {
		return new MenuInflaterCompat(mActivity, superMenuInflater);
	}
}
