package io.bean.virtual.client.hook.patchs.mount;

import io.bean.virtual.client.hook.base.Patch;
import io.bean.virtual.client.hook.base.PatchBinderDelegate;

import mirror.android.os.mount.IMountService;

/**
 * @author Lody
 */
@Patch({GetVolumeList.class, Mkdirs.class,})
public class MountServicePatch extends PatchBinderDelegate {

	public MountServicePatch() {
		super(IMountService.Stub.TYPE, "mount");
	}
}
