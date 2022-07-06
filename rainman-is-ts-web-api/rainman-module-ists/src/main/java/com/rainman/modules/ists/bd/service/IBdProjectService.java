package com.rainman.modules.ists.bd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rainman.modules.ists.bd.dto.BdProjectDto;
import com.rainman.modules.ists.bd.entity.BdProject;

import java.util.List;
import java.util.Map;

/**
 * @Description: bd_project
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
public interface IBdProjectService extends IService<BdProject> {
    /**
     * 查询分页
     *
     * @param param
     * @return
     */
    IPage<BdProjectDto> pageByWfCode(BdProjectDto param);

    /**
     * 查询分页
     *
     * @param param
     * @return
     */
    IPage<BdProjectDto> pageByWfCodeAndWfNodeCode(BdProjectDto param);

    /**
     * 查询分页
     *
     * @param param
     * @return
     */
    IPage<BdProjectDto> pageByWfCodeAndWfInstanceStateCode(BdProjectDto param);

    /**
     * 查询：总数：发起人编号
     *
     * @param initiatorCode 发起人编号
     * @return
     */
    Integer countByInitiatorCode(String initiatorCode);

    /**
     * @return
     */
    List<Map<String, Object>> listBySystemGradingCode();

    /**
     * 删除：项目与流程实例的关联对应的多个临时状态的项目
     *
     * @param wfInstanceId
     * @return
     */
    boolean removeByWfInstanceId(String wfInstanceId);
}
