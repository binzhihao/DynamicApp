package io.bean.android.knock.util;

import android.util.Log;

import io.bean.android.knock.common.Constants;

/**
 * @author Jerrybean on 2017/3/17.
 */

public class LogUtil {

    public static void d(String tag, String msg) {
        if (!Constants.DEBUG) return;
        Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (!Constants.DEBUG) return;
        Log.e(tag, msg);
    }

}
