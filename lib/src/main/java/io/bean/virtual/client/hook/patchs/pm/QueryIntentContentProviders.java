package io.bean.virtual.client.hook.patchs.pm;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Build;

import io.bean.virtual.client.hook.base.Hook;
import io.bean.virtual.client.ipc.VPackageManager;
import io.bean.virtual.helper.compat.ParceledListSliceCompat;
import io.bean.virtual.os.VUserHandle;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Lody
 *
 *
 *         Android 4.4+
 *
 */
@SuppressWarnings("unchecked")
@TargetApi(Build.VERSION_CODES.KITKAT)
/* package */ class QueryIntentContentProviders extends Hook {

	@Override
	public String getName() {
		return "queryIntentContentProviders";
	}

	@Override
	public Object call(Object who, Method method, Object... args) throws Throwable {
		int userId = VUserHandle.myUserId();
		List<ResolveInfo> appResult =  VPackageManager.get().queryIntentContentProviders((Intent) args[0], (String) args[1],
				(Integer) args[2], userId);
		if (ParceledListSliceCompat.isReturnParceledListSlice(method)) {
			return ParceledListSliceCompat.create(appResult);
		}
		return appResult;
	}

	@Override
	public boolean isEnable() {
		return isAppProcess();
	}
}
