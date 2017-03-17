package io.bean.virtual.client.hook.patchs.isub;

import io.bean.virtual.client.hook.base.PatchBinderDelegate;
import io.bean.virtual.client.hook.base.ReplaceCallingPkgHook;
import io.bean.virtual.client.hook.base.ReplaceLastPkgHook;

import mirror.com.android.internal.telephony.ISub;

/**
 * @author Lody
 */
public class ISubPatch extends PatchBinderDelegate {

    public ISubPatch() {
        super(ISub.Stub.TYPE, "isub");
    }

    @Override
    protected void onBindHooks() {
        super.onBindHooks();
        addHook(new ReplaceCallingPkgHook("getAllSubInfoList"));
        addHook(new ReplaceCallingPkgHook("getAllSubInfoCount"));
        addHook(new ReplaceLastPkgHook("getActiveSubscriptionInfo"));
        addHook(new ReplaceLastPkgHook("getActiveSubscriptionInfoForIccId"));
        addHook(new ReplaceLastPkgHook("getActiveSubscriptionInfoForSimSlotIndex"));
        addHook(new ReplaceLastPkgHook("getActiveSubscriptionInfoList"));
        addHook(new ReplaceLastPkgHook("getActiveSubInfoCount"));
        addHook(new ReplaceLastPkgHook("getSubscriptionProperty"));
    }
}
