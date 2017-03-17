package io.bean.virtual.client.hook.base;

import java.lang.reflect.Method;

import io.bean.virtual.client.hook.utils.HookUtils;

/**
 * @author Lody
 */

public class ReplaceLastPkgHook extends StaticHook {

	public ReplaceLastPkgHook(String name) {
		super(name);
	}

	@Override
	public boolean beforeCall(Object who, Method method, Object... args) {
		HookUtils.replaceLastAppPkg(args);
		return super.beforeCall(who, method, args);
	}
}
