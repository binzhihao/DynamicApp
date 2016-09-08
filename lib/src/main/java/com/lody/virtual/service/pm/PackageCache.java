package com.lody.virtual.service.pm;

import android.content.pm.PackageParser;

import com.lody.virtual.helper.proto.AppSetting;
import com.lody.virtual.helper.utils.collection.ArrayMap;

/**
 * @author Lody
 */

public class PackageCache {

	static final ArrayMap<String, PackageParser.Package> sPackageCaches = new ArrayMap<>();


	public static void put(PackageParser.Package pkg, AppSetting appSetting) {
		synchronized (PackageCache.class) {
			pkg.mExtras = appSetting;
			sPackageCaches.put(pkg.packageName, pkg);
			VPackageManagerService.get().analyzePackageLocked(pkg);
		}
	}

	public static PackageParser.Package get(String packageName) {
		return sPackageCaches.get(packageName);
	}

	public static void remove(String packageName) {
		synchronized (PackageCache.class) {
			sPackageCaches.remove(packageName);
			VPackageManagerService.get().deletePackageLocked(packageName);
		}
	}
}
