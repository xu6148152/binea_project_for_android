package demo.binea.com.mediacodecdemo;

import android.app.Application;
import android.content.Context;

/**
 * Created by xubinggui on 7/12/15.
 */
public class AppContext extends Application{

    private static Context mContext;
    @Override public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }
}
