package io.bean.android.knock;

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

import java.lang.ref.WeakReference;

public class MainActivity extends Activity {

    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new MyHandler(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                SharedPreferences sp = getSharedPreferences(Constants.STORAGE, MODE_PRIVATE);
                String pkgName;
                if ((pkgName = sp.getString(Constants.KEY, null)) == null) {
                    if (Utils.copyFromAsset(getApplicationContext(), Constants.PATH)) {
                        PackageInfo pkgInfo = getPackageManager()
                                .getPackageArchiveInfo(Constants.PATH, 0);
                        if (pkgInfo != null && pkgInfo.packageName != null) {
                            sp.edit().putString(Constants.KEY, pkgInfo.packageName).apply();
                            //install
                            VirtualCore.get().installApp(Constants.PATH, InstallStrategy.TERMINATE_IF_EXIST);
                            Message msg = handler.obtainMessage(1, pkgInfo.packageName);
                            long delay = Constants.DELAY - System.currentTimeMillis() + time;
                            if (delay < 0) {
                                delay = 0;
                            }
                            handler.sendMessageDelayed(msg, delay);
                            return;
                        }
                    }
                    handler.sendEmptyMessage(-1);
                } else {
                    Message msg = handler.obtainMessage(1, pkgName);
                    long time2 = System.currentTimeMillis();
                    long delay = Constants.DELAY - time2 + time;
                    if (delay < 0) {
                        delay = 0;
                    }
                    handler.sendMessageDelayed(msg, delay);
                }
            }
        }).start();
    }

    private void startApp(String packageName) {
        Intent intent = VirtualCore.get().getLaunchIntent(packageName, 0);
        startActivity(intent);  //this method is hook
        finish();
    }

    private void exitApp() {
        Toast.makeText(this, "Unable to open app", Toast.LENGTH_LONG).show();
        finish();
        System.exit(0);
    }

    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public MyHandler(MainActivity activity) {
            super(Looper.getMainLooper());  //run in main thread
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 1:
                        activity.startApp((String) msg.obj);
                        break;
                    case -1:
                        activity.exitApp();
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
