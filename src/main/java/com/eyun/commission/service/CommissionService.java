package com.eyun.commission.service;

import java.math.BigDecimal;

public interface CommissionService {

    public String handleFacilitatorWallet(Long shopId, BigDecimal payment, String orderNo) throws Exception;

    public void joinMoney(Long userId);
}
