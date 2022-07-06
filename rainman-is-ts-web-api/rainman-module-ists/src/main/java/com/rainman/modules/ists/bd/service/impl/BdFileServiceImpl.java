package com.rainman.modules.ists.bd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainman.modules.ists.bd.dto.BdFileDto;
import com.rainman.modules.ists.bd.entity.BdFile;
import com.rainman.modules.ists.bd.mapper.BdFileMapper;
import com.rainman.modules.ists.bd.service.IBdFileService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * @Description: bd_file
 * @Author: jeecg-boot
 * @Date: 2021-11-29
 * @Version: V1.0
 */
@Service
public class BdFileServiceImpl extends ServiceImpl<BdFileMapper, BdFile> implements IBdFileService {
    @Value("${rainman.upload.path:/rainman/data/rainman-is-ts-web-api/upload/}")
    String uploadPath;
    @Autowired
    private IBdFileService bdFileService;
    @Override
    public List<BdFileDto> listByWfCode(String wfCode, String wfNodeCode, String wfInstanceId, String tableName, String tableId, String inWfCode) {
        return this.baseMapper.listByWfCode(wfCode, wfNodeCode, wfInstanceId, tableName, tableId, inWfCode);
    }


    @Override
    public void removeByWfInstanceId(String wfInstanceId) {
        this.lambdaQuery()
                .eq(BdFile::getWfInstanceId, wfInstanceId)
                .list()
                .forEach(item -> {
                    File oldFile = new File(StringUtils.join(new String[]{uploadPath, item.getRelativePath()}, File.separator));

                    FileUtils.deleteQuietly(oldFile);
                });

        this.lambdaUpdate().eq(BdFile::getWfInstanceId, wfInstanceId).remove();
    }
}
