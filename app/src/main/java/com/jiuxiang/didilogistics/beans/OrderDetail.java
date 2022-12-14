package com.jiuxiang.didilogistics.beans;

public class OrderDetail {
    private String ownerID;//    private int ownerID;
    private String driverID;//    private int driverID;
    private String orderID;//
    private String orderTime;//private Date orderTime;
    private String weight;//private Double weight;
    private String price;//private int price;
    private String deliverTime;// private Date deliverTime;
    private String length;//private Double length;
    private String ownerName;
    private String driverName;
    private String ownerPhone;
    private String driverPhone;

    //order
    private String startPlaceCity;
    private String desPlaceCity;
    private String state;
    private String description;
    private String startPlaceProvince;
    private String startPlaceDistrict;
    private String desPlaceProvince;
    private String desPlaceDistrict;

    public OrderDetail() {
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(String deliverTime) {
        this.deliverTime = deliverTime;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
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

    public String getStartPlaceProvince() {
        return startPlaceProvince;
    }

    public void setStartPlaceProvince(String startPlaceProvince) {
        this.startPlaceProvince = startPlaceProvince;
    }

    public String getStartPlaceDistrict() {
        return startPlaceDistrict;
    }

    public void setStartPlaceDistrict(String startPlaceDistrict) {
        this.startPlaceDistrict = startPlaceDistrict;
    }

    public String getDesPlaceProvince() {
        return desPlaceProvince;
    }

    public void setDesPlaceProvince(String desPlaceProvince) {
        this.desPlaceProvince = desPlaceProvince;
    }

    public String getDesPlaceDistrict() {
        return desPlaceDistrict;
    }

    public void setDesPlaceDistrict(String desPlaceDistrict) {
        this.desPlaceDistrict = desPlaceDistrict;
    }
}
