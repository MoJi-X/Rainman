package com.rainman.modules.ists.bd.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.rainman.modules.ists.bd.dto.BdFileDto;
import com.rainman.modules.ists.bd.dto.BdProjectDto;
import com.rainman.modules.ists.bd.dto.BdProjectWfInstanceDto;
import com.rainman.modules.ists.bd.entity.BdProject;
import com.rainman.modules.ists.bd.entity.BdProjectWfInstance;
import com.rainman.modules.ists.bd.service.IBdFileService;
import com.rainman.modules.ists.bd.service.IBdProjectService;
import com.rainman.modules.ists.bd.service.IBdProjectWfInstanceService;
import com.rainman.modules.ists.wf.dto.WfInstanceDto;
import com.rainman.modules.ists.wf.entity.WfInstance;
import com.rainman.modules.ists.wf.service.IWfInstanceService;
import com.rainman.util.DateUtils;
import com.rainman.util.RunnableUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.ImportExcelUtil;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description： 基础数据-项目与流程实例
 * @Author： yao-hai-tao
 * @Date： 2021-11-23
 * @Version： V1.0
 */
@Api(tags = "基础数据-项目与流程实例")
@RestController
@RequestMapping("/ists.bd/bdProjectWfInstance")
@Slf4j
public class BdProjectWfInstanceController extends JeecgController<BdProjectWfInstance, IBdProjectWfInstanceService> {
    @Autowired
    private IBdProjectWfInstanceService bdProjectWfInstanceService;

    @Autowired
    private IBdProjectService bdProjectService;

    @Autowired
    private IWfInstanceService wfInstanceService;

    @Autowired
    private IBdFileService bdFileService;

    /**
     * 查询：分页：流程编号
     *
     * @param bdProjectWfInstanceDto
     * @return
     */
    @AutoLog(value = "查询：分页：系统定级")
    @ApiOperation(value = "查询：分页：系统定级", notes = "查询：分页：系统定级")
    @PostMapping(value = "/pageForProjectRank")
    public Result<IPage<BdProjectWfInstanceDto>> pageForProjectRank(@RequestBody BdProjectWfInstanceDto bdProjectWfInstanceDto) {
        IPage<Map<String, Object>> page = bdProjectWfInstanceService.pageForProjectRank(bdProjectWfInstanceDto);

        IPage<BdProjectWfInstanceDto> newPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());

        newPage.setRecords(Lists.newArrayList());

        page.getRecords().forEach(map -> {
            BdProjectWfInstanceDto item = new BdProjectWfInstanceDto();

            newPage.getRecords().add(item);

            item.setWfInstanceId(MapUtils.getString(map, "wf_instance_id"));

            item.setWfInstance(new WfInstanceDto());

            item.getWfInstance().setTaskId(MapUtils.getString(map, "task_id"));
            item.getWfInstance().setWfInstanceId(MapUtils.getString(map, "wf_instance_id"));
            item.getWfInstance().setName(MapUtils.getString(map, "name"));
            item.getWfInstance().setWfCode(MapUtils.getString(map, "wf_code"));
            item.getWfInstance().setWfNodeCode(MapUtils.getString(map, "wf_node_code"));
            item.getWfInstance().setStateCode(MapUtils.getString(map, "state_code"));

            if (StringUtils.isNotEmpty(item.getWfInstance().getStateCode())) {
                RunnableUtils.execute(() -> {
                    String stateName = SpringContextUtils.getBean(ISysBaseAPI.class)
                            .translateDict("process_status", item.getWfInstance().getStateCode());

                    item.getWfInstance().setStateName(stateName);
                });
            }

            if (StringUtils.isNotEmpty(item.getWfInstance().getWfNodeCode())) {
                RunnableUtils.execute(() -> {
                    String wfNodeName = SpringContextUtils.getBean(ISysBaseAPI.class)
                            .translateDict(item.getWfInstance().getWfCode() + "_node", item.getWfInstance().getWfNodeCode());

                    item.getWfInstance().setWfNodeName(wfNodeName);
                });
            }
        });

