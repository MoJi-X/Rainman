package com.rainman.modules.ists.bd.controller;

import com.rainman.modules.ists.bd.dto.BdStrPointsDto;
import com.rainman.modules.ists.bd.entity.BdStrPoints;
import com.rainman.modules.ists.bd.service.IBdStrPointsService;
import com.rainman.modules.ists.wf.entity.WfInstance;
import com.rainman.modules.ists.wf.service.IWfInstanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.SpringContextUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @Description: 风险点
 * @Author: xiezhenhao
 * @Date: 2021-12-2
 * @Version: V1.0
 */
@Api(tags = "基础数据-风险点")
@RestController
@RequestMapping("/ists.bd/bdStrPoints")
@Slf4j
public class BdStrPointsController extends JeecgController<BdStrPoints, IBdStrPointsService> {
    @Autowired
    private IBdStrPointsService bdStrPointsService;

    @Autowired
    private IWfInstanceService wfInstanceService;

    /**
     * 编辑
     *
     * @param bdStrPointsDto
     * @return
     */
    @AutoLog(value = "编辑:单个")
    @ApiOperation(value = "编辑:单个", notes = "风险点-编辑:单个")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "wfInstanceId", value = "流程实例标识")
    })
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody BdStrPointsDto bdStrPointsDto, String wfInstanceId) {

        if (StringUtils.isEmpty(bdStrPointsDto.getBdStrPointsId())) {
            return Result.error("安全测试风险点标识不能为空");
        }
        if (StringUtils.isEmpty(bdStrPointsDto.getRiskPoints())) {
            return Result.error("风险点不能为空");
        }
        if (StringUtils.isEmpty(bdStrPointsDto.getRiskRectificationBeginDate())) {
            return Result.error("风险整改开始时间不能为空");
        }
        if (StringUtils.isEmpty(bdStrPointsDto.getRectificationDescription())) {
            return Result.error("整改描述不能为空");
        }
        if (StringUtils.isEmpty(bdStrPointsDto.getRectificationProposal())) {
            return Result.error("整改建议不能为空");
        }
        if (StringUtils.isEmpty(bdStrPointsDto.getRiskRectificationBeginDate())) {
            return Result.error("整改开始时间不能为空");
        }
        if (StringUtils.isEmpty(bdStrPointsDto.getRiskRectificationEndDate())) {
            return Result.error("整改完成时间不能为空");
        }

        WfInstance wfInstance = wfInstanceService.getById(wfInstanceId);

        if (wfInstance == null) {
            return Result.error("查询不到：流程实例");
        }

        if ("accept_node".equals(wfInstance.getWfCode())) {
            if (StringUtils.isEmpty(bdStrPointsDto.getRectificationResult())) {
                return Result.error("整改结果不能为空");
            }
        }

        BdStrPoints bdStrPoints = new BdStrPoints();

        BeanUtils.copyProperties(bdStrPointsDto, bdStrPoints);
        try {
            bdStrPointsService.updateById(bdStrPoints);
        } catch (Exception ex) {
            return Result.error(StringUtils.defaultString(ex.getMessage(), ex.getCause().getMessage()));
        }
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param bdStrPointsId
     * @return
     */
    @AutoLog(value = "删除:单个")
    @ApiOperation(value = "删除:单个", notes = "风险点-删除:单个")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bdStrPointsId", value = "风险点标识")
    })
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "bdStrPointsId", required = true) String bdStrPointsId) {

        try {

            bdStrPointsService.deleteById(bdStrPointsId);

        } catch (Exception ex) {
            return Result.error(StringUtils.defaultString(ex.getMessage(), ex.getCause().getMessage()));
        }
        return Result.OK("删除成功!");
    }


    /**
     * 通过id查询
     *
     * @param bdStrPointsId
     * @return
     */
    @AutoLog(value = "查询:单个")
    @ApiOperation(value = "查询:单个", notes = "风险点-查询:单个")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bdStrPointsId", value = "风险点标识")
    })
    @GetMapping(value = "/queryById")
    public Result<BdStrPointsDto> queryById(@RequestParam(name = "bdStrPointsId", required = true) String bdStrPointsId) {
        BdStrPoints bdStrPoints = bdStrPointsService.getById(bdStrPointsId);
        BdStrPointsDto bdStrPointsDto = new BdStrPointsDto();

        if (bdStrPoints == null) {
            return Result.error("未找到对应数据", null);
        }

        if (StringUtils.isNotEmpty(bdStrPoints.getRiskLevelCode())) {

            String riskLevelName = SpringContextUtils.getBean(ISysBaseAPI.class)
                    .translateDict("risk_level", bdStrPoints.getRiskLevelCode());

            bdStrPointsDto.setRiskLevelName(riskLevelName);

        }

        BeanUtils.copyProperties(bdStrPoints, bdStrPointsDto);


        return Result.OK(bdStrPointsDto);
    }

}
