package org.mariotaku.popupmenu;


import org.mariotaku.aria2.android.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.TableRow;
import android.widget.Toast;

public class MenuBar extends TableRow implements PopupMenu.OnMenuItemClickListener {

	private Menu mMenu;
	private final Context mContext;
	private OnMenuItemClickListener mItemClickListener;

	private PopupMenu mPopupMenu;

	public MenuBar(Context context) {
		this(context, null);
	}

	public MenuBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mMenu = new MenuImpl(context);
	}

	public Menu getMenu() {
		return mMenu;
	}

	public void inflate(int menuRes) {
		mMenu.clear();
		new MenuInflater(mContext).inflate(menuRes, mMenu);
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		if (mItemClickListener != null) return mItemClickListener.onMenuItemClick(item);
		return false;
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
		removeAllViews();
		for (MenuItem item : ((MenuImpl) mMenu).getMenuItems()) {
			if (item.isVisible()) {
				addMenuButton(item);
			}
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		if (mPopupMenu != null) {
			mPopupMenu.dismiss();
		}
		super.onDetachedFromWindow();
	}

	private View addMenuButton(final MenuItem item) {

		ImageButton actionButton = (ImageButton) LayoutInflater.from(mContext).inflate(R.layout.menu_button_item, null);

		@SuppressWarnings("deprecation")
		LayoutParams params = new LayoutParams((int) getResources().getDimension(R.dimen.actionbar_button_width),
				ViewGroup.LayoutParams.FILL_PARENT);
		params.weight = 1;

		actionButton.setLayoutParams(params);

		actionButton.setImageDrawable(item.getIcon());
		actionButton.setScaleType(ScaleType.CENTER);
		actionButton.setContentDescription(item.getTitle());
		actionButton.setAlpha(item.isEnabled() ? 0xFF : 0x80);
		actionButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				if (!item.isEnabled()) return;
				if (item.hasSubMenu()) {
					mPopupMenu = new PopupMenu(mContext, view);
					mPopupMenu.setOnMenuItemClickListener(MenuBar.this);
					mPopupMenu.setMenu(item.getSubMenu());
					mPopupMenu.show(true);
				} else {
					if (mItemClickListener != null) {
						mItemClickListener.onMenuItemClick(item);
					}
				}
			}
		});
		actionButton.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				if (item.getItemId() == android.R.id.home) return false;

				Toast t = Toast.makeText(mContext, item.getTitle(), Toast.LENGTH_SHORT);

				final int[] screenPos = new int[2];
				v.getLocationInWindow(screenPos);

				final int height = v.getHeight();

				t.setGravity(Gravity.TOP | Gravity.LEFT, screenPos[0], (int) (screenPos[1] - height * 1.5));
				t.show();
				return true;
			}
		});

		addView(actionButton);
		return actionButton;
	}

	/**
	 * Listener for item click
	 * 
	 */
	public interface OnMenuItemClickListener {
		public boolean onMenuItemClick(MenuItem item);
	}

}
