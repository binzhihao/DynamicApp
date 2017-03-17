package io.bean.virtual.client.ipc;

import android.app.job.JobInfo;
import android.os.IBinder;
import android.os.RemoteException;

import io.bean.virtual.client.env.VirtualRuntime;
import io.bean.virtual.server.IJobScheduler;
import io.bean.virtual.server.IPackageManager;

import java.util.List;

/**
 * @author Lody
 */

public class VJobScheduler {

    private static final VJobScheduler sInstance = new VJobScheduler();

    private IJobScheduler mRemote;

    public static VJobScheduler get() {
        return sInstance;
    }

    public IJobScheduler getRemote() {
        if (mRemote == null) {
            synchronized (this) {
                if (mRemote == null) {
                    Object remote = getRemoteInterface();
                    mRemote = LocalProxyUtils.genProxy(IJobScheduler.class, remote, new LocalProxyUtils.DeadServerHandler() {
                        @Override
                        public Object getNewRemoteInterface() {
                            return getRemoteInterface();
                        }
                    });
                }
            }
        }
        return mRemote;
    }

    private Object getRemoteInterface() {
        final IBinder binder = ServiceManagerNative.getService(ServiceManagerNative.JOB);
        return IJobScheduler.Stub.asInterface(binder);
    }

    public int schedule(JobInfo job) {
        try {
            return getRemote().schedule(job);
        } catch (RemoteException e) {
            return VirtualRuntime.crash(e);
        }
    }

    public List<JobInfo> getAllPendingJobs() {
        try {
            return getRemote().getAllPendingJobs();
        } catch (RemoteException e) {
            return VirtualRuntime.crash(e);
        }
    }

    public void cancelAll() {
        try {
            getRemote().cancelAll();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void cancel(int jobId) {
        try {
            getRemote().cancel(jobId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
