package com.jiuxiang.didilogistics.beans;

import androidx.lifecycle.MutableLiveData;

import com.jiuxiang.didilogistics.ui.orderDetail.OrderDetailActivity;

public class Demand {
    private String orderID;
    private String orderTime;
    private String startPlaceCity;
    private String desPlaceCity;
    private String deliverTime;
    private Double weight;
    private int price;
    private String state;
    private String description;

    public Demand() {
    }

    public Demand(String orderID, String orderTime, String startPlaceCity, String desPlaceCity, String deliverTime, Double weight, int price, String state, String description) {
        this.orderID = orderID;
        this.orderTime = orderTime;
        this.startPlaceCity = startPlaceCity;
        this.desPlaceCity = desPlaceCity;
        this.deliverTime = deliverTime;
        this.weight = weight;
        this.price = price;
        this.state = state;
        this.description = description;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getStartPlaceCity() {
        return startPlaceCity;
    }

    public void setStartPlaceCity(String startPlaceCity) {
        this.startPlaceCity = startPlaceCity;
    }

    public String getDesPlaceCity() {
        return desPlaceCity;
    }

    public void setDesPlaceCity(String desPlaceCity) {
        this.desPlaceCity = desPlaceCity;
    }

    public String getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(String deliverTime) {
        this.deliverTime = deliverTime;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Demand{" +
                "orderID='" + orderID + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", startPlaceCity='" + startPlaceCity + '\'' +
                ", desPlaceCity='" + desPlaceCity + '\'' +
                ", deliverTime='" + deliverTime + '\'' +
                ", weight=" + weight +
                ", price=" + price +
                ", state='" + state + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
