package com.jiuxiang.didilogistics.ui.register;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.jiuxiang.didilogistics.R;
import com.jiuxiang.didilogistics.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;//private ViewDataBinding binding;
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        registerViewModel.setRegisterActivity(this);
        binding.setVariable(com.jiuxiang.didilogistics.BR.registerViewModel, registerViewModel);
        binding.setLifecycleOwner(this);


        binding.tvLogin.setOnClickListener(v -> {
            finish();
        });
    }
}