package com.rainman.modules.ists.bd.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainman.modules.ists.bd.dto.BdProjectDto;
import com.rainman.modules.ists.bd.entity.BdProject;
import com.rainman.modules.ists.bd.mapper.BdProjectMapper;
import com.rainman.modules.ists.bd.service.IBdProjectService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: bd_project
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
@Service
public class BdProjectServiceImpl extends ServiceImpl<BdProjectMapper, BdProject> implements IBdProjectService {
    @Override
    public IPage<BdProjectDto> pageByWfCode(BdProjectDto param) {
        Page<BdProjectDto> page = new Page<>(param.getPageNo(), param.getPageSize());

        return this.baseMapper.pageByWfCode(page, param);
    }

    @Override
    public IPage<BdProjectDto> pageByWfCodeAndWfNodeCode(BdProjectDto param) {
        Page<BdProjectDto> page = new Page<>(param.getPageNo(), param.getPageSize());

        return this.baseMapper.pageByWfCodeAndWfNodeCode(page, param);
    }

    @Override
    public IPage<BdProjectDto> pageByWfCodeAndWfInstanceStateCode(BdProjectDto param) {
        Page<BdProjectDto> page = new Page<>(param.getPageNo(), param.getPageSize());

        return this.baseMapper.pageByWfCodeAndWfInstanceStateCode(page, param);
    }

    @Override
    public Integer countByInitiatorCode(String initiatorCode) {
        return this.lambdaQuery().eq(BdProject::getInitiatorCode, initiatorCode).count();
    }

    @Override
    public List<Map<String, Object>> listBySystemGradingCode() {
        return this.baseMapper.listBySystemGradingCode();
    }

    @Override
    public boolean removeByWfInstanceId(String wfInstanceId) {
        return this.baseMapper.removeByWfInstanceId(wfInstanceId);
    }
}
