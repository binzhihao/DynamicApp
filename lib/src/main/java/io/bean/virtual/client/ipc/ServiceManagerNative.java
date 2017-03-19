package io.bean.virtual.client.ipc;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import io.bean.virtual.client.core.VirtualCore;
import io.bean.virtual.client.env.VirtualRuntime;
import io.bean.virtual.helper.compat.BundleCompat;
import io.bean.virtual.helper.utils.VLog;
import io.bean.virtual.server.ServiceCache;
import io.bean.virtual.server.interfaces.IServiceFetcher;

public class ServiceManagerNative {

	public static final String PACKAGE = "package";
	public static final String ACTIVITY = "activity";
	public static final String USER = "user";
	public static final String APP = "app";
	public static final String ACCOUNT = "account";
	public static final String JOB = "job";
	public static final String NOTIFICATION ="virtual_notification";
	public static final String SERVICE_DEF_AUTH = "virtual.service.BinderProvider";
	private static final String TAG = ServiceManagerNative.class.getSimpleName();
	public static String SERVICE_CP_AUTH = "virtual.service.BinderProvider";

	private static IServiceFetcher sFetcher;

	private static IServiceFetcher getServiceFetcher() {
		if (sFetcher == null) {
			synchronized (ServiceManagerNative.class) {
				if (sFetcher == null) {
					Context context = VirtualCore.get().getContext();
					Bundle response = new ProviderCall.Builder(context, SERVICE_CP_AUTH).methodName("@").call();
					if (response != null) {
						IBinder binder = BundleCompat.getBinder(response, "_VA_|_binder_");
						linkBinderDied(binder);
						sFetcher = IServiceFetcher.Stub.asInterface(binder);
					}
				}
			}
		}
		return sFetcher;
	}

	public static void clearServerFetcher() {
        sFetcher = null;
    }

	private static void linkBinderDied(final IBinder binder) {
		IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
			@Override
			public void binderDied() {
				binder.unlinkToDeath(this, 0);
			}
		};
		try {
			binder.linkToDeath(deathRecipient, 0);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public static IBinder getService(String name) {
		if (VirtualCore.get().isServerProcess()) {
			return ServiceCache.getService(name);
		}
		IServiceFetcher fetcher = getServiceFetcher();
		if (fetcher != null) {
			try {
				return fetcher.getService(name);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		VLog.e(TAG, "GetService(%s) return null.", name);
		return null;
	}

	public static void addService(String name, IBinder service) {
		IServiceFetcher fetcher = getServiceFetcher();
		if (fetcher != null) {
			try {
				fetcher.addService(name, service);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

	}

	public static void removeService(String name) {
		IServiceFetcher fetcher = getServiceFetcher();
		if (fetcher != null) {
			try {
				fetcher.removeService(name);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

}
