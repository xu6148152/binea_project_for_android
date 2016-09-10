package com.zepp.www.binderdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import java.util.concurrent.Executors;

//  Created by xubinggui on 9/10/16.
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
public class BinderPoolActivity extends Activity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override public void run() {
                doWork();
            }
        });
    }

    private void doWork() {
        BinderPool binderPool = BinderPool.getInstance(this);
        IBinder securityBinder = binderPool.queryBinder(Constants.BINDER_SECURITY_CENTER);
        ISecurityCenter securityCenter = SecurityCenterImpl.asInterface(securityBinder);
        String msg = "hello-android";
        System.out.println("content: " + msg);
        try {
            String password = securityCenter.encrypt(msg);
            System.out.println("encrypt:" + password);
            System.out.println("decrypt:" + securityCenter.decrypt(password));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        IBinder computerBinder = binderPool.queryBinder(Constants.BINDER_COMPUTE);
        ICompute compute = ComputeImpl.asInterface(computerBinder);
        try {
            System.out.println("3+5=" + compute.add(3, 5));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
