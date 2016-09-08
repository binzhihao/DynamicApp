package com.lody.virtual.client.env;

import android.content.Intent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lody
 */
public final class SpecialComponentList {

	private static final List<String> ACTION_BLACK_LIST = new ArrayList<String>(1);

	private static final Map<String, String> MODIFY_ACTION_MAP = new HashMap<>();

	static {
		ACTION_BLACK_LIST.add("android.appwidget.action.APPWIDGET_UPDATE");
		MODIFY_ACTION_MAP.put(Intent.ACTION_PACKAGE_ADDED, Constants.ACTION_PACKAGE_ADDED);
		MODIFY_ACTION_MAP.put(Intent.ACTION_PACKAGE_REMOVED, Constants.ACTION_PACKAGE_REMOVED);
		MODIFY_ACTION_MAP.put(Intent.ACTION_PACKAGE_CHANGED, Constants.ACTION_PACKAGE_CHANGED);
		MODIFY_ACTION_MAP.put("android.intent.action.USER_ADDED", Constants.ACTION_USER_ADDED);
		MODIFY_ACTION_MAP.put("android.intent.action.USER_REMOVED", Constants.ACTION_USER_REMOVED);
	}

	/**
	 * 是否为黑名单Action
	 * 
	 * @param action
	 *            Action
	 */
	public static boolean isActionInBlackList(String action) {
		return ACTION_BLACK_LIST.contains(action);
	}

	/**
	 * 添加一个黑名单 Action
	 * 
	 * @param action
	 *            action
	 */
	public static void addBlackAction(String action) {
		ACTION_BLACK_LIST.add(action);
	}

	public static String modifyAction(String originAction) {
		String newAction = MODIFY_ACTION_MAP.get(originAction);
		if (newAction == null) {
			return String.format("_VA_protected_%s", originAction);
		}
		return newAction;
	}

	public static String restoreAction(String action) {
		for (Map.Entry<String, String> next : MODIFY_ACTION_MAP.entrySet()) {
			String modifiedAction = next.getValue();
			if (modifiedAction.equals(action)) {
				return next.getKey();
			}
		}
		return action.length() > "_VA_protected_".length() ?  action.substring("_VA_protected_".length()) : null;
	}
}
