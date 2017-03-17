package io.bean.virtual.client.hook.patchs.media.router;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import io.bean.virtual.client.hook.base.Patch;
import io.bean.virtual.client.hook.base.PatchBinderDelegate;

import mirror.android.media.IMediaRouterService;

/**
 * @author Lody
 *
 * @see android.media.MediaRouter
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@Patch({RegisterClientAsUser.class})
public class MediaRouterServicePatch extends PatchBinderDelegate {

	public MediaRouterServicePatch() {
		super(IMediaRouterService.Stub.TYPE, Context.MEDIA_ROUTER_SERVICE);
	}
}
