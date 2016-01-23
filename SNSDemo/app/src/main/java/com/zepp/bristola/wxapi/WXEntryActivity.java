package com.zepp.bristola.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.zepp.bristola.wechat.WeChatManager;

/**
 * Created by xubinggui on 1/19/16.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        setIntent(intent);
        final IWXAPI api = WeChatManager.getInstance();
        if(api != null) {
            api.handleIntent(intent, this);
        }
    }

    @Override public void onReq(BaseReq baseReq) {
        Log.d("onReq", "onReq " + baseReq);
    }

    @Override public void onResp(BaseResp baseResp) {
        Log.d("onResp", "onResp " + baseResp);
        if(baseResp instanceof SendAuth.Resp) {
            WeChatManager.handleSso((SendAuth.Resp)baseResp);
        }else if(baseResp instanceof SendMessageToWX.Resp) {

        }
        finish();
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }
}
