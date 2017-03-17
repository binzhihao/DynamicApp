package io.bean.virtual.client.hook.patchs.pm;

import android.content.ComponentName;

import io.bean.virtual.client.hook.base.Hook;
import io.bean.virtual.client.ipc.VPackageManager;
import io.bean.virtual.os.VUserHandle;

import java.lang.reflect.Method;

import static android.content.pm.PackageManager.GET_DISABLED_COMPONENTS;

/**
 * @author Lody
 *
 *
 *         原型: public ActivityInfo getActivityInfo(ComponentName className, int
 *         flags, int userId)
 *
 */
/* package */ class GetActivityInfo extends Hook {

	@Override
	public String getName() {
		return "getActivityInfo";
	}

	@Override
	public Object call(Object who, Method method, Object... args) throws Throwable {
		ComponentName componentName = (ComponentName) args[0];
		if (getHostPkg().equals(componentName.getPackageName())) {
			return method.invoke(who, args);
		}
		int userId = VUserHandle.myUserId();
		int flags = (int) args[1];
		if ((flags & GET_DISABLED_COMPONENTS) == 0) {
			flags |= GET_DISABLED_COMPONENTS;
		}
		return VPackageManager.get().getActivityInfo(componentName, flags, userId);
	}

	@Override
	public boolean isEnable() {
		return isAppProcess();
	}
}
