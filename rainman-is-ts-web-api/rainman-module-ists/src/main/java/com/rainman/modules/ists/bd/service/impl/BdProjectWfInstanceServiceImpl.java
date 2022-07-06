package com.rainman.modules.ists.bd.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.rainman.config.RainmanWfProperties;
import com.rainman.modules.ists.bd.dto.BdProjectWfInstanceDto;
import com.rainman.modules.ists.bd.entity.BdProject;
import com.rainman.modules.ists.bd.entity.BdProjectWfInstance;
import com.rainman.modules.ists.bd.mapper.BdProjectWfInstanceMapper;
import com.rainman.modules.ists.bd.service.IBdProjectService;
import com.rainman.modules.ists.bd.service.IBdProjectWfInstanceService;
import com.rainman.modules.ists.wf.entity.WfInstance;
import lombok.SneakyThrows;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.SpringContextUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Description: bd_project_wf_instance
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
@Service
public class BdProjectWfInstanceServiceImpl extends ServiceImpl<BdProjectWfInstanceMapper, BdProjectWfInstance> implements IBdProjectWfInstanceService {
    @Autowired
    private RainmanWfProperties rainmanWfProperties;

    @SneakyThrows
    @Override
    public boolean save(BdProjectWfInstanceDto bdProjectWfInstanceDto) {
        bdProjectWfInstanceDto.getBdProject().setBdProjectId(null);

        BdProject bdProject = SpringContextUtils.getBean(IBdProjectService.class)
                .lambdaQuery()
                .eq(BdProject::getName, bdProjectWfInstanceDto.getBdProject().getName())
                .eq(BdProject::getYear, bdProjectWfInstanceDto.getBdProject().getYear())
                .one();

        if (bdProject != null) {
            if (!"temp".equals(bdProject.getDataStateCode())) {
                throw new Exception("”" + bdProject.getName() + "“与已有项目重复");
            }

            bdProjectWfInstanceDto.getBdProject().setBdProjectId(bdProject.getBdProjectId());
        }

        bdProjectWfInstanceDto.getBdProject().setDataStateCode("temp");

        SpringContextUtils.getBean(IBdProjectService.class).saveOrUpdate(bdProjectWfInstanceDto.getBdProject());

        bdProjectWfInstanceDto.setBdProjectId(bdProjectWfInstanceDto.getBdProject().getBdProjectId());

        BdProjectWfInstance bdProjectWfInstance = this.lambdaQuery()
                .eq(BdProjectWfInstance::getBdProjectId, bdProjectWfInstanceDto.getBdProjectId())
                .eq(BdProjectWfInstance::getWfInstanceId, bdProjectWfInstanceDto.getWfInstanceId())
                .one();

        if (bdProjectWfInstance != null) {
            BeanUtils.copyProperties(bdProjectWfInstance, bdProjectWfInstanceDto);

            return true;
        }

        bdProjectWfInstance = new BdProjectWfInstance();

        BeanUtils.copyProperties(bdProjectWfInstanceDto, bdProjectWfInstance);

        boolean save = this.saveOrUpdate(bdProjectWfInstance);

        bdProjectWfInstanceDto.setBdProjectWfInstanceId(bdProjectWfInstance.getBdProjectWfInstanceId());

        return save;
    }

