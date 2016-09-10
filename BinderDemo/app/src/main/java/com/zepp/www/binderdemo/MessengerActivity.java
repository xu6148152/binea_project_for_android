package com.zepp.www.binderdemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
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
public class MessengerActivity extends Activity {
    private static final String TAG = MessengerActivity.class.getCanonicalName();

    private Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override public void onServiceConnected(ComponentName name, IBinder service) {
            Messenger remoteMessenger = new Messenger(service);
            Message msg = Message.obtain(null, Constants.MSG_FROM_CLIENT);
            Bundle data = new Bundle();
            data.putString(Constants.MSG_NAME, "hello, this is  client");
            msg.setData(data);
            msg.replyTo = mGetReplyMessenger;
            try {
                remoteMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            //MessengerService.LocalBinder localBinder = (MessengerService.LocalBinder)service;
            //final MessengerService messengerService = localBinder.getService();
        }

        @Override public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }

    private static class MessengerHandler extends Handler {
        @Override public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MSG_FROM_SERVICE:
                    Log.i(TAG, "receive msg from service:" + msg.getData().getString(Constants.REPLY_NAME));
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
