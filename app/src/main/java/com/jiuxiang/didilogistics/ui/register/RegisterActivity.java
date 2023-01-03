package com.jiuxiang.didilogistics.ui.register;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.jiuxiang.didilogistics.R;
import com.jiuxiang.didilogistics.databinding.ActivityRegisterBinding;

//注册界面的Activity
public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RegisterViewModel registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        registerViewModel.setRegisterActivity(this);

        ActivityRegisterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.setVariable(com.jiuxiang.didilogistics.BR.registerViewModel, registerViewModel);
        binding.setLifecycleOwner(this);

        binding.tvLogin.setOnClickListener(v -> {
            finish();
        });
    }
}