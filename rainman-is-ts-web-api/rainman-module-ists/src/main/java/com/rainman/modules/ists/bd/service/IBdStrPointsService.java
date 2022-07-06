package com.rainman.modules.ists.bd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rainman.modules.ists.bd.entity.BdStrPoints;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: bd_str_points
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
public interface IBdStrPointsService extends IService<BdStrPoints> {

    /**
     * 删除
     * @param bdStrPointsId
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteById(String bdStrPointsId);

    /**
     * 删除
     *
     * @param id
     */
    void removeByWfInstanceId(String id);
}
