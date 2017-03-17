package io.bean.android.knock.util;

import android.content.Context;

import com.lody.virtual.helper.utils.MD5Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.bean.android.knock.common.Constants;

public class AssetsUtil {

    public static boolean copyFromAsset(Context context, String path) {
        File file = new File(path);
        if (file.isFile() && file.exists()) {
            try {
                if (Constants.MD5.equalsIgnoreCase(MD5Utils.getFileMD5String(file))) {
                    return true;
                } else {
                    file.deleteOnExit();
                    return false;
                }
            } catch (IOException e) {
                return false;
            }
            //return true;
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
                return false;
            }
        }
    }

}
