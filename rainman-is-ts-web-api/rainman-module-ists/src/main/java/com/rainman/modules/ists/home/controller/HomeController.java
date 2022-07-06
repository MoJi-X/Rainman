package com.rainman.modules.ists.home.controller;

import com.rainman.modules.ists.home.response.LoginUserInfoResponse;
import com.rainman.modules.ists.wf.entity.WfInstance;
import com.rainman.modules.ists.wf.service.IWfInstanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.SpringContextUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * @Description: 首页
 * @Author: yao-hai-tao
 * @Date: 2021-11-25
 * @Version: V1.0
 */
@Api(tags = "首页")
@RestController
@RequestMapping("/ists.home")
@Slf4j
public class HomeController extends JeecgController<WfInstance, IWfInstanceService> {
    /**
     * 查询用户拥有角色
     *
     * @return
     */
    @AutoLog(value = "查询：列表：用户角色")
    @ApiOperation(value = "查询：列表：用户角色", notes = "查询：列表：用户角色")
    @GetMapping(value = "/listRoleName")
    public Result<Set<String>> listRoleName() {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        Set<String> roleNameList = SpringContextUtils.getBean(ISysBaseAPI.class).queryUserRoleName(sysUser.getUsername());

        return Result.OK(roleNameList);
    }

    /**
     * 查询用户部门
     *
     * @return
     */
    @AutoLog(value = "查询：列表：用户部门")
    @ApiOperation(value = "查询：列表：用户部门", notes = "查询：列表：用户部门")
    @GetMapping(value = "/listDepartName")
    public Result<List<String>> listDepartName() {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        List<String> departList = SpringContextUtils.getBean(ISysBaseAPI.class).getDepartNamesByUsername(sysUser.getUsername());

        return Result.OK(departList);
    }

    /**
     * 查询：单个：当前登录用户信息
     *
     * @return
     */
    @AutoLog(value = "查询：单个：当前登录用户信息")
    @ApiOperation(value = "查询：单个：当前登录用户信息", notes = "首页-当前登录用户信息")
    @GetMapping(value = "/loginUserInfo")
    public Result<LoginUserInfoResponse> loginUserInfo() {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        LoginUserInfoResponse loginUserInfoResponse = new LoginUserInfoResponse();

        loginUserInfoResponse.setRealname(sysUser.getRealname());

        if (StringUtils.isNotBlank(sysUser.getOrgCode())) {
            loginUserInfoResponse.setOrgName(SpringContextUtils.getBean(ISysBaseAPI.class).getDepartNameByOrgCode(sysUser.getOrgCode()));
        }

        Set<String> roleNameList = SpringContextUtils.getBean(ISysBaseAPI.class).queryUserRoleName(sysUser.getUsername());

        loginUserInfoResponse.setRoleNameList(roleNameList);

        return Result.OK(loginUserInfoResponse);
    }
}
