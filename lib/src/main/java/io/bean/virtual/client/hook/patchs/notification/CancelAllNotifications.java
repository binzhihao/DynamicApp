package io.bean.virtual.client.hook.patchs.notification;

import io.bean.virtual.client.core.VirtualCore;
import io.bean.virtual.client.hook.base.Hook;
import io.bean.virtual.client.hook.utils.HookUtils;
import io.bean.virtual.client.ipc.VNotificationManager;

import java.lang.reflect.Method;

/**
 * @author Lody
 */
/* package */ class CancelAllNotifications extends Hook {

    @Override
    public String getName() {
        return "cancelAllNotifications";
    }

    @Override
    public Object call(Object who, Method method, Object... args) throws Throwable {
        String pkg = HookUtils.replaceFirstAppPkg(args);
//        int user = 0;
//        if (Build.VERSION.SDK_INT >= 17) {
//            user = (int) args[1];
//        }
        if (VirtualCore.get().isAppInstalled(pkg)) {
            VNotificationManager.get().cancelAllNotification(pkg, getAppUserId());
            return 0;
        }
        return method.invoke(who, args);
    }
}
