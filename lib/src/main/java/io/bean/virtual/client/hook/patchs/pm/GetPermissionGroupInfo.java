package io.bean.virtual.client.hook.patchs.pm;

import java.lang.reflect.Method;

import io.bean.virtual.client.hook.base.Hook;
import io.bean.virtual.client.ipc.VPackageManager;

import android.content.pm.PermissionGroupInfo;

/**
 * @author Lody
 *
 * @see android.content.pm.IPackageManager#getPermissionInfo(String, int)
 *
 */

public class GetPermissionGroupInfo extends Hook {

	@Override
	public String getName() {
		return "getPermissionGroupInfo";
	}

	@Override
	public Object call(Object who, Method method, Object... args) throws Throwable {
		String name = (String) args[0];
		int flags = (int) args[1];
		PermissionGroupInfo info = VPackageManager.get().getPermissionGroupInfo(name, flags);
		if (info != null) {
			return info;
		}
		return super.call(who, method, args);
	}

	@Override
	public boolean isEnable() {
		return isAppProcess();
	}
}
