package io.bean.virtual.client.hook.patchs.display;

import io.bean.virtual.client.hook.base.Hook;
import io.bean.virtual.client.hook.utils.HookUtils;

import java.lang.reflect.Method;

/**
 * @author Lody
 *
 *
 */
/* package */ class CreateVirtualDisplay extends Hook {

	@Override
	public boolean beforeCall(Object who, Method method, Object... args) {
		HookUtils.replaceFirstAppPkg(args);
		return super.beforeCall(who, method, args);
	}

	@Override
	public String getName() {
		return "createVirtualDisplay";
	}

}
