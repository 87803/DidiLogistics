package com.jiuxiang.didilogistics.ui.userInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jiuxiang.didilogistics.BR;
import com.jiuxiang.didilogistics.R;
import com.jiuxiang.didilogistics.databinding.FragmentUserInfoBinding;
import com.jiuxiang.didilogistics.ui.main.MainActivity;
import com.jiuxiang.didilogistics.ui.modifyInfo.ModifyInfoActivity;
import com.jiuxiang.didilogistics.utils.App;

//用户信息界面
public class UserInfoFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        UserInfoViewModel userInfoViewModel = new ViewModelProvider(this).get(UserInfoViewModel.class);
        UserInfoViewModel.setMainActivity((MainActivity) getActivity());

        FragmentUserInfoBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_info, container, false);
        View root = binding.getRoot();
        binding.setVariable(BR.userViewModel, userInfoViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //根据用户类型显示不同的字段，用户类型为货主，隐藏司机相关字段
        if (!App.getUser().isType())
            binding.driverTable.setVisibility(View.GONE);

        binding.modifyBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ModifyInfoActivity.class);
            startActivity(intent);
        });
        return root;
    }

}