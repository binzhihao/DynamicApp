package com.lody.virtual.client.hook.binders;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.os.IInterface;

import com.lody.virtual.client.hook.base.HookBinderDelegate;

import mirror.android.os.IUserManager;
import mirror.android.os.ServiceManager;

/**
 * @author Lody
 *
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class UserBinderDelegate extends HookBinderDelegate {

	@Override
	protected IInterface createInterface() {
		IBinder binder = ServiceManager.getService.call(Context.USER_SERVICE);
		return IUserManager.Stub.asInterface.call(binder);
	}
}
