package io.bean.virtual.client.hook.patchs.pm;

import android.content.pm.ApplicationInfo;

import io.bean.virtual.client.hook.base.Hook;
import io.bean.virtual.client.ipc.VPackageManager;
import io.bean.virtual.helper.utils.ComponentUtils;
import io.bean.virtual.os.VUserHandle;

import java.lang.reflect.Method;

/**
 * @author Lody
 *
 */
/* package */ class GetApplicationInfo extends Hook {

	@Override
	public String getName() {
		return "getApplicationInfo";
	}

	@Override
	public Object call(Object who, Method method, Object... args) throws Throwable {
		String pkg = (String) args[0];
		int flags = (int) args[1];
		if (getHostPkg().equals(pkg)) {
			return method.invoke(who, args);
		}
		int userId = VUserHandle.myUserId();
		ApplicationInfo applicationInfo = VPackageManager.get().getApplicationInfo(pkg, flags, userId);
		if (applicationInfo != null) {
			return applicationInfo;
		}
		applicationInfo = (ApplicationInfo) method.invoke(who, args);
		if (applicationInfo != null) {
			if (ComponentUtils.isSystemApp(applicationInfo)) {
				return applicationInfo;
			}
		}
		return null;
	}

	@Override
	public boolean isEnable() {
		return isAppProcess();
	}
}
