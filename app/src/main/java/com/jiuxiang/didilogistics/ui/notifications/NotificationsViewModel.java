package com.jiuxiang.didilogistics.ui.notifications;

import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuxiang.didilogistics.beans.Message;
import com.jiuxiang.didilogistics.utils.App;
import com.jiuxiang.didilogistics.utils.HTTPResult;
import com.jiuxiang.didilogistics.utils.HTTPUtils;

import java.util.ArrayList;
import java.util.List;

public class NotificationsViewModel extends ViewModel {
    private final List<Message> data = new ArrayList<>();

    public NotificationsViewModel() {
        loadData();
    }

    public void loadData() {
        HTTPUtils.get("/auth/message", new HTTPResult() {
            @Override
            public void onSuccess(JSONObject result) {
                System.out.println(result);
                data.clear();
                JSONArray data2 = result.getJSONArray("data");
                data.addAll(data2.toJavaList(Message.class));
                android.os.Message message = App.getNotificationFragmentHandler().obtainMessage(1);
                message.obj = "获取消息数据成功";
                App.getNotificationFragmentHandler().sendMessage(message);
            }

            @Override
            public void onFailure(String error) {
                android.os.Message message = App.getNotificationFragmentHandler().obtainMessage(1);
                message.obj = "获取消息数据失败，请重试，" + error;
                App.getNotificationFragmentHandler().sendMessage(message);
            }
        });
    }

    public List<Message> getData() {
        return data;
    }
}