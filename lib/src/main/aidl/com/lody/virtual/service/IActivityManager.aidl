// IActivityManager.aidl
package com.lody.virtual.service;

import com.lody.virtual.helper.proto.VParceledListSlice;
import com.lody.virtual.helper.proto.AppTaskInfo;
import com.lody.virtual.helper.proto.PendingIntentData;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.app.Notification;
import android.app.IServiceConnection;
import android.app.IActivityManager.ContentProviderHolder;
import com.lody.virtual.service.interfaces.IProcessObserver;


interface IActivityManager {

    int getFreeStubCount();

    int getSystemPid();

    int getUidByPid(int pid);

    void attachClient(in IBinder clinet);

    boolean isAppProcess(String processName);

    boolean isAppPid(int pid);

    String getAppProcessName(int pid);

    List<String> getProcessPkgList(int pid);

    void killAllApps();

    void killAppByPkg(String pkg, int userId);

    void killApplicationProcess(String procName, int uid);

    void dump();

    void registerProcessObserver(in IProcessObserver observer);

    void unregisterProcessObserver(in IProcessObserver observer);

    String getInitialPackage(int pid);

    void handleApplicationCrash();

    void appDoneExecuting();

    Intent startActivity(in Intent intent, in ActivityInfo info, in IBinder resultTo, in Bundle options, in boolean fromHost, int userId);

    void onActivityCreated(in IBinder token, in ActivityInfo info, in ActivityInfo caller, int taskId);

    void onActivityResumed(in IBinder token);

    void onActivityDestroyed(in IBinder token);

    ActivityInfo getCallingActivity(in IBinder token);

    AppTaskInfo getTaskInfo(int taskId);

    String getPackageForToken(in IBinder token);

    ActivityInfo getActivityInfo(in IBinder token);


    ComponentName startService(in IBinder caller,in Intent service, String resolvedType);

    int stopService(in IBinder caller, in Intent service, String resolvedType);

    boolean stopServiceToken(in ComponentName className, in IBinder token, int startId);

    void setServiceForeground(in ComponentName className, in IBinder token, int id,
                            in Notification notification, boolean keepNotification);

    int bindService(in IBinder caller, in IBinder token, in Intent service,
                    String resolvedType, in IServiceConnection connection, int flags);

    boolean unbindService(in IServiceConnection connection);

    void unbindFinished(in IBinder token, in Intent service, in boolean doRebind);

    void serviceDoneExecuting(in IBinder token, in int type, in int startId, in int res);

    IBinder peekService(in Intent service, String resolvedType);

    void publishService(in IBinder token, in Intent intent, in IBinder service);

    VParceledListSlice getServices(int maxNum, int flags);

    void ensureAppBound(in String processName, in String packageName, int userId);

    IBinder acquireProviderClient(int userId, in ProviderInfo info);

    PendingIntentData getPendingIntent(IBinder binder);

    void addPendingIntent(IBinder binder, String packageName);

    void removePendingIntent(IBinder binder);
}
