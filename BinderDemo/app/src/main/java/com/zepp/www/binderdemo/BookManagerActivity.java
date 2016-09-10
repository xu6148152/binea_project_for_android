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
import android.os.RemoteException;
import android.util.Log;
import java.util.List;

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
public class BookManagerActivity extends Activity {
    private static final String TAG = BookManagerActivity.class.getCanonicalName();

    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;

    private IBookManager mRemoteBookManager;

    private static Handler mHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.i(TAG, "receive new book: " + msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager bookManager = IBookManager.Stub.asInterface(service);
            mRemoteBookManager = bookManager;
            try {
                Book book = new Book(3, "Android 开发艺术探索");
                bookManager.addBook(book);
                List<Book> bookList = bookManager.getBookList();
                Log.i(TAG, "query book list, list type:" + bookList.getClass().getCanonicalName());
                Log.i(TAG, "query book list:" + bookList.toString());
                bookManager.registerListener(onNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected");
        }
    };

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override protected void onDestroy() {
        if (mRemoteBookManager != null && mRemoteBookManager.asBinder().isBinderAlive()) {
            try {
                mRemoteBookManager.unregisterListener(onNewBookArrivedListener);
                Log.i(TAG, "listener count " + mRemoteBookManager.getListenerCount());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mConnection);
        super.onDestroy();
    }

    private IOnNewBookArrivedListener onNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {

        @Override public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString)
                throws RemoteException {

        }

        @Override public void onNewBookArrived(Book newBook) throws RemoteException {
            mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, newBook).sendToTarget();
        }
    };
}
