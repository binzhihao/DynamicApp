package io.bean.virtual.client.hook.patchs.pm;

import android.content.Intent;
import android.content.pm.ResolveInfo;

import io.bean.virtual.client.hook.base.Hook;
import io.bean.virtual.client.ipc.VPackageManager;
import io.bean.virtual.os.VUserHandle;

import java.lang.reflect.Method;

/**
 * @author Lody
 *
 *
 *         原型: public ResolveInfo resolveService(Intent intent, String
 *         resolvedType, int flags, int userId)
 */
/* package */ class ResolveService extends Hook {

	@Override
	public String getName() {
		return "resolveService";
	}

	@Override
	public Object call(Object who, Method method, Object... args) throws Throwable {
		Intent intent = (Intent) args[0];
		String resolvedType = (String) args[1];
		int flags = (int) args[2];
		int userId = VUserHandle.myUserId();
		ResolveInfo resolveInfo = VPackageManager.get().resolveService(intent, resolvedType, flags, userId);
		if (resolveInfo == null) {
			resolveInfo = (ResolveInfo) method.invoke(who, args);
		}
		return resolveInfo;
	}
}
