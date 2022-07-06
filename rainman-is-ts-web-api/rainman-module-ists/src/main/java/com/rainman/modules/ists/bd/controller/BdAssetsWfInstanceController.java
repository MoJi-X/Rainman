package com.rainman.modules.ists.bd.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.collect.Lists;
import com.rainman.modules.ists.bd.dto.BdAssetsWfInstanceDto;
import com.rainman.modules.ists.bd.entity.BdAssets;
import com.rainman.modules.ists.bd.entity.BdAssetsWfInstance;
import com.rainman.modules.ists.bd.entity.BdProject;
import com.rainman.modules.ists.bd.service.IBdAssetsWfInstanceService;
import com.rainman.modules.ists.bd.service.IBdProjectService;
import com.rainman.modules.ists.wf.entity.WfInstance;
import com.rainman.modules.ists.wf.service.IWfInstanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.ImportExcelUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @Description: 基础数据-资产与流程实例
 * @Author: 谢桢昊
 * @Date: 2021-12-2
 * @Version: V1.0
 */
@Api(tags = "基础数据-资产与流程实例")
@RestController
@RequestMapping("/ists.bd/bdAssetsWfInstance")
@Slf4j
public class BdAssetsWfInstanceController extends JeecgController<BdAssetsWfInstance, IBdAssetsWfInstanceService> {

    @Autowired
    private IWfInstanceService wfInstanceService;

    @Autowired
    private IBdAssetsWfInstanceService bdAssetsWfInstanceService;

    @Autowired
    private IBdProjectService bdProjectService;

