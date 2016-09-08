package com.lody.virtual.client.hook.binders;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;

import com.lody.virtual.client.hook.base.HookBinderDelegate;

import mirror.android.location.ILocationManager;
import mirror.android.os.ServiceManager;

/**
 * @author Lody
 *
 */
public class LocationBinderDelegate extends HookBinderDelegate {

	@Override
	protected IInterface createInterface() {
		IBinder binder = ServiceManager.getService.call(Context.LOCATION_SERVICE);
		return ILocationManager.Stub.asInterface.call(binder);
	}
}
