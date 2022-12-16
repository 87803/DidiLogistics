package com.jiuxiang.didilogistics.ui.login;

import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSONObject;
import com.jiuxiang.didilogistics.beans.User;
import com.jiuxiang.didilogistics.ui.main.MainActivity;
import com.jiuxiang.didilogistics.utils.App;
import com.jiuxiang.didilogistics.utils.DataUtils;
import com.jiuxiang.didilogistics.utils.HTTPResult;
import com.jiuxiang.didilogistics.utils.HTTPUtils;
import com.jiuxiang.didilogistics.utils.MD5Utils;

import java.util.Objects;

public class LoginViewModel extends ViewModel {
    private LoginActivity loginActivity;
    private MutableLiveData<String> phone;
    private MutableLiveData<String> password;

    public LoginViewModel() {
        phone = new MutableLiveData<>("");
        password = new MutableLiveData<>("");
    }

    public void clickLogin() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", getPhone().getValue());
        jsonObject.put("password", MD5Utils.encrypt(Objects.requireNonNull(getPassword().getValue())));
        String data = jsonObject.toJSONString();

        HTTPUtils.post("/login", data, new HTTPResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                if (jsonObject.getInteger("code") == 200) {
                    loginActivity.runOnUiThread(() -> {
                        Toast.makeText(loginActivity, "登录成功", Toast.LENGTH_SHORT).show();
                        User user = JSONObject.parseObject(jsonObject.getJSONObject("data").getString("user"), User.class);
                        String token = jsonObject.getJSONObject("data").getString("token");
                        App.setUser(user);//保存用户信息
                        App.setToken(token);//保存token
                        DataUtils.saveTokenAndUserInfo(loginActivity, token, user);//保存token
                        DataUtils.rememberPhonePwd(loginActivity, getPhone().getValue(), getPassword().getValue());//保存手机号和密码
                        Intent intent = new Intent(loginActivity, MainActivity.class);
                        loginActivity.startActivity(intent);
                        loginActivity.finish();
                    });
                } else {
                    loginActivity.runOnUiThread(() -> {
                        Toast.makeText(loginActivity, "登录失败，" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(String error) {
                loginActivity.runOnUiThread(() -> {
                    Toast.makeText(loginActivity, "登录失败，" + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    public LoginActivity getLoginActivity() {
        return loginActivity;
    }

    public void setLoginActivity(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    public MutableLiveData<String> getPhone() {
        return phone;
    }

    public void setPhone(MutableLiveData<String> phone) {
        this.phone = phone;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public void setPassword(MutableLiveData<String> password) {
        this.password = password;
    }
}
