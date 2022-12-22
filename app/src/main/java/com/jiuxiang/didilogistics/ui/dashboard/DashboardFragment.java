package com.jiuxiang.didilogistics.ui.dashboard;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jiuxiang.didilogistics.BR;
import com.jiuxiang.didilogistics.R;
import com.jiuxiang.didilogistics.adapter.DemandAdapter;
import com.jiuxiang.didilogistics.adapter.DriverAdapter;
import com.jiuxiang.didilogistics.databinding.FragmentDashboardBinding;
import com.jiuxiang.didilogistics.databinding.FragmentDashboardDriverBinding;
import com.jiuxiang.didilogistics.ui.home.HomeViewModel;
import com.jiuxiang.didilogistics.ui.orderDetail.OrderDetailActivity;
import com.jiuxiang.didilogistics.utils.App;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.lljjcoder.style.citypickerview.CityPickerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding fragmentDashboardBinding;
    private FragmentDashboardDriverBinding fragmentDashboardDriverBinding;
    private DashboardDriverViewModel dashboardDriverViewModel;
    private DashboardViewModel dashboardViewModel;

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String info = (String) msg.obj;
            if (msg.what == 1) {
                System.out.println("更新列表");
                fragmentDashboardDriverBinding.getAdapter().notifyDataSetChanged();
                Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
                fragmentDashboardDriverBinding.noDataTextview.setVisibility(dashboardDriverViewModel.getData().size() == 0 ? View.VISIBLE : View.GONE);
                fragmentDashboardDriverBinding.listview.setVisibility(dashboardDriverViewModel.getData().size() == 0 ? View.GONE : View.VISIBLE);
            } else if (msg.what == 3) {
                Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
                fragmentDashboardBinding.getAdapter().notifyDataSetChanged();
                fragmentDashboardBinding.noDataTextview.setVisibility(dashboardViewModel.getData().size() == 0 ? View.VISIBLE : View.GONE);
                fragmentDashboardBinding.listview.setVisibility(dashboardViewModel.getData().size() == 0 ? View.GONE : View.VISIBLE);
            } else if (msg.what == 2) {
                Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
            }
        }
    };


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        App.setDashboardFragmentHandler(handler);

        if (App.getUser().isType()) { //司机端界面，显示司机订单
            dashboardDriverViewModel = new ViewModelProvider(this).get(DashboardDriverViewModel.class);

            fragmentDashboardDriverBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard_driver, container, false);
            View root = fragmentDashboardDriverBinding.getRoot();

            fragmentDashboardDriverBinding.setVariable(BR.DashboardDriverViewModel, dashboardDriverViewModel);
            fragmentDashboardDriverBinding.setLifecycleOwner(getViewLifecycleOwner());

            fragmentDashboardDriverBinding.noDataTextview.setVisibility(dashboardDriverViewModel.getData().size() == 0 ? View.VISIBLE : View.GONE);
            fragmentDashboardDriverBinding.listview.setVisibility(dashboardDriverViewModel.getData().size() == 0 ? View.GONE : View.VISIBLE);

            //设置适配器方式和以往不同
            fragmentDashboardDriverBinding.setAdapter(new DemandAdapter(getActivity(), dashboardDriverViewModel.getData()));//
