package com.jiuxiang.didilogistics.utils;

import com.alibaba.fastjson.JSONObject;

//HTTP请求结果的封装类
public interface HTTPResult {
    void onSuccess(JSONObject result);

    void onFailure(String error);
}
