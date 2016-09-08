package com.lody.virtual.client.stub;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;

import com.lody.virtual.helper.compat.ActivityManagerCompat;
import com.lody.virtual.helper.component.BaseService;

/**
 * @author Lody
 *
 *
 *         与ServiceContentProvider同进程，用于维持进程不死。
 *
 */
public class KeepService extends BaseService {

	public static void startup(Context context) {
		context.startService(new Intent(context, KeepService.class));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		startup(this);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		try {
			Notification notification = new Notification();
			notification.flags |= Notification.FLAG_NO_CLEAR;
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			startForeground(0, notification);
		} catch (Throwable e) {
			// Ignore
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			handlePendingIntent(intent);
		}
		return START_STICKY;
	}

	private void handlePendingIntent(Intent intent) {
		int flags = intent.getIntExtra("_VA_|_type_", -1);
		Intent originIntent = intent.getParcelableExtra("_VA_|_intent_");
		if (originIntent == null) {
			return;
		}
		switch (flags) {
			case ActivityManagerCompat.INTENT_SENDER_ACTIVITY : {
				originIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				try {
					startActivity(originIntent);
				} catch (Throwable e) {
					e.printStackTrace();
				}
				break;
			}
			case ActivityManagerCompat.INTENT_SENDER_SERVICE : {
				try {
					startService(originIntent);
				} catch (Throwable e) {
					e.printStackTrace();
				}
				break;
			}
		}

	}

}
