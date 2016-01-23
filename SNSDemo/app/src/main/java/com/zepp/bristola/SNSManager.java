package com.zepp.bristola;

import android.content.Context;
import android.util.Log;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.zepp.bristola.model.SocialInfo;
import com.zepp.bristola.model.SocialToken;
import com.zepp.bristola.model.SocialUser;
import com.zepp.bristola.qq.TencentQQManager;
import com.zepp.bristola.wechat.WeChatManager;
import com.zepp.bristola.wechat.WechatResultCallback;

/**
 * Created by xubinggui on 1/19/16.
 */
public class SNSManager {
    private final String TAG = getClass().getCanonicalName();

    private SocialUser mUser = new SocialUser();

    private static class Holder {
        public static SNSManager manager = new SNSManager();
    }

    public static SNSManager getInstance() {
        return Holder.manager;
    }

    public void loginWeChat(final Context context) {
        if(!checkTokenInvalid()) {
            return;
        }
        final SocialInfo info = new SocialInfo();
        WeChatManager.login(context, info, new WechatResultCallback() {
            @Override public void onGetCodeSuccess(String code) {
                Log.d(TAG, "onGetCodeSuccess " + code);
                fetchWechatAccessToken(code);
            }

            private void fetchWechatAccessToken(String code) {
                WeChatManager.getAccessToken(code, info.getUrlForWeChatToken());
            }

            @Override public void onGetTokenSuccess(SocialToken token) {
                Log.d(TAG, "onGetTokenSuccess ");
                WeChatManager.getUserInfo(context, info.getUrlForWeChatUserInfo(), token);
            }

            @Override public void onGetUserInfoSuccess(SocialUser user) {
                Log.d(TAG, "onGetUserInfoSuccess");
                //callback to js
            }

            @Override public void onFailure(Exception e) {
                Log.d(TAG, "onFailure " + e.getMessage());
            }

            @Override public void onCancel() {
                Log.d(TAG, "onCancel");
            }
        });
    }

    public void logoutWechat() {

    }

    public void loginQQ(final Context context) {
        if(!checkTokenInvalid()) {
            return;
        }
        final SocialInfo info = new SocialInfo();
        TencentQQManager.getInstance(context, info.QQ_APP_ID);
        TencentQQManager.login(context, info.QQ_APP_ID, info.SCOPE, new IUiListener() {
            @Override public void onComplete(Object o) {
                Log.d(TAG, "onComplete");
            }

            @Override public void onError(UiError uiError) {
                Log.d(TAG, "onError " + uiError.errorMessage);
            }

            @Override public void onCancel() {
                Log.d(TAG, "onCancel");
            }
        });
    }

    private boolean checkTokenInvalid() {
        return true;
    }

    public void shareWX() {
        WeChatManager.inviteFriend();
    }

    public void registerWX(Context context, SocialInfo info) {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = info.SCOPE;
        WeChatManager.getInstance(context, info.WECHAT_APP_ID).sendReq(req);
    }
}
