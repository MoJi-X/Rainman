package com.rainman.modules.ists.bd.controller;

import com.rainman.modules.ists.bd.entity.BdAssets;
import com.rainman.modules.ists.bd.service.IBdAssetsService;
import com.rainman.modules.ists.bd.service.IBdAssetsWfInstanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @Description: bd_assets
 * @Author: 谢桢昊
 * @Date: 2021-12-2
 * @Version: V1.0
 */
@Api(tags = "基础数据-资产信息")
@RestController
@RequestMapping("/ists.bd/bdAssets")
@Slf4j
public class BdAssetsController extends JeecgController<BdAssets, IBdAssetsService> {
    @Autowired
    private IBdAssetsService bdAssetsService;

    /**
     * 编辑
     *
     * @param bdAssets
     * @return
     */
    @AutoLog(value = "编辑:单个")
    @ApiOperation(value = "编辑:单个", notes = "资产-编辑:单个")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody BdAssets bdAssets) {

        if (StringUtils.isEmpty(bdAssets.getEquipmentTypeName())) {
            return Result.error("设备类型名称不能为空");
        }
        if (StringUtils.isEmpty(bdAssets.getEquipmentIp())) {
            return Result.error("设备类型IP不能为空");
        }
        if (StringUtils.isEmpty(bdAssets.getEquipmentPort())) {
            return Result.error("设备类型端口不能为空");
        }
        if (StringUtils.isEmpty(bdAssets.getDeployComputerRoom())) {
            return Result.error("部署机房不能为空");
        }
        //255数据库字段长度
        if (bdAssets.getDeployComputerRoom().length() > 255) {
            return Result.error("部署机房输入异常");
        }
        if (StringUtils.isEmpty(bdAssets.getOmPrincipalName()) && StringUtils.isEmpty(bdAssets.getOmPrincipalCode())) {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            if (sysUser == null) {
                return Result.error("未登录");
            }
            bdAssets.setOmPrincipalName(sysUser.getRealname());
            bdAssets.setOmPrincipalCode(sysUser.getUsername());
        }

        try {
            bdAssetsService.updateById(bdAssets);
        } catch (Exception ex) {
            return Result.error(StringUtils.defaultString(ex.getMessage(), ex.getCause().getMessage()), bdAssets);

        }
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param bdAssetsId
     * @return
     */
    @AutoLog(value = "删除:单个")
    @ApiOperation(value = "删除:单个", notes = "资产-删除:单个")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "bdAssetsId", value = "基础数据-资产标识", dataTypeClass = String.class)
    )
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "bdAssetsId", required = true) String bdAssetsId) {

        try {
            bdAssetsService.deleteById(bdAssetsId);
        } catch (Exception ex) {
            return Result.error(StringUtils.defaultString(ex.getMessage(), ex.getCause().getMessage()));
        }
        return Result.OK("删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param bdAssetsId
     * @return
     */
    @AutoLog(value = "查询:单个")
    @ApiOperation(value = "查询:单个", notes = "资产-查询:单个")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "bdAssetsId", value = "基础数据-资产标识", dataTypeClass = String.class)
    )
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "bdAssetsId", required = true) String bdAssetsId) {
        BdAssets bdAssets = bdAssetsService.getById(bdAssetsId);
        if (bdAssets == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bdAssets);
    }
}
