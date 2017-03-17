package io.bean.virtual.client.hook.patchs.wifi;

import java.lang.reflect.Method;

import io.bean.virtual.client.hook.base.Hook;
import io.bean.virtual.client.hook.utils.HookUtils;

/**
 * @author Lody
 *
 */
/* package */ class GetBatchedScanResults extends Hook {

	@Override
	public String getName() {
		return "getBatchedScanResults";
	}

	@Override
	public Object call(Object who, Method method, Object... args) throws Throwable {
		HookUtils.replaceFirstAppPkg(args);
		return method.invoke(who, args);
	}
}
