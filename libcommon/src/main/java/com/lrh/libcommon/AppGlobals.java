package com.lrh.libcommon;

import android.app.Application;
import android.content.Context;

/**
 * Created by LRH on 2020/10/28 0028
 */
public class AppGlobals extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
    }

    public static Context getApplication() {
        return context;
    }
}
