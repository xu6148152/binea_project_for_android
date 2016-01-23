package com.zepp.bristola.wechat;

import com.zepp.bristola.model.SocialToken;
import com.zepp.bristola.model.SocialUser;

/**
 * Created by xubinggui on 1/20/16.
 */
public interface WechatResultCallback {
    void onGetCodeSuccess(String code);
    void onGetTokenSuccess(SocialToken token);

    void onGetUserInfoSuccess(SocialUser user);

    void onFailure(Exception e);

    void onCancel();
}
