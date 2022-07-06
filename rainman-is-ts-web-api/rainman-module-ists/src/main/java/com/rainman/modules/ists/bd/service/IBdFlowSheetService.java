package com.rainman.modules.ists.bd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rainman.modules.ists.bd.entity.BdFlowSheet;

/**
 * @Description: bd_flow_sheet
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
public interface IBdFlowSheetService extends IService<BdFlowSheet> {
    /**
     * 生成流水单号
     *
     * @param bdFlowSheet 基础数据-流水单
     * @return
     */
    String build(BdFlowSheet bdFlowSheet);
}
