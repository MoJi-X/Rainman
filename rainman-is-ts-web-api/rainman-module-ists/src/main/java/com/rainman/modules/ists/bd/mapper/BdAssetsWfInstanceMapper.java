package com.rainman.modules.ists.bd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rainman.modules.ists.bd.dto.BdAssetsWfInstanceDto;
import com.rainman.modules.ists.bd.entity.BdAssetsWfInstance;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @Description: bd_assets_wf_instance
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
public interface BdAssetsWfInstanceMapper extends BaseMapper<BdAssetsWfInstance> {
    /**
     * 分页查询
     * @param page
     * @param bdAssetsWfInstanceDto
     * @return
     */
    IPage<BdAssetsWfInstanceDto> pageByDto (Page<BdAssetsWfInstanceDto> page,@Param("param") BdAssetsWfInstanceDto bdAssetsWfInstanceDto);

}
