package io.bean.virtual.client.hook.patchs.notification;

import android.app.Notification;

import io.bean.virtual.client.hook.base.Hook;
import io.bean.virtual.client.ipc.VNotificationManager;
import io.bean.virtual.helper.utils.ArrayUtils;

import java.lang.reflect.Method;

/**
 * @author Lody
 */
/* package */ class EnqueueNotification extends Hook {

    @Override
    public String getName() {
        return "enqueueNotification";
    }

    @Override
    public Object call(Object who, Method method, Object... args) throws Throwable {
        //enqueueNotification(pkg, id, notification, idOut);
        String pkg = (String) args[0];
        int notificationIndex = ArrayUtils.indexOfFirst(args, Notification.class);
        int idIndex = ArrayUtils.indexOfFirst(args, Integer.class);
        int id = (int) args[idIndex];
        id = VNotificationManager.get().dealNotificationId(id, pkg, null, getAppUserId());
        args[idIndex] = id;
        Notification notification = (Notification) args[notificationIndex];
        if (!VNotificationManager.get().dealNotification(id, notification, pkg)) {
            return 0;
        }
        VNotificationManager.get().addNotification(id, null, pkg, getAppUserId());
        args[0] = getHostPkg();
        return method.invoke(who, args);
    }
}
