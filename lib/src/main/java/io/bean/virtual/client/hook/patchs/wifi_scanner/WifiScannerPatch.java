package io.bean.virtual.client.hook.patchs.wifi_scanner;

import io.bean.virtual.client.hook.base.PatchBinderDelegate;

/**
 * @author Lody
 */

public class WifiScannerPatch extends PatchBinderDelegate {

    public WifiScannerPatch() {
        super(new GhostWifiScannerImpl(), "wifiscanner");
    }

}
