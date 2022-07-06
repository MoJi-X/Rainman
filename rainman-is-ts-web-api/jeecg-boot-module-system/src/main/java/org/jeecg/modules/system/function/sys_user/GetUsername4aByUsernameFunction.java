package org.jeecg.modules.system.function.sys_user;

import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component("sys_user.GetUsername4aByUsernameFunction")
public class GetUsername4aByUsernameFunction implements Function<String, String> {
    @Autowired
    ISysUserService sysUserService;

    @Override
    public String apply(String username) {
        return sysUserService.getUsername4aByUsername(username);
    }
}
