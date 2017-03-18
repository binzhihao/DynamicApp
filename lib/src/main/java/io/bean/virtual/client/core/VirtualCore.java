package io.bean.virtual.client.core;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.ConditionVariable;
import android.os.Looper;
import android.os.Process;
import android.os.RemoteException;

import java.util.List;

import io.bean.virtual.client.env.Constants;
import io.bean.virtual.client.env.VirtualRuntime;
import io.bean.virtual.client.fixer.ContextFixer;
import io.bean.virtual.client.hook.delegate.ComponentDelegate;
import io.bean.virtual.client.hook.delegate.PhoneInfoDelegate;
import io.bean.virtual.client.ipc.LocalProxyUtils;
import io.bean.virtual.client.ipc.ServiceManagerNative;
import io.bean.virtual.client.ipc.VActivityManager;
import io.bean.virtual.client.ipc.VPackageManager;
import io.bean.virtual.client.stub.StubManifest;
import io.bean.virtual.os.VUserHandle;
import io.bean.virtual.remote.InstallResult;
import io.bean.virtual.remote.InstalledAppInfo;
import io.bean.virtual.server.IAppManager;
import mirror.android.app.ActivityThread;

public final class VirtualCore {

    public static final int GET_HIDDEN_APP = 0x00000001;

    private enum ProcessType {
        Server,     // Server process
        VAppClient, // Virtual app process
        Main,       // Main process
        CHILD       // Child process
    }

    private final int myUid = Process.myUid();
    private PackageManager unHookPackageManager;
    private String hostPkgName;
    private Object mainThread;
    private Context context;
    private String processName;
    private ProcessType processType;
    private IAppManager mService;
    private boolean isStartUp;
    private PackageInfo hostPkgInfo;
    private int systemPid;
    private ConditionVariable initLock = new ConditionVariable();
    private PhoneInfoDelegate phoneInfoDelegate;
    private ComponentDelegate componentDelegate;

    @SuppressLint("StaticFieldLeak")
    private static class InstanceHolder {
        private static final VirtualCore gCore = new VirtualCore();
    }

    private VirtualCore() {
    }

    public static VirtualCore get() {
        return InstanceHolder.gCore;
    }

    public void startup(Context context) throws Throwable {
        if (!isStartup()) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                throw new IllegalStateException("VirtualCore.startup() must called in main thread.");
            }
            StubManifest.STUB_CP_AUTHORITY = context.getPackageName() + "." + StubManifest.STUB_DEF_AUTHORITY;
            ServiceManagerNative.SERVICE_CP_AUTH = context.getPackageName() + "." + ServiceManagerNative.SERVICE_DEF_AUTH;
            this.context = context;
            this.mainThread = ActivityThread.currentActivityThread.call();
            this.unHookPackageManager = context.getPackageManager();
            this.hostPkgInfo = unHookPackageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_PROVIDERS);
            detectProcessType();
            PatchManager patchManager = PatchManager.getInstance();
            patchManager.init();
            patchManager.injectAll();
            ContextFixer.fixContext(context);   // fix
            isStartUp = true;
            if (this.initLock != null) {
                this.initLock.open();
                this.initLock = null;
            }
        }
    }

    private void detectProcessType() {
        // Host package name
        hostPkgName = context.getApplicationInfo().packageName;
        // Current process name
        processName = ActivityThread.getProcessName.call(mainThread);
        if (processName.equals(context.getApplicationInfo().processName)) {
            processType = ProcessType.Main;
        } else if (processName.endsWith(Constants.SERVER_PROCESS_NAME)) {
            processType = ProcessType.Server;
        } else if (VActivityManager.get().isAppProcess(processName)) {
            processType = ProcessType.VAppClient;
        } else {
            processType = ProcessType.CHILD;
        }
        if (isVAppProcess()) {
            systemPid = VActivityManager.get().getSystemPid();
        }
    }

