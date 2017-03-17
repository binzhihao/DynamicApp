package io.bean.virtual.client.hook.patchs.am;

import android.os.IBinder;

import io.bean.virtual.client.hook.base.Hook;
import io.bean.virtual.client.ipc.VActivityManager;

import java.lang.reflect.Method;

/**
 * @author Lody
 *
 *
 */

/*package*/ class GetCallingActivity extends Hook {

	@Override
	public String getName() {
		return "getCallingActivity";
	}

	@Override
	public Object call(Object who, Method method, Object... args) throws Throwable {
		IBinder token = (IBinder) args[0];
		return VActivityManager.get().getCallingActivity(token);
	}

	@Override
	public boolean isEnable() {
		return isAppProcess();
	}
}
