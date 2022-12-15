package com.jiuxiang.didilogistics.ui.dashboard;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuxiang.didilogistics.beans.Demand;
import com.jiuxiang.didilogistics.beans.Driver;
import com.jiuxiang.didilogistics.databinding.FragmentDashboardBinding;
import com.jiuxiang.didilogistics.ui.main.MainActivity;
import com.jiuxiang.didilogistics.utils.HTTPResult;
import com.jiuxiang.didilogistics.utils.HTTPUtils;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends ViewModel {
    @SuppressLint("StaticFieldLeak")
    private static FragmentDashboardBinding binding;
    @SuppressLint("StaticFieldLeak")
    private static MainActivity mainActivity;
    private final List<Driver> data = new ArrayList<>();
    private List<Demand> orderData = new ArrayList<>();

    public DashboardViewModel() {
        data.add(new Driver());
        loadData();
    }

    public void loadData() {
        HTTPUtils.get("/auth/driver", new HTTPResult() {
            @Override
            public void onSuccess(JSONObject result) {
                System.out.println(result);
                data.clear();
                JSONArray data2 = result.getJSONArray("data");
                data.addAll(data2.toJavaList(Driver.class));
                mainActivity.runOnUiThread(() -> {
                    Toast.makeText(mainActivity, "获取订单数据成功", Toast.LENGTH_SHORT).show();
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

    public void setBinding(FragmentDashboardBinding binding) {
        DashboardViewModel.binding = binding;
        //设置适配器方式和以往不同
        binding.setAdapter(new DriverAdapter(mainActivity, data));//
//        binding.listview.setAdapter(binding.getAdp());
        //通过binding来设置点击长按事件
        binding.listview.setOnItemClickListener((parent, view, position, id) -> {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < orderData.size(); i++) {
                if (orderData.get(i).getState().equals("待接单"))   //只显示待接单的订单
                    list.add(orderData.get(i).getOrderID() + " " + (orderData.get(i).getDescription() == null ? "无货物信息" : orderData.get(i).getDescription()));
            }
            String[] arr = list.toArray(new String[0]);
            System.out.println(arr);
//                String[] array = new String[100];
//            String result;
            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
            builder.setTitle("请选择要推送的订单");
            builder.setItems(arr, (dialog, which) -> {
//                    String result = homeViewModel.getData()[which];
                dialog.dismiss();
            });
            builder.show();
//            Intent intent = new Intent(mainActivity, OrderDetailActivity.class);
//            intent.putExtra("orderID", data.get(position).getOrderID());
//            mainActivity.startActivity(intent);
//            System.out.println("点击了" + position);
//            System.out.println(data.get(position).getOrderID());
            //点击事件
        });
    }

    public static FragmentDashboardBinding getBinding() {
        return binding;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        DashboardViewModel.mainActivity = mainActivity;
    }

    public List<Driver> getData() {
        return data;
    }


    public List<Demand> getOrderData() {
        return orderData;
    }

    public void setOrderData(List<Demand> orderData) {
        this.orderData = orderData;
    }
}