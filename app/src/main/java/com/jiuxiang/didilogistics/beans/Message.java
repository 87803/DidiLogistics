package com.jiuxiang.didilogistics.beans;

//每一条消息的基本信息，用于显示在列表中
public class Message {
    String orderID;
    String UserID;
    String content;
    String time;

    public Message() {
    }

    public Message(String orderID, String userID, String content, String time) {
        this.orderID = orderID;
        UserID = userID;
        this.content = content;
        this.time = time;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
