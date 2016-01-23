package com.zepp.bristola.model;

/**
 * Created by xubinggui on 1/20/16.
 */
public class SocialToken {
    public String openId;
    public String token;
    public String refreshToken;
    public long expiresTime;

    public SocialToken(String openId, String token, String refreshToken, long expiresTime) {
        this.openId = openId;
        this.token = token;
        this.refreshToken = refreshToken;
        this.expiresTime = expiresTime;
    }

    public SocialToken() {
    }
}
