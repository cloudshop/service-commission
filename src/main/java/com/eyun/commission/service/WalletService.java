package com.eyun.commission.service;


import java.util.List;


import com.eyun.commission.service.dto.WalletDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eyun.commission.client.AuthorizedFeignClient;
import com.eyun.commission.service.dto.ServiceProviderChainRewardDTO;
import com.eyun.commission.service.dto.SettlementWalletDTO;

@AuthorizedFeignClient(name="wallet",decode404=true)
public interface WalletService {

	@PutMapping("/api/settlement")
	public void settlementWallet(@RequestBody List<SettlementWalletDTO> settlementWalletDTOList);

    @PutMapping("/api/wallet/commission/cash")
    public String commissionCash(@RequestBody SettlementWalletDTO settlementWalletDTO);


	@PutMapping("/api/serviceProvider/chainReward")
	public void serviceProviderChainReward(@RequestBody ServiceProviderChainRewardDTO serviceProviderChainRewardDTO);

    @GetMapping("/api/wallets/user")
    ResponseEntity<WalletDTO> getUserWalletInfos();

    /**
     * 扣除商户的响应的资金
     * @param
     * @return
     */
    @PostMapping("/api/wallet/deductmoney")
    ResponseEntity<String> deductmoney(@RequestBody SettlementWalletDTO settlementWalletDTO);





    /**
     * 线下给给对应的用户添加积分
     * @param
     * @return
     */
    @PostMapping("/api/wallet/AddUserIntegral")
    ResponseEntity<String> AddUserIntegral(@RequestBody SettlementWalletDTO settlementWalletDTO);








}
