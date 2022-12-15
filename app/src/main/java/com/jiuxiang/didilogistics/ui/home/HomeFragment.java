package com.jiuxiang.didilogistics.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jiuxiang.didilogistics.R;
import com.jiuxiang.didilogistics.beans.App;
import com.jiuxiang.didilogistics.databinding.FragmentHomeBinding;
import com.jiuxiang.didilogistics.ui.main.MainActivity;
import com.jiuxiang.didilogistics.ui.postDemand.PostDemandActivity;

//import com.jiuxiang.didilogistics.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            int arg1=msg.arg1;
//            String info= (String) msg.obj;
            if (msg.what == 1) {
                System.out.println("更新列表");
                homeViewModel.loadData();
            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
        homeViewModel.setMainActivity((MainActivity) getActivity());
        homeViewModel.setHomeFragment(this);

        App.setHomeFragmentHandler(handler);

//return inflater.inflate(R.layout.fragment_home, container, false);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View root = binding.getRoot();

        binding.setVariable(com.jiuxiang.didilogistics.BR.homeViewModel, homeViewModel);
        homeViewModel.setBinding(binding);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        if (App.getUser().isType()) {
            binding.fab.setVisibility(View.GONE);
        }
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PostDemandActivity.class);
                startActivity(intent);
            }
        });

        return root;
//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}