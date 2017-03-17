package io.bean.virtual.client.hook.patchs.am;

import io.bean.virtual.client.hook.base.Hook;
import io.bean.virtual.client.hook.utils.HookUtils;

import java.lang.reflect.Method;

/**
 * @author Lody
 *
 *
 *         public int checkGrantUriPermission(int callingUid, String
 *         targetPkg, Uri uri, int modeFlags)
 */
/* package */ class CheckGrantUriPermission extends Hook {

	@Override
	public String getName() {
		return "checkGrantUriPermission";
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
