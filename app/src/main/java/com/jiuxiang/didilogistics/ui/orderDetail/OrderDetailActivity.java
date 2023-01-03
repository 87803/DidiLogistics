package com.jiuxiang.didilogistics.ui.orderDetail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.jiuxiang.didilogistics.BR;
import com.jiuxiang.didilogistics.R;
import com.jiuxiang.didilogistics.alipay.PayDemoActivity;
import com.jiuxiang.didilogistics.beans.OrderDetail;
import com.jiuxiang.didilogistics.databinding.ActivityOrderDetailBinding;
import com.jiuxiang.didilogistics.utils.App;
import com.jiuxiang.didilogistics.utils.PriceUtils;

//订单详情界面，用于显示订单详情
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
                //判断是否有司机接单，如果有则显示司机信息，否则不显示
                if (orderDetailViewModel.getOrderDetail().getValue().getDriverName() == null) {
                    binding.driverPhone.setVisibility(View.GONE);
                    binding.driverName.setVisibility(View.GONE);
                }
                //根据用户类型和订单状态显示不同的按钮
                if (!App.getUser().isType()) {
                    switch (orderDetailViewModel.getOrderDetail().getValue().getState()) {
                        case "待接单":
                            break;
                        case "已接单":
                            binding.cvBtn2.setVisibility(View.GONE);
                            break;
                        case "进行中":
                            binding.cvBtn2.setVisibility(View.GONE);
                            binding.btn.setText("请等待订单完成后支付");
                            binding.btn.setEnabled(false);
                            break;
                        case "待支付":
                            binding.cvBtn2.setVisibility(View.GONE);
                            binding.btn.setText("支付订单");
                            break;
                        case "已完成":
                        case "已取消":
                            binding.cvBtn2.setVisibility(View.GONE);
                            binding.cvBtn1.setVisibility(View.GONE);
                            break;
                    }
                } else {
                    binding.cvBtn2.setVisibility(View.GONE);
                    binding.goStart.setVisibility(View.VISIBLE);
                    binding.goDes.setVisibility(View.VISIBLE);
                    binding.ivStartHelp.setVisibility(View.GONE);
                    binding.ivDesHelp.setVisibility(View.GONE);
                    switch (orderDetailViewModel.getOrderDetail().getValue().getState()) {
                        case "待接单":
                            binding.btn.setText("接单");
                            binding.goStart.setVisibility(View.GONE);
                            binding.goDes.setVisibility(View.GONE);
                            binding.ivStartHelp.setVisibility(View.VISIBLE);
                            binding.ivDesHelp.setVisibility(View.VISIBLE);
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
                            binding.cvBtn1.setVisibility(View.GONE);
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

        orderDetailViewModel = new ViewModelProvider(this).get(OrderDetailViewModel.class);
        orderDetailViewModel.setOrderDetailActivity(this);
        orderDetailViewModel.loadData(getIntent().getStringExtra("orderID"));

        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);
        binding.setVariable(BR.orderDetailViewModel, orderDetailViewModel);
        binding.setLifecycleOwner(this);

        binding.goDes.setOnClickListener(v -> {
            OrderDetail orderDetail = orderDetailViewModel.getOrderDetail().getValue();
            assert orderDetail != null;
            goAmap(orderDetail.getDesPlaceProvince() + "省" + orderDetail.getDesPlaceCity() + "市" + orderDetail.getDesPlaceDistrict() + "区" + orderDetail.getDesPlaceDetail());
        });
        binding.goStart.setOnClickListener(v -> {
            OrderDetail orderDetail = orderDetailViewModel.getOrderDetail().getValue();
            assert orderDetail != null;
            goAmap(orderDetail.getStartPlaceProvince() + "省" + orderDetail.getStartPlaceCity() + "市" + orderDetail.getStartPlaceDistrict() + "区" + orderDetail.getStartPlaceDetail());
        });

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
        binding.ivPriceHelp.setOnClickListener((v) -> {
            new AlertDialog.Builder(this)
                    .setTitle("价格说明")
                    .setMessage("系统建议价格是系统根据两地距离以及货物的重量和体积，" +
                            "选取合适的车型并根据当前市场均价计算得出，" +
                            "您提供的预期价格不得低于此价格，价格越高，接单几率越大。" +
                            "\n--------------------------------------\n当前价格计算规则：\n" +
                            "车型：" + PriceUtils.ITEM[0].length + "米货车，起步价：" + PriceUtils.ITEM[0].flagFallPrice + "元(" + PriceUtils.ITEM[0].flagFallPrice_distance + "公里)，超出后每公里：" + PriceUtils.ITEM[0].price + "元\n" +
                            "车型：" + PriceUtils.ITEM[1].length + "米货车，起步价：" + PriceUtils.ITEM[1].flagFallPrice + "元(" + PriceUtils.ITEM[1].flagFallPrice_distance + "公里)，超出后每公里：" + PriceUtils.ITEM[1].price + "元\n" +
                            "车型：" + PriceUtils.ITEM[2].length + "米货车，起步价：" + PriceUtils.ITEM[2].flagFallPrice + "元(" + PriceUtils.ITEM[2].flagFallPrice_distance + "公里)，超出后每公里：" + PriceUtils.ITEM[2].price + "元\n" +
                            "车型：" + PriceUtils.ITEM[3].length + "米货车，起步价：" + PriceUtils.ITEM[3].flagFallPrice + "元(" + PriceUtils.ITEM[3].flagFallPrice_distance + "公里)，超出后每公里：" + PriceUtils.ITEM[3].price + "元\n" +
                            "车型：" + PriceUtils.ITEM[4].length + "米货车，起步价：" + PriceUtils.ITEM[4].flagFallPrice + "元(" + PriceUtils.ITEM[4].flagFallPrice_distance + "公里)，超出后每公里：" + PriceUtils.ITEM[4].price + "元\n" +
                            "--------------------------------------\n您可以在车主接单前，与车主协商最终的价格，并在平台上修改价格，接单后，不支持修改价格，" +
                            "为保证订单安全，请勿在平台外交易，如有疑问，请联系客服。")
                    .setPositiveButton("我知道了", null)
                    .show();
        });
        binding.btn2.setOnClickListener((v) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("修改价格");
            builder.setMessage("请输入新价格，注意不得低于最低价格");
            EditText editText = new EditText(this);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(editText);
            builder.setPositiveButton("确定", (dialog, which) -> {
                if (editText.getText().toString().equals("")) {
                    Toast.makeText(this, "请输入价格", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Double.parseDouble(editText.getText().toString()) < Double.parseDouble(orderDetailViewModel.getOrderDetail().getValue().getRecommendPrice().substring(0, orderDetailViewModel.getOrderDetail().getValue().getRecommendPrice().length() - 1))) {
                    Toast.makeText(this, "价格不得低于最低价格", Toast.LENGTH_SHORT).show();
                    return;
                }
                orderDetailViewModel.updateOrder(6, editText.getText().toString());
            });
            builder.setNegativeButton("取消", (dialog, which) -> {
            });
            builder.show();
        });
        binding.ivStartHelp.setOnClickListener((v) -> {
            new AlertDialog.Builder(this)
                    .setTitle("地址说明")
                    .setMessage("为保护货主隐私，司机用户在未接单时，不能查看详细地址。确认接单后，才能查看详细地址。")
                    .setPositiveButton("我知道了", null)
                    .show();
        });
        binding.ivDesHelp.setOnClickListener((v) -> {
            new AlertDialog.Builder(this)
                    .setTitle("地址说明")
                    .setMessage("为保护货主隐私，司机用户在未接单时，不能查看详细地址。确认接单后，才能查看详细地址。")
                    .setPositiveButton("我知道了", null)
                    .show();
        });
    }

    //跳转到高德地图
    private void goAmap(String location) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        //将功能Scheme以URI的方式传入data
        Uri uri = Uri.parse("androidamap://poi?sourceApplication=softname&keywords=" + location + "&dev=0");
        intent.setData(uri);
        //启动该页面即可
        startActivity(intent);
    }
}