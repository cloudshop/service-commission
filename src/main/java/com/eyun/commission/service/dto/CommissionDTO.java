package com.eyun.commission.service.dto;

import java.math.BigDecimal;

public class CommissionDTO {
    private Long userid;
    private Integer type;// 1、b端收入余额 2、c端获得积分 3、b端获得积分 4、邀请人获得积分
    private BigDecimal amount;// 现金
    private BigDecimal integral;//积分
    private String orderNo;

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getIntegral() {
        return integral;
    }

    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public String toString() {
        return "CommissionDTO{" +
            "userid=" + userid +
            ", type=" + type +
            ", amount=" + amount +
            ", integral=" + integral +
            ", orderNo='" + orderNo + '\'' +
            '}';
    }
}
