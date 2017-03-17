package io.bean.virtual.client.hook.patchs.am;

import java.lang.reflect.Method;

import io.bean.virtual.client.hook.base.Hook;
import io.bean.virtual.client.ipc.VActivityManager;

import android.app.Notification;
import android.content.ComponentName;
import android.os.IBinder;

/**
 * @author Lody
 *
 *
 * android.app.IActivityManager.setServiceForeground(ComponentName,
 *      IBinder, int, Notification, boolean)
 *
 *  N:
 *  android.app.IActivityManager.setServiceForeground(ComponentName,
 *      IBinder, int, Notification, boolean)
 */
/* package */ class SetServiceForeground extends Hook {

	@Override
	public String getName() {
		return "setServiceForeground";
	}

	@Override
	public Object call(Object who, Method method, Object... args) throws Throwable {
		return 0;
	}

	@Override
	public boolean isEnable() {
		return isAppProcess();
	}
}
