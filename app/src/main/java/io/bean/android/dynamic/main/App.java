package io.bean.android.dynamic.main;

import android.app.Application;
import android.content.Context;

import io.bean.android.dynamic.common.Constants;
import io.bean.virtual.client.core.VirtualCore;

public class App extends Application {

    private static final String TAG = Constants.CONFIG + "_app";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            VirtualCore.get().startup(base);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

}
