package com.eyun.commission.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Jack_wen on 2018/5/15.
 */
public class FormparamsDTO implements Serializable {


    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    private String phone;

    private BigDecimal ment;

    /**
     * 上传小票
     */
    private String title;


    /**
     * 上传小票2
     */
    private String title2;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getMent() {
        return ment;
    }

    public void setMent(BigDecimal ment) {
        this.ment = ment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    /**
     * 让利金额

     */
    private BigDecimal transferAmount;





}
