package com.jiuxiang.didilogistics.beans;

//发布需求时用户填写的信息
public class PostDemand extends Demand {
    private Double length;
    private String startPlaceProvince;
    private String startPlaceDistrict;
    private String desPlaceProvince;
    private String desPlaceDistrict;
    private String startPlaceDetail;
    private String desPlaceDetail;
    private String distance;
    private String recommendPrice;

    public PostDemand() {
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
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

    public String getStartPlaceDetail() {
        return startPlaceDetail;
    }

    public void setStartPlaceDetail(String startPlaceDetail) {
        this.startPlaceDetail = startPlaceDetail;
    }

    public String getDesPlaceDetail() {
        return desPlaceDetail;
    }

    public void setDesPlaceDetail(String desPlaceDetail) {
        this.desPlaceDetail = desPlaceDetail;
    }

    @Override
    public String toString() {
        return "PostDemand{" +
                "length=" + length +
                ", startPlaceProvince='" + startPlaceProvince + '\'' +
                ", startPlaceDistrict='" + startPlaceDistrict + '\'' +
                ", desPlaceProvince='" + desPlaceProvince + '\'' +
                ", desPlaceDistrict='" + desPlaceDistrict + '\'' +
                ", startPlaceDetail='" + startPlaceDetail + '\'' +
                ", desPlaceDetail='" + desPlaceDetail + '\'' +
                '}';
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getRecommendPrice() {
        return recommendPrice;
    }

    public void setRecommendPrice(String recommendPrice) {
        this.recommendPrice = recommendPrice;
    }
}
