package com.rainman.modules.ists.bd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainman.modules.ists.bd.entity.BdAssets;
import com.rainman.modules.ists.bd.entity.BdAssetsWfInstance;
import com.rainman.modules.ists.bd.mapper.BdAssetsMapper;
import com.rainman.modules.ists.bd.service.IBdAssetsService;
import com.rainman.modules.ists.bd.service.IBdAssetsWfInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: bd_assets
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
@Service
public class BdAssetsServiceImpl extends ServiceImpl<BdAssetsMapper, BdAssets> implements IBdAssetsService {

    @Autowired
    private IBdAssetsService bdAssetsService;

    @Autowired
    private IBdAssetsWfInstanceService bdAssetsWfInstanceService;

    @Resource
    private BdAssetsMapper bdAssetsMapper;
    @Override
    public void deleteById(String bdAssetsId) {
        bdAssetsService.removeById(bdAssetsId);
        bdAssetsWfInstanceService.lambdaUpdate()
                .eq(BdAssetsWfInstance::getBdAssetsId, bdAssetsId)
                .remove();
    }

    @Override
    public void removeByWfInstanceId(String id) {
        bdAssetsMapper.removeByWfInstanceId(id);
    }
}
