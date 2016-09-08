package com.lody.virtual.client.hook.patchs.pm;

import com.lody.virtual.client.hook.base.Hook;
import com.lody.virtual.client.local.VPackageManager;
import com.lody.virtual.os.VUserHandle;

import java.lang.reflect.Method;

/**
 * @author Lody
 *
 *
 *         原型: public ProviderInfo resolveContentProvider(String name, int
 *         flags, int userId);
 * @see android.content.pm.IPackageManager#resolveContentProvider(String, int,
 *      int)
 */
/* package */ class ResolveContentProvider extends Hook {

	@Override
	public String getName() {
		return "resolveContentProvider";
	}

	@Override
	public Object onHook(Object who, Method method, Object... args) throws Throwable {
		String name = (String) args[0];
		int flags = (int) args[1];
		int userId = VUserHandle.myUserId();
		return VPackageManager.get().resolveContentProvider(name, flags, userId);
	}
}
