package com.zepp.www.binderdemo;

import android.os.IBinder;
import android.os.RemoteException;

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
public class BinderPoolImpl extends IBinderPool.Stub {
    @Override public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

    }

    @Override public IBinder queryBinder(int binderCode) throws RemoteException {
        switch (binderCode) {
            case Constants.BINDER_SECURITY_CENTER:
                return new SecurityCenterImpl();
            case Constants.BINDER_COMPUTE:
                return new ComputeImpl();
        }
        return null;
    }
}
