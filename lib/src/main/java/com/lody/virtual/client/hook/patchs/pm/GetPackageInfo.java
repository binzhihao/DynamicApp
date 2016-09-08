package com.lody.virtual.client.hook.patchs.pm;

import android.content.pm.PackageInfo;
import android.os.Process;

import com.lody.virtual.client.hook.base.Hook;
import com.lody.virtual.client.local.VPackageManager;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.os.VUserHandle;

import java.lang.reflect.Method;
/**
 * @author Lody
 *
 *
 *         适配插件的包信息获取.
 *
 *         原型: public PackageInfo getPackageInfo(String packageName, int flags,
 *         int userId)
 */
public final class GetPackageInfo extends Hook {

	@Override
	public String getName() {
		return "getPackageInfo";
	}

	@Override
	public boolean beforeHook(Object who, Method method, Object... args) {
		return args != null && args[0] != null;
	}

	@Override
	public Object onHook(Object who, Method method, Object... args) throws Throwable {
		String pkg = (String) args[0];
		int flags = (int) args[1];
		int userId = VUserHandle.myUserId();
		PackageInfo packageInfo = VPackageManager.get().getPackageInfo(pkg, flags, userId);
		if (packageInfo != null) {
			packageInfo.applicationInfo.uid = Process.myUid();
			return packageInfo;
		}
		packageInfo = (PackageInfo) method.invoke(who, args);
		if (packageInfo != null) {
			if (getHostPkg().equals(packageInfo.packageName) || ComponentUtils.isSystemApp(packageInfo)) {
				return packageInfo;
			}
		}
		return null;
	}

}
