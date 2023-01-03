package com.jiuxiang.didilogistics.ui.dashboard;

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

//司机端界面对应的ViewModel
//获取司机的订单列表
public class DashboardDriverViewModel extends ViewModel {
    private final List<Demand> data = new ArrayList<>();

    public DashboardDriverViewModel() {
        loadData();
    }

    //获取司机的订单列表
    public void loadData() {
        HTTPUtils.get("/auth/demand?order=true", new HTTPResult() {
            @Override
            public void onSuccess(JSONObject result) {
                data.clear();
                JSONArray data2 = result.getJSONArray("data");
                data.addAll(data2.toJavaList(Demand.class));
                Message message = App.getDashboardFragmentHandler().obtainMessage(1);
                message.obj = "获取订单数据成功";
                App.getDashboardFragmentHandler().sendMessage(message);
            }

            @Override
            public void onFailure(String error) {
                Message message = App.getDashboardFragmentHandler().obtainMessage(1);
                message.obj = "获取订单数据失败，请重试，" + error;
                App.getDashboardFragmentHandler().sendMessage(message);
            }
        });
    }


    public List<Demand> getData() {
        return data;
    }
}