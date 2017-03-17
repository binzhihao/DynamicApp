package io.bean.virtual.client.hook.patchs.am;

import io.bean.virtual.client.hook.base.Hook;

import java.lang.reflect.Method;

/**
 */
public class StartActivityIntentSender extends Hook {
	@Override
	public String getName() {
		return "startActivityIntentSender";
	}

	@Override
	public Object call(Object who, Method method, Object... args) throws Throwable {

		return super.call(who, method, args);
	}
}
