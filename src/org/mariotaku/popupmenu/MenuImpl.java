package org.mariotaku.popupmenu;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

class MenuImpl implements Menu {

	private final List<MenuItem> mMenuItems;
	private boolean mIsQwerty;
	private final Context mContext;

	public MenuImpl(Context context) {
		this(context, null);
	}

	public MenuImpl(Context context, MenuAdapter adapter) {
		mMenuItems = adapter == null ? null : new Menus(adapter);
		mContext = context;
	}

	@Override
	public MenuItem add(CharSequence title) {
		MenuItem item = new MenuItemImpl(mContext).setTitle(title);
		mMenuItems.add(item);
		return item;
	}

	@Override
	public MenuItem add(int titleRes) {
		MenuItem item = new MenuItemImpl(mContext).setTitle(titleRes);
		mMenuItems.add(item);
		return item;
	}

	@Override
	public MenuItem add(int groupId, int itemId, int order, CharSequence title) {
		MenuItem item = new MenuItemImpl(mContext).setGroupId(groupId).setItemId(itemId).setOrder(order)
				.setTitle(title);
		mMenuItems.add(order, item);
		return item;
	}

	@Override
	public MenuItem add(int groupId, int itemId, int order, int titleRes) {
		MenuItem item = new MenuItemImpl(mContext).setGroupId(groupId).setItemId(itemId).setOrder(order)
				.setTitle(titleRes);
		mMenuItems.add(order, item);
		return item;
	}

	@Override
	public int addIntentOptions(int groupId, int itemId, int order, ComponentName caller, Intent[] specifics,
			Intent intent, int flags, MenuItem[] outSpecificItems) {
		return 0;
	}

	@Override
	public SubMenu addSubMenu(CharSequence title) {
		MenuItem item = new MenuItemImpl(mContext).setTitle(title);
		SubMenu subMenu = new SubMenuImpl(mContext, item);
		((MenuItemImpl) item).setSubMenu(subMenu);
		mMenuItems.add(item);
		return subMenu;
	}

	@Override
	public SubMenu addSubMenu(int titleRes) {
		MenuItem item = new MenuItemImpl(mContext).setTitle(titleRes);
		SubMenu subMenu = new SubMenuImpl(mContext, item);
		((MenuItemImpl) item).setSubMenu(subMenu);
		mMenuItems.add(item);
		return subMenu;
	}

	@Override
	public SubMenu addSubMenu(int groupId, int itemId, int order, CharSequence title) {
		MenuItem item = new MenuItemImpl(mContext).setGroupId(groupId).setItemId(itemId).setOrder(order)
				.setTitle(title);
		SubMenu subMenu = new SubMenuImpl(mContext, item);
		((MenuItemImpl) item).setSubMenu(subMenu);
		mMenuItems.add(order, item);
		return subMenu;
	}

	@Override
	public SubMenu addSubMenu(int groupId, int itemId, int order, int titleRes) {
		MenuItem item = new MenuItemImpl(mContext).setGroupId(groupId).setItemId(itemId).setOrder(order)
				.setTitle(titleRes);
		SubMenu subMenu = new SubMenuImpl(mContext, item);
		((MenuItemImpl) item).setSubMenu(subMenu);
		mMenuItems.add(order, item);
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
			if (item.getItemId() == id) return item;
		}
		return null;
	}

	@Override
	public MenuItem getItem(int index) {
		return mMenuItems.get(index);
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

	}

	@Override
	public void setGroupEnabled(int group, boolean enabled) {

	}

	@Override
	public void setGroupVisible(int group, boolean visible) {

	}

	@Override
	public void setQwertyMode(boolean isQwerty) {
		mIsQwerty = isQwerty;

	}

	@Override
	public int size() {
		return mMenuItems.size();
	}

	List<MenuItem> getMenuItems() {
		return mMenuItems;
	}

}
