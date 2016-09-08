package io.bean.android.knock;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Utils {

    public static boolean copyFromAsset(Context context, String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        } else {
            try {
                InputStream is = context.getAssets().open(Constants.TEMP);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] temp = new byte[1024];
                int i;
                while ((i = is.read(temp)) > 0) {
                    fos.write(temp, 0, i);
                }
                fos.close();
                is.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public static void log(String tag, String msg) {
        if (!Constants.DEBUG) return;
        Log.e(tag, msg);
    }

}
