package com.lody.virtual.helper.compat;

import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import mirror.android.app.ActivityManagerNative;
import mirror.android.app.IActivityManagerICS;
import mirror.android.app.IActivityManagerL;

/**
 * @author Lody
 */

public class ActivityManagerCompat {
	/**
	 * Result for IActivityManaqer.startActivity: activity wasn't really started, but
	 * a task was simply brought to the foreground.
	 */
	public static final int START_TASK_TO_FRONT = 2;

	/**
	 * Type for IActivityManaqer.getIntentSender: this PendingIntent is
	 * for a sendBroadcast operation.
	 */
	public static final int INTENT_SENDER_BROADCAST = 1;

	/**
	 * Type for IActivityManaqer.getIntentSender: this PendingIntent is
	 * for a startActivity operation.
	 */
	public static final int INTENT_SENDER_ACTIVITY = 2;

	/**
	 * Type for IActivityManaqer.getIntentSender: this PendingIntent is
	 * for an activity result operation.
	 */
	public static final int INTENT_SENDER_ACTIVITY_RESULT = 3;

	/**
	 * Type for IActivityManaqer.getIntentSender: this PendingIntent is
	 * for a startService operation.
	 */
	public static final int INTENT_SENDER_SERVICE = 4;

	/** User operation call: success! */
	public static final int USER_OP_SUCCESS = 0;

	public static boolean finishActivity(IBinder token, int code, Intent data) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			return IActivityManagerL.finishActivity.call(
						ActivityManagerNative.getDefault.call(),
						token, code, data, false);
		} else {
			IActivityManagerICS.finishActivity.call(
					ActivityManagerNative.getDefault.call(),
					token, code, data
			);
		}

		return false;
	}
}
