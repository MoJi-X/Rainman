package com.rainman.modules.ists.cmcc.dto;

import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

import java.util.Date;
import java.util.TimeZone;

/**
 * Title: UniteWorkItem
 * Description: 统一待办工单结构体
 *
 * @author 李京泽
 * @date 2021年12月7日
 */
@XStreamAlias("UniteWorkItem")
public class UniteWorkItem {
    /**
     * @Fields optType 操作类型
     */
    private String optType;

    /**
     * @Fields UniteWorkID 唯一标识符
     */
    @XStreamAlias("UniteWorkID")
    private String uniteWorkId;

    /**
     * @Fields UniteWorkCurUserID 当前处理人用户ID
     */
    @XStreamAlias("UniteWorkCurUserID")
    private String uniteWorkCurUserId;

    /**
     * @Fields UniteWorkAccountid 当前处理人用户账号
     */
    @XStreamAlias("UniteWorkAccountid")
    private String uniteWorkAccountId;

    /**
     * @Fields UniteWorkTitle 标题
     */
    @XStreamAlias("UniteWorkTitle")
    private String uniteWorkTitle;

    /**
     * @Fields UniteWorkCreateTime 创建时间
     */
    @XStreamAlias("UniteWorkCreateTime")
    private Date uniteWorkCreateTime;

    /**
     * @Fields UniteWorkUserDealTime 用户处理时间
     */
    @XStreamAlias("UniteWorkUserDealTime")
    private Date uniteWorkUserDealTime;

    /**
     * @Fields UniteWorkStateUpdateTime 最后状态更新时间
     */
    @XStreamAlias("UniteWorkStateUpdateTime")
    private Date uniteWorkStateUpdateTime;

    /**
     * @Fields UniteWorkCompanyID 单位ID
     */
    @XStreamAlias("UniteWorkCompanyID")
    private String uniteWorkCompanyId;

    /**
     * @Fields UniteWorkURL 详细处理URL
     */
    @XStreamAlias("UniteWorkURL")
    private String uniteWorkUrl;

    /**
     * @Fields UniteWorkType 类型
     */
    @XStreamAlias("UniteWorkType")
    private Integer uniteWorkType;

    /**
     * @Fields UniteWorkState 状态
     */
    @XStreamAlias("UniteWorkState")
    private Integer uniteWorkState;

    /**
     * @Fields UniteWorkAppExampleKey 业务实例关键字
     */
    @XStreamAlias("UniteWorkAppExampleKey")
    private String uniteWorkAppExampleKey;

    /**
     * @Fields UniteWorkAppExampleType 业务实例类型名称
     */
    @XStreamAlias("UniteWorkAppExampleType")
    private String uniteWorkAppExampleType;

    /**
     * @Fields UniteWorkAppID 业务应用ID
     */
    @XStreamAlias("UniteWorkAppID")
    private String uniteWorkAppId;

    /**
     * @Fields UniteWorkSubAppID 子业务应用ID
     */
    @XStreamAlias("UniteWorkSubAppID")
    private String uniteWorkSubAppId;

    /**
     * @Fields UniteWorkHuanJi 缓急程度
     */
    @XStreamAlias("UniteWorkHuanJi")
    private Integer uniteWorkHuanJi;

    /**
     * @Fields UniteWorkCurStepName 当前步骤名称
     */
    @XStreamAlias("UniteWorkCurStepName")
    private String uniteWorkCurStepName;

    /**
     * @Fields UniteWorkPreUserName 上一步骤处理人姓名
     */
    @XStreamAlias("UniteWorkPreUserName")
    private String uniteWorkPreUserName;

    /**
     * @Fields UniteWorkPreStepName 上一步骤名称
     */
    @XStreamAlias("UniteWorkPreStepName")
    private String uniteWorkPreStepName;

    /**
     * @Fields UniteWorkField1 自定义字段1
     */
    @XStreamAlias("UniteWorkField1")
    private String uniteWorkField1;

