package com.rainman.modules.ists.wf.task;

import com.rainman.config.RainmanWfProperties;
import com.rainman.modules.ists.wf.entity.WfInstance;
import com.rainman.modules.ists.wf.service.IWfInstanceService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author XieZhenhao
 * @version 1.0
 * @date 2021/12/21 16:49
 */
@Component

public class WfInstanceRemoveTimeTask {
    @Autowired
    private IWfInstanceService wfInstanceService;

    @Autowired
    private RainmanWfProperties properties;

    /**
     * 每天4点清除
     */
    @Scheduled(cron = "0 0 4 * * ?")
    public void removeTempWfInstance() {
        wfInstanceService.lambdaQuery()
                .eq(WfInstance::getDataStateCode, "temp")
                .lt(WfInstance::getLastModifiedDate, Timestamp.valueOf(LocalDateTime.now().minusHours(properties.getCleanTime())))
                .list()
                .forEach(item -> {
                    wfInstanceService.removeByWfInstanceId(item.getWfInstanceId());
                });
    }
}
