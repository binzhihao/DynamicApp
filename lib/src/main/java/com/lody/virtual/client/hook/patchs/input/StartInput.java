package com.lody.virtual.client.hook.patchs.input;

import java.lang.reflect.Method;

import com.lody.virtual.client.hook.base.Hook;

import android.view.inputmethod.EditorInfo;

/**
 * @author Lody
 *
 *
 *         原型: public InputBindResult startInput(IInputMethodClient client,
 *         IInputContext inputContext, EditorInfo attribute, int controlFlags)
 */
/* package */ class StartInput extends Hook {

	@Override
	public String getName() {
		return "startInput";
	}

	@Override
	public Object onHook(Object who, Method method, Object... args) throws Throwable {
		if (args.length > 2 && args[2] instanceof EditorInfo) {
			EditorInfo attribute = (EditorInfo) args[2];
			String pkgName = attribute.packageName;
			if (isAppPkg(pkgName)) {
				attribute.packageName = getHostPkg();
			}
		}
		return method.invoke(who, args);
	}

}
