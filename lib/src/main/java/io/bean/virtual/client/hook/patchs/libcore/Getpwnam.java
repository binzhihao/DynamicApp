package io.bean.virtual.client.hook.patchs.libcore;

import io.bean.virtual.client.VClientImpl;
import io.bean.virtual.client.core.VirtualCore;
import io.bean.virtual.client.hook.base.Hook;
import io.bean.virtual.helper.utils.Reflect;

import java.lang.reflect.Method;

class Getpwnam extends Hook {
        @Override
        public String getName() {
            return "getpwnam";
        }

        @Override
        public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
            if (result != null) {
                Reflect pwd = Reflect.on(result);
                int uid = pwd.get("pw_uid");
                if (uid == VirtualCore.get().myUid()) {
                    pwd.set("pw_uid", VClientImpl.get().getVUid());
                }
            }
            return result;
        }
    }
