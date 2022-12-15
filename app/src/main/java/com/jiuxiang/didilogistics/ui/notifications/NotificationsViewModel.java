package com.jiuxiang.didilogistics.ui.notifications;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuxiang.didilogistics.beans.Message;
import com.jiuxiang.didilogistics.databinding.FragmentNotificationsBinding;
import com.jiuxiang.didilogistics.ui.main.MainActivity;
import com.jiuxiang.didilogistics.ui.orderDetail.OrderDetailActivity;
import com.jiuxiang.didilogistics.utils.HTTPResult;
import com.jiuxiang.didilogistics.utils.HTTPUtils;

import java.util.ArrayList;
import java.util.List;

public class NotificationsViewModel extends ViewModel {

    private static FragmentNotificationsBinding binding;
    @SuppressLint("StaticFieldLeak")
    private static MainActivity mainActivity;

    private final List<Message> data = new ArrayList<>();

    public NotificationsViewModel() {
        loadData();
    }

    public void loadData() {
        HTTPUtils.get("/auth/message", new HTTPResult() {
            @Override
            public void onSuccess(JSONObject result) {
                System.out.println(result);
                data.clear();
                JSONArray data2 = result.getJSONArray("data");
                data.addAll(data2.toJavaList(Message.class));
                mainActivity.runOnUiThread(() -> {
                    binding.getAdapter().notifyDataSetChanged();
                    binding.noDataTextView.setVisibility(data.size() == 0 ? View.VISIBLE : View.GONE);
                    binding.listview.setVisibility(data.size() == 0 ? View.GONE : View.VISIBLE);
                });
            }

            @Override
            public void onFailure(String error) {
                mainActivity.runOnUiThread(() -> Toast.makeText(mainActivity, "获取消息数据失败，请重试，" + error, Toast.LENGTH_LONG).show());
            }
        });
    }

    public void setBinding(FragmentNotificationsBinding binding) {
        this.binding = binding;
        //设置适配器方式和以往不同
        binding.setAdapter(new MessageAdapter(mainActivity, data));
        //通过binding来设置点击长按事件
        binding.listview.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(mainActivity, OrderDetailActivity.class);
            intent.putExtra("orderID", data.get(position).getOrderID());
            mainActivity.startActivity(intent);
        });
    }

    public static void setMainActivity(MainActivity mainActivity) {
        NotificationsViewModel.mainActivity = mainActivity;
//        mainActivity.runOnUiThread(() -> {
//            binding.noDataTextView.setVisibility(data.size() == 0 ? View.VISIBLE : View.GONE);
//            binding.listview.setVisibility(data.size() == 0 ? View.GONE : View.VISIBLE);
//        });
    }

    public FragmentNotificationsBinding getBinding() {
        return binding;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

}