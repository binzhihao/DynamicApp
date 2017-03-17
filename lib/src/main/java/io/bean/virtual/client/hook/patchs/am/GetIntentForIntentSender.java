package io.bean.virtual.client.hook.patchs.am;

import io.bean.virtual.client.hook.base.Hook;

import java.lang.reflect.Method;

/**
 * @author Lody
 */

public class GetIntentForIntentSender extends Hook {

    @Override
    public String getName() {
        return "getIntentForIntentSender";
    }

    @Override
    public Object call(Object who, Method method, Object... args) throws Throwable {
        return super.call(who, method, args);
    }
}
