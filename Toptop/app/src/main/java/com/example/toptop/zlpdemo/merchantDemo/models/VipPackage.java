package com.example.toptop.zlpdemo.merchantDemo.models;

public class VipPackage {
    private String vipPackageID;
    private  String vipPackageName;
    private int monthDuration;
    private int price;

    public VipPackage(String vipPackageID, String vipPackageName, int monthDuration, int price) {
        this.vipPackageID = vipPackageID;
        this.vipPackageName = vipPackageName;
        this.monthDuration = monthDuration;
        this.price = price;
    }

    public String getVipPackageID() {
        return vipPackageID;
    }

    public void setVipPackageID(String vipPackageID) {
        this.vipPackageID = vipPackageID;
    }

    public String getVipPackageName() {
        return vipPackageName;
    }

    public void setVipPackageName(String vipPackageName) {
        this.vipPackageName = vipPackageName;
    }

    public int getMonthDuration() {
        return monthDuration;
    }

    public void setMonthDuration(int monthDuration) {
        this.monthDuration = monthDuration;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
