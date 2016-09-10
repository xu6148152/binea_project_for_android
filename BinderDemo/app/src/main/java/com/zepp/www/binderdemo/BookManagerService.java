package com.zepp.www.binderdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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
public class BookManagerService extends Service {

    private static final String TAG = BookManagerService.class.getCanonicalName();

    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();

    private RemoteCallbackList<IOnNewBookArrivedListener> mListenerList = new RemoteCallbackList<>();

    private Binder mBinder = new IBookManager.Stub() {

        @Override public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString)
                throws RemoteException {

        }

        @Override public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mListenerList.register(listener);
        }

        @Override public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mListenerList.unregister(listener);
        }

        @Override public int getListenerCount() throws RemoteException {
            return mListenerList.getRegisteredCallbackCount();
        }
    };

    @Nullable @Override public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "Android"));
        mBookList.add(new Book(2, "ios"));
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override public void run() {
                if (mIsServiceDestoryed.get()) {
                    executorService.shutdown();
                }
                int bookId = mBookList.size() + 1;
                Book newBook = new Book(bookId, "new book#" + bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
        //try {
        //    final Object result = schedule.get();
        //    if (!result.equals("success")) {
        //        Log.i(TAG, "success");
        //    } else {
        //        Log.i(TAG, "fail");
        //    }
        //} catch (ExecutionException e) {
        //    e.printStackTrace();
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}
    }

    private void onNewBookArrived(Book book) throws RemoteException {
        mBookList.add(book);
        final int N = mListenerList.beginBroadcast();
        for (int i = 0; i < N; i++) {
            final IOnNewBookArrivedListener broadcastItem = mListenerList.getBroadcastItem(i);
            if (broadcastItem != null) {
                broadcastItem.onNewBookArrived(book);
            }
        }
        mListenerList.finishBroadcast();
    }

    @Override public void onDestroy() {
        mIsServiceDestoryed.set(false);
        super.onDestroy();
    }
}
