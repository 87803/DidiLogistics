package com.jiuxiang.didilogistics.ui.orderDetail;

import android.os.Message;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSONObject;
import com.jiuxiang.didilogistics.beans.App;
import com.jiuxiang.didilogistics.beans.OrderDetail;
import com.jiuxiang.didilogistics.utils.HTTPResult;
import com.jiuxiang.didilogistics.utils.HTTPUtils;

public class OrderDetailViewModel extends ViewModel {
    private OrderDetailActivity orderDetailActivity;
    private MutableLiveData<OrderDetail> orderDetail;


    public OrderDetailViewModel() {
        orderDetail = new MutableLiveData<>();
    }

    public void loadData(String orderID) {
        HTTPUtils.get("/auth/order?orderID=" + orderID, new HTTPResult() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.getInteger("code") == 200) {
                    OrderDetail orderDetailData = result.getObject("data", OrderDetail.class);
                    System.out.println(orderDetailData.getDeliverTime());
                    orderDetailActivity.runOnUiThread(() -> {
                        orderDetail.setValue(orderDetailData);
                    });
                    Message message = new Message();
                    message.what = 1;
                    App.getOrderDetailHandler().sendMessage(message);
                } else {
                    orderDetailActivity.runOnUiThread(() -> {
                        Toast.makeText(orderDetailActivity, "获取订单详情失败，" + result.getString("msg"), Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(String error) {
                orderDetailActivity.runOnUiThread(() -> {
                    Toast.makeText(orderDetailActivity, "获取信息失败，请稍后再试，" + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    public void updateOrder(int type) {
        //0:支付 1:取消
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderID", orderDetail.getValue().getOrderID());
        jsonObject.put("type", type);
        HTTPUtils.post("/auth/order", jsonObject.toJSONString(), new HTTPResult() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.getInteger("code") == 200) {
                    orderDetailActivity.runOnUiThread(() -> {
                        Toast.makeText(orderDetailActivity, "操作成功", Toast.LENGTH_SHORT).show();
                        loadData(orderDetail.getValue().getOrderID()); //重新加载数据
                        Message message = new Message();
                        message.what = 1;
                        App.getHomeFragmentHandler().sendMessage(message);//刷新首页数据
                    });
                } else {
                    orderDetailActivity.runOnUiThread(() -> {
                        Toast.makeText(orderDetailActivity, "操作失败，" + result.getString("msg"), Toast.LENGTH_LONG).show();
                    });
                }
            }

            @Override
            public void onFailure(String error) {
                orderDetailActivity.runOnUiThread(() -> {
                    Toast.makeText(orderDetailActivity, "操作失败，请稍后再试，" + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    public OrderDetailActivity getOrderDetailActivity() {
        return orderDetailActivity;
    }

    public void setOrderDetailActivity(OrderDetailActivity orderDetailActivity) {
        this.orderDetailActivity = orderDetailActivity;
    }

    public MutableLiveData<OrderDetail> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(MutableLiveData<OrderDetail> orderDetail) {
        this.orderDetail = orderDetail;
    }
}