    @SneakyThrows
    @Override
    public boolean updateById(BdProjectWfInstanceDto bdProjectWfInstanceDto) {
        BdProject bdProject = SpringContextUtils.getBean(IBdProjectService.class)
                .lambdaQuery()
                .eq(BdProject::getName, bdProjectWfInstanceDto.getBdProject().getName())
                .eq(BdProject::getYear, bdProjectWfInstanceDto.getBdProject().getYear())
                .one();

        if (bdProject != null) {
            if (!StringUtils.equals(bdProjectWfInstanceDto.getBdProject().getBdProjectId(), bdProject.getBdProjectId())) {
                if (!"temp".equals(bdProject.getDataStateCode())) {
                    throw new Exception("”" + bdProject.getName() + "“与已有项目重复");
                }

                bdProjectWfInstanceDto.getBdProject().setBdProjectId(bdProject.getBdProjectId());
            }
        }

        bdProjectWfInstanceDto.getBdProject().setDataStateCode("temp");

        SpringContextUtils.getBean(IBdProjectService.class).saveOrUpdate(bdProjectWfInstanceDto.getBdProject());

        bdProjectWfInstanceDto.setBdProjectId(bdProjectWfInstanceDto.getBdProject().getBdProjectId());

        BdProjectWfInstance bdProjectWfInstance = this.lambdaQuery()
                .eq(BdProjectWfInstance::getBdProjectId, bdProjectWfInstanceDto.getBdProject().getBdProjectId())
                .eq(BdProjectWfInstance::getWfInstanceId, bdProjectWfInstanceDto.getWfInstanceId())
                .one();

        if (bdProjectWfInstance != null) {
            if (Objects.equals(bdProjectWfInstanceDto.getBdProjectWfInstanceId(), bdProjectWfInstance.getBdProjectWfInstanceId())) {
                BeanUtils.copyProperties(bdProjectWfInstance, bdProjectWfInstanceDto);

                return true;
            }

            this.removeById(bdProjectWfInstance.getBdProjectWfInstanceId());
        } else {
            bdProjectWfInstance = new BdProjectWfInstance();
        }

        BeanUtils.copyProperties(bdProjectWfInstanceDto, bdProjectWfInstance);

        bdProjectWfInstanceDto.setBdProjectId(bdProjectWfInstanceDto.getBdProject().getBdProjectId());

        return this.saveOrUpdate(bdProjectWfInstance);
    }

    @Override
    public boolean saveOrUpdate(String wfInstanceId, String bdProjectId) {
        List<BdProjectWfInstance> bdProjectWfInstanceList = this.lambdaQuery().eq(BdProjectWfInstance::getWfInstanceId, wfInstanceId)
                .list();

        BdProjectWfInstance bdProjectWfInstance = null;

        List<String> bdProjectWfInstanceIdList = Lists.newArrayList();

        for (BdProjectWfInstance it : bdProjectWfInstanceList) {
            if (StringUtils.equals(bdProjectId, it.getBdProjectId())) {
                bdProjectWfInstance = it;

                continue;
            }

            bdProjectWfInstanceIdList.add(it.getBdProjectWfInstanceId());
        }

        if (!bdProjectWfInstanceIdList.isEmpty()) {
            this.removeByIds(bdProjectWfInstanceIdList);
        }

        if (bdProjectWfInstance != null) {
            return true;
        }

        bdProjectWfInstance = new BdProjectWfInstance();

        bdProjectWfInstance.setWfInstanceId(wfInstanceId);
        bdProjectWfInstance.setBdProjectId(bdProjectId);

        return this.saveOrUpdate(bdProjectWfInstance);
    }

    @Override
    public List<Map<String, Object>> listByWfInstanceId(String wfInstanceId) {
        return this.baseMapper.listByWfInstanceId(wfInstanceId);
    }

    @Override
    public IPage<Map<String, Object>> pageByWfCode(BdProjectWfInstanceDto param) {
        boolean isSetLoginUsername = true;

        if (CollectionUtils.isNotEmpty(rainmanWfProperties.getQueryRoles())) {
            for (String queryRole : rainmanWfProperties.getQueryRoles()) {
                if (SecurityUtils.getSubject().hasRole(queryRole)) {
                    isSetLoginUsername = false;

                    break;
                }
            }
        }

        if (isSetLoginUsername) {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

            param.setLoginUsername(sysUser.getUsername());
        }

        Page<Map<String, Object>> page = new Page<>(param.getPageNo(), param.getPageSize());

        return this.baseMapper.pageByWfCode(page, param);
    }

    @Override
    public IPage<Map<String, Object>> pageForProjectRank(BdProjectWfInstanceDto param) {
        boolean isSetLoginUsername = true;

        if (CollectionUtils.isNotEmpty(rainmanWfProperties.getQueryRoles())) {
            for (String queryRole : rainmanWfProperties.getQueryRoles()) {
                if (SecurityUtils.getSubject().hasRole(queryRole)) {
                    isSetLoginUsername = false;

                    break;
                }
            }
        }

        if (isSetLoginUsername) {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

            param.setLoginUsername(sysUser.getUsername());
        }

        Page<Map<String, Object>> page = new Page<>(param.getPageNo(), param.getPageSize());

        return this.baseMapper.pageForProjectRank(page, param);
    }

    @Override
    public List<WfInstance> listByWfCodeAndBdProjectId(String bdProjectId, String wfCode) {
        return this.baseMapper.listByWfCodeAndBdProjectId(bdProjectId,wfCode);
    }
}
