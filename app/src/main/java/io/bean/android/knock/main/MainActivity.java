package io.bean.android.knock.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.lody.virtual.client.core.InstallStrategy;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.remote.InstallResult;
import com.lody.virtual.remote.InstalledAppInfo;

import java.lang.ref.WeakReference;

import io.bean.android.knock.R;
import io.bean.android.knock.common.Constants;
import io.bean.android.knock.util.AssetsUtil;

public class MainActivity extends Activity {

    private static final int MSG_START = 1;
    private static final int MSG_EXIT = -1;

    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new MyHandler(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg;
                if (AssetsUtil.copyFromAsset(getApplicationContext(), Constants.PATH)) {
                    PackageInfo pkgInfo = getPackageManager()
                            .getPackageArchiveInfo(Constants.PATH, 0);
                    if (pkgInfo != null && pkgInfo.packageName != null) {
                        // try to install or update, according to version code
                        // if there is an exist one and no need to update, just return success
                        InstallResult res = VirtualCore.get().installPackage(Constants.PATH,
                                InstallStrategy.COMPARE_VERSION);
                        if (res.isSuccess || res.isUpdate) {
                            msg = handler.obtainMessage(MSG_START, pkgInfo.packageName);
                        } else {
                            msg = handler.obtainMessage(MSG_EXIT);
                        }
                    } else {
                        msg = handler.obtainMessage(MSG_EXIT);
                    }
                } else {
                    msg = handler.obtainMessage(MSG_EXIT);
                }
                handler.sendMessageDelayed(msg, Constants.DELAY);
            }
        }).start();
    }

    private void startApp(String packageName) {
        Intent intent = VirtualCore.get().getLaunchIntent(packageName, 0);
        VActivityManager.get().startActivity(intent, 0);
        finish();
    }

    private void exitApp() {
        Toast.makeText(this, "Unable to open app", Toast.LENGTH_LONG).show();
        finish();
        System.exit(0);
    }

    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        MyHandler(MainActivity activity) {
            super(Looper.getMainLooper());
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case MSG_START:
                        activity.startApp((String) msg.obj);
                        break;
                    case MSG_EXIT:
                        activity.exitApp();
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
