package com.jiuxiang.didilogistics.ui.home;

import android.os.Message;

import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuxiang.didilogistics.beans.Demand;
import com.jiuxiang.didilogistics.utils.App;
import com.jiuxiang.didilogistics.utils.HTTPResult;
import com.jiuxiang.didilogistics.utils.HTTPUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private final List<Demand> data = new ArrayList<>();

    public HomeViewModel() {
        loadData();
    }

    public void loadData() {
        HTTPUtils.get("/auth/demand", new HTTPResult() {
            @Override
            public void onSuccess(JSONObject result) {
                System.out.println(result);
                JSONArray data2 = result.getJSONArray("data");
                data.clear();
                data.addAll(data2.toJavaList(Demand.class));
                Message message = App.getHomeFragmentHandler().obtainMessage(1);
                message.obj = "获取订单数据成功";
                App.getHomeFragmentHandler().sendMessage(message);
            }

            @Override
            public void onFailure(String error) {
                Message message = App.getHomeFragmentHandler().obtainMessage(1);
                message.obj = "获取订单数据失败";
                App.getHomeFragmentHandler().sendMessage(message);
            }
        });
    }

    public List<Demand> getData() {
        return data;
    }
}