    /**
     * @Fields UniteWorkField2 自定义字段2
     */
    @XStreamAlias("UniteWorkField2")
    private String uniteWorkField2;

    /**
     * @Fields UniteWorkField3 自定义字段3
     */
    @XStreamAlias("UniteWorkField3")
    private String uniteWorkField3;

    /**
     * @Fields UniteWorkField4 自定义字段4
     */
    @XStreamAlias("UniteWorkField4")
    private String uniteWorkField4;

    /**
     * @Fields UniteWorkField5 自定义字段5
     */
    @XStreamAlias("UniteWorkField5")
    private String uniteWorkField5;

    /**
     * @Fields UniteWorkField6 自定义字段6
     */
    @XStreamAlias("UniteWorkField6")
    private String uniteWorkField6;

    /**
     * @Fields UniteWorkField7 自定义字段7
     */
    @XStreamAlias("UniteWorkField7")
    private String uniteWorkField7;

    /**
     * @Fields UniteWorkField8 自定义字段8
     */
    @XStreamAlias("UniteWorkField8")
    private String uniteWorkField8;

    /**
     * @Fields UniteWorkField9 自定义字段9
     */
    @XStreamAlias("UniteWorkField9")
    private String uniteWorkField9;

    /**
     * @Fields UniteWorkremark 备注
     */
    @XStreamAlias("UniteWorkremark")
    private String uniteWorkremark;

    /**
     * @Fields UniteWorkField10 自定义字段10
     */
    @XStreamAlias("UniteWorkField10")
    private String uniteWorkField10;

    /**
     * @Fields UniteWorkField11 自定义字段11
     */
    @XStreamAlias("UniteWorkField11")
    private String uniteWorkField11;

    /**
     * @Fields UniteWorkField12 自定义字段12
     */
    @XStreamAlias("UniteWorkField12")
    private String uniteWorkField12;

    /**
     * @Fields UniteWorkField13 自定义字段13
     */
    @XStreamAlias("UniteWorkField13")
    private String uniteWorkField13;

    /**
     * @Fields UniteWorkField14 自定义字段14
     */
    @XStreamAlias("UniteWorkField14")
    private String uniteWorkField14;

    /**
     * @Fields UniteWorkField15 自定义字段15
     */
    @XStreamAlias("UniteWorkField15")
    private String uniteWorkField15;

    /**
     * @Fields UniteWorkField16 自定义字段16
     */
    @XStreamAlias("UniteWorkField16")
    private String uniteWorkField16;

    /**
     * @Fields UniteWorkField17 自定义字段17
     */
    @XStreamAlias("UniteWorkField17")
    private String uniteWorkField17;

    /**
     * @Fields UniteWorkField18 自定义字段18
     */
    @XStreamAlias("UniteWorkField18")
    private String uniteWorkField18;

