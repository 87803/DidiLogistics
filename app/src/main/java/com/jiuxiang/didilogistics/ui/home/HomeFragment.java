package com.jiuxiang.didilogistics.ui.home;

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

import com.jiuxiang.didilogistics.R;
import com.jiuxiang.didilogistics.adapter.DemandAdapter;
import com.jiuxiang.didilogistics.databinding.FragmentHomeBinding;
import com.jiuxiang.didilogistics.ui.orderDetail.OrderDetailActivity;
import com.jiuxiang.didilogistics.ui.postDemand.PostDemandActivity;
import com.jiuxiang.didilogistics.utils.App;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.lljjcoder.style.citypickerview.CityPickerView;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String info = (String) msg.obj;
            if (msg.what == 1 && binding != null) { //有来自order detail的消息，需判断是否为null
                System.out.println("更新列表");
                binding.getAdapter().notifyDataSetChanged();
                Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
                binding.noDataTextview.setVisibility(homeViewModel.getData().size() == 0 ? View.VISIBLE : View.GONE);
                binding.listview.setVisibility(homeViewModel.getData().size() == 0 ? View.GONE : View.VISIBLE);
            } else if (msg.what == 2 && binding != null) {
                homeViewModel.loadData();
            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        App.setHomeFragmentHandler(handler);


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View root = binding.getRoot();

        binding.setVariable(com.jiuxiang.didilogistics.BR.homeViewModel, homeViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.noDataTextview.setVisibility(homeViewModel.getData().size() == 0 ? View.VISIBLE : View.GONE);
        binding.listview.setVisibility(homeViewModel.getData().size() == 0 ? View.GONE : View.VISIBLE);
        //设置适配器方式和以往不同
        binding.setAdapter(new DemandAdapter(getActivity(), homeViewModel.getData()));//
//        binding.listview.setAdapter(binding.getAdp());
        //通过binding来设置点击长按事件
        binding.listview.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
            intent.putExtra("orderID", homeViewModel.getData().get(position).getOrderID());
            requireActivity().startActivity(intent);
            System.out.println("点击了" + position);

            //点击事件
        });

        if (App.getUser().isType()) {
//            binding.fab.setVisibility(View.GONE);
            //设置图片为@android:drawable/ic_menu_help
            binding.fab.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_help));
            binding.fab.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("提示");
                builder.setMessage("系统只会推送货物长度和重量符合您货车长度和载重要求的订单，如果没有符合要求的订单，您可以尝试修改相关资料，然后刷新再试。\n若您为新注册用户，请先完善个人信息，以免影响后续使用。");
                builder.setPositiveButton("我知道了", (dialog, which) -> dialog.dismiss());
                builder.show();
            });

            CityPickerView mPicker = new CityPickerView();
            //预先加载仿iOS滚轮实现的全部数据
            mPicker.init(requireActivity());
            //添加默认的配置，不需要自己定义，当然也可以自定义相关熟悉，详细属性请看demo
            CityConfig cityConfig = new CityConfig.Builder().build();
            mPicker.setConfig(cityConfig);

            binding.startPlace.setOnClickListener((v) -> {
//监听选择点击事件及返回结果
                mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
                    @Override
                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                        if (!Objects.equals(city.getName(), "市辖区") && !Objects.equals(city.getName(), "县")) {
                            homeViewModel.getDriverStartPlaceCity().setValue(city.getName());
                        } else
                            homeViewModel.getDriverStartPlaceCity().setValue(province.getName());
                    }

                    @Override
                    public void onCancel() {
                        ToastUtils.showLongToast(requireActivity(), "已取消");
                    }
                });
                //显示
                mPicker.showCityPicker();
            });
            binding.desPlace.setOnClickListener((v) -> {
//监听选择点击事件及返回结果
                mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
                    @Override
                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                        if (!Objects.equals(city.getName(), "市辖区") && !Objects.equals(city.getName(), "县")) {
                            homeViewModel.getDriverEndPlaceCity().setValue(city.getName());
                        } else
                            homeViewModel.getDriverEndPlaceCity().setValue(province.getName());
                    }

                    @Override
                    public void onCancel() {
                        ToastUtils.showLongToast(requireActivity(), "已取消");
                    }
                });
                //显示
                mPicker.showCityPicker();
            });
        } else {
            binding.llDriverLoc.setVisibility(View.GONE);
            binding.fab.setOnClickListener(view -> {
                Intent intent = new Intent(getActivity(), PostDemandActivity.class);
                startActivity(intent);
            });
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }
}