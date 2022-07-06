package com.rainman.modules.ists.bd.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.rainman.modules.ists.bd.dto.BdStrPointsDto;
import com.rainman.modules.ists.bd.dto.BdStrPointsWfInstanceDto;
import com.rainman.modules.ists.bd.entity.BdProject;
import com.rainman.modules.ists.bd.entity.BdStrPoints;
import com.rainman.modules.ists.bd.entity.BdStrPointsWfInstance;
import com.rainman.modules.ists.bd.service.IBdProjectService;
import com.rainman.modules.ists.bd.service.IBdStrPointsWfInstanceService;
import com.rainman.modules.ists.wf.entity.WfInstance;
import com.rainman.modules.ists.wf.service.IWfInstanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.ImportExcelUtil;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @Description: 基础数据-风险与实例
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
@Api(tags = "基础数据-风险与实例")
@RestController
@RequestMapping("/ists.bd/bdStrPointsWfInstance")
@Slf4j
public class BdStrPointsWfInstanceController extends JeecgController<BdStrPointsWfInstance, IBdStrPointsWfInstanceService> {
    @Autowired
    private IBdStrPointsWfInstanceService bdStrPointsWfInstanceService;

    @Autowired
    private IWfInstanceService wfInstanceService;

    @Autowired
    private IBdProjectService bdProjectService;


