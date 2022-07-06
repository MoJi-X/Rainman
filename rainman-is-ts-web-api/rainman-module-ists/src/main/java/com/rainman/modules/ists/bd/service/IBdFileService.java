package com.rainman.modules.ists.bd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rainman.modules.ists.bd.dto.BdFileDto;
import com.rainman.modules.ists.bd.entity.BdFile;

import java.util.List;

/**
 * @Description: bd_file
 * @Author: jeecg-boot
 * @Date: 2021-11-29
 * @Version: V1.0
 */
public interface IBdFileService extends IService<BdFile> {
    /**
     * 查询：列表
     *
     * @param wfCode       流程编号
     * @param wfNodeCode   流程节点编号
     * @param wfInstanceId 流程实例标识
     * @param tableName    表名
     * @param tableId      表标识
     * @param inWfCode     流程编号
     * @return
     */
    List<BdFileDto> listByWfCode(String wfCode, String wfNodeCode, String wfInstanceId, String tableName, String tableId, String inWfCode);

    /**
     * 查询：列表
     *
     * @param wfCode       流程编号
     * @param wfInstanceId 流程实例标识
     * @param tableName    表名
     * @param tableId      表标识
     * @return
     */
    default List<BdFileDto> listByWfCode(String wfCode, String wfInstanceId, String tableName, String tableId) {
        return this.listByWfCode(wfCode, null, wfInstanceId, tableName, tableId, null);
    }

    /**
     * 查询：列表
     *
     * @param wfCode       流程编号
     * @param wfInstanceId 流程实例标识
     * @param tableName    表名
     * @param tableId      表标识
     * @param inWfCode     流程编号
     * @return
     */
    default List<BdFileDto> listByWfCode(String wfCode, String wfInstanceId, String tableName, String tableId, String inWfCode) {
        return this.listByWfCode(wfCode, null, wfInstanceId, tableName, tableId, inWfCode);
    }


    /**
     * 删除文件
     *
     * @param wfInstanceId
     */
    void removeByWfInstanceId(String wfInstanceId);
}
