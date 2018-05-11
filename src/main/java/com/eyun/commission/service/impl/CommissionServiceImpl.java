package com.eyun.commission.service.impl;

import com.eyun.commission.service.UserService;
import com.eyun.commission.service.WalletService;
import com.eyun.commission.service.dto.SettlementWalletDTO;
import com.eyun.commission.service.dto.UserAnnexDTO;
import com.eyun.commission.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eyun.commission.service.CommissionService;
import com.eyun.commission.service.UserService;
import com.eyun.commission.service.WalletService;
import com.eyun.commission.service.dto.ServiceProviderChainRewardDTO;
import com.eyun.commission.service.dto.UserAnnexDTO;
import com.eyun.commission.web.rest.errors.BadRequestAlertException;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CommissionServiceImpl implements CommissionService{
	@Autowired
	public UserService userService;
	@Autowired
	public WalletService walletService;
    private final Logger log = LoggerFactory.getLogger(CommissionServiceImpl.class);
    @Override
	public void joinMoney(Long userId) {
		ResponseEntity<UserAnnexDTO> userAnnex;
		//查询当前的用户
		userAnnex = userService.getUserAnnex(userId);
		Integer i = new Integer(0);//计数器
		Integer type = new Integer(0);
		ServiceProviderChainRewardDTO spcr = new ServiceProviderChainRewardDTO();
		spcr.setUserID(userId);
		//如果邀请人不为空，就直接获取用户信息，然后判断是否为服务商
		while(userAnnex.getBody().getInviterId()!= null){
			System.out.println(userAnnex.getBody().getInviterId() + "5的邀请人为3");
			Long inviterId = userAnnex.getBody().getInviterId();
			userAnnex = userService.getUserAnnex(inviterId);
			//如果为直接商家
			type = userAnnex.getBody().getType();
			if(type.equals(5)){
				System.out.println("-------------------------@@@@@@@@@@---------------" + inviterId);
				i = i + 1;
				if(i > 3){
					break;
				}
				spcr.setServiceProviderID(inviterId);
				walletService.serviceProviderChainReward(spcr);
			}
		}
	}


    @Override
    public String handleFacilitatorWallet(Long shopId, BigDecimal payment, String orderNo) throws Exception {
        Long userid = userService.getShopIdFindByUserid(shopId).getBody();
        UserAnnexDTO userAnnexDTO = userService.getUserAnnex(userid).getBody();
        Long inviterId = userAnnexDTO.getInviterId();
        if (inviterId != null) {
            userAnnexDTO = userService.getUserAnnex(inviterId).getBody();
            if (userAnnexDTO.getType() == 5) {//该商户在服务商直接体系内
                SettlementWalletDTO settlementWalletDTO = new SettlementWalletDTO();
                settlementWalletDTO.setOrderNo(orderNo);
                settlementWalletDTO.setUserid(userAnnexDTO.getId());
                DecimalFormat df = new DecimalFormat("0.00");
                Double amond = payment.doubleValue();
                BigDecimal decimal = new BigDecimal(df.format((double) amond * 0.004));
                settlementWalletDTO.setAmount(decimal);
                walletService.commissionCash(settlementWalletDTO);
            } else {//该商户在服务商间接体系内
                while (true) {
                    inviterId = userAnnexDTO.getInviterId();
                    if (inviterId == null) {
                        break;
                    }
                    userAnnexDTO = userService.getUserAnnex(inviterId).getBody();
                    if (userAnnexDTO.getType() != 5) {
                        break;
                    }
                    SettlementWalletDTO settlementWalletDTO = new SettlementWalletDTO();
                    settlementWalletDTO.setOrderNo(orderNo);
                    settlementWalletDTO.setUserid(userAnnexDTO.getId());
                    DecimalFormat df = new DecimalFormat("0.00");
                    Double amond = payment.doubleValue();
                    BigDecimal decimal = new BigDecimal(df.format((double) amond * 0.004));
                    settlementWalletDTO.setAmount(decimal);
                    walletService.commissionCash(settlementWalletDTO);
                    break;
                }
            }
        }
        return "success";
    }
}
