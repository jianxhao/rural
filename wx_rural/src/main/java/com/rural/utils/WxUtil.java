package com.rural.utils;

import java.text.MessageFormat;

public class WxUtil {
    private final static String WX_ACCESS_TOKEN_URL= "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";
    private final static String WX_LOGIN_SERVER_URL= "https://api.weixin.qq.com/sns/jscode2session?appid={0}&secret={1}&js_code={2}&grant_type=authorization_code";
    public static String getWxServerUrl(String appId, String appSecret, String code) {
        String url = MessageFormat.format(WX_LOGIN_SERVER_URL, new String[]{appId, appSecret, code});
        return url;
    }
    public static String getWxAccessTokenUrl(String appId, String appSecret) {
        String url = MessageFormat.format(WX_ACCESS_TOKEN_URL, new String[]{appId, appSecret});
        return url;
    }
}
