package com.example.toptop.payment.models;

import java.util.Date;

public class SuperUser {
   private String superUserID;
   private int userID;
   private Date createDate;
   private String createBy;
   private String vipPackageID;
   private  Date modifiedDate;

    public SuperUser() {
    }

    public SuperUser(String superUserID, int userID, Date createDate, String createBy, String vipPackageID, Date modifiedDate) {
        this.superUserID = superUserID;
        this.userID = userID;
        this.createDate = createDate;
        this.createBy = createBy;
        this.vipPackageID = vipPackageID;
        this.modifiedDate = modifiedDate;
    }

    public String getSuperUserID() {
        return superUserID;
    }

    public void setSuperUserID(String superUserID) {
        this.superUserID = superUserID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getVipPackageID() {
        return vipPackageID;
    }

    public void setVipPackageID(String vipPackageID) {
        this.vipPackageID = vipPackageID;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
