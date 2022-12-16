package com.jiuxiang.didilogistics.ui.orderDetail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.jiuxiang.didilogistics.BR;
import com.jiuxiang.didilogistics.R;
import com.jiuxiang.didilogistics.alipay.PayDemoActivity;
import com.jiuxiang.didilogistics.databinding.ActivityOrderDetailBinding;
import com.jiuxiang.didilogistics.utils.App;

public class OrderDetailActivity extends AppCompatActivity {
    private OrderDetailViewModel orderDetailViewModel;
    public ActivityOrderDetailBinding binding;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0)//支付成功
                orderDetailViewModel.updateOrder(0);
            if (msg.what == 1) {
                if (orderDetailViewModel.getOrderDetail().getValue().getDriverName() == null) {
                    binding.driverPhone.setVisibility(View.GONE);
                    binding.driverName.setVisibility(View.GONE);
                }
                if (!App.getUser().isType()) {
                    switch (orderDetailViewModel.getOrderDetail().getValue().getState()) {
                        case "待接单":
                        case "已接单":
                            break;
                        case "进行中":
                            binding.btn.setText("请等待订单完成后支付");
                            binding.btn.setEnabled(false);
                            break;
                        case "待支付":
                            binding.btn.setText("支付订单");
                            break;
                        case "已完成":
                        case "已取消":
                            binding.btn.setVisibility(View.GONE);
                            break;
                    }
                } else {
                    switch (orderDetailViewModel.getOrderDetail().getValue().getState()) {
                        case "待接单":
                            binding.btn.setText("接单");
                            break;
                        case "已接单":
                            binding.btn.setText("开始运输");
                            break;
                        case "进行中":
                            binding.btn.setText("完成订单");
                            break;
                        case "待支付":
                            binding.btn.setText("已通知用户支付");
                            binding.btn.setEnabled(false);
                            break;
                        case "已完成":
                        case "已取消":
                            binding.btn.setVisibility(View.GONE);
                            break;
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.setOrderDetailHandler(handler);
//        setContentView(R.layout.activity_order_detail);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);
        orderDetailViewModel = new ViewModelProvider(this).get(OrderDetailViewModel.class);
        orderDetailViewModel.setOrderDetailActivity(this);
        orderDetailViewModel.loadData(getIntent().getStringExtra("orderID"));
        binding.setVariable(BR.orderDetailViewModel, orderDetailViewModel);
        binding.setLifecycleOwner(this);

        binding.btn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            if (binding.btn.getText().equals("支付订单")) {
                builder.setMessage("是否确认支付？");
                builder.setPositiveButton("确定", (dialog, which) -> {
                    Intent intent = new Intent(this, PayDemoActivity.class);
                    intent.putExtra("orderID", orderDetailViewModel.getOrderDetail().getValue().getOrderID());
                    intent.putExtra("money", orderDetailViewModel.getOrderDetail().getValue().getPrice());
                    intent.putExtra("description", orderDetailViewModel.getOrderDetail().getValue().getDescription());
                    startActivity(intent);
                });
            } else if (binding.btn.getText().equals("取消订单")) {
                builder.setMessage("是否确认取消订单？");
                builder.setPositiveButton("确定", (dialog, which) -> {
                    orderDetailViewModel.updateOrder(1);
                });
            } else if (binding.btn.getText().equals("接单")) {
                builder.setMessage("是否确认接单？");
                builder.setPositiveButton("确定", (dialog, which) -> {
                    orderDetailViewModel.updateOrder(2);
                });
            } else if (binding.btn.getText().equals("开始运输")) {
                builder.setMessage("是否确认开始运输？");
                builder.setPositiveButton("确定", (dialog, which) -> {
                    orderDetailViewModel.updateOrder(3);
                });
            } else if (binding.btn.getText().equals("完成订单")) {
                builder.setMessage("是否确认完成订单？");
                builder.setPositiveButton("确定", (dialog, which) -> {
                    orderDetailViewModel.updateOrder(4);
                });
            }
            builder.setNegativeButton("取消", (dialog, which) -> {
            });
            builder.show();

        });
    }
}