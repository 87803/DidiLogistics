package com.jiuxiang.didilogistics.ui.postDemand;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSONObject;
import com.jiuxiang.didilogistics.beans.App;
import com.jiuxiang.didilogistics.beans.PostDemand;
import com.jiuxiang.didilogistics.utils.HTTPResult;
import com.jiuxiang.didilogistics.utils.HTTPUtils;

public class PostDemandViewModel extends ViewModel {
    private PostDemandActivity postDemandActivity;

    private PostDemand postDemand;

    private String price;
    private String weight;
    private String length;
    private String deliverTime;
    private MutableLiveData<String> startPlaceProvince;
    private MutableLiveData<String> startPlaceCity;
    private MutableLiveData<String> startPlaceDistrict;
    private MutableLiveData<String> desPlaceProvince;
    private MutableLiveData<String> desPlaceCity;
    private MutableLiveData<String> desPlaceDistrict;

    public PostDemandViewModel() {
        postDemand = new PostDemand();
        startPlaceProvince = new MutableLiveData<>("北京市");
        startPlaceCity = new MutableLiveData<>("北京市");
        startPlaceDistrict = new MutableLiveData<>("海淀区");
        desPlaceProvince = new MutableLiveData<>("广东省");
        desPlaceCity = new MutableLiveData<>("广州市");
        desPlaceDistrict = new MutableLiveData<>("天河区");
    }

    public void onClickPostDemand() {
//        System.out.println(postDemand);
        try {
            postDemand.setPrice(Integer.parseInt(price));
            postDemand.setWeight(Double.parseDouble(weight));
            postDemand.setLength(Double.parseDouble(length));
            postDemand.setDeliverTime(deliverTime);
            postDemand.setStartPlaceProvince(startPlaceProvince.getValue());
            postDemand.setStartPlaceCity(startPlaceCity.getValue());
            postDemand.setStartPlaceDistrict(startPlaceDistrict.getValue());
            postDemand.setDesPlaceProvince(desPlaceProvince.getValue());
            postDemand.setDesPlaceCity(desPlaceCity.getValue());
            postDemand.setDesPlaceDistrict(desPlaceDistrict.getValue());
        } catch (Exception e) {
            postDemandActivity.runOnUiThread(() -> Toast.makeText(postDemandActivity, "输入有误，请检查输入", Toast.LENGTH_LONG).show());
            return;
        }


        HTTPUtils.post("/auth/demand", JSONObject.toJSONString(postDemand), new HTTPResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                postDemandActivity.runOnUiThread(() -> {
                    if (jsonObject.getInteger("code") == 200) {
                        Toast.makeText(postDemandActivity, "发布成功", Toast.LENGTH_LONG).show();
                        postDemandActivity.finish();

                        //通知主页刷新数据
                        Message message = Message.obtain();
                        message.what = 1;
                        App.getHomeFragmentHandler().sendMessage(message);
                    } else {
                        Toast.makeText(postDemandActivity, "发布失败，请检查输入，" + jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                postDemandActivity.runOnUiThread(() -> Toast.makeText(postDemandActivity, "提交失败，请重试，" + error, Toast.LENGTH_LONG).show());
            }
        });
    }

    public PostDemandActivity getPostDemandActivity() {
        return postDemandActivity;
    }

    public void setPostDemandActivity(PostDemandActivity postDemandActivity) {
        this.postDemandActivity = postDemandActivity;
    }

    public PostDemand getPostDemand() {
        return postDemand;
    }

    public void setPostDemand(PostDemand postDemand) {
        this.postDemand = postDemand;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(String deliverTime) {
        this.deliverTime = deliverTime;
    }


    public MutableLiveData<String> getStartPlaceProvince() {
        return startPlaceProvince;
    }

    public void setStartPlaceProvince(MutableLiveData<String> startPlaceProvince) {
        this.startPlaceProvince = startPlaceProvince;
    }

    public MutableLiveData<String> getStartPlaceCity() {
        return startPlaceCity;
    }

    public void setStartPlaceCity(MutableLiveData<String> startPlaceCity) {
        this.startPlaceCity = startPlaceCity;
    }

    public MutableLiveData<String> getStartPlaceDistrict() {
        return startPlaceDistrict;
    }

    public void setStartPlaceDistrict(MutableLiveData<String> startPlaceDistrict) {
        this.startPlaceDistrict = startPlaceDistrict;
    }

    public MutableLiveData<String> getDesPlaceProvince() {
        return desPlaceProvince;
    }

    public void setDesPlaceProvince(MutableLiveData<String> desPlaceProvince) {
        this.desPlaceProvince = desPlaceProvince;
    }

    public MutableLiveData<String> getDesPlaceCity() {
        return desPlaceCity;
    }

    public void setDesPlaceCity(MutableLiveData<String> desPlaceCity) {
        this.desPlaceCity = desPlaceCity;
    }

    public MutableLiveData<String> getDesPlaceDistrict() {
        return desPlaceDistrict;
    }

    public void setDesPlaceDistrict(MutableLiveData<String> desPlaceDistrict) {
        this.desPlaceDistrict = desPlaceDistrict;
    }
}
