package io.bean.virtual.client.hook.patchs.am;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;

import io.bean.virtual.client.VClientImpl;
import io.bean.virtual.client.core.VirtualCore;
import io.bean.virtual.client.hook.base.Hook;
import io.bean.virtual.client.hook.delegate.TaskDescriptionDelegate;
import io.bean.virtual.helper.utils.DrawableUtils;

import java.lang.reflect.Method;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class SetTaskDescription extends Hook {
    @Override
    public String getName() {
        return "setTaskDescription";
    }

    @Override
    public Object call(Object who, Method method, Object... args) throws Throwable {
        ActivityManager.TaskDescription td = (ActivityManager.TaskDescription) args[1];
        String label = td.getLabel();
        Bitmap icon = td.getIcon();

        // If the activity label/icon isn't specified, the application's label/icon is shown instead
        // Android usually does that for us, but in this case we want info about the contained app,
        // not VirtualApp itself
        if (label == null || icon == null) {
            Application app = VClientImpl.get().getCurrentApplication();
            if (app != null) {
                try {
                    if (label == null) {
                        label = app.getApplicationInfo().loadLabel(app.getPackageManager()).toString();
                    }
                    if (icon == null) {
                        Drawable drawable = app.getApplicationInfo().loadIcon(app.getPackageManager());
                        if (drawable != null) {
                            icon = DrawableUtils.drawableToBitMap(drawable);
                        }
                    }
                    td = new ActivityManager.TaskDescription(label, icon, td.getPrimaryColor());
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        args[1] = td;
        return method.invoke(who, args);
    }

    @Override
    public boolean isEnable() {
        return isAppProcess();
    }
}
