package com.rainman.modules.ists.cmcc.response.oa;

import com.rainman.modules.ists.wf.entity.WfInstance;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("登录用户信息:响应参数")
@Data
@Accessors(chain = true)
public class GetInfoResponse {
    /**
     * 令牌
     */
    @ApiModelProperty(value = "令牌")
    private String token;

    /**
     * 流程实例
     */
    @ApiModelProperty(value = "流程实例")
    private WfInstance wfInstance;
}
