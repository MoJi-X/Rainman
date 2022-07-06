package com.rainman.modules.ists.bd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rainman.modules.ists.bd.dto.BdProjectWfInstanceDto;
import com.rainman.modules.ists.bd.entity.BdProjectWfInstance;
import com.rainman.modules.ists.wf.entity.WfInstance;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description: bd_project_wf_instance
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
public interface BdProjectWfInstanceMapper extends BaseMapper<BdProjectWfInstance> {
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
    IPage<Map<String, Object>> pageByWfCode(IPage<Map<String, Object>> page, @Param("param") BdProjectWfInstanceDto param);

    /**
     * 查询分页
     *
     * @param param
     * @return
     */
    IPage<Map<String, Object>> pageForProjectRank(IPage<Map<String, Object>> page, @Param("param") BdProjectWfInstanceDto param);

    /**
     * 查询
     * @return
     */
    List<WfInstance> listByWfCodeAndBdProjectId(@Param("bdProjectId") String bdProjectId, @Param("wfCode") String wfCode);
}
