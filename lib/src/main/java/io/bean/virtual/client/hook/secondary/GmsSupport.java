package io.bean.virtual.client.hook.secondary;

import io.bean.virtual.client.core.VirtualCore;

/**
 * @author Lody
 */
public class GmsSupport {

    public static boolean isGmsFamilyPackage(String packageName) {
        return packageName.equals("com.android.vending")
                || packageName.equals("com.google.android.gms");
    }

    public static boolean isGmsFrameworkInstalled() {
        return VirtualCore.get().isAppInstalled("com.google.android.gms");
    }

}
