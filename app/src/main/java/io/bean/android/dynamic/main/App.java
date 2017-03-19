package io.bean.android.dynamic.main;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import io.bean.android.dynamic.common.Constants;
import io.bean.android.dynamic.util.LogUtil;
import io.bean.virtual.client.core.InstallStrategy;
import io.bean.virtual.client.core.VirtualCore;
import io.bean.virtual.remote.InstallResult;

public class App extends Application {

    private static final String TAG = Constants.CONFIG + "_app";

    private static final String[] GMS_PKG = {
            "com.google.android.gsf",
            "com.google.android.gsf.login",
            "com.google.android.syncadapters.calendar",
            "com.google.android.syncadapters.contacts",
            "com.google.android.gms",
            "com.google.vending"
    };

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            VirtualCore.get().startup(base);
            if (VirtualCore.get().isMainProcess()) {
                //installGms();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void installGms() {
        PackageManager pm = VirtualCore.get().getUnHookPackageManager();
        for (String pkg : GMS_PKG) {
            try {
                ApplicationInfo appInfo = pm.getApplicationInfo(pkg, 0);
                if (appInfo != null) {
                    String apkPath = appInfo.sourceDir;
                    InstallResult res = VirtualCore.get().installPackage(apkPath,
                            InstallStrategy.DEPEND_SYSTEM_IF_EXIST | InstallStrategy.COMPARE_VERSION);
                    if (!res.isSuccess) {
                        LogUtil.e(TAG, "Unable to install google app");
                    }
                }
            } catch (Throwable e) {
                // Ignore
            }
        }
        //LogUtil.e(TAG, "" + VirtualCore.get().getInstalledAppCount());
    }

}