/*------------------------------------------remote method-----------------------------------------*/

    private IAppManager getService() {
        if (mService == null) {
            synchronized (this) {
                if (mService == null) {
                    Object remote = getStubInterface();
                    mService = LocalProxyUtils.genProxy(IAppManager.class, remote,
                            new LocalProxyUtils.DeadServerHandler() {
                        @Override
                        public Object getNewRemoteInterface() {
                            return getStubInterface();
                        }
                    });
                }
            }
        }
        return mService;
    }

    private Object getStubInterface() {
        return IAppManager.Stub
                .asInterface(ServiceManagerNative.getService(ServiceManagerNative.APP));
    }

    public InstallResult installPackage(String apkPath, int flags) {
        try {
            return getService().installPackage(apkPath, flags);
        } catch (RemoteException e) {
            return VirtualRuntime.crash(e);
        }
    }

    public boolean isAppInstalled(String pkg) {
        try {
            return getService().isAppInstalled(pkg);
        } catch (RemoteException e) {
            return VirtualRuntime.crash(e);
        }
    }

    public Intent getLaunchIntent(String packageName, int userId) {
        VPackageManager pm = VPackageManager.get();
        Intent intentToResolve = new Intent(Intent.ACTION_MAIN);
        intentToResolve.addCategory(Intent.CATEGORY_INFO);
        intentToResolve.setPackage(packageName);
        List<ResolveInfo> ris = pm.queryIntentActivities(intentToResolve,
                intentToResolve.resolveType(context), 0, userId);

        // Otherwise, try to find a main launcher activity.
        if (ris == null || ris.size() <= 0) {
            // reuse the intent instance
            intentToResolve.removeCategory(Intent.CATEGORY_INFO);
            intentToResolve.addCategory(Intent.CATEGORY_LAUNCHER);
            intentToResolve.setPackage(packageName);
            ris = pm.queryIntentActivities(intentToResolve, intentToResolve.resolveType(context), 0,
                    userId);
        }
        if (ris == null || ris.size() <= 0) {
            return null;
        }
        Intent intent = new Intent(intentToResolve);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName(ris.get(0).activityInfo.packageName, ris.get(0).activityInfo.name);
        return intent;
    }

    public InstalledAppInfo getInstalledAppInfo(String pkg, int flags) {
        try {
            return getService().getInstalledAppInfo(pkg, flags);
        } catch (RemoteException e) {
            return VirtualRuntime.crash(e);
        }
    }

    public int getInstalledAppCount() {
        try {
            return getService().getInstalledAppCount();
        } catch (RemoteException e) {
            return VirtualRuntime.crash(e);
        }
    }

    public boolean isStartup() {
        return isStartUp;
    }

    public boolean uninstallPackage(String pkgName, int userId) {
        try {
            return getService().uninstallPackage(pkgName, userId);
        } catch (RemoteException e) {
            // Ignore
        }
        return false;
    }

    public Resources getResources(String pkg) {
        InstalledAppInfo installedAppInfo = getInstalledAppInfo(pkg, 0);
        if (installedAppInfo != null) {
            AssetManager assets = mirror.android.content.res.AssetManager.ctor.newInstance();
            mirror.android.content.res.AssetManager.addAssetPath.call(assets,
                    installedAppInfo.apkPath);
            Resources hostRes = context.getResources();
            return new Resources(assets, hostRes.getDisplayMetrics(), hostRes.getConfiguration());
        }
        return null;
    }

    public synchronized ActivityInfo resolveActivityInfo(Intent intent, int userId) {
        ActivityInfo activityInfo = null;
        if (intent.getComponent() == null) {
            ResolveInfo resolveInfo = VPackageManager.get().resolveIntent(intent, intent.getType(),
                    0, userId);
            if (resolveInfo != null && resolveInfo.activityInfo != null) {
                activityInfo = resolveInfo.activityInfo;
                intent.setClassName(activityInfo.packageName, activityInfo.name);
            }
        } else {
            activityInfo = resolveActivityInfo(intent.getComponent(), userId);
        }
        if (activityInfo != null) {
            if (activityInfo.targetActivity != null) {
                ComponentName componentName = new ComponentName(activityInfo.packageName,
                        activityInfo.targetActivity);
                activityInfo = VPackageManager.get().getActivityInfo(componentName, 0, userId);
                intent.setComponent(componentName);
            }
        }
        return activityInfo;
    }

    private ActivityInfo resolveActivityInfo(ComponentName componentName, int userId) {
        return VPackageManager.get().getActivityInfo(componentName, 0, userId);
    }

    public ServiceInfo resolveServiceInfo(Intent intent, int userId) {
        ServiceInfo serviceInfo = null;
        ResolveInfo resolveInfo = VPackageManager.get().resolveService(intent, intent.getType(), 0,
                userId);
        if (resolveInfo != null) {
            serviceInfo = resolveInfo.serviceInfo;
        }
        return serviceInfo;
    }

    public List<InstalledAppInfo> getInstalledApps(int flags) {
        try {
            return getService().getInstalledApps(flags);
        } catch (RemoteException e) {
            return VirtualRuntime.crash(e);
        }
    }

    public boolean isPackageLaunched(int userId, String packageName) {
        try {
            return getService().isPackageLaunched(userId, packageName);
        } catch (RemoteException e) {
            return VirtualRuntime.crash(e);
        }
    }

    public boolean installPackageAsUser(int userId, String packageName) {
        try {
            return getService().installPackageAsUser(userId, packageName);
        } catch (RemoteException e) {
            return VirtualRuntime.crash(e);
        }
    }

    public int[] getPackageInstalledUsers(String packageName) {
        try {
            return getService().getPackageInstalledUsers(packageName);
        } catch (RemoteException e) {
            return VirtualRuntime.crash(e);
        }
    }

    public boolean isOutsideInstalled(String packageName) {
        try {
            return unHookPackageManager.getApplicationInfo(packageName, 0) != null;
        } catch (PackageManager.NameNotFoundException e) {
            // Ignore
        }
        return false;
    }

