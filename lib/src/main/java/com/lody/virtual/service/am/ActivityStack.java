package com.lody.virtual.service.am;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.compat.ActivityManagerCompat;
import com.lody.virtual.helper.utils.ComponentUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import mirror.android.app.IApplicationThread;
import mirror.com.android.internal.content.ReferrerIntent;

/**
 * @author Lody
 */

/* package */ class ActivityStack {

	final LinkedList<ActivityTaskRecord> mTasks = new LinkedList<ActivityTaskRecord>();
	final ActivityManager mAM;
	private final VActivityManagerService mService;

	public ActivityStack(VActivityManagerService mService) {
		this.mService = mService;
		mAM = (ActivityManager) VirtualCore.get().getContext().getSystemService(Context.ACTIVITY_SERVICE);
	}

	@SuppressWarnings("deprecation")
	public Intent startActivityLocked(int userId, Intent intent, ActivityInfo info, IBinder resultTo, boolean fromHost, Bundle options) {
		Intent newIntent = new Intent();
		newIntent.setType(intent.getComponent().flattenToString());
		String taskAffinity = ComponentUtils.getTaskAffinity(info, userId);
		ActivityRecord sourceRecord = findRecord(resultTo);
		if (fromHost && findTask(taskAffinity) == null) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
				newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			else
				newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
			newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			newIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		}
		int launchFlags = intent.getFlags();
		if ((launchFlags & Intent.FLAG_ACTIVITY_CLEAR_TASK) != 0) {
            ActivityTaskRecord task = findTask(taskAffinity);
            if (task != null) {
                for (ActivityRecord r : task.activityList) {
                    ActivityManagerCompat.finishActivity(r.token, -1, null);
                }
            }
        }
		if (info.launchMode == ActivityInfo.LAUNCH_SINGLE_INSTANCE) {
            ActivityTaskRecord inTask = findTask(taskAffinity);
            if (inTask != null) {
                mAM.moveTaskToFront(inTask.taskId, 0);
                ActivityRecord r = inTask.topActivity();
                ProcessRecord processRecord = mService.findProcess(r.pid);
                // Only one Activity in the SingleInstance task
                scheduleNewIntent(intent, info, r.token, processRecord);
                return null;
            } else {
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            }
        }

		if (info.launchMode == ActivityInfo.LAUNCH_SINGLE_TOP) {
            ActivityTaskRecord topTask = mService.getTopTask();
            if (topTask != null && topTask.isOnTop(info)) {
                ActivityRecord r = topTask.topActivity();
                ProcessRecord processRecord = mService.findProcess(r.pid);
                // The top Activity is the target Activity
                scheduleNewIntent(intent, info, r.token, processRecord);
                return null;
            }
        }

		if (info.launchMode == ActivityInfo.LAUNCH_SINGLE_TASK) {
            newIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            ActivityTaskRecord topTask = mService.getTopTask();
            if (topTask != null && topTask.isInTask(info)) {
                int size = topTask.size();
                ActivityRecord top = null;
                ListIterator<ActivityRecord> iterator = topTask.activityList.listIterator(size);
                while (iterator.hasPrevious()) {
                    top = iterator.previous();
                    if (ComponentUtils.isSameComponent(top.activityInfo, info)) {
                        break;
                    }
                    ActivityManagerCompat.finishActivity(top.token, -1, null);
                }
                if (top != null) {
                    ProcessRecord processRecord = mService.findProcess(top.pid);
                    // The top Activity is the target Activity
                    scheduleNewIntent(intent, info, top.token, processRecord);
                    return null;
                }
            }
        }
		if (sourceRecord != null && sourceRecord.caller != null) {
            if (sourceRecord.activityInfo.launchMode == ActivityInfo.LAUNCH_SINGLE_INSTANCE) {
                String comebackTaskAffinity = ComponentUtils.getTaskAffinity(sourceRecord.caller, userId);
                ActivityTaskRecord comebackTask = findTask(comebackTaskAffinity);
                if (comebackTask != null) {
                    mAM.moveTaskToFront(comebackTask.taskId, 0);
                }
            }
        }
		if ((launchFlags & Intent.FLAG_ACTIVITY_NO_USER_ACTION) != 0) {
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        }
		if ((launchFlags & Intent.FLAG_ACTIVITY_CLEAR_TOP) != 0) {
            ActivityTaskRecord task = findTask(taskAffinity);
            if (task != null && task.isInTask(info)) {
                if (task.isOnTop(info)) {
                    ActivityManagerCompat.finishActivity(task.topActivityToken(), -1, null);
                    return null;
                }
                List<ActivityRecord> activityList = task.activityList;
                ListIterator<ActivityRecord> iterator = activityList.listIterator();
                while (iterator.hasNext()) {
                    ActivityRecord current = iterator.next();
                    if (ComponentUtils.isSameComponent(current.activityInfo, info)) {
                        while (iterator.hasNext()) {
                            ActivityRecord afterCurrent = iterator.next();
                            ActivityManagerCompat.finishActivity(afterCurrent.token, -1, null);
                        }
                        ProcessRecord processRecord = mService.findProcess(current.pid);
                        scheduleNewIntent(intent, info, current.token, processRecord);
                        return null;
                    }
                }
            }
        }
		ProcessRecord processRecord = mService.startProcessIfNeedLocked(info.processName, userId, info.packageName);
		if (processRecord == null) {
			return null;
		}
		StubInfo selectedStub = processRecord.stubInfo;
		ActivityInfo stubActInfo = selectedStub.fetchStubActivityInfo(info);
		if (stubActInfo == null) {
			return null;
		}
		newIntent.setClassName(stubActInfo.packageName, stubActInfo.name);
		newIntent.putExtra("_VA_|_intent_", intent);
		newIntent.putExtra("_VA_|_stub_activity_", stubActInfo);
		newIntent.putExtra("_VA_|_target_activity_", info);
		newIntent.putExtra("_VA_|_user_id_", userId);
		return newIntent;
	}

	private void scheduleNewIntent(Intent intent, ActivityInfo info, IBinder token, ProcessRecord record) {
		List<Intent> intents = new ArrayList<>(1);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
			Intent referrerIntent = ReferrerIntent.ctor.newInstance(intent, info.packageName);
			intents.add(referrerIntent);
		} else {
			intents.add(intent);
		}
		IApplicationThread.scheduleNewIntent.call(record.thread, intents, token);
	}

	public ActivityTaskRecord findTask(String affinity) {
		synchronized (mTasks) {
			for (ActivityTaskRecord task : mTasks) {
				if (affinity.equals(task.rootAffinity)) {
					return task;
				}
			}
		}
		return null;
	}
	public ActivityTaskRecord findTask(IBinder activityToken) {
		synchronized (mTasks) {
			for (ActivityTaskRecord task : mTasks) {
				ActivityRecord r = task.activities.get(activityToken);
				if (r != null) {
					return task;
				}
			}
		}
		return null;
	}

	public ActivityRecord findRecord(IBinder activityToken) {
		synchronized (mTasks) {
			for (ActivityTaskRecord task : mTasks) {
				ActivityRecord r = task.activities.get(activityToken);
				if (r != null) {
					return r;
				}
			}
		}
		return null;
	}

	public ActivityTaskRecord findTask(int taskId) {
		synchronized (mTasks) {
			for (ActivityTaskRecord task : mTasks) {
				if (task.taskId == taskId) {
					return task;
				}
			}
		}
		return null;
	}

	public synchronized void trimTasks() {
		ListIterator<ActivityTaskRecord> iterator = mTasks.listIterator();
		while (iterator.hasNext()) {
			ActivityTaskRecord task = iterator.next();
			if (task.activities.isEmpty()) {
				iterator.remove();
			}
		}
	}
}
