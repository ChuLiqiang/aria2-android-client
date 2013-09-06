package org.mariotaku.popupmenu;

import java.util.List;

import org.mariotaku.aria2.android.R;


import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

final class MenuAdapter extends ArrayAdapter<MenuItem> {

	private Menu mMenu;

	public MenuAdapter(Context context) {
		super(context, R.layout.menu_list_item);
	}

	@Override
	public long getItemId(int index) {
		return getItem(index).getItemId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView view = (TextView) super.getView(position, convertView, parent);
		MenuItem item = getItem(position);
		view.setEnabled(item.isEnabled());
		view.setVisibility(item.isVisible() ? View.VISIBLE : View.GONE);
		view.setCompoundDrawablesWithIntrinsicBounds(item.getIcon(), null, null, null);
		return view;
	}

	public void setMenu(Menu menu) {
		mMenu = menu;
		setMenuItems();
	}

	public void setMenuItems() {
		clear();
		List<MenuItem> items = mMenu == null ? null : ((MenuImpl) mMenu).getMenuItems();
		if (items == null) return;
		for (MenuItem item : items) {
			if (item.isVisible()) {
				add(item);
			}
		}
	}

}
