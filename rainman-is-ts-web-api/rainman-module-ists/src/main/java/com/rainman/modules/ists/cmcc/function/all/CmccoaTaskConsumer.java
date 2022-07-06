package com.rainman.modules.ists.cmcc.function.all;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.rainman.modules.ists.cmcc.config.RainmanCmccProperties;
import com.rainman.modules.ists.cmcc.dto.UniteWork;
import com.rainman.modules.ists.cmcc.dto.UniteWorkItem;
import com.rainman.modules.ists.cmcc.util.UniteWorkServiceClient;
import com.rainman.modules.ists.wf.entity.WfInstance;
import com.rainman.modules.ists.wf.service.IWfInstanceService;
import org.activiti.engine.delegate.DelegateTask;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 流程节点创建发移动OA：待办
 * 流程节点签收处理发移动OA：已办
 */
@Component("cmccoaTaskConsumer")
public class CmccoaTaskConsumer implements Consumer<DelegateTask> {
    @Autowired
    RainmanCmccProperties rainmanCmccProperties;

    private static final String CREATE = "create";

    private static final String ASSIGNMENT = "assignment";

    @Override
    public void accept(DelegateTask delegateTask) {
        if (!(CREATE.equals(delegateTask.getEventName()) || ASSIGNMENT.equals(delegateTask.getEventName()))) {
            return;
        }

        Function saveFunction = SpringContextUtils.getBean("sys_announcement.SaveFunction", Function.class);

        if (saveFunction == null) {
            return;
        }

        Function listUserIdByRoleCodeFunction = SpringContextUtils.getBean("sys_user_role.ListUserIdByRoleCodeFunction", Function.class);

        if (listUserIdByRoleCodeFunction == null) {
            return;
        }

        Function getUsername4aByUserIdFunction = SpringContextUtils.getBean("sys_user.GetUsername4aByUserIdFunction", Function.class);

        if (getUsername4aByUserIdFunction == null) {
            return;
        }

        Function getUsername4aByUsernameFunction = SpringContextUtils.getBean("sys_user.GetUsername4aByUsernameFunction", Function.class);

        if (getUsername4aByUsernameFunction == null) {
            return;
        }

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        if (sysUser == null) {
            return;
        }

        WfInstance wfInstance = SpringContextUtils.getBean(IWfInstanceService.class).getById(delegateTask.getExecution().getProcessInstanceBusinessKey());

        if (wfInstance == null) {
            return;
        }

        Set<String> allUsername4aSet = Sets.newHashSet();

        Set<String> defaultUsername4aSet = Sets.newHashSet();

        if (CREATE.equals(delegateTask.getEventName())) {
            List<String> lastCandidateUsers = (List<String>) delegateTask.getVariable("last_candidate_users");

            for (String lastCandidateUser : lastCandidateUsers) {
                String username4a = (String) getUsername4aByUsernameFunction.apply(lastCandidateUser);

                if (StringUtils.isNotBlank(username4a)) {
                    defaultUsername4aSet.add(username4a);
                }
            }
        } else {
            String username4a = (String) getUsername4aByUsernameFunction.apply(sysUser.getUsername());

            if (StringUtils.isNotBlank(username4a)) {
                defaultUsername4aSet.add(username4a);
            }
        }

        List<String> xaRoleUserIds = Lists.newArrayList();

        if (ASSIGNMENT.equals(delegateTask.getEventName())
                && !StringUtils.contains(delegateTask.getName(), "信安领导审批")
                && StringUtils.contains(delegateTask.getName(), "领导审批")) {
            List<String> tempXaRoleUserIds = (List<String>) listUserIdByRoleCodeFunction.apply(rainmanCmccProperties.getXaRoleCode());

            Set<String> xaRoleUsername4aSet = Sets.newHashSet();

            if (!CollectionUtils.isEmpty(tempXaRoleUserIds)) {
                xaRoleUserIds.addAll(tempXaRoleUserIds);

                for (String xaRoleUserId : xaRoleUserIds) {
                    String username4a = (String) getUsername4aByUserIdFunction.apply(xaRoleUserId);

                    if (StringUtils.isBlank(username4a)) {
                        continue;
                    }

                    xaRoleUsername4aSet.add(username4a);
                }
            }

            if (!xaRoleUsername4aSet.isEmpty()) {
                // 添加信安人员:
                allUsername4aSet.addAll(xaRoleUsername4aSet);
            }
        }

        if (!defaultUsername4aSet.isEmpty()) {
            // 添加默认人员:已办、待办
            allUsername4aSet.addAll(defaultUsername4aSet);
        }

        if (allUsername4aSet.isEmpty()) {
            return;
        }

        String wfName = SpringContextUtils.getBean(ISysBaseAPI.class).translateDict("process_number", wfInstance.getWfCode());

        String wfNodeCode = StringUtils.substring(delegateTask.getTaskDefinitionKey(), "usertask_".length());

        String wfNodeName = SpringContextUtils.getBean(ISysBaseAPI.class).translateDict(wfInstance.getWfCode() + "_node", wfNodeCode);

        List<UniteWorkItem> uniteWorkItems = Lists.newArrayList();

        for (String allUsername4a : allUsername4aSet) {
            //0:办件 1:阅件
            Integer uniteWorkType = 1;

            //0:待办 2:已办 4:待阅 5:已阅 7:作废
            Integer uniteWorkState = 4;

            String uniteWorkUrl = new StringBuffer(rainmanCmccProperties.getJumpBackUrl())
                    .append("?wfInstanceId=")
                    .append(delegateTask.getExecution().getProcessInstanceBusinessKey())
                    .append("&isView=true")
                    .toString();

            if (defaultUsername4aSet.contains(allUsername4a)) {
                if (CREATE.equals(delegateTask.getEventName())) {
                    uniteWorkUrl = new StringBuffer(rainmanCmccProperties.getJumpBackUrl())
                            .append("?wfInstanceId=")
                            .append(delegateTask.getExecution().getProcessInstanceBusinessKey())
                            .append("&isView=false")
                            .toString();

                    uniteWorkType = 0;

                    uniteWorkState = 0;
                } else {
                    uniteWorkState = 2;
                }
            }

            UniteWorkItem uniteWorkItem = new UniteWorkItem();

            uniteWorkItems.add(uniteWorkItem);

            uniteWorkItem.setOptType(CREATE.equals(delegateTask.getEventName()) ? "1" : "3");
            uniteWorkItem.setUniteWorkId(delegateTask.getExecution().getProcessInstanceBusinessKey() + ":" + delegateTask.getId() + ":" + allUsername4a);
            uniteWorkItem.setUniteWorkCurUserId(allUsername4a);
            uniteWorkItem.setUniteWorkAccountId(allUsername4a);
            uniteWorkItem.setUniteWorkTitle(wfInstance.getName());
            uniteWorkItem.setUniteWorkCreateTime(wfInstance.getInitiationDate());
            uniteWorkItem.setUniteWorkUserDealTime(wfInstance.getLastModifiedDate());
            uniteWorkItem.setUniteWorkStateUpdateTime(wfInstance.getLastModifiedDate());
            uniteWorkItem.setUniteWorkCompanyId("00360078001500000000");

            uniteWorkItem.setUniteWorkUrl(uniteWorkUrl);
            uniteWorkItem.setUniteWorkType(uniteWorkType);
            uniteWorkItem.setUniteWorkState(uniteWorkState);
            uniteWorkItem.setUniteWorkAppExampleKey(delegateTask.getExecution().getProcessInstanceBusinessKey());
            uniteWorkItem.setUniteWorkAppExampleType(StringUtils.defaultString(wfName, wfInstance.getWfCode()));
            uniteWorkItem.setUniteWorkAppId(rainmanCmccProperties.getAppCode());
            //0:空 1:一般 2:紧急 3:特急
            uniteWorkItem.setUniteWorkHuanJi(1);
            uniteWorkItem.setUniteWorkCurStepName(wfNodeName);
            uniteWorkItem.setUniteWorkPreUserName(sysUser.getUsername());
            uniteWorkItem.setUniteWorkField6(delegateTask.getExecution().getProcessInstanceBusinessKey());
        }

        UniteWork uniteWork = new UniteWork();

        uniteWork.setUniteWorkItems(uniteWorkItems);

        SpringContextUtils.getBean("slowTaskExecutor", AsyncListenableTaskExecutor.class).submitListenable(() -> {
            UniteWorkServiceClient.requestWebService(uniteWork);
        });

        if (!xaRoleUserIds.isEmpty()) {
            SpringContextUtils.getBean("slowTaskExecutor", AsyncListenableTaskExecutor.class).submitListenable(() -> {
                Map<String, Object> map = Maps.newHashMap();

                map.put("titile", wfInstance.getName());
                map.put("msgContent", "流程结束： " + wfInstance.getName());
                map.put("startTime", new Date());
                map.put("endTime", Date.from(LocalDateTime.now().plusMonths(1).atZone(ZoneId.systemDefault()).toInstant()));
                map.put("sender", sysUser.getUsername());
                map.put("priority", "L");
                map.put("msgCategory", "1");
                map.put("msgType", "USER");
                map.put("sendStatus", "1");
                map.put("sendTime", new Date());
                map.put("openType", "url");
                map.put("openPage", wfInstance.getWfCode() + ",view," + wfInstance.getWfInstanceId());
                map.put("userIds", StringUtils.join(xaRoleUserIds, ","));

                saveFunction.apply(map);
            });
        }
    }
}