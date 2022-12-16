package com.jiuxiang.didilogistics.ui.postDemand;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.jiuxiang.didilogistics.BR;
import com.jiuxiang.didilogistics.R;
import com.jiuxiang.didilogistics.databinding.ActivityPostDemandBinding;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.lljjcoder.style.citypickerview.CityPickerView;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class PostDemandActivity extends AppCompatActivity {
    private PostDemandViewModel postDemandViewModel;
    private ActivityPostDemandBinding binding;
    private Calendar calendar;// 用来装日期的
    private DatePickerDialog dialog;
    CityPickerView mPicker = new CityPickerView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_post_demand);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_demand);

//        PostDemand postDemand = new PostDemand();
        postDemandViewModel = new ViewModelProvider(this).get(PostDemandViewModel.class);
        postDemandViewModel.setPostDemandActivity(this);
//        postDemandViewModel.setPostDemand(postDemand);
        binding.setVariable(BR.postDemandViewModel, postDemandViewModel);
//        binding.setVariable(BR.postDemand, postDemand);
        binding.setLifecycleOwner(this);


        binding.date.setOnClickListener(v -> {
            calendar = Calendar.getInstance();
            dialog = new DatePickerDialog(PostDemandActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> binding.date.setText(year + "/" + (monthOfYear + 1) + "/"
                            + dayOfMonth), calendar.get(Calendar.YEAR), calendar
                    .get(Calendar.MONTH), calendar
                    .get(Calendar.DAY_OF_MONTH));
            dialog.getDatePicker().setMinDate(new Date().getTime());
            dialog.show();
        });

//预先加载仿iOS滚轮实现的全部数据
        mPicker.init(this);
        //添加默认的配置，不需要自己定义，当然也可以自定义相关熟悉，详细属性请看demo
        CityConfig cityConfig = new CityConfig.Builder().build();
        mPicker.setConfig(cityConfig);

        binding.startPlace.setOnClickListener((v) -> {
//监听选择点击事件及返回结果
            mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
                @Override
                public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
//                    System.out.println(province.getName() + city.getName() + district.getName());
                    postDemandViewModel.getStartPlaceProvince().setValue(province.getName());
                    if (!Objects.equals(city.getName(), "市辖区") && !Objects.equals(city.getName(), "县")) {
                        postDemandViewModel.getStartPlaceCity().setValue(city.getName());
                    } else
                        postDemandViewModel.getStartPlaceCity().setValue(province.getName());
                    postDemandViewModel.getStartPlaceDistrict().setValue(district.getName());
                }

                @Override
                public void onCancel() {
                    ToastUtils.showLongToast(PostDemandActivity.this, "已取消");
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
                    postDemandViewModel.getDesPlaceProvince().setValue(province.getName());
                    if (!Objects.equals(city.getName(), "市辖区") && !Objects.equals(city.getName(), "县")) {
                        postDemandViewModel.getDesPlaceCity().setValue(city.getName());
                    } else
                        postDemandViewModel.getDesPlaceCity().setValue(province.getName());
                    postDemandViewModel.getDesPlaceDistrict().setValue(district.getName());
                }

                @Override
                public void onCancel() {
                    ToastUtils.showLongToast(PostDemandActivity.this, "已取消");
                }
            });
            //显示
            mPicker.showCityPicker();
        });
    }
}