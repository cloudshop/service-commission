package com.eyun.commission.web.rest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
     * 迎新
     * 服务商：交易额现金,积分分配（交易额2%手续费额度20%，让利额20%积分）
     * @param shopId,payment
     */
    @GetMapping("/order/facilitator/wallet")
    public ResponseEntity handleFacilitatorWallet(@NotNull @RequestParam("shopId") Long shopId, @NotNull @RequestParam("payment")BigDecimal payment,@RequestParam("orderNo")String orderNo,@RequestParam("transfer")BigDecimal transfer)throws Exception{
        String result=commissionService.handleFacilitatorWallet(shopId,payment,orderNo,transfer);
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


    /**
     * 穿着西装敲代码 文亮
     * @param formparamsDTO
     */
    @ApiOperation("线下付资料的提交")
    @PostMapping("/user-annexes-offlineParams")
    @Timed
    public ResponseEntity<String> offlineParams(@RequestBody FormparamsDTO formparamsDTO) {
        UserAnnexDTO userAnnexC = null;
        if (!StringUtils.isBlank(formparamsDTO.getPhone())) {
            userAnnexC = userService.getUserInfosByPhone(formparamsDTO.getPhone()).getBody();
        }
        //c端获得让利额10倍得积分,B端要减去的10倍积分
        BigDecimal cUserjifen = formparamsDTO.getTransferAmount().multiply(new BigDecimal("10"));
        //交易手续费20%给服务商邀请人
        BigDecimal TradingFee = formparamsDTO.getMent().multiply(new BigDecimal(0.02))
            .multiply(new BigDecimal(0.2));
        //拿到商家的信息
        UserAnnexDTO annexDTO = userService.getUserAnnex(formparamsDTO.getUserId()).getBody();
        //查看商户的钱包信息
        WalletDTO wallet = walletService.getwalletInfos(formparamsDTO.getUserId()).getBody();
        List<SettlementWalletDTO> list = new ArrayList<>();
        //只容许普通商家，增值商家，服务商进来
        if (annexDTO.getType() == 3 || annexDTO.getType() == 4 || annexDTO.getType()==5) {
            if (Objects.equals(userAnnexC.getId(), annexDTO.getId())){
                return ResponseEntity.ok().body("jb");
            }
            SettlementWalletDTO settlementWalletDTO = new SettlementWalletDTO();
            settlementWalletDTO.setUserid(formparamsDTO.getUserId());
            settlementWalletDTO.setAmount(formparamsDTO.getTransferAmount());
            settlementWalletDTO.setType(9);
            list.add(settlementWalletDTO);
            System.out.println("******************扣除商户的钱**********************" + settlementWalletDTO.getAmount());
            //给用户开始加积分
            SettlementWalletDTO  CsettlementWalletDTO = new SettlementWalletDTO ();
            CsettlementWalletDTO.setUserid(userAnnexC.getId());
            CsettlementWalletDTO.setAmount(cUserjifen);
            //消费用户获得积分
            CsettlementWalletDTO.setType(1);
            list.add(CsettlementWalletDTO);
            System.out.println("*******************给用户开始加积分******************"+CsettlementWalletDTO.getAmount());
            if (annexDTO.getType() == 4) {
                //给当前商户加积分
                SettlementWalletDTO   bsettlementWallet = new SettlementWalletDTO ();
                bsettlementWallet.setUserid(formparamsDTO.getUserId());
                bsettlementWallet.setType(4);
                //2呗积分
                System.out.println("增值商家2倍积分开始添加**********************************************");
                BigDecimal bUserjifen = formparamsDTO.getTransferAmount().multiply(new BigDecimal("2"));
                bsettlementWallet.setAmount(bUserjifen);
                list.add(bsettlementWallet);
            }else if (annexDTO.getType() == 5){

                //给当前商户加积分
                SettlementWalletDTO   bsettlementWalletService = new SettlementWalletDTO ();
                bsettlementWalletService.setUserid(formparamsDTO.getUserId());
                bsettlementWalletService.setType(4);
                BigDecimal bUserjifenService = formparamsDTO.getTransferAmount().multiply(new BigDecimal("2"));
                bsettlementWalletService.setAmount(bUserjifenService);
                list.add(bsettlementWalletService);

            }
            System.out.println("消费者支线-------------------------------------------------------------------------->");
            //消费者支线
            if (userAnnexC.getInviterId() !=null && userAnnexC.getInviterId() !=0) {
                //直接邀请人
                UserAnnexDTO oneInviterC = userService.getUserAnnex(userAnnexC.getInviterId()).getBody();
                    SettlementWalletDTO userInviterC = new SettlementWalletDTO();
                    userInviterC.setUserid(oneInviterC.getId());
                    userInviterC.setAmount(formparamsDTO.getTransferAmount().multiply(new BigDecimal(0.1)));
                    userInviterC.setType(2);
                    list.add(userInviterC);
                    System.out.println("*****************给直接邀请人加积分******************"+userInviterC.getAmount());

                if (oneInviterC.getInviterId() != null && oneInviterC.getInviterId()!=0) {
                    //间接邀请人
                    UserAnnexDTO twoInviterC = userService.getUserAnnex(oneInviterC.getInviterId()).getBody();
                        SettlementWalletDTO twouserInviterC = new SettlementWalletDTO();
                        twouserInviterC.setUserid(twoInviterC.getId());
                        twouserInviterC.setAmount(formparamsDTO.getTransferAmount().multiply(new BigDecimal(0.1)));
                        twouserInviterC.setType(3);
                        list.add(twouserInviterC);
                        System.out.println("*************给间接邀请人加积分**************" + twouserInviterC.getAmount());
                }
            }
            //商家支线
            System.out.println("商家支线----------------------------------------------------------------------------->");
            if (annexDTO.getInviterId() != null && annexDTO.getInviterId()!=0) {
                //直接邀请人
                UserAnnexDTO OneuserAnnexB = userService.getUserAnnex(annexDTO.getInviterId()).getBody();
                if (OneuserAnnexB.getType()==3 || OneuserAnnexB.getType()==4){
                    SettlementWalletDTO   ServiceInviterB = new SettlementWalletDTO ();
                    ServiceInviterB.setUserid(OneuserAnnexB.getId());
                    ServiceInviterB.setAmount(formparamsDTO.getTransferAmount().multiply(new BigDecimal(0.1)));
                    ServiceInviterB.setType(5);
                    list.add(ServiceInviterB);
                    System.out.println("*****************给直接邀请人加积分***************"+ServiceInviterB.getAmount());
                }else {
                    //加现金奖励
                    if (OneuserAnnexB.getType()==5){
                        SettlementWalletDTO   ServiceTradingFeeB = new SettlementWalletDTO ();
                        ServiceTradingFeeB.setUserid(OneuserAnnexB.getId());
                        ServiceTradingFeeB.setAmount(TradingFee);
                        ServiceTradingFeeB.setType(7);
                        list.add(ServiceTradingFeeB);
                        System.out.println("服务商加奖金************************************************************");

                        //加积分
                        SettlementWalletDTO   ServiceE = new SettlementWalletDTO ();
                        ServiceE.setUserid(OneuserAnnexB.getId());
                        ServiceE.setType(5);
                        ServiceE.setAmount(formparamsDTO.getTransferAmount().multiply(new BigDecimal(0.2)));
                        list.add(ServiceE);

                    }else if (OneuserAnnexB.getType()==3 ||OneuserAnnexB.getType()==4 ||OneuserAnnexB.getType()==2 || OneuserAnnexB.getType()==1){
                        //加积分
                        SettlementWalletDTO   ServiceB = new SettlementWalletDTO ();
                        ServiceB.setUserid(OneuserAnnexB.getId());
                        ServiceB.setAmount(formparamsDTO.getTransferAmount().multiply(new BigDecimal(0.1)));
                        ServiceB.setType(5);
                        list.add(ServiceB);
                    }
                }
                if (OneuserAnnexB.getInviterId() != null && OneuserAnnexB.getInviterId()!=0) {
                    //间接邀请人
                    UserAnnexDTO twouserAnnexB = userService.getUserAnnex(OneuserAnnexB.getInviterId()).getBody();
                    if (twouserAnnexB.getType()==3 || twouserAnnexB.getType()==4){
                        SettlementWalletDTO   ServiceTwoInviterB = new SettlementWalletDTO ();
                        ServiceTwoInviterB.setUserid(twouserAnnexB.getId());
                        ServiceTwoInviterB.setAmount(formparamsDTO.getTransferAmount().multiply(new BigDecimal(0.1)));
                        ServiceTwoInviterB.setType(6);
                        list.add(ServiceTwoInviterB);
                        System.out.println("************给直接邀请人加积分***********" + ServiceTwoInviterB.getAmount());
                    }else {
                        //加钱
                        if (twouserAnnexB.getType()==5){
                            SettlementWalletDTO   ServiceTradingFee = new SettlementWalletDTO ();
                            ServiceTradingFee.setUserid(twouserAnnexB.getId());
                            ServiceTradingFee.setAmount(TradingFee);
                            ServiceTradingFee.setType(8);
                            list.add(ServiceTradingFee);
                            System.out.println("服务商加奖金---------------------------------------------------------->");

                            //加积分
                            SettlementWalletDTO   ServiceTwoInviterL = new SettlementWalletDTO ();
                            ServiceTwoInviterL.setUserid(twouserAnnexB.getId());
                            ServiceTwoInviterL.setAmount(formparamsDTO.getTransferAmount().multiply(new BigDecimal(0.2)));
                            ServiceTwoInviterL.setType(6);
                            list.add(ServiceTwoInviterL);
                            System.out.println("服务商加积分---------------------------------------------------------->");

                        } else if (twouserAnnexB.getType()==3 || twouserAnnexB.getType()==4 || twouserAnnexB.getType()==2 || twouserAnnexB.getType()==4){

                            SettlementWalletDTO   ServiceTwoInviterE = new SettlementWalletDTO ();
                            ServiceTwoInviterE.setUserid(twouserAnnexB.getId());
                            ServiceTwoInviterE.setAmount(formparamsDTO.getTransferAmount().multiply(new BigDecimal(0.1)));
                            ServiceTwoInviterE.setType(6);
                            list.add(ServiceTwoInviterE);
                        }
                    }
                }
            }
            walletService.batchintegrals(list);
            return ResponseEntity.ok().body("ok");
        }
        return ResponseEntity.ok().body("fai");
    }

}
