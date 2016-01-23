package com.zepp.bristola.model;

/**
 * Created by xubinggui on 1/20/16.
 */
public class SocialUser {
    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_WEIBO = 1;
    public static final int TYPE_WECHAT = 2;
    public static final int TYPE_QQ = 3;

    public static final int GENDER_UNKNOWN = 0;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;

    public int type;
    public String name;
    public String avatar;
    public int gender;
    public String desc;
    public SocialToken token;
    public String openId;
    public String city;
    public String province;

    public SocialUser(int typeWechat, String openId, String name, String avatar, int gender,
            String city, String province, SocialToken token) {
        this.type = typeWechat;
        this.name = name;
        this.avatar = avatar;
        this.gender = gender;
        this.token = token;
        this.openId = openId;
        this.city = city;
        this.province = province;
    }

    public SocialUser() {
    }

    public boolean isTokenValid() {
        return token != null && System.currentTimeMillis() < token.expiresTime;
    }
}
