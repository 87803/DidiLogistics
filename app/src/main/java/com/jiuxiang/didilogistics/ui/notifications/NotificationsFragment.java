package com.jiuxiang.didilogistics.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jiuxiang.didilogistics.BR;
import com.jiuxiang.didilogistics.R;
import com.jiuxiang.didilogistics.beans.App;
import com.jiuxiang.didilogistics.databinding.FragmentNotificationsBinding;
import com.jiuxiang.didilogistics.ui.MainActivity;
import com.jiuxiang.didilogistics.ui.home.HomeViewModel;
import com.jiuxiang.didilogistics.ui.orderDetail.OrderDetailActivity;

//import com.jiuxiang.didilogistics.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifications, container, false);
        View root = binding.getRoot();
        binding.setVariable(BR.notificationViewModel, notificationsViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        notificationsViewModel.setNotificationsFragment(this);
        notificationsViewModel.setBinding(binding);
        notificationsViewModel.setMainActivity((MainActivity) getActivity());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }
}