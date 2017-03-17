package io.bean.virtual.client.hook.patchs.pm;

import android.content.pm.ProviderInfo;

import io.bean.virtual.client.hook.base.Hook;
import io.bean.virtual.client.ipc.VPackageManager;
import io.bean.virtual.helper.compat.ParceledListSliceCompat;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Lody
 *
 *         <p/>
 *         Android 4.4+
 *
 * @see android.content.pm.IPackageManager#queryContentProviders(String, int,
 *      int)
 *
 */
@SuppressWarnings("unchecked")
/* package */ class QueryContentProviders extends Hook {

	@Override
	public String getName() {
		return "queryContentProviders";
	}

	@Override
	public Object call(Object who, Method method, Object... args) throws Throwable {
		String processName = (String) args[0];
		int flags = (int) args[2];
		List<ProviderInfo> infos = VPackageManager.get().queryContentProviders(processName, flags, 0);
		if (ParceledListSliceCompat.isReturnParceledListSlice(method)) {
			return ParceledListSliceCompat.create(infos);
		}
		return infos;
	}

}
