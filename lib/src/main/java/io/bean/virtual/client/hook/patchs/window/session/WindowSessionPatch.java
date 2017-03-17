package io.bean.virtual.client.hook.patchs.window.session;

import android.os.IInterface;

import io.bean.virtual.client.hook.base.HookDelegate;
import io.bean.virtual.client.hook.base.Patch;
import io.bean.virtual.client.hook.base.PatchDelegate;

/**
 * @author Lody
 */
@Patch({Add.class, AddToDisplay.class, AddToDisplayWithoutInputChannel.class,
		AddWithoutInputChannel.class, Relayout.class})
public class WindowSessionPatch extends PatchDelegate<HookDelegate<IInterface>> {

	public WindowSessionPatch(IInterface session) {
		super(new HookDelegate(session));
	}

	@Override
	public void onBindHooks() {
		super.onBindHooks();
	}

	@Override
	public void inject() throws Throwable {
		// <EMPTY>
	}

	@Override
	public boolean isEnvBad() {
		return getHookDelegate().getProxyInterface() != null;
	}
}
