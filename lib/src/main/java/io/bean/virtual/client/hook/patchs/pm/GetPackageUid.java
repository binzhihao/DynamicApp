package io.bean.virtual.client.hook.patchs.pm;

import io.bean.virtual.client.hook.base.Hook;
import io.bean.virtual.client.ipc.VPackageManager;
import io.bean.virtual.os.VUserHandle;

import java.lang.reflect.Method;

/**
 * @author Lody
 *
 */
/* package */ class GetPackageUid extends Hook {

	@Override
	public String getName() {
		return "getPackageUid";
	}

	@Override
	public Object call(Object who, Method method, Object... args) throws Throwable {
		String pkgName = (String) args[0];
		if (pkgName.equals(getHostPkg())) {
			return method.invoke(who, args);
		}
		int uid = VPackageManager.get().getPackageUid(pkgName, 0);
		return VUserHandle.getAppId(uid);
	}

	@Override
	public boolean isEnable() {
		return isAppProcess();
	}

}