    public UniteWorkItem(String optType, String uniteWorkId, String uniteWorkCurUserId, String uniteWorkAccountId,
                         String uniteWorkTitle, Date uniteWorkCreateTime, Date uniteWorkUserDealTime, Date uniteWorkStateUpdateTime,
                         String uniteWorkCompanyId, String uniteWorkUrl, Integer uniteWorkType, Integer uniteWorkState,
                         String uniteWorkAppExampleKey, String uniteWorkAppExampleType, String uniteWorkAppId,
                         String uniteWorkSubAppId, Integer uniteWorkHuanJi, String uniteWorkCurStepName, String uniteWorkPreUserName,
                         String uniteWorkPreStepName, String uniteWorkField1, String uniteWorkField2, String uniteWorkField3,
                         String uniteWorkField4, String uniteWorkField5, String uniteWorkField6, String uniteWorkField7,
                         String uniteWorkField8, String uniteWorkField9, String uniteWorkremark, String uniteWorkField10,
                         String uniteWorkField11, String uniteWorkField12, String uniteWorkField13, String uniteWorkField14,
                         String uniteWorkField15, String uniteWorkField16, String uniteWorkField17, String uniteWorkField18) {
        super();
        this.optType = optType;
        this.uniteWorkId = uniteWorkId;
        this.uniteWorkCurUserId = uniteWorkCurUserId;
        this.uniteWorkAccountId = uniteWorkAccountId;
        this.uniteWorkTitle = uniteWorkTitle;
        this.uniteWorkCreateTime = uniteWorkCreateTime;
        this.uniteWorkUserDealTime = uniteWorkUserDealTime;
        this.uniteWorkStateUpdateTime = uniteWorkStateUpdateTime;
        this.uniteWorkCompanyId = uniteWorkCompanyId;
        this.uniteWorkUrl = uniteWorkUrl;
        this.uniteWorkType = uniteWorkType;
        this.uniteWorkState = uniteWorkState;
        this.uniteWorkAppExampleKey = uniteWorkAppExampleKey;
        this.uniteWorkAppExampleType = uniteWorkAppExampleType;
        this.uniteWorkAppId = uniteWorkAppId;
        this.uniteWorkSubAppId = uniteWorkSubAppId;
        this.uniteWorkHuanJi = uniteWorkHuanJi;
        this.uniteWorkCurStepName = uniteWorkCurStepName;
        this.uniteWorkPreUserName = uniteWorkPreUserName;
        this.uniteWorkPreStepName = uniteWorkPreStepName;
        this.uniteWorkField1 = uniteWorkField1;
        this.uniteWorkField2 = uniteWorkField2;
        this.uniteWorkField3 = uniteWorkField3;
        this.uniteWorkField4 = uniteWorkField4;
        this.uniteWorkField5 = uniteWorkField5;
        this.uniteWorkField6 = uniteWorkField6;
        this.uniteWorkField7 = uniteWorkField7;
        this.uniteWorkField8 = uniteWorkField8;
        this.uniteWorkField9 = uniteWorkField9;
        this.uniteWorkremark = uniteWorkremark;
        this.uniteWorkField10 = uniteWorkField10;
        this.uniteWorkField11 = uniteWorkField11;
        this.uniteWorkField12 = uniteWorkField12;
        this.uniteWorkField13 = uniteWorkField13;
        this.uniteWorkField14 = uniteWorkField14;
        this.uniteWorkField15 = uniteWorkField15;
        this.uniteWorkField16 = uniteWorkField16;
        this.uniteWorkField17 = uniteWorkField17;
        this.uniteWorkField18 = uniteWorkField18;
    }

    public UniteWorkItem() {
        super();
    }

    public static void main(String[] args) {
        UniteWorkItem item = new UniteWorkItem("&", "11", "22", "33", "http://localhost:8080/&ate=df", new Date(), new Date(), new Date(), "4",
                "66", 1, 2, "77", "88", "99", "qq", 3, "ww", "ee", "rr", "ss", "zz", "fff", "ccc", "ddd", "sss", "gg",
                "ee", "vbv", "fgf", "gffd", "fgf", "dfg", "ggh", "rtr", "", "", "", null);
        XStream xstream = new XStream(new XppDriver(new NoNameCoder()));
        xstream.registerConverter(new DateConverter("yyyy-MM-dd HH:mm:ss", null, TimeZone.getTimeZone("GMT+8")));
        xstream.autodetectAnnotations(true);
        String word = xstream.toXML(item);
        System.out.println(word);
        xstream.alias("UniteWorkItem", UniteWorkItem.class);
        UniteWork wUniteWork = (UniteWork) xstream.fromXML(word, UniteWorkItem.class);
        System.out.println(JSON.toJSONString(wUniteWork));
    }

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    public String getUniteWorkId() {
        return uniteWorkId;
    }

    public void setUniteWorkId(String uniteWorkId) {
        this.uniteWorkId = uniteWorkId;
    }

    public String getUniteWorkCurUserId() {
        return uniteWorkCurUserId;
    }

    public void setUniteWorkCurUserId(String uniteWorkCurUserId) {
        this.uniteWorkCurUserId = uniteWorkCurUserId;
    }

