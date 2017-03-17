package io.bean.virtual.server;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import io.bean.virtual.client.core.VirtualCore;
import io.bean.virtual.client.ipc.ServiceManagerNative;
import io.bean.virtual.client.stub.DaemonService;
import io.bean.virtual.helper.compat.BundleCompat;
import io.bean.virtual.server.accounts.VAccountManagerService;
import io.bean.virtual.server.am.BroadcastSystem;
import io.bean.virtual.server.am.VActivityManagerService;
import io.bean.virtual.server.interfaces.IServiceFetcher;
import io.bean.virtual.server.job.VJobSchedulerService;
import io.bean.virtual.server.notification.VNotificationManagerService;
import io.bean.virtual.server.pm.VAppManagerService;
import io.bean.virtual.server.pm.VPackageManagerService;
import io.bean.virtual.server.pm.VUserManagerService;

/**
 * @author Lody
 */
public final class BinderProvider extends ContentProvider {

    private final ServiceFetcher mServiceFetcher = new ServiceFetcher();

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DaemonService.startup(context);
        if (!VirtualCore.get().isStartup()) {
            return true;
        }
        VPackageManagerService.systemReady();
        addService(ServiceManagerNative.PACKAGE, VPackageManagerService.get());
        VActivityManagerService.systemReady(context);
        addService(ServiceManagerNative.ACTIVITY, VActivityManagerService.get());
        addService(ServiceManagerNative.USER, VUserManagerService.get());
        VAppManagerService.systemReady();
        addService(ServiceManagerNative.APP, VAppManagerService.get());
        BroadcastSystem.attach(VActivityManagerService.get(), VAppManagerService.get());
        VAccountManagerService.systemReady();
        addService(ServiceManagerNative.ACCOUNT, VAccountManagerService.get());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            addService(ServiceManagerNative.JOB, VJobSchedulerService.get());
        }
        VNotificationManagerService.systemReady(context);
        addService(ServiceManagerNative.NOTIFICATION, VNotificationManagerService.get());
        VAppManagerService.get().scanApps();
        return true;
    }


    private void addService(String name, IBinder service) {
        ServiceCache.addService(name, service);
    }

    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        Bundle bundle = new Bundle();
        BundleCompat.putBinder(bundle, "_VA_|_binder_", mServiceFetcher);
        return bundle;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private class ServiceFetcher extends IServiceFetcher.Stub {
        @Override
        public IBinder getService(String name) throws RemoteException {
            if (name != null) {
                return ServiceCache.getService(name);
            }
            return null;
        }

        @Override
        public void addService(String name, IBinder service) throws RemoteException {
            if (name != null && service != null) {
                ServiceCache.addService(name, service);
            }
        }

        @Override
        public void removeService(String name) throws RemoteException {
            if (name != null) {
                ServiceCache.removeService(name);
            }
        }
    }
}
