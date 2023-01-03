package com.jiuxiang.didilogistics.utils;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//HTTP请求工具类，封装了get和post请求
public class HTTPUtils {
    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
    private static final MediaType JSON = MediaType.get("application/json");
    private static final String BASE_URL = "http://192.168.31.35:8080";

    public static void get(String url, HTTPResult httpResult) {
        Request request = new Request.Builder()
                .url(BASE_URL + url)
                .method("GET", null)
                .addHeader("token", App.getToken())
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                httpResult.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    String result = Objects.requireNonNull(response.body()).string();
                    System.out.println(result);
                    httpResult.onSuccess(JSONObject.parseObject(result));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    httpResult.onFailure("服务器未能处理您的请求" + e.getMessage());
                }
            }
        });
    }

    public static void post(String url, String json, HTTPResult httpResult) {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + url)
                .method("POST", body)
                .addHeader("token", App.getToken())
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                httpResult.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    String result = Objects.requireNonNull(response.body()).string();
                    System.out.println(result);
                    httpResult.onSuccess(JSONObject.parseObject(result));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    httpResult.onFailure("服务器未能处理您的请求，请检查提交的数据是否正确" + e.getMessage());
                }
//                String result = Objects.requireNonNull(response.body()).string();
//                httpResult.onSuccess(JSONObject.parseObject(result));
            }
        });
    }

    public static void get_api(String url, HTTPResult httpResult) { //访问第三方提供的api
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                httpResult.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    String result = Objects.requireNonNull(response.body()).string();
                    System.out.println(result);
                    httpResult.onSuccess(JSONObject.parseObject(result));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    httpResult.onFailure("服务器未能处理您的请求" + e.getMessage());
                }
            }
        });
    }
}