    /**
     * 分页列表查询
     *
     * @param bdStrPointsWfInstanceDto
     * @param pageNo
     * @param pageSize
     * @return
     */
    @AutoLog(value = "查询:分页:风险点与实例项目")
    @ApiOperation(value = "查询:分页:风险点与实例项目", notes = "风险点-查询:分页:风险点与实例项目")

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "页号", example = "1", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "pageSize", value = "页大小", example = "5", dataTypeClass = Long.class)
    })
    @PostMapping(value = "/pageList")
    public Result<IPage<BdStrPointsDto>> queryPageList(@RequestBody BdStrPointsWfInstanceDto bdStrPointsWfInstanceDto,
                                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                       @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize) {
        return Result.OK(bdStrPointsWfInstanceService.pageByDto(bdStrPointsWfInstanceDto, pageNo, pageSize));
    }

    @AutoLog(value = "查询:列表:风险点与实例项目")
    @ApiOperation(value = "查询:列表:风险点与实例项目", notes = "风险点-查询:列表:风险点与实例项目")
    @PostMapping(value = "/list")
    public Result<List<BdStrPointsDto>> queryList(@RequestBody BdStrPointsWfInstanceDto bdStrPointsWfInstanceDto) {
        return Result.OK(bdStrPointsWfInstanceService.listByDto(bdStrPointsWfInstanceDto));
    }

    /**
     * 添加
     *
     * @param bdStrPointsWfInstanceDto
     * @return
     */
    @AutoLog(value = "添加:单个")
    @ApiOperation(value = "添加:单个", notes = "风险点-添加:单个")
    @PostMapping(value = "/add")
    public Result<BdStrPointsWfInstanceDto> add(@RequestBody BdStrPointsWfInstanceDto bdStrPointsWfInstanceDto) {

        BdStrPointsDto bdStrPointsDto = bdStrPointsWfInstanceDto.getBdStrPointsDto();

        if (bdStrPointsDto == null) {
            return Result.error("风险信息缺失", bdStrPointsWfInstanceDto);
        }

        if (bdStrPointsDto.getRiskPoints() == null) {
            return Result.error("请输入风险点", bdStrPointsWfInstanceDto);
        }

        if (bdStrPointsDto.getRectificationDescription() == null) {
            return Result.error("请输入风险描述", bdStrPointsWfInstanceDto);
        }
        if (bdStrPointsDto.getRectificationProposal() == null) {
            return Result.error("请输入整改内容", bdStrPointsWfInstanceDto);
        }
        if (bdStrPointsDto.getRiskRectificationBeginDate() == null) {
            return Result.error("请输入整改内容", bdStrPointsWfInstanceDto);
        }
        if (bdStrPointsDto.getRiskRectificationEndDate() == null) {
            return Result.error("请输入整改内容", bdStrPointsWfInstanceDto);
        }

        WfInstance wfInstance = wfInstanceService.getById(bdStrPointsWfInstanceDto.getWfInstanceId());

        if (wfInstance == null) {
            return Result.error("查询不到：流程实例", bdStrPointsWfInstanceDto);
        }

        if ("aceept_node".equals(wfInstance.getWfCode())) {
            if (StringUtils.isEmpty(bdStrPointsWfInstanceDto.getBdStrPointsDto().getRectificationResult())) {
                return Result.error("整改结果不能为空", bdStrPointsWfInstanceDto);
            }
        }

        BdProject bdProject = bdProjectService.getById(bdStrPointsWfInstanceDto.getBdProjectId());

        if (bdProject == null) {
            return Result.error("查询不到：项目信息", bdStrPointsWfInstanceDto);
        }

        BdStrPoints bdStrPoints = new BdStrPoints();

        BeanUtils.copyProperties(bdStrPointsWfInstanceDto.getBdStrPointsDto(), bdStrPoints);

        BdStrPointsWfInstance bdStrPointsWfInstance = new BdStrPointsWfInstance();

        BeanUtils.copyProperties(bdStrPointsWfInstanceDto, bdStrPointsWfInstance);

        Long id = IdWorker.getId();

        bdStrPoints.setBdStrPointsId(id.toString());

        bdStrPointsWfInstance.setBdStrPointsId(id.toString());
        try {
            bdStrPointsWfInstanceService.saveInfo(bdStrPoints, bdStrPointsWfInstance);
        } catch (Exception ex) {
            return Result.error(StringUtils.defaultString(ex.getMessage(), ex.getCause().getMessage()), bdStrPointsWfInstanceDto);

        }
        return Result.OK("添加成功！", bdStrPointsWfInstanceDto);
    }


    /**
     * 通过id删除
     *
     * @param bdStrPointsWfInstanceId
     * @return
     */
    @AutoLog(value = "删除:单个")
    @ApiOperation(value = "删除:单个", notes = "风险点-删除:单个")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bdStrPointsWfInstanceId", value = "安全测试风险点与流程-实例标识", dataTypeClass = String.class)
    })
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "bdStrPointsWfInstanceId", required = true) String bdStrPointsWfInstanceId) {

        String bdStrPointsId = bdStrPointsWfInstanceService.getById(bdStrPointsWfInstanceId).getBdStrPointsId();

        bdStrPointsWfInstanceService.deleteByIds(bdStrPointsWfInstanceId, bdStrPointsId);

        return Result.OK("删除成功!");
    }


    /**
     * 导入：批量：风险点
     *
     * @return
     */
    @ApiOperation(value = "导入：批量：风险点", notes = "同步建设-安全验收-风险点录入-批量导入")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "wfInstanceId", value = "流程-流程实例标识", dataTypeClass = String.class),
            @ApiImplicitParam(name = "bdProjectId", value = "项目-项目实例标识", dataTypeClass = String.class)
    })
    @RequestMapping(value = "/importExcelJoin", method = RequestMethod.POST)
    public Result<?> importExcelJoin(@RequestParam("file") MultipartFile file, @RequestParam(name = "wfInstanceId") String wfInstanceId, @RequestParam(name = "bdProjectId") String bdProjectId) {
        if (file.isEmpty()) {
            return Result.error("没有上传文件");
        }

        if (StringUtils.isBlank(wfInstanceId)) {
            return Result.error("流程实例标识不能为空值");
        }
        if (StringUtils.isBlank(bdProjectId)) {
            return Result.error("项目实例标识不能为空值");
        }

        Table<String, String, String> dictTable = HashBasedTable.create();

        SpringContextUtils.getBean(ISysBaseAPI.class)
                .queryDictItemsByCode("risk_level")
                .forEach(dictModel -> {
                    dictTable.put("risk_level", dictModel.getText(), dictModel.getValue());
                });
        //Excel文件导入解析
        ImportParams params = new ImportParams();
        params.setTitleRows(0);
        params.setHeadRows(0);
        params.setNeedSave(true);

        try {
            InputStream is = file.getInputStream();

            List<Map<String, Object>> mapList = ExcelImportUtil.importExcel(is, Map.class, params);

            List<String> errorMessage = Lists.newArrayList();

            int successLines = 0;

            int errorLines = 0;

            //写入表中
            for (int i = 0; i < mapList.size(); i++) {
                Map<String, Object> temp = mapList.get(i);

                BdStrPoints bdStrPoints = new BdStrPoints();

                bdStrPoints.setRiskPoints(MapUtils.getString(temp, "风险点"));
                String riskLevel = MapUtils.getString(temp, "风险等级");
                bdStrPoints.setRiskLevelCode(dictTable.get("risk_level", riskLevel));
                bdStrPoints.setRectificationDescription(MapUtils.getString(temp, "风险描述"));
                bdStrPoints.setRectificationProposal(MapUtils.getString(temp, "整改内容"));

                String beginDateString = MapUtils.getString(temp, "整改开始时间");
                bdStrPoints.setRiskRectificationBeginDate(beginDateString);
                String endDateString = MapUtils.getString(temp, "整改完成时间");
                bdStrPoints.setRiskRectificationEndDate(endDateString);

                bdStrPoints.setRectificationResult(MapUtils.getString(temp, temp.get("整改结果")));

                BdStrPointsWfInstanceDto bdStrPointsWfInstanceDto = new BdStrPointsWfInstanceDto();

                BdStrPointsDto bdStrPointsDto = new BdStrPointsDto();
                BeanUtils.copyProperties(bdStrPoints, bdStrPointsDto);

                bdStrPointsWfInstanceDto.setBdStrPointsDto(bdStrPointsDto);
                bdStrPointsWfInstanceDto.setWfInstanceId(wfInstanceId);
                bdStrPointsWfInstanceDto.setBdProjectId(bdProjectId);

                Result<?> ans = this.add(bdStrPointsWfInstanceDto);

                if (ans.isSuccess()) {
                    successLines++;

                    continue;
                }

                errorLines++;

                int lineNumber = i + 1;

                errorMessage.add("第 " + lineNumber + " 行：" + ans.getMessage());
            }

            return ImportExcelUtil.imporReturnRes(errorLines, successLines, errorMessage);
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            return Result.error("文件导入失败:" + e.getMessage());
        }
    }
}
