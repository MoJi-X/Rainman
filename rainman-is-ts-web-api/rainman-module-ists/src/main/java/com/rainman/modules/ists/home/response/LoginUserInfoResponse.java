package com.rainman.modules.ists.home.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@ApiModel("登录用户信息:响应参数")
@Data
@Accessors(chain = true)
public class LoginUserInfoResponse {
    /**
     * 登录人名字
     */
    @ApiModelProperty(value = "登录人名字")
    private String realname;

    /**
     * 当前登录部门名称
     */
    @ApiModelProperty(value = "当前登录人部门名称")
    private String orgName;

    @ApiModelProperty(value = "当前登录人角色集合名称")
    private Set<String> roleNameList;
}
