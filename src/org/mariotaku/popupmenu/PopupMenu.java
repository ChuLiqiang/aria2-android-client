package org.mariotaku.popupmenu;


import org.mariotaku.aria2.android.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

/**
 * QuickAction dialog, shows action list as icon and text like the one in
 * Gallery3D app. Currently supports vertical and horizontal layout.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 * 
 *         Contributors: - Kevin Peck <kevinwpeck@gmail.com>
 */
public class PopupMenu implements OnDismissListener, OnItemClickListener, OnTouchListener {
	private ListView mListView;
	private View mRootView;

	private OnMenuItemClickListener mItemClickListener;
	private OnDismissListener mDismissListener;

	private Menu mMenu;
	private final Context mContext;
	private final View mView;
	private PopupWindow mWindow;
	private WindowManager mWindowManager;

	private boolean mDidAction;

	private int rootWidth = 0, mPosX, mPosY;

	private MenuAdapter mAdapter;

	private boolean mIsShowing;

	/**
	 * Constructor for default vertical layout
	 * 
	 * @param context Context
	 */
	public PopupMenu(Context context, View view) {
		mContext = context;
		mView = view;
		mWindow = new PopupWindow(context);
		mWindow.setTouchInterceptor(this);
		mWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
		mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

		mAdapter = new MenuAdapter(context);
		mMenu = new MenuImpl(mContext, mAdapter);
		setView();

	}
	
	public void setAnchorByTouch(boolean enabled) {
		mView.setOnTouchListener(enabled ? mViewTouchListener : null);
	}

	private OnTouchListener mViewTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			return true;
		}
		
	};
	
	/**
	 * Dismiss the popup window.
	 */
	public void dismiss() {
		if (mIsShowing) {
			mWindow.dismiss();
		}
		mIsShowing = false;
	}

	public Menu getMenu() {
		return mMenu;
	}

	public void inflate(int menuRes) {
		new MenuInflater(mContext).inflate(menuRes, mMenu);
	}

	@Override
	public void onDismiss() {
		if (!mDidAction && mDismissListener != null) {
			mDismissListener.onDismiss();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		mDidAction = true;

		MenuItem item = mAdapter.getItem(position);
		if (item.hasSubMenu()) {
			dismiss();
			showMenu(item.getSubMenu(), false, false);
		} else {
			if (mItemClickListener != null) {
				mItemClickListener.onMenuItemClick(item);
				dismiss();
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
			mWindow.dismiss();

			return true;
		}

		return false;
	}

	public void setMenu(Menu menu) {
		mMenu = menu;
	}

	/**
	 * Set listener for window dismissed. This listener will only be fired if
	 * the quickaction dialog is dismissed by clicking outside the dialog or
	 * clicking on sticky item.
	 */
	public void setOnDismissListener(PopupMenu.OnDismissListener listener) {
		setOnDismissListener(this);

		mDismissListener = listener;
	}

	/**
	 * Set listener on window dismissed.
	 * 
	 * @param listener
	 */
	public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
		mWindow.setOnDismissListener(listener);
	}

	/**
	 * Set listener for action item clicked.
	 * 
	 * @param listener Listener
	 */
	public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
		mItemClickListener = listener;
	}

	public void show() {
		show(false);
	}

	public void show(boolean above_view) {
		if (mIsShowing) {
			dismiss();
		}

		showMenu(getMenu(), true, above_view);
	}

	/**
	 * On pre show
	 */
	private void preShow() {
		if (mRootView == null)
			throw new IllegalStateException("setContentView was not called with a view to display.");

		mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		mWindow.setWidth(mContext.getResources().getDimensionPixelSize(R.dimen.popup_window_width));
		mWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		mWindow.setTouchable(true);
		mWindow.setFocusable(true);
		mWindow.setOutsideTouchable(true);

		mWindow.setContentView(mRootView);
	}

	@SuppressWarnings("deprecation")
	private void setAnchor(View anchor, boolean above_view) {
		preShow();

		mDidAction = false;

		final int[] location = new int[2];

		anchor.getLocationOnScreen(location);

		Rect anchorRect = new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[1]
				+ anchor.getHeight());

		if (rootWidth == 0) {
			rootWidth = mRootView.getMeasuredWidth();
		}

		final Display display = mWindowManager.getDefaultDisplay();
		final int screenWidth = display.getWidth(), screenHeight = display.getHeight();

		// automatically get X coord of popup (top left)
		if (anchorRect.left + rootWidth > screenWidth) {
			mPosX = anchorRect.left - (rootWidth - anchor.getWidth());
			mPosX = mPosX < 0 ? 0 : mPosX;

		} else {
			if (anchor.getWidth() > rootWidth) {
				mPosX = anchorRect.centerX() - rootWidth / 2;
			} else {
				mPosX = anchorRect.left;
			}

		}

		int dyTop = anchorRect.top;
		int dyBottom = screenHeight - anchorRect.bottom;

		boolean onTop = dyTop > dyBottom;

		mPosY = anchorRect.top - (above_view ? anchor.getHeight() * 2 : 0);

		setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);
	}

	/**
	 * Set animation style
	 * 
	 * @param screenWidth screen width
	 * @param requestedX distance from left edge
	 * @param onTop flag to indicate where the popup should be displayed. Set
	 *            TRUE if displayed on top of anchor view and vice versa
	 */
	private void setAnimationStyle(int screenWidth, int requestedX, boolean onTop) {

		if (requestedX <= screenWidth / 4) {
			mWindow.setAnimationStyle(onTop ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);
		} else if (requestedX > screenWidth / 4 && requestedX < 3 * (screenWidth / 4)) {
			mWindow.setAnimationStyle(onTop ? R.style.Animations_PopUpMenu_Center
					: R.style.Animations_PopDownMenu_Center);
		} else {
			mWindow.setAnimationStyle(onTop ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
		}
	}

	/**
	 * Set root view.
	 * 
	 */
	private void setView() {
		mRootView = LayoutInflater.from(mContext).inflate(R.layout.popup_list, null);
		mListView = (ListView) mRootView.findViewById(android.R.id.list);

		// This was previously defined on show() method, moved here to prevent
		// force close that occured
		// when tapping fastly on a view to show quickaction dialog.
		// Thanx to zammbi (github.com/zammbi)
		mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mWindow.setContentView(mRootView);
	}

	private void showMenu(Menu menu, boolean set_anchor, boolean above_view) {
		mAdapter.setMenu(menu);
		if (set_anchor) {
			setAnchor(mView, above_view);
		}
		mWindow.showAtLocation(mView, Gravity.NO_GRAVITY, mPosX, mPosY);
		mIsShowing = true;
	}

	/**
	 * Listener for window dismiss
	 * 
	 */
	public interface OnDismissListener {
		public abstract void onDismiss();
	}

	/**
	 * Listener for item click
	 * 
	 */
	public interface OnMenuItemClickListener {
		public boolean onMenuItemClick(MenuItem item);
	}
}