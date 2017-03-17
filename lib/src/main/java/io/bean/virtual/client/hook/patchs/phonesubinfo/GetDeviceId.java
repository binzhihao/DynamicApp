package io.bean.virtual.client.hook.patchs.phonesubinfo;


import io.bean.virtual.client.core.VirtualCore;
import io.bean.virtual.client.hook.base.ReplaceCallingPkgHook;

import java.lang.reflect.Method;

class GetDeviceId extends ReplaceCallingPkgHook {

    public GetDeviceId(){
        super("getDeviceId");
    }

    @Override
    public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
        if (VirtualCore.get().getPhoneInfoDelegate() != null) {
            String res = VirtualCore.get().getPhoneInfoDelegate().getDeviceId((String) result, getAppUserId());
            if (res != null) {
                return res;
            }
        }
        return super.afterCall(who, method, args, result);
    }
}