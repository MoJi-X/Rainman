package com.rainman.modules.ists.bd.controller;

import com.google.common.base.Charsets;
import com.rainman.modules.ists.bd.entity.BdFile;
import com.rainman.modules.ists.bd.service.IBdFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @Description: 基础数据-文件
 * @Author: jeecg-boot
 * @Date: 2021-11-29
 * @Version: V1.0
 */
@Api(tags = "基础数据-文件")
@RestController
@RequestMapping("/ists.bd/bdFile")
@Slf4j
public class BdFileController extends JeecgController<BdFile, IBdFileService> {
    @Value("${rainman.upload.path:/rainman/data/rainman-is-ts-web-api/upload/}")
    String uploadPath;
    @Autowired
    private IBdFileService bdFileService;

    private String errorText = "调用失败:不存在的文件";

    @SneakyThrows
    @ApiOperation(value = "上传")
    @PostMapping({"/upload"})
    Result<BdFile> upload(@RequestParam("file") MultipartFile file, BdFile bdFile) {
        if (file.isEmpty()) {
            return Result.error("没有上传文件", bdFile);
        }

        if (bdFile == null) {
            return Result.error("基础数据-文件对象不能为空值", bdFile);
        }

        if (StringUtils.isBlank(bdFile.getWfInstanceId())) {
            return Result.error("流程实例标识不能为空值", bdFile);
        }

        if (StringUtils.isBlank(bdFile.getTableName())) {
            return Result.error("表名不能为空值", bdFile);
        }

        if (StringUtils.isBlank(bdFile.getTableId())) {
            return Result.error("表标识不能为空值", bdFile);
        }

        String uploadSuffix = ".pdf,.doc,.docx,.xls,.xlsx,.png,.jpeg,.jpg,.bmp,.txt,.gpg,.pgp,.pdf,.rar,.zip,.gif,.jpeg,.sql,.psd,.svg,.eps,.mm,.wmf,.emf,.asc";

        String filename = file.getOriginalFilename();

        String fileExtension = StringUtils.lowerCase(FilenameUtils.getExtension(filename));

        String fileExtensions = StringUtils.remove(org.apache.commons.lang3.StringUtils.defaultIfBlank(uploadSuffix, ".pdf,.doc,.docx,.xls,.xlsx,.png,.jpeg,.jpg"), ".");

        String[] fileExtensionList = StringUtils.split(fileExtensions, ",");

        if (!ArrayUtils.contains(fileExtensionList, fileExtension)) {
            return Result.error("上传文件类型不支持", bdFile);
        }

        BdFile oldBdFile = null;

        if (StringUtils.isNotBlank(bdFile.getBdFileId())) {
            oldBdFile = bdFileService.getById(bdFile.getBdFileId());
        }

        if (oldBdFile != null) {
            File oldFile = new File(StringUtils.join(new String[]{uploadPath, bdFile.getRelativePath()}, File.separator));

            FileUtils.deleteQuietly(oldFile);
        }

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        bdFile.setFilename(filename);
        bdFile.setStorageType("default");
        bdFile.setRelativePath(StringUtils.join(new String[]{
                sysUser.getUsername(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM/dd")),
                StringUtils.remove(UUID.randomUUID().toString(), "-")
        }, "/"));

        if (StringUtils.isNotEmpty(fileExtension)) {
            bdFile.setRelativePath(bdFile.getRelativePath() + "." + fileExtension);
        }

        File newFile = new File(StringUtils.join(new String[]{uploadPath, bdFile.getRelativePath()}, File.separator));

        FileUtils.forceMkdirParent(newFile);

        FileUtils.copyToFile(file.getInputStream(), newFile);

        bdFileService.saveOrUpdate(bdFile);

        return Result.OK(bdFile);
    }

    @SneakyThrows
    @ApiOperation(value = "下载")
    @GetMapping("/download/{bdFileId}")
    void download(HttpServletResponse response, @PathVariable String bdFileId) {
        response.setContentType("application/force-download");

        String contentDisposition = response.getHeader("content-disposition");

        if (StringUtils.isBlank(bdFileId) || "empty".equals(bdFileId)) {
            if (org.apache.commons.lang3.StringUtils.isBlank(contentDisposition)) {
                response.addHeader("Content-Disposition", "attachment;fileName=error.txt");
            }

            try (OutputStream os = response.getOutputStream()) {
                IOUtils.write(errorText, os, Charsets.UTF_8);
            }

            return;
        }

        BdFile oldBdFile = bdFileService.getById(bdFileId);

        if (oldBdFile == null) {
            if (org.apache.commons.lang3.StringUtils.isBlank(contentDisposition)) {
                response.addHeader("Content-Disposition", "attachment;fileName=error.txt");
            }

            try (OutputStream os = response.getOutputStream()) {
                IOUtils.write(errorText, os, Charsets.UTF_8);
            }

            return;
        }

        File oldFile = new File(StringUtils.join(new String[]{uploadPath, oldBdFile.getRelativePath()}, File.separator));

        if (!oldFile.exists()) {
            if (org.apache.commons.lang3.StringUtils.isBlank(contentDisposition)) {
                response.addHeader("Content-Disposition", "attachment;fileName=error.txt");
            }

            try (OutputStream os = response.getOutputStream()) {
                IOUtils.write(errorText, os, Charsets.UTF_8);
            }

            return;
        }

        if (org.apache.commons.lang3.StringUtils.isBlank(contentDisposition)) {
            response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(oldBdFile.getFilename(), StandardCharsets.UTF_8.name()));
        }

        try (OutputStream os = response.getOutputStream()) {
            FileUtils.copyFile(oldFile, os);

            response.flushBuffer();
        }
    }
}
