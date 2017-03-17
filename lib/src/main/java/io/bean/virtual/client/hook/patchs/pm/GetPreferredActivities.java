package io.bean.virtual.client.hook.patchs.pm;

import java.lang.reflect.Method;
import java.util.List;

import io.bean.virtual.client.hook.base.Hook;
import io.bean.virtual.client.hook.utils.HookUtils;

/**
 * @author Lody
 *
 * @see android.content.pm.IPackageManager#getPreferredActivities(List, List,
 *      String)
 *
 */
/* package */ class GetPreferredActivities extends Hook {

	@Override
	public String getName() {
		return "getPreferredActivities";
	}

	@Override
	public Object call(Object who, Method method, Object... args) throws Throwable {
		HookUtils.replaceLastAppPkg(args);
		return method.invoke(who, args);
	}
}
