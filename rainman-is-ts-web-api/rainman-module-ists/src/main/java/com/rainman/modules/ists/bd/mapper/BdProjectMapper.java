package com.rainman.modules.ists.bd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rainman.modules.ists.bd.dto.BdProjectDto;
import com.rainman.modules.ists.bd.entity.BdProject;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description: bd_project
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
public interface BdProjectMapper extends BaseMapper<BdProject> {
    /**
     * 查询分页
     *
     * @param param
     * @return
     */
    IPage<BdProjectDto> pageByWfCode(IPage<BdProjectDto> page, @Param("param") BdProjectDto param);

    /**
     * 查询分页
     *
     * @param param
     * @return
     */
    IPage<BdProjectDto> pageByWfCodeAndWfNodeCode(IPage<BdProjectDto> page, @Param("param") BdProjectDto param);

    /**
     * 查询分页
     *
     * @param param
     * @return
     */
    IPage<BdProjectDto> pageByWfCodeAndWfInstanceStateCode(IPage<BdProjectDto> page, @Param("param") BdProjectDto param);

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
    boolean removeByWfInstanceId(@Param("id") String wfInstanceId);
}
