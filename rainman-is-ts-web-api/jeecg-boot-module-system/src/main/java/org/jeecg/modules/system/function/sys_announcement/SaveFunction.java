package org.jeecg.modules.system.function.sys_announcement;

import org.apache.commons.collections.MapUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.system.entity.SysAnnouncement;
import org.jeecg.modules.system.service.ISysAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component("sys_announcement.SaveFunction")
public class SaveFunction implements Function<Map<String, Object>, Map<String, Object>> {
    @Autowired
    ISysAnnouncementService sysAnnouncementService;

    @Override
    public Map<String, Object> apply(Map<String, Object> map) {
        SysAnnouncement sysAnnouncement = new SysAnnouncement();

        sysAnnouncement.setTitile(MapUtils.getString(map, "titile"));
        sysAnnouncement.setMsgContent(MapUtils.getString(map, "msgContent"));
        sysAnnouncement.setStartTime((Date) MapUtils.getObject(map, "startTime"));
        sysAnnouncement.setEndTime((Date) MapUtils.getObject(map, "endTime"));
        sysAnnouncement.setSender(MapUtils.getString(map, "sender"));
        sysAnnouncement.setPriority(MapUtils.getString(map, "priority"));
        sysAnnouncement.setMsgCategory(MapUtils.getString(map, "msgCategory"));
        sysAnnouncement.setMsgType(MapUtils.getString(map, "msgType"));
        sysAnnouncement.setSendStatus(MapUtils.getString(map, "sendStatus"));
        sysAnnouncement.setSendTime((Date) MapUtils.getObject(map, "sendTime"));
        sysAnnouncement.setOpenType(MapUtils.getString(map, "openType"));
        sysAnnouncement.setOpenPage(MapUtils.getString(map, "openPage"));
        sysAnnouncement.setUserIds(MapUtils.getString(map, "userIds"));
        sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_0.toString());

        sysAnnouncementService.saveAnnouncement(sysAnnouncement);

        map.put("id", sysAnnouncement.getId());

        return map;
    }
}
