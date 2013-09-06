package org.mariotaku.actionbarcompat.app;

import java.io.IOException;


import org.mariotaku.aria2.android.R;
import org.mariotaku.popupmenu.MenuImpl;
import org.mariotaku.popupmenu.MenuItemImpl;
import org.mariotaku.popupmenu.PopupMenu;
import org.mariotaku.popupmenu.PopupMenu.OnMenuItemClickListener;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.XmlResourceParser;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

class ActionBarCompatBase extends ActionBarCompat {

	private static final String MENU_RES_NAMESPACE = "http://schemas.android.com/apk/res/android";
	private static final String MENU_ATTR_ID = "id";
	private static final String MENU_ATTR_SHOW_AS_ACTION = "showAsAction";

	private final Activity mActivity;
	private View mActionBarView, mCustomView;
	private ViewGroup mCustomViewContainer, mActionMenuView;
	private final Menu mRealMenu, mActionBarMenu;

	public ActionBarCompatBase(Activity activity) {
		mActivity = activity;
		mActionBarMenu = new MenuImpl(activity);
		mRealMenu = new MenuImpl(mActivity);
	}

	public void createActionBarMenu() {
		mActivity.onCreatePanelMenu(Window.FEATURE_OPTIONS_PANEL, mRealMenu);
		mActivity.onPrepareOptionsMenu(mRealMenu);
	}

	@Override
	public View getCustomView() {
		return mCustomView;
	}

	@Override
	public int getDisplayOptions() {
		return 0;
	}

	@Override
	public int getHeight() {
		return 0;
	}

	/**
	 * Returns a {@link android.view.MenuInflater} that can read action bar
	 * metadata on pre-Honeycomb devices.
	 */
	@Override
	public MenuInflater getMenuInflater(MenuInflater superMenuInflater) {
		return new MenuInflaterWrapper(mActivity, superMenuInflater);
	}

	@Override
	public int getNavigationItemCount() {
		return 0;
	}

	@Override
	public int getNavigationMode() {
		return 0;
	}

	@Override
	public int getSelectedNavigationIndex() {
		return 0;
	}

	@Override
	public CharSequence getSubtitle() {
		return null;
	}

	@Override
	public CharSequence getTitle() {
		return null;
	}

	@Override
	public void hide() {
		mActionBarView.setVisibility(View.GONE);
	}

	/**
	 * Action bar helper code to be run in
	 * {@link Activity#onCreateOptionsMenu(android.view.Menu)}.
	 * 
	 * NOTE: This code will mark on-screen menu items as invisible.
	 */
	@Override
	public boolean hideMenuInActionBar(Menu menu) {
		return true;
	}

	@Override
	public void invalidateOptionsMenu() {
		clearMenuButtons();
		for (MenuItem item : ((MenuImpl) mActionBarMenu).getMenuItems()) {
			addActionItemCompatFromMenuItem(item);
		}
		mActivity.onPrepareOptionsMenu(new SupportMenu(mActivity));
	}

	@Override
	public boolean isShowing() {
		return mActionBarView.getVisibility() == View.VISIBLE;
	}

	@Override
	public boolean requestCustomTitleView() {
		mActivity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		return true;
	}

	@Override
	public void setBackgroundDrawable(Drawable paramDrawable) {
	}

	@Override
	public boolean setCustomTitleView() {
		mActivity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.actionbar);
		mActionBarView = mActivity.findViewById(R.id.actionbar);

		mActionMenuView = (ViewGroup) mActionBarView.findViewById(R.id.actionbar_menu_buttons);
		mCustomViewContainer = (ViewGroup) mActivity.findViewById(R.id.actionbar_custom_view_container);
		setTitle(mActivity.getTitle());

		// Add Home button
		setHomeButton();

