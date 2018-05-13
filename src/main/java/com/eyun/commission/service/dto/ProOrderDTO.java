package com.eyun.commission.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the ProOrder entity.
 */
public class ProOrderDTO implements Serializable {

	private Long id;

	private Long cUserid;

	private String orderNo;

	private BigDecimal price;
	
	private Integer status;

	private BigDecimal payment;

	private Integer paymentType;

	private Instant paymentTime;

	private BigDecimal postFee;

	private Instant consignTime;

	private Instant endTime;

	private Instant closeTime;

	private String shippingName;

	private String shipingCode;

	private String buyerMessage;

	private String buyerNick;

	private Boolean buyerRate;

	private Instant createdTime;

	private Instant updateTime;

	private Boolean deletedB;

	private Boolean deletedC;

	private String payNo;

	private String shopName;

	private String orderString;

	private BigDecimal transferAmount;

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Boolean getBuyerRate() {
		return buyerRate;
	}

	public Boolean getDeletedB() {
		return deletedB;
	}

	public Boolean getDeletedC() {
		return deletedC;
	}

	private Long shopId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getcUserid() {
		return cUserid;
	}

	public void setcUserid(Long cUserid) {
		this.cUserid = cUserid;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public BigDecimal getPayment() {
		return payment;
	}

	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public Instant getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(Instant paymentTime) {
		this.paymentTime = paymentTime;
	}

	public BigDecimal getPostFee() {
		return postFee;
	}

	public void setPostFee(BigDecimal postFee) {
		this.postFee = postFee;
	}

	public Instant getConsignTime() {
		return consignTime;
	}

	public void setConsignTime(Instant consignTime) {
		this.consignTime = consignTime;
	}

	public Instant getEndTime() {
		return endTime;
	}

	public void setEndTime(Instant endTime) {
		this.endTime = endTime;
	}

	public Instant getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Instant closeTime) {
		this.closeTime = closeTime;
	}

	public String getShippingName() {
		return shippingName;
	}

	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}

	public String getShipingCode() {
		return shipingCode;
	}

	public void setShipingCode(String shipingCode) {
		this.shipingCode = shipingCode;
	}

	public String getBuyerMessage() {
		return buyerMessage;
	}

	public void setBuyerMessage(String buyerMessage) {
		this.buyerMessage = buyerMessage;
	}

	public String getBuyerNick() {
		return buyerNick;
	}

	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	public Boolean isBuyerRate() {
		return buyerRate;
	}

	public void setBuyerRate(Boolean buyerRate) {
		this.buyerRate = buyerRate;
	}

	public Instant getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Instant createdTime) {
		this.createdTime = createdTime;
	}

	public Instant getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Instant updateTime) {
		this.updateTime = updateTime;
	}

	public Boolean isDeletedB() {
		return deletedB;
	}

	public void setDeletedB(Boolean deletedB) {
		this.deletedB = deletedB;
	}

	public Boolean isDeletedC() {
		return deletedC;
	}

	public void setDeletedC(Boolean deletedC) {
		this.deletedC = deletedC;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public String getOrderString() {
		return orderString;
	}

	public void setOrderString(String orderString) {
		this.orderString = orderString;
	}

	public BigDecimal getTransferAmount() {
		return transferAmount;
	}

	public void setTransferAmount(BigDecimal transferAmount) {
		this.transferAmount = transferAmount;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		ProOrderDTO proOrderDTO = (ProOrderDTO) o;
		if (proOrderDTO.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), proOrderDTO.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "ProOrderDTO [id=" + id + ", cUserid=" + cUserid + ", orderNo=" + orderNo + ", price=" + price
				+ ", status=" + status + ", payment=" + payment + ", paymentType=" + paymentType + ", paymentTime="
				+ paymentTime + ", postFee=" + postFee + ", consignTime=" + consignTime + ", endTime=" + endTime
				+ ", closeTime=" + closeTime + ", shippingName=" + shippingName + ", shipingCode=" + shipingCode
				+ ", buyerMessage=" + buyerMessage + ", buyerNick=" + buyerNick + ", buyerRate=" + buyerRate
				+ ", createdTime=" + createdTime + ", updateTime=" + updateTime + ", deletedB=" + deletedB
				+ ", deletedC=" + deletedC + ", payNo=" + payNo + ", shopName=" + shopName + ", orderString="
				+ orderString + ", transferAmount=" + transferAmount + ", shopId=" + shopId + "]";
	}

}
