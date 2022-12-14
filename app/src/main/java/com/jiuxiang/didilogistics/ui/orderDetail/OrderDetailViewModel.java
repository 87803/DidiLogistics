package com.jiuxiang.didilogistics.ui.orderDetail;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSONObject;
import com.jiuxiang.didilogistics.beans.OrderDetail;
import com.jiuxiang.didilogistics.ui.postDemand.PostDemandActivity;
import com.jiuxiang.didilogistics.utils.HTTPResult;
import com.jiuxiang.didilogistics.utils.HTTPUtils;

import java.util.Date;

public class OrderDetailViewModel extends ViewModel {
    private OrderDetailActivity orderDetailActivity;
    private MutableLiveData<OrderDetail> orderDetail;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            int arg1=msg.arg1;
//            String info= (String) msg.obj;
            if (msg.what == 1) {
//                System.out.println("更新列表");
                orderDetail.setValue((OrderDetail) msg.obj);
                if (((OrderDetail) msg.obj).getDriverName() == null) {
                    orderDetailActivity.binding.driverPhone.setVisibility(View.GONE);
                    orderDetailActivity.binding.driverName.setVisibility(View.GONE);
                }
                switch (((OrderDetail) msg.obj).getState()) {
                    case "待接单":
                    case "已接单":
                        break;
                    case "进行中":
                        orderDetailActivity.binding.btn.setText("请等待订单完成后支付");
                        orderDetailActivity.binding.btn.setEnabled(false);
                        break;
                    case "待支付":
                        orderDetailActivity.binding.btn.setText("支付");
                        break;
                    case "已完成":
                    case "已取消":
                        orderDetailActivity.binding.btn.setVisibility(View.GONE);
                        break;
                }
            }
        }
    };

    public OrderDetailViewModel() {
        orderDetail = new MutableLiveData<>();
    }

    public void loadData(String orderID) {
        HTTPUtils.get("/auth/order?orderID=" + orderID, new HTTPResult() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.getInteger("code") == 200) {
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result.getObject("data", OrderDetail.class);
                    handler.sendMessage(message);
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
