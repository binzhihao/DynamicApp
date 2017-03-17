package io.bean.virtual.client.hook.patchs.notification;

import io.bean.virtual.client.hook.base.Hook;
import io.bean.virtual.client.ipc.VNotificationManager;

import java.lang.reflect.Method;

class AreNotificationsEnabledForPackage extends Hook {
    @Override
    public String getName() {
        return "areNotificationsEnabledForPackage";
    }

    @Override
    public Object call(Object who, Method method, Object... args) throws Throwable {
        String pkg  = (String) args[0];
        return VNotificationManager.get().areNotificationsEnabledForPackage(pkg, getAppUserId());
//        return super.call(who, method, args);
    }
}
