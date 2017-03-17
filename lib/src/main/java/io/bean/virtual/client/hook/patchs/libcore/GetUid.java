package io.bean.virtual.client.hook.patchs.libcore;

import io.bean.virtual.client.NativeEngine;
import io.bean.virtual.client.hook.base.Hook;

import java.lang.reflect.Method;

/**
 * @author Lody
 */
public class GetUid extends Hook {

    @Override
    public String getName() {
        return "getuid";
    }

    @Override
    public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
        int uid = (int) result;
        return NativeEngine.onGetUid(uid);
    }
}
