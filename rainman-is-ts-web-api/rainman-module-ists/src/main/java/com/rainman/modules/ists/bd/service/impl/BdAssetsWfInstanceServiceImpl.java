package com.rainman.modules.ists.bd.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.rainman.modules.ists.bd.dto.BdAssetsWfInstanceDto;
import com.rainman.modules.ists.bd.entity.BdAssets;
import com.rainman.modules.ists.bd.entity.BdAssetsWfInstance;
import com.rainman.modules.ists.bd.mapper.BdAssetsMapper;
import com.rainman.modules.ists.bd.mapper.BdAssetsWfInstanceMapper;
import com.rainman.modules.ists.bd.service.IBdAssetsService;
import com.rainman.modules.ists.bd.service.IBdAssetsWfInstanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @Description: bd_assets_wf_instance
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
@Service
public class BdAssetsWfInstanceServiceImpl extends ServiceImpl<BdAssetsWfInstanceMapper, BdAssetsWfInstance> implements IBdAssetsWfInstanceService {

    @Resource
    private BdAssetsWfInstanceMapper bdAssetsWfInstanceMapper;

    @Resource
    private BdAssetsMapper  bdAssetsMapper;

    @Autowired
    private IBdAssetsService bdAssetsService;

    @Autowired
    private IBdAssetsWfInstanceService bdAssetsWfInstanceService;


    @Override
    public IPage<BdAssets> pageByDto(BdAssetsWfInstanceDto param, Integer pageNo, Integer pageSize) {

        Page<BdAssetsWfInstanceDto> page = new Page<>(pageNo,pageSize);

        IPage<BdAssetsWfInstanceDto> dtoPage=bdAssetsWfInstanceMapper.pageByDto(page,param);

        IPage<BdAssets> bdAssetsPage = new Page<>(dtoPage.getCurrent(),dtoPage.getSize(), dtoPage.getTotal());

        bdAssetsPage.setRecords(Lists.newArrayList());
        //待优化
        for(BdAssetsWfInstanceDto dto:dtoPage.getRecords()){
            BdAssets temp = bdAssetsMapper.selectById(dto.getBdAssetsId());
            bdAssetsPage.getRecords().add(temp);
        }


        return bdAssetsPage;
    }

    @Override
    public void deleteById(String bdAssetId, String bdAssetsWfInstanceId) {
        bdAssetsService.removeById(bdAssetId);
        bdAssetsWfInstanceService.removeById(bdAssetsWfInstanceId);
    }

    @Override
    public void saveInfo(BdAssetsWfInstance bdAssetsWfInstance, BdAssetsWfInstanceDto bdAssetsWfInstanceDto) {
        bdAssetsWfInstanceService.save(bdAssetsWfInstance);
        bdAssetsService.save(bdAssetsWfInstanceDto.getBdAssets());
    }
}
