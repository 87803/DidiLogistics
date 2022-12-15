package com.jiuxiang.didilogistics.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuxiang.didilogistics.beans.Demand;
import com.jiuxiang.didilogistics.databinding.FragmentDashboardDriverBinding;
import com.jiuxiang.didilogistics.ui.home.DemandAdapter;
import com.jiuxiang.didilogistics.ui.main.MainActivity;
import com.jiuxiang.didilogistics.ui.orderDetail.OrderDetailActivity;
import com.jiuxiang.didilogistics.utils.HTTPResult;
import com.jiuxiang.didilogistics.utils.HTTPUtils;

import java.util.ArrayList;
import java.util.List;

public class DashboardDriverViewModel extends ViewModel {

    @SuppressLint("StaticFieldLeak")
    private static FragmentDashboardDriverBinding binding;
    @SuppressLint("StaticFieldLeak")
    private static MainActivity mainActivity;
    private final List<Demand> data = new ArrayList<>();

    public DashboardDriverViewModel() {
        loadData();
    }

    public void loadData() {
        HTTPUtils.get("/auth/demand?order=true", new HTTPResult() {
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

    public void setBinding(FragmentDashboardDriverBinding binding) {
        DashboardDriverViewModel.binding = binding;
        //设置适配器方式和以往不同
        binding.setAdapter(new DemandAdapter(mainActivity, data));//
//        binding.listview.setAdapter(binding.getAdp());
        //通过binding来设置点击长按事件
        binding.listview.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(mainActivity, OrderDetailActivity.class);
            intent.putExtra("orderID", data.get(position).getOrderID());
            mainActivity.startActivity(intent);
            System.out.println("点击了" + position);
            System.out.println(data.get(position).getOrderID());
            //点击事件
        });
    }

    public static FragmentDashboardDriverBinding getBinding() {
        return binding;
    }


    public List<Demand> getData() {
        return data;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        DashboardDriverViewModel.mainActivity = mainActivity;
    }
}