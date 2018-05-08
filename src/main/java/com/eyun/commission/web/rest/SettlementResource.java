package com.eyun.commission.web.rest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eyun.commission.service.OrderService;
import com.eyun.commission.service.UserService;
import com.eyun.commission.service.WalletService;
import com.eyun.commission.service.dto.ProOrderDTO;
import com.eyun.commission.service.dto.SettlementWalletDTO;
import com.eyun.commission.service.dto.UserAnnexDTO;

import io.undertow.util.BadRequestException;

@RestController
@RequestMapping("/api")
public class SettlementResource {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private WalletService walletService;
	
	/**
	 * 结算业务 b端and c端
	 * @author 逍遥子
	 * @email 756898059@qq.com
	 * @date 2018年5月8日
	 * @version 1.0
	 * @param orderNo
	 * @throws Exception
	 */
	@PostMapping("/order/settlement/{orderNo}")
	public void orderSettlement(@PathVariable("orderNo") String orderNo) throws Exception {
		ResponseEntity<ProOrderDTO> resp = orderService.findOrderByOrderNo(orderNo);
		if (404 == resp.getStatusCodeValue() || resp.getBody().getStatus() != 4) { //校验订单状态
			throw new BadRequestException("订单异常");
		}
		List<SettlementWalletDTO> settlementWalletDTOList = new ArrayList<SettlementWalletDTO>();
		ProOrderDTO proOrderDTO = resp.getBody();
		Long cUserid = proOrderDTO.getcUserid();//买家id
		Long shopId = proOrderDTO.getShopId();//店铺id
		//TODO 根据店铺id查询 B端用户userId
		Long bUserid = 3l;
		BigDecimal transferAmount = proOrderDTO.getTransferAmount();//让利额
		BigDecimal price = proOrderDTO.getPrice().subtract(transferAmount).multiply(new BigDecimal("0.98"));//b端获得金额
		BigDecimal cUserjifen = transferAmount.multiply(new BigDecimal("10"));//c端获得让利额10倍得积分
		SettlementWalletDTO CsettlementWalletDTO = new SettlementWalletDTO();
		CsettlementWalletDTO.setUserid(cUserid);
		CsettlementWalletDTO.setAmount(cUserjifen);
		CsettlementWalletDTO.setType(2);
		CsettlementWalletDTO.setOrderNo(orderNo);
		settlementWalletDTOList.add(CsettlementWalletDTO);
		BigDecimal bUserjifen = transferAmount.multiply(new BigDecimal("2"));//b端获得让利额2倍得积分
		
		SettlementWalletDTO BsettlementWalletDTO = new SettlementWalletDTO();
		BsettlementWalletDTO.setUserid(bUserid);
		BsettlementWalletDTO.setAmount(price);
		BsettlementWalletDTO.setType(1);
		BsettlementWalletDTO.setOrderNo(orderNo);
		settlementWalletDTOList.add(BsettlementWalletDTO);
		
		SettlementWalletDTO BbsettlementWalletDTO = new SettlementWalletDTO();
		BbsettlementWalletDTO.setUserid(bUserid);
		BbsettlementWalletDTO.setAmount(bUserjifen);
		BbsettlementWalletDTO.setType(3);
		BbsettlementWalletDTO.setOrderNo(orderNo);
		settlementWalletDTOList.add(BbsettlementWalletDTO);
		
		BigDecimal inviterJF = transferAmount.multiply(new BigDecimal("0.10"));
		
		//获取c端用户上两级用户
		UserAnnexDTO cUser = userService.getUserAnnex(cUserid).getBody();
		if (cUser.getInviterId() != null) {
			UserAnnexDTO cUser2 = userService.getUserAnnex(cUser.getInviterId()).getBody();
			SettlementWalletDTO CsettlementWalletDTO1 = new SettlementWalletDTO();
			CsettlementWalletDTO1.setUserid(cUser2.getId());
			CsettlementWalletDTO1.setAmount(inviterJF);
			CsettlementWalletDTO1.setType(4);
			CsettlementWalletDTO1.setOrderNo(orderNo);
			settlementWalletDTOList.add(CsettlementWalletDTO1);
			if (cUser2.getInviterId() != null) {
				UserAnnexDTO cUser3 = userService.getUserAnnex(cUser2.getInviterId()).getBody();
				SettlementWalletDTO CsettlementWalletDTO2 = new SettlementWalletDTO();
				CsettlementWalletDTO2.setUserid(cUser3.getId());
				CsettlementWalletDTO2.setAmount(inviterJF);
				CsettlementWalletDTO2.setType(4);
				CsettlementWalletDTO2.setOrderNo(orderNo);
				settlementWalletDTOList.add(CsettlementWalletDTO2);
			}
		}
		
		//获取b端用户上两级用户
		UserAnnexDTO bUser = userService.getUserAnnex(bUserid).getBody();
		if (bUser.getInviterId() != null) {
			UserAnnexDTO bUser2 = userService.getUserAnnex(bUser.getInviterId()).getBody();
			SettlementWalletDTO BsettlementWalletDTO1 = new SettlementWalletDTO();
			BsettlementWalletDTO1.setUserid(bUser2.getId());
			BsettlementWalletDTO1.setAmount(inviterJF);
			BsettlementWalletDTO1.setType(4);
			BsettlementWalletDTO1.setOrderNo(orderNo);
			settlementWalletDTOList.add(BsettlementWalletDTO1);
			if (bUser2.getInviterId() != null) {
				UserAnnexDTO bUser3 = userService.getUserAnnex(bUser2.getInviterId()).getBody();
				SettlementWalletDTO BsettlementWalletDTO2 = new SettlementWalletDTO();
				BsettlementWalletDTO2.setUserid(bUser3.getId());
				BsettlementWalletDTO2.setAmount(inviterJF);
				BsettlementWalletDTO2.setType(4);
				BsettlementWalletDTO2.setOrderNo(orderNo);
				settlementWalletDTOList.add(BsettlementWalletDTO2);
			}
		}
		walletService.settlementWallet(settlementWalletDTOList);
	
	}
	
}
