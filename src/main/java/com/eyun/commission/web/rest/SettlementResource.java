package com.eyun.commission.web.rest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.codahale.metrics.annotation.Timed;
import com.eyun.commission.service.*;
import com.eyun.commission.service.dto.*;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eyun.commission.service.CommissionService;

import io.swagger.annotations.ApiOperation;
import io.undertow.util.BadRequestException;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api")
public class SettlementResource {

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserService userService;

	@Autowired
	private WalletService walletService;

	@Autowired private UserUaaService userUaaService;

	@Autowired
    CommissionService commissionService;
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

    /**
     * 文亮
     * 服务商积分分配
     * @param orderNo
     */
    @PostMapping("/order/integralDistribution/{orderNo}")
	public void integralDistribution(@PathVariable("orderNo") String orderNo) throws Exception{
        ResponseEntity<ProOrderDTO> resp = orderService.findOrderByOrderNo(orderNo);
        if (404 == resp.getStatusCodeValue() || resp.getBody().getStatus() != 4) { //校验订单状态
            throw new BadRequestException("订单异常");
        }

        ProOrderDTO proOrderDTO = resp.getBody();
        //拿到店铺的ID，根据店铺的ID查询出店铺邀请人的服务商是谁
        Long shopId = proOrderDTO.getShopId();
        //根据店铺ID拿到，店铺持有人的用户的ID
        Long userId = userService.getShopIdFindByUserid(shopId).getBody();
        UserAnnexDTO userAnnexDTO = userService.getUserAnnex(userId).getBody();
        //拿到邀请人的并且的邀请人的ID不能为空和必须的得等于5
        Long inviterId = userAnnexDTO.getInviterId();
        UserAnnexDTO inviterUser = null;
        while (true) {
            if (userAnnexDTO.getInviterId() != null && userAnnexDTO.getType() == 5) {
                 inviterUser = userService.getUserAnnex(inviterId).getBody();
                break;
            } else if (inviterUser.getInviterId()==null){
                break;

            }else if (inviterUser.getInviterId()!=null){
                inviterId = inviterUser.getInviterId();
            }
        }
    }

    /**
     * 迎新
     * 服务商现金分配
     * @param shopId,payment
     */
    @GetMapping("/order/facilitator/wallet")
    public ResponseEntity handleFacilitatorWallet(@NotNull @RequestParam("shopId") Long shopId, @NotNull @RequestParam("payment")BigDecimal payment,@RequestParam("orderNo")String orderNo)throws Exception{
        String result=commissionService.handleFacilitatorWallet(shopId,payment,orderNo);
       return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

	/**
	 * jiangsi
	 * @param userId
	 * @throws Exception
	 */

	@ApiOperation("链上直接或间接的服务商账户更改")
	@GetMapping("/serviceProvider/{userId}")
	public void serviceProviderUpdateAcount(@PathVariable("userId") Long userId) throws Exception {
		commissionService.joinMoney(userId);
	}





    @ApiOperation("线下付资料的提交")
    @PostMapping("/user-annexes-offlineParams")
    @Timed
    public void offlineParams(@RequestBody FormparamsDTO formparamsDTO){

        UserAnnexDTO userAnnexC =null;
        if (!StringUtils.isBlank(formparamsDTO.getPhone())){
             userAnnexC = userService.getUserInfosByPhone(formparamsDTO.getPhone()).getBody();
        }
        //TODO 1.获取商户的ID
        //c端获得让利额10倍得积分,B端要减去的10倍积分
        BigDecimal cUserjifen = formparamsDTO.getTransferAmount().multiply(new BigDecimal("10"));
        UserDTO account = userUaaService.getAccount();
        //拿到商家的信息
        UserAnnexDTO annexDTO = userService.getUserAnnex(account.getId()).getBody();
        if (annexDTO.getType()==3||annexDTO.getType()==4){
            //判断商户的越
            WalletDTO wallet = walletService.getUserWalletInfos().getBody();
            int i = formparamsDTO.getTransferAmount().compareTo(wallet.getBalance());
            if (i==0||i==1){
                //TODO 1.扣除当前商户的让利金融
                System.out.println("扣钱开始--------------------------------------------------------------------------");
                String messags = walletService.deductmoney(formparamsDTO.getTransferAmount()).getBody();
                System.out.println("扣钱结束--------------------------------------------------------------------------");
                System.out.println("给平台加钱开始*********************************************************************");
                //TODO 2.给平台账户加钱
                System.out.println("给平台加钱结束*********************************************************************");
                //TODO 2.给用户加积分
                System.out.println("给用户加积分开始====================================================================");
                List<SettlementWalletDTO> settlementWalletDTOList = new ArrayList<SettlementWalletDTO>();
                SettlementWalletDTO CsettlementWalletDTO = new SettlementWalletDTO();
                CsettlementWalletDTO.setUserid(userAnnexC.getId());
                CsettlementWalletDTO.setAmount(cUserjifen );
                CsettlementWalletDTO.setType(2);
                walletService.settlementWallet(settlementWalletDTOList);
                System.out.println("给用户加积分结束====================================================================");

            }


        }

    }
}
