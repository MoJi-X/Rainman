package com.rainman.modules.ists.bd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rainman.modules.ists.bd.dto.BdProjectWfInstanceDto;
import com.rainman.modules.ists.bd.entity.BdProjectWfInstance;
import com.rainman.modules.ists.wf.entity.WfInstance;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Description: bd_project_wf_instance
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
public interface IBdProjectWfInstanceService extends IService<BdProjectWfInstance> {
    /**
     * 保存
     *
     * @param bdProjectWfInstanceDto
     * @return
     */
    @Transactional
    boolean save(BdProjectWfInstanceDto bdProjectWfInstanceDto);

    /**
     * 保存
     *
     * @param wfInstanceId
     * @param bdProjectId
     * @return
     */
    @Transactional
    boolean saveOrUpdate(String wfInstanceId, String bdProjectId);

    /**
     * 更新
     *
     * @param bdProjectWfInstanceDto
     * @return
     */
    @Transactional
    boolean updateById(BdProjectWfInstanceDto bdProjectWfInstanceDto);

    /**
     * 查询列表
     *
     * @param wfInstanceId 流程-实例标识
     * @return
     */
    List<Map<String, Object>> listByWfInstanceId(@Param("wfInstanceId") String wfInstanceId);

    /**
     * 查询分页
     *
     * @param param
     * @return
     */
    IPage<Map<String, Object>> pageByWfCode(@Param("param") BdProjectWfInstanceDto param);

    /**
     * 查询分页
     *
     * @param param
     * @return
     */
    IPage<Map<String, Object>> pageForProjectRank(@Param("param") BdProjectWfInstanceDto param);

    /**
     * 流程实例查询
     * @param bdProjectId
     * @param wfCode
     * @return
     */
    List<WfInstance> listByWfCodeAndBdProjectId(String bdProjectId, String wfCode);
}
