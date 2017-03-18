package io.bean.virtual.client.hook.patchs.am;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.TypedValue;

import io.bean.virtual.client.core.VirtualCore;
import io.bean.virtual.client.env.Constants;
import io.bean.virtual.client.hook.base.Hook;
import io.bean.virtual.client.ipc.ActivityClientRecord;
import io.bean.virtual.client.ipc.VActivityManager;
import io.bean.virtual.client.stub.ChooserActivity;
import io.bean.virtual.helper.compat.ActivityManagerCompat;
import io.bean.virtual.helper.utils.ArrayUtils;
import io.bean.virtual.helper.utils.ComponentUtils;
import io.bean.virtual.os.VUserHandle;
import io.bean.virtual.server.interfaces.IAppRequestListener;

import java.io.File;
import java.lang.reflect.Method;

class StartActivity extends Hook {

    @Override
    public String getName() {
        return "startActivity";
    }

    @Override
    public Object call(Object who, Method method, Object... args) throws Throwable {
        int intentIndex = ArrayUtils.indexOfObject(args, Intent.class, 1);
        if (intentIndex < 0) {
            return ActivityManagerCompat.START_INTENT_NOT_RESOLVED;
        }
        int resultToIndex = ArrayUtils.indexOfObject(args, IBinder.class, 2);
        String resolvedType = (String) args[intentIndex + 1];
        Intent intent = (Intent) args[intentIndex];
        intent.setDataAndType(intent.getData(), resolvedType);
        IBinder resultTo = resultToIndex >= 0 ? (IBinder) args[resultToIndex] : null;
        int userId = VUserHandle.myUserId();

        if (ComponentUtils.isStubComponent(intent)) {
            return method.invoke(who, args);
        }

        String resultWho = null;
        int requestCode = 0;
        Bundle options = ArrayUtils.getFirst(args, Bundle.class);
        if (resultTo != null) {
            resultWho = (String) args[resultToIndex + 1];
            requestCode = (int) args[resultToIndex + 2];
        }
        // chooser
        if (ChooserActivity.check(intent)) {
            intent.setComponent(new ComponentName(getHostContext(), ChooserActivity.class));
            intent.putExtra(Constants.EXTRA_USER_HANDLE, userId);
            intent.putExtra(ChooserActivity.EXTRA_DATA, options);
            intent.putExtra(ChooserActivity.EXTRA_WHO, resultWho);
            intent.putExtra(ChooserActivity.EXTRA_REQUEST_CODE, requestCode);
            return method.invoke(who, args);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            args[intentIndex - 1] = getHostPkg();
        }

        ActivityInfo activityInfo = VirtualCore.get().resolveActivityInfo(intent, userId);
        if (activityInfo == null) {
            return method.invoke(who, args);
        }

        int res = VActivityManager.get().startActivity(intent, activityInfo, resultTo, options, resultWho, requestCode, VUserHandle.myUserId());
        if (res != 0 && resultTo != null && requestCode > 0) {
            VActivityManager.get().sendActivityResult(resultTo, resultWho, requestCode);
        }
        if (resultTo != null) {
            ActivityClientRecord r = VActivityManager.get().getActivityRecord(resultTo);
            if (r != null && r.activity != null) {
                try {
                    TypedValue out = new TypedValue();
                    Resources.Theme theme = r.activity.getResources().newTheme();
                    theme.applyStyle(activityInfo.getThemeResource(), true);
                    if (theme.resolveAttribute(android.R.attr.windowAnimationStyle, out, true)) {

                        TypedArray array = theme.obtainStyledAttributes(out.data,
                                new int[]{
                                        android.R.attr.activityOpenEnterAnimation,
                                        android.R.attr.activityOpenExitAnimation
                                });

                        r.activity.overridePendingTransition(array.getResourceId(0, 0), array.getResourceId(1, 0));
                        array.recycle();
                    }
                } catch (Throwable e) {
                    // Ignore
                }
            }
        }
        return res;
    }

}
