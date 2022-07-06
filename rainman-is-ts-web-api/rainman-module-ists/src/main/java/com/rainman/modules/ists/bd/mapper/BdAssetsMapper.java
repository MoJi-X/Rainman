package com.rainman.modules.ists.bd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rainman.modules.ists.bd.entity.BdAssets;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: bd_assets
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
public interface BdAssetsMapper extends BaseMapper<BdAssets> {
    /**
     * 删除：项目与流程实例的关联对应的多个临时状态的项目
     *
     * @param id wfInstanceID
     */
    void removeByWfInstanceId(@Param("id") String id);
}