//        binding.listview.setAdapter(binding.getAdp());
            //通过binding来设置点击长按事件
            fragmentDashboardDriverBinding.listview.setOnItemClickListener((parent, view, position, id) -> {
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                intent.putExtra("orderID", dashboardDriverViewModel.getData().get(position).getOrderID());
                getActivity().startActivity(intent);
                //点击事件
            });


            return root;
        } else {  //货主端界面，显示在线司机

            dashboardViewModel =
                    new ViewModelProvider(this).get(DashboardViewModel.class);

//            binding = FragmentDashboardBinding.inflate(inflater, container, false);
//            View root = binding.getRoot();

//return inflater.inflate(R.layout.fragment_home, container, false);

            fragmentDashboardBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
            View root = fragmentDashboardBinding.getRoot();
            fragmentDashboardBinding.setVariable(BR.DashboardViewModel, dashboardViewModel);
            System.out.println("DashboardViewModel:" + dashboardViewModel);
            //传入订单列表
            HomeViewModel homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
//            dashboardViewModel.setOrderData(homeViewModel.getData());//用户的订单信息
//            System.out.println("homeViewModel.getData():" + homeViewModel.getData());

            fragmentDashboardBinding.noDataTextview.setVisibility(dashboardViewModel.getData().size() == 0 ? View.VISIBLE : View.GONE);
            fragmentDashboardBinding.listview.setVisibility(dashboardViewModel.getData().size() == 0 ? View.GONE : View.VISIBLE);

            fragmentDashboardBinding.setLifecycleOwner(getViewLifecycleOwner());

            //设置适配器方式和以往不同
            fragmentDashboardBinding.setAdapter(new DriverAdapter(getActivity(), dashboardViewModel.getData()));//
//        binding.listview.setAdapter(binding.getAdp());

            fragmentDashboardBinding.noDataTextview.setText("暂无符合要求的司机");
            //通过binding来设置点击长按事件
            fragmentDashboardBinding.listview.setOnItemClickListener((parent, view, position, id) -> {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < homeViewModel.getData().size(); i++) {
                    if (homeViewModel.getData().get(i).getState().equals("待接单"))   //只显示待接单的订单
                        list.add(homeViewModel.getData().get(i).getOrderID() + " " + (homeViewModel.getData().get(i).getDescription() == null ? "无货物信息" : homeViewModel.getData().get(i).getDescription()));
                }
                String[] arr = list.toArray(new String[0]);
                System.out.println(arr);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("请选择要推送的订单");
                builder.setItems(arr, (dialog, which) -> {
                    String orderID = arr[which].split(" ")[0];
                    dashboardViewModel.pushOrder(orderID, position);

                    dialog.dismiss();
                });
                builder.show();
                //点击事件
            });
            fragmentDashboardBinding.help.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("提示");
                builder.setMessage("系统只会显示货车长度和载重符合您要求的司机，如果没有符合要求的司机，您可以重新发布订单，系统会自动推送符合要求的司机。\n若您为新注册用户，请先完善个人信息，以免影响后续使用。");
                builder.setPositiveButton("我知道了", (dialog, which) -> dialog.dismiss());
                builder.show();
            });

            CityPickerView mPicker = new CityPickerView();
            //预先加载仿iOS滚轮实现的全部数据
            mPicker.init(requireActivity());
            //添加默认的配置，不需要自己定义，当然也可以自定义相关熟悉，详细属性请看demo
            CityConfig cityConfig = new CityConfig.Builder().build();
            mPicker.setConfig(cityConfig);

            fragmentDashboardBinding.startPlace.setOnClickListener((v) -> {
//监听选择点击事件及返回结果
                mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
                    @Override
                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                        if (!Objects.equals(city.getName(), "市辖区") && !Objects.equals(city.getName(), "县")) {
                            dashboardViewModel.getDriverStartPlaceCity().setValue(city.getName());
                        } else
                            dashboardViewModel.getDriverStartPlaceCity().setValue(province.getName());
                    }

                    @Override
                    public void onCancel() {
                        ToastUtils.showLongToast(requireActivity(), "已取消");
                    }
                });
                //显示
                mPicker.showCityPicker();
            });
            fragmentDashboardBinding.desPlace.setOnClickListener((v) -> {
//监听选择点击事件及返回结果
                mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
                    @Override
                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                        if (!Objects.equals(city.getName(), "市辖区") && !Objects.equals(city.getName(), "县")) {
                            dashboardViewModel.getDriverEndPlaceCity().setValue(city.getName());
                        } else
                            dashboardViewModel.getDriverEndPlaceCity().setValue(province.getName());
                    }

                    @Override
                    public void onCancel() {
                        ToastUtils.showLongToast(requireActivity(), "已取消");
                    }
                });
                //显示
                mPicker.showCityPicker();
            });
            return root;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }
}