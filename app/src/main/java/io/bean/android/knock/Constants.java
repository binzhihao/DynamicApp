package io.bean.android.knock;

import android.os.Environment;

import java.io.File;

public class Constants {
    public static final boolean DEBUG = true;
    public static final String TEMP = "6b46cd72";
    public static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + TEMP;
    public static final String STORAGE = "storage";
    public static final String KEY = "key";
    public static final long DELAY = 5000;
}
