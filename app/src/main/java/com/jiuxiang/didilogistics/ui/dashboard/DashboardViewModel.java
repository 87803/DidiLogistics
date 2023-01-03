package com.jiuxiang.didilogistics.ui.dashboard;

import android.os.Message;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuxiang.didilogistics.beans.Driver;
import com.jiuxiang.didilogistics.utils.App;
import com.jiuxiang.didilogistics.utils.HTTPResult;
import com.jiuxiang.didilogistics.utils.HTTPUtils;

import java.util.ArrayList;
import java.util.List;

//货主端界面对应的ViewModel
//获取在线司机列表，向司机推送订单
public class DashboardViewModel extends ViewModel {
    private final List<Driver> data = new ArrayList<>();
    private MutableLiveData<String> driverStartPlaceCity;
    private MutableLiveData<String> driverEndPlaceCity;

    public DashboardViewModel() {
        driverStartPlaceCity = new MutableLiveData<>();
        driverEndPlaceCity = new MutableLiveData<>();
        loadData();
    }

    //获取在线司机列表
    public void loadData() {
        HTTPUtils.get("/auth/driver?start=" + driverStartPlaceCity.getValue() + "&end=" + driverEndPlaceCity.getValue(), new HTTPResult() {
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

    //取消筛选
    public void resetData() {
        driverEndPlaceCity.setValue(null);
        driverStartPlaceCity.setValue(null);
        loadData();
    }

    //向司机推送订单
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

    public MutableLiveData<String> getDriverStartPlaceCity() {
        return driverStartPlaceCity;
    }

    public void setDriverStartPlaceCity(MutableLiveData<String> driverStartPlaceCity) {
        this.driverStartPlaceCity = driverStartPlaceCity;
    }

    public MutableLiveData<String> getDriverEndPlaceCity() {
        return driverEndPlaceCity;
    }

    public void setDriverEndPlaceCity(MutableLiveData<String> driverEndPlaceCity) {
        this.driverEndPlaceCity = driverEndPlaceCity;
    }
}