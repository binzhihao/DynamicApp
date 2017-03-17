package io.bean.virtual.client.hook.patchs.pm;

import android.content.pm.IPackageDeleteObserver2;

import io.bean.virtual.client.core.VirtualCore;
import io.bean.virtual.client.hook.base.Hook;
import io.bean.virtual.os.VUserHandle;

import java.lang.reflect.Method;


/**
 * @author Lody
 *
 */
/* package */ class DeletePackage extends Hook {

	@Override
	public String getName() {
		return "deletePackage";
	}

	@Override
	public Object call(Object who, Method method, Object... args) throws Throwable {
		String pkgName = (String) args[0];
		try {
            VirtualCore.get().uninstallPackage(pkgName, VUserHandle.myUserId());
            IPackageDeleteObserver2 observer = (IPackageDeleteObserver2) args[1];
            if (observer != null) {
                observer.onPackageDeleted(pkgName, 0, "done.");
            }
        } catch (Throwable e) {
            // Ignore
        }
		return 0;
	}

}
