package io.bean.virtual.client.hook.patchs.pm;

import android.content.pm.ProviderInfo;

import io.bean.virtual.client.hook.base.Hook;
import io.bean.virtual.client.ipc.VPackageManager;
import io.bean.virtual.os.VUserHandle;

import java.lang.reflect.Method;

/**
 * @author Lody
 *
 *
 */
/* package */ class ResolveContentProvider extends Hook {

	@Override
	public String getName() {
		return "resolveContentProvider";
	}

	@Override
	public Object call(Object who, Method method, Object... args) throws Throwable {
		String name = (String) args[0];
		int flags = (int) args[1];
		int userId = VUserHandle.myUserId();
		ProviderInfo info =  VPackageManager.get().resolveContentProvider(name, flags, userId);
		if (info == null) {
			if (name.equals("settings")) {
				info = (ProviderInfo) method.invoke(who, args);
			}
		}
		return info;
	}
}
