package io.bean.virtual.client.hook.patchs.graphics;

import io.bean.virtual.client.hook.base.PatchBinderDelegate;
import io.bean.virtual.client.hook.base.ReplaceCallingPkgHook;

import mirror.android.view.IGraphicsStats;


/**
 * @author Lody
 */
public class GraphicsStatsPatch extends PatchBinderDelegate {

	public GraphicsStatsPatch() {
		super(IGraphicsStats.Stub.TYPE, "graphicsstats");
	}

	@Override
	protected void onBindHooks() {
		super.onBindHooks();
		addHook(new ReplaceCallingPkgHook("requestBufferForProcess"));
	}
}
