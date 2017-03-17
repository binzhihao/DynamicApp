package io.bean.virtual.client.hook.base;

import android.os.Process;

import io.bean.virtual.helper.utils.ArrayUtils;

import java.lang.reflect.Method;

public class ReplaceLastUidHook extends StaticHook {

    public ReplaceLastUidHook(String name) {
        super(name);
    }

    @Override
    public boolean beforeCall(Object who, Method method, Object... args) {
        int index = ArrayUtils.indexOfLast(args, Integer.class);
        if (index != -1) {
            int uid = (int) args[index];
            if (uid == Process.myUid()) {
                args[index] = getRealUid();
            }
        }
        return super.beforeCall(who, method, args);
    }
}