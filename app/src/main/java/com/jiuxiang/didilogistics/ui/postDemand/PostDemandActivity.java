package com.jiuxiang.didilogistics.ui.postDemand;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DistanceResult;
import com.amap.api.services.route.DistanceSearch;
import com.jiuxiang.didilogistics.BR;
import com.jiuxiang.didilogistics.R;
import com.jiuxiang.didilogistics.databinding.ActivityPostDemandBinding;
import com.jiuxiang.didilogistics.utils.HTTPResult;
import com.jiuxiang.didilogistics.utils.HTTPUtils;
import com.jiuxiang.didilogistics.utils.PriceUtils;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.lljjcoder.style.citypickerview.CityPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class PostDemandActivity extends AppCompatActivity {
    private PostDemandViewModel postDemandViewModel;
    private ActivityPostDemandBinding binding;
    private Calendar calendar;// 用来装日期的
    private DatePickerDialog dialog;
    private CityPickerView mPicker = new CityPickerView();
    //声明mlocationClient对象
    private AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    private AMapLocationClientOption mLocationOption = null;
    private DistanceSearch distanceSearch;

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

        try {
            distanceSearch = new DistanceSearch(this);
            distanceSearch.setDistanceSearchListener(new DistanceSearch.OnDistanceSearchListener() {
                @Override
                public void onDistanceSearched(DistanceResult distanceResult, int i) {
                    System.out.println("distanceResult = " + distanceResult.getDistanceResults().get(0).getDistance());
                    try {
                        float distance = distanceResult.getDistanceResults().get(0).getDistance();
                        double price = PriceUtils.getPrice(Double.parseDouble(postDemandViewModel.getLength()), Math.ceil(distanceResult.getDistanceResults().get(0).getDistance() / 1000));
                        postDemandViewModel.getDistance().setValue(String.valueOf(distance / 1000));
                        postDemandViewModel.getLowestPrice().setValue(String.valueOf(price));
                    } catch (Exception ignored) {
                    }
                }
            });
//            new DistanceSearch.DistanceQuery().addOrigins(new LatLonPoint());
        } catch (AMapException e) {
            e.printStackTrace();
        }

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
        try {
            //隐私合规
            AMapLocationClient.updatePrivacyAgree(this, true);
            AMapLocationClient.updatePrivacyShow(this, true, true);

            mlocationClient = new AMapLocationClient(getApplicationContext());
            //设置定位监听
        } catch (Exception e) {
            e.printStackTrace();
        }
        mlocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        Toast.makeText(PostDemandActivity.this, "定位成功", Toast.LENGTH_SHORT).show();
                        //定位成功回调信息，设置相关消息
                        postDemandViewModel.getStartPlaceProvince().setValue(amapLocation.getProvince());
                        postDemandViewModel.getStartPlaceCity().setValue(amapLocation.getCity());
                        postDemandViewModel.getStartPlaceDistrict().setValue(amapLocation.getDistrict());
//                            amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                            Date date = new Date(amapLocation.getTime());
//                            df.format(date);//定位时间
                    } else {
                        Toast.makeText(PostDemandActivity.this, "定位失败，请重试", Toast.LENGTH_SHORT).show();
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    }
                }
            }
        });
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();

        binding.ivLocation.setOnClickListener((v) -> {
            //请求定位权限
            boolean granted = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            if (!granted) {

                int requestCode = 0;
                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
                ActivityCompat.requestPermissions(this, permissions, requestCode);
            }

//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms
//            mLocationOption.setInterval(2000);
//设置定位参数
            mLocationOption.setOnceLocation(true);
            mlocationClient.setLocationOption(mLocationOption);
// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
// 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
// 在定位结束后，在合适的生命周期调用onDestroy()方法
// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//启动定位
            mlocationClient.startLocation();
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

        binding.startDetailPlace.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                if (!postDemandViewModel.getDesPlaceDetail().getValue().equals("") &&
                        !postDemandViewModel.getStartPlaceDetail().getValue().equals("") &&
                        !postDemandViewModel.getLength().equals(""))
                    getDistance(postDemandViewModel.getStartPlace(), postDemandViewModel.getDesPlace());
            }
        });
        binding.desDetailPlace.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                if (!postDemandViewModel.getDesPlaceDetail().getValue().equals("") &&
                        !postDemandViewModel.getStartPlaceDetail().getValue().equals("") &&
                        !postDemandViewModel.getLength().equals(""))
                    getDistance(postDemandViewModel.getStartPlace(), postDemandViewModel.getDesPlace());
            }
        });
        binding.etLength.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                if (!postDemandViewModel.getDesPlaceDetail().getValue().equals("") &&
                        !postDemandViewModel.getStartPlaceDetail().getValue().equals("") &&
                        !postDemandViewModel.getLength().equals(""))
                    getDistance(postDemandViewModel.getStartPlace(), postDemandViewModel.getDesPlace());
            }
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
    }

    private void getDistance(String start, String des) {
        postDemandViewModel.getDistance().setValue("计算中...");
        postDemandViewModel.getLowestPrice().setValue("计算中...");
        List<LatLonPoint> startPoints = new ArrayList<>();
        final LatLonPoint[] desPoints = {null};

        //将起点和终点的地址转换为经纬度
        HTTPUtils.get_api("https://restapi.amap.com/v3/geocode/geo?address=" + start + "&key=", new HTTPResult() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.getString("status").equals("1")) {
                    JSONObject geocodes = result.getJSONArray("geocodes").getJSONObject(0);
                    String location = geocodes.getString("location");
                    String[] split = location.split(",");
                    startPoints.add(new LatLonPoint(Double.parseDouble(split[1]), Double.parseDouble(split[0])));

                    HTTPUtils.get_api("https://restapi.amap.com/v3/geocode/geo?address=" + des + "&key=", new HTTPResult() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            if (result.getString("status").equals("1")) {
                                JSONObject geocodes = result.getJSONArray("geocodes").getJSONObject(0);
                                String location = geocodes.getString("location");
                                String[] split = location.split(",");
                                desPoints[0] = (new LatLonPoint(Double.parseDouble(split[1]), Double.parseDouble(split[0])));


                                //计算两点之间的距离
                                DistanceSearch.DistanceQuery distanceQuery = new DistanceSearch.DistanceQuery();
                                distanceQuery.setOrigins(startPoints);
                                distanceQuery.setDestination(desPoints[0]);
//设置测量方式，支持直线和驾车
                                distanceQuery.setType(DistanceSearch.TYPE_DRIVING_DISTANCE);
                                distanceSearch.calculateRouteDistanceAsyn(distanceQuery);
                            } else {
                                ToastUtils.showLongToast(PostDemandActivity.this, "起始地点经纬度获取失败");
                            }
                        }

                        @Override
                        public void onFailure(String error) {
                            Toast.makeText(PostDemandActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    ToastUtils.showLongToast(PostDemandActivity.this, "起始地点经纬度获取失败");
                }
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(PostDemandActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

    }
}