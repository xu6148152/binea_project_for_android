package com.zepp.www.launchmodedemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by xubinggui on 5/31/16.
 * //                            _ooOoo_
 * //                           o8888888o
 * //                           88" . "88
 * //                           (| -_- |)
 * //                            O\ = /O
 * //                        ____/`---'\____
 * //                      .   ' \\| |// `.
 * //                       / \\||| : |||// \
 * //                     / _||||| -:- |||||- \
 * //                       | | \\\ - /// | |
 * //                     | \_| ''\---/'' | |
 * //                      \ .-\__ `-` ___/-. /
 * //                   ___`. .' /--.--\ `. . __
 * //                ."" '< `.___\_<|>_/___.' >'"".
 * //               | | : `- \`.;`\ _ /`;.`/ - ` : | |
 * //                 \ \ `-. \_ __\ /__ _/ .-` / /
 * //         ======`-.____`-.___\_____/___.-`____.-'======
 * //                            `=---='
 * //
 * //         .............................................
 * //                  佛祖镇楼                  BUG辟易
 */
public class NotificationReceiver extends BroadcastReceiver {

    @Override public void onReceive(Context context, Intent tmpIntent) {
        Log.i(Constant.TAG, "onReceive");
        int cId = 100;
        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent =
                PendingIntent.getActivity(context, cId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(
                context.getApplicationInfo().icon)
                                                                                     .setContentTitle(
                                                                                             "aa")
                                                                                     .setAutoCancel(
                                                                                             true)
                                                                                     .setContentIntent(
                                                                                             contentIntent)
                                                                                     //.setDefaults(Notification.DEFAULT_SOUND)
                                                                                     .setContentText(
                                                                                             "aa");
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = mBuilder.build();
        //if (sound != null && sound.trim().length() > 0) {
        //  notification.sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + sound);
        //}
        manager.notify(cId, notification);
    }
}
