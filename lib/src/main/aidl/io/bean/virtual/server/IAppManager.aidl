// IAppManager.aidl
package io.bean.virtual.server;

import io.bean.virtual.server.interfaces.IAppObserver;
import io.bean.virtual.server.interfaces.IAppRequestListener;
import io.bean.virtual.remote.InstalledAppInfo;
import io.bean.virtual.remote.InstallResult;

interface IAppManager {
    int[] getPackageInstalledUsers(String packageName);
    void scanApps();
    InstalledAppInfo getInstalledAppInfo(String pkg, int flags);
    InstallResult installPackage(String path, int flags);
    boolean isPackageLaunched(int userId, String packageName);
    void setPackageHidden(int userId, String packageName, boolean hidden);
    boolean installPackageAsUser(int userId, String packageName);
    boolean uninstallPackage(String packageName, int userId);
    List<InstalledAppInfo> getInstalledApps(int flags);
    List<InstalledAppInfo> getInstalledAppsAsUser(int userId, int flags);
    int getInstalledAppCount();
    boolean isAppInstalled(String packageName);
    boolean isAppInstalledAsUser(int userId, String packageName);

    void registerObserver(IAppObserver observer);
    void unregisterObserver(IAppObserver observer);

    void setAppRequestListener(IAppRequestListener listener);
    void clearAppRequestListener();
    IAppRequestListener getAppRequestListener();

}
