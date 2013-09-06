package org.mariotaku.popupmenu;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

public final class SubMenuImpl extends MenuImpl implements SubMenu {

	private final List<MenuItem> mMenuItems;
	private final MenuAdapter mAdapter;
	private final MenuItem menuItem;
	private final Context mContext;

	public SubMenuImpl(Context context, MenuItem menuItem) {
		super(context);
		mContext = context;
		mAdapter = new MenuAdapter(context);

		mMenuItems = new Menus(mAdapter);
		this.menuItem = menuItem;
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
		// TODO Auto-generated method stub
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
	public void clearHeader() {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public MenuItem findItem(int id) {
		for (MenuItem item : mMenuItems) {
			if (item.getItemId() == id) return item;
		}
		return null;
	}

	@Override
	public MenuItem getItem() {
		return menuItem;
	}

	@Override
	public MenuItem getItem(int index) {
		return mMenuItems.get(index);
	}

	@Override
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
		// TODO Auto-generated method stub

	}

	@Override
	public void setGroupEnabled(int group, boolean enabled) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setGroupVisible(int group, boolean visible) {
		// TODO Auto-generated method stub

	}

	@Override
	public SubMenu setHeaderIcon(Drawable icon) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public SubMenu setHeaderIcon(int iconRes) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public SubMenu setHeaderTitle(CharSequence title) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public SubMenu setHeaderTitle(int titleRes) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public SubMenu setHeaderView(View view) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public SubMenu setIcon(Drawable icon) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public SubMenu setIcon(int iconRes) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public void setQwertyMode(boolean isQwerty) {
		// TODO Auto-generated method stub

	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

}
