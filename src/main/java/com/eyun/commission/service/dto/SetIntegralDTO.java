package com.eyun.commission.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Jack_wen on 2018/5/16.
 */
public class SetIntegralDTO implements Serializable {

    private Long userid;

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

    private Integer type;// 1、直接用户获取10倍积分  2.直接用户要和间接邀请，3.直接邀请商家和间接邀请商家 4,当前商家
    private BigDecimal amount;// 额
}
