package com.jiuxiang.didilogistics.utils;

import com.alibaba.fastjson.JSONObject;

public interface HTTPResult {
    void onSuccess(JSONObject result);

    void onFailure(String error);
}
