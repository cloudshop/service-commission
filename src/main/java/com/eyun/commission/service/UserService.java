package com.eyun.commission.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eyun.commission.client.AuthorizedFeignClient;
import com.eyun.commission.service.dto.UserAnnexDTO;

@AuthorizedFeignClient(name="user",decode404=true)
public interface UserService {

	@GetMapping("/api/user-annexes/{id}")
    public ResponseEntity<UserAnnexDTO> getUserAnnex(@PathVariable("id") Long id);
	
}
