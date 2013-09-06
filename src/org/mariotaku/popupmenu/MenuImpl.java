package org.mariotaku.popupmenu;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

public class MenuImpl implements Menu {

	private final List<MenuItem> mMenuItems;
	private boolean mIsQwerty;
	private final Context mContext;

	public MenuImpl(Context context) {
		this(context, null);
	}

	public MenuImpl(Context context, MenuAdapter adapter) {
		mMenuItems = new Menus(adapter);
		mContext = context;
	}

	@Override
	public MenuItem add(CharSequence title) {
		return add(0, 0, 0, title);
	}

	@Override
	public MenuItem add(int titleRes) {
		return add(0, 0, 0, titleRes);
	}

	@Override
	public MenuItem add(int groupId, int itemId, int order, CharSequence title) {
		MenuItem item = new MenuItemImpl(mContext).setGroupId(groupId).setItemId(itemId).setOrder(order)
				.setTitle(title);
		mMenuItems.add(item);
		return item;
	}

	@Override
	public MenuItem add(int groupId, int itemId, int order, int titleRes) {
		MenuItem item = new MenuItemImpl(mContext).setGroupId(groupId).setItemId(itemId).setOrder(order)
				.setTitle(titleRes);
		mMenuItems.add(item);
		return item;
	}

	@Override
	public int addIntentOptions(int groupId, int itemId, int order, ComponentName caller, Intent[] specifics,
			Intent intent, int flags, MenuItem[] outSpecificItems) {
		return 0;
	}

	@Override
	public SubMenu addSubMenu(CharSequence title) {
		return addSubMenu(0, 0, 0, title);
	}

	@Override
	public SubMenu addSubMenu(int titleRes) {
		return addSubMenu(0, 0, 0, titleRes);
	}

	@Override
	public SubMenu addSubMenu(int groupId, int itemId, int order, CharSequence title) {
		MenuItem item = new MenuItemImpl(mContext).setGroupId(groupId).setItemId(itemId).setOrder(order)
				.setTitle(title);
		SubMenu subMenu = new SubMenuImpl(mContext, item);
		((MenuItemImpl) item).setSubMenu(subMenu);
		if (order != 0) {
			mMenuItems.add(order, item);
		} else {
			mMenuItems.add(item);
		}
		return subMenu;
	}

	@Override
	public SubMenu addSubMenu(int groupId, int itemId, int order, int titleRes) {
		MenuItem item = new MenuItemImpl(mContext).setGroupId(groupId).setItemId(itemId).setOrder(order)
				.setTitle(titleRes);
		SubMenu subMenu = new SubMenuImpl(mContext, item);
		((MenuItemImpl) item).setSubMenu(subMenu);
		if (order != 0) {
			mMenuItems.add(order, item);
		} else {
			mMenuItems.add(item);
		}
		return subMenu;
	}

	@Override
	public void clear() {
		mMenuItems.clear();
	}

	@Override
	public void close() {

	}

	@Override
	public MenuItem findItem(int id) {
		for (MenuItem item : mMenuItems) {
			if (item.getItemId() == id)
				return item;
			else if (item.hasSubMenu()) {
				MenuItem possibleItem = item.getSubMenu().findItem(id);

				if (possibleItem != null) return possibleItem;
			}
		}
		return null;
	}

	@Override
	public MenuItem getItem(int index) {
		return mMenuItems.get(index);
	}

	public List<MenuItem> getMenuItems() {
		return mMenuItems;
	}

	@Override
	public boolean hasVisibleItems() {
		for (MenuItem item : mMenuItems) {
			if (item.isVisible()) return true;
		}
		return false;
	}

	@Override
	public boolean isShortcutKey(int keyCode, KeyEvent event) {
		return false;
	}

	@Override
	public boolean performIdentifierAction(int id, int flags) {
		return false;
	}

	@Override
	public boolean performShortcut(int keyCode, KeyEvent event, int flags) {
		return false;
	}

	@Override
	public void removeGroup(int groupId) {
		for (MenuItem item : mMenuItems) {
			if (item.getGroupId() == groupId) {
				mMenuItems.remove(item);
			}
		}
	}

	@Override
	public void removeItem(int id) {
		for (MenuItem item : mMenuItems) {
			if (item.getItemId() == id) {
				mMenuItems.remove(item);
			}
		}

	}

	@Override
	public void setGroupCheckable(int group, boolean checkable, boolean exclusive) {
		for (MenuItem item : mMenuItems) {
			if (item.getGroupId() == group) {
				item.setCheckable(checkable);
				if (exclusive) {
					break;
				}
			}
		}
	}

	@Override
	public void setGroupEnabled(int group, boolean enabled) {
		for (MenuItem item : mMenuItems) {
			if (item.getGroupId() == group) {
				item.setEnabled(enabled);
			}
		}

	}

	@Override
	public void setGroupVisible(int group, boolean visible) {
		for (MenuItem item : mMenuItems) {
			if (item.getGroupId() == group) {
				item.setVisible(visible);
			}
		}
	}

	@Override
	public void setQwertyMode(boolean isQwerty) {
		mIsQwerty = isQwerty;

	}

	@Override
	public int size() {
		return mMenuItems.size();
	}

}
