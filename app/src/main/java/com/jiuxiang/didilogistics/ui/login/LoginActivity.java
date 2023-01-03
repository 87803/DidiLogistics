package com.jiuxiang.didilogistics.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.fastjson.JSONObject;
import com.jiuxiang.didilogistics.BR;
import com.jiuxiang.didilogistics.R;
import com.jiuxiang.didilogistics.databinding.ActivityLoginBinding;
import com.jiuxiang.didilogistics.ui.main.MainActivity;
import com.jiuxiang.didilogistics.ui.register.RegisterActivity;
import com.jiuxiang.didilogistics.utils.App;
import com.jiuxiang.didilogistics.utils.DataUtils;

//该界面用于用户登录
public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);//setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        // loginViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);
        loginViewModel.setLoginActivity(this);
        binding.setVariable(BR.loginViewModel, loginViewModel);
        binding.setLifecycleOwner(this);

        quickLogin();//本地存在用户数据的情况下快速登录
        //读取本地的账号密码，如果存在则自动填充
        JSONObject userData = DataUtils.readPhonePwd(this);
        loginViewModel.getPhone().setValue(userData.getString("phone"));
        loginViewModel.getPassword().setValue(userData.getString("password"));

        binding.tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    //读取token，如果存在则直接跳转到主界面
    private void quickLogin() {
        DataUtils.readTokenAndUserInfo(this);
        if (App.getToken() != null && App.getToken().length() > 0) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}