    /**
     * 分页列表查询
     *
     * @param bdAssetsWfInstanceDto
     * @param pageNo
     * @param pageSize
     * @return
     */
    @AutoLog(value = "查询:分页:资产信息")
    @ApiOperation(value = "查询:分页:资产信息", notes = "查询:分页:资产信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "页号", example = "1", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "pageSize", value = "页大小", example = "5", dataTypeClass = Long.class)
    })
    @PostMapping(value = "/pageList")
    public Result<IPage<BdAssets>> queryPageList(@RequestBody BdAssetsWfInstanceDto bdAssetsWfInstanceDto,
                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize) {

        IPage<BdAssets> newPage = bdAssetsWfInstanceService.pageByDto(bdAssetsWfInstanceDto, pageNo, pageSize);

        return Result.OK(newPage);
    }

    /**
     * 添加
     *
     * @param bdAssetsWfInstanceDto
     * @return
     */
    @AutoLog(value = "添加：单个")
    @ApiOperation(value = "添加：单个", notes = "添加：单个")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BdAssetsWfInstanceDto bdAssetsWfInstanceDto) {
        if (bdAssetsWfInstanceDto.getBdAssets() == null) {
            return Result.error("资产信息缺失");
        }
        if (bdAssetsWfInstanceDto.getBdAssets().getEquipmentTypeName() == null) {
            return Result.error("请输入设备类型");
        }
        if (bdAssetsWfInstanceDto.getBdAssets().getEquipmentIp() == null) {
            return Result.error("请输入设备IP");
        }
        if (bdAssetsWfInstanceDto.getBdAssets().getEquipmentPort() == null) {
            return Result.error("请输入设备端口");
        }
        if (bdAssetsWfInstanceDto.getBdAssets().getDeployComputerRoom() == null) {
            return Result.error("请输入部署机房");
        }
        // 数据库字段长度为255
        if (bdAssetsWfInstanceDto.getBdAssets().getDeployComputerRoom().length() > 178) {
            return Result.error("部署机房输入异常");
        }

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        if (sysUser == null) {
            return Result.error("未登录，无法操作");
        }
        bdAssetsWfInstanceDto.getBdAssets().setOmPrincipalCode(sysUser.getUsername());
        bdAssetsWfInstanceDto.getBdAssets().setOmPrincipalName(sysUser.getRealname());

        WfInstance wfInstance = wfInstanceService.getById(bdAssetsWfInstanceDto.getWfInstanceId());

        if (wfInstance == null) {
            return Result.error("查询不到：流程实例", bdAssetsWfInstanceDto);
        }

        BdProject bdProject = bdProjectService.getById(bdAssetsWfInstanceDto.getBdProjectId());

        if (bdProject == null) {
            return Result.error("查询不到：项目信息", bdAssetsWfInstanceDto);
        }

        BdAssetsWfInstance bdAssetsWfInstance = new BdAssetsWfInstance();
        bdAssetsWfInstance.setBdAssetsId(bdAssetsWfInstanceDto.getBdAssets().getBdAssetsId());
        bdAssetsWfInstance.setWfInstanceId(bdAssetsWfInstanceDto.getWfInstanceId());
        bdAssetsWfInstance.setBdProjectId(bdAssetsWfInstanceDto.getBdProjectId());

        Long id = IdWorker.getId(bdAssetsWfInstanceDto);

        bdAssetsWfInstance.setBdAssetsId(id.toString());

        bdAssetsWfInstanceDto.getBdAssets().setBdAssetsId(id.toString());
        try {

            bdAssetsWfInstanceService.saveInfo(bdAssetsWfInstance, bdAssetsWfInstanceDto);

        } catch (Exception ex) {

            return Result.error(StringUtils.defaultString(ex.getMessage(), ex.getCause().getMessage()));

        }

        return Result.OK("添加成功！");
    }

    /**
     * 通过id删除
     *
     * @param bdAssetsWfInstanceId
     * @return
     */
    @AutoLog(value = "删除:单个")
    @ApiOperation(value = "删除:单个", notes = "删除:单个")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "bdAssetsWfInstanceId", value = "基础数据-资产实例标识", dataTypeClass = String.class)
    )
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "bdAssetsWfInstanceId", required = true) String bdAssetsWfInstanceId) {

        BdAssetsWfInstance bdAssetsWfInstance = bdAssetsWfInstanceService.getById(bdAssetsWfInstanceId);

        try {

            bdAssetsWfInstanceService.deleteById(bdAssetsWfInstance.getBdAssetsId(), bdAssetsWfInstanceId);

        } catch (Exception ex) {

            return Result.error(StringUtils.defaultString(ex.getMessage(), ex.getCause().getMessage()));

        }

        return Result.OK("删除成功!");
    }

    /**
     * 导入：批量：资产信息
     *
     * @return
     */
    @ApiOperation(value = "导入：批量：资产信息", notes = "同步建设-建设入网-资产录入-批量导入")
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

        //获取登录信息
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userRealName = loginUser.getRealname();
        String userName = loginUser.getUsername();

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

            //入库
            for (int i = 0; i < mapList.size(); i++) {
                Map<String, Object> map = mapList.get(i);

                BdAssets bdAssets = new BdAssets();

                bdAssets.setEquipmentTypeName(MapUtils.getString(map, "设备类型"));
                bdAssets.setEquipmentIp(MapUtils.getString(map, "设备IP"));
                bdAssets.setEquipmentPort(String.valueOf(MapUtils.getInteger(map, "设备端口")));
                bdAssets.setDeployComputerRoom(MapUtils.getString(map, "部署机房"));
                bdAssets.setOmPrincipalName(userRealName);
                bdAssets.setOmPrincipalCode(userName);

                BdAssetsWfInstanceDto bdAssetsWfInstanceDto = new BdAssetsWfInstanceDto();

                //设置流程与项目标识
                bdAssetsWfInstanceDto.setWfInstanceId(wfInstanceId);
                bdAssetsWfInstanceDto.setBdProjectId(bdProjectId);

                //设置资产信息
                bdAssetsWfInstanceDto.setBdAssets(bdAssets);

                //入库
                Result<?> ans = this.add(bdAssetsWfInstanceDto);

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
