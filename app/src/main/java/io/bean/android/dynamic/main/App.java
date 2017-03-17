package io.bean.android.dynamic.main;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import io.bean.virtual.client.core.InstallStrategy;
import io.bean.virtual.client.core.VirtualCore;
import io.bean.virtual.remote.InstallResult;

import io.bean.android.dynamic.common.Constants;
import io.bean.android.dynamic.util.LogUtil;

public class App extends Application {

    private static final String TAG = Constants.CONFIG + "_app";

    private static final String[] GMS_PKG = {
            "com.google.android.gms",
    };

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            VirtualCore.get().startup(base);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (VirtualCore.get().isMainProcess()) {
            // Install the Google mobile service
            installGms();
        }
    }

    private void installGms() {
        PackageManager pm = VirtualCore.get().getUnHookPackageManager();
        for (String pkg : GMS_PKG) {
            try {
                ApplicationInfo appInfo = pm.getApplicationInfo(pkg, 0);
                String apkPath = appInfo.sourceDir;
                InstallResult res = VirtualCore.get().installPackage(apkPath,
                        InstallStrategy.DEPEND_SYSTEM_IF_EXIST | InstallStrategy.COMPARE_VERSION);
                if (!res.isSuccess) {
                    LogUtil.e(TAG, "Unable to install google app");
                }
            } catch (Throwable e) {
                // Ignore
            }
        }
    }
}
