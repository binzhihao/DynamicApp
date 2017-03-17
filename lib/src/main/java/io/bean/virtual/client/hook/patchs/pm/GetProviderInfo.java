package io.bean.virtual.client.hook.patchs.pm;

import android.content.ComponentName;

import io.bean.virtual.client.hook.base.Hook;
import io.bean.virtual.client.ipc.VPackageManager;
import io.bean.virtual.os.VUserHandle;

import java.lang.reflect.Method;

/**
 * @author Lody
 *
 *
 *         原型: public ActivityInfo getServiceInfo(ComponentName className, int
 *         flags, int userId)
 *
 */
/* package */ class GetProviderInfo extends Hook {

	@Override
	public String getName() {
		return "getProviderInfo";
	}

	@Override
	public Object call(Object who, Method method, Object... args) throws Throwable {
		ComponentName componentName = (ComponentName) args[0];
		int flags = (int) args[1];
		if (getHostPkg().equals(componentName.getPackageName())) {
			return method.invoke(who, args);
		}
		int userId = VUserHandle.myUserId();
		return VPackageManager.get().getProviderInfo(componentName, flags, userId);
	}

}
