package com.eyun.commission.service;

import java.util.List;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.eyun.commission.client.AuthorizedFeignClient;
import com.eyun.commission.service.dto.ServiceProviderChainRewardDTO;
import com.eyun.commission.service.dto.SettlementWalletDTO;

@AuthorizedFeignClient(name="wallet")
public interface WalletService {

	@PutMapping("/api/settlement")
	public void settlementWallet(@RequestBody List<SettlementWalletDTO> settlementWalletDTOList);
	
	@PutMapping("/api/serviceProvider/chainReward")
	public void serviceProviderChainReward(@RequestBody ServiceProviderChainRewardDTO serviceProviderChainRewardDTO);
	
}
