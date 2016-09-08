package com.lody.virtual.client.hook.binders;

import android.os.IBinder;
import android.os.IInterface;

import com.lody.virtual.client.hook.base.HookBinderDelegate;

import mirror.android.os.ServiceManager;
import mirror.com.android.internal.telephony.ISub;

/**
 * @author Lody
 */

public class ISubBinderDelegate extends HookBinderDelegate {

    @Override
    protected IInterface createInterface() {
        IBinder binder = ServiceManager.getService.call("isub");
        if (ISub.Stub.Class != null) {
            return ISub.Stub.asInterface.call(binder);
        }
        return null;
    }
}
