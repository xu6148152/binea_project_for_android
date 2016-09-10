package com.zepp.www.binderdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

//  Created by xubinggui on 9/9/16.
//                            _ooOoo_  
//                           o8888888o  
//                           88" . "88  
//                           (| -_- |)  
//                            O\ = /O  
//                        ____/`---'\____  
//                      .   ' \\| |// `.  
//                       / \\||| : |||// \  
//                     / _||||| -:- |||||- \  
//                       | | \\\ - /// | |  
//                     | \_| ''\---/'' | |  
//                      \ .-\__ `-` ___/-. /  
//                   ___`. .' /--.--\ `. . __  
//                ."" '< `.___\_<|>_/___.' >'"".  
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |  
//                 \ \ `-. \_ __\ /__ _/ .-` / /  
//         ======`-.____`-.___\_____/___.-`____.-'======  
//                            `=---='  
//  
//         .............................................  
//                  佛祖镇楼                  BUG辟易 
public class MessengerService extends Service {

    private static final String TAG = MessengerService.class.getCanonicalName();

    private static class MessengerHandler extends Handler {
        @Override public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MSG_FROM_CLIENT:
                    Log.i(TAG, "receive msg from client:" + msg.getData().getString("msg"));
                    Messenger replyMessenger = msg.replyTo;
                    Message replyMsg = Message.obtain(null, Constants.MSG_FROM_SERVICE);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.REPLY_NAME, "ok, reply you later");
                    replyMsg.setData(bundle);
                    try {
                        replyMessenger.send(replyMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private final Messenger mMessenger = new Messenger(new MessengerHandler());

    @Nullable @Override public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
        //return new LocalBinder();
    }

    public class LocalBinder extends Binder {
        MessengerService getService() {
            return MessengerService.this;
        }
    }
}
