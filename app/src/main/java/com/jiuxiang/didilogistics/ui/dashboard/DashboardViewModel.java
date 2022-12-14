package com.jiuxiang.didilogistics.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jiuxiang.didilogistics.beans.Demand;
import com.jiuxiang.didilogistics.beans.Driver;
import com.jiuxiang.didilogistics.databinding.FragmentDashboardBinding;
import com.jiuxiang.didilogistics.databinding.FragmentDashboardDriverBinding;
import com.jiuxiang.didilogistics.ui.MainActivity;
import com.jiuxiang.didilogistics.ui.home.DemandAdapter;
import com.jiuxiang.didilogistics.ui.orderDetail.OrderDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends ViewModel {
    @SuppressLint("StaticFieldLeak")
    private static FragmentDashboardBinding binding;
    @SuppressLint("StaticFieldLeak")
    private static MainActivity mainActivity;
    private final List<Driver> data = new ArrayList<>();
    private List<Demand> orderData = new ArrayList<>();
    private final MutableLiveData<String> mText;

    public DashboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
        data.add(new Driver());

    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setBinding(FragmentDashboardBinding binding) {
        DashboardViewModel.binding = binding;
        //设置适配器方式和以往不同
        binding.setAdapter(new DriverAdapter(mainActivity, data));//
//        binding.listview.setAdapter(binding.getAdp());
        //通过binding来设置点击长按事件
        binding.listview.setOnItemClickListener((parent, view, position, id) -> {
            String[] arr = new String[orderData.size()];
            for (int i = 0; i < orderData.size(); i++) {
                if (orderData.get(i).getState().equals("待接单"))   //只显示待接单的订单
                    arr[i] = orderData.get(i).getOrderID() + " " + (orderData.get(i).getDescription() == null ? "无货物信息" : orderData.get(i).getDescription());
            }
//                String[] array = new String[100];
//            String result;
            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity).setTitle("请选择要推送的订单");
            builder.setSingleChoiceItems(arr, 0, (dialog, which) -> {
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

    public MutableLiveData<String> getmText() {
        return mText;
    }

    public List<Demand> getOrderData() {
        return orderData;
    }

    public void setOrderData(List<Demand> orderData) {
        this.orderData = orderData;
    }
}