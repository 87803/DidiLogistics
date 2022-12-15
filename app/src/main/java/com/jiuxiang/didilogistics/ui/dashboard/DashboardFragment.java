package com.jiuxiang.didilogistics.ui.dashboard;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jiuxiang.didilogistics.BR;
import com.jiuxiang.didilogistics.R;
import com.jiuxiang.didilogistics.beans.App;
import com.jiuxiang.didilogistics.databinding.FragmentDashboardBinding;
import com.jiuxiang.didilogistics.databinding.FragmentDashboardDriverBinding;
import com.jiuxiang.didilogistics.ui.home.HomeViewModel;
import com.jiuxiang.didilogistics.ui.main.MainActivity;

//import com.jiuxiang.didilogistics.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (App.getUser().isType()) { //司机端界面，显示司机订单
            DashboardDriverViewModel dashboardDriverViewModel = new ViewModelProvider(this).get(DashboardDriverViewModel.class);
            FragmentDashboardDriverBinding binding;
            dashboardDriverViewModel.setMainActivity((MainActivity) getActivity());

//return inflater.inflate(R.layout.fragment_home, container, false);

            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard_driver, container, false);
            View root = binding.getRoot();

            binding.setVariable(BR.DashboardDriverViewModel, dashboardDriverViewModel);
            dashboardDriverViewModel.setBinding(binding);
            binding.setLifecycleOwner(getViewLifecycleOwner());

            return root;
        } else {  //货主端界面，显示在线司机
            FragmentDashboardBinding binding;

            DashboardViewModel dashboardViewModel =
                    new ViewModelProvider(this).get(DashboardViewModel.class);

//            binding = FragmentDashboardBinding.inflate(inflater, container, false);
//            View root = binding.getRoot();

            dashboardViewModel.setMainActivity((MainActivity) getActivity());

//return inflater.inflate(R.layout.fragment_home, container, false);

            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
            View root = binding.getRoot();
            binding.setVariable(BR.DashboardViewModel, dashboardViewModel);
            dashboardViewModel.setBinding(binding);
            //传入订单列表
            HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
            ;    //用户的订单信息
            dashboardViewModel.setOrderData(homeViewModel.getData());

            binding.setLifecycleOwner(getViewLifecycleOwner());
//            final TextView textView = binding.textDashboard;
//            textView.setOnClickListener(v -> {
//                String[] arr = new String[homeViewModel.getData().size()];
//                for (int i = 0; i < homeViewModel.getData().size(); i++) {
//                    arr[i] = homeViewModel.getData().get(i).getOrderID() + " " + (homeViewModel.getData().get(i).getDescription() == null ? "无货物信息" : homeViewModel.getData().get(i).getDescription());
//                }
////                String[] array = new String[100];
////            String result;
//                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardFragment.this.getActivity());
//                builder.setSingleChoiceItems(arr, 0, (dialog, which) -> {
////                    String result = homeViewModel.getData()[which];
//                    dialog.dismiss();
//                });
//                builder.show();
//            });
//            dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
            return root;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }
}