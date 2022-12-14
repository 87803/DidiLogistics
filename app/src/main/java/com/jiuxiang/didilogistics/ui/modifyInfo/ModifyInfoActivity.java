package com.jiuxiang.didilogistics.ui.modifyInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.jiuxiang.didilogistics.R;
import com.jiuxiang.didilogistics.beans.App;
import com.jiuxiang.didilogistics.beans.User;
import com.jiuxiang.didilogistics.databinding.ActivityModifyInfoBinding;
import com.jiuxiang.didilogistics.ui.postDemand.PostDemandActivity;
import com.jiuxiang.didilogistics.utils.HTTPResult;
import com.jiuxiang.didilogistics.utils.HTTPUtils;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.lljjcoder.style.citypickerview.CityPickerView;

import java.util.Objects;


public class ModifyInfoActivity extends AppCompatActivity {
    ActivityModifyInfoBinding binding;
    CityPickerView mPicker = new CityPickerView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_modify_info);
        binding.setLifecycleOwner(this);
        User user = App.getUser();
        binding.nameEt.setText(user.getName());
        if (!user.isType())
            binding.driverTable.setVisibility(View.GONE);
        else {
            binding.stateSwitch.setChecked(user.isState());
            binding.startPlace.setText(user.getCurCity());
            binding.desPlace.setText(user.getDesCity());
            binding.carLengthEt.setText(user.getCarLength());
            binding.carWeightEt.setText(user.getCarWeight());
        }

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
                    if (!Objects.equals(city.getName(), "市辖区") && !Objects.equals(city.getName(), "县")) {
                        binding.startPlace.setText(city.getName());
                    } else
                        binding.startPlace.setText(province.getName());
                }

                @Override
                public void onCancel() {
                    ToastUtils.showLongToast(ModifyInfoActivity.this, "已取消");
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
                        binding.desPlace.setText(city.getName());
                    } else
                        binding.desPlace.setText(province.getName());
                }

                @Override
                public void onCancel() {
                    ToastUtils.showLongToast(ModifyInfoActivity.this, "已取消");
                }
            });
            //显示
            mPicker.showCityPicker();
        });
    }

    public void modifyInfo(View v) {
        String name = binding.nameEt.getText().toString();
        String oldPassword = binding.oldPwdEt.getText().toString();
        String newPwd = binding.newPwdEt.getText().toString();
        String newPwdConfirm = binding.confirmPwdEt.getText().toString();
        boolean state = binding.stateSwitch.isChecked();
        String curCity = binding.startPlace.getText().toString();
        String desCity = binding.desPlace.getText().toString();
        String carLength = binding.carLengthEt.getText().toString();
        String carWeight = binding.carWeightEt.getText().toString();
        System.out.println(binding.oldPwdEt.getText().toString());
        if (!oldPassword.equals("") || !newPwd.equals("") || !newPwdConfirm.equals("")) {
            if (newPwd.length() < 8 || newPwd.length() > 16) {
                Toast.makeText(this, "密码必须在8-16位之间", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!newPwd.equals(newPwdConfirm)) {
                Toast.makeText(this, "密码不一致，请重新输入", Toast.LENGTH_LONG).show();
                return;
            }
        }
        User user = new User();
        user.setName(name);
        user.setState(state);
        user.setCurCity(curCity);
        user.setDesCity(desCity);
        user.setCarLength(carLength);
        user.setCarWeight(carWeight);
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(user));
        jsonObject.put("oldPassword", oldPassword);
        jsonObject.put("newPassword", newPwd);
        HTTPUtils.post("/auth/modifyInfo", jsonObject.toJSONString(), new HTTPResult() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.getInteger("code") == 200) {
                    ModifyInfoActivity.this.runOnUiThread(() -> {
                        Toast.makeText(ModifyInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                        Message msg = new Message();
                        msg.what = 1;
                        App.getUserInfoFragmentHandler().sendMessage(msg);
                    });
                } else {
                    ModifyInfoActivity.this.runOnUiThread(() -> {
                        Toast.makeText(ModifyInfoActivity.this, result.getString("msg"), Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(String error) {
                ModifyInfoActivity.this.runOnUiThread(() -> {
                    Toast.makeText(ModifyInfoActivity.this, "修改失败，" + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}