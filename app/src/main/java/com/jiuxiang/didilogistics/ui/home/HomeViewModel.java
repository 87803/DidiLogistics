package com.jiuxiang.didilogistics.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuxiang.didilogistics.beans.Demand;
import com.jiuxiang.didilogistics.databinding.FragmentHomeBinding;
import com.jiuxiang.didilogistics.ui.MainActivity;
import com.jiuxiang.didilogistics.ui.orderDetail.OrderDetailActivity;
import com.jiuxiang.didilogistics.utils.HTTPResult;
import com.jiuxiang.didilogistics.utils.HTTPUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    @SuppressLint("StaticFieldLeak")
    private static FragmentHomeBinding binding;
    @SuppressLint("StaticFieldLeak")
    private static MainActivity mainActivity;
    private static HomeFragment homeFragment;
    private final List<Demand> data = new ArrayList<>();
    private final MutableLiveData<String> mText;

    public void setBinding(FragmentHomeBinding binding) {
        HomeViewModel.binding = binding;
        //设置适配器方式和以往不同
        binding.setAdapter(new DemandAdapter(homeFragment.getActivity(), data));//
//        binding.listview.setAdapter(binding.getAdp());
        //通过binding来设置点击长按事件
        binding.listview.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(homeFragment.getActivity(), OrderDetailActivity.class);
            intent.putExtra("orderID", data.get(position).getOrderID());
            homeFragment.getActivity().startActivity(intent);
            System.out.println("点击了" + position);
            System.out.println(data.get(position).getOrderID());

            //点击事件
        });
//        binding.listview.setOnItemLongClickListener(null);
        //往列表里添加数据
//        data.add(new Demand("emmmmm"));
        //更新列表
//        binding.getAdapter().notifyDataSetChanged();
        //不在主线陈更新
//        mainActivity.runOnUiThread(() -> binding.getAdapter().notifyDataSetChanged());
    }

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        loadData();
    }

    public void loadData() {
        HTTPUtils.get("/auth/demand", new HTTPResult() {
            @Override
            public void onSuccess(JSONObject result) {
                System.out.println(result);
                data.clear();
                JSONArray data2 = result.getJSONArray("data");
                data.addAll(data2.toJavaList(Demand.class));
                mainActivity.runOnUiThread(() -> {
                    binding.getAdapter().notifyDataSetChanged();
                    binding.noDataTextview.setVisibility(data.size() == 0 ? View.VISIBLE : View.GONE);
                    binding.listview.setVisibility(data.size() == 0 ? View.GONE : View.VISIBLE);
                });
            }

            @Override
            public void onFailure(String error) {
                mainActivity.runOnUiThread(() -> Toast.makeText(mainActivity, "获取订单数据失败，请重试，" + error, Toast.LENGTH_LONG).show());
            }
        });
    }


    public void setMainActivity(MainActivity mainActivity) {
        HomeViewModel.mainActivity = mainActivity;
    }

    public static void setHomeFragment(HomeFragment homeFragment) {
        HomeViewModel.homeFragment = homeFragment;
    }

    public LiveData<String> getText() {
        return mText;
    }

    public static FragmentHomeBinding getBinding() {
        return binding;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static HomeFragment getHomeFragment() {
        return homeFragment;
    }

    public List<Demand> getData() {
        return data;
    }

    public MutableLiveData<String> getmText() {
        return mText;
    }
}