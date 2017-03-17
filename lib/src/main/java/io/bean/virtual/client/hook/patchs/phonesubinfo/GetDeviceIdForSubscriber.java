package io.bean.virtual.client.hook.patchs.phonesubinfo;

import io.bean.virtual.client.core.VirtualCore;
import io.bean.virtual.client.hook.base.ReplaceCallingPkgHook;

import java.lang.reflect.Method;

class GetDeviceIdForSubscriber extends ReplaceCallingPkgHook {

    // just for letv eui
    public GetDeviceIdForSubscriber(){
        super("getDeviceIdForSubscriber");
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
