package com.rainman.modules.ists.wf.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rainman.modules.ists.wf.dto.WfInstanceDto;
import com.rainman.modules.ists.wf.dto.WfSubmitDto;
import com.rainman.modules.ists.wf.entity.WfInstance;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: wf_instance
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
public interface IWfInstanceService extends IService<WfInstance> {
    /**
     * 发起
     *
     * @param wfInstance
     * @return
     */
    @Transactional
    WfInstance launch(WfInstance wfInstance);

    /**
     * 提交
     *
     * @param wfSubmitDto
     */
    @Transactional
    void submit(WfSubmitDto wfSubmitDto, Map<String, Object> variables);

    /**
     * 查询分页
     *
     * @param username
     * @return
     */
    IPage<WfInstanceDto> pageByUsername(Long pageNo, Long pageSize, String username, String isHistory);

    /**
     * 查询：总数：流程编号
     *
     * @param wfCode         流程编号
     * @param bdFileFlagCode 流程编号
     * @return
     */
    Integer countByWfCodeAndBdFileFlagCode(String wfCode, String bdFileFlagCode);

    /**
     * 查询：总数：发起时间
     *
     * @param beginInitiationDate 开始发起时间
     * @param endInitiationDate   结束发起时间
     * @return
     */
    Integer countByInitiationDate(Date beginInitiationDate, Date endInitiationDate);

    /**
     * 查询：总数：三同步整体概况
     *
     * @return
     */
    Map<String, Object> countForBdProject();

    /**
     * 查询：总数：未纳入三同步安全管理
     *
     * @return
     */
    Integer countByWfCodeNqIneedCheckForBdProject();

    /**
     * 查询：总数：做了安全规划，未验收系统数
     *
     * @return
     */
    Integer countByWfCodeNqAcceptForBdProject();

    /**
     * 查询：总数：未做安全规划、投入建设系统数
     *
     * @return
     */
    Integer countByWfCodeEqIneedCheckForBdProject();

    /**
     * 查询：总数：全面纳入三同步，投入运行系统数
     *
     * @return
     */
    Integer countByWfCodeInAllForBdProject();

    /**
     * 查询：列表：三同步部门开展情况
     *
     * @return
     */
    List<Map<String, Object>> listCountByOrgForWf();

    /**
     * 删除：单个：流程实例
     *
     * @param wfInstanceId
     * @return
     */
    @Transactional
    boolean removeByWfInstanceId(Serializable wfInstanceId);

    /**
     * 暂存：单个：流程实例
     *
     * @param wfInstanceId
     * @return
     */
    boolean temporaryStorageByWfInstanceId(Serializable wfInstanceId);
}
