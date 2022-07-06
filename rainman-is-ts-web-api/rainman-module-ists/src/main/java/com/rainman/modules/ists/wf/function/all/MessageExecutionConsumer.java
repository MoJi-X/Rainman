package com.rainman.modules.ists.wf.function.all;

import com.google.common.collect.Maps;
import com.rainman.config.RainmanWfProperties;
import com.rainman.modules.ists.wf.entity.WfInstance;
import com.rainman.modules.ists.wf.service.IWfInstanceService;
import org.activiti.engine.delegate.DelegateExecution;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 流程结束发系统消息:信安人员
 */
@Component("messageExecutionConsumer")
public class MessageExecutionConsumer implements Consumer<DelegateExecution> {
    @Autowired
    private RainmanWfProperties rainmanWfProperties;

    @Override
    public void accept(DelegateExecution delegateExecution) {
        Function saveFunction = SpringContextUtils.getBean("sys_announcement.SaveFunction", Function.class);

        if (saveFunction == null) {
            return;
        }

        Function listUserIdByRoleCodeFunction = SpringContextUtils.getBean("sys_user_role.ListUserIdByRoleCodeFunction", Function.class);

        if (listUserIdByRoleCodeFunction == null) {
            return;
        }

        WfInstance wfInstance = SpringContextUtils.getBean(IWfInstanceService.class).getById(delegateExecution.getProcessInstanceBusinessKey());

        if (wfInstance == null) {
            return;
        }

        if ("start".equals(delegateExecution.getEventName())) {
//            Map<String, Object> map = Maps.newHashMap();
//
//            map.put("titile", wfInstance.getName());
//            map.put("msgContent", "流程发起： " + wfInstance.getName());
//            map.put("startTime", new Date());
//            map.put("endTime", Date.from(LocalDateTime.now().plusMonths(1).atZone(ZoneId.systemDefault()).toInstant()));
//
//            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
//
//            if (sysUser != null) {
//                map.put("sender", sysUser.getUsername());
//            }
//
//            map.put("priority", "L");
//            map.put("msgCategory", "1");
//            map.put("msgType", "USER");
//            map.put("sendStatus", "1");
//            map.put("sendTime", new Date());
//            map.put("openType", "url");
//            map.put("openPage", wfInstance.getWfCode() + ",view," + wfInstance.getWfInstanceId());
//            List<String> userIds = (List<String>) listUserIdByRoleCodeFunction.apply(rainmanWfProperties.getXaRoleCode());
//            map.put("userIds", StringUtils.join(userIds, ","));
//
//            saveFunction.apply(map);
        } else if ("end".equals(delegateExecution.getEventName())) {
//            Map<String, Object> map = Maps.newHashMap();
//
//            map.put("titile", wfInstance.getName());
//            map.put("msgContent", "流程结束： " + wfInstance.getName());
//            map.put("startTime", new Date());
//            map.put("endTime", Date.from(LocalDateTime.now().plusMonths(1).atZone(ZoneId.systemDefault()).toInstant()));
//
//            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
//
//            if (sysUser != null) {
//                map.put("sender", sysUser.getUsername());
//            }
//
//            map.put("priority", "L");
//            map.put("msgCategory", "1");
//            map.put("msgType", "USER");
//            map.put("sendStatus", "1");
//            map.put("sendTime", new Date());
//            map.put("openType", "url");
//            map.put("openPage", wfInstance.getWfCode() + ",view," + wfInstance.getWfInstanceId());
//            List<String> userIds = (List<String>) listUserIdByRoleCodeFunction.apply(rainmanWfProperties.getXaRoleCode());
//            map.put("userIds", StringUtils.join(userIds, ","));
//
//            saveFunction.apply(map);
        }
    }
}