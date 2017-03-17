package io.bean.virtual.client.hook.patchs.telephony;

import io.bean.virtual.client.hook.base.PatchBinderDelegate;
import io.bean.virtual.client.hook.base.ReplaceCallingPkgHook;
import io.bean.virtual.client.hook.base.ReplaceSequencePkgHook;

import mirror.com.android.internal.telephony.ITelephonyRegistry;

public class TelephonyRegistryPatch extends PatchBinderDelegate {

	public TelephonyRegistryPatch() {
		super(ITelephonyRegistry.Stub.TYPE, "telephony.registry");
	}

	@Override
	protected void onBindHooks() {
		super.onBindHooks();

		addHook(new ReplaceCallingPkgHook("listen"));
		addHook(new ReplaceSequencePkgHook("listenForSubscriber", 1));
	}
}