    public String getUniteWorkAccountId() {
        return uniteWorkAccountId;
    }

    public void setUniteWorkAccountId(String uniteWorkAccountId) {
        this.uniteWorkAccountId = uniteWorkAccountId;
    }

    public String getUniteWorkTitle() {
        return uniteWorkTitle;
    }

    public void setUniteWorkTitle(String uniteWorkTitle) {
        this.uniteWorkTitle = uniteWorkTitle;
    }

    public Date getUniteWorkCreateTime() {
        return uniteWorkCreateTime;
    }

    public void setUniteWorkCreateTime(Date uniteWorkCreateTime) {
        this.uniteWorkCreateTime = uniteWorkCreateTime;
    }

    public Date getUniteWorkUserDealTime() {
        return uniteWorkUserDealTime;
    }

    public void setUniteWorkUserDealTime(Date uniteWorkUserDealTime) {
        this.uniteWorkUserDealTime = uniteWorkUserDealTime;
    }

    public Date getUniteWorkStateUpdateTime() {
        return uniteWorkStateUpdateTime;
    }

    public void setUniteWorkStateUpdateTime(Date uniteWorkStateUpdateTime) {
        this.uniteWorkStateUpdateTime = uniteWorkStateUpdateTime;
    }

    public String getUniteWorkCompanyId() {
        return uniteWorkCompanyId;
    }

    public void setUniteWorkCompanyId(String uniteWorkCompanyId) {
        this.uniteWorkCompanyId = uniteWorkCompanyId;
    }

    public String getUniteWorkUrl() {
        return uniteWorkUrl;
    }

    public void setUniteWorkUrl(String uniteWorkUrl) {
        this.uniteWorkUrl = uniteWorkUrl;
    }

    public Integer getUniteWorkType() {
        return uniteWorkType;
    }

    public void setUniteWorkType(Integer uniteWorkType) {
        this.uniteWorkType = uniteWorkType;
    }

    public Integer getUniteWorkState() {
        return uniteWorkState;
    }

    public void setUniteWorkState(Integer uniteWorkState) {
        this.uniteWorkState = uniteWorkState;
    }

    public String getUniteWorkAppExampleKey() {
        return uniteWorkAppExampleKey;
    }

    public void setUniteWorkAppExampleKey(String uniteWorkAppExampleKey) {
        this.uniteWorkAppExampleKey = uniteWorkAppExampleKey;
    }

    public String getUniteWorkAppExampleType() {
        return uniteWorkAppExampleType;
    }

    public void setUniteWorkAppExampleType(String uniteWorkAppExampleType) {
        this.uniteWorkAppExampleType = uniteWorkAppExampleType;
    }

    public String getUniteWorkAppId() {
        return uniteWorkAppId;
    }

    public void setUniteWorkAppId(String uniteWorkAppId) {
        this.uniteWorkAppId = uniteWorkAppId;
    }

    public String getUniteWorkSubAppId() {
        return uniteWorkSubAppId;
    }

    public void setUniteWorkSubAppId(String uniteWorkSubAppId) {
        this.uniteWorkSubAppId = uniteWorkSubAppId;
    }

    public Integer getUniteWorkHuanJi() {
        return uniteWorkHuanJi;
    }

    public void setUniteWorkHuanJi(Integer uniteWorkHuanJi) {
        this.uniteWorkHuanJi = uniteWorkHuanJi;
    }

    public String getUniteWorkCurStepName() {
        return uniteWorkCurStepName;
    }

    public void setUniteWorkCurStepName(String uniteWorkCurStepName) {
        this.uniteWorkCurStepName = uniteWorkCurStepName;
    }

    public String getUniteWorkPreUserName() {
        return uniteWorkPreUserName;
    }

    public void setUniteWorkPreUserName(String uniteWorkPreUserName) {
        this.uniteWorkPreUserName = uniteWorkPreUserName;
    }

    public String getUniteWorkPreStepName() {
        return uniteWorkPreStepName;
    }

