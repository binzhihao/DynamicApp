package com.lody.virtual.client.hook.patchs.alerm;

import android.content.Context;
import android.os.Build;
import android.os.WorkSource;

import com.lody.virtual.client.hook.base.Hook;
import com.lody.virtual.client.hook.base.PatchDelegate;
import com.lody.virtual.client.hook.binders.AlarmBinderDelegate;
import com.lody.virtual.helper.utils.ArrayUtils;

import java.lang.reflect.Method;

import mirror.android.os.ServiceManager;

/**
 * @author Lody
 *
 */
public class AlarmManagerPatch extends PatchDelegate<AlarmBinderDelegate> {

	@Override
	protected AlarmBinderDelegate createHookDelegate() {
		return new AlarmBinderDelegate();
	}

	@Override
	public void inject() throws Throwable {
		getHookDelegate().replaceService(Context.ALARM_SERVICE);
	}

	@Override
	protected void onBindHooks() {
		super.onBindHooks();
		addHook(new Set());
		addHook(new SetTime());
		addHook(new SetTimeZone());
	}

	@Override
	public boolean isEnvBad() {
		return getHookDelegate() != ServiceManager.getService.call(Context.ALARM_SERVICE);
	}

	private static class SetTimeZone extends Hook {
		@Override
		public String getName() {
			return "setTimeZone";
		}

		@Override
		public Object onHook(Object who, Method method, Object... args) throws Throwable {
			return null;
		}
	}

	private static class SetTime extends Hook {
		@Override
		public String getName() {
			return "setTime";
		}

		@Override
		public Object onHook(Object who, Method method, Object... args) throws Throwable {
			if (Build.VERSION.SDK_INT >= 21) {
				return false;
			}
			return null;
		}
	}

	private static class Set extends Hook {

        @Override
        public String getName() {
            return "set";
        }

        @Override
        public boolean beforeHook(Object who, Method method, Object... args) {
            int index = ArrayUtils.indexOfFirst(args, WorkSource.class);
            if (index >= 0) {
                args[index] = null;
            }
            return true;
        }
    }
}
