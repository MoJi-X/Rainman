package com.rainman.modules.ists.wf.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.rainman.modules.ists.bd.entity.BdAssetsWfInstance;
import com.rainman.modules.ists.bd.entity.BdFlowSheet;
import com.rainman.modules.ists.bd.entity.BdProjectWfInstance;
import com.rainman.modules.ists.bd.entity.BdStrPointsWfInstance;
import com.rainman.modules.ists.bd.service.*;
import com.rainman.modules.ists.wf.dto.WfInstanceDto;
import com.rainman.modules.ists.wf.dto.WfSubmitDto;
import com.rainman.modules.ists.wf.entity.WfInstance;
import com.rainman.modules.ists.wf.mapper.WfInstanceMapper;
import com.rainman.modules.ists.wf.service.IWfInstanceService;
import lombok.SneakyThrows;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @Description: wf_instance
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
@Service
public class WfInstanceServiceImpl extends ServiceImpl<WfInstanceMapper, WfInstance> implements IWfInstanceService {
    @Override
    public WfInstance launch(WfInstance wfInstance) {
        wfInstance.setDataStateCode("temp");

        BdFlowSheet bdFlowSheet = new BdFlowSheet();

        bdFlowSheet.setBdFlowSheetTypeCode("wf");

        wfInstance.setBdFlowSheetCode(SpringContextUtils.getBean(IBdFlowSheetService.class).build(bdFlowSheet));

        this.save(wfInstance);

        try {
            Map<String, Object> variables = Maps.newHashMap();

            variables.put("last_candidate_users", Lists.newArrayList(StringUtils.split(wfInstance.getInitiatorCode(), ",")));

            SpringContextUtils.getBean(IdentityService.class).setAuthenticatedUserId(wfInstance.getInitiatorCode());

            SpringContextUtils.getBean(RuntimeService.class).startProcessInstanceByKey(
                    wfInstance.getWfCode(),
                    wfInstance.getWfInstanceId(),
                    variables);
        } finally {
            SpringContextUtils.getBean(IdentityService.class).setAuthenticatedUserId(null);
        }

        return wfInstance;
    }

    @SneakyThrows
    @Override
    public void submit(WfSubmitDto wfSubmitDto, Map<String, Object> variables) {
        Task task = (Task) variables.get("task");

        String wfCode = task.getProcessDefinitionId().split(":")[0];

        String wfNodeCode = StringUtils.substring(task.getTaskDefinitionKey(), "usertask_".length());

        String beanName = new StringBuffer(wfCode)
                .append("_user_task_")
                .append(wfNodeCode)
                .append("_consumer")
                .toString();

        BiConsumer<WfSubmitDto, Map<String, Object>> biConsumer = SpringContextUtils.getBean(beanName, BiConsumer.class);

        if (biConsumer == null) {
            throw new Exception("????????????????????????????????????????????????????????????");
        }

        biConsumer.accept(wfSubmitDto, variables);
    }

    @Override
    public IPage<WfInstanceDto> pageByUsername(Long pageNo, Long pageSize, String username, String isHistory) {
        Page<WfInstanceDto> page = new Page<>(pageNo, pageSize);

        return this.baseMapper.pageByUsername(page, username, isHistory);
    }

    @Override
    public Integer countByWfCodeAndBdFileFlagCode(String wfCode, String bdFileFlagCode) {
        return this.baseMapper.countByWfCodeAndBdFileFlagCode(wfCode, bdFileFlagCode);
    }

    @Override
    public Integer countByInitiationDate(Date beginInitiationDate, Date endInitiationDate) {
        return this.baseMapper.countByInitiationDate(beginInitiationDate, endInitiationDate);
    }

    @Override
    public Map<String, Object> countForBdProject() {
        return this.baseMapper.countForBdProject();
    }

    @Override
    public Integer countByWfCodeNqIneedCheckForBdProject() {
        return this.baseMapper.countByWfCodeNqIneedCheckForBdProject();
    }

    @Override
    public Integer countByWfCodeNqAcceptForBdProject() {
        return this.baseMapper.countByWfCodeNqAcceptForBdProject();
    }

    @Override
    public Integer countByWfCodeEqIneedCheckForBdProject() {
        return this.baseMapper.countByWfCodeEqIneedCheckForBdProject();
    }

    @Override
    public Integer countByWfCodeInAllForBdProject() {
        return this.baseMapper.countByWfCodeInAllForBdProject();
    }

    @Override
    public List<Map<String, Object>> listCountByOrgForWf() {
        return this.baseMapper.listCountByOrgForWf();
    }

    @SneakyThrows
    @Override
    public boolean removeByWfInstanceId(Serializable wfInstanceId) {
        WfInstance wfInstance = this.getById(wfInstanceId);

        if (wfInstance == null) {
            throw new Exception("??????????????????????????????");
        }

        if (!"not_started".equals(wfInstance.getStateCode())) {
            throw new Exception("????????????\"?????????\"???????????????");
        }

        // ?????????activity
        try {
            SpringContextUtils.getBean(RuntimeService.class).deleteProcessInstance(wfInstance.getProcessId(), wfInstance.getWfInstanceId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        // ???????????????????????????????????????????????????????????????????????????
        SpringContextUtils.getBean(IBdProjectService.class).removeByWfInstanceId(wfInstance.getWfInstanceId());

        // ???????????????????????????????????????
        SpringContextUtils.getBean(IBdProjectWfInstanceService.class).lambdaUpdate().eq(BdProjectWfInstance::getWfInstanceId, wfInstanceId).remove();

        // ??????????????????????????????????????????????????????????????????????????????????????????
        SpringContextUtils.getBean(IBdStrPointsService.class).removeByWfInstanceId(wfInstance.getWfInstanceId());

        // ??????????????????????????????????????????????????????
        SpringContextUtils.getBean(IBdStrPointsWfInstanceService.class).lambdaUpdate().eq(BdStrPointsWfInstance::getWfInstanceId, wfInstanceId).remove();

        // ????????????????????????????????????????????????????????????
        SpringContextUtils.getBean(IBdAssetsService.class).removeByWfInstanceId(wfInstance.getWfInstanceId());

        // ???????????????????????????????????????
        SpringContextUtils.getBean(IBdAssetsWfInstanceService.class).lambdaUpdate().eq(BdAssetsWfInstance::getWfInstanceId, wfInstanceId).remove();

        // ????????????????????????????????????????????????????????????????????????
        SpringContextUtils.getBean(IBdFileService.class).removeByWfInstanceId(wfInstance.getWfInstanceId());

        return this.removeById(wfInstanceId);
    }

    @Override
    public boolean temporaryStorageByWfInstanceId(Serializable wfInstanceId) {
        return this.lambdaUpdate()
                .set(WfInstance::getDataStateCode, "use")
                .eq(WfInstance::getWfInstanceId, wfInstanceId)
                .update();
    }
}