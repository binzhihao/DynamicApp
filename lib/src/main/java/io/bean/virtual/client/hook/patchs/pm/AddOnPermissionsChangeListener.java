package io.bean.virtual.client.hook.patchs.pm;

import java.lang.reflect.Method;

import io.bean.virtual.client.hook.base.Hook;

/**
 * @author Lody
 */

public class AddOnPermissionsChangeListener extends Hook {

	@Override
	public String getName() {
		return "addOnPermissionsChangeListener";
	}

	@Override
	public Object call(Object who, Method method, Object... args) throws Throwable {
		return 0;
	}
}
