package io.bean.virtual.client.hook.patchs.notification;

import io.bean.virtual.client.hook.base.Hook;
import io.bean.virtual.client.ipc.VNotificationManager;
import io.bean.virtual.helper.utils.ArrayUtils;

import java.lang.reflect.Method;

class SetNotificationsEnabledForPackage extends Hook {
    @Override
    public String getName() {
        return "setNotificationsEnabledForPackage";
    }
    @Override
    public Object call(Object who, Method method, Object... args) throws Throwable {
        String pkg  = (String) args[0];
        int enableIndex = ArrayUtils.indexOfFirst(args, Boolean.class);
        boolean enable = (boolean) args[enableIndex];
        VNotificationManager.get().setNotificationsEnabledForPackage(pkg, enable, getAppUserId());
        return 0;
    }
}
