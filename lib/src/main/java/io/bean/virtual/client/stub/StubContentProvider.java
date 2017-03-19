package io.bean.virtual.client.stub;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.IBinder;
import android.os.Process;

import io.bean.virtual.client.VClientImpl;
import io.bean.virtual.client.core.VirtualCore;
import io.bean.virtual.helper.compat.BundleCompat;

public class StubContentProvider extends ContentProvider {

	@Override
	public boolean onCreate() {
		return true;
	}

	@Override
	public Bundle call(String method, String arg, Bundle extras) {
		if ("_VA_|_init_process_".equals(method)) {
			return initProcess(extras);
		}
		return null;
	}

	private Bundle initProcess(Bundle extras) {
		ConditionVariable lock = VirtualCore.get().getInitLock();
		if (lock != null) {
			lock.block();
		}
		IBinder token = BundleCompat.getBinder(extras,"_VA_|_binder_");
		int vuid = extras.getInt("_VA_|_vuid_");
		VClientImpl client = VClientImpl.get();
		client.initProcess(token, vuid);
		Bundle res = new Bundle();
		BundleCompat.putBinder(res, "_VA_|_client_", client.asBinder());
		res.putInt("_VA_|_pid_", Process.myPid());
		return res;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		return null;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
	}


	public static class S0 extends StubContentProvider {
	}

	public static class S1 extends StubContentProvider {
	}

	public static class S2 extends StubContentProvider {
	}

	public static class S3 extends StubContentProvider {
	}

	public static class S4 extends StubContentProvider {
	}

	public static class S5 extends StubContentProvider {
	}

	public static class S6 extends StubContentProvider {
	}

	public static class S7 extends StubContentProvider {
	}

	public static class S8 extends StubContentProvider {
	}

	public static class S9 extends StubContentProvider {
	}

	public static class S10 extends StubContentProvider {
	}

	public static class S11 extends StubContentProvider {
	}

	public static class S12 extends StubContentProvider {
	}

	public static class S13 extends StubContentProvider {
	}

	public static class S14 extends StubContentProvider {
	}

	public static class S15 extends StubContentProvider {
	}

	public static class S16 extends StubContentProvider {
	}

	public static class S17 extends StubContentProvider {
	}

	public static class S18 extends StubContentProvider {
	}

	public static class S19 extends StubContentProvider {
	}

	public static class S20 extends StubContentProvider {
	}

	public static class S21 extends StubContentProvider {
	}

	public static class S22 extends StubContentProvider {
	}

	public static class S23 extends StubContentProvider {
	}

	public static class S24 extends StubContentProvider {
	}

	public static class S25 extends StubContentProvider {
	}

	public static class S26 extends StubContentProvider {
	}

	public static class S27 extends StubContentProvider {
	}

	public static class S28 extends StubContentProvider {
	}

	public static class S29 extends StubContentProvider {
	}

}
