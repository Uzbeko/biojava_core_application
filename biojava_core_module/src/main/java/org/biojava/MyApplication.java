package org.biojava;

import android.app.Application;
import android.content.Context;

/**
 * Created by edvinas on 17.2.4.
 */

public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

}