        RunnableUtils.waitExecute();

        return Result.OK(newPage);
    }

    /**
     * 查询：分页：流程编号
     *
     * @param bdProjectWfInstanceDto
     * @return
     */
    @AutoLog(value = "查询：分页：流程编号")
    @ApiOperation(value = "查询：分页：流程编号", notes = "查询：分页：流程编号")
    @PostMapping(value = "/pageByWfCode")
    public Result<IPage<BdProjectWfInstanceDto>> pageByWfCode(@RequestBody BdProjectWfInstanceDto bdProjectWfInstanceDto) {
        IPage<Map<String, Object>> page = bdProjectWfInstanceService.pageByWfCode(bdProjectWfInstanceDto);

        IPage<BdProjectWfInstanceDto> newPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());

        newPage.setRecords(Lists.newArrayList());

        page.getRecords().forEach(map -> {
            BdProjectWfInstanceDto item = new BdProjectWfInstanceDto();

            newPage.getRecords().add(item);

            item.setWfInstanceId(MapUtils.getString(map, "wf_instance_id"));

            item.setBdProject(new BdProjectDto());
            item.getBdProject().setName(MapUtils.getString(map, "name"));
            item.getBdProject().setYear(MapUtils.getInteger(map, "year"));
            item.getBdProject().setSystemTypeCode(MapUtils.getString(map, "system_type_code"));
            item.getBdProject().setSystemGradingCode(MapUtils.getString(map, "system_grading_code"));

            item.setWfInstance(new WfInstanceDto());

            item.getWfInstance().setTaskId(MapUtils.getString(map, "task_id"));
            item.getWfInstance().setWfInstanceId(MapUtils.getString(map, "wf_instance_id"));
            item.getWfInstance().setWfCode(MapUtils.getString(map, "wf_code"));
            item.getWfInstance().setWfNodeCode(MapUtils.getString(map, "wf_node_code"));
            item.getWfInstance().setStateCode(MapUtils.getString(map, "state_code"));

            if (StringUtils.isNotEmpty(item.getBdProject().getSystemTypeCode())) {
                RunnableUtils.execute(() -> {
                    String systemTypeName = SpringContextUtils.getBean(ISysBaseAPI.class)
                            .translateDict("system_type", item.getBdProject().getSystemTypeCode());

                    item.getBdProject().setSystemTypeName(systemTypeName);
                });
            }

            if (StringUtils.isNotEmpty(item.getBdProject().getSystemGradingCode())) {
                RunnableUtils.execute(() -> {
                    String systemGradingName = SpringContextUtils.getBean(ISysBaseAPI.class)
                            .translateDict("project_rank", item.getBdProject().getSystemGradingCode());

                    item.getBdProject().setSystemGradingName(systemGradingName);
                });
            }

            if (StringUtils.isNotEmpty(item.getWfInstance().getStateCode())) {
                RunnableUtils.execute(() -> {
                    String stateName = SpringContextUtils.getBean(ISysBaseAPI.class)
                            .translateDict("process_status", item.getWfInstance().getStateCode());

                    item.getWfInstance().setStateName(stateName);
                });
            }

            if (StringUtils.isNotEmpty(item.getWfInstance().getWfNodeCode())) {
                RunnableUtils.execute(() -> {
                    String wfNodeName = SpringContextUtils.getBean(ISysBaseAPI.class)
                            .translateDict(item.getWfInstance().getWfCode() + "_node", item.getWfInstance().getWfNodeCode());

                    item.getWfInstance().setWfNodeName(wfNodeName);
                });
            }
        });

        RunnableUtils.waitExecute();

        return Result.OK(newPage);
    }

    /**
     * 查询：列表：流程实例标识
     *
     * @param wfInstanceId 流程-实例标识
     * @return
     */
    @AutoLog(value = "查询：列表：流程实例标识")
    @ApiOperation(value = "查询：列表：流程实例标识", notes = "查询：列表：流程实例标识")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "wfInstanceId", value = "流程-流程实例标识", dataTypeClass = String.class)
    )
    @GetMapping(value = "/listByWfInstanceId")
    public Result<List<BdProjectWfInstanceDto>> listByWfInstanceId(@RequestParam(name = "wfInstanceId") String wfInstanceId) {
        List<BdProjectWfInstanceDto> bdProjectWfInstanceDtoList = Lists.newArrayList();

        if (StringUtils.isEmpty(wfInstanceId)) {
            return Result.error("流程-流程实例标识不能为空值", bdProjectWfInstanceDtoList);
        }

        WfInstance wfInstance = wfInstanceService.getById(wfInstanceId);

        if (wfInstance == null) {
            return Result.error("流程-流程实例标识查询不到对应的流程-流程实例", bdProjectWfInstanceDtoList);
        }

        List<Map<String, Object>> mapList = bdProjectWfInstanceService.listByWfInstanceId(wfInstanceId);

        for (Map<String, Object> map : mapList) {
            BdProjectWfInstanceDto bdProjectWfInstanceDto = new BdProjectWfInstanceDto();

            bdProjectWfInstanceDtoList.add(bdProjectWfInstanceDto);

            bdProjectWfInstanceDto.setBdProjectWfInstanceId(MapUtils.getString(map, "bd_project_wf_instance_id"));
            bdProjectWfInstanceDto.setBdProjectId(MapUtils.getString(map, "bd_project_id"));
            bdProjectWfInstanceDto.setWfInstanceId(MapUtils.getString(map, "wf_instance_id"));

            bdProjectWfInstanceDto.setBdProject(new BdProjectDto());

            bdProjectWfInstanceDto.getBdProject().setBdProjectId(MapUtils.getString(map, "bd_project_id"));
            bdProjectWfInstanceDto.getBdProject().setName(MapUtils.getString(map, "name"));
            bdProjectWfInstanceDto.getBdProject().setYear(MapUtils.getInteger(map, "year"));
            bdProjectWfInstanceDto.getBdProject().setProjectLeaderCode(MapUtils.getString(map, "project_leader_code"));
            bdProjectWfInstanceDto.getBdProject().setSystemGradingCode(MapUtils.getString(map, "system_grading_code"));

            if (StringUtils.isNotEmpty(bdProjectWfInstanceDto.getBdProject().getSystemGradingCode())) {
                RunnableUtils.execute(() -> {
                    String systemGradingName = SpringContextUtils.getBean(ISysBaseAPI.class)
                            .translateDict("project_rank", bdProjectWfInstanceDto.getBdProject().getSystemGradingCode());

                    bdProjectWfInstanceDto.getBdProject().setSystemGradingName(systemGradingName);
                });
            }

            if (StringUtils.isNotEmpty(bdProjectWfInstanceDto.getBdProject().getProjectLeaderCode())) {
                LoginUser loginUser = SpringContextUtils.getBean(ISysBaseAPI.class)
                        .getUserByName(bdProjectWfInstanceDto.getBdProject().getProjectLeaderCode());

                if (loginUser != null) {
                    bdProjectWfInstanceDto.getBdProject().setProjectLeaderName(loginUser.getRealname());
                }
            }

            RunnableUtils.execute(() -> {
                List<BdFileDto> bdFileList = bdFileService.listByWfCode(wfInstance.getWfCode(),
                        wfInstance.getWfInstanceId(),
                        "bd_project",
                        bdProjectWfInstanceDto.getBdProject().getBdProjectId());

                bdProjectWfInstanceDto.getBdProject().setBdFileList(bdFileList);
            });
        }

        RunnableUtils.waitExecute();

        return Result.OK(bdProjectWfInstanceDtoList);
    }

    /**
     * 新增或更新：单个
     *
     * @param wfInstanceId
     * @param bdProjectId
     * @return
     */
    @AutoLog(value = "新增或更新：单个")
    @ApiOperation(value = "新增或更新：单个", notes = "新增或更新：单个")
    @PostMapping(value = "/saveOrUpdate")
    public Result<BdProjectWfInstanceDto> saveOrUpdate(@RequestParam(name = "wfInstanceId") String wfInstanceId,
                                                       @RequestParam(name = "bdProjectId") String bdProjectId) {
        BdProjectWfInstanceDto bdProjectWfInstanceDto = new BdProjectWfInstanceDto();

        if (StringUtils.isEmpty(wfInstanceId)) {
            return Result.error("流程-流程实例标识不能为空值", bdProjectWfInstanceDto);
        }

        if (StringUtils.isEmpty(bdProjectId)) {
            return Result.error("基础数据-项目标识不能为空", bdProjectWfInstanceDto);
        }

        BdProject bdProject = bdProjectService.getById(bdProjectId);

        if (bdProject == null) {
            return Result.error("查询不到：基础数据-项目", bdProjectWfInstanceDto);
        }

        WfInstance wfInstance = wfInstanceService.getById(wfInstanceId);

        if (wfInstance == null) {
            return Result.error("查询不到：流程实例", bdProjectWfInstanceDto);
        }

        try {
            bdProjectWfInstanceService.saveOrUpdate(wfInstanceId, bdProjectId);
        } catch (Exception ex) {
            return Result.error(StringUtils.defaultString(ex.getMessage(), ex.getCause().getMessage()), bdProjectWfInstanceDto);
        }

        return Result.OK("新增或更新：单个：成功！", bdProjectWfInstanceDto);
    }

    /**
     * 新增：单个：含新增项目
     *
     * @param bdProjectWfInstanceDto
     * @return
     */
    @AutoLog(value = "新增：单个：含新增项目")
    @ApiOperation(value = "新增：单个：含新增项目", notes = "新增：单个：含新增项目")
    @PostMapping(value = "/add")
    public Result<BdProjectWfInstanceDto> add(@RequestBody BdProjectWfInstanceDto bdProjectWfInstanceDto) {
        if (StringUtils.isEmpty(bdProjectWfInstanceDto.getWfInstanceId())) {
            return Result.error("流程-流程实例标识不能为空值", bdProjectWfInstanceDto);
        }

        if (bdProjectWfInstanceDto.getBdProject() == null) {
            return Result.error("基础数据-项目不能为空", bdProjectWfInstanceDto);
        }

        if (StringUtils.isEmpty(bdProjectWfInstanceDto.getBdProject().getName())) {
            return Result.error("基础数据-项目名称不能为空", bdProjectWfInstanceDto);
        }

        if (bdProjectWfInstanceDto.getBdProject().getYear() == null) {
            return Result.error("基础数据-项目年度不能为空", bdProjectWfInstanceDto);
        }

        WfInstance wfInstance = wfInstanceService.getById(bdProjectWfInstanceDto.getWfInstanceId());

        if (wfInstance == null) {
            return Result.error("流程-流程实例标识查询不到对应的流程-流程实例", bdProjectWfInstanceDto);
        }

        try {
            bdProjectWfInstanceService.save(bdProjectWfInstanceDto);
        } catch (Exception ex) {
            return Result.error(StringUtils.defaultString(ex.getMessage(), ex.getCause().getMessage()), bdProjectWfInstanceDto);
        }

        return Result.OK("新增：单个：含新增项目：成功！", bdProjectWfInstanceDto);
    }

    /**
     * 更新：单个：含修改项目
     *
     * @param bdProjectWfInstanceDto
     * @return
     */
    @AutoLog(value = "更新：单个：含修改项目")
    @ApiOperation(value = "更新：单个：含修改项目", notes = "更新：单个：含修改项目")
    @PostMapping(value = "/updateByBdProjectWfInstanceId")
    public Result<BdProjectWfInstanceDto> updateByBdProjectWfInstanceId(@RequestBody BdProjectWfInstanceDto bdProjectWfInstanceDto) {
        if (StringUtils.isEmpty(bdProjectWfInstanceDto.getBdProjectWfInstanceId())) {
            return Result.error("基础数据-项目与流程实例标识不能为空值", bdProjectWfInstanceDto);
        }

        if (StringUtils.isEmpty(bdProjectWfInstanceDto.getWfInstanceId())) {
            return Result.error("流程-流程实例标识不能为空值", bdProjectWfInstanceDto);
        }

        if (bdProjectWfInstanceDto.getBdProject() == null) {
            return Result.error("基础数据-项目不能为空", bdProjectWfInstanceDto);
        }

        if (StringUtils.isEmpty(bdProjectWfInstanceDto.getBdProject().getBdProjectId())) {
            return Result.error("基础数据-项目标识不能为空", bdProjectWfInstanceDto);
        }

        if (StringUtils.isEmpty(bdProjectWfInstanceDto.getBdProject().getName())) {
            return Result.error("基础数据-项目名称不能为空", bdProjectWfInstanceDto);
        }

        if (bdProjectWfInstanceDto.getBdProject().getYear() == null) {
            return Result.error("基础数据-项目年度不能为空", bdProjectWfInstanceDto);
        }

        BdProject bdProject = bdProjectService.getById(bdProjectWfInstanceDto.getBdProject().getBdProjectId());

        if (bdProject == null) {
            return Result.error("查询不到：基础数据-项目", bdProjectWfInstanceDto);
        }

        WfInstance wfInstance = wfInstanceService.getById(bdProjectWfInstanceDto.getWfInstanceId());

        if (wfInstance == null) {
            return Result.error("查询不到：流程实例", bdProjectWfInstanceDto);
        }

        BdProjectWfInstance bdProjectWfInstance = bdProjectWfInstanceService.getById(bdProjectWfInstanceDto.getBdProjectWfInstanceId());

        if (bdProjectWfInstance == null) {
            return Result.error("查询不到：基础数据-项目与流程实例", bdProjectWfInstanceDto);
        }

        try {
            bdProjectWfInstanceService.updateById(bdProjectWfInstanceDto);
        } catch (Exception ex) {
            return Result.error(StringUtils.defaultString(ex.getMessage(), ex.getCause().getMessage()), bdProjectWfInstanceDto);
        }

        return Result.OK("更新：单个：含新增项目：成功!", bdProjectWfInstanceDto);
    }

    /**
     * 删除：单个
     *
     * @param bdProjectWfInstanceId
     * @return
     */
    @AutoLog(value = "删除：单个")
    @ApiOperation(value = "删除：单个", notes = "删除：单个")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "bdProjectWfInstanceId", value = "基础数据-项目与流程实例标识", dataTypeClass = String.class)
    )
    @DeleteMapping(value = "/removeByBdProjectWfInstanceId")
    public Result<?> removeByBdProjectWfInstanceId(@RequestParam(name = "bdProjectWfInstanceId") String bdProjectWfInstanceId) {
        if (StringUtils.isEmpty(bdProjectWfInstanceId)) {
            return Result.error("基础数据-项目与流程实例标识不能为空值");
        }

        bdProjectWfInstanceService.removeById(bdProjectWfInstanceId);

        return Result.OK("删除成功!");
    }

    /**
     * 删除：批量
     *
     * @param bdProjectWfInstanceIds
     * @return
     */
    @AutoLog(value = "删除：批量")
    @ApiOperation(value = "删除：批量", notes = "删除：批量")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "bdProjectWfInstanceIds", value = "基础数据-项目与流程实例标识集合，逗号分隔", dataTypeClass = String.class, example = "1,2,3")
    )
    @DeleteMapping(value = "/removeByBdProjectWfInstanceIds")
    public Result<?> removeByBdProjectWfInstanceIds(@RequestParam(name = "bdProjectWfInstanceIds") String bdProjectWfInstanceIds) {
        if (StringUtils.isEmpty(bdProjectWfInstanceIds)) {
            return Result.error("基础数据-项目与流程实例标识集合不能为空值");
        }

        this.bdProjectWfInstanceService.removeByIds(Arrays.asList(bdProjectWfInstanceIds.split(",")));

        return Result.OK("删除：批量成功!");
    }

    /**
     * 查询：单个
     *
     * @param bdProjectWfInstanceId
     * @return
     */
    @AutoLog(value = "查询：单个")
    @ApiOperation(value = "查询：单个", notes = "查询：单个")
    @GetMapping(value = "/getByBdProjectWfInstanceId")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "bdProjectWfInstanceId", value = "基础数据-项目与流程实例标识", dataTypeClass = String.class)
    )
    public Result<BdProjectWfInstanceDto> getByBdProjectWfInstanceId(@RequestParam(name = "bdProjectWfInstanceId") String bdProjectWfInstanceId) {
        BdProjectWfInstanceDto bdProjectWfInstanceDto = new BdProjectWfInstanceDto();

        if (StringUtils.isEmpty(bdProjectWfInstanceId)) {
            return Result.error("基础数据-项目与流程实例标识不能为空值", bdProjectWfInstanceDto);
        }

        BdProjectWfInstance bdProjectWfInstance = bdProjectWfInstanceService.getById(bdProjectWfInstanceId);

        if (bdProjectWfInstance == null) {
            return Result.error("未查询到项目与流程实例数据", bdProjectWfInstanceDto);
        }

        BeanUtils.copyProperties(bdProjectWfInstance, bdProjectWfInstanceDto);

        BdProject bdProject = bdProjectService.getById(bdProjectWfInstanceDto.getBdProjectId());

        if (bdProject == null) {
            bdProject = new BdProject();
        }

        BdProjectDto bdProjectDto = new BdProjectDto();

        bdProjectWfInstanceDto.setBdProject(bdProjectDto);

        BeanUtils.copyProperties(bdProject, bdProjectDto);

        if (bdProjectDto.getProjectInitiationTime() != null) {
            bdProjectDto.setProjectInitiationTimeString(DateFormatUtils.format(bdProjectDto.getProjectInitiationTime(), "yyyy-MM-dd HH:mm"));
        }

        if (StringUtils.isNotEmpty(bdProjectDto.getSystemGradingCode())) {
            RunnableUtils.execute(() -> {
                String systemGradingName = SpringContextUtils.getBean(ISysBaseAPI.class)
                        .translateDict("project_rank", bdProjectDto.getSystemGradingCode());

                bdProjectDto.setSystemGradingName(systemGradingName);
            });
        }

        if (StringUtils.isNotEmpty(bdProjectDto.getSystemTypeCode())) {
            RunnableUtils.execute(() -> {
                String systemTypeName = SpringContextUtils.getBean(ISysBaseAPI.class)
                        .translateDict("system_type", bdProjectDto.getSystemTypeCode());

                bdProjectDto.setSystemTypeName(systemTypeName);
            });
        }

        if (StringUtils.isNotEmpty(bdProjectDto.getExpenditureTypeCode())) {
            RunnableUtils.execute(() -> {
                String expenditureTypeName = SpringContextUtils.getBean(ISysBaseAPI.class)
                        .translateDict("system_spending_type", bdProjectDto.getExpenditureTypeCode());

                bdProjectDto.setExpenditureTypeName(expenditureTypeName);
            });
        }

        if (StringUtils.isNotEmpty(bdProjectDto.getServiceOpeningScopeCode())) {
            RunnableUtils.execute(() -> {
                String serviceOpeningScopeName = SpringContextUtils.getBean(ISysBaseAPI.class)
                        .translateDict("system_service_coverage", bdProjectDto.getServiceOpeningScopeCode());

                bdProjectDto.setServiceOpeningScopeName(serviceOpeningScopeName);
            });
        }

        if (StringUtils.isNotEmpty(bdProjectDto.getProjectLeaderCode())) {
            LoginUser loginUser = SpringContextUtils.getBean(ISysBaseAPI.class)
                    .getUserByName(bdProjectDto.getProjectLeaderCode());

            if (loginUser != null) {
                bdProjectDto.setProjectLeaderName(loginUser.getRealname());
            }
        }

        if (StringUtils.isNotEmpty(bdProjectDto.getOmPrincipalCode())) {
            LoginUser loginUser = SpringContextUtils.getBean(ISysBaseAPI.class)
                    .getUserByName(bdProjectDto.getOmPrincipalCode());

            if (loginUser != null) {
                bdProjectDto.setOmPrincipalName(loginUser.getRealname());
            }
        }

        if (StringUtils.isNotEmpty(bdProjectDto.getOmDepartmentCode())) {
            RunnableUtils.execute(() -> {
                String omDepartmentName = SpringContextUtils.getBean(ISysBaseAPI.class)
                        .getDepartNameByOrgCode(bdProjectDto.getOmDepartmentCode());

                bdProjectDto.setOmDepartmentName(omDepartmentName);
            });
        }

        RunnableUtils.execute(() -> {
            WfInstance wfInstance = wfInstanceService.getById(bdProjectWfInstanceDto.getWfInstanceId());

            WfInstanceDto wfInstanceDto = new WfInstanceDto();

            BeanUtils.copyProperties(wfInstance, wfInstanceDto);

            bdProjectWfInstanceDto.setWfInstance(wfInstanceDto);
        });

        RunnableUtils.waitExecute();

        return Result.OK(bdProjectWfInstanceDto);
    }

    /**
     * 导入：批量：关联流程实例标识
     *
     * @return
     */
    @ApiOperation(value = "导入：批量：关联流程实例标识", notes = "同步规划-系统定级-系统录入-批量导入")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "wfInstanceId", value = "流程-流程实例标识", dataTypeClass = String.class)
    })
    @RequestMapping(value = "/importExcelJoinWfInstanceId", method = RequestMethod.POST)
    public Result<?> importExcelJoinWfInstanceId(@RequestParam("file") MultipartFile file, @RequestParam(name = "wfInstanceId") String wfInstanceId) {
        if (file.isEmpty()) {
            return Result.error("没有上传文件");
        }

        if (StringUtils.isBlank(wfInstanceId)) {
            return Result.error("流程实例标识不能为空值");
        }

        ImportParams params = new ImportParams();
        params.setTitleRows(0);
        params.setHeadRows(0);
        params.setNeedSave(true);

        Table<String, String, String> dictTable = HashBasedTable.create();

        Lists.newArrayList(
                "project_rank",
                "system_type",
                "system_spending_type",
                "system_service_coverage"
        ).forEach(dictCode -> {
            SpringContextUtils.getBean(ISysBaseAPI.class)
                    .queryDictItemsByCode(dictCode)
                    .forEach(dictModel -> {
                        dictTable.put(dictCode, dictModel.getText(), dictModel.getValue());
                    });
        });

        try (InputStream is = file.getInputStream()) {
            List<Map<String, Object>> mapList = ExcelImportUtil.importExcel(is, Map.class, params);

            List<String> errorMessage = Lists.newArrayList();

            int successLines = 0;

            int errorLines = 0;

            for (int i = 0; i < mapList.size(); i++) {
                Map<String, Object> map = mapList.get(i);

                BdProjectWfInstanceDto bdProjectWfInstanceDto = new BdProjectWfInstanceDto();

                bdProjectWfInstanceDto.setWfInstanceId(wfInstanceId);
                bdProjectWfInstanceDto.setBdProject(new BdProjectDto());

                bdProjectWfInstanceDto.getBdProject().setName(MapUtils.getString(map, "系统名称"));
                bdProjectWfInstanceDto.getBdProject().setDescription(MapUtils.getString(map, "系统简介"));
                bdProjectWfInstanceDto.getBdProject().setYear(MapUtils.getInteger(map, "年度"));

                String systemTypeName = MapUtils.getString(map, "系统类型");
                bdProjectWfInstanceDto.getBdProject().setSystemTypeCode(dictTable.get("system_type", systemTypeName));

                String systemGradingName = MapUtils.getString(map, "自主定级");
                bdProjectWfInstanceDto.getBdProject().setSystemGradingCode(dictTable.get("project_rank", systemGradingName));

                String expenditureTypeName = MapUtils.getString(map, "开支类型");
                bdProjectWfInstanceDto.getBdProject().setExpenditureTypeCode(dictTable.get("system_spending_type", expenditureTypeName));

                String serviceOpeningScopeName = MapUtils.getString(map, "服务开发范围");
                bdProjectWfInstanceDto.getBdProject().setServiceOpeningScopeCode(dictTable.get("system_service_coverage", serviceOpeningScopeName));

                String projectInitiationTimeString = MapUtils.getString(map, "立项时间");

                if (StringUtils.isNotBlank(projectInitiationTimeString)) {
                    bdProjectWfInstanceDto.getBdProject().setProjectInitiationTime(DateUtils.parseDate(projectInitiationTimeString, "yyyy-MM-dd"));
                }

                String projectLeaderName = MapUtils.getString(map, "项目负责人");

                LoginUser projectLeaderUser = SpringContextUtils.getBean(ISysBaseAPI.class).getUserByRealname(projectLeaderName);

                if (projectLeaderUser != null) {
                    bdProjectWfInstanceDto.getBdProject().setProjectLeaderCode(projectLeaderUser.getUsername());
                }

                String omPrincipalName = MapUtils.getString(map, "运维负责人");

                LoginUser omPrincipalUser = SpringContextUtils.getBean(ISysBaseAPI.class).getUserByRealname(omPrincipalName);

                if (omPrincipalUser != null) {
                    bdProjectWfInstanceDto.getBdProject().setOmPrincipalCode(omPrincipalUser.getUsername());
                    bdProjectWfInstanceDto.getBdProject().setOmDepartmentCode(omPrincipalUser.getOrgCode());
                }

                Result<BdProjectWfInstanceDto> bdProjectWfInstanceDtoResult = this.add(bdProjectWfInstanceDto);

                if (bdProjectWfInstanceDtoResult.isSuccess()) {
                    successLines++;

                    continue;
                }

                errorLines++;

                int lineNumber = i + 1;

                errorMessage.add("第 " + lineNumber + " 行：" + bdProjectWfInstanceDtoResult.getMessage());
            }

            return ImportExcelUtil.imporReturnRes(errorLines, successLines, errorMessage);
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            return Result.error("文件导入失败:" + e.getMessage());
        }
    }

    /**
     * 查询：集合：流程实例
     *
     * @return
     */
    @ApiOperation(value = "查询：集合：流程实例", notes = "项目清单库：查看详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bdProjectId", value = "项目-项目实例标识", dataTypeClass = String.class),
            @ApiImplicitParam(name = "wfCode", value = "流程编号-流程编号", dataTypeClass = String.class)
    })
    @RequestMapping(value = "/listByWfCodeAndBdProjectId", method = RequestMethod.GET)
    public Result<List<WfInstance>> listByWfCodeAndBdProjectId(@RequestParam(name = "bdProjectId") String bdProjectId,@RequestParam(name = "wfCode")String wfCode){
        List<WfInstance> result = Lists.newArrayList();
        if(StringUtils.isEmpty(bdProjectId)){
            return Result.error("项目实例标识不能为空",result);
        }
        if(StringUtils.isEmpty(wfCode)){
            return Result.error("流程编号不能为空",result);
        }
        result = bdProjectWfInstanceService.listByWfCodeAndBdProjectId(bdProjectId,wfCode);
        return Result.OK(result);
    }
}
