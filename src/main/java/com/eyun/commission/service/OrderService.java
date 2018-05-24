package com.eyun.commission.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eyun.commission.client.AuthorizedFeignClient;
import com.eyun.commission.service.dto.ProOrderDTO;

@AuthorizedFeignClient(name="order",decode404=true)
public interface OrderService {

	@GetMapping("/api/findOrderByOrderNo/{orderNo}")
    public ResponseEntity<ProOrderDTO> findOrderByOrderNo(@PathVariable("orderNo")String orderNo);
	
}
