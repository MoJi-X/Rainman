package com.rainman.modules.ists.cmcc.controller;

import com.rainman.modules.ists.cmcc.response.oa.GetInfoResponse;
import com.rainman.modules.ists.cmcc.util.DeSedeCoder;
import com.rainman.modules.ists.wf.entity.WfInstance;
import com.rainman.modules.ists.wf.service.IWfInstanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author yaohaitao
 * @date 2021年12月14日
 */
@Api(tags = "移动OA跳回")
@RestController
@RequestMapping("/ists.cmccoa/oa")
public class OaController {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IWfInstanceService wfInstanceService;

    @AutoLog(value = "移动OA跳回")
    @ApiOperation(value = "移动OA跳回接口处理", notes = "移动OA跳回接口处理")
    @PostMapping("/getInfo")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userInfo", value = "加密用户标识", dataTypeClass = String.class),
            @ApiImplicitParam(name = "wfInstanceId", value = "流程-流程实例标识", dataTypeClass = String.class)
    })
    public Result<GetInfoResponse> getInfo(@RequestParam(name = "userInfo") String userInfo, @RequestParam(name = "wfInstanceId") String wfInstanceId) {
        GetInfoResponse getInfoResponse = new GetInfoResponse();

        if (StringUtils.isEmpty(userInfo)) {
            return Result.error("用户信息不能为空值", getInfoResponse);
        }

        if (StringUtils.isEmpty(wfInstanceId)) {
            return Result.error("流程-流程实例标识不能为空值", getInfoResponse);
        }

        WfInstance wfInstance = wfInstanceService.getById(wfInstanceId);

        if (wfInstance == null) {
            return Result.error("流程-流程实例标识查询不到对应的流程-流程实例", getInfoResponse);
        }

        try {
            /**String test = DESedeCoder.encryptMode("admin");**/

            String username = DeSedeCoder.decryptMode(userInfo);

            String token = JwtUtil.sign(StringUtils.lowerCase(username), "123456");

            redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);

            redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 2 / 1000);

            getInfoResponse.setToken(token);

            getInfoResponse.setWfInstance(wfInstance);

            return Result.OK(getInfoResponse);
        } catch (Exception e) {
            return Result.error(e.getMessage(), getInfoResponse);
        }
    }
}