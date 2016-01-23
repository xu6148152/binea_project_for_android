package com.zepp.bristola.qq;

import android.app.Activity;
import android.content.Context;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.zepp.bristola.model.SocialToken;

/**
 * Created by xubinggui on 1/20/16.
 */
public class TencentQQManager {
    private static Tencent sTencent;

    public static Tencent getInstance(Context context, String appId) {
        if (sTencent == null) {
            sTencent = Tencent.createInstance(appId, context);
        }
        return sTencent;
    }

    public static void login(Context context, String appId, String scope, IUiListener listener) {
        getInstance(context, appId);
        sTencent.login((Activity) context, scope, listener);
    }

    public static void logout(Context context, String appId) {
        sTencent.logout(context);
    }

    public static void getUserInfo(Context context, String appId, SocialToken token,
            IUiListener listener) {
        UserInfo info = new UserInfo(context, parseToken(appId, token));
        info.getUserInfo(listener);
    }

    private static QQToken parseToken(String appId, SocialToken socialToken) {
        QQToken token = new QQToken(appId);
        token.setAccessToken(socialToken.token, "3600");
        token.setOpenId(socialToken.openId);
        return token;
    }
}
