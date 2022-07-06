package org.jeecg.modules.system.function.sys_user_role;

import org.jeecg.modules.system.service.ISysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component("sys_user_role.ListUserIdByRoleCodeFunction")
public class ListUserIdByRoleCodeFunction implements Function<String, List<String>> {
    @Autowired
    ISysUserRoleService sysUserRoleService;

    @Override
    public List<String> apply(String roleCode) {
        return sysUserRoleService.listUserIdByRoleCode(roleCode);
    }
}
