package com.jiuxiang.didilogistics.ui.orderDetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.jiuxiang.didilogistics.BR;
import com.jiuxiang.didilogistics.R;
import com.jiuxiang.didilogistics.alipay.PayDemoActivity;
import com.jiuxiang.didilogistics.databinding.ActivityOrderDetailBinding;
import com.jiuxiang.didilogistics.databinding.ActivityPostDemandBinding;
import com.jiuxiang.didilogistics.ui.postDemand.PostDemandViewModel;

public class OrderDetailActivity extends AppCompatActivity {
    private OrderDetailViewModel orderDetailViewModel;
    public ActivityOrderDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_order_detail);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);
        orderDetailViewModel = new ViewModelProvider(this).get(OrderDetailViewModel.class);
        orderDetailViewModel.setOrderDetailActivity(this);
        orderDetailViewModel.loadData(getIntent().getStringExtra("orderID"));
        binding.setVariable(BR.orderDetailViewModel, orderDetailViewModel);
        binding.setLifecycleOwner(this);
        System.out.println("接受的数据" + getIntent().getStringExtra("orderID"));
    }
}