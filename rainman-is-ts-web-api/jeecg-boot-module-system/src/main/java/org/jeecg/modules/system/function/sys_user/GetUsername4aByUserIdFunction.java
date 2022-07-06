package org.jeecg.modules.system.function.sys_user;

import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component("sys_user.GetUsername4aByUserIdFunction")
public class GetUsername4aByUserIdFunction implements Function<String, String> {
    @Autowired
    ISysUserService sysUserService;

    @Override
    public String apply(String userId) {
        return sysUserService.getUsername4aByUserId(userId);
    }
}
