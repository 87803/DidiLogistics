package com.jiuxiang.didilogistics.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.jiuxiang.didilogistics.R;
import com.jiuxiang.didilogistics.databinding.ActivityRegisterBinding;
import com.jiuxiang.didilogistics.ui.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;//private ViewDataBinding binding;
    private RegisterViewModel registerViewModel;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        registerViewModel.setRegisterActivity(this);
        binding.setVariable(com.jiuxiang.didilogistics.BR.registerViewModel, registerViewModel);
        binding.setLifecycleOwner(this);
//        etAccount = findViewById(R.id.et_account);
//        etPassword = findViewById(R.id.et_password);
//        etPasswordConfirm = findViewById(R.id.et_password_confirm);
        tvLogin = findViewById(R.id.tv_login);

        tvLogin.setOnClickListener(v -> {
            finish();
        });
    }
}