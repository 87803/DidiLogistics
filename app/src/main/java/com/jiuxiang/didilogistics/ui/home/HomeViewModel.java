package com.jiuxiang.didilogistics.ui.home;

import android.os.Message;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuxiang.didilogistics.beans.Demand;
import com.jiuxiang.didilogistics.utils.App;
import com.jiuxiang.didilogistics.utils.HTTPResult;
import com.jiuxiang.didilogistics.utils.HTTPUtils;

import java.util.ArrayList;
import java.util.List;

//首页对应的ViewModel，获取首页的订单列表
public class HomeViewModel extends ViewModel {
    private final List<Demand> data = new ArrayList<>();

    private MutableLiveData<String> driverStartPlaceCity;
    private MutableLiveData<String> driverEndPlaceCity;

    public HomeViewModel() {
        driverEndPlaceCity = new MutableLiveData<>();
        driverStartPlaceCity = new MutableLiveData<>();
        loadData();
    }

    //获取首页的订单列表
    public void loadData() {
        HTTPUtils.get("/auth/demand?start=" + driverStartPlaceCity.getValue() + "&end=" + driverEndPlaceCity.getValue(), new HTTPResult() {
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

    //取消筛选
    public void resetData() {
        driverEndPlaceCity.setValue(null);
        driverStartPlaceCity.setValue(null);
        loadData();
    }

    public List<Demand> getData() {
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