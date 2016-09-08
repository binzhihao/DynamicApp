package com.lody.virtual.client.hook.binders;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;

import com.lody.virtual.client.hook.base.HookBinderDelegate;

import mirror.android.media.IAudioService;
import mirror.android.os.ServiceManager;

/**
 * @author Lody
 */

public class AudioBinderDelegate extends HookBinderDelegate {

	@Override
	protected IInterface createInterface() {
		IBinder binder = ServiceManager.getService.call(Context.AUDIO_SERVICE);
		return IAudioService.Stub.asInterface.call(binder);
	}
}
