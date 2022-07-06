package com.rainman.modules.ists.bd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rainman.modules.ists.bd.dto.BdFileDto;
import com.rainman.modules.ists.bd.entity.BdFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: bd_file
 * @Author: jeecg-boot
 * @Date: 2021-11-29
 * @Version: V1.0
 */
public interface BdFileMapper extends BaseMapper<BdFile> {
    /**
     * 查询：列表
     *
     * @param wfCode       流程编号
     * @param wfCode       流程节点编号
     * @param wfInstanceId 流程实例标识
     * @param tableName    表名
     * @param tableId      表标识
     * @param inWfCode     流程编号
     * @return
     */
    List<BdFileDto> listByWfCode(@Param("wfCode") String wfCode,
                                 @Param("wfNodeCode") String wfNodeCode,
                                 @Param("wfInstanceId") String wfInstanceId,
                                 @Param("tableName") String tableName,
                                 @Param("tableId") String tableId,
                                 @Param("inWfCode") String inWfCode);
}
