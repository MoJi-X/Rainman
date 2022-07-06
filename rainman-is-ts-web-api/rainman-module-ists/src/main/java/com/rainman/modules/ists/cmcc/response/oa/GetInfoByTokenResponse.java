package com.rainman.modules.ists.cmcc.response.oa;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("响应参数")
@Data
@Accessors(chain = true)
public class GetInfoByTokenResponse {
    /**
     * 令牌
     */
    @ApiModelProperty(value = "令牌")
    private String token;

    /**
     * 用户信息
     */
    @ApiModelProperty(value = "用户信息")
    private UserInfo userInfo;

    @Data
    public static class UserInfo {
        @ApiModelProperty(value = "昵称")
        private String username;
        @ApiModelProperty(value = "姓名")
        private String realname;
    }

}
