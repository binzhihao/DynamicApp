package io.bean.virtual.client.hook.patchs.am;

import java.lang.reflect.Method;

import io.bean.virtual.client.hook.base.Hook;
import io.bean.virtual.client.hook.utils.HookUtils;

/**
 * @author Lody
 *
 *
 * @see android.app.IActivityManager#addPackageDependency(String)
 */
/* package */ class AddPackageDependency extends Hook {

	@Override
	public String getName() {
		return "addPackageDependency";
	}

	@Override
	public Object call(Object who, Method method, Object... args) throws Throwable {
		HookUtils.replaceFirstAppPkg(args);
		return method.invoke(who, args);
	}

	@Override
	public boolean isEnable() {
		return isAppProcess();
	}
}
