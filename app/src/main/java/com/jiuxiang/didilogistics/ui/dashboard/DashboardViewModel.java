package com.jiuxiang.didilogistics.ui.dashboard;

import android.os.Message;

import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuxiang.didilogistics.beans.Driver;
import com.jiuxiang.didilogistics.utils.App;
import com.jiuxiang.didilogistics.utils.HTTPResult;
import com.jiuxiang.didilogistics.utils.HTTPUtils;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends ViewModel {
    private final List<Driver> data = new ArrayList<>();

    public DashboardViewModel() {
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
                Message message = App.getDashboardFragmentHandler().obtainMessage(3);
                message.obj = "获取司机列表数据成功";
                App.getDashboardFragmentHandler().sendMessage(message);
            }

            @Override
            public void onFailure(String error) {
                Message message = App.getDashboardFragmentHandler().obtainMessage(3);
                message.obj = "获取司机列表数据失败，请重试，" + error;
                App.getDashboardFragmentHandler().sendMessage(message);
            }
        });
    }

    public void pushOrder(String orderID, int position) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("driverID", getData().get(position).getUserID());
        jsonObject.put("orderID", orderID);
        jsonObject.put("type", 5);
        HTTPUtils.post("/auth/order", jsonObject.toJSONString(), new HTTPResult() {
            @Override
            public void onSuccess(JSONObject result) {
                Message message = App.getDashboardFragmentHandler().obtainMessage(2);
                if (result.getInteger("code") == 200) {
                    message.obj = "推送成功";
                } else {
                    message.obj = result.getString("msg");
                }
                App.getDashboardFragmentHandler().sendMessage(message);
            }

            @Override
            public void onFailure(String error) {
                Message message = App.getDashboardFragmentHandler().obtainMessage(2);
                message.obj = "推送失败，请重试，" + error;
                App.getDashboardFragmentHandler().sendMessage(message);
            }
        });
    }

    public List<Driver> getData() {
        return data;
    }
}