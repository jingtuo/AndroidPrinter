package com.jingtuo.android.printer.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 我的用户信息
 *
 * @author JingTtuo
 */
@Entity()
public class MyUserInfo {

    @PrimaryKey(autoGenerate = true)
    private long id;

    /**
     * 姓名
     */
    private String fullName;

    /**
     * 手机号码
     */
    private String mobileNo;

    /**
     * 性别
     */
    private String sex;

    /**
     * 地址
     */
    private String address;

    /**
     * 邮编
     */
    private String postcode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
}
