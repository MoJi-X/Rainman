package com.rainman.modules.ists.bd.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainman.modules.ists.bd.entity.BdFlowSheet;
import com.rainman.modules.ists.bd.mapper.BdFlowSheetMapper;
import com.rainman.modules.ists.bd.service.IBdFlowSheetService;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description: bd_flow_sheet
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
@Service
public class BdFlowSheetServiceImpl extends ServiceImpl<BdFlowSheetMapper, BdFlowSheet> implements IBdFlowSheetService {
    @SneakyThrows
    @Override
    public String build(BdFlowSheet bdFlowSheet) {
        String bdFlowSheetCodePrefix = DateFormatUtils.format(new Date(), "yyyyMMddHHmm");

        LambdaQueryChainWrapper<BdFlowSheet> lambdaQuery = this.lambdaQuery();

        for (int i = 0; i < 100; i++) {
            Integer count = lambdaQuery.likeRight(BdFlowSheet::getBdFlowSheetCode, bdFlowSheetCodePrefix)
                    .eq(BdFlowSheet::getBdFlowSheetTypeCode, bdFlowSheet.getBdFlowSheetTypeCode())
                    .count();

            String bdFlowSheetCodeSuffix = StringUtils.leftPad((++count).toString(), 3, "0");

            bdFlowSheet.setStateCode("use");
            bdFlowSheet.setBdFlowSheetCode(bdFlowSheetCodePrefix + bdFlowSheetCodeSuffix);

            try {
                this.save(bdFlowSheet);

                return bdFlowSheet.getBdFlowSheetCode();
            } catch (Exception e) {
                e.printStackTrace();

                continue;
            }
        }

        throw new Exception("生成流水单号出错");
    }
}
