package com.zepp.bristola.wechat;

import android.content.Context;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zepp.bristola.model.SocialInfo;
import com.zepp.bristola.model.SocialToken;
import com.zepp.bristola.model.SocialUser;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xubinggui on 1/19/16.
 */
public class WeChatManager {
    private static IWXAPI sApi;

    public static IWXAPI getInstance(Context context, String appId) {
        if (sApi == null) {
            sApi = WXAPIFactory.createWXAPI(context, appId, true);
            sApi.registerApp(appId);
        }
        return sApi;
    }

    public static IWXAPI getInstance() {
        return sApi;
    }

    private static WechatResultCallback callback;

    public static void handleSso(SendAuth.Resp baseResp) {
        if (callback != null) {
            if (baseResp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
                callback.onCancel();
            } else if (baseResp.errCode == BaseResp.ErrCode.ERR_AUTH_DENIED) {
                callback.onFailure(new Exception("BaseResp.ErrCode.ERR_AUTH_DENIED"));
            } else if (baseResp.errCode == BaseResp.ErrCode.ERR_UNSUPPORT) {
                callback.onFailure(new Exception("BaseResp.ErrCode.ERR_UNSUPPORT"));
            } else if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
                callback.onGetCodeSuccess(baseResp.code);
            }
        }
    }

    public static void login(Context context, SocialInfo info, WechatResultCallback callback) {
        WeChatManager.callback = callback;
        SendAuth.Req req = new SendAuth.Req();
        req.scope = info.SCOPE;
        WeChatManager.getInstance(context, info.WECHAT_APP_ID).sendReq(req);
    }

    public static void getAccessToken(final String code, final String getTokenUrl) {
        new Thread(new Runnable() {
            @Override public void run() {
                try {
                    URL url = new URL(String.format(getTokenUrl, code));
                    HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConn.getInputStream());

                    String result = inputStream2String(in);
                    try {
                        JSONObject info = new JSONObject(result);
                        //当前token的有效市场只有7200s，需要利用refresh_token去获取新token，考虑当前需要利用token的只有获取用户信息，手动设置token超时为30天
                        SocialToken token = new SocialToken(info.getString("openid"),
                                info.getString("access_token"), info.getString("refresh_token"), /*info.getLong("expires_in")*/
                                30 * 24 * 60 * 60);
                        callback.onGetTokenSuccess(token);
                    } catch (JSONException e) {
                        callback.onFailure(e);
                    }
                } catch (Exception e) {
                    callback.onFailure(e);
                }
            }
        }).start();
    }

    private static String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }

    public static void getUserInfo(final Context context, final String getUserInfoUrl,
            final SocialToken token) {
        new Thread(new Runnable() {
            @Override public void run() {
                try {
                    URL url = new URL(String.format(getUserInfoUrl, token.token, token.openId));
                    HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConn.getInputStream());

                    String result = inputStream2String(in);
                    try {
                        JSONObject info = new JSONObject(result);
                        String openId = info.getString("openid");
                        String city = info.getString("city");
                        String province = info.getString("province");

                        String name = info.getString("nickname");
                        int gender = info.getInt("sex");
                        String avatar = info.getString("headimgurl");

                        SocialUser user =
                                new SocialUser(SocialUser.TYPE_WECHAT, openId, name, avatar, gender,
                                        city, province, token);

                        callback.onGetUserInfoSuccess(user);
                    } catch (JSONException e) {
                        callback.onFailure(e);
                    }
                } catch (Exception e) {
                    callback.onFailure(e);
                }
            }
        }).start();
    }

    public static void inviteFriend() {
        sendMsgToWX(SceneType.WXSceneSession);
    }

    private static void sendMsgToWX(SceneType type) {
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = "www.zepp.com";

        WXMediaMessage msg = new WXMediaMessage(webpageObject);
        msg.title = "网页标题";
        msg.title = "网页描述";
        //msg.setThumbImage();

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        if(type == SceneType.WXSceneSession) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        } else if(type == SceneType.WXSceneTimeline) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        } else {
            req.scene = SendMessageToWX.Req.WXSceneFavorite;
        }
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    enum SceneType {
        WXSceneSession,
        WXSceneTimeline,
        WXSceneFavorite
    }
}
