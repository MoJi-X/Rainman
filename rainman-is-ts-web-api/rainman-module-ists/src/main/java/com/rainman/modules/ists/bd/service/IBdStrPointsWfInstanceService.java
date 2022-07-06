package com.rainman.modules.ists.bd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rainman.modules.ists.bd.dto.BdAssetsWfInstanceDto;
import com.rainman.modules.ists.bd.dto.BdStrPointsDto;
import com.rainman.modules.ists.bd.dto.BdStrPointsWfInstanceDto;
import com.rainman.modules.ists.bd.entity.BdAssets;
import com.rainman.modules.ists.bd.entity.BdStrPoints;
import com.rainman.modules.ists.bd.entity.BdStrPointsWfInstance;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: bd_str_points_wf_instance
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
public interface IBdStrPointsWfInstanceService extends IService<BdStrPointsWfInstance> {

    /**
     * 分页查询
     * @param param
     * @param pageNo
     * @param pageSize
     * @return
     */
    IPage<BdStrPointsDto> pageByDto(BdStrPointsWfInstanceDto param, Integer pageNo, Integer pageSize);

    /**
     * 分页查询
     * @param param
     * @param pageNo
     * @param pageSize
     * @return
     */
    IPage<BdStrPointsDto> pageByDtoMap(BdStrPointsWfInstanceDto param, Integer pageNo, Integer pageSize);
    /**
     * 列表查询
     * @param param
     * @return
     */
    List<BdStrPointsDto> listByDto(BdStrPointsWfInstanceDto param);

    /**
     * 删除
     * @param bdStrPointsWfInstanceId
     * @param bdStrPointsId
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteByIds(String bdStrPointsWfInstanceId,String bdStrPointsId);

    /**
     * 增加 或 修改
     * @param bdStrPoints
     * @param bdStrPointsWfInstance
     */
    @Transactional(rollbackFor = Exception.class)
    void saveInfo(BdStrPoints bdStrPoints,BdStrPointsWfInstance bdStrPointsWfInstance);
}
