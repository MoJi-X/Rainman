package com.rainman.modules.ists.bd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rainman.modules.ists.bd.entity.BdAssets;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: bd_assets
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
public interface IBdAssetsService extends IService<BdAssets> {

    /**
     * 删除
     * @param bdAssetsId
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteById(String bdAssetsId);

    /**
     * 删除
     *
     * @param id
     */
    void removeByWfInstanceId(String id);
}
