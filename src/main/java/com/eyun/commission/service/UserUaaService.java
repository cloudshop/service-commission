package com.eyun.commission.service;

import com.eyun.commission.client.AuthorizedFeignClient;
import com.eyun.commission.service.dto.UserDTO;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Jack_wen on 2018/5/15.
 */
@AuthorizedFeignClient(name="uaa")
public interface UserUaaService {

    @GetMapping("/api/account")
    UserDTO getAccount();


}
