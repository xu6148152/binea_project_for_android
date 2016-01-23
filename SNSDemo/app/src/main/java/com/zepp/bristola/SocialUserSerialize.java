package com.zepp.bristola;

import android.content.Context;
import android.content.SharedPreferences;
import com.zepp.bristola.model.SocialUser;

/**
 * Created by xubinggui on 1/20/16.
 */
public class SocialUserSerialize {
    private static final String PREFERENCE_NAME = "social_user";

    private static final String KEY_TYPE = "type";
    private static final String KEY_OPENID = "open_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_AVATAR = "avatar";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_EXPIRES_TIME = "expires_time";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_SIGNATURE = "signature";

    /**
     * 持久化用户信息
     *
     * @param context context
     * @param user    用户信息
     */
    protected static void writeSocialUser(Context context, SocialUser user) {
        if (user == null || context == null)
            return;

        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(KEY_TYPE, user.type);
        editor.putString(KEY_OPENID, user.token.openId);
        editor.putString(KEY_NAME, user.name);
        editor.putString(KEY_AVATAR, user.avatar);
        editor.putInt(KEY_GENDER, user.gender);
        editor.putString(KEY_TOKEN, user.token.token);
        editor.putString(KEY_REFRESH_TOKEN, user.token.refreshToken);
        editor.putLong(KEY_EXPIRES_TIME, user.token.expiresTime);
        editor.putString(KEY_SIGNATURE, user.desc);
        editor.commit();
    }

    /**
     * 读取用户信息
     *
     * @param context context
     * @return 用户信息
     */
    protected static SocialUser readSocialUser(Context context) {
        if (context == null)
            return null;

        //SocialUser user = new SocialUser(SocialUser.TYPE_WECHAT, openId, name, avatar, gender, city,
        //        province, token);
        //SocialToken token = new SocialToken();
        //SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        //user.type = (preferences.getInt(KEY_TYPE, 0));
        //user.name = (preferences.getString(KEY_NAME, ""));
        //user.avatar = (preferences.getString(KEY_AVATAR, ""));
        //user.gender = (preferences.getInt(KEY_GENDER, 0));
        //user.desc = (preferences.getString(KEY_SIGNATURE, ""));
        //token.openId = (preferences.getString(KEY_OPENID, ""));
        //token.token = (preferences.getString(KEY_TOKEN, ""));
        //token.refreshToken = (preferences.getString(KEY_REFRESH_TOKEN, ""));
        //token.expiresTime = (preferences.getLong(KEY_EXPIRES_TIME, 0));
        //user.token = (token);

        return new SocialUser();
    }

    /**
     * 清除用户信息
     *
     * @param context context
     */
    protected static void clear(Context context) {
        if (null == context)
            return;

        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }
}
