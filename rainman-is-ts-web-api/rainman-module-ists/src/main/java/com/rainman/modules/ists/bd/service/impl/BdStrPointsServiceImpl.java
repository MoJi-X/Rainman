package com.rainman.modules.ists.bd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainman.modules.ists.bd.entity.BdStrPoints;
import com.rainman.modules.ists.bd.entity.BdStrPointsWfInstance;
import com.rainman.modules.ists.bd.mapper.BdStrPointsMapper;
import com.rainman.modules.ists.bd.service.IBdStrPointsService;
import com.rainman.modules.ists.bd.service.IBdStrPointsWfInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: bd_str_points
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
@Service
public class BdStrPointsServiceImpl extends ServiceImpl<BdStrPointsMapper, BdStrPoints> implements IBdStrPointsService {
    @Autowired
    private IBdStrPointsService bdStrPointsService;

    @Autowired
    private IBdStrPointsWfInstanceService bdStrPointsWfInstanceService;

    @Resource
    private BdStrPointsMapper bdStrPointsMapper;

    @Override
    public void deleteById(String bdStrPointsId) {

        bdStrPointsService.removeById(bdStrPointsId);

        bdStrPointsWfInstanceService.lambdaUpdate()
                .eq(BdStrPointsWfInstance::getBdStrPointsId,bdStrPointsId)
                .remove();
    }

    @Override
    public void removeByWfInstanceId(String id) {
        bdStrPointsMapper.removeByWfInstanceId(id);
    }
}
