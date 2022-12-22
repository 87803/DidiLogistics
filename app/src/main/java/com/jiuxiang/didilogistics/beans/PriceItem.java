package com.jiuxiang.didilogistics.beans;

public class PriceItem {
    public double length;//车辆长度，米
    public double flagFallPrice;//起步价，元
    public double price;//超出起步里程后的每公里单价，元
    public double flagFallPrice_distance;//起步价包含的里程，公里

    public PriceItem(double length, double flagFallPrice, double price, double flagFallPrice_distance) {
        this.length = length;
        this.flagFallPrice = flagFallPrice;
        this.price = price;
        this.flagFallPrice_distance = flagFallPrice_distance;
    }
}
