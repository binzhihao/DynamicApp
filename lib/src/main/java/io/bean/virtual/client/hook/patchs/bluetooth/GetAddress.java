package io.bean.virtual.client.hook.patchs.bluetooth;


import io.bean.virtual.client.core.VirtualCore;
import io.bean.virtual.client.hook.base.StaticHook;

import java.lang.reflect.Method;

class GetAddress extends StaticHook {

    GetAddress(){
        super("getAddress");
    }

    @Override
    public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
        if (VirtualCore.get().getPhoneInfoDelegate() != null) {
            String res = VirtualCore.get().getPhoneInfoDelegate().getBluetoothAddress((String) result, getAppUserId());
            if (res != null) {
                return res;
            }
        }
        return super.afterCall(who, method, args, result);
    }
}