    public void setUniteWorkPreStepName(String uniteWorkPreStepName) {
        this.uniteWorkPreStepName = uniteWorkPreStepName;
    }

    public String getUniteWorkField1() {
        return uniteWorkField1;
    }

    public void setUniteWorkField1(String uniteWorkField1) {
        this.uniteWorkField1 = uniteWorkField1;
    }

    public String getUniteWorkField2() {
        return uniteWorkField2;
    }

    public void setUniteWorkField2(String uniteWorkField2) {
        this.uniteWorkField2 = uniteWorkField2;
    }

    public String getUniteWorkField3() {
        return uniteWorkField3;
    }

    public void setUniteWorkField3(String uniteWorkField3) {
        this.uniteWorkField3 = uniteWorkField3;
    }

    public String getUniteWorkField4() {
        return uniteWorkField4;
    }

    public void setUniteWorkField4(String uniteWorkField4) {
        this.uniteWorkField4 = uniteWorkField4;
    }

    public String getUniteWorkField5() {
        return uniteWorkField5;
    }

    public void setUniteWorkField5(String uniteWorkField5) {
        this.uniteWorkField5 = uniteWorkField5;
    }

    public String getUniteWorkField6() {
        return uniteWorkField6;
    }

    public void setUniteWorkField6(String uniteWorkField6) {
        this.uniteWorkField6 = uniteWorkField6;
    }

    public String getUniteWorkField7() {
        return uniteWorkField7;
    }

    public void setUniteWorkField7(String uniteWorkField7) {
        this.uniteWorkField7 = uniteWorkField7;
    }

    public String getUniteWorkField8() {
        return uniteWorkField8;
    }

    public void setUniteWorkField8(String uniteWorkField8) {
        this.uniteWorkField8 = uniteWorkField8;
    }

    public String getUniteWorkField9() {
        return uniteWorkField9;
    }

    public void setUniteWorkField9(String uniteWorkField9) {
        this.uniteWorkField9 = uniteWorkField9;
    }

    public String getUniteWorkremark() {
        return uniteWorkremark;
    }

    public void setUniteWorkremark(String uniteWorkremark) {
        this.uniteWorkremark = uniteWorkremark;
    }

    public String getUniteWorkField10() {
        return uniteWorkField10;
    }

    public void setUniteWorkField10(String uniteWorkField10) {
        this.uniteWorkField10 = uniteWorkField10;
    }

    public String getUniteWorkField11() {
        return uniteWorkField11;
    }

    public void setUniteWorkField11(String uniteWorkField11) {
        this.uniteWorkField11 = uniteWorkField11;
    }

    public String getUniteWorkField12() {
        return uniteWorkField12;
    }

    public void setUniteWorkField12(String uniteWorkField12) {
        this.uniteWorkField12 = uniteWorkField12;
    }

    public String getUniteWorkField13() {
        return uniteWorkField13;
    }

    public void setUniteWorkField13(String uniteWorkField13) {
        this.uniteWorkField13 = uniteWorkField13;
    }

    public String getUniteWorkField14() {
        return uniteWorkField14;
    }

    public void setUniteWorkField14(String uniteWorkField14) {
        this.uniteWorkField14 = uniteWorkField14;
    }

    public String getUniteWorkField15() {
        return uniteWorkField15;
    }

    public void setUniteWorkField15(String uniteWorkField15) {
        this.uniteWorkField15 = uniteWorkField15;
    }

    public String getUniteWorkField16() {
        return uniteWorkField16;
    }

    public void setUniteWorkField16(String uniteWorkField16) {
        this.uniteWorkField16 = uniteWorkField16;
    }

    public String getUniteWorkField17() {
        return uniteWorkField17;
    }

    public void setUniteWorkField17(String uniteWorkField17) {
        this.uniteWorkField17 = uniteWorkField17;
    }

    public String getUniteWorkField18() {
        return uniteWorkField18;
    }

    public void setUniteWorkField18(String uniteWorkField18) {
        this.uniteWorkField18 = uniteWorkField18;
    }
}
