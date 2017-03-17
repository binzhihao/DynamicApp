package io.bean.android.knock;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.lody.virtual.client.core.InstallStrategy;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.proto.InstallResult;

public class App extends Application {

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
                InstallResult res = VirtualCore.get().installApp(apkPath,
                        InstallStrategy.DEPEND_SYSTEM_IF_EXIST | InstallStrategy.TERMINATE_IF_EXIST);
                if (!res.isSuccess) {
                    Utils.log("fuck", "Unable to install google app");
                }
            } catch (Throwable e) {
                // Ignore
            }
        }
    }
}
