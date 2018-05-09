package com.eyun.commission.service.dto;

import java.io.Serializable;

public class  ServiceProviderChainRewardDTO implements Serializable{

	private Long UserID;// 用户id
	private Long ServiceProviderID;// 服务商id
	
	public Long getUserID() {
		return UserID;
	}
	public void setUserID(Long userID) {
		UserID = userID;
	}
	public Long getServiceProviderID() {
		return ServiceProviderID;
	}
	public void setServiceProviderID(Long serviceProviderID) {
		ServiceProviderID = serviceProviderID;
	}
	
	
}
