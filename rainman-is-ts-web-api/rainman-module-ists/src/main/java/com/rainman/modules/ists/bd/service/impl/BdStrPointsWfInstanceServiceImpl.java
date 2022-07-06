package com.rainman.modules.ists.bd.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.rainman.modules.ists.bd.dto.BdStrPointsDto;
import com.rainman.modules.ists.bd.dto.BdStrPointsWfInstanceDto;
import com.rainman.modules.ists.bd.entity.BdStrPoints;
import com.rainman.modules.ists.bd.entity.BdStrPointsWfInstance;
import com.rainman.modules.ists.bd.mapper.BdStrPointsMapper;
import com.rainman.modules.ists.bd.mapper.BdStrPointsWfInstanceMapper;
import com.rainman.modules.ists.bd.service.IBdStrPointsService;
import com.rainman.modules.ists.bd.service.IBdStrPointsWfInstanceService;
import org.apache.commons.collections.MapUtils;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * @Description: bd_str_points_wf_instance
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
@Service
public class BdStrPointsWfInstanceServiceImpl extends ServiceImpl<BdStrPointsWfInstanceMapper, BdStrPointsWfInstance> implements IBdStrPointsWfInstanceService {

    @Resource
    private BdStrPointsWfInstanceMapper bdStrPointsWfInstanceMapper;

    @Resource
    private BdStrPointsMapper bdStrPointsMapper;

    @Autowired
    private IBdStrPointsWfInstanceService bdStrPointsWfInstanceService;

    @Autowired
    private IBdStrPointsService bdStrPointsService;


    @Override
    public IPage<BdStrPointsDto> pageByDto(BdStrPointsWfInstanceDto param, Integer pageNo, Integer pageSize) {
        Page<BdStrPointsWfInstanceDto> page = new Page<>(pageNo,pageSize);

        IPage<BdStrPointsWfInstanceDto>dtoPage = bdStrPointsWfInstanceMapper.pageByDto(page,param);

        IPage<BdStrPointsDto> strPage = new Page<>(dtoPage.getCurrent(),dtoPage.getSize(),dtoPage.getTotal());

        strPage.setRecords(Lists.newArrayList());


        //待优化
        dtoPage.getRecords().forEach(item->{
            BdStrPoints points = bdStrPointsMapper.selectById(item.getBdStrPointsId());

            BdStrPointsDto temp = new BdStrPointsDto();

            BeanUtils.copyProperties(points,temp);

            String riskLevelName = SpringContextUtils.getBean(ISysBaseAPI.class)
                    .translateDict("risk_level", temp.getRiskLevelCode());

            temp.setRiskLevelName(riskLevelName);

            strPage.getRecords().add(temp);
        });
        return strPage;
    }

    @Override
    public IPage<BdStrPointsDto> pageByDtoMap(BdStrPointsWfInstanceDto param, Integer pageNo, Integer pageSize) {
        Page<BdStrPointsWfInstanceDto> page = new Page<>(pageNo,pageSize);

        IPage<Map<String,Object>>dtoPage = bdStrPointsWfInstanceMapper.pageByDtoToMap(page,param);

        IPage<BdStrPointsDto> strPage = new Page<>(dtoPage.getCurrent(),dtoPage.getSize(),dtoPage.getTotal());

        strPage.setRecords(Lists.newArrayList());

        dtoPage.getRecords().forEach(map->{

            BdStrPointsWfInstanceDto item = new BdStrPointsWfInstanceDto();

            item.setWfInstanceId(MapUtils.getString(map,"wf_instance_id"));
            item.setBdStrPointsId(MapUtils.getString(map,"bd_str_points_id"));
            item.setBdProjectId(MapUtils.getString(map,"bd_project_id"));
            item.setBdStrPointsWfInstanceId(MapUtils.getString(map,"bd_str_points_wf_instance_id"));

            BdStrPointsDto temp = new BdStrPointsDto();
            temp.setRiskLevelCode(MapUtils.getString(map,"risk_level_code"));

            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            temp.setCreatedDate(DateUtils.str2Date(MapUtils.getString(map,"risk_rectification_begin_date"),simpleDate));
            temp.setLastModifiedDate(DateUtils.str2Date(MapUtils.getString(map,"risk_rectification_end_date"),simpleDate));
            temp.setRiskPoints(MapUtils.getString(map,"risk_points"));
            


            strPage.getRecords().add(item.getBdStrPointsDto());
        });

        return strPage;
    }

    @Override
    public List<BdStrPointsDto> listByDto(BdStrPointsWfInstanceDto param) {

        List<BdStrPointsWfInstanceDto> dtoList = bdStrPointsWfInstanceMapper.listByDto(param);

        List<BdStrPointsDto> strList = Lists.newArrayList();

        //待优化
        dtoList.forEach(item->{
            BdStrPoints points = bdStrPointsMapper.selectById(item.getBdStrPointsId());

            BdStrPointsDto temp = new BdStrPointsDto();

            BeanUtils.copyProperties(points,temp);

            String riskLevelName = SpringContextUtils.getBean(ISysBaseAPI.class)
                    .translateDict("risk_level", temp.getRiskLevelCode());

            temp.setRiskLevelName(riskLevelName);

            strList.add(temp);
        });

        return strList;
    }

    @Override
    public void deleteByIds(String bdStrPointsWfInstanceId, String bdStrPointsId) {

        bdStrPointsService.removeById(bdStrPointsId);

        bdStrPointsWfInstanceService.removeById(bdStrPointsWfInstanceId);
    }

    @Override
    public void saveInfo(BdStrPoints bdStrPoints, BdStrPointsWfInstance bdStrPointsWfInstance) {
        BdStrPoints template = new BdStrPoints();
        BeanUtils.copyProperties(bdStrPoints,template);
        List<BdStrPointsWfInstance> bdStrPointsIdList = bdStrPointsWfInstanceService.lambdaQuery()
                .eq(BdStrPointsWfInstance::getBdProjectId,bdStrPointsWfInstance.getBdProjectId())
                .eq(BdStrPointsWfInstance::getWfInstanceId,bdStrPointsWfInstance.getWfInstanceId()).list();

        bdStrPointsIdList.forEach(item->{
            template.setBdStrPointsId(item.getBdStrPointsId());
            if(bdStrPointsService.lambdaUpdate()
                    .eq(BdStrPoints::getBdStrPointsId,bdStrPoints.getBdStrPointsId())
                    .eq(BdStrPoints::getRiskPoints,bdStrPoints.getRiskPoints())
                    .eq(BdStrPoints::getRiskLevelCode,bdStrPoints.getRiskLevelCode())
                    .eq(BdStrPoints::getRectificationDescription,bdStrPoints.getRectificationDescription())
                    .eq(BdStrPoints::getRectificationProposal,bdStrPoints.getRectificationProposal())
                    .eq(BdStrPoints::getRiskRectificationBeginDate,bdStrPoints.getRiskRectificationBeginDate())
                    .eq(BdStrPoints::getRiskRectificationEndDate,bdStrPoints.getRiskRectificationEndDate())
                    .update(template)){
                return;
            }
        });

        bdStrPointsService.save(bdStrPoints);

        bdStrPointsWfInstanceService.save(bdStrPointsWfInstance);
    }
}
