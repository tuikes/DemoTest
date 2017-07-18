package com.tuikes.demotest.app;

import android.app.Application;


/**
 * 应用Application
 * Created by chendx on 2017/5/18.
 */

public class IApplication extends Application {
    /**
     * 应用上下文
     */
    private static IApplication gInstance;

    /**
     * 获取应用上下文
     *
     * @return 应用上下文
     */
    public static IApplication getInstance() {
        return gInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (gInstance != null) {
            gInstance = this;
            return;
        }
        gInstance = this;
        initMainService();
    }

    private void initMainService() {
    }

}
