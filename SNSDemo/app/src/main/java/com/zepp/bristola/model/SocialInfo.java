package com.zepp.bristola.model;

/**
 * Created by xubinggui on 1/20/16.
 */
public class SocialInfo {
    public final String WECHAT_APP_ID = "wxa00dba30c1f978b6";
    public final String WEIBO_APP_ID = "wb1299747600";
    public final String QQ_APP_ID = "1104845090";

    public String wechatappSecret = "212df27cd3abc66786b122b3564379b8";

    public final String WeChatScope = "snsapi_userinfo";

    public final String SCOPE = "snsapi_userinfo";

    public String getUrlForWeChatToken() {
        return "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + WECHAT_APP_ID
                + "&secret="
                + wechatappSecret
                + "&code=%s&grant_type=authorization_code";
    }

    public String getUrlForWeChatUserInfo() {
        return "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
    }

    public String getUrlForWeChatRefreshToken() {
        return "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid="
                + WECHAT_APP_ID
                + "&grant_type=refresh_token&refresh_token=%s";
    }
}
