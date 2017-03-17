package io.bean.virtual.client.hook.patchs.media.router;

import io.bean.virtual.client.hook.base.Hook;
import io.bean.virtual.client.hook.utils.HookUtils;

import java.lang.reflect.Method;

/**
 * @author Lody
 *
 */
/* package */ class RegisterClientAsUser extends Hook {


	@Override
	public String getName() {
		return "registerClientAsUser";
	}

	@Override
	public Object call(Object who, Method method, Object... args) throws Throwable {
		HookUtils.replaceFirstAppPkg(args);
		return method.invoke(who, args);
	}
}
