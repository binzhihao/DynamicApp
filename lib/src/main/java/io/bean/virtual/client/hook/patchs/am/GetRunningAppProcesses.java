package io.bean.virtual.client.hook.patchs.am;

import android.app.ActivityManager;

import io.bean.virtual.client.hook.base.Hook;
import io.bean.virtual.client.ipc.VActivityManager;
import io.bean.virtual.os.VUserHandle;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Lody
 *
 */
@SuppressWarnings("unchecked")
/* package */ class GetRunningAppProcesses extends Hook {

	@Override
	public String getName() {
		return "getRunningAppProcesses";
	}

	@Override
	public synchronized Object call(Object who, Method method, Object... args) throws Throwable {
		List<ActivityManager.RunningAppProcessInfo> infoList = (List<ActivityManager.RunningAppProcessInfo>) method
				.invoke(who, args);
		if (infoList != null) {
			for (ActivityManager.RunningAppProcessInfo info : infoList) {
				if (VActivityManager.get().isAppPid(info.pid)) {
					List<String> pkgList = VActivityManager.get().getProcessPkgList(info.pid);
					String processName = VActivityManager.get().getAppProcessName(info.pid);
					if (processName != null) {
						info.processName = processName;
					}
					info.pkgList = pkgList.toArray(new String[pkgList.size()]);
					info.uid = VUserHandle.getAppId(VActivityManager.get().getUidByPid(info.pid));
				}
			}
		}
		return infoList;
	}
}
