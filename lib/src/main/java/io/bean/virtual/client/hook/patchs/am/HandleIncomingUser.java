package io.bean.virtual.client.hook.patchs.am;

import io.bean.virtual.client.hook.base.Hook;

import java.lang.reflect.Method;

/**
 * @author Lody
 *
 */
/* package */ class HandleIncomingUser extends Hook {

	@Override
	public String getName() {
		return "handleIncomingUser";
	}

	@Override
	public Object call(Object who, Method method, Object... args) throws Throwable {
		int lastIndex = args.length - 1;
		if (args[lastIndex] instanceof String) {
			args[lastIndex] = getHostPkg();
		}
		return method.invoke(who, args);
	}

	@Override
	public boolean isEnable() {
		return isAppProcess();
	}

}
