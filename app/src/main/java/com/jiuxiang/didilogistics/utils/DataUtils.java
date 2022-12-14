package com.jiuxiang.didilogistics.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSONObject;
import com.jiuxiang.didilogistics.beans.App;
import com.jiuxiang.didilogistics.beans.User;
import com.jiuxiang.didilogistics.ui.MainActivity;
import com.jiuxiang.didilogistics.ui.login.LoginActivity;

public class DataUtils {
    public static void saveTokenAndUserInfo(LoginActivity loginActivity, String token, User userinfo) {
        App.setToken(token);
        //步骤1：创建一个SharedPreferences对象
        SharedPreferences sharedPreferences = loginActivity.getSharedPreferences("token", Context.MODE_PRIVATE);
        //步骤2： 实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //步骤3：将获取过来的值放入文件
        editor.putString("token", token);
        String userinfoStr = JSONObject.toJSONString(userinfo);
        editor.putString("userinfo", userinfoStr);
        //步骤4：提交
        editor.commit();
    }

    public static void saveTokenAndUserInfo(MainActivity loginActivity, String token, User userinfo) {
        App.setToken(token);
        //步骤1：创建一个SharedPreferences对象
        SharedPreferences sharedPreferences = loginActivity.getSharedPreferences("token", Context.MODE_PRIVATE);
        //步骤2： 实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //步骤3：将获取过来的值放入文件
        editor.putString("token", token);
        String userinfoStr = JSONObject.toJSONString(userinfo);
        editor.putString("userinfo", userinfoStr);
        //步骤4：提交
        editor.commit();
    }

    public static void readTokenAndUserInfo(LoginActivity loginActivity) {
        //步骤1：创建一个SharedPreferences对象
        SharedPreferences sharedPreferences = loginActivity.getSharedPreferences("token", Context.MODE_PRIVATE);
        //步骤2：获取文件中的值
        String token = sharedPreferences.getString("token", "");
        String userinfoStr = sharedPreferences.getString("userinfo", "");
        User userinfo = JSONObject.parseObject(userinfoStr, User.class);
        App.setToken(token);
        App.setUser(userinfo);
    }

    public static void rememberPhonePwd(LoginActivity loginActivity, String phone, String pwd) {
        SharedPreferences sharedPreferences = loginActivity.getSharedPreferences("userLoginInput", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phone", phone);
        editor.putString("pwd", pwd);
        editor.commit();
    }

    public static JSONObject readPhonePwd(LoginActivity loginActivity) {
        SharedPreferences sharedPreferences = loginActivity.getSharedPreferences("userLoginInput", Context.MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", "");
        String pwd = sharedPreferences.getString("pwd", "");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", phone);
        jsonObject.put("password", pwd);
        return jsonObject;
    }

}
