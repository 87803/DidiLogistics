package com.jiuxiang.didilogistics.beans;

import android.os.Handler;

;

public class App {
    static private User user;
    static private String token = "";
    static private Handler homeFragmentHandler; //首页的handler，用于刷新首页订单列表数据
    static private Handler userInfoFragmentHandler; //我的页面的handler，用于刷新用户基本信息

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        App.user = user;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        App.token = token;
    }

    public static Handler getHomeFragmentHandler() {
        return homeFragmentHandler;
    }

    public static void setHomeFragmentHandler(Handler homeFragmentHandler) {
        App.homeFragmentHandler = homeFragmentHandler;
    }

    public static Handler getUserInfoFragmentHandler() {
        return userInfoFragmentHandler;
    }

    public static void setUserInfoFragmentHandler(Handler userInfoFragmentHandler) {
        App.userInfoFragmentHandler = userInfoFragmentHandler;
    }
}
