package com.jiuxiang.didilogistics.utils;

import com.jiuxiang.didilogistics.beans.PriceItem;

//不同货车类型的价格
public class PriceUtils {
    public static final PriceItem[] ITEM = {
            new PriceItem(3, 65, 4, 5),
            new PriceItem(5, 230, 4, 15),
            new PriceItem(8, 350, 6, 15),
            new PriceItem(10, 480, 7, 15),
            new PriceItem(13, 900, 9, 30),
    };

    //计算价格，货物长度不一定和货车长度相等
    public static double getPrice(double length, double distance) {
        double price = 0;
        for (PriceItem item : ITEM) {
            if (length <= item.length) {
                if (distance <= item.flagFallPrice_distance) {
                    price = item.flagFallPrice;
                } else {
                    price = item.flagFallPrice + (distance - item.flagFallPrice_distance) * item.price;
                }
                break;
            }
        }
        return price;
    }
}
