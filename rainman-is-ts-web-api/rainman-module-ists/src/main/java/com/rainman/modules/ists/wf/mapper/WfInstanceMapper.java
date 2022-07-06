package com.rainman.modules.ists.wf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rainman.modules.ists.wf.dto.WfInstanceDto;
import com.rainman.modules.ists.wf.entity.WfInstance;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: wf_instance
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
public interface WfInstanceMapper extends BaseMapper<WfInstance> {
    /**
     * 查询分页
     *
     * @param username
     * @return
     */
    IPage<WfInstanceDto> pageByUsername(IPage<WfInstanceDto> page, @Param("username") String username, @Param("isHistory") String isHistory);

    /**
     * 查询：总数：流程编号
     *
     * @param wfCode         流程编号
     * @param bdFileFlagCode 流程编号
     * @return
     */
    Integer countByWfCodeAndBdFileFlagCode(@Param("wfCode") String wfCode, @Param("bdFileFlagCode") String bdFileFlagCode);

    /**
     * 查询：总数：发起时间
     *
     * @param beginInitiationDate 开始发起时间
     * @param endInitiationDate   结束发起时间
     * @return
     */
    Integer countByInitiationDate(@Param("beginInitiationDate") Date beginInitiationDate, @Param("endInitiationDate") Date endInitiationDate);

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
}
