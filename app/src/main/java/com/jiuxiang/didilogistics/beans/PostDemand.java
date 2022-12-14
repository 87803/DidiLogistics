package com.jiuxiang.didilogistics.beans;

public class PostDemand extends Demand {
    private Double length;
    private String startPlaceProvince;
    private String startPlaceDistrict;
    private String desPlaceProvince;
    private String desPlaceDistrict;

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

}