/*------------------------------------------getter and checker------------------------------------*/

    public static PackageManager getPM() {
        return get().getPackageManager();
    }

    public static Object mainThread() {
        return get().mainThread;
    }

    public String getProcessName() {
        return processName;
    }

    public ConditionVariable getInitLock() {
        return initLock;
    }

    public int getSystemPid() {
        return systemPid;
    }

    public int myUid() {
        return myUid;
    }

    public int myUserId() {
        return VUserHandle.getUserId(myUid);
    }

    public int[] getGids() {
        return hostPkgInfo.gids;
    }

    public Context getContext() {
        return context;
    }

    public PackageManager getPackageManager() {
        return context.getPackageManager();
    }

    public String getHostPkg() {
        return hostPkgName;
    }

    public PackageManager getUnHookPackageManager() {
        return unHookPackageManager;
    }

    public boolean isVAppProcess() {
        return ProcessType.VAppClient == processType;
    }

    public boolean isMainProcess() {
        return ProcessType.Main == processType;
    }

    public boolean isChildProcess() {
        return ProcessType.CHILD == processType;
    }

    public boolean isServerProcess() {
        return ProcessType.Server == processType;
    }

/*------------------------------------------delegate----------------------------------------------*/

    public ComponentDelegate getComponentDelegate() {
        return componentDelegate == null ? ComponentDelegate.EMPTY : componentDelegate;
    }

    public void setComponentDelegate(ComponentDelegate delegate) {
        this.componentDelegate = delegate;
    }

    public PhoneInfoDelegate getPhoneInfoDelegate() {
        return phoneInfoDelegate;
    }

    public void setPhoneInfoDelegate(PhoneInfoDelegate phoneInfoDelegate) {
        this.phoneInfoDelegate = phoneInfoDelegate;
    }

}
