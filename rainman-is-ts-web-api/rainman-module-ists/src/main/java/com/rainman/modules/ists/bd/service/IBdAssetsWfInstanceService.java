package com.rainman.modules.ists.bd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rainman.modules.ists.bd.dto.BdAssetsWfInstanceDto;
import com.rainman.modules.ists.bd.entity.BdAssets;
import com.rainman.modules.ists.bd.entity.BdAssetsWfInstance;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: bd_assets_wf_instance
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
public interface IBdAssetsWfInstanceService extends IService<BdAssetsWfInstance> {

    /**
     * 查询分页
     * @param param
     * @param pageNo
     * @param pageSize
     * @return
     */
    IPage<BdAssets> pageByDto(BdAssetsWfInstanceDto param, Integer pageNo, Integer pageSize);

    /**
     *  通过id删除两个表中关联数据
     * @param bdAssetId
     * @param bdAssetsWfInstanceId
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteById(String bdAssetId,String bdAssetsWfInstanceId);

    /**
     * 添加
     * @param bdAssetsWfInstance
     * @param bdAssetsWfInstanceDto
     */
    @Transactional(rollbackFor = Exception.class)
    void saveInfo(BdAssetsWfInstance bdAssetsWfInstance,BdAssetsWfInstanceDto bdAssetsWfInstanceDto);
}