		createActionBarMenu();
		return true;
	}

	@Override
	public void setCustomView(int resId) {
		setCustomView(mActivity.getLayoutInflater().inflate(resId, null));
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setCustomView(View view) {
		setCustomView(view, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}

	@Override
	public void setCustomView(View view, LayoutParams params) {
		mCustomViewContainer.removeAllViews();
		mCustomViewContainer.addView(view, params.width, params.height);
		mCustomView = view;
	}

	@Override
	public void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
		mActivity.findViewById(R.id.actionbar_home_as_up_indicator).setVisibility(
				showHomeAsUp ? View.VISIBLE : View.GONE);
	}

	@Override
	public void setDisplayOptions(int options) {
	}

	@Override
	public void setDisplayOptions(int options, int mask) {
	}

	@Override
	public void setDisplayShowCustomEnabled(boolean enabled) {
		if (mCustomViewContainer != null) {
			mCustomViewContainer.setVisibility(enabled ? View.VISIBLE : View.GONE);
			setDisplayShowTitleEnabled(!enabled);
		}
	}

	@Override
	public void setDisplayShowHomeEnabled(boolean showHome) {
		mActionBarView.findViewById(R.id.actionbar_home).setVisibility(showHome ? View.VISIBLE : View.GONE);
	}

	@Override
	public void setDisplayShowTitleEnabled(boolean enabled) {
		mActivity.findViewById(R.id.actionbar_title_view).setVisibility(enabled ? View.VISIBLE : View.GONE);
	}

	@Override
	public void setDisplayUseLogoEnabled(boolean useLogo) {
	}

	@Override
	public void setIcon(Drawable icon) {
		((ImageView) mActionBarView.findViewById(R.id.actionbar_icon)).setImageDrawable(icon);
	}

	@Override
	public void setIcon(int resId) {
		((ImageView) mActionBarView.findViewById(R.id.actionbar_icon)).setImageResource(resId);
	}

	@Override
	public void setLogo(Drawable logo) {
	}

	@Override
	public void setLogo(int resId) {
	}

	@Override
	public void setNavigationMode(int paramInt) {
	}

	@Override
	public void setSelectedNavigationItem(int paramInt) {
	}

	@Override
	public void setSubtitle(CharSequence subtitle) {
		TextView mSubtitleView = (TextView) mActivity.findViewById(R.id.actionbar_subtitle);
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
	public void setTitle(CharSequence title) {
		TextView mTitleView = (TextView) mActivity.findViewById(R.id.actionbar_title);
		if (mTitleView != null) {
			mTitleView.setText(title);
		}
	}

	@Override
	public void setTitle(int resId) {
		setTitle(mActivity.getString(resId));
	}

	@Override
	public void show() {
		mActionBarView.setVisibility(View.VISIBLE);
	}

	/**
	 * Adds an action button to the compatibility action bar, using menu
	 * information from a {@link android.view.MenuItem}. If the menu item ID is
	 * <code>menu_refresh</code>, the menu item's state can be changed to show a
	 * loading spinner using
	 * {@link com.android.actionbarcompat.ActionBarHelperBase#setRefreshActionItemState(boolean)}
	 * .
	 */
	@SuppressWarnings("deprecation")
	private View addActionItemCompatFromMenuItem(final MenuItem item) {

		if (mActionMenuView == null || item == null) return null;

		// Create the button
		ImageButton actionButton = new ImageButton(mActivity, null, R.attr.actionBarItemStyle);
		// actionButton.setVisibility(item.isVisible() ? View.VISIBLE :
		// View.GONE);
		actionButton.setLayoutParams(new ViewGroup.LayoutParams((int) mActivity.getResources().getDimension(
				R.dimen.actionbar_button_width), ViewGroup.LayoutParams.FILL_PARENT));
		actionButton.setId(item.getItemId());
		actionButton.setImageDrawable(item.getIcon());
		actionButton.setScaleType(ScaleType.CENTER);
		actionButton.setContentDescription(item.getTitle());

		actionButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				if (!item.isEnabled()) return;
				if (item.hasSubMenu()) {
					PopupMenu popup = new PopupMenu(mActivity, view);
					popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

						@Override
						public boolean onMenuItemClick(MenuItem item) {
							return mActivity.onMenuItemSelected(Window.FEATURE_OPTIONS_PANEL, item);
						}

					});
					popup.setMenu(item.getSubMenu());
					popup.show();
				}
				mActivity.onMenuItemSelected(Window.FEATURE_OPTIONS_PANEL, item);
			}
		});
		actionButton.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				if (item.getItemId() == android.R.id.home) return false;

				Toast t = Toast.makeText(mActivity, item.getTitle(), Toast.LENGTH_SHORT);

				final int[] screenPos = new int[2];
				final Rect displayFrame = new Rect();
				v.getLocationOnScreen(screenPos);
				v.getWindowVisibleDisplayFrame(displayFrame);

				final int width = v.getWidth();
				final int height = v.getHeight();
				final int midy = screenPos[1] + height / 2;
				final int screenWidth = mActivity.getResources().getDisplayMetrics().widthPixels;

				if (midy < displayFrame.height()) {
					// Show along the top; follow action buttons
					t.setGravity(Gravity.TOP | Gravity.RIGHT, screenWidth - screenPos[0] - width / 2, height);
				} else {
					// Show along the bottom center
					t.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, height);
				}
				t.show();
				return true;
			}
		});

		mActionMenuView.addView(actionButton);
		return actionButton;
	}

	private void clearMenuButtons() {
		mActionMenuView.removeAllViews();
	}

	private void setHomeButton() {
		// Add Home button
		final MenuItem homeItem = MenuItemImpl.createItem(mActivity, android.R.id.home);
		View homeButton = mActionBarView.findViewById(R.id.actionbar_home);
		PackageManager pm = mActivity.getPackageManager();
		try {
			((ImageView) homeButton.findViewById(R.id.actionbar_icon)).setImageDrawable(pm.getActivityIcon(mActivity
					.getComponentName()));
		} catch (NameNotFoundException e) {
			// This should not happen.
		}
		homeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				mActivity.onMenuItemSelected(Window.FEATURE_OPTIONS_PANEL, homeItem);
			}
		});
	}

	@Override
	void hideInRealMenu(Menu menu) {
		if (menu instanceof MenuImpl) return;
		for (MenuItem item : ((MenuImpl) mActionBarMenu).getMenuItems()) {
			MenuItem realItem = menu.findItem(item.getItemId());
			if (realItem != null) {
				realItem.setVisible(false);
			}
		}
	}

	/**
	 * A {@link android.view.MenuInflater} that reads action bar metadata.
	 */
	private class MenuInflaterWrapper extends MenuInflater {

		MenuInflater mInflater;

		public MenuInflaterWrapper(Context context, MenuInflater inflater) {
			super(context);
			mInflater = inflater;
		}

		@Override
		public void inflate(int menuRes, Menu menu) {
			mInflater.inflate(menuRes, menu);
			loadActionBarMetadata(menu, menuRes);
		}

		/**
		 * Loads action bar metadata from a menu resource, storing a list of
		 * menu item IDs that should be shown on-screen (i.e. those with
		 * showAsAction set to always or ifRoom).
		 * 
		 * @param menuResId
		 */
		private void loadActionBarMetadata(Menu menu, int menuResId) {
			clearMenuButtons();
			mActionBarMenu.clear();
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

							itemId = parser.getAttributeResourceValue(MENU_RES_NAMESPACE, MENU_ATTR_ID, 0);
							if (itemId == 0) {
								break;
							}

							showAsAction = parser
									.getAttributeIntValue(MENU_RES_NAMESPACE, MENU_ATTR_SHOW_AS_ACTION, -1);

							MenuItem item = menu.findItem(itemId);
							boolean isShowAsAction = showAsAction == MenuItem.SHOW_AS_ACTION_ALWAYS
									|| showAsAction == MenuItem.SHOW_AS_ACTION_IF_ROOM
									|| showAsAction == MenuItem.SHOW_AS_ACTION_WITH_TEXT;
							if (isShowAsAction) {
								mActionBarMenu.add(item.getGroupId(), item.getItemId(), item.getOrder(),
										item.getTitle()).setIcon(item.getIcon());
							}
							item.setVisible(!isShowAsAction);
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
			invalidateOptionsMenu();
		}
	}

	private class SupportMenu extends MenuImpl {

		private final Context context;

		public SupportMenu(Context context) {
			super(context);
			this.context = context;
		}

		@Override
		public MenuItem findItem(int id) {
			for (MenuItem item : ((MenuImpl) mRealMenu).getMenuItems()) {
				if (item.getItemId() == id) return new SupportMenuItem(context, id);
			}
			return null;
		}

	}

	private class SupportMenuItem extends MenuItemImpl {

		private final int itemId;

		public SupportMenuItem(Context context, int itemId) {
			super(context);
			this.itemId = itemId;
		}

		@Override
		public MenuItem setEnabled(boolean enabled) {
			final MenuItem item = mActionBarMenu.findItem(itemId);
			final MenuItem realItem = mRealMenu.findItem(itemId);
			if (item != null) {
				item.setEnabled(enabled);
			}
			if (realItem != null) {
				realItem.setEnabled(enabled);
			}
			return this;
		}

		@Override
		public MenuItem setIcon(int iconRes) {
			final MenuItem item = mActionBarMenu.findItem(itemId);
			final MenuItem realItem = mRealMenu.findItem(itemId);
			if (item != null) {
				item.setIcon(iconRes);
			}
			if (realItem != null) {
				realItem.setIcon(iconRes);
			}
			return this;
		}

		@Override
		public MenuItem setTitle(int titleRes) {
			final MenuItem item = mActionBarMenu.findItem(itemId);
			final MenuItem realItem = mRealMenu.findItem(itemId);
			if (item != null) {
				item.setTitle(titleRes);
			}
			if (realItem != null) {
				realItem.setTitle(titleRes);
			}
			return this;
		}

		@Override
		public MenuItem setVisible(boolean visible) {
			final MenuItem item = mActionBarMenu.findItem(itemId);
			final MenuItem realItem = mRealMenu.findItem(itemId);
			if (item != null) {
				item.setVisible(visible);
				View view = mActionMenuView.findViewById(itemId);
				if (view != null) {
					view.setVisibility(visible ? View.VISIBLE : View.GONE);
				}
				if (realItem != null) {
					realItem.setVisible(false);
				}
				return this;
			}
			if (realItem != null) {
				realItem.setVisible(visible);
			}
			return this;
		}

	}

	@Override
	void setProgressBarIndeterminateVisibility(boolean visible) {
		if(mProgressBarIndeterminateEnabled )mActionBarView.findViewById(R.id.actionbar_progress_indeterminate).setVisibility(visible ? View.VISIBLE : View.GONE);
		
	}

	private boolean mProgressBarIndeterminateEnabled = false;
	
	@Override
	void setProgressBarIndeterminateEnabled(boolean enabled) {
		mProgressBarIndeterminateEnabled = enabled;
		
	}
}
