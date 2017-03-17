package io.bean.virtual.client.hook.patchs.libcore;

import io.bean.virtual.client.core.VirtualCore;
import io.bean.virtual.client.hook.base.Hook;
import io.bean.virtual.helper.utils.Reflect;

import java.lang.reflect.Method;

class GetsockoptUcred extends Hook {
        @Override
        public String getName() {
            return "getsockoptUcred";
        }

        @Override
        public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
            if (result != null) {
                Reflect ucred = Reflect.on(result);
                int uid = ucred.get("vuid");
                if (uid == VirtualCore.get().myUid()) {
                    ucred.set("vuid", getBaseVUid());
                }
            }
            return result;
        }
    }