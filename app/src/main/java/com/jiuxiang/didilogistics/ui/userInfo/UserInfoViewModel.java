package com.jiuxiang.didilogistics.ui.userInfo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiuxiang.didilogistics.beans.User;
import com.jiuxiang.didilogistics.ui.login.LoginActivity;
import com.jiuxiang.didilogistics.ui.main.MainActivity;
import com.jiuxiang.didilogistics.utils.App;
import com.jiuxiang.didilogistics.utils.DataUtils;
import com.jiuxiang.didilogistics.utils.HTTPResult;
import com.jiuxiang.didilogistics.utils.HTTPUtils;

//用户信息界面对应的ViewModel，获取用户信息
public class UserInfoViewModel extends ViewModel {
    @SuppressLint("StaticFieldLeak")
    private static MainActivity mainActivity;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                System.out.println("更新用户数据");
                updateUserInfo();
            }
        }
    };

    private MutableLiveData<User> userMutableLiveData;

    public UserInfoViewModel() {
        userMutableLiveData = new MutableLiveData<>();
        userMutableLiveData.setValue(App.getUser());
        App.setUserInfoFragmentHandler(handler);
    }

    //用户退出登录，清除本地数据
    public void logout() {
        App.setUser(null);
        App.setToken("");
        DataUtils.saveTokenAndUserInfo(mainActivity, "", null);
        Intent intent = new Intent(mainActivity, LoginActivity.class);
        mainActivity.startActivity(intent);
        mainActivity.finish();
    }

    public void updateUserInfo() {
        HTTPUtils.get("/auth/modifyInfo", new HTTPResult() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.getInteger("code") == 200) {
                    mainActivity.runOnUiThread(() -> {
                        Toast.makeText(mainActivity, "更新用户信息成功", Toast.LENGTH_SHORT).show();
                        User user = JSON.toJavaObject(result.getJSONObject("data"), User.class);
                        App.setUser(user);//保存用户信息
                        DataUtils.saveTokenAndUserInfo(mainActivity, App.getToken(), user);
                        userMutableLiveData.setValue(App.getUser());
                    });
                } else {
                    mainActivity.runOnUiThread(() -> {
                        Toast.makeText(mainActivity, result.getString("msg"), Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(String error) {
                mainActivity.runOnUiThread(() -> {
                    Toast.makeText(mainActivity, "更新用户信息失败，" + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        UserInfoViewModel.mainActivity = mainActivity;
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public void setUserMutableLiveData(MutableLiveData<User> userMutableLiveData) {
        this.userMutableLiveData = userMutableLiveData;
    }
}