package com.rainman.modules.ists.bd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rainman.modules.ists.bd.dto.BdAssetsWfInstanceDto;
import com.rainman.modules.ists.bd.dto.BdStrPointsDto;
import com.rainman.modules.ists.bd.dto.BdStrPointsWfInstanceDto;
import com.rainman.modules.ists.bd.entity.BdStrPointsWfInstance;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description: bd_str_points_wf_instance
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
public interface BdStrPointsWfInstanceMapper extends BaseMapper<BdStrPointsWfInstance> {
    /**
     * 列表查询
     * @param bdStrPointsWfInstanceDto
     * @return
     */
    List<BdStrPointsWfInstanceDto> listByDto(@Param("param") BdStrPointsWfInstanceDto bdStrPointsWfInstanceDto);

    /**
     * 分页查询
     * @param page
     * @param bdStrPointsWfInstanceDto
     * @return
     */
    IPage<BdStrPointsWfInstanceDto> pageByDto (Page<BdStrPointsWfInstanceDto> page, @Param("param") BdStrPointsWfInstanceDto bdStrPointsWfInstanceDto);

    /**
     * 分页查询
     * @param page
     * @param bdStrPointsWfInstanceDto
     * @return
     */
    IPage<Map<String,Object>> pageByDtoToMap (Page<BdStrPointsWfInstanceDto> page, @Param("param") BdStrPointsWfInstanceDto bdStrPointsWfInstanceDto);